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
package com.w4t.administration.core;

import com.w4t.administration.core.impl.AdminModelFactoryImpl;


/** <p>The factory responsible for creating the adminstration model
 *  objects. These objects provide information about the current state
 *  of a W4Toolkit application.</p>
 * 
 * <p>Usage:
 * <pre>
 *   private IAdminModelFactory factory = IAdminModelFactory.INSTANCE;
 *   private ILicense license = ( ILicense )factory.create( ILicense.class );
 * </pre>
 * </p>
 * 
 * @see ILicense
 * @see IPreloadBuffer
 * @see IReport
 */
public interface IAdminModelFactory {
  
  /** <p>The default implementation of IAdminModelFactory.</p> */
  public static IAdminModelFactory INSTANCE = new AdminModelFactoryImpl();
  
  /** <p>creates an instance of the given type.</p>
   *  
   * @param type one of the currently available W4Toolkit administration info 
   *             top level model types ( ILicense, IPreloadBuffer, IRegistry, 
   *             IJDBCPoolManager ). */
  Object create( Class type );
  
}