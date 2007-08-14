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

import org.eclipse.rwt.SessionSingletonBase;

/**
  * <p>This is a helping class for the WebComponentApplication to create unique
  * keys for every single WebComponent</p>
  */
public class WebComponentCounter extends SessionSingletonBase {

  /** the WebComponent counter */
  private int count;

  private WebComponentCounter() {
    count = 0;
  }

  /**
    * Returns the session singleton WebComponentCounter instance.
    */
  public static WebComponentCounter getInstance() {
    return ( WebComponentCounter )getInstance( WebComponentCounter.class );
  }

  /**
    * returns the unique key for the WebComponent
    * @return a String, which contains the unique key for the WebComponent
    */
  public synchronized String getNewID() {
    count++;
    StringBuffer result = new StringBuffer( "p" );
    result.append( count );
    return result.toString();
  }
}

