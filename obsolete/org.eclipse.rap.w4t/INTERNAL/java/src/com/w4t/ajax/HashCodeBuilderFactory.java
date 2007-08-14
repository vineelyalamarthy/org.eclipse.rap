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
package com.w4t.ajax;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.WebCardLayout;
import com.w4t.WebObject;
import com.w4t.types.TabConfig;

public final class HashCodeBuilderFactory {

  
  /** <p>Holds all registered hashCode builders.</p>
   * </p>Key: Class for which the builder (stored in value) can compute a 
   * hashCode; value: IHashCodeBuilder</p> */
  private static final Map registeredBuilders = new HashMap();
  
  private HashCodeBuilderFactory() {
    // prevent instantiation
  }

  /**
   * <p>Obtains the <code>IHashCodeBuilder</code> for the given <code>clazz
   * </code>. If there is no concrete builder registered for the given class,
   * the method will try to find a builder for the super-class. If there is
   * no builder registered at all a <code>DefaultHashCodeBuilder</code> for
   * the given <code>clazz</code> will be registered and returned</p>
   * @param clazz - the class to obtain a builder for. Must not be 
   * <code>null</code>.
   * @return the registered <code>IHashCodeBuilder</code> or a 
   * <code>DefaultHashCodeBuilder</code> if none was registered explicitly.
   * @throws NullPointerException if <code>clazz</code> is <code>null</code>.
   */
  public static HashCodeBuilder getBuilder( final Class clazz ) {
    ParamCheck.notNull( clazz, "clazz" );
    synchronized( HashCodeBuilderFactory.class ) {
      HashCodeBuilder builder = findBuilder( clazz );
      if ( builder == null ) {
        builder = new DefaultHashCodeBuilder( clazz );
        registerBuilder( clazz, builder );
      }
      return builder;
    }
  }
  
  /**
   * <p>Registers the given <code>builder</code> as the 
   * <code>IHashCodeBuilder</code> for the given <code>clazz</code>. A
   * possibly previoulsy registered builder will be overridden.</p>
   */
  public static void registerBuilder( final Class clazz, 
                                      final HashCodeBuilder builder ) 
  {
    ParamCheck.notNull( clazz, "clazz" );
    ParamCheck.notNull( builder, "builder" );
    if ( !isW4TClass( clazz ) ) {
      String text = "The argument 'clazz' ({0}) must specify a W4Toolkit " 
                  + "class type.";
      String msg = MessageFormat.format( text, new Object[] { clazz }  );
      throw new IllegalArgumentException( msg );
    }
    synchronized( HashCodeBuilderFactory.class ) {
      registeredBuilders.put( clazz, builder );
    }
  }
  
  //////////////////////////
  // Private helper methods
  
  static boolean isW4TClass( final Class clazz ) {
    // FIXME [fappel] replace this by reading "W4T-classes" from an xml file
    // FIXME [rh] find a general solution for layout hash-code calculation
    return    WebObject.class.isAssignableFrom( clazz )
           || TabConfig.class.isAssignableFrom( clazz )
           || WebCardLayout.class.isAssignableFrom( clazz );
  }

  private static HashCodeBuilder findBuilder( final Class clazz ) {
    HashCodeBuilder result;
    result = ( HashCodeBuilder )registeredBuilders.get( clazz );
    return result;
  }
}
