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

import org.eclipse.rwt.internal.*;

import com.w4t.engine.util.W4TModelList;


/** <p>This frees the registry from WebComponents, which are not needed 
  * anymore.</p>
  */
public class RegistrySkimmer extends Thread {

  private static boolean alive = true;  

  
  /** Constructor */
  public RegistrySkimmer() {
    super( "RegistrySkimmer" );
  }

  public void run() {
    while( alive ) {
      try {
        synchronized( this ) {
          wait( getSkimmerFrequency() );    
        }
      } catch( Exception ignored ) {}
      try {
        W4TModelList.getInstance().cleanup();
      } catch( Exception e ) {
        // TODO Exception handling
        e.printStackTrace();
      }
    }
  }
  
  public static void shutdown() {
    RegistrySkimmer.alive = false;
  }
  
  
  // skimming functionality
  /////////////////////////
  
  /** returns the time which could pass between the skimmer scans for 
    * WebForms to close. Default = 150 sec. Minimum = 30 sec. */
  public long getSkimmerFrequency() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    IInitialization initialization = configuration.getInitialization();
    long result = initialization.getSkimmerFrequenzy();
    return ( result < 30000 ) ? 30000 : result;    
  }
}