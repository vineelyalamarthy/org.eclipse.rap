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


/**
 * <p>Used to compute a hash code from all the properties relevant for 
 * rendering an object.
 * The resulting hash code is used to decide whether the object will be rendered
 * or not.</p>
 * <p>This interface is not intended to be implemented by clients.</p>
 */
public interface HashCodeBuilder {

  /**
   * <p>Recursion-aware version of <code>compute(Object)</code> which should
   * do the actual computation. Don't call this method from 'outside'.</p>
   */
  int compute( HashCodeBuilderSupport support, Object object );
  
}
