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
import com.w4t.engine.classloader.ClassLoaderCache;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.service.*;
import com.w4t.engine.util.W4TModelList;

/**
 * <p>Utility class for the W4TModel.</p>
 * <p>This class is not inteded to be used by clients.</p> 
 */
public final class W4TModelUtil {
  
  private W4TModelUtil() {
    // prevent instantiation
  }

  public static W4TModel getW4TModel() {
    HttpSession session = getRequest().getSession( true );
    W4TModel result = ( W4TModel )session.getAttribute( W4TModel.ID_W4T_MODEL );
    if( result == null ) {
      getStateInfo().setFirstAccess( true );
      if( ClassLoaderCache.getInstance().isBusy() ) {
        throw new EngineBusyException( "No more sessions available!" );
      }
      result = new W4TModel();
      W4TModelList.getInstance().add( session, result );
      session.setAttribute( W4TModel.ID_W4T_MODEL, result );
    }
    getStateInfo().setFirstAccess( isStartupRequested() );
    return result;
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
