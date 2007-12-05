/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.forms;

import org.eclipse.rwt.RWT;

public class Messages {

  private static final String BUNDLE_NAME = "org.eclipse.ui.internal.forms.Messages"; //$NON-NLS-1$

  private Messages() {
  }

  public static Messages get() {
    Class clazz = Messages.class;
    return ( Messages )RWT.NLS.getISO8859_1Encoded( BUNDLE_NAME, clazz );
  }
  public String FormDialog_defaultTitle;
  public String FormText_copy;
  public String Form_tooltip_minimize;
  public String Form_tooltip_restore;
  /*
   * Message manager
   */
  public String MessageManager_sMessageSummary;
  public String MessageManager_sWarningSummary;
  public String MessageManager_sErrorSummary;
  public String MessageManager_pMessageSummary;
  public String MessageManager_pWarningSummary;
  public String MessageManager_pErrorSummary;
  public String ToggleHyperlink_accessibleColumn;
  public String ToggleHyperlink_accessibleName;		
}
