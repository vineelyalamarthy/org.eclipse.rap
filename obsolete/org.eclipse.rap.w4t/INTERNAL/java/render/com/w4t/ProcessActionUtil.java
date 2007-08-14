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
package com.w4t;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.event.*;


public class ProcessActionUtil {
  
  private ProcessActionUtil() {
  }

  public static void processActionPerformedScript( final WebComponent cmp ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String id = request.getParameter( WebActionEvent.FIELD_NAME );
    if( cmp.getUniqueID().equals( id ) ) {
      doProcessActionPerformed( cmp );
    }
  }
  
  public static void processActionPerformedNoScript( final WebComponent cmp ) {
    StringBuffer key = new StringBuffer( WebActionEvent.PREFIX );
    key.append( cmp.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      doProcessActionPerformed( cmp );
    }
  }

  public static void processItemStateChangedScript( final WebComponent cmp ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String id = request.getParameter( WebItemEvent.FIELD_NAME );
    if( cmp.getUniqueID().equals( id ) ) {
      doItemStateChanged( cmp );
    }
  }
  
  public static void processItemStateChangedNoScript( final WebComponent cmp ) {
    StringBuffer key = new StringBuffer( WebItemEvent.PREFIX );
    key.append( cmp.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      doItemStateChanged( cmp );
    }    
  }
  
  public static void processFocusGained( final WebComponent cmp ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String id = request.getParameter( WebFocusGainedEvent.FIELD_NAME );
    if( cmp.getUniqueID().equals( id ) ) {
      doFocusGained( cmp );
    }
  }
  
  
  //////////////////
  // helping methods
  
  private static void doProcessActionPerformed( final WebComponent cmp ) {
    int evtId = WebActionEvent.ACTION_PERFORMED;
    WebActionEvent evt = new WebActionEvent( cmp, evtId );
    evt.processEvent();
  }

  
  private static void doItemStateChanged( final WebComponent component ) {
    int evtId = WebItemEvent.ITEM_STATE_CHANGED;
    WebItemEvent evt = new WebItemEvent( component, evtId );
    evt.processEvent();
  }
  
  private static void doFocusGained( final WebComponent cmp ) {
    int evtId = WebFocusGainedEvent.FOCUS_GAINED;
    WebFocusGainedEvent evt = new WebFocusGainedEvent( cmp, evtId );
    evt.processEvent();
  }
}