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
package com.w4t.webtextkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;

/**
 * <p>
 * the superclass of all Renderers that render org.eclipse.rap.WebText.
 * </p>
 */
final class WebTextUtil {

  private WebTextUtil() {
    // prevent instantiation
  }

  public static void renderStart( final WebText wtx ) throws IOException {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.INPUT, wtx );
    out.writeAttribute( HTML.TYPE, getType( wtx ), null );
    out.writeAttribute( HTML.NAME, wtx.getUniqueID(), null );
    out.writeAttribute( HTML.VALUE, getValue( wtx ), null );
    out.writeAttribute( HTML.ID, wtx.getUniqueID(), null );
    renderSize( wtx );
    renderMaxLength( wtx );
    RenderUtil.writeUniversalAttributes( wtx );
    RenderUtil.writeReadOnly( wtx );
    RenderUtil.writeDisabled( wtx );
  }

  public static void renderEnd() throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.INPUT );
  }

  private static void renderSize( final WebText wtx ) throws IOException {
    if( wtx.getSize() > 0 ) {
      HtmlResponseWriter out = getResponseWriter();
      out.writeAttribute( HTML.SIZE, String.valueOf( wtx.getSize() ), null );
    }
  }

  private static void renderMaxLength( final WebText wtx )
    throws IOException
  {
    if( wtx.getMaxLength() > 0 ) {
      HtmlResponseWriter out = getResponseWriter();
      String maxLength = String.valueOf( wtx.getMaxLength() );
      out.writeAttribute( HTML.MAXLENGTH, maxLength, null );
    }
  }

  private static String getType( final WebText wtx ) {
    return wtx.isPassword()? HTML.PASSWORD : HTML.TEXT;
  }

  private static String getValue( final WebText wtx ) {
    String value = wtx.getValue();
    if( wtx.getFormatter() != null ) {
      value = ConverterUtil.format( wtx.getFormatter(), value );
    }
    value = RenderUtil.resolve( value );
    return value;
  }

  private static HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();
    return out;
  }
}