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
package com.w4t.engine.lifecycle;

import com.w4t.util.*;


/** <p>supplies factory methods for lifecycle managers for various 
  * <code>LifeCycle</code> implementations.</p>
  */
public final class LifeCycleFactory {
  
  private static LifeCycle globalLifeCycle; 
  
  private LifeCycleFactory() {
    // prevent instantiation
  }

  /** <p>returns a LifeCycle object for the specified compatibility mode.</p> */
  public static ILifeCycle loadLifeCycle( ) {
    LifeCycle result = globalLifeCycle;
    if( result == null ) {
      try {
        IConfiguration configuration = ConfigurationReader.getConfiguration();
        IInitialization initialization = configuration.getInitialization();
        String lifeCycleClassName = initialization.getLifeCycle();
        Class lifeCycleClass = Class.forName( lifeCycleClassName );
        result = ( LifeCycle )lifeCycleClass.newInstance();
        if( result.getScope().equals( Scope.APPLICATION ) ) {
          globalLifeCycle = result;
        }
      } catch( Exception ex ) {
        System.out.println( "Could not load lifecycle. " + ex.toString() );
        result = new com.w4t.engine.lifecycle.standard.LifeCycle_Standard();
      }
    }
    return result;
  }
}
