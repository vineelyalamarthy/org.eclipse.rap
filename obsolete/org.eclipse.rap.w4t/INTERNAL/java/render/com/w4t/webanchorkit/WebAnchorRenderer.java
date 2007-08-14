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
package com.w4t.webanchorkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;

/** <p>the superclass of all Renderers that render org.eclipse.rap.WebAnchor.</p>
  */
public abstract class WebAnchorRenderer extends DecoratorRenderer {
  
  /** this method will render the opening tag of the anchor */
  protected void createDecoratorHead( final Decorator dec ) throws IOException {
    WebAnchor wac = ( WebAnchor )dec;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.ID, wac.getUniqueID(), null );
    RenderUtil.writeUniversalAttributes( wac );
    if( hasActiveTargetForm( wac ) ) {      
      buildFormHRef( wac.retrieveTargetForm() );
    } else if( hasActiveHRef( wac ) ) {
      buildStandardHRef( wac );
    }
    if( !"".equals( wac.getTarget() ) ) {
      out.writeAttribute( HTML.TARGET, wac.getTarget(), null );
    }
  }
  
  /** this method will render the closing tag of the anchor */
  protected void createDecoratorFoot( final Decorator dec ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.A );
  }  
  
  //////////////////
  // helping methods
  
  private void buildStandardHRef( final WebAnchor wac ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.writeAttribute( HTML.HREF, wac.getHRef(), null );
  }

  private void buildFormHRef( final WebForm target ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.writeAttribute( HTML.HREF, 
                        RenderUtil.createEncodedFormGetURL( target ), 
                        null );
  }
  
  ///////////////
  // conditionals
  
  private boolean hasActiveTargetForm( final WebAnchor wac ) {
    return wac.retrieveTargetForm() != null && wac.isEnabled();
  }
  
  private boolean hasActiveHRef( final WebAnchor wac ) {
    return !wac.getHRef().equals( "" ) && wac.isEnabled();
  }
}