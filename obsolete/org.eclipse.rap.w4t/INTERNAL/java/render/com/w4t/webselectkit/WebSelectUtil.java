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
package com.w4t.webselectkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebFocusGainedEvent;
import com.w4t.event.WebItemEvent;


/** 
 * <p>Helper methods to render WebSelect components.</p>
 */
final class WebSelectUtil {

  /**
   * <p>Returns the event-handler attributes for script- and ajax-renderer</p>
   */
  
  private static final String EVENT_HANDLER_SET_FOCUS_ID 
    = "eventHandler.setFocusID(this)";

  static void writeFocusEventHandler( final WebSelect wsl ) throws IOException {
    if( wsl.isEnabled() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      StringBuffer buffer = new StringBuffer( EVENT_HANDLER_SET_FOCUS_ID );
      if( WebFocusGainedEvent.hasListener( wsl ) ) {
        buffer.append( ";" );
        buffer.append( WebFocusGainedEvent.JS_HANDLER_CALL );
        out.writeAttribute( WebFocusGainedEvent.JS_HANDLER, 
                            buffer.toString(), 
                            null );          
        out.writeAttribute( HTML.ON_MOUSE_DOWN, 
                            WebItemEvent.JS_SUSPEND_SUBMIT, 
                            null );
        out.writeAttribute( HTML.ON_MOUSE_UP, 
                            WebItemEvent.JS_RESUME_SUBMIT, 
                            null );
      } else {
        out.writeAttribute( WebFocusGainedEvent.JS_HANDLER,
                            buffer.toString(),
                            null );          
      }
    }
  }

  static boolean needsItemHandler( final WebComponent component ) {
    return    (    component instanceof IInputValueHolder
                && ( ( IInputValueHolder )component ).isUpdatable()
                && WebItemEvent.hasListener( component ) )
           || (   !( component instanceof IInputValueHolder )
                && WebItemEvent.hasListener( component ) );
  }
  
  /** creates the option tags of the html select */
  static void appendItemEntries( final WebSelect wsl ) throws IOException {
    applyEmptyItem( wsl );
    applyItems( wsl );
  }

  private static void applyItems( final WebSelect wsl )
    throws IOException
  {
    String[] items = wsl.getItem();
    for( int i = 0; i < items.length; i++ ) {
      if( items[ i ] != null ) {
        String itemValue = getItemValue( items[ i ] );
        String itemName  = getItemName( items[ i ] );
        itemName = itemName.equals( "" ) ? itemValue : itemName;
        boolean selected = isSelected( wsl, itemValue );
        appendOption( wsl, itemValue, itemName, selected );
      } else {
        String msg = "The item array set on the WebSelect "
                   + "contained a null reference.";
        throw new IllegalStateException(   msg );
      }
    }
  }

  private static boolean isSelected( final WebSelect wsl, 
                                     final String itemValue ) 
  {
    boolean result = false;
    String value = wsl.getValue();
    if(     itemValue.trim().equals( value.trim() )
        && !itemValue.equals( "" ) ) 
    {
      result = true;
    }
    return result;
  }

  private static void appendOption(  final WebSelect wsl,
                                     final String itemValue, 
                                     final String itemName,
                                     final boolean isSelected ) 
    throws IOException 
  {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();
    out.startElement( HTML.OPTION, null );
    out.writeAttribute( HTML.VALUE, itemValue, null );
    if ( isSelected ) {
      out.writeAttribute( HTML.SELECTED , null, null );
    }
    out.writeText( RenderUtil.resolve( itemName ), null );
    out.endElement( HTML.OPTION );
  }

  private static void applyEmptyItem( final WebSelect wsl ) 
    throws IOException
  {
    if( wsl.isUseEmptyItem() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.startElement( HTML.OPTION, null );
      out.endElement( HTML.OPTION );
    }
  }
  
  /** returns the value of a name=value pair
    * @param itemEntry the name=value pair to parse
    * @return the value of the name=value pair
    */
  private static String getItemValue( final String itemEntry ) {
    String result;
    if( hasEquals( itemEntry ) ) {
      result = itemEntry.substring( itemEntry.indexOf( "=" ) + 1, 
                                    itemEntry.length() );
    } else {
      result = itemEntry;      
    }
    return result;
  }
  
  /** returns the name of a name=value pair
    * @param itemEntry the name=value pair to parse
    * @return the name of the name=value pair */
  private static String getItemName( final String itemEntry ) {
    String result = "";
    if( hasEquals( itemEntry ) ) {
      result = itemEntry.substring( 0, itemEntry.indexOf( "=" ) );
    }
    return result;
  }
  
  private static boolean hasEquals( final String entry ) {
    return entry.indexOf( "=" ) != -1;
  }

  private WebSelectUtil() {
    // prevent instantiation
  }
}