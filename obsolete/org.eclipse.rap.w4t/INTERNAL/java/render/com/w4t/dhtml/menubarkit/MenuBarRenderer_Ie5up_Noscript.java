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
package com.w4t.dhtml.menubarkit;

import java.io.IOException;
import com.w4t.ProcessActionUtil;
import com.w4t.WebComponent;


/** <p>The renderer for {@link org.eclipse.rwt.dhtml.MenuBar MenuBar} on Microsoft 
  * Internet Explorer 5 and higher without javascript support.</p>
  */
public class MenuBarRenderer_Ie5up_Noscript extends MenuBarRenderer {
  
  protected final static int PADDING_CORRECTION = 2;  

  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }
  
  protected void useJSLibrary() throws IOException {
    // no JavaScript library for Noscript renderer
  }
  
  int getPaddingCorrection() {
    return PADDING_CORRECTION;
  }
}