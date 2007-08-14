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
import java.math.BigDecimal;
import com.w4t.ProcessActionUtil;
import com.w4t.WebComponent;
import com.w4t.dhtml.MenuBar;
import com.w4t.dhtml.menustyle.MenuBarStyle;

/** 
 * <p>The renderer for org.eclipse.rap.dhtml.MenuBar on Mozilla 1.6 
 * and higher with javascript support.</p>
 */
public class MenuBarRenderer_Mozilla1_6up_Script extends MenuBarRenderer {  
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedScript( component );
  }
  
  protected void useJSLibrary() throws IOException {
    MenuBarUtil.useJSLibrary( MenuBarUtil.JS_NAV6 );
  }
  
  String createStyleContent( final MenuBar menuBar ) {
    MenuBarStyle style = getStyle( menuBar );
    String result = style.toString();
    if( !style.getWidth().endsWith( "%" ) ) {
      String widthBuffer = style.getWidth();
      BigDecimal oldWidth = new BigDecimal( widthBuffer );
      BigDecimal factor = new BigDecimal( 0.982111 );
      BigDecimal newWidth = oldWidth.multiply( factor );
      newWidth.setScale( 0, BigDecimal.ROUND_HALF_UP );
      style.setWidth( String.valueOf( newWidth.intValue() ) );
      result = style.toString();
      style.setWidth( widthBuffer );
    }
    return result;
  } 
}