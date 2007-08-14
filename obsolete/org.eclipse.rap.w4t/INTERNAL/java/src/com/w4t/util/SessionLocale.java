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
package com.w4t.util;

import java.util.Locale;

import org.eclipse.rwt.SessionSingletonBase;

/** <p>Stores a Locale object that can be set via {@link 
  * org.eclipse.rwt.W4TContext#setLocale(Locale) W4TContext.setLocale(Locale)}.</p>
  */
public class SessionLocale extends SessionSingletonBase {

  private Locale privLocale;
  
  private static SessionLocale getInstance() {
    return ( SessionLocale )getInstance( SessionLocale.class ); 
  }
  
  public static boolean isSet() {
    return getInstance().privLocale != null;
  }

  public static void set( final Locale locale ) {
    getInstance().privLocale = locale;
  } 

  public static Locale get() {
    return getInstance().privLocale;
  }
}
