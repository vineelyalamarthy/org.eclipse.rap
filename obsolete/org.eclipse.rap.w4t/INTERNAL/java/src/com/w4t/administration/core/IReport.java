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


/** <p>This is an encapsulation for convenience of the 
 *  <code>W4TContext.getStatistics</code> result.</p>
 * 
 *  <p>Use <code>IAdminModelFactory</code> to create an instance of 
 *  IReport.</p>
 * 
 *  @see IAdminModelFactory
 * 
 */
public interface IReport extends IModelElement {

  /** <p>returns a report summary of the application's basic infos.</p> */
  String getStatisticSummary();
}