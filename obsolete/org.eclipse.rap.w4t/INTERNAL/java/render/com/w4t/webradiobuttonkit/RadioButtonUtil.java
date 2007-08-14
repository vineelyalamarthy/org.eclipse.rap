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
package com.w4t.webradiobuttonkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebFocusGainedEvent;
import com.w4t.event.WebItemEvent;


final class RadioButtonUtil {
  
  private static final String EVENT_HANDLER_SET_FOCUS_ID 
    = "eventHandler.setFocusID(this)";

  static void renderInputForScript( final HtmlResponseWriter writer,
                                    final WebRadioButton radioButton ) 
    throws IOException
  {
    writer.startElement( HTML.INPUT, null );
    writer.writeAttribute( HTML.TYPE, HTML.RADIO, null );
    writer.writeAttribute( HTML.STYLE, "vertical-align:middle", null );
    RadioButtonUtil.renderName( writer, radioButton );
    RadioButtonUtil.renderChecked( radioButton );
    renderDisabled( writer, radioButton );
    String value = RadioButtonUtil.getValue( radioButton );
    writer.writeAttribute( HTML.VALUE, value, null );
    RadioButtonUtil.createEventHandler( radioButton );
    writer.endElement( HTML.INPUT );
  }
  
  static void renderName( final HtmlResponseWriter writer,
                          final WebRadioButton radioButton ) 
    throws IOException 
  {
    // name attribute is used to group radioButtons (radioButtons with same
    // name belong to one group); if there is no group a radioButton only
    // belongs to itself
    WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton );
    String name;
    if( group != null ) {
      name = group.getUniqueID();
    } else {
      name = radioButton.getUniqueID();
    }
    writer.writeAttribute( HTML.NAME, name, null );
  }

  static void renderChecked( final WebRadioButton radioButton ) 
    throws IOException 
  {
    if ( radioButton.isSelected() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.writeAttribute( HTML.CHECKED, null, null );
    }
  }
  
  static void renderDisabled( final HtmlResponseWriter writer,
                              final WebRadioButton radioButton ) 
    throws IOException 
  {
    WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton );
    if( group == null || !group.isUpdatable() || !radioButton.isEnabled() ) {
      writer.writeAttribute( HTML.DISABLED, null, null );
    }
  }

  static void renderDisplay( final WebRadioButton radioButton ) 
    throws IOException 
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.SPAN, null );
    RenderUtil.writeUniversalAttributes( radioButton );
    out.writeText( RenderUtil.resolve( radioButton.getLabel() ), null );
    out.endElement( HTML.SPAN );
  }

  static String getValue( final WebRadioButton radioButton ) {
    // TODO [rh] is it really helpful to resolve the value?
    return RenderUtil.resolve( radioButton.getValue() );
  }

  static void createEventHandler( final WebRadioButton radioButton ) 
    throws IOException
  {
    if( radioButton.isEnabled() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton );
      if( group != null && WebItemEvent.hasListener( group ) ) {
        out.writeAttribute( HTML.ON_CLICK,
                            WebItemEvent.JS_EVENT_CALL,
                            null );
      }
      if( WebFocusGainedEvent.hasListener( radioButton ) ) {
        StringBuffer buffer = new StringBuffer( EVENT_HANDLER_SET_FOCUS_ID );
        buffer.append( ";" );
        buffer.append( WebFocusGainedEvent.JS_HANDLER_CALL );
        out.writeAttribute( WebFocusGainedEvent.JS_HANDLER,
                            buffer.toString(),
                            null );       
      }
    }
  }

  private RadioButtonUtil() {
    // prevent instantiation
  }
  
}