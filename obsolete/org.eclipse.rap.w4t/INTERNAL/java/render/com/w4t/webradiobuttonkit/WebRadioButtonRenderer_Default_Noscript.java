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
import com.w4t.event.WebItemEvent;


/** <p>The default noscript renderer for org.eclipse.rap.WebRadioButton.</p>
  *
  * <p>The default noscript renderer is non-browser-specific and implements 
  * functionality in a way that runs on browsers that do not implement or 
  * permit javascript execution.</p>
  */
public class WebRadioButtonRenderer_Default_Noscript 
  extends Renderer
{
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processFocusGained( component );
  }

  public void render( final WebComponent component ) throws IOException {
    WebRadioButton radioButton = ( WebRadioButton )component;
    HtmlResponseWriter writer = getResponseWriter();
    writer.startElement( HTML.INPUT, null );
    writer.writeAttribute( HTML.TYPE, HTML.RADIO, null );
    writer.writeAttribute( HTML.STYLE, "vertical-align:middle", null );
    RadioButtonUtil.renderName( writer, radioButton );
    RadioButtonUtil.renderChecked( radioButton );
    RadioButtonUtil.renderDisabled( writer, radioButton );
    String value = RadioButtonUtil.getValue( radioButton );
    writer.writeAttribute( HTML.VALUE, value, null );
    writer.endElement( HTML.INPUT );
    RadioButtonUtil.renderDisplay( radioButton );
    createSubmitter( radioButton );
  }
  
  private static void createSubmitter( final WebRadioButton radioButton ) 
    throws IOException 
  {
    WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( radioButton );  
    if(    group != null
        && group.isUpdatable()
        && WebItemEvent.hasListener( group ) 
        && radioButton.isEnabled() ) 
    {
      RenderUtil.writeItemSubmitter( group.getUniqueID() );
    }
  }
}