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
package org.eclipse.rap.ui.themeeditor.scanner.region;


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

}
