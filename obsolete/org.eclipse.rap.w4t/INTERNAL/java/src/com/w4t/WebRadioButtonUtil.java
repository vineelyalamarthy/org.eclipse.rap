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
package com.w4t;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.util.ComponentTreeVisitor;

/**
 * <p>
 * Utility class that helps working with {@link org.eclipse.rwt.WebRadioButton 
 * <code>WebRadioButton</code>} and {@link org.eclipse.rwt.WebRadioButtonGroup 
 * <code>WebRadioButtonGroup</code>}.
 * </p> 
 */
public final class WebRadioButtonUtil {

  /**
   * <p>
   * Returns the {@link WebRadioButtonGroup <code>WebRadioButtonGroup</code>}
   * the given <code>button</code> belongs to or <code>null</code> if the 
   * radioButton is not associated to any group.
   * </p>
   * @param button - the radioButton whose group should be obtained. Must not 
   * be <code>null</code>.
   * @throws NullPointerException when <code>button</code> is <code>null</code>.
   */
  public static WebRadioButtonGroup findGroup( final WebRadioButton button ) {
    ParamCheck.notNull( button, "button" );
    WebContainer parent = button.getParent();
    WebRadioButtonGroup result = null;
    while( parent != null && result == null ) {
      if( parent instanceof WebRadioButtonGroup ) {
        result = ( WebRadioButtonGroup )parent;
      }
      parent = parent.getParent();
    }
    return result;
  }

  /**
   * <p>
   * Returns all {@link WebRadioButton <code>WebRadioButton</code>s} that 
   * belong to the given <code>group</code>. An empty array is returned if
   * the given group dos not contain any radioButton.  
   * </p> 
   * @param group the group whose radioButtons should be returned. Must not
   * be <code>null</code>.
   * @throws NullPointerException when <code>group</code> is <code>null</code>.
   */
  public static WebRadioButton[] findButtons( final WebRadioButtonGroup group ) 
  {
    ParamCheck.notNull( group, "group" );
    final List buttons = new ArrayList();
    ComponentTreeVisitor.accept( group, new ComponentTreeVisitor() {
      public boolean visit( final WebComponent component ) {
        if( component instanceof WebRadioButton ) {
          buttons.add( component );
        }
        return true;
      }
      public boolean visit( final WebContainer container ) {
        // ignore radioButtons that belong to sub-radioGroups of 'group'
        boolean result;
        if( container != group && container instanceof WebRadioButtonGroup ) {
          result = false;
        } else {
          result = true;
        }
        return result;
      }
    } );
    WebRadioButton[] result = new WebRadioButton[ buttons.size() ];
    buttons.toArray( result );
    return result;
  }

  /**
   * <p>
   * Returns the currently selected {@link WebRadioButton
   * <code>WebRadioButton</code>} of the given <code>group</code>.
   * <code>Null</code> is returned when there is no (selected) radioButton
   * that belongs to the group.  
   * </p> 
   * @param group the group whose selected radioButtons should be returned. Must
   * not be <code>null</code>.
   * @throws NullPointerException when <code>group</code> is <code>null</code>.
   */
  public static WebRadioButton findSelected( final WebRadioButtonGroup group ) {
    ParamCheck.notNull( group, "group" );
    WebRadioButton[] buttons = findButtons( group );
    WebRadioButton result = null;
    for( int i = 0; result == null && i < buttons.length; i++ ) {
      if( group.getValue().equals( buttons[ i ].getValue() ) ) {
        result = buttons[ i ];
      }
    }
    return result;
  }
  
  private WebRadioButtonUtil() {
    // prevent instantiation
  }
}
