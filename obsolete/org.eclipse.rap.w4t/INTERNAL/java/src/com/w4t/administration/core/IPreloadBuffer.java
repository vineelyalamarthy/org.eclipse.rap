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

/** <p>Instances of <code>IPreloadBuffer</code> provide information about the 
 *  configuration an the current state of the preloader mechanism build in
 *  W4Toolkit. For more information about the preloader see the online 
 *  documentation</p>
 * 
 *  <p>Use <code>IAdminModelFactory</code> to create an instance of 
 *  ILicense.</p>
 * 
 *  @see IAdminModelFactory
 */
public interface IPreloadBuffer extends IModelElement {

  /** <p>returns the minimum amount of preloaded sessions in the cache.</p> */
  String getMinThreshold();
  /** <p>returns the current size of the buffer.</p> */
  String getSize();
  /** <p>returns the maximum amount of preloaded sessions.</p> */
  String getMaxSize();
}