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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.*;


public abstract class WebCheckBoxRenderer extends Renderer {

  public static final String PREFIX = "cbc";

  public void render( final WebComponent component ) throws IOException {
    WebCheckBox wcb = ( WebCheckBox )component;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    out.writeAttribute( HTML.ID, wcb.getUniqueID(), null );
    out.startElement( HTML.INPUT, null );
    out.writeAttribute( HTML.TYPE, HTML.CHECKBOX, null );
    out.writeAttribute( HTML.NAME, wcb.getUniqueID(), null );
    if( wcb.isSelected() ) {
      out.writeAttribute( HTML.CHECKED, HTML.CHECKED, null );
    }
    RenderUtil.writeReadOnly( wcb );
    RenderUtil.writeDisabled( wcb );
    out.writeAttribute( HTML.VALUE, wcb.getValCheck(), null );
    createEventHandler( wcb );
    createLabelString( wcb );
    createSubmitter( wcb );
    if( wcb.isEnabled() && wcb.isUpdatable() ) {
      createValueControlField( wcb );
    }
    out.endElement( HTML.SPAN );
  }
  
  void createValueControlField( final WebCheckBox wcb ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    StringBuffer id = new StringBuffer();
    id.append( PREFIX );
    id.append( wcb.getUniqueID() );
    HTMLUtil.hiddenInput( out, id.toString(), wcb.getValue() );
  }
    
  abstract void createEventHandler( WebCheckBox wcb ) throws IOException;
  
  abstract void createSubmitter( final WebCheckBox wcb ) throws IOException;

  protected void createLabelString( final WebCheckBox wcb ) throws IOException {
    if( !isEmptyLabel( wcb ) ) {
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.SPAN, null );
      RenderUtil.writeUniversalAttributes( wcb );
      String label = RenderUtil.resolve( wcb.getLabel() );
      out.writeText( label, null );
      out.endElement( HTML.SPAN );
    }
  }
  
  protected static boolean isEmptyLabel( final WebCheckBox wcb ) {
    return wcb.getLabel().equals( "" );
  }
}