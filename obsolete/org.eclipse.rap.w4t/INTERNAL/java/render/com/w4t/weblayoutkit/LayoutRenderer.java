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
package com.w4t.weblayoutkit;

import com.w4t.*;

/** <p>the superclass for all renderers that render implementations of 
  * org.eclipse.rap.WebLayout.</p>
  */
public abstract class LayoutRenderer extends Renderer {
  
  public void scheduleRendering( final WebComponent component ) {
    WebContainer container = ( WebContainer )component;
    for( int i = 0; i < container.getWebComponentCount(); i++ ) {
      if( container.get( i ).isVisible() ) {
        getRenderingSchedule().schedule( container.get( i ) );
      }
    }
  }
}
