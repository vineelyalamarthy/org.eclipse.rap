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
package com.w4t.webcardlayoutkit;

import com.w4t.*;


/** <p>The default noscript renderer for 
  * {@link org.eclipse.rwt.WebCardLayout WebCardLayout}.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebCardLayoutRenderer_Default_Noscript 
  extends WebCardLayoutRenderer_DOM
{
  public void processAction( final WebComponent component ) {
    WebButton[] cardList = getCardList( ( WebContainer )component );
    if( cardList != null ) {
      for( int i = 0; i < cardList.length; i++ ) {
        ProcessActionUtil.processActionPerformedNoScript( cardList[ i ] );      
      }
    }
  }
}