/*******************************************************************************
 * Copyright (c) 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.internal.custom.styledtextkit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.custom.IStyledTextAdapter;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

public final class StyledTextLCA extends AbstractWidgetLCA {

  private static final String QX_TYPE = "org.eclipse.swt.custom.StyledText";

  //Property names for preserveValues
  static final String PROP_HTML = "html";
  static final String PROP_SELECTION = "selection";
  static final String PROP_CARET_OFFSET = "caretOffset";
  static final String SELECTION_LISTENERS = "selectionListeners";
  static final String MOUSE_LISTENERS = "mouseListeners";

  static final String EVENT_WIDGET_SELECTED
    = "org.eclipse.swt.events.widgetSelected";
  static final String EVENT_MOUSE_UP
    = "org.eclipse.swt.events.mouseUp";
  static final String EVENT_MOUSE_DOWN
    = "org.eclipse.swt.events.mouseDown";
  static final String EVENT_MOUSE_UP_BUTTON
    = "org.eclipse.swt.events.mouseUp.button";
  static final String EVENT_MOUSE_UP_X
    = "org.eclipse.swt.events.mouseUp.x";
  static final String EVENT_MOUSE_UP_Y
    = "org.eclipse.swt.events.mouseUp.y";
  static final String EVENT_MOUSE_UP_TIME
    = "org.eclipse.swt.events.mouseUp.time";
  static final String EVENT_MOUSE_DOWN_BUTTON
    = "org.eclipse.swt.events.mouseDown.button";
  static final String EVENT_MOUSE_DOWN_X
    = "org.eclipse.swt.events.mouseDown.x";
  static final String EVENT_MOUSE_DOWN_Y
    = "org.eclipse.swt.events.mouseDown.y";
  static final String EVENT_MOUSE_DOWN_TIME
    = "org.eclipse.swt.events.mouseDown.time";

  //Default values
  static final String DEFAULT_HTML = "";
  static final Point DEFAULT_SELECTION = new Point( 0, 0 );
  static final Integer DEFAULT_CARET_OFFSET = new Integer( 0 );

  public void preserveValues( final Widget widget ) {
    StyledText styledText = ( StyledText )widget;
    ControlLCAUtil.preserveValues( styledText );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( styledText );
    boolean hasSelectionListeners = SelectionEvent.hasListener( styledText );
    adapter.preserve( SELECTION_LISTENERS,
                      Boolean.valueOf( hasSelectionListeners ) );
    boolean hasMouseListeners = MouseEvent.hasListener( styledText );
    adapter.preserve( MOUSE_LISTENERS,
                      Boolean.valueOf( hasMouseListeners ) );
    adapter.preserve( PROP_HTML, getHtml( styledText ) );
    adapter.preserve( PROP_SELECTION, styledText.getSelection() );
    adapter.preserve( PROP_CARET_OFFSET,
                      new Integer( styledText.getCaretOffset() ) );
    WidgetLCAUtil.preserveCustomVariant( styledText );
  }

  public void readData( final Widget widget ) {
    final StyledText styledText = ( StyledText )widget;
    String caretOffset
      = WidgetLCAUtil.readPropertyValue( styledText, "caretOffset" );
    if( caretOffset != null ) {
      styledText.setCaretOffset( Integer.parseInt( caretOffset ) );
    }
    if( WidgetLCAUtil.wasEventSent( styledText, EVENT_WIDGET_SELECTED ) ) {

      String selStart
        = WidgetLCAUtil.readPropertyValue( styledText, "selectionStart" );
      String selEnd
        = WidgetLCAUtil.readPropertyValue( styledText, "selectionEnd" );
      if( selStart != null && selEnd != null ) {
        final int start = Integer.parseInt( selStart );
        final int end = Integer.parseInt( selEnd );
        styledText.setSelection( start, end );
        int eventId = SelectionEvent.WIDGET_SELECTED;
        Rectangle bounds = new Rectangle( start, end, 0, 0 );
        SelectionEvent event = new SelectionEvent( styledText,
                                                   null,
                                                   eventId,
                                                   bounds,
                                                   null,
                                                   true,
                                                   0 );
        event.processEvent();
      }
    }
    processMouseEvents( styledText );
  }

  public void renderInitialization( final Widget widget ) throws IOException {
    StyledText styledText = ( StyledText )widget;
    JSWriter writer = JSWriter.getWriterFor( styledText );
    writer.newWidget( QX_TYPE );
    ControlLCAUtil.writeStyleFlags( styledText );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    StyledText styledText = ( StyledText )widget;
    ControlLCAUtil.writeChanges( styledText );
    writeHtml( styledText );
    writeSelection( styledText );
    writeCaretOffset( styledText );
    writeSelectionListener( styledText );
    writeMouseListener( styledText );
    WidgetLCAUtil.writeCustomVariant( styledText );
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  public void createResetHandlerCalls( final String typePoolId )
    throws IOException
  {
    ControlLCAUtil.resetStyleFlags();
  }

  public String getTypePoolId( final Widget widget ) {
    return null;
  }

  private void writeHtml( final StyledText styledText )
    throws IOException
  {
    String newValue = getHtml( styledText );
    String prop = PROP_HTML;
    String defValue = DEFAULT_HTML;
    if( WidgetLCAUtil.hasChanged( styledText, prop, newValue, defValue ) ) {
      JSWriter writer = JSWriter.getWriterFor( styledText );
      writer.set( prop, newValue );
    }
  }

  private void writeSelection( final StyledText styledText )
    throws IOException
  {
    Point newValue = styledText.getSelection();
    String prop = PROP_SELECTION;
    Point defValue = DEFAULT_SELECTION;
    if( WidgetLCAUtil.hasChanged( styledText, prop, newValue, defValue ) ) {
      JSWriter writer = JSWriter.getWriterFor( styledText );
      Object[] args = new Object[]{
        new Integer( newValue.x ),
        new Integer( newValue.y )
      };
      writer.call( "setSelection", args );
    }
  }

  private void writeCaretOffset( final StyledText styledText )
    throws IOException
  {
    Integer newValue = new Integer( styledText.getCaretOffset() );
    String prop = PROP_CARET_OFFSET;
    Integer defValue = DEFAULT_CARET_OFFSET;
    if( WidgetLCAUtil.hasChanged( styledText, prop, newValue, defValue ) ) {
      JSWriter writer = JSWriter.getWriterFor( styledText );
      writer.set( prop, newValue );
    }
  }

  private void writeSelectionListener( final StyledText styledText )
    throws IOException
  {
    boolean hasSelectionListener = SelectionEvent.hasListener( styledText );
    Boolean newValue = Boolean.valueOf( hasSelectionListener );
    String prop = SELECTION_LISTENERS;
    if( WidgetLCAUtil.hasChanged( styledText, prop, newValue, Boolean.FALSE ) ) {
      JSWriter writer = JSWriter.getWriterFor( styledText );
      writer.set( "hasSelectionListener", newValue );
    }
  }

  private void writeMouseListener( final StyledText styledText )
    throws IOException
  {
    boolean hasMouseListener = MouseEvent.hasListener( styledText );
    Boolean newValue = Boolean.valueOf( hasMouseListener );
    String prop = MOUSE_LISTENERS;
    if( WidgetLCAUtil.hasChanged( styledText, prop, newValue, Boolean.FALSE ) ) {
      JSWriter writer = JSWriter.getWriterFor( styledText );
      writer.set( "hasMouseListener", newValue );
    }
  }

  ///////////////////////
  // Mouse event handling

  private static void processMouseEvents( final Control control ) {
    if( WidgetLCAUtil.wasEventSent( control, EVENT_MOUSE_DOWN ) ) {
      try {
        MouseEvent event = new MouseEvent( control, MouseEvent.MOUSE_DOWN );
        event.button
          = readIntParam( control, EVENT_MOUSE_DOWN_BUTTON );
        Point point = readXYParams( control,
                                    EVENT_MOUSE_DOWN_X,
                                    EVENT_MOUSE_DOWN_Y );
        event.x = point.x;
        event.y = point.y;
        event.time = readIntParam( control,
                                   EVENT_MOUSE_DOWN_TIME );
        event.processEvent();
      } catch( NumberFormatException nfe ) {
        // do nothing
      }
    }
    if( WidgetLCAUtil.wasEventSent( control, EVENT_MOUSE_UP ) ) {
      try {
        MouseEvent event = new MouseEvent( control, MouseEvent.MOUSE_UP );
        event.button = readIntParam( control,
                                     EVENT_MOUSE_UP_BUTTON );
        Point point = readXYParams( control,
                                    EVENT_MOUSE_UP_X,
                                    EVENT_MOUSE_UP_Y );
        event.x = point.x;
        event.y = point.y;
        event.time = readIntParam( control,
                                   EVENT_MOUSE_UP_TIME );
        event.processEvent();
      } catch( NumberFormatException nfe ) {
        // do nothing
      }
    }
  }


  private static int readIntParam( final Control control,
                                   final String paramName )
  {
    String value = WidgetLCAUtil.readPropertyValue( control, paramName );
    return Integer.parseInt( value );
  }

  private static Point readXYParams( final Control control,
                                     final String paramNameX,
                                     final String paramNameY )
  {
    int x = readIntParam( control, paramNameX );
    int y = readIntParam( control, paramNameY );
    return new Point( x, y );
  }

  private static String getHtml( final StyledText styledText ) {
    IStyledTextAdapter adapter
      = ( IStyledTextAdapter )styledText.getAdapter( IStyledTextAdapter.class );
    return adapter.getHtml();
  }
}
