/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * All keywords that are supported by RAP, including selectors, styles, states
 * and properties. Used for content assists and problem markers in source tab.
 */
public class SupportedKeywords {

  public static final int UNDEFINED = 0;
  public static final int SELECTOR_TYPE = 1;
  public static final int PROPERTY_TYPE = 2;
  public static final int STYLE_TYPE = 3;
  public static final int STATE_TYPE = 4;
  public static final String[] SELECTORS = {
    "Button",
    "Combo",
    "CoolBar",
    "CoolItem",
    "CTabFolder",
    "CTabItem",
    "Group",
    "Group-Frame",
    "Group-Label",
    "Label",
    "Link",
    "Link-Hyperlink",
    "List",
    "List-Item",
    "Menu",
    "MenuItem",
    "ProgressBar",
    "ProgressBar-Indicator",
    "Shell",
    "Shell-Titlebar",
    "Shell-MinButton",
    "Shell-MaxButton",
    "Shell-CloseButton",
    "Spinner",
    "TabFolder",
    "TabItem",
    "Table",
    "TableItem",
    "TableColumn",
    "TableColumn-SortIndicator",
    "Table-GridLine",
    "Table-Checkbox",
    "Text",
    "ToolBar",
    "ToolItem",
    "ToolTip",
    "Tree",
    "TreeItem",
    "TreeColumn",
    "TreeColumn-SortIndicator",
    "*"
  };
  public static final String[] STYLES = {
    "PUSH", "TOGGLE", "CHECK", "RADIO", "BORDER", "FLAT", "SINGLE", "MULTI"
  };
  public static final String[] STATES = {
    "hover",
    "pressed",
    "up",
    "down",
    "disabled",
    "selected",
    "inactive",
    "maximized",
    "minimized"
  };
  public static final String[] PROPERTIES = {
    "background-color",
    "background-gradient-color",
    "background-image",
    "border",
    "color",
    "font",
    "height",
    "padding",
    "margin",
    "spacing",
    "width",
    "rwt-darkshadow-color",
    "rwt-highlight-color",
    "rwt-lightshadow-color",
    "rwt-selectionmarker-color",
    "rwt-shadow-color",
    "rwt-thinborder-color"
  };
  public static final String[] COLOR_PROPERTIES = {
    "background-color",
    "background-gradient-color",
    "border",
    "color",
    "rwt-darkshadow-color",
    "rwt-highlight-color",
    "rwt-lightshadow-color",
    "rwt-selectionmarker-color",
    "rwt-shadow-color",
    "rwt-thinborder-color"
  };
  private static final String STARTS_WITH = "rwt";
  private static final String ENDS_WITH = "color";

  public static boolean isPropertySupported( final String name ) {
    boolean result = false;
    if( name.startsWith( STARTS_WITH ) && name.endsWith( ENDS_WITH ) ) {
      result = true;
    } else {
      for( int i = 0; i < PROPERTIES.length; i++ ) {
        if( name.equals( PROPERTIES[ i ] ) ) {
          result = true;
        }
      }
    }
    return result;
  }

  public static boolean isSelectorSupported( final String selector ) {
    boolean result = false;
    if( selector == null ) {
      result = true;
    } else {
      for( int i = 0; i < SELECTORS.length; i++ ) {
        if( selector.equals( SELECTORS[ i ] ) ) {
          result = true;
        }
      }
    }
    return result;
  }

  public static boolean isStyleSupported( final String style ) {
    boolean result = false;
    if( style == null ) {
      result = true;
    } else {
      for( int i = 0; i < STYLES.length; i++ ) {
        if( style.equals( STYLES[ i ] ) ) {
          result = true;
        }
      }
    }
    return result;
  }

  public static boolean isStateSupported( final String state ) {
    boolean result = false;
    if( state == null ) {
      result = true;
    } else {
      for( int i = 0; i < STATES.length; i++ ) {
        if( state.equals( STATES[ i ] ) ) {
          result = true;
        }
      }
    }
    return result;
  }

  public static String[] getKeywordsStartWith( final String start,
                                               final int type )
  {
    String[] result = null;
    String[] keywords = null;
    switch( type ) {
      case SELECTOR_TYPE:
        keywords = SELECTORS;
      break;
      case PROPERTY_TYPE:
        keywords = PROPERTIES;
      break;
      case STYLE_TYPE:
        keywords = STYLES;
      break;
      case STATE_TYPE:
        keywords = STATES;
      break;
    }
    if( keywords != null && start != null ) {
      List resultList = new ArrayList();
      for( int i = 0; i < keywords.length; i++ ) {
        String item = keywords[ i ];
        if( item.toLowerCase().startsWith( start.toLowerCase() ) ) {
          resultList.add( item );
        }
      }
      result = new String[ resultList.size() ];
      result = ( String[] )resultList.toArray( result );
      Arrays.sort( result );
    }
    return result;
  }
}
