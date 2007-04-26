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

package org.eclipse.jface.resource;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public class JFaceResources {

  private static final ResourceBundle bundle = ResourceBundle
          .getBundle( "org.eclipse.jface.messages" ); //$NON-NLS-1$

  public static String getString( final String key ) {
    // TODO use resource bundle
    String result;
    try {
      result = bundle.getString( key );
    } catch( MissingResourceException e ) {
      result = key;
    }
    return result;
  }

  public static Font getDialogFont() {
    // TODO [rst] reasonable implementation
    Font result = Font.getFont( "Helvetica", 11, SWT.NORMAL );
    return result;
  }

  public static String format( final String key, final Object[] args ) {
    return MessageFormat.format(getString(key), args);
  }
}
