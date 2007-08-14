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
package com.w4t.internal.simplecomponent;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;

/** <p>Basic implementation of SimpleComponent. WebComponents which are 
 *  a representation of simple HTML tags, will use this class to delegate
 *  the implementation of the interface. The attribute getters and setters 
 *  are the counterpart to the universal attributes usable on every tag.</p>
 */
public class UniversalAttributes 
  extends WebComponentProperties
  implements SimpleComponent
{
  
  private static final Style DEFAULT_STYLE = new Style();
  
  private Style style;
  private String dir = "";
  private String cssClass = "";
  private String lang = "";
  private String title = "";  
  private boolean ignoreLocalStyle = true;
   
  /** <p>Creates a new instance of UniversalAttributes.</p> */
  public UniversalAttributes() {
  }
  
  /** returns a String which contains the settings of the HTML
    * attributes which are used in every HTML tag */
  // TODO [rh] eliminate this method
  public String getUniversalAttributes() {
    StringBuffer result = new StringBuffer();

    // style attribute
    Style current = style != null ? style : DEFAULT_STYLE;
    String css = current.toString();
    if( !css.equals( "" ) ) {
      result.append( " style=\"" );
      result.append( css );
      result.append( "\" " );
    }
    // css class
    if( !cssClass.equals( "" ) ) {
      result.append( " class=\"" );
      result.append( cssClass );
      result.append( "\" " );
    }
    // dir
    if( !dir.equals( "" ) ) {
      result.append( " dir=\"" );
      result.append( dir );
      result.append( "\" " );
    }
    // lang
    if( !lang.equals( "" ) ) {
      result.append( " lang=\"" );
      result.append( lang );
      result.append( "\" " );
    }
    // title
    if( !title.equals( "" ) ) {
      String tmpTitle = RenderUtil.resolve( title );
      tmpTitle = RenderUtil.encodeHTMLEntities( tmpTitle );
      result.append( " title=\"" );
      result.append( tmpTitle );
      result.append( "\" " );
    }

    return result.toString();
  }

  // TODO [rh] move to RenderUtil and unite with one of the already existing
  //      methods?
  public void writeUniversalAttributes() throws IOException {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    // style attribute
    Style current = style != null ? style : DEFAULT_STYLE;
    String css = current.toString();
    if( !css.equals( "" ) ) {
      out.writeAttribute( HTML.STYLE, css, null );
    }
    // css class
    if( !cssClass.equals( "" ) ) {
      out.writeAttribute( HTML.CLASS, cssClass, null );
    }
    // dir
    if( !dir.equals( "" ) ) {
      out.writeAttribute( HTML.DIR, dir, null );
    }
    // lang
    if( !lang.equals( "" ) ) {
      out.writeAttribute( HTML.LANG, lang, null );
    }
    // title
    if( !title.equals( "" ) ) {
      out.writeAttribute( HTML.TITLE, RenderUtil.resolve( title ), null );
    }
  }

  // interface methods of org.eclipse.rap.SimpleComponent
  // (no javadoc comments, so they are copied from the interface)
  ///////////////////////////////////////////////////////////////
  
  public void setStyle( final Style style ) {
    this.style = style;
  }

  public Style getStyle() {
    if( this.style == null ) {
      style = new Style();
    }
    return style;
  }

  public void setDir( final String dir ) {
    this.dir = dir;
  }

  public String getDir() {
    return dir;
  }

  public void setCssClass( final String cssClass ) {
    this.cssClass = cssClass;
  }

  public String getCssClass() {
    return cssClass;
  }

  public void setLang( final String lang ) {
    this.lang = lang;
  }

  public String getLang() {
    return lang;
  }
    
  public void setTitle( final String title ) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    this.ignoreLocalStyle = ignoreLocalStyle;
  }
  
  public boolean isIgnoreLocalStyle() {
    return ignoreLocalStyle;
  }
  
}