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

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.Props;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class StyledTextLCA_Test extends TestCase {

  public void testPreserveValues() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    Fixture.markInitialized( display );
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( styledText );
    String html
      = ( String )adapter.getPreserved( StyledTextLCA.PROP_HTML );
    assertEquals( StyledTextLCA.DEFAULT_HTML, html );
    Point selection
      = ( Point )adapter.getPreserved( StyledTextLCA.PROP_SELECTION );
    assertEquals( StyledTextLCA.DEFAULT_SELECTION, selection );
    Integer caretOffset
      = ( Integer )adapter.getPreserved( StyledTextLCA.PROP_CARET_OFFSET );
    assertEquals( StyledTextLCA.DEFAULT_CARET_OFFSET, caretOffset );
    Fixture.clearPreserved();
    // Test preserved control properties
    testPreserveControlProperties( styledText );
    // Test preserved selection listeners
    testPreserveSelectionListener( styledText );
    // Test preserved mouse listeners
    testPreserveMouseListener( styledText );
    display.dispose();
  }

  public void testSelectionEvent() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    testSelectionEvent( styledText );
  }

  public void testMouseEvent() {
    Display display = new Display();
    Composite shell = new Shell( display, SWT.NONE );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    testMouseEvent( styledText );
  }

  private void testPreserveControlProperties( final StyledText styledText ) {
    // bound
    Rectangle rectangle = new Rectangle( 10, 10, 10, 10 );
    styledText.setBounds( rectangle );
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( rectangle, adapter.getPreserved( Props.BOUNDS ) );
    Fixture.clearPreserved();
    // enabled
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( Boolean.TRUE, adapter.getPreserved( Props.ENABLED ) );
    Fixture.clearPreserved();
    styledText.setEnabled( false );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( Boolean.FALSE, adapter.getPreserved( Props.ENABLED ) );
    Fixture.clearPreserved();
    // visible
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( Boolean.TRUE, adapter.getPreserved( Props.VISIBLE ) );
    Fixture.clearPreserved();
    styledText.setVisible( false );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( Boolean.FALSE, adapter.getPreserved( Props.VISIBLE ) );
    Fixture.clearPreserved();
    //foreground background font
    Color background = Graphics.getColor( 122, 33, 203 );
    styledText.setBackground( background );
    Color foreground = Graphics.getColor( 211, 178, 211 );
    styledText.setForeground( foreground );
    Font font = Graphics.getFont( "font", 12, SWT.BOLD );
    styledText.setFont( font );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    assertEquals( background, adapter.getPreserved( Props.BACKGROUND ) );
    assertEquals( foreground, adapter.getPreserved( Props.FOREGROUND ) );
    assertEquals( font, adapter.getPreserved( Props.FONT ) );
    Fixture.clearPreserved();
  }

  private void testPreserveSelectionListener( final StyledText styledText ) {
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( styledText );
    Boolean hasListeners
      = ( Boolean )adapter.getPreserved( StyledTextLCA.SELECTION_LISTENERS );
    assertEquals( Boolean.FALSE, hasListeners );
    Fixture.clearPreserved();
    SelectionListener selectionListener = new SelectionAdapter() { };
    styledText.addSelectionListener( selectionListener );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    hasListeners
      = ( Boolean )adapter.getPreserved( StyledTextLCA.SELECTION_LISTENERS );
    assertEquals( Boolean.TRUE, hasListeners );
    Fixture.clearPreserved();
  }

  private void testPreserveMouseListener( final StyledText styledText ) {
    Fixture.preserveWidgets();
    IWidgetAdapter adapter = WidgetUtil.getAdapter( styledText );
    Boolean hasListeners
      = ( Boolean )adapter.getPreserved( StyledTextLCA.MOUSE_LISTENERS );
    assertEquals( Boolean.FALSE, hasListeners );
    Fixture.clearPreserved();
    MouseListener selectionListener = new MouseAdapter() { };
    styledText.addMouseListener( selectionListener );
    Fixture.preserveWidgets();
    adapter = WidgetUtil.getAdapter( styledText );
    hasListeners
      = ( Boolean )adapter.getPreserved( StyledTextLCA.MOUSE_LISTENERS );
    assertEquals( Boolean.TRUE, hasListeners );
    Fixture.clearPreserved();
  }

  private void testSelectionEvent( final StyledText styledText ) {
    final StringBuffer log = new StringBuffer();
    SelectionListener selectionListener = new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        assertEquals( styledText, event.getSource() );
        assertEquals( null, event.item );
        assertEquals( SWT.NONE, event.detail );
        assertEquals( 10, event.x );
        assertEquals( 20, event.y );
        assertEquals( 0, event.width );
        assertEquals( 0, event.height );
        assertEquals( true, event.doit );
        log.append( "widgetSelected" );
      }
    };
    styledText.addSelectionListener( selectionListener );
    String styledTextId = WidgetUtil.getId( styledText );
    Fixture.fakeRequestParam( styledTextId + ".selectionStart", "10" );
    Fixture.fakeRequestParam( styledTextId + ".selectionEnd", "20" );
    Fixture.fakeRequestParam( StyledTextLCA.EVENT_WIDGET_SELECTED,
                              styledTextId );
    Fixture.readDataAndProcessAction( styledText );
    assertEquals( "widgetSelected", log.toString() );
  }

  private void testMouseEvent( final StyledText styledText ) {
    final StringBuffer log = new StringBuffer();
    MouseListener mouseListener = new MouseAdapter() {
      public void mouseDown( final MouseEvent event ) {
        assertEquals( styledText, event.getSource() );
        assertEquals( 1, event.button );
        assertEquals( 10, event.x );
        assertEquals( 20, event.y );
        assertEquals( 30, event.time );
        log.append( "mouseDown" );
      }
      public void mouseUp( final MouseEvent event ) {
        assertEquals( styledText, event.getSource() );
        assertEquals( 1, event.button );
        assertEquals( 10, event.x );
        assertEquals( 20, event.y );
        assertEquals( 30, event.time );
        log.append( "mouseUp" );
      }
    };
    styledText.addMouseListener( mouseListener );
    String styledTextId = WidgetUtil.getId( styledText );
    String reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_DOWN_BUTTON;
    Fixture.fakeRequestParam( reqParam, "1" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_DOWN_X;
    Fixture.fakeRequestParam( reqParam, "10" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_DOWN_Y;
    Fixture.fakeRequestParam( reqParam, "20" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_DOWN_TIME;
    Fixture.fakeRequestParam( reqParam, "30" );
    Fixture.fakeRequestParam( StyledTextLCA.EVENT_MOUSE_DOWN,
                              styledTextId );
    Fixture.readDataAndProcessAction( styledText );
    assertEquals( "mouseDown", log.toString() );
    log.setLength( 0 );
    Fixture.fakeNewRequest();
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_UP_BUTTON;
    Fixture.fakeRequestParam( reqParam, "1" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_UP_X;
    Fixture.fakeRequestParam( reqParam, "10" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_UP_Y;
    Fixture.fakeRequestParam( reqParam, "20" );
    reqParam = styledTextId + "." + StyledTextLCA.EVENT_MOUSE_UP_TIME;
    Fixture.fakeRequestParam( reqParam, "30" );
    Fixture.fakeRequestParam( StyledTextLCA.EVENT_MOUSE_UP,
                              styledTextId );
    Fixture.readDataAndProcessAction( styledText );
    assertEquals( "mouseUp", log.toString() );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
