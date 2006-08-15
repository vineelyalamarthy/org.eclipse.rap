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
import java.util.Vector;
import com.w4t.WebForm;

/**
 * A bean encapsulation, which contains attributes for the code generation
 */
public interface BeanHandle {
  
  /** returns the count of events of the handles bean */
  int getEventCount();
  
  /**
   * clears the list of WebForms to refresh. Each Form in the list will be 
   * rendered, after a itemStateChange event has occured at the editors 
   * WebComponent representation. Also the refresh could be pushed
   * by calling {@link #refreshForms() refreshForms}
   */
  void clearFormToRefreshList();

  /** refresh of the beans property values */
  void refreshProperties() throws Exception;  
  
  /**
   * returns a String array with the propertyNames of the encapsulated
   * bean, sorted alphabetically
   */
  String[] getPropertyNames();
  
  /**
   * returns the PropertyHandle specified by the properties name
   */
  PropertyHandle getPropertyHandle( String propertyName );
  
  /**
   * returns a String array with the eventNames of the encapsulated
   * bean, sorted alphabetically
   */
  String[] getEventNames();  

  /** sets the Object, which is encapsulated by this BeanHandle */
  void setBean( Object bean );

  /** sets the Object, which is encapsulated by this BeanHandle */
  Object getBean();

  /** sets the variable name of the encapsulated Object */
  void setBeanName( String beanName );

  /** gets the variable name of the encapsulated Object */
  String getBeanName();

  /** sets the full qualified class name of the encapsulated Object */
  void setClassName( String className );

  /** gets the full qualified class name of the encapsulated Object */
  String getClassName();

  /**
   * if the bean is added to a WebForm during this
   * developer session this is should be set to true
   */
  void setNewBean( boolean newBean );

  /**
   * if the bean is added to a WebForm during this
   * developer session this returns true
   */
  boolean isNewBean();

  /** true, if the current bean is an element of the edited WebForm */
  void setFormElement( boolean formElement );

  /** true, if the current bean is an element of the edited WebForm */
  boolean isFormElement();

  /** returns the count of properties of the current bean */
  int getPropertyCount();

  
  int getEventMethodCount();


  /** sets, if the bean is actually used in the developed WebComponent */
  void setUsed( boolean used ) throws Exception;

  /** gets, if the bean is actually used in the developed WebComponent */
  boolean isUsed();

  /**
   * adds a WebForm to a list of WebForms to refresh. Each Form in
   * the list will be rendered, after a itemStateChange event has occured at
   * the editors WebComponent representation. Also the refresh could be pushed
   * by calling {@link #refreshForms() refreshForms}
   */
  void addFormToRefresh( WebForm formToRefresh );

  /**
   * removes a WebForm from a list of WebForms to refresh. Each Form in the 
   * list will be rendered, after a itemStateChange event has occured at
   * the editors WebComponent representation. Also the refresh could be pushed
   * by calling {@link #refreshForms() refreshForms}
   */
  void removeFormToRefresh( WebForm formToRefresh );

  /** enforces the rendering of the WebForms in the refreshList */
  void refreshForms();

  /**
   * used if this is a property BeanHandle. The parent beanHandle references the
   * component to which the property belongs
   */
  void setParent( BeanHandle parent );

  /**
   * used if this is a property BeanHandle. Returns a reference to the
   * component to which the property belongs
   */
  BeanHandle getParent();

  /**
   * the Object reference to  the value of the bean, used in case
   * of indexed properties
   */
  void setValue( Object value );

  /**
   * the Object reference to the value of the bean, used in case
   * of indexed properties
   */
  Object getValue();

  /**
   * used during code generation for adding a component to its container,
   * if the BeanHandles component is a WebComponent
   */
  void setConstraint( Object constraint );

  /**
   * used during code generation for adding a component to its container,
   * if the BeanHandles component is a WebComponent
   */
  Object getConstraint();
  
  /**
   * sets a flag which tells, if one of the bean properties has been changed
   */
  void setChanged( boolean changed );

  /**
   * gets the flag which tells, if one of the bean properties has been changed
   */
  boolean isChanged();
  
  /** gets the DeveloperBase instance this BeanHandle is added to */
  DeveloperBase getDeveloperBase();
  
  /** gets the properties value */
  Object getValue( Object bean, PropertyDescriptor propertyDescriptor );

  /** sets the properties value */
  void setValue( Object bean,
                 PropertyDescriptor propertyDescriptor,
                 Object argument );

  Vector getEventMethodNames( String event );    
  
  /** retrieve the WebPropertyEditor for the specified PropertyDescriptor */
  WebPropertyEditor getPropertyEditor( PropertyDescriptor property );
  
  void changeName( String newName );
  
}