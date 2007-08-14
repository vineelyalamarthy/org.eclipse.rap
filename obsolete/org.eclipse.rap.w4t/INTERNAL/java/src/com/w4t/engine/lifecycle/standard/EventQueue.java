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
package com.w4t.engine.lifecycle.standard;

import java.util.ArrayList;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import com.w4t.*;
import com.w4t.event.WebDataEvent;
import com.w4t.event.WebFileUploadEvent;


/** 
 * <p>Encapsulates a queue for WebDataEvents, needed for buffering events 
 * that occur when data from the request are read (ReadData phase); the 
 * events are fired after all data on components have been set.</p>
 */
public class EventQueue {

  /** the internal data structure that holds the queue. */
  private ArrayList dataQueue = new ArrayList();
  private ArrayList fileUploadQueue = new ArrayList();

  public static EventQueue getEventQueue() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    EventQueue result = ( EventQueue )stateInfo.getEventQueue();
    if( result == null ) {
      result = new EventQueue();
      stateInfo.setEventQueue( result );
    }
    return result;
  }
  
  /** <p>adds the passed evt to the queue.</p> */
  public void addDataEvent( final WebDataEvent evt ) {
    dataQueue.add( evt );
  }
  
  /** <p>removes the specified evt from the queue.</p> */
  public void removeDataEvent( final WebDataEvent evt ) {
    dataQueue.remove( evt );
  }
  
  /** <p>adds the passed evt to the queue.</p> */
  public void addFileUploadEvent( final WebFileUploadEvent evt ) {
    fileUploadQueue.add( evt );
  }
  
  /** <p>removes the specified evt from the queue.</p> */
  public void removeFileUploadEvent( final WebFileUploadEvent evt ) {
    fileUploadQueue.remove( evt );
  }
  
  /** <p>fires the events which were added to the queue in the order of 
    * their adding.</p> */
  public void fireEvents() {
    WebDataEvent[] dataEvents = new WebDataEvent[ dataQueue.size() ];
    dataQueue.toArray( dataEvents );
    for( int i = 0; i < dataEvents.length; i++ ) {
      WebDataEvent evt = dataEvents[ i ];
      evt.processEvent();
    }
    
    int size = fileUploadQueue.size();
    WebFileUploadEvent[] uploadEvents = new WebFileUploadEvent[ size ];
    fileUploadQueue.toArray( uploadEvents );
    for( int i = 0; i < uploadEvents.length; i++ ) {
      WebFileUploadEvent evt = uploadEvents[ i ];
      evt.processEvent();
    }
    clear();
  }
  
  /** <p>adds the specified WebDataEvent to the event queue of the 
   * request for which this ServiceStateInfo collects data.</p>
   * 
   * <p>The event queue for a request contains all WebDataEvents, i.e. 
   * events which are fired from a component when its value changes.</p>
   */
  public void addToQueue( final Object evt ) {
    if( evt instanceof WebDataEvent ) {
      addDataEvent( ( WebDataEvent )evt );
    } else if( evt instanceof WebFileUploadEvent ) {
      addFileUploadEvent( ( WebFileUploadEvent )evt );
    }
  }

  /** <p>removes all elements from the queue.</p> */
  public void clear() {
    dataQueue.clear();
    fileUploadQueue.clear();
  }
  
  /** <p>returns whether this EventQueue contains a WebDataEvent which 
    * has the specified source as event source.</p> */
  public boolean contains( final Object source ) {
    boolean result = false;
    for( int i = 0; !result && i < dataQueue.size(); i++ ) {
      WebDataEvent evt = ( WebDataEvent )dataQueue.get( i );
      if( evt.getSource() == source ) {
        result = true;
      }
    }
    for( int i = 0; !result && i < fileUploadQueue.size(); i++ ) {
      WebFileUploadEvent evt = ( WebFileUploadEvent )fileUploadQueue.get( i );
      if( evt.getSource() == source ) {
        result = true;
      }
    }
    return result;
  }
  
  /** <p>returns the first element of the queue that has the specified object 
    * as source, or null, if none was found.</p> */
  public WebDataEvent getDataEventBySource( final WebComponent source ) {
    WebDataEvent result = null;
    for( int i = 0; i < dataQueue.size() && result != null; i++ ) {
      WebDataEvent evt = ( WebDataEvent )dataQueue.get( i );
      if( evt.getSource() == source ) {
        result = evt;
      }
    }
    return result;
  }
  
  /** <p>removes all pairs of elements within this EventQueue that have
    * the same event source.</p> */
  public void removeDataEventPairs() {
    removePairs( dataQueue );
  }

  /** <p>returns a list of all WebDataEvents from this EventQueue which have
    * a WebCheckBox as event source. The returned list is already freed 
    * from doublettes (i.e. there are no two WebDataEvents with the same
    * event source in the returned list).</p> */
  public WebDataEvent[] getCheckBoxDataEvents() {
    ArrayList buffer = new ArrayList();
    for( int i = 0; i < dataQueue.size(); i++ ) {
      WebDataEvent evt = ( WebDataEvent )dataQueue.get( i );
      if( evt.getSource() instanceof WebCheckBox ) {
        buffer.add( evt );
      }
    }
    removePairs( buffer );
    return createWDEArray( buffer );
  }

  /** <p>returns a list of all WebDataEvents from this EventQueue which have
    * a WebRadioButtonGroup as event source and are doublettes. </p> */
  // TODO [rh] check if this can be removed
  public WebDataEvent[] getRadioButtonGroupDoublettes() {
    ArrayList buffer = new ArrayList();
    for( int i = 0; i < dataQueue.size(); i++ ) {
      WebDataEvent evt = ( WebDataEvent )dataQueue.get( i );
      if( evt.getSource() instanceof WebRadioButtonGroup ) {
        buffer.add( evt );
      }
    }
    buffer = findPairs( buffer );
    return createWDEArray( buffer );
  }
  
  
  // helping methods
  //////////////////
  
  /** returns the WebDataEvents contained in the passed list as array of#
    * WebDataEvents. */
  private WebDataEvent[] createWDEArray( final ArrayList list ) {
    WebDataEvent[] result = new WebDataEvent[ list.size() ];
    list.toArray( result );
    return result;
  }
  
  private ArrayList findPairs( final ArrayList list ) {
    ArrayList result = new ArrayList();
    for( int i = 0; i < list.size() - 1; i++ ) {
      WebDataEvent evt = ( WebDataEvent )list.get( i );
      int pos = i;
      boolean found = false;
      while( ++pos < list.size()  && !found ) {
        WebDataEvent nextEvt = ( WebDataEvent )dataQueue.get( pos );
        if( evt.getSource() == nextEvt.getSource() ) {
          result.add( evt );
          result.add( nextEvt );
          found = true;
        }
      }
    }
    return result;
  }
  
  /** removes all doublettes from the passed list, i.e. removes all pairs of 
    * elements within the list that have the same event source.</p> */
  private void removePairs( final ArrayList list ) {
    ArrayList buffer = findPairs( list );
    list.removeAll( buffer );
  }
}