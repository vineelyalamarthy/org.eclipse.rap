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
package com.w4t.engine.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.ServletException;

/** <p>Adapter used for exceptions which occur during the service method of 
 *  the W4TDelegate instance. ServletExceptionAdapters are used instead of 
 *  the ServletExceptions in order to keep the inner exception information.</p> 
 */
public class ServletExceptionAdapter extends ServletException {
  
  private static final long serialVersionUID = 1L;
  
  private Throwable cause;
  
  // FIXME [rh] can we remove JRE 1.3 compatibility?
  public ServletExceptionAdapter( final Throwable cause ) {
    this.cause = cause;
    // Workaround to keep 1.3.x compatibility
    try {
      Method getStack 
        = cause.getClass().getMethod( "getStackTrace", new Class[ 0 ] );
      Object trace = getStack.invoke( cause, new Object[ 0 ] );
      Method[] methods = getClass().getMethods();
      Method setStack = null;
      for( int i = 0; i < methods.length; i++ ) {
        if( methods[ i ].getName().equals( "setStackTrace" ) ) {
          setStack = methods[ i ];
        }
      }      
      setStack.invoke( this, new Object[] { trace } );
    } catch( Exception ignore ) {
      // ignore stacktrace setting on 1.3.x VMs
    }
  }
      
  public synchronized Throwable fillInStackTrace() {
    return super.fillInStackTrace();
  }

  public String getLocalizedMessage() {
    return cause.getLocalizedMessage();
  }

  public String getMessage() {
    return cause.getMessage();
  }

  public void printStackTrace() {
    cause.printStackTrace();
  }

  public void printStackTrace( final PrintStream printStream ) {
    cause.printStackTrace( printStream );
  }

  public void printStackTrace( final PrintWriter printWriter ) {
    cause.printStackTrace( printWriter );
  }

  public String toString() {
    return cause.toString();
  }
}