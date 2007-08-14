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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.Decorator;
import com.w4t.DecoratorRenderer;
import com.w4t.dhtml.WebScrollPane;


/**
 * <p>
 * The default noscript renderer for org.eclipse.rap.WebScrollPane.
 * </p>
 * <p>
 * The default noscript renderer is non-browser-specific and implements
 * functionality in a way that runs on browsers that do not implement or permit
 * javascript execution.
 * </p>
 */
public class WebScrollPaneRenderer_Default_Noscript 
  extends DecoratorRenderer
{

  protected void createDecoratorHead( final Decorator dec ) throws IOException {
    WebScrollPane scrollPane = ( WebScrollPane )dec;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ALIGN, HTML.LEFT, null );
    out.writeAttribute( HTML.ID, scrollPane.getUniqueID(), null );
    String style = WebScrollPaneUtil.createStyle( scrollPane );
    String styleClass = out.registerCssClass( style );
    out.writeAttribute( HTML.CLASS, styleClass, null );
    out.closeElementIfStarted();
  }

  protected void createDecoratorFoot( final Decorator dec ) throws IOException {
    getResponseWriter().endElement( HTML.DIV );
  }
  
}