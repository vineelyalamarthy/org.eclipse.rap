/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.engine;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import com.w4t.engine.classloader.DelegateClassLoader;
import com.w4t.engine.util.*;
import com.w4t.util.*;


/** <p>The Delegate servlet of the W4T plattform handles HTTP requests and 
  * provides the W4TModel with the corresponding HTTP session.</p>
  *
  * <p>It uses a reference to the W4TModel, which is placed in the session,
  * to create the page content. The W4TModel is initialized by the 
  * Delegate.</p>
  */
public class W4TDelegate extends HttpServlet {

  ///////////////////////
  // constant definitions
  
  private static final long serialVersionUID = 1L;
  /** <p>name of the attribute used to store an URL array 
   *  with additional classpath entries in the ServletContext.</p> */
  public final static String ADDITIONAL_CLASSPATH = "w4t_add_classpath";

  /////////
  // fields
  
  private DelegateClassLoader loader = null;
  private HttpServlet core = null;
  private ThreadLocal requestWorker = new ThreadLocal();
  private ThreadLocal requestLock = new ThreadLocal();
  
  /** Initialize global variables */
  public void init( final ServletConfig config ) throws ServletException {  
    super.init( config );
    IEngineConfig engineConfig = getEngineConfig();
    try {
      ConfigurationReader.setEngineConfig( engineConfig );
      if( loader == null ) {
        ClassLoader parentLoader = this.getClass().getClassLoader();
        URL[] urls = getURLList( engineConfig );
        ServletContext context = getServletContext();
        URL[] additionals 
          = ( URL[] )context.getAttribute( ADDITIONAL_CLASSPATH );
        urls = addURLs( urls, additionals );
        loader = new DelegateClassLoader( urls, parentLoader, false );
      }
      Class coreClass = loader.loadClass( "com.w4t.engine.W4TDelegateCore" );
      core = ( HttpServlet )coreClass.newInstance();
      core.init( config );      
    } catch( Exception e ) {
      throw new ServletException( e );
    }
  }

  /** <p>do the cleanup.</p> */
  public void destroy() {
    core.destroy();
    core = null;
    loader = null;
    super.destroy();
  }
  
  /**
    * Process the HTTP Post request
    * @param request encapsulates the information of a http request
    * from a client browser
    * @param response encapsulates the information of a http response
    * to a client browser
    * @throw IOException error accessing the response output stream
    * @throw ServletException
    */
  public void doPost( final HttpServletRequest request,
                      final HttpServletResponse response )
    throws ServletException, IOException {
    // run each request handling in own thread to avoid memory leaks
    // because of threadlocal pattern used in external libraries
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IInitialization initialization = configuration.getInitialization();
    if( initialization.isUseRequestWorker() ) {
      runRequestWorker( createRequestWorker( request, response ) );
    } else {
      core.service( request, response );
    }
  }

  /**
    * Process the http get request, which is used for the first access to a
    * web application
    * @param request encapsulates the information of a http request
    * from a client browser
    * @param response encapsulates the information of a http response
    * to a client browser
    * @throw IOException error accessing the response output stream
    * @throw ServletException
    */
  public void doGet( final HttpServletRequest request,
                     final HttpServletResponse response )
    throws ServletException, IOException {
    doPost( request, response );
  }

  public String getServletInfo() {
    return "com.w4t.engine.W4TDelegate Information";
  }
  

  // helping methods
  //////////////////
  
  private IRequestWorker createRequestWorker( final HttpServletRequest request, 
                                              final HttpServletResponse resp ) {
    return new IRequestWorker() {
      IOException ioException = null;
      ServletException servletException;
      Throwable throwable;
      
      public void run() {
        try {
          core.service( request, resp );
        } catch( ServletException we ) {
          servletException = we;
        } catch (IOException ioe ) {
          ioException = ioe;
        } catch( Throwable thr ) {
          throwable = thr;
        } finally {
        }
      }
      
      public void reThrow() throws IOException, ServletException {
        if( ioException != null ) {
          throw ioException;
        }
        if( servletException != null ) {
          throw servletException;
        }
        if( throwable != null ) {
          if( throwable instanceof Error ) {
            Error error = ( Error )throwable;
            throw error;
          }
          throwable.printStackTrace();
          String msg =   "An unexpected error has occured:\n"
                       + throwable.getClass(). getName()
                       + " " 
                       + throwable.getMessage() 
                       + "\nFor more information see application log.";
          throw new ServletException( msg );
        }
      }
    };
  }

  
  private void runRequestWorker( final IRequestWorker worker ) 
    throws IOException, ServletException 
  {
    initLock();
    synchronized( requestLock.get() ) {
      // get buffered thread for request handling
      RequestWorker requestWorkerThread = retrieveWorkerThread();
      requestWorkerThread.setWorker( worker );
      requestWorkerThread.setParent( Thread.currentThread() );
      try {
        // sleep till worker thread has finished
        requestLock.get().notify();
        requestLock.get().wait();
      } catch( InterruptedException trigger ) {
      } finally {
        // buffer new Thread for next request
        requestWorker.set( retrieveWorkerThread() );
        worker.reThrow();
      }
    }
  }

  private void initLock() {
    if( requestLock.get() == null ) {
      requestLock.set( new Object() );
    }
  }

  private RequestWorker retrieveWorkerThread() {
    RequestWorker result = ( RequestWorker )requestWorker.get();
    if( result == null ) {
      result = new RequestWorker();
      result.setLock( requestLock.get() );
      result.start();
    }
    requestWorker.set( null );
    return result;
  }
  
  
  private IEngineConfig getEngineConfig() {
    String name = IEngineConfig.class.getName();
    ServletContext sc = getServletContext();
    IEngineConfig result = ( IEngineConfig )sc.getAttribute( name );
    if( result == null ) {
      result = new EngineConfig( sc.getRealPath( "/" ) );
      sc.setAttribute( name, result );
    }
    return result;
  }
  
  private URL[] getURLList( final IEngineConfig config ) {
    List appURLs = WebAppURLs.getWebAppURLs( config );
    URL[] result = new URL[ appURLs.size() ];  
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = ( URL )appURLs.get( i );
    }
    return result;
  }
  
  private static URL[] addURLs( final URL[] urls, final URL[] urlsToAdd ) {    
    URL[] result = urls;
    if( urlsToAdd != null ) {
      result = new URL[ urls.length + urlsToAdd.length ];
      System.arraycopy( urls, 0, result, 0, urls.length );
      System.arraycopy( urlsToAdd, 0, result, urls.length, urlsToAdd.length );
    }
    return result;    
  }
  
  
  ////////////////
  // inner classes

  private interface IRequestWorker extends Runnable {
    /** <p>rethrows buffered exceptions which occured during the
     *  run method of this Runnable</p> */ 
    void reThrow() throws IOException, ServletException;
  }
  
  private class RequestWorker extends Thread {
    private IRequestWorker worker;
    private Object lock;
    private Thread parent;
    
    private RequestWorker() {
      super( "RequestWorker[" + Thread.currentThread().getName() + "]" );
      setDaemon( true );  
    }

    public void setParent( final Thread parent ) {
      this.parent = parent;
    }

    public void setLock( final Object lock ) {
      this.lock = lock;
    }

    void setWorker( final IRequestWorker worker ) {
      this.worker = worker;
    }
    
    public void run() {
      synchronized( lock ) {
        try {
          if( worker == null ) {  
            lock.notify();
            lock.wait();
          }
        } catch( InterruptedException trigger ) {
        } finally {
          worker.run();
          parent.interrupt();
        }
      }
    }    
  }
}