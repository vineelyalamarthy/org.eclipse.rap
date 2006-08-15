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
import com.w4t.util.browser.Default;


public class MenuBarRenderer_Default_Script_Test extends RenderingTestCase {

  public MenuBarRenderer_Default_Script_Test( final String name ) {
    super( name );
    setGenerateResources( false );
  }

  public void testDefault_Script() throws Exception {
    Fixture.fakeBrowser( new Default( true, false ) );
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
    doRenderTest( menuBar, 1 );
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
    doRenderTest( menuBar, 2 );
    
    //
    MenuItem exitMenuItem = new MenuItem();
    exitMenuItem.setLabel( "Exit" );
    exitMenuItem.setName( "Exit" );
    fileMenu.addItem( exitMenuItem );
    doRenderTest( menuBar, 3 );
    
    //
    MenuItemSeparator seperator = new MenuItemSeparator();
    seperator.setLabel( "myLabel" );
    seperator.setName( "myName" );
    fileMenu.addItem( seperator );
    doRenderTest( menuBar, 4 );
    assertJSLibrary();
  }
  
  private static void assertJSLibrary() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter writer = stateInfo.getResponseWriter();
    String[] libraries = writer.getJSLibraries();
    assertEquals( "Wrong number of JavaScript libraries", 
                  1, 
                  libraries.length );
    assertEquals( "resources/js/menubar/menubar_default.js", libraries[ 0 ] );
  }
}
//$endOfPublicClass
class Render_4 {

  private static String[] res = new String[] {
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p3",
    "\"",
    " ",
    "align",
    "=\"",
    "left",
    "\"",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "menubar_p3",
    "\"",
    " ",
    "class",
    "=\"",
    "w4tCss861c23b2",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButton",
    "\"",
    " ",
    "href",
    "=\"",
    "",
    "\"",
    " ",
    "onclick",
    "=\"",
    "return menuBarHandler.buttonClick(this, 'p4', 'p3');",
    "\"",
    " ",
    "onmouseover",
    "=\"",
    "menuBarHandler.buttonMouseover(this, 'p4', 'p3');",
    "\"",
    ">",
    "File",
    "</",
    "a",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "disabledMenuButton",
    "\"",
    ">",
    "Help",
    "</",
    "a",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p4",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "id",
    "=\"",
    "p8",
    "\"",
    " ",
    "class",
    "=\"",
    "menuItem",
    "\"",
    " ",
    "style",
    "=\"",
    "font-family:arial,verdana;font-size:8pt;color:#808080;",
    "\"",
    ">",
    "Exit",
    "</",
    "a",
    ">",
    "<",
    "div",
    " ",
    "class",
    "=\"",
    "w4tCssd8f5858f",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p6",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_2_css {

  private static String[] res = new String[] {
    "com.w4t.types.CssClass [ .w4tCss861c23b2, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:11px solid;padding:4px;width:11%; ]",
    "com.w4t.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "com.w4t.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "com.w4t.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:hidden;z-index:99999; ]",
    "com.w4t.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_4_css {

  private static String[] res = new String[] {
    "com.w4t.types.CssClass [ .w4tCssd8f5858f, border-bottom: 1px solid #f0f0f0;border-top: 1px solid #808080;margin: 3px 4px 3px 4px; ]",
    "com.w4t.types.CssClass [ .w4tCss861c23b2, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:11px solid;padding:4px;width:11%; ]",
    "com.w4t.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "com.w4t.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "com.w4t.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:hidden;z-index:99999; ]",
    "com.w4t.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_3 {

  private static String[] res = new String[] {
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p3",
    "\"",
    " ",
    "align",
    "=\"",
    "left",
    "\"",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "menubar_p3",
    "\"",
    " ",
    "class",
    "=\"",
    "w4tCss861c23b2",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButton",
    "\"",
    " ",
    "href",
    "=\"",
    "",
    "\"",
    " ",
    "onclick",
    "=\"",
    "return menuBarHandler.buttonClick(this, 'p4', 'p3');",
    "\"",
    " ",
    "onmouseover",
    "=\"",
    "menuBarHandler.buttonMouseover(this, 'p4', 'p3');",
    "\"",
    ">",
    "File",
    "</",
    "a",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "disabledMenuButton",
    "\"",
    ">",
    "Help",
    "</",
    "a",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p4",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "id",
    "=\"",
    "p8",
    "\"",
    " ",
    "class",
    "=\"",
    "menuItem",
    "\"",
    " ",
    "style",
    "=\"",
    "font-family:arial,verdana;font-size:8pt;color:#808080;",
    "\"",
    ">",
    "Exit",
    "</",
    "a",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p6",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_2 {

  private static String[] res = new String[] {
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p3",
    "\"",
    " ",
    "align",
    "=\"",
    "left",
    "\"",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "menubar_p3",
    "\"",
    " ",
    "class",
    "=\"",
    "w4tCss861c23b2",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "disabledMenuButton",
    "\"",
    ">",
    "File",
    "</",
    "a",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "disabledMenuButton",
    "\"",
    ">",
    "Help",
    "</",
    "a",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p4",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p6",
    "\"",
    " ",
    "class",
    "=\"",
    "menu",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_1 {

  private static String[] res = new String[] {
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "p3",
    "\"",
    " ",
    "align",
    "=\"",
    "left",
    "\"",
    ">",
    "<",
    "div",
    " ",
    "id",
    "=\"",
    "menubar_p3",
    "\"",
    " ",
    "class",
    "=\"",
    "w4tCss861c23b2",
    "\"",
    ">",
    "</",
    "div",
    ">",
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_1_css {

  private static String[] res = new String[] {
    "com.w4t.types.CssClass [ .w4tCss861c23b2, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:11px solid;padding:4px;width:11%; ]",
    "com.w4t.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "com.w4t.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "com.w4t.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:hidden;z-index:99999; ]",
    "com.w4t.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_3_css {

  private static String[] res = new String[] {
    "com.w4t.types.CssClass [ .w4tCss861c23b2, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:11px solid;padding:4px;width:11%; ]",
    "com.w4t.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "com.w4t.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "com.w4t.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "com.w4t.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:hidden;z-index:99999; ]",
    "com.w4t.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

