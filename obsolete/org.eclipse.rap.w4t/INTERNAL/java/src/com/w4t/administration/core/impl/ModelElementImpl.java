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
package com.w4t.administration.core.impl;

import org.eclipse.rwt.Adaptable;

import com.w4t.W4TContext;
import com.w4t.administration.core.IModelElement;


class ModelElementImpl implements Adaptable, IModelElement {

  public Object getAdapter( final Class adapter ) {
    return W4TContext.getAdapterManager().getAdapter( this, adapter );
  }
}
