/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal.branding;

import java.io.IOException;
import java.util.*;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

public class Header {

  private String tagName;
  private Map attributes = new HashMap();

  public Header( final String tagName ) {
    this.tagName = tagName;
  }

  public String getTagName() {
    return tagName;
  }

  public void setAttributes( final HashMap attributes ) {
    this.attributes = attributes;
  }

  public Map getAttributes() {
    return attributes;
  }

  public String getAttribute( final String key ) {
    return ( String )attributes.get( key );
  }

  public String render() throws IOException {
    // TODO [bm] eliminate use of HtmlResponseWriter
    HtmlResponseWriter writer = new HtmlResponseWriter();
    writer.startElement( tagName, null );
    Set keys = attributes.keySet();
    Iterator iterator = keys.iterator();
    while( iterator.hasNext() ) {
      String key = ( String )iterator.next();
      String value = ( String )attributes.get( key );
      if( key != null && value != null ) {
        writer.writeAttribute( key, value, "" );
      }
    }
    writer.endElement( tagName );
    return getBodyMarkup( writer );
  }

  public static String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }
}
