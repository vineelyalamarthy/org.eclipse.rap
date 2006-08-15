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

/**
 * <p>Typesafe enumeration which specifies browser capabilities.</p> 
 */
// TODO [rh] use this enum throughout the Browser class and its subclasses
public final class BrowserCapability {
  
  public static final BrowserCapability NO_SCRIPT 
    = new BrowserCapability( "no_script" );
  public static final BrowserCapability SCRIPT 
    = new BrowserCapability( "script" );
  public static final BrowserCapability AJAX 
    = new BrowserCapability( "ajax" );
  
  private final String name;
  
  private BrowserCapability( final String name ) {
    this.name = name;
  }
  
  public String toString() {
    return name;
  }
}
