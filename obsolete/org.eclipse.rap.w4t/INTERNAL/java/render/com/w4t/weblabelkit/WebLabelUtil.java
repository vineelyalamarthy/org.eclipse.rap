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
package com.w4t.weblabelkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;


final class WebLabelUtil {

  private WebLabelUtil() {
    // prevent instatiation
  }
  
  static String getValue( final WebLabel wlb ) {
    String value = wlb.getValue();
    if( wlb.getFormatter() != null ) {
      value = ConverterUtil.format( wlb.getFormatter(), value );
    }
    value = RenderUtil.resolve( value );
    return value;
  }

  static void createScriptMarkup( final WebLabel wlb ) throws IOException {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();
    out.startElement( HTML.SPAN, wlb );
    out.writeAttribute( HTML.ID, wlb.getUniqueID(), null );
    RenderUtil.writeUniversalAttributes( wlb );
    String value = WebLabelUtil.getValue( wlb );
    String[] splitLineBreak = RenderUtil.splitLineBreak( value );
    for( int i = 0; i < splitLineBreak.length; i++ ) {
      if( splitLineBreak[ i ].equals( "\n" ) ) {
        out.write( "<br>" );
      } else {
        out.writeText( splitLineBreak[ i ], null );
      }      
    }
    out.endElement( HTML.SPAN );
  }
}
