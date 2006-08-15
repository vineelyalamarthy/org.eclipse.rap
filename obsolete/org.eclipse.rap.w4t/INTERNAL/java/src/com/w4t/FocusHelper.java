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
 * <p>Utility class to set and query the focus of a component.</p>
 */
final class FocusHelper {
  
  private FocusHelper() {
  }
  
  public static void setFocus( final IFocusable focusable, 
                               final boolean focus )
  {
    WebComponent component = ( WebComponent )focusable;
    WebForm webForm = component.getWebForm();
    if( webForm != null ) {
      if( focus ) {
        webForm.setFocusID( component.getUniqueID() );
      } else {
        if( webForm.retrieveFocusID().equals( component.getUniqueID() ) ) {
          webForm.setFocusID( "" );
        }
      }
    }
  }
  
  public static boolean hasFocus( final IFocusable focusable ) {
    WebComponent component = ( WebComponent )focusable;
    WebForm webForm = component.getWebForm();
    return    webForm != null 
           && component.getUniqueID().equals( webForm.retrieveFocusID() );
  }
  
}
