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

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.mockup.TestForm;
import com.w4t.util.IInitialization;
import com.w4t.util.browser.Default;
import com.w4t.util.image.ImageCache;


public class MenuBarRenderer_Default_Noscript_Test extends RenderingTestCase {

  public MenuBarRenderer_Default_Noscript_Test( final String name ) {
    super( name );
    setGenerateResources( false );
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    Fixture.setPrivateField( ImageCache.class, null, "_instance", null );
    ImageCache.createInstance( Fixture.getWebAppBase().toString(), 
                               IInitialization.NOSCRIPT_SUBMITTERS_NONE );
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
    Fixture.setPrivateField( ImageCache.class, null, "_instance", null );
  }
  
  public void testDefault_No_Script() throws Exception {
    Fixture.fakeBrowser( new Default( false, false ) );
    // reset the component counter, so that we have always the same IDs
    resetWebComponentCounter();
    WebForm form = new TestForm();
    WebPanel panel = new WebPanel();
    WebBorderLayout layout = new WebBorderLayout();
    panel.setWebLayout( layout );
    
    // empty menuBar
    MenuBar menuBar = new MenuBar();
    menuBar.setLabel( "menuBar" );
    menuBar.setName( "menuBar" );
    menuBar.getMenuBarStyle().setBorder( 11 );
    menuBar.getMenuBarStyle().setWidth( "11%" );
    form.add( menuBar, WebBorderLayout.NORTH );
    doRenderTest( menuBar, 21 );
    assertJSLibrary();
    
    //
    Menu fileMenu = new Menu();
    fileMenu.setLabel( "File" );
    fileMenu.setName( "File" );
    fileMenu.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
        // do nothing
      }
    } );
    menuBar.addItem( fileMenu );
    Menu helpMenu = new Menu();
    helpMenu.setLabel( "Help" );
    helpMenu.setName( "Help" );
    menuBar.addItem( helpMenu );
    doRenderTest( menuBar, 22 );
    
    //
    MenuItem exitMenuItem = new MenuItem();
    exitMenuItem.setLabel( "Exit" );
    exitMenuItem.setName( "Exit" );
    fileMenu.addItem( exitMenuItem );
    doRenderTest( menuBar, 23 );
    
    //
    MenuItemSeparator seperator = new MenuItemSeparator();
    seperator.setLabel( "myLabel" );
    seperator.setName( "myName" );
    fileMenu.addItem( seperator );
    doRenderTest( menuBar, 24 );
    assertJSLibrary();
  }

  private static void assertJSLibrary() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    String[] libraries = writer.getJSLibraries();
    assertEquals( "Wrong number of JavaScript libraries", 
                  0, 
                  libraries.length );
  }
}
//$endOfPublicClass
class Render_22_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_24_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_24 {

  private static String[] res = new String[] {
    "<",
    "table",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "0",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "11%",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "nowrap",
    " ",
    "bgcolor",
    "=\"",
    "#ffffff",
    "\"",
    ">",
    "<",
    "table",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "4",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    ">",
    "File<input type=\"image\" src=\"resources/images/submitter.gif\" name=\"waep4\" border=\"0\">",
    "</",
    "td",
    ">",
    "<",
    "td",
    ">",
    "<font face=\"arial,verdana\" color=\"#808080\" size=\"1\">",
    "Help",
    "</",
    "font",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_23 {

  private static String[] res = new String[] {
    "<",
    "table",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "0",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "11%",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "nowrap",
    " ",
    "bgcolor",
    "=\"",
    "#ffffff",
    "\"",
    ">",
    "<",
    "table",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "4",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    ">",
    "File<input type=\"image\" src=\"resources/images/submitter.gif\" name=\"waep4\" border=\"0\">",
    "</",
    "td",
    ">",
    "<",
    "td",
    ">",
    "<font face=\"arial,verdana\" color=\"#808080\" size=\"1\">",
    "Help",
    "</",
    "font",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_22 {

  private static String[] res = new String[] {
    "<",
    "table",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "0",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "11%",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "nowrap",
    " ",
    "bgcolor",
    "=\"",
    "#ffffff",
    "\"",
    ">",
    "<",
    "table",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "4",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    ">",
    "<font face=\"arial,verdana\" color=\"#808080\" size=\"1\">",
    "File",
    "</",
    "font",
    ">",
    "</",
    "td",
    ">",
    "<",
    "td",
    ">",
    "<font face=\"arial,verdana\" color=\"#808080\" size=\"1\">",
    "Help",
    "</",
    "font",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_21_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_23_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_21 {

  private static String[] res = new String[] {
    "<",
    "table",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "0",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "11%",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "nowrap",
    " ",
    "bgcolor",
    "=\"",
    "#ffffff",
    "\"",
    ">",
    "<",
    "table",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "cellspacing",
    "=\"",
    "0",
    "\"",
    " ",
    "cellpadding",
    "=\"",
    "4",
    "\"",
    ">",
    "<",
    "tr",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "<",
    "tr",
    ">",
    "<",
    "td",
    " ",
    "bgcolor",
    "=\"",
    "",
    "\"",
    ">",
    "<",
    "img",
    " ",
    "src",
    "=\"",
    "resources/images/transparent.gif",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " ",
    "width",
    "=\"",
    "1",
    "\"",
    " ",
    "height",
    "=\"",
    "11",
    "\"",
    " ",
    "align",
    "=\"",
    "top",
    "\"",
    ">",
    "</",
    "td",
    ">",
    "</",
    "tr",
    ">",
    "</",
    "table",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

