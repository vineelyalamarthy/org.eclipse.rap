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
package com.w4t.util;

import java.lang.ref.WeakReference;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.rwt.internal.*;
import org.eclipse.rwt.internal.service.*;

import com.w4t.*;
import com.w4t.event.WebFormEvent;
import com.w4t.internal.adaptable.IFormAdapter;


/** 
 * <p>This is a helping class for the WebComponentApplication to keep access
 * to a single WebComponent by hashing the component with its ID as key.</p>
 */
public class WebComponentRegistry extends SessionSingletonBase {

  /** the time at which this WebComponentRegistry was created */
  private final long creationTime;
  private final Map registry;
  private final HttpSession session;

  private WebComponentRegistry() {
    this.registry = new WeakHashMap();
    this.creationTime = System.currentTimeMillis();    
    this.session = ContextProvider.getRequest().getSession();
  }

  /**
   * Returns the session singletion instance.
   */
  public static WebComponentRegistry getInstance() {
    return ( WebComponentRegistry )getInstance( WebComponentRegistry.class );
  }

  /**
    * adds a webComponent to the registry
    * @webComponent WebComponent to add
    */
  public synchronized void add( final WebComponent webComponent ) {
    registry.put( webComponent, new WeakReference( webComponent ) );
  }

  /** <p>returns a Statistics object with registry statistics
    * information: how many components of which types are contained in
    * this WebComponentRegistry, how much memory is used etc.</p>
    * + writes out the list to console (temp only)
    */
  public WebComponentStatistics getStatistics() {
    WebComponentStatistics result = new WebComponentStatistics();
    long statsStartTime = System.currentTimeMillis();
    Hashtable stats = new Hashtable();

    int regSize = countRegisteredComponents( stats );
    
    // provide the WebComponentStatistics with the collected info:
    // component counting
    result.applyComponentCounts( stats );
    result.setComponentCountAltogether( regSize );
    // runtime statistics
    Runtime rt = Runtime.getRuntime();
    result.setOccupiedMemoryKB( ( rt.totalMemory() - rt.freeMemory() ) / 1024 );
    result.setApplicationUptime(   System.currentTimeMillis()
                                 - this.creationTime );
    // the last thing to do is to say how long doing statistics took itself
    result.setStatisticsTime( System.currentTimeMillis() - statsStartTime );

    return result;
  }

  /** Loops over the whole registry and count how many instances of the
    * various types of webComponents are contained in it. */
  private int countRegisteredComponents( final Hashtable stats ) {
    int result = 0;
    Object[] componentRefs = registry.values().toArray();
    for( int i = 0; i < componentRefs.length; i++ ) {
      Object content = ( ( WeakReference )componentRefs[ i ] ).get();
      if( content != null ) {
        WebComponent component = ( WebComponent )content;
        String className = component.getClass().getName();
        int count = 1;
        if( stats.containsKey( className ) ) {
          count = ( ( Integer )stats.get( className ) ).intValue();
          count++;
        }
        result++;
        stats.put( className, new Integer( count ) );
      }
    }
    return result;
  }  

  /** Free the registry from expired WebComponents, and throw the closing
   *  event on closed WebForms */
  public synchronized void cleanup() {
    List closingList = new ArrayList();
    Object[] componentRefs = registry.values().toArray();
    for( int i = 0; i < componentRefs.length; i++ ) {
      Object content = ( ( WeakReference )componentRefs[ i ] ).get();
      if( content != null ) {
        WebComponent component = ( WebComponent )content;
        if( component instanceof WebForm ) {
          WebForm wf = ( WebForm )component;
          long closingTimeout = getClosingTimout();
          if( wf.getClosingTimeout() != -1 ) {
            closingTimeout = wf.getClosingTimeout();
          }
          long now = System.currentTimeMillis();
          IFormAdapter adapter
            = ( IFormAdapter )wf.getAdapter( IFormAdapter.class );
          long timeStamp = adapter.getTimeStamp();
          boolean active = WebComponentControl.isActive( wf );
          if( active && ( now - timeStamp ) > closingTimeout ) {
            closingList.add( wf );
          }
        }
      }
    }
    int toClose = closingList.size();
    for( int i = 0; i < toClose; i++ ) {
      WebForm wf = ( WebForm )closingList.get( i );
      closeForm( wf );
    }
    closingList.clear();
    notifyAll();
  }

  private long getClosingTimout() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IInitialization initialization = configuration.getInitialization();
    long closingTimeout = initialization.getClosingTimeout();
    return closingTimeout;
  }

  private void closeForm( final WebForm form ) {
    WebComponentControl.setActive( form, false );
    Request request = new Request( session );
    Response response = new Response();
    ContextProvider.setContext( new ServiceContext( request, response ) );
    ContextProvider.getContext().setStateInfo( new ServiceStateInfo() );
    try {
      int webEventType = WebFormEvent.WEBFORM_CLOSING;
      WebFormEvent webFormEvent = new WebFormEvent( form, webEventType );
      webFormEvent.processEvent();
    } catch( final Exception e ) {
      if( form != null ) {
        System.out.println( "Exception in registry cleanup call \nduring "
                          + "process closing Event of WebForm '"
                          + form.getClass().getName() + " "
                          + form.getName() + "]: \n"
                          + e.toString() );
      } else {
        System.out.println( "Exception in registry cleanup call \nduring "
                          + "process closing Event of a WebForm \n'"
                          + e.toString() );
      }
      e.printStackTrace();
    } finally {
      ContextProvider.disposeContext();
    }
  }

  /** returns the time at which this WebComponentRegistry was created */
  long getCreationTime() {
    return creationTime;
  }
}