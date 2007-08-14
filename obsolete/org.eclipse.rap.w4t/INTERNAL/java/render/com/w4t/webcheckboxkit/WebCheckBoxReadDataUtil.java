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
package com.w4t.webcheckboxkit;

import java.text.MessageFormat;

import com.w4t.*;


final class WebCheckBoxReadDataUtil {
  
  private WebCheckBoxReadDataUtil() {
  }

  static void applyValue( final WebCheckBox checkBox ) {
    String id = checkBox.getUniqueID();
    String sendValue = ReadDataUtil.findValue( addCheckBoxControlPrefix( id ) );
    String value = ReadDataUtil.findValue( id );
    if( sendValue != null ) {
      if( sendValue.equals( checkBox.getValCheck() ) ) {
        if( value == null ) {
          LifeCycleHelper.applyRequestValue( checkBox, 
                                             checkBox.getValUnCheck() );
        }
      } else if( sendValue.equals( checkBox.getValUnCheck() ) ) {
        if( value != null ) {
          LifeCycleHelper.applyRequestValue( checkBox, checkBox.getValCheck() );
        }
      } else {
        String msg =   "The checkbox value control field does not contain "
                     + "a valid value: {0}.";
        Object[] param = new Object[]{ sendValue };
        throw new IllegalStateException( MessageFormat.format( msg, param ) );
      }
    }
  }
  
  static String addCheckBoxControlPrefix( final String name ) {
    return WebCheckBoxRenderer.PREFIX + name;
  }
}
