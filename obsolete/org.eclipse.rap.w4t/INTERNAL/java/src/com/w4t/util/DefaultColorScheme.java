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
package com.w4t.util;

import java.io.*;
import java.util.Properties;

import org.eclipse.rwt.internal.ConfigurationReader;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.resources.IResourceManager;



/** <p>Contains the default color scheme used by the W4 Toolkit components.</p>
  */
public class DefaultColorScheme {

  public static final String DEFAULT_COLOR_SCHEME_PROPS
    = "defaultColorScheme.properties";

  private static Properties properties;

    public static final String WEB_OBJECT_BG = "WEB_OBJECT_BG";
    // org.eclipse.rap.WebForm
    public static final String WEB_FORM_TEXT = "WEB_FORM_TEXT";
    // org.eclipse.rap.WebCardLayout
    public static final String CARD_LAYOUT_BORDER = "CARD_LAYOUT_BORDER";
    public static final String CARD_LAYOUT_INACTIVE_TAB 
      = "CARD_LAYOUT_INACTIVE_TAB";
    public static final String CARD_LAYOUT_ACTIVE_TAB_LINK 
      = "CARD_LAYOUT_ACTIVE_TAB_LINK";
    public static final String CARD_LAYOUT_INACTIVE_TAB_LINK 
      = "CARD_LAYOUT_INACTIVE_TAB_LINK";
    // org.eclipse.rap.dhtml
    public static final String ITEM_FONT = "ITEM_FONT";
    public static final String ITEM_DISABLED_FONT = "ITEM_DISABLED_FONT";
    public static final String ITEM_MARKED_FONT = "ITEM_MARKED_FONT";
    public static final String ITEM_MARKED_BG = "ITEM_MARKED_BG";
    // org.eclipse.rap.dhtml.menustyle
    public static final String MENU_BG = "MENU_BG";
    public static final String MENU_BORDER = "MENU_BORDER";
    public static final String MENU_DEFAULT_FONT = "MENU_DEFAULT_FONT";
    public static final String MENU_DISABLED_FONT = "MENU_DISABLED_FONT";
    public static final String MENU_HOVER_FONT = "MENU_HOVER_FONT";
    
    // still old settings!!
    ///////////////////////
    
    // org.eclipse.rap.WebButton
    public static final String BUTTON_DISABLED = "BUTTON_DISABLED";
    // org.eclipse.rap.WebTable
    public static final String TABLE_BG = "TABLE_BG";
    // org.eclipse.rap.WebDataBaseTable
    public static final String DBTABLE_ERR = "DBTABLE_ERR";
    public static final String DBTABLE_HEAD_CELL_BG = "DBTABLE_HEAD_CELL_BG";
    public static final String DBTABLE_TABLE_CELL_BG = "DBTABLE_TABLE_CELL_BG";
    // org.eclipse.rap.Style
    public static final String STYLE_BG = "STYLE_BG";
    public static final String STYLE_FONT = "STYLE_FONT";
    public static final String STYLE_BORDER = "STYLE_BORDER";
    public static final String STYLE_BORDER_TOP = "STYLE_BORDER_TOP";
    public static final String STYLE_BORDER_BOTTOM = "STYLE_BORDER_BOTTOM";
    public static final String STYLE_BORDER_LEFT = "STYLE_BORDER_LEFT";
    public static final String STYLE_BORDER_RIGHT = "STYLE_BORDER_RIGHT";
    // org.eclipse.rap.WebBorderComponent
    public static final String BORDER_COMPONENT_BG = "BORDER_COMPONENT_BG";
    public static final String BORDER_COMPONENT_LIGHT 
      = "BORDER_COMPONENT_LIGHT";
    public static final String BORDER_COMPONENT_DARK = "BORDER_COMPONENT_DARK";
    // org.eclipse.rap.extensions.DesignButton
    public static final String DESIGN_BUTTON_FONT = "DESIGN_BUTTON_FONT";
    // org.eclipse.rap.dhtml
    public static final String ABSOLUTE_LAYOUT_BG = "ABSOLUTE_LAYOUT_BG";
    // org.eclipse.rap.administration 
    public static final String ADMIN_BG = "ADMIN_BG";
    public static final String ADMIN_LINK = "ADMIN_LINK";
    public static final String ADMIN_HEADER_FONT = "ADMIN_HEADER_FONT";
    public static final String ADMIN_MENU = "ADMIN_MENU";
    public static final String ADMIN_CENTER = "ADMIN_CENTER";
    public static final String ADMIN_INACTIVE_TAB = "ADMIN_INACTIVE_TAB";
    public static final String ADMIN_MESSAGE_FORM_BG = "ADMIN_MESSAGE_FORM_BG";
    public static final String ADMIN_LABEL_BG = "ADMIN_LABEL_BG";

  
  public static String get( final String key ) {
    if( properties == null ) {
      init();
    }
    return ( String )properties.get( key );
  }


  // helping methods
  //////////////////

