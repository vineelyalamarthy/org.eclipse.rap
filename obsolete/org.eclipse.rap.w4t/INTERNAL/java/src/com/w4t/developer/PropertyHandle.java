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
package com.w4t.developer;

import java.beans.PropertyDescriptor;

/**
 * this contains a PropertyEditor for a specified Property and
 * attributes for code generation
 */
public interface PropertyHandle {
  
  void setPropertyName( String propertyName );

  /** gets the name of the encapsulated property */
  String getPropertyName();

  /** sets the WebPropertyEditor used for this property */
  void setEditor( WebPropertyEditor editor );

  /** gets the WebPropertyEditor used for this property */
  WebPropertyEditor getEditor();

  /** sets the defaultValue of this property before any changes where made */
  void setDefaultValue( Object defaultValue );

  /** gets the defaultValue of this property before any changes where made */
  Object getDefaultValue();

  /** checks if the actual property value differs to the defaultValue*/
  boolean isChanged();

  /** set the changed property true, if the actual property value was changed */
  void setChanged( boolean changed );

  /** sets the PropertyDescriptor of the encapsulated property */
  void setPropertyDescriptor( PropertyDescriptor propertyDescriptor );

  /** gets the PropertyDescriptor of the encapsulated property */
  PropertyDescriptor getPropertyDescriptor();

  /**
   * sets the BeanHandle Object for this property,
   * used if the property is not primitiv
   */
  void setBeanHandle( BeanHandle beanHandle );

  /**
   * gets the BeanHandle Object for this property,
   * used if the property is not primitiv
   */
  BeanHandle getBeanHandle();

  /** sets the BeanHandle of the component to which this property belongs */
  void setComponentHandle( BeanHandle componentHandle );
  
  /** gets the BeanHandle of the component to which this property belongs */
  BeanHandle getComponentHandle();
  
}