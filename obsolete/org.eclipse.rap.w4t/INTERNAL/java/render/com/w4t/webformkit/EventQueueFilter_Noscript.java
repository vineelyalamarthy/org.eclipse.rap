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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.*;
import com.w4t.engine.lifecycle.standard.EventQueue;
import com.w4t.engine.lifecycle.standard.EventQueueFilter;
import com.w4t.event.WebDataEvent;
import com.w4t.event.WebItemEvent;


/** <p>the superclass of specific EventQueueFilter classes for browser-dependent
  * algorithms of the filtering of the data event queue (which is a part of
  * the ReadData phase).</p>
  */
class EventQueueFilter_Noscript extends EventQueueFilter {

  
  /** does the actual filtering. */
  public void filter() {
    filterCheckBoxEvents();
    EventQueue.getEventQueue().removeDataEventPairs();
  }
  
  private void filterCheckBoxEvents() {
    EventQueue queue = EventQueue.getEventQueue();
    WebDataEvent[] checkBoxEvents = queue.getCheckBoxDataEvents();
    for( int i = 0; i < checkBoxEvents.length; i++ ) {
      processCheckBoxEvent( checkBoxEvents[ i ] );
    }
  }
  
  private void processCheckBoxEvent( final WebDataEvent evt ) {
    WebCheckBox source = ( WebCheckBox )evt.getSource();
    if( wasSubmitted( source ) ) {
      String oldValue = source.getValue();
      String newValue = invertValue( source, oldValue );
      LifeCycleHelper.applyRequestValue( source, newValue );
    }
  }
  
  /** returns the value for 'checked' on the specified checkbox, if the passed 
    * value is the value for 'unchecked', and the value for 'unchecked' 
    * otherwise. */
  private String invertValue( final WebCheckBox wcb, final String value ) {
    String result = wcb.getValUnCheck();
    if( value.equals( result ) ) {
      result = wcb.getValCheck();
    }
    return result;
  }

  /** returns whether the passed WebComponent was the source of the event 
    * which caused the request. */ 
  boolean wasSubmitted( final WebComponent component ) {
    return     WebItemEvent.hasListener( component )
           &&  contains( component.getUniqueID() );
  }
  
  /** returns whether the specified request contains a request parameter with
    * the specified name. */ 
  private boolean contains( final String name ) {
    String prefixedName = NoscriptUtil.addItemPrefix( name );
    String suffixedName = NoscriptUtil.addSuffix( prefixedName );
    HttpServletRequest request = ContextProvider.getRequest();
    return (    request.getParameter( prefixedName ) != null 
             || request.getParameter( suffixedName ) != null );
  }
}