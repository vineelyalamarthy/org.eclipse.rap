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

import com.w4t.WebForm;

/**
 * interface for information exchange between the component to edit
 * and the designers core.
 */
public interface BaseInspector {

  /** gets the common datastructure for the developer used in all forms */
  DeveloperBase getDeveloperBase();

  /** sets the WebForm to edit */
  void setDesignForm( WebForm designer );

  /** gets the WebForm to edit */
  WebForm getDesignForm();

  /** sets the bean encapsulation to edit its properties */
  void setBeanHandle( BeanHandle beanHandle );

  /** gets the bean encapsulation, which properties edited by this Introspector
   */
  BeanHandle getBeanHandle();
  
  /** refresh the inspectors WebForm representation */
  void refreshWindow();
}