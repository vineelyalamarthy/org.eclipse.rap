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
package com.w4t.webcheckboxkit;

import java.io.IOException;
import com.w4t.WebCheckBox;
import com.w4t.WebComponent;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;


/** <p>The default AJAX-enabled renderer for org.eclipse.rap.WebCheckBox.</p>
  */
public class WebCheckBoxRenderer_Default_Ajax
  extends WebCheckBoxRenderer_Default_Script 
{

  public void readData( WebComponent component ) {
    WebCheckBox webCheckBox = ( WebCheckBox )component;
    boolean oldSelectedState = webCheckBox.isSelected();
    WebCheckBoxReadDataUtil.applyValue( webCheckBox );
    if( oldSelectedState != webCheckBox.isSelected() ) {
      AjaxStatus ajaxStatus 
        = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
      ajaxStatus.updateStatus( true );
    }
  }
  
  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      super.render( component );
    }
  }
}