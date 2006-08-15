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
package com.w4t.webformkit;

import com.w4t.engine.lifecycle.standard.EventQueue;
import com.w4t.engine.lifecycle.standard.EventQueueFilter;
import com.w4t.event.WebDataEvent;


/** <p>the superclass of specific EventQueueFilter classes for browser-dependent
  * algorithms of the filtering of the data event queue (which is a part of
  * the ReadData phase).</p>
  */
class EventQueueFilter_Script extends EventQueueFilter {

  
  /** does the actual filtering. */  
  public void filter() {
    filterRadioButtonGroupEvents();
    // remove remaining pairs from the queue
    EventQueue.getEventQueue().removeDataEventPairs();
  }

  /** replaces doubled events on radio buttons with events that sum up
    * their functionality. */
  private void filterRadioButtonGroupEvents() {
    EventQueue queue = EventQueue.getEventQueue();
    WebDataEvent[] doublettes = queue.getRadioButtonGroupDoublettes();
    for( int i = 0; i < doublettes.length; i += 2 ) {
      WebDataEvent first  = doublettes[ i ];
      WebDataEvent second = doublettes[ i + 1 ];
      if( !cancelsOut( first, second ) ) {
        queue.addDataEvent( createResultingEvent( first, second ) );
      }
      queue.removeDataEvent( first );
      queue.removeDataEvent( second );
    }
  }
  
  /** returns true, if the second of the passed two data events cancels the 
    * first out, i.e. if applying both the old state would be reproduced.
    *
    * It is assumed that both passed WebDataEvents have the same event source.
    */
  private boolean cancelsOut( final WebDataEvent first, 
                              final WebDataEvent second ) {
    // assert ( first.getSource() == second.getSource() );
    return first.getOldValue().equals( second.getNewValue() );
  }
  
  /** creates a WebDataEvent that has the same effect as the successive firing
    * of first and second would have. 
    *
    * It is assumed that both passed WebDataEvents have the same event source.
    */
  private WebDataEvent createResultingEvent( final WebDataEvent first, 
                                             final WebDataEvent second ) {
    // assert ( first.getSource() == second.getSource() );
    return new WebDataEvent( first.getSource(), 
                             first.getID(), 
                             first.getOldValue(), 
                             second.getNewValue() );
  }
}