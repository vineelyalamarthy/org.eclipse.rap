/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.widgetidgenerator;

import java.util.*;
import java.util.List;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.*;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor;
import org.eclipse.swt.internal.widgets.WidgetTreeVisitor.AllWidgetTreeVisitor;
import org.eclipse.swt.widgets.*;

/**
 * <p>A simple utility class for the generation of custom widget ids. Such
 * custom ids may be useful in stress test scenarios, if the original 
 * ids of the 'same' widgets differ from session to session. This is currently
 * a hardcoded example that fits a certain test scenario. If you want to adjust
 * it for your own purposes change the setWidgetId implementation.</p>
 * 
 * <p>Note that the system property <code>org.eclipse.rwt.enableUITests</code>
 * must be set to true to activate the usage of the generated custom ids.</p>
 * 
 * @see WidgetIdGenerator#calculateAttributeHash(Object[])
 */
public class WidgetIdGenerator implements PhaseListener {
  
  private static final String WIDGET_IDS
    = WidgetIdGenerator.class.getName()+ "#ids";
  private static final long serialVersionUID = 1L;


  //////////////////////////
  // interface PhaseListener

  public void afterPhase( final PhaseEvent event ) {
  }

  public void beforePhase( final PhaseEvent event ) {
    Widget[] newWidgets = collectNewWidgets( Display.getCurrent() );
    for( int i = 0; i < newWidgets.length; i++ ) {
      setWidgetId( newWidgets[ i ] );
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.RENDER;
  }


  //////////////////
  // helping methods

  private void setWidgetId( final Widget widget ) {
    // TODO [fappel]: read the following from a configuration file,
    //                extension point or whatever...
    if( widget instanceof Tree ) {
      Object[] attributes = new Object[] {
        Tree.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof TreeItem ) {
      TreeItem item = ( TreeItem )widget;
      Object[] attributes = new Object[] {
        item.getText(),
        item.getParentItem() == null ? null : item.getParentItem().getText(),
        TreeItem.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof Table ) {
      Object[] attributes = new Object[] {
        Table.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof TableItem ) {
      TableItem item = ( TableItem )widget;
      Object[] attributes = new Object[] {
        item.getText(),
        TableItem.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof org.eclipse.swt.widgets.List ) {
      org.eclipse.swt.widgets.List list
        = ( org.eclipse.swt.widgets.List )widget;
      setCustomId( widget, generateCustomId( list.getItems() ) );
    }
    
    if( widget instanceof Text ) {
      Text text = ( Text )widget;
      Object[] attributes = new Object[] {
        Text.class.getName(),
        text.getText()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof Button ) {
      Button button = ( Button )widget;
      Object[] attributes = new Object[] {
        button.getText(),
        button.getToolTipText(),
        Button.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof ToolItem ) {
      ToolItem item = ( ToolItem )widget;
      Object[] attributes = new Object[]{
        item.getText(),
        item.getToolTipText(),
        ToolItem.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof CTabItem ) {
      CTabItem item = ( CTabItem )widget;
      Object[] attributes = new Object[] {
        item.getText(),
        item.getToolTipText(),
        ToolItem.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof CTabFolder ) {
      CTabFolder folder = ( CTabFolder )widget;
      CTabItem[] items = folder.getItems();
      Object[] attributes = new Object[ items.length * 2 + 1 ];
      for( int i = 0; i < items.length; i++ ) {
        attributes[ i * 2 ] = items[ i ].getText();
        attributes[ i * 2 + 1 ] = items[ i ].getToolTipText();
      }
      attributes[ attributes.length - 1 ] = CTabFolder.class.getName();
      setCustomId( widget, generateCustomId( attributes ) );
    }
    
    if( widget instanceof Menu ) {
      Menu menu = ( Menu )widget;
      MenuItem[] items = menu.getItems();
      Object[] attributes = new Object[ items.length * 2 + 1 ];
      for( int i = 0; i < items.length; i++ ) {
        attributes[ i * 2 ] = items[ i ].getText();
      }
      attributes[ attributes.length - 1 ] = Menu.class.getName();
      setCustomId( widget, generateCustomId( attributes ) );
    }
 
    if( widget instanceof MenuItem ) {
      MenuItem item = ( MenuItem )widget;
      Object[] attributes = new Object[]{
        item.getText(),
        MenuItem.class.getName()
      };
      setCustomId( widget, generateCustomId( attributes ) );
    }
  }

  private void setCustomId( final Widget widget, final String customId ) {
    widget.setData( WidgetUtil.CUSTOM_WIDGET_ID, customId );
  }

  private String generateCustomId( final Object[] attributes ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "G" );
    String id = calculateAttributeHash( attributes ).toString();
    id = id.replace( '-', '_' );
    buffer.append( id );
    return buffer.toString();
  }

  /**
   * Calculates a hash value of the given attribute list by using
   * the {@link Object#hashCode()} method of each list item.
   * 
   * @param attributes <p>A list of objects that describes a widget instance 
   *                   in such a way that calculating a reasonable hash value
   *                   that can be used as custom widget id for stress test
   *                   purposes is possible. Note that such a value should be 
   *                   the same for a certain test scenario after restarting the
   *                   server. Therefore class objects are not a good choice, 
   *                   but the fully qualified name of classes are.</p>
   *                   <p>In case of collisions (per session) a rehashing
   *                   is done automatically.</p> 
   */
  private Integer calculateAttributeHash( final Object[] attributes ) {
    Set hashs = getUsedIds();
    Integer result = new Integer( generateHash( attributes ) );
    while( hashs.contains( result ) ) {
      result = new Integer( generateHash( new Object[]{ result } ) );
    }
    hashs.add( result );
    return result;
  }

  private int generateHash( final Object[] attributes ) {
    int hash = 17;
    for( int i = 0; i < attributes.length; i++ ) {
      if( attributes[ i ] != null ) {
        hash = 37 * hash + attributes[ i ].hashCode();
      } else {
        hash = 37 * hash + 0;
      }
    }
    return hash;
  }

  private Set getUsedIds() {
    ISessionStore sessionStore = RWT.getSessionStore();
    Set result = ( Set )sessionStore.getAttribute( WIDGET_IDS );
    if( result == null ) {
      result = new HashSet();
      sessionStore.setAttribute( WIDGET_IDS, result );
    }
    return result;
  }

  private static Widget[] collectNewWidgets( final Display display ) {
    final List newWidgets = new ArrayList();
    AllWidgetTreeVisitor visitor = new AllWidgetTreeVisitor() {
      public boolean doVisit( final Widget widget ) {
        Object adapter = widget.getAdapter( IWidgetAdapter.class );
        IWidgetAdapter widgetAdapter = ( IWidgetAdapter )adapter;
        if( !widgetAdapter.isInitialized() ) {
          newWidgets.add( widget );
        }
        return true;
      }
    };
    Shell[] shells = display.getShells();
    for( int i = 0; i < shells.length; i++ ) {
      WidgetTreeVisitor.accept( shells[ i ], visitor );
    }
    Widget[] result = new Widget[ newWidgets.size() ];
    newWidgets.toArray( result );
    return result;
  }
}
