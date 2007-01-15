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

package org.eclipse.rap.ui.internal.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.w4t.engine.W4TDelegate;
import com.w4t.engine.util.IEngineConfig;


public class RequestHandler extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private final W4TDelegate servlet;

  public RequestHandler() {
    servlet = new W4TDelegate();
  }
  
  public void init( final ServletConfig config ) throws ServletException {
    ServletContext servletContext = config.getServletContext();
    servletContext.setAttribute( IEngineConfig.class.getName(), 
                                 new EngineConfigWrapper() );
    servlet.init( config );
  }
  
  public void service( final HttpServletRequest request, 
                       final HttpServletResponse response )
    throws ServletException, IOException
  {
    servlet.doPost( request, response );
  }
  
  public void destroy() {
    servlet.destroy();
  }
}
