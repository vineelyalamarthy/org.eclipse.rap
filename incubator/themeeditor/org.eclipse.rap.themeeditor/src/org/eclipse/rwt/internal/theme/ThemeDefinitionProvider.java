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
package org.eclipse.rwt.internal.theme;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.themeeditor.editor.source.CSSTokenProvider;
import org.eclipse.rap.themeeditor.editor.source.region.IRegionExt;
import org.eclipse.rap.themeeditor.editor.source.region.SelectorRegion;
import org.eclipse.rap.themeeditor.editor.source.region.StateRegion;
import org.eclipse.rap.themeeditor.editor.source.region.StyleRegion;
import org.xml.sax.SAXException;

public class ThemeDefinitionProvider {

  private static final String[] THEME_FILES = {
    "org/eclipse/swt/internal/widgets/widgetkit/Widget.theme.xml",
    "org/eclipse/swt/internal/widgets/buttonkit/Button.theme.xml",
    "org/eclipse/swt/internal/widgets/combokit/Combo.theme.xml",
    "org/eclipse/swt/internal/widgets/coolbarkit/CoolBar.theme.xml",
    "org/eclipse/swt/internal/custom/ctabfolderkit/CTabFolder.theme.xml",
    "org/eclipse/swt/internal/widgets/groupkit/Group.theme.xml",
    "org/eclipse/swt/internal/widgets/labelkit/Label.theme.xml",
    "org/eclipse/swt/internal/widgets/linkkit/Link.theme.xml",
    "org/eclipse/swt/internal/widgets/listkit/List.theme.xml",
    "org/eclipse/swt/internal/widgets/menukit/Menu.theme.xml",
    "org/eclipse/swt/internal/widgets/progressbarkit/ProgressBar.theme.xml",
    "org/eclipse/swt/internal/widgets/shellkit/Shell.theme.xml",
    "org/eclipse/swt/internal/widgets/spinnerkit/Spinner.theme.xml",
    "org/eclipse/swt/internal/widgets/tabfolderkit/TabFolder.theme.xml",
    "org/eclipse/swt/internal/widgets/tablekit/Table.theme.xml",
    "org/eclipse/swt/internal/widgets/textkit/Text.theme.xml",
    "org/eclipse/swt/internal/widgets/toolbarkit/ToolBar.theme.xml",
    "org/eclipse/swt/internal/widgets/treekit/Tree.theme.xml"
  };
  private ThemeDefElementWrapper[] content;
  private Map widgetMap;
  private static ThemeDefinitionProvider provider = new ThemeDefinitionProvider();

  private ThemeDefinitionProvider() {
    widgetMap = new HashMap();
    content = new ThemeDefElementWrapper[ THEME_FILES.length ];
    for( int i = 0; i < THEME_FILES.length; i++ ) {
      ThemeDefElementWrapper wrapper = readFile( THEME_FILES[ i ] );
      content[ i ] = wrapper;
      widgetMap.put( wrapper.element.name, wrapper );
      for( int j = 0; j < wrapper.getChildren().length; j++ ) {
        ThemeDefElementWrapper child = ( ThemeDefElementWrapper )wrapper.getChildren()[ j ];
        widgetMap.put( child.element.name, child );
      }
    }
  }

  public static ThemeDefElementWrapper[] getContent() {
    return provider.content;
  }

  public static String getDescription( final IRegionExt regionExt,
                                       final String content )
  {
    String result = "";
    if( regionExt != null && content != null ) {
      ThemeDefElementWrapper wrapper = getElementWrapper( regionExt );
      if( wrapper == null ) {
        wrapper = ( ThemeDefElementWrapper )provider.widgetMap.get( content );
      }
      if( wrapper != null ) {
        String text;
        switch( regionExt.getTokenType() ) {
          case CSSTokenProvider.SELECTOR_TOKEN:
            result = wrapper.element.description;
          break;
          case CSSTokenProvider.STATE_TOKEN:
            text = ( String )wrapper.element.stateMap.get( content );
            if( text != null ) {
              result = text;
            } else {
              result = "This state is not supported for "
                       + wrapper.element.name
                       + " widgets.";
            }
          break;
          case CSSTokenProvider.STYLE_TOKEN:
            text = ( String )wrapper.element.styleMap.get( content );
            if( text != null ) {
              result = text;
            } else {
              result = "This style is not supported for "
                       + wrapper.element.name
                       + " widgets.";
            }
          break;
        }
      }
    }
    return result;
  }

