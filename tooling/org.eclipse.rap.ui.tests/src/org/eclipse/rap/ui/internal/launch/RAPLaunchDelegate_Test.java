/*******************************************************************************
 * Copyright (c) 2007, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rap.ui.internal.launch;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.rap.ui.tests.Fixture;


public class RAPLaunchDelegate_Test extends TestCase {
  
  /*
   * Make sure that user VM arguments are 'overridden' by those added by the
   * launch delegate.
   * Overriding currently is done by ensuring that the VM arguments added by
   * the launcher come *after* the user VM arguments. 
   */
  public void testGetVMArguments() throws CoreException {
    ILaunchConfigurationWorkingCopy config = Fixture.createRAPLaunchConfig();
    RAPLaunchConfig rapConfig = new RAPLaunchConfig( config );
    RAPLaunchDelegate launchDelegate = new RAPLaunchDelegate();
    // prepare launch configuration
    config.setAttribute( IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
                         "-Dorg.osgi.service.http.port=manually" );
    rapConfig.setUseManualPort( true );
    rapConfig.setPort( 1234 );
    // setup launch configuration
    try {
      launchDelegate.launch( config, null, null, null );
    } catch( Throwable thr ) {
      // ignore any exceptions, the only purpose of the above call is to
      // set the 'config' field of the RAPLaunchDelegate
    }
    String[] arguments = launchDelegate.getVMArguments( config );
    int manualPortIndex
      = indexOf( arguments, "-Dorg.osgi.service.http.port=manually" );
    int autoPortIndex = indexOf( arguments, "-Dorg.osgi.service.http.port=0" );
    assertTrue( manualPortIndex > -1 );
    assertTrue( autoPortIndex > -1 );
    assertTrue( autoPortIndex > manualPortIndex );
  }
  
  /*
   * Make sure that the user specified session timeout value is used, when the
   * "use session timeout" checkbox is selected.
   */
  public void testUseSessionTimeout() throws CoreException {
    ILaunchConfigurationWorkingCopy config = Fixture.createRAPLaunchConfig();
    RAPLaunchConfig rapConfig = new RAPLaunchConfig( config );
    RAPLaunchDelegate launchDelegate = new RAPLaunchDelegate();
    // prepare launch configuration
    rapConfig.setUseSessionTimeout( true );
    rapConfig.setSessionTimeout( 100 );
    // setup launch configuration
    try {
      launchDelegate.launch( config, null, null, null );
    } catch( Throwable thr ) {
      // ignore any exceptions, the only purpose of the above call is to
      // set the 'config' field of the RAPLaunchDelegate
    }
    String[] arguments = launchDelegate.getVMArguments( config );
    int timeoutIndex = indexOf( arguments,
      "-Dorg.eclipse.equinox.http.jetty.context.sessioninactiveinterval=100" );
    assertTrue( timeoutIndex > -1 );
  }

  /*
   * Make sure that the default session timeout value (zero) is used, when the
   * "use session timeout" checkbox is NOT selected.
   */
  public void testDefaultSessionTimeout() throws CoreException {
    ILaunchConfigurationWorkingCopy config = Fixture.createRAPLaunchConfig();
    RAPLaunchConfig rapConfig = new RAPLaunchConfig( config );
    RAPLaunchDelegate launchDelegate = new RAPLaunchDelegate();
    // prepare launch configuration
    rapConfig.setUseSessionTimeout( false );
    rapConfig.setSessionTimeout( 100 );
    // setup launch configuration
    try {
      launchDelegate.launch( config, null, null, null );
    } catch( Throwable thr ) {
      // ignore any exceptions, the only purpose of the above call is to
      // set the 'config' field of the RAPLaunchDelegate
    }
    String[] arguments = launchDelegate.getVMArguments( config );
    String expected
      = "-Dorg.eclipse.equinox.http.jetty.context.sessioninactiveinterval=0";
    int timeoutIndex = indexOf( arguments, expected );
    assertTrue( timeoutIndex > -1 );
  }
  
  private static int indexOf( final String[] strings, final String string ) {
    int result = -1;
    for( int i = 0; result == -1 && i < strings.length; i++ ) {
      if( string.equals( strings[ i ] ) ) {
        result = i;
      }
    }
    return result;
  }
}
