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


public class JavaScriptUtil {
  
  public final static String JS_SET_FOCUS_ID = "eventHandler.setFocusID(this)";
  public final static String JS_PRINT_PAGE = "windowManager.printPage()";
  public final static String JS_RESET_FORM = "eventHandler.resetForm()";
  public final static String JS_RESUME_SUBMIT = "eventHandler.resumeSubmit()";
  public final static String JS_SUSPEND_SUBMIT = "eventHandler.suspendSubmit()";

  private JavaScriptUtil() {
  }
}
