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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
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
    HttpSession session = getRequest().getSession( true );
    if( W4TModelList.getInstance().get( session.getId() ) == null ) {
      getStateInfo().setFirstAccess( true );
      W4TModelList.getInstance().add( session, W4TModel.getInstance() );
    }
    getStateInfo().setFirstAccess( isStartupRequested() );
  }
  
  private static boolean isStartupRequested() {
    String paramStartup = getRequest().getParameter( RequestParams.STARTUP );
    return paramStartup != null && paramStartup.equalsIgnoreCase( "true" );
  }

  private static IServiceStateInfo getStateInfo() {
    return ContextProvider.getStateInfo();
  }

  private static HttpServletRequest getRequest() {
    return ContextProvider.getRequest();
  }
}
