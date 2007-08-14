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

package com.w4t.engine.service;

import java.io.IOException;

import javax.servlet.ServletException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.service.IServiceAdapter;
import org.eclipse.rwt.internal.service.LifeCycleServiceHandler;

import com.w4t.engine.W4TModel;
import com.w4t.engine.W4TModelUtil;

public final class LifeCycleRunner
  implements LifeCycleServiceHandler.ILifeCycleRunner
{

  public void init() {
    W4TModelUtil.initModel();
  }

  public void run() throws ServletException, IOException {
    getServiceAdapter( W4TModel.getInstance() ).execute();
  }

  private IServiceAdapter getServiceAdapter( final Adaptable model ) {
    return ( IServiceAdapter )model.getAdapter( IServiceAdapter.class );
  }
}