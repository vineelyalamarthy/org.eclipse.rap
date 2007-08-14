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

import org.eclipse.rwt.internal.IInitialization;
import org.eclipse.rwt.internal.browser.Mozilla1_6up;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.mockup.TestForm;
import com.w4t.util.image.ImageCache;

public class MenuBarRenderer_Mozilla1_6up_Noscript_Test extends RenderingTestCase {

  public MenuBarRenderer_Mozilla1_6up_Noscript_Test( final String name ) {
    super( name );
    setGenerateResources( false );
  }

  protected void setUp() throws Exception {
    super.setUp();
    W4TFixture.setPrivateField( ImageCache.class, null, "_instance", null );
    ImageCache.createInstance( W4TFixture.getWebAppBase().toString(), 
                               IInitialization.NOSCRIPT_SUBMITTERS_NONE );
  }
  public void tearDown() throws Exception {
    super.tearDown();
    W4TFixture.setPrivateField( ImageCache.class, null, "_instance", null );
  }
  
  public void testMarkup() throws Exception {
    // Mozilla 1.6 - no script
    W4TFixture.fakeBrowser( new Mozilla1_6up( false, false ) );
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
    form.add( menuBar, WebBorderLayout.NORTH );
    doRenderTest( menuBar, 41 );
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
    doRenderTest( menuBar, 42 );
    //
    MenuItem exitMenuItem = new MenuItem();
    exitMenuItem.setLabel( "Exit" );
    exitMenuItem.setName( "Exit" );
    fileMenu.addItem( exitMenuItem );
    doRenderTest( menuBar, 43 );
    
    //
    MenuItemSeparator seperator = new MenuItemSeparator();
    seperator.setLabel( "myLabel" );
    seperator.setName( "myName" );
    fileMenu.addItem( seperator );
    doRenderTest( menuBar, 44 );
    
    // 
    MenuButton button = new MenuButton();
    button.setName( "myName" );
    panel.add( button, WebBorderLayout.CENTER );
    doRenderTest( menuBar, 45 );
    
    fileMenu.setExpanded( true );
    doRenderTest( menuBar, 46 );
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
class Render_43_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:visible;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_42_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:visible;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_41_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:hidden;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_46 {

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
    "w4tCss6343a30b",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButtonActive",
    "\"",
    ">",
    "File",
    "&nbsp;",
    "<",
    "input",
    " ",
    "type",
    "=\"",
    "image",
    "\"",
    " ",
    "src",
    "=\"",
    "resources/images/submitter.gif",
    "\"",
    " ",
    "name",
    "=\"",
    "waep4",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " />",
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
    "<div id=\"",
    "p4",
    "\" class=\"menu\"",
    ">",
    "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">",
    "<tr>",
    "<td>",
    "<a title=\"",
    "",
    "\" class=\"menuItem\" style=\"",
    "font-family:arial,verdana;font-size:8pt;color:#808080;",
    "\">",
    "Exit",
    "</a>",
    "</td>",
    "<td>",
    "&nbsp;|&nbsp;",
    "</td>",
    "</tr></table>",
    "</div>",
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_45 {

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
    "w4tCss6343a30b",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButton",
    "\"",
    ">",
    "File",
    "&nbsp;",
    "<",
    "input",
    " ",
    "type",
    "=\"",
    "image",
    "\"",
    " ",
    "src",
    "=\"",
    "resources/images/submitter.gif",
    "\"",
    " ",
    "name",
    "=\"",
    "waep4",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " />",
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
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_44 {

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
    "w4tCss6343a30b",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButton",
    "\"",
    ">",
    "File",
    "&nbsp;",
    "<",
    "input",
    " ",
    "type",
    "=\"",
    "image",
    "\"",
    " ",
    "src",
    "=\"",
    "resources/images/submitter.gif",
    "\"",
    " ",
    "name",
    "=\"",
    "waep4",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " />",
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
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_43 {

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
    "w4tCss6343a30b",
    "\"",
    ">",
    "<",
    "a",
    " ",
    "class",
    "=\"",
    "menuButton",
    "\"",
    ">",
    "File",
    "&nbsp;",
    "<",
    "input",
    " ",
    "type",
    "=\"",
    "image",
    "\"",
    " ",
    "src",
    "=\"",
    "resources/images/submitter.gif",
    "\"",
    " ",
    "name",
    "=\"",
    "waep4",
    "\"",
    " ",
    "border",
    "=\"",
    "0",
    "\"",
    " />",
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
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_42 {

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
    "w4tCss6343a30b",
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
    "</",
    "div",
    ">"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_41 {

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
    "w4tCss6343a30b",
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

class Render_46_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:visible;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_45_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:visible;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_44_css {

  private static String[] res = new String[] {
    "org.eclipse.rap.types.CssClass [ .w4tCss6343a30b, background-color:#ffffff;font-family:arial,verdana;font-size:1pt;color:#000000;text-align:left;border:0px solid;padding:4px;width:100%; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem:hover, background-color:#ffffff;color:#ff9a00; ]",
    "org.eclipse.rap.types.CssClass [ .menu, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;text-align:left;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;padding:0px;position:absolute;visibility:visible;z-index:99999; ]",
    "org.eclipse.rap.types.CssClass [ .menuItem, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;margin:0px;padding:2px 12px 2px 12px;cursor:default;display:block;white-space:nowrap; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton, font-family:arial,verdana;font-size:8pt;color:#000000;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButtonActive, .menuButtonActive:hover, background-color:#ffffff;font-family:arial,verdana;font-size:8pt;color:#ff9a00;text-decoration:none;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .disabledMenuButton, font-family:arial,verdana;font-size:8pt;color:#808080;text-decoration:none;border:1px solid;border-color:#ffffff #ffffff #ffffff #ffffff;margin:1px;padding:2px 6px 2px 6px;position:relative;cursor:default; ]",
    "org.eclipse.rap.types.CssClass [ .menuButton:hover, color:#000000;border:1px solid;border-color:#d6d3d6 #d6d3d6 #d6d3d6 #d6d3d6; ]"
  };

  static String[] getRes() {
    return res;
  }
}

