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

/** <p>The renderer for org.eclipse.rap.dhtml.MenuBar on IE 5 and higher with
  * javascript support.</p>
  */
public class MenuBarRenderer_Ie5up_Script extends MenuBarRenderer {

  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedScript( component );
  }
  
  protected void useJSLibrary() throws IOException {
    MenuBarUtil.useJSLibrary( MenuBarUtil.JS_IE );
  }
}