  private static void init() {
    properties = new Properties();
    try {
      if( createFile().exists() ) {
        loadFromFile();
      } else {
        loadFromStream();
      }
      if( properties.isEmpty() ) {
        loadFallBackValues();
      }
    } catch( Exception ex ) {
      loadFallBackValues();
    }
  }

  private static void loadFromStream() throws IOException {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    InputStream is = manager.getResourceAsStream( DEFAULT_COLOR_SCHEME_PROPS );
    try {
      properties.load( is );
    } finally {
      is.close();
    }
  }

  private static void loadFromFile() throws IOException {
    File file = createFile();
    if( file.exists() ) {
      FileInputStream fis = new FileInputStream( file );
      try {
        properties.load( fis );
      } finally {
        fis.close();
      }
    }
  }


  private static File createFile() {
    return new File( createFileName() );
  }

  private static String createFileName() {
    String result =  "not_found";
    if( ConfigurationReader.getEngineConfig() != null ) {
      result =    ConfigurationReader.getEngineConfig().getServerContextDir() 
                + File.separator 
                + "WEB-INF" 
                + File.separator 
                + "conf" 
                + File.separator 
                + DEFAULT_COLOR_SCHEME_PROPS;
    }
    return result;
  }

  private static void loadFallBackValues() {
    System.out.println(   "WARNING: Could not load default color scheme "
                       + "from file:\n"
                       + "         WEB-INF/conf/defaultColorScheme.properties\n"
                       + "         Loading fallback values." );
    
    properties.put( WEB_OBJECT_BG, "#ffffff" );
    // org.eclipse.rap.WebForm
    properties.put( WEB_FORM_TEXT, "#000000" );
    // org.eclipse.rap.WebCardLayout
    properties.put( CARD_LAYOUT_BORDER, "#ff9900" );
    properties.put( CARD_LAYOUT_INACTIVE_TAB, "#e6e3e6" );
    properties.put( CARD_LAYOUT_ACTIVE_TAB_LINK, "#000000" );
    properties.put( CARD_LAYOUT_INACTIVE_TAB_LINK, "#000000" );
    // org.eclipse.rap.dhtml
    properties.put( ITEM_FONT, "#000000" );
    properties.put( ITEM_DISABLED_FONT, "gray" );  
    properties.put( ITEM_MARKED_FONT, "#ffffff" );
    properties.put( ITEM_MARKED_BG, "#C6DBEF" );
    // org.eclipse.rap.dhtml.menustyle
    properties.put( MENU_BG, "#ffffff" );
    properties.put( MENU_BORDER, "#d6d3d6" );
    properties.put( MENU_DEFAULT_FONT, "#000000" );
    properties.put( MENU_DISABLED_FONT, "#808080" );
    properties.put( MENU_HOVER_FONT, "#ff9a00" );
    
    // still old settings!!
    ///////////////////////
    loadOldFallbackSettings();
  }

  private static void loadOldFallbackSettings() {
    // org.eclipse.rap.WebButton
    properties.put( BUTTON_DISABLED, "gray" );
    // org.eclipse.rap.WebTable
    properties.put( TABLE_BG, "" );
    // org.eclipse.rap.WebDataBaseTable
    properties.put( DBTABLE_ERR, "red" );
    properties.put( DBTABLE_HEAD_CELL_BG, "" );
    properties.put( DBTABLE_TABLE_CELL_BG, "" );
    // org.eclipse.rap.Style
    properties.put( STYLE_BG, "" );
    properties.put( STYLE_FONT, "" );
    properties.put( STYLE_BORDER, "" );
    properties.put( STYLE_BORDER_TOP, "" );
    properties.put( STYLE_BORDER_BOTTOM, "" );
    properties.put( STYLE_BORDER_LEFT, "" );
    properties.put( STYLE_BORDER_RIGHT, "" );        
    // org.eclipse.rap.WebBorderComponent
    properties.put( BORDER_COMPONENT_BG, "" ); 
    properties.put( BORDER_COMPONENT_LIGHT, "#ffffff" );
    properties.put( BORDER_COMPONENT_DARK, "#808080" );
    // org.eclipse.rap.extensions.DesignButton
    properties.put( DESIGN_BUTTON_FONT, "#ffffff" );
    // org.eclipse.rap.dhtml
    properties.put( ABSOLUTE_LAYOUT_BG, "" );
    // org.eclipse.rap.administration
    properties.put( ADMIN_BG, "#d6d3ce" );
    properties.put( ADMIN_LINK, "#d6d3ce" );
    properties.put( ADMIN_HEADER_FONT, "#ffffff" );
    properties.put( ADMIN_MENU, "#990000" );
    properties.put( ADMIN_CENTER, "#d8d8d8" );
    properties.put( ADMIN_INACTIVE_TAB, "#404040" );
    properties.put( ADMIN_MESSAGE_FORM_BG, "#000080" );
    properties.put( ADMIN_LABEL_BG, "#ffffff" );
  }
}