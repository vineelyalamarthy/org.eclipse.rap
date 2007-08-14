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

import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.event.WebItemEvent;


public class ReadDataUtil {
  
  private ReadDataUtil() {
  }

  public static void applyValue( final WebComponent component ) {
    IInputValueHolder inputValueHolder = ( IInputValueHolder )component; 
    if( inputValueHolder.isUpdatable() ) {
      String oldValue = inputValueHolder.getValue();
      String value = findValue( ( component ).getUniqueID() );
      if( value != null ) {
        if( W4TContext.getBrowser().isScriptEnabled() ) {
          doApplyValueScript( component, value );
        } else {
          doApplyValueNoScript( component, value );
        }
        updateAjaxHashCode( component, oldValue, value );
      }
    }
  }

  public static String findValue( final String name ) {
    String result = ContextProvider.getRequest().getParameter( name );
    return result == null ? null : result.trim();
  }
  
  
  //////////////////
  // helping methods

  private static void updateAjaxHashCode( final WebComponent component, 
                                          final String oldValue, 
                                          final String value )
  {
    if(    !value.equals( oldValue ) 
        && W4TContext.getBrowser().isAjaxEnabled() )
    {
      AjaxStatusUtil.updateHashCode( component );
    }
  }
  
  private static void doApplyValueScript( final WebComponent cmp, 
                                          final String value )
  {
    LifeCycleHelper.applyRequestValue( ( IInputValueHolder )cmp, value );
  }
  
  private static void doApplyValueNoScript( final WebComponent cmp, 
                                            final String value )
  {
    if(    !WebItemEvent.hasListener( cmp )
        || NoscriptUtil.wasSubmitted( cmp ) ) 
    {
      LifeCycleHelper.applyRequestValue( ( IInputValueHolder )cmp, value );
    }
  }
}
