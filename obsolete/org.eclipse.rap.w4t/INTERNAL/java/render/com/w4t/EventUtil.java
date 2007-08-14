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
package com.w4t;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.event.WebFocusGainedEvent;
import com.w4t.event.WebItemEvent;


public class EventUtil {
  
  private static final String EVENT_HANDLER_SET_FOCUS_ID 
    = "eventHandler.setFocusID(this)";

  private EventUtil() {
  }

  public static void createItemAndFocusHandler( final WebComponent component )
    throws IOException
  {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    if( component.isEnabled() ) {
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      if( needsItemHandler( component ) ) {
        out.writeAttribute( HTML.ON_CHANGE,
                            WebItemEvent.JS_EVENT_CALL,
                            null );
      }
      StringBuffer buffer = new StringBuffer( EVENT_HANDLER_SET_FOCUS_ID );
      if( WebFocusGainedEvent.hasListener( component ) ) {
        buffer.append( ";" );
        buffer.append( WebFocusGainedEvent.JS_HANDLER_CALL );
      }
      out.writeAttribute( WebFocusGainedEvent.JS_HANDLER,
                          buffer.toString(),
                          null );          
    }
  }

  private static boolean needsItemHandler( final WebComponent component ) {
    return    (    component instanceof IInputValueHolder
                && ( ( IInputValueHolder )component ).isUpdatable()
                && WebItemEvent.hasListener( component ) )
           || (   !( component instanceof IInputValueHolder )
                && WebItemEvent.hasListener( component ) );
  }
}
