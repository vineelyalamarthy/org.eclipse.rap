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

import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import com.w4t.*;
import com.w4t.dhtml.Node;
import com.w4t.dhtml.menustyle.MenuProperties;
import com.w4t.event.IEventAdapter;
import com.w4t.types.WebPropertyBase;

/**
 * <p><code>AbstractHashCodeBuilder</code> implementation which uses all public 
 * read- and writable (bean-style) properties to compute the hash code.</p>
 */
public final class DefaultHashCodeBuilder implements HashCodeBuilder {

  private static final String RENDER_PROPERTY_FILTER 
      = "RenderPropertyFilter";
  private static final String EXCLUDED_PROPERTY_NAMES 
      = "EXCLUDED_PROPERTY_NAMES";

  private static final Object[] NO_ARGS = new Object[ 0 ];
  
  private BeanInfo beanInfo;
  private final Set excludedPropertyNames = new HashSet();

  public DefaultHashCodeBuilder( final Class clazz ) {
    this.beanInfo = getBeanInfo( clazz );
    collectExcludedPropertyNames( clazz );
  }
  
  
  ////////////////////////
  // AbstractHashCodeBuilder override
  
  public int compute( final HashCodeBuilderSupport support, 
                      final Object object ) 
  {
    checkComputeArgument( object );
    HashCodeCalculator calculator = new HashCodeCalculator();
    // Special handling for non-java-beans style get-property on WebContainer
    // and the non-java-beans style area on its WebLayout
    if ( object instanceof WebContainer ) {
      WebContainer container = ( WebContainer )object;
      calculator.append( container );
      WebLayout webLayout = container.getWebLayout();
      int count = container.getWebComponentCount();
      for( int i = 0; i < count; i++ ) {
        Object constraint = container.getConstraint( i );
        if( constraint != null ) {
          Area area = webLayout.getArea( constraint );
          if( area == null && constraint instanceof String ) {
            constraint = RenderUtil.resolve( ( String )constraint );
          }
          area = webLayout.getArea( constraint );
          if( area != null ) {
            DefaultHashCodeBuilder builder 
              = new DefaultHashCodeBuilder( area.getClass() );
            calculator.append( builder.compute( support, area ) );
          }
        }
      }
    }
    
    // in case that a listener was added/removed to the WebComponent/
    // WebLayout etc. rendering may be necessary
    if( object instanceof Adaptable ) {
      Adaptable adaptable = ( Adaptable )object;
      Class clz = IEventAdapter.class;
      IEventAdapter adapter = ( IEventAdapter )adaptable.getAdapter( clz );
      if( adapter != null ) {
        calculator.append( adapter.getListener() );
      }
    }

    // standard java-beans style properties
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    for( int i = 0; i < descriptors.length; i++ ) {
      if(    !isExcludedProperty( descriptors[ i ].getName() ) 
          && descriptors[ i ].getReadMethod() != null
          && descriptors[ i ].getWriteMethod() != null ) 
      {
        Object propertyValue = getPropertyValue( object, descriptors[ i ] );
        internalCompute( support, calculator, propertyValue );
      }
    }
    return calculator.toHashCode();
  }
  
  /////////////////////////////
  // Private helper methods

  private static void internalCompute( final HashCodeBuilderSupport support, 
                                       final HashCodeCalculator calculator, 
                                       final Object value )
  {
    if( value != null && HashCodeBuilderFactory.isW4TClass( value.getClass() ) ) 
    {
      if( !support.isInRecursionList( value ) ) {
        if( canCauseRecursion( value ) ) 
        {
          support.addToRecursionList( value );
        }
        HashCodeBuilder nestedBuilder;
        nestedBuilder = HashCodeBuilderFactory.getBuilder( value.getClass() );
        calculator.append( nestedBuilder.compute( support, value ) );
      }
    } else if(    value instanceof WebPropertyBase
               || value instanceof Style
               || value instanceof MenuProperties )
    {
      // TODO: [fappel] check how we could use the common super class here
      calculator.append( value.toString() );
    } else {
      calculator.append( value );
    }
  }


  // /////////////
  // Private helper methods
  
  /** 
   * <p>Returns whether the given <code>component</code> can potentially cause 
   * endless recursion.</p>
   */
  private static boolean canCauseRecursion( final Object component ) {
    return    component instanceof WebContainer
           || component instanceof Decorator 
           || component instanceof Node;
  }
  
  private Object getPropertyValue( final Object object, 
                                   final PropertyDescriptor propertyDescriptor ) 
  {
    Method readMethod = propertyDescriptor.getReadMethod();
    try {
      // In case we have a package private or protected WebComponent, we
      // have to change the access modifier.
      readMethod.setAccessible( true );
      return readMethod.invoke( object, NO_ARGS );
    } catch( Exception e ) {
      String text = "Failed to obtain value of property ''{0}#{1}''.";
      Object[] args = new Object[]{
        beanInfo.getBeanDescriptor().getBeanClass().getName(),
        propertyDescriptor.getName()
      };
      throw new RuntimeException( MessageFormat.format( text, args ), e );
    } 
  }
  
  private boolean isExcludedProperty( final String propertyName ) {
    return excludedPropertyNames.contains( propertyName );
  }
  
  private void checkComputeArgument( final Object object ) {
    Class beanClass = beanInfo.getBeanDescriptor().getBeanClass();
    if( !beanClass.isAssignableFrom( object.getClass() ) ) {
      String text = "The argument ''object'' must be of type ''{0}''.";
      Object[] args = new Object[] { beanClass.getName() };
      throw new IllegalArgumentException( MessageFormat.format( text, args ) );
    }
  }
  
  private void collectExcludedPropertyNames( final Class clazz ) {
    String className = clazz.getName() + RENDER_PROPERTY_FILTER;
    try {
      Class filterClass = Class.forName( className );
      Field field = filterClass.getField( EXCLUDED_PROPERTY_NAMES );
      String[] excluded = ( String[] )field.get( null );
      for( int i = 0; i < excluded.length; i++ ) {
        if( !excludedPropertyNames.contains( excluded[ i ] ) ) {
          excludedPropertyNames.add( excluded[ i ] );
        }
      }
    } catch ( Exception e ) {
      // ignore
    }
    if ( clazz.getSuperclass() != null ) {
      collectExcludedPropertyNames( clazz.getSuperclass() );
    }
  }
  
  private static BeanInfo getBeanInfo( final Class clazz ) {
    ParamCheck.notNull( clazz, "clazz" );
    try {
      return Introspector.getBeanInfo( clazz );
    } catch( IntrospectionException e ) {
      String text
        = "Failed to obtain BeanInfo for class \'{0}\' (Reason: {1}).";
      Object[] args = new Object[] { clazz.getName(), e.getMessage() };
      throw new IllegalArgumentException( MessageFormat.format( text, args ) );
    }
  }

}
