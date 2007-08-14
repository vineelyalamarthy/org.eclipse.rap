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
package com.w4t.dhtml.webscrollpanekit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.WebScrollPane;

/**
 * <p>The renderer for {@link org.eclipse.rwt.dhtml.WebScrollPane WebScrollPane} on 
 * Mozilla 1.6 and higher in AJaX mode.</p>
 */
public class WebScrollPaneRenderer_Mozilla1_6up_Ajax
  extends WebScrollPaneRenderer_DOM_Script
{
  
  public void readData( final WebComponent component ) {
    WebScrollPaneUtil.readDataInAjaxMode( ( WebScrollPane )component );
  }
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    } else {
      Decorator decorator = ( Decorator )component;
      LifeCycleHelper.render( decorator.getContent() );
    }
  }
}