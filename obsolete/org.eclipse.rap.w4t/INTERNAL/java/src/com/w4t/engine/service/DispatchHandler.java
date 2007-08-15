/*******************************************************************************
 * Copyright (c) 2007 Innoopract Informationssysteme GmbH.
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
import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.*;
import org.eclipse.rwt.service.IServiceHandler;



public final class DispatchHandler implements IServiceHandler {

  private static IServiceHandler lifeCycleRequestHandler;
  private static IServiceHandler resourceRequestHandler;
  private static IServiceHandler triggerFormHandler;

  public void service() throws IOException, ServletException {
    if( ServiceManager.isCustomHandler() ) {
      IServiceHandler customHandler = ServiceManager.getCustomHandler();
      customHandler.service();
    } else if( isResourceRequest() ) {
      getResourceRequestHandler().service();
    } else if( isTimeStampTrigger() ) {
      getTriggerFormRequestHandler().service();
    } else {
      getLifeCycleRequestHandler().service();
    }
  }

  private static boolean isTimeStampTrigger() {
    HttpServletRequest request = ContextProvider.getRequest();
    return request.getParameter( RequestParams.REQUEST_TIMESTAMP_NAME ) != null;
  }
  
  private static boolean isResourceRequest() {
    HttpServletRequest request = ContextProvider.getRequest();
    String resource = request.getParameter( RequestParams.RESOURCE );
    return resource != null && !resource.equals( "" );
  }

  // TODO [rh] synchronized missing
  private static IServiceHandler getLifeCycleRequestHandler() {
    if( lifeCycleRequestHandler == null ) {
      lifeCycleRequestHandler = new LifeCycleServiceHandler();
    }
    return lifeCycleRequestHandler;
  }
  
  private synchronized static IServiceHandler getResourceRequestHandler() {
    if( resourceRequestHandler == null ) {
      resourceRequestHandler = new ResourceRequestServiceHandler();
    }
    return resourceRequestHandler;
  }
  
  private synchronized static IServiceHandler getTriggerFormRequestHandler() {
    if( triggerFormHandler == null ) {
      triggerFormHandler = new TimestampRequestServiceHandler();
    }
    return triggerFormHandler;
  }
}
