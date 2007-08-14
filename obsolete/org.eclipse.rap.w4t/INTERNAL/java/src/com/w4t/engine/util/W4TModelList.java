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
package com.w4t.engine.util;

import java.lang.ref.WeakReference;
import java.util.*;

import javax.servlet.http.*;

import org.eclipse.rwt.internal.*;

import com.w4t.WebComponentStatistics;
import com.w4t.engine.W4TModel;

/** <p>encapsulates a list of W4TModel objects. List entries are stored 
  * with weak references, so the references to the list members that are
  * kept here do not prevent the garbage collector to clean them away.</p>
  * 
  * <p>Implementation as singleton.</p>
  */
public final class W4TModelList {

  private static int unboundCount = 0;  
  private static boolean unboundThreadRunning = false;
  private static W4TModelList _instance;
  
  /** the internal data structure for the list. */
  private final Hashtable list;
  
  /** Creates the singleton instance of W4TModelList */
  private W4TModelList() {
    list = new Hashtable();
  }
  
  public synchronized static W4TModelList getInstance() {
    if( _instance == null ) {
      _instance = new W4TModelList();
    }
    return _instance;
  }
  
  
  // list handling
  ////////////////
  
  /** adds the specified W4TModel to the list, using the specified id. 
    * We use the session id for this purpose. */
  public void add( final HttpSession session, final W4TModel model ) {
    // put model under its respective sessionId into list
    WeakReference modelWeakRef = new WeakReference( model );    
    list.put( session.getId(), modelWeakRef );
    // register a listener to remove model if its session is invalidated
    HttpSessionBindingListener listener = new HttpSessionBindingListener() {
      private String sessionId;
      public void valueBound( final HttpSessionBindingEvent event ) {
        sessionId = event.getSession().getId();
      }
      public void valueUnbound( final HttpSessionBindingEvent event ) {
        W4TModelList.getInstance().remove( sessionId );
        W4TModelList.getInstance().forceGC();
      }
    };
    session.setAttribute( "HttpSessionBindingListener", listener );
  }
  
  public W4TModel get( final String id ) {
    WeakReference modelWeakRef = ( WeakReference )list.get( id );
    W4TModel result = null;
    if( modelWeakRef != null ) {
      result = ( W4TModel )modelWeakRef.get();
    }
    return result;
  }
    
  /** removes the W4TModel that is stored with the specified id from the 
    * list. We use the session id for this purpose. */
  public void remove( final String id ) {
    WeakReference modelWeakRef = ( WeakReference )list.remove( id );
    Object obj = modelWeakRef.get();
    if( obj != null ) {
// TODO: Think about, if this is still necessary
//      W4TModel model = ( W4TModel )obj;
//      DelegateClassLoader dcl = model.getClassLoader();
//      dcl.cleanup();
    }
  }  
  
  /** returns a WebComponentStatistics with information about the components,
    * uptime etc. of all models in the list. */
  public WebComponentStatistics getStatistics() {
    WebComponentStatistics result = null;
    Enumeration en = list.elements();
    while( en.hasMoreElements() ) {
      WeakReference modelWeakRef = ( WeakReference )en.nextElement();
      Object obj = modelWeakRef.get();
      if( obj != null ) {
        W4TModel model = ( W4TModel )obj;
        WebComponentStatistics statistics = model.getStatistics();
        if( statistics != null ) {
          result = addUpStatistics( result, statistics );
        }
      }     
    }
    if( result != null ) {
      result.setSessionCount( getListSize() );
    }
    return result;
  }
  
  /** returns an array with the currently added W4TModel instances to this
   *  W4TModelList. */
  public W4TModel[] getList() {
    List buffer = new Vector();
    synchronized ( list ) {
      Enumeration en = list.elements();      
      while( en.hasMoreElements() ) {
        WeakReference modelWeakRef = ( WeakReference )en.nextElement();
        Object obj = modelWeakRef.get();
        if( obj != null ) {
          buffer.add( obj );
        }     
      }
    }
    W4TModel[] result = new W4TModel[ buffer.size() ];
    buffer.toArray( result );
    return result;
  }
  
  public void cleanup() {
    W4TModel[] list = getList();
    for( int i = 0; i < list.length; i++ ) {
      list[ i ].cleanup();
    }
  }
  
  private void forceGC() {
    // determine the maximum amount of sessions which could be released
    // until the garbage collection will be forced
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IInitialization initialization = configuration.getInitialization();
    long maxSessionUnboundToForceGC
      = initialization.getMaxSessionUnboundToForceGC();
    if( maxSessionUnboundToForceGC > 0 ) {
      if( !unboundThreadRunning ) {
        unboundCount++;
      }
      if( getUnboundCount() >= maxSessionUnboundToForceGC ) {      
        unboundThreadRunning = true;
        unboundCount = 0;
        Thread forceGC = new Thread() {
          public void run() {

  System.out.println( "\n***************************\nforceGC - step 1\n" );
            System.gc();          

            synchronized( this ) {
              try {
                this.wait( 100 );
              } catch( Exception shouldNotHappen ) {
                System.out.println( shouldNotHappen );
              }
            }

  System.out.println( "\n***************************\nforceGC - step 2\n" );
            System.gc(); 
            unboundThreadRunning = false;
          }  
        };
        forceGC.start();
      }    
    }
  }
  
  synchronized void setUnboundCount( final int unboundCount ) {
    W4TModelList.unboundCount = unboundCount;
  }
 
  int getUnboundCount() {
    return unboundCount;
  }  


  // helping methods
  //////////////////
  
  /** adds all statistics found in statsToAdd to the corresponding statistics 
    * elements in stats (which contains then the sum of both passed 
    * WebComponentStatistics objects). */
  private WebComponentStatistics addUpStatistics( 
    final WebComponentStatistics stats, 
    final WebComponentStatistics statsToAdd ) 
  {
    WebComponentStatistics result = stats;
    if( stats == null ) {
      result = statsToAdd;
    } else {
      stats.integrate( statsToAdd );
    }
    return result;
  }
  
  private int getListSize() {
    return list.values().size();
  }
}