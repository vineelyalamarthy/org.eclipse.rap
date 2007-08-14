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
package com.w4t.dhtml.menukit;

import org.eclipse.rwt.internal.browser.Browser;

import com.w4t.dhtml.Menu;
import com.w4t.dhtml.Point;


/** <p>The renderer for {@link org.eclipse.rwt.custom.CMenu CMenu} on Microsoft
  * Internet Explorer 5 and higher without javascript support.</p>
  */
public class MenuRenderer_Ie5up_Noscript 
  extends MenuRenderer_Mozilla1_6up_Noscript 
{

  Point getAbsolutePosition( final Menu menu ) {
    return menu.getAbsolutePosition( Browser.INTERNETEXPLORER_5_UP );    
  } 
}