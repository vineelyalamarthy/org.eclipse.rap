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
package com.w4t.webradiobuttonkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;


/** <p>The default renderer for org.eclipse.rap.WebRadioButton.</p>
  *
  * <p>The default renderer is non-browser-specific and implements 
  * functionality in a way that runs on the most commonly used browsers.</p>
  */
public class WebRadioButtonRenderer_Default_Ajax 
  extends Renderer
{
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
  }

  public void render( final WebComponent component ) throws IOException {
    if( AjaxStatusUtil.mustRender( component ) ) {
      doRender( getResponseWriter(), ( WebRadioButton )component );
    }
  }

  private static void doRender( final HtmlResponseWriter writer, 
                                final WebRadioButton radioButton )
    throws IOException 
  {
    writer.startElement( HTML.SPAN, null );
    writer.writeAttribute( HTML.ID, radioButton.getUniqueID(), null );
    RadioButtonUtil.renderInputForScript( writer, radioButton );
    RadioButtonUtil.renderDisplay( radioButton );
    writer.endElement( HTML.SPAN );
  }
}