  public static String getPropertyDescription( final String widgetName,
                                               final String propertyName )
  {
    String result = null;
    ThemeDefElementWrapper wrapper = ( ThemeDefElementWrapper )provider.widgetMap.get( widgetName );
    if( wrapper != null ) {
      for( int i = 0; i < wrapper.element.properties.length; i++ ) {
        ThemeDefProperty property = wrapper.element.properties[ i ];
        if( property.name.equals( propertyName ) ) {
          result = property.description;
        }
      }
    }
    return result;
  }

  public static ThemeDefElementWrapper getElementWrapper( final IRegionExt regionExt )
  {
    ThemeDefElementWrapper result = null;
    if( regionExt != null ) {
      SelectorRegion parent;
      switch( regionExt.getTokenType() ) {
        case CSSTokenProvider.SELECTOR_TOKEN:
          result = ( ThemeDefElementWrapper )provider.widgetMap.get( regionExt.getContent()
            .trim() );
        break;
        case CSSTokenProvider.STATE_TOKEN:
          parent = ( ( StateRegion )regionExt ).getParentRegion();
          result = ( ThemeDefElementWrapper )provider.widgetMap.get( parent.getContent()
            .trim() );
        break;
        case CSSTokenProvider.STYLE_TOKEN:
          parent = ( ( StyleRegion )regionExt ).getParentRegion();
          result = ( ThemeDefElementWrapper )provider.widgetMap.get( parent.getContent()
            .trim() );
        break;
      }
    }
    return result;
  }

  private ThemeDefElementWrapper readFile( final String filename ) {
    ClassLoader loader = this.getClass().getClassLoader();
    InputStream is = loader.getResourceAsStream( filename );
    NewThemeDefinitionReader reader = new NewThemeDefinitionReader( is, "test" );
    ThemeDefElement[] elements = null;
    try {
      elements = reader.read();
      is.close();
    } catch( SAXException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
    ThemeDefElementWrapper result = new ThemeDefElementWrapper( elements[ 0 ],
                                                                null );
    for( int i = 1; i < elements.length; i++ ) {
      result.addChildElement( new ThemeDefElementWrapper( elements[ i ], result ) );
    }
    return result;
  }

  // TODO [mschaeff] do this only once and store in map
  public static boolean isStyleSupported( final String selector,
                                          final String styleName )
  {
    ThemeDefElementWrapper[] wrappers = getContent();
    for( int i = 0; i < wrappers.length; i++ ) {
      ThemeDefElementWrapper wrapper = wrappers[ i ];
      if( wrapper.element.name.equals( selector ) ) {
        for( int j = 0; j < wrapper.element.properties.length; j++ ) {
          String[] styles = wrapper.element.properties[ j ].styles;
          for( int k = 0; k < styles.length; k++ ) {
            if( styles[ k ].equals( styleName ) ) {
              return true;
            }
          }
        }
        return false;
      } else {
        // check child elements
        for( int m = 0; m < wrapper.getChildren().length; m++ ) {
          ThemeDefElementWrapper child = ( ThemeDefElementWrapper )wrapper.getChildren()[ m ];
          if( child.element.name.equals( selector ) ) {
            for( int j = 0; j < child.element.properties.length; j++ ) {
              String[] styles = child.element.properties[ j ].styles;
              for( int k = 0; k < styles.length; k++ ) {
                if( styles[ k ].equals( styleName ) ) {
                  return true;
                }
              }
            }
            return false;
          }
        }
      }
    }
    return false;
  }

  // TODO [mschaeff] do this only once and store in map
  // TODO [mschaeff] only supports direct child elements on level 1
  public static boolean isStateSupported( String selector, String stateName ) {
    ThemeDefElementWrapper[] wrappers = getContent();
    for( int i = 0; i < wrappers.length; i++ ) {
      ThemeDefElementWrapper wrapper = wrappers[ i ];
      if( wrapper.element.name.equals( selector ) ) {
        for( int j = 0; j < wrapper.element.properties.length; j++ ) {
          String[] states = wrapper.element.properties[ j ].states;
          for( int k = 0; k < states.length; k++ ) {
            if( states[ k ].equals( stateName ) ) {
              return true;
            }
          }
        }
        return false;
      } else {
        // check child elements
        for( int m = 0; m < wrapper.getChildren().length; m++ ) {
          ThemeDefElementWrapper child = ( ThemeDefElementWrapper )wrapper.getChildren()[ m ];
          if( child.element.name.equals( selector ) ) {
            for( int j = 0; j < child.element.properties.length; j++ ) {
              String[] states = child.element.properties[ j ].states;
              for( int k = 0; k < states.length; k++ ) {
                if( states[ k ].equals( stateName ) ) {
                  return true;
                }
              }
            }
            return false;
          }
        }
      }
    }
    return false;
  }
}
