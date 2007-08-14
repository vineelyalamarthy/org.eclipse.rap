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
package com.w4t.dhtml.menubarkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.CssClass;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.MenuBar;
import com.w4t.dhtml.MenuButton;
import com.w4t.dhtml.menustyle.MenuBarStyle;
import com.w4t.types.WebTriState;


/** <p>The common superclass renderer for org.eclipse.rap.dhtml.MenuBar 
  * on browsers with DOM support.</p>
  */
abstract class MenuBarRenderer extends Renderer {

  private final static int PADDING_CORRECTION = 0;

  public void scheduleRendering( final WebComponent component ) {
    // TODO refine this: should consider visibility and script/no-script/ajax 
    //      to determine whether to render or not
    MenuBar menuBar = ( MenuBar )component;
    for( int i = 0; i < menuBar.getItemCount(); i++ ) {
      getRenderingSchedule().schedule( menuBar.getItem( i ) );
    }
  }

  public void render( final WebComponent component ) throws IOException {
    useJSLibrary();
    createContainmentOpen( component );    
    MenuBar menuBar = ( MenuBar )component;
    createStyleTag( menuBar );    
    createDivOpen( menuBar );
    renderMenuButtons( menuBar );
    createDivClose();
    MenuBarUtil.renderPopupMenu( menuBar );    
    if( !MenuBarUtil.isScriptEnabled() ) { 
      menuBar.getMenuPopupStyle().setVisibility( new WebTriState( "visible" ) );
    }
    createContainmentClose();
  }
  
  // Methods to override by derived classes
  /////////////////////////////////////////

  protected abstract void useJSLibrary() throws IOException;
  
  /* some subclasses manipulate the style content (for padding correction). */
  String createStyleContent( final MenuBar menuBar ) {
    return getStyle( menuBar ).toString();
  }

  // Helper methods
  /////////////////
  
  private void createContainmentClose() throws IOException {
    getResponseWriter().endElement( HTML.DIV );
  }

  private void createContainmentOpen( final WebComponent component ) 
    throws IOException 
  {
    getResponseWriter().startElement( HTML.DIV, null );
    String id = component.getUniqueID();
    getResponseWriter().writeAttribute( HTML.ID, id, null );
    // Only necessary for Ie 5 and up:
    getResponseWriter().writeAttribute( HTML.ALIGN, HTML.LEFT, null );
  }
  
  private void createDivOpen( final MenuBar menuBar ) throws IOException {    
    int paddingBuffer = getStyle( menuBar ).getPadding();
    getStyle( menuBar ).setPadding( paddingBuffer - PADDING_CORRECTION );
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    StringBuffer buffer = new StringBuffer();
    buffer.append( "menubar_" );
    buffer.append( menuBar.getUniqueID() );
    out.writeAttribute( HTML.ID, buffer, null );
    createStyle( menuBar );
    getStyle( menuBar ).setPadding( paddingBuffer );
  }
  
  private void createStyle( final MenuBar menuBar ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( !getStyle( menuBar ).toString().equals( "" ) ) {
      out.writeAttribute( HTML.CLASS, 
                          out.registerCssClass( createStyleContent( menuBar ) ), 
                          null );
    }
  }
  
  private void renderMenuButtons( final MenuBar menuBar ) throws IOException {
    MenuButton[] menuButtons = MenuBarUtil.getInfo( menuBar ).getMenuButtons();
    for( int i = 0; i < menuButtons.length; i++ ) {
      LifeCycleHelper.render( menuButtons[ i ] );
    }
  }
  
  private void createDivClose() throws IOException {
    getResponseWriter().endElement( HTML.DIV );
  }
  
  private void createStyleTag( final MenuBar menuBar ) {
    addClass( "disabledMenuButton", menuBar.getButtonDisabledStyle() );
    addClass( "menuButton", menuBar.getButtonEnabledStyle() );
    addClass( "menuButton:hover", menuBar.getButtonHoverStyle() );
    addClass( "menuButtonActive", menuBar.getButtonActiveStyle() );
    addClass( "menuButtonActive:hover", menuBar.getButtonActiveStyle() );
    addClass( "menu", menuBar.getMenuPopupStyle() );
    addClass( "menuItem", menuBar.getItemEnabledStyle() );
    addClass( "menuItem:hover", menuBar.getItemHoverStyle() );
  }
  
  private void addClass( final String className, final Object style ) {
    HtmlResponseWriter out = getResponseWriter();
    out.addNamedCssClass( new CssClass( className, style.toString() ) );
  }
  
  static MenuBarStyle getStyle( final MenuBar menuBar ) {
    return menuBar.getMenuBarStyle();
  }
}