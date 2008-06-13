// Created on 13.06.2008
package org.eclipse.rap.widgetidgenerator;

import java.util.*;
import java.util.List;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.*;
import org.eclipse.rwt.service.ISessionStore;
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
      Object[] attributes = new Object[] {
        item.getText(),
        item.getToolTipText(),
        ToolItem.class.getName()
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
    int hash = 17;
    for( int i = 0; i < attributes.length; i++ ) {
      if( attributes[ i ] != null ) {
        hash = 37 * hash + attributes[ i ].hashCode();
      } else {
        hash = 37 * hash + 0;
      }
    }
    Set hashs = getUsedIds();
    Integer result = new Integer( hash );
    if( hashs.contains( result ) ) {
      result = calculateAttributeHash( new Object[] { result } );
    }
    hashs.add( result );
    return result;
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
