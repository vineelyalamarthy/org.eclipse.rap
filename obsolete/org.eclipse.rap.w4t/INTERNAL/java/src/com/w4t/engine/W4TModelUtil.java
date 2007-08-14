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
package com.w4t.engine;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.engine.util.W4TModelList;

/**
 * <p>Utility class for the W4TModel.</p>
 * <p>This class is not inteded to be used by clients.</p> 
 */
public final class W4TModelUtil {
  
  private W4TModelUtil() {
    // prevent instantiation
  }

  public static void initModel() {
    HttpSession session = ContextProvider.getRequest().getSession( true );
    if( W4TModelList.getInstance().get( session.getId() ) == null ) {
      W4TModelList.getInstance().add( session, W4TModel.getInstance() );
    }
  }
}
