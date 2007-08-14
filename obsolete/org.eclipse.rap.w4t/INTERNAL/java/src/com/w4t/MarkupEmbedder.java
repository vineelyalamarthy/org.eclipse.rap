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
package com.w4t;

import com.w4t.internal.simplecomponent.UniversalAttributes;
import com.w4t.types.WebColor;

/**
 * <p>
 * Instances of this class pass the given {@link #setContent(String)
 * <code>content</code>} as-is to the client.
 * </p>
 * <p>
 * <strong>Example:</strong>
 * <pre>
 * new MarkupEmbedder( &quot;one line&lt;br /&gt;and another...&quot; );
 * </pre>
 * will look like the following on the client:
 * <pre>
 * one line
 * and another...
 * </pre>
 * <strong>Note:</strong> When this component is rendered, the
 * <code>content</code> will be wrapped inside a &lt;div&gt; element. All
 * universal attributes (like {@link #setTitle(String) <code>title</code>})
 * specified on a <code>MarkupEmbedder</code> will be applied to this element.
 * </p>
 * <p>
 * Use this class with great precaution. Misbehaving markup may corrupt the
 * whole web page.
 * </p>
 */
public class MarkupEmbedder extends WebComponent implements SimpleComponent {
  
  private String content = "";
  private final UniversalAttributes universalAttributes 
    = new UniversalAttributes();
  
  /** 
   * <p>Creates a new instance of <code>MarkupEmbedder</code>.</p> 
   */
  public MarkupEmbedder() {
    resetStyle();
  }

  /** 
   * <p>Creates a new instance of <code>MarkupEmbedder</code> and uses the
   * given <code>content</code> as its content.
   * </p> 
   */
  public MarkupEmbedder( final String content ) {
    this();
    setContent( content );
  }
  
  /**
   * <p>Sets the content (which may contain markup) that will be passed to the 
   * web page.</p>
   * @param content the new content. A <code>null</code> argument will be
   * considered as an empty string (""). 
   */
  public void setContent( final String content ) {
    this.content = content == null ? "" : content;
  }
  
  /**
   * <p>
   * Returns the content.
   * </p>
   */
  public String getContent() {
    return content;
  }

  // interface methods of org.eclipse.rap.SimpleComponent
  ///////////////////////////////////////////////
  
  public String getCssClass() {
    return universalAttributes.getCssClass();
  }
  
  public String getDir() {
    return universalAttributes.getDir();
  }
  
  public String getLang() {
    return universalAttributes.getLang();
  }
  
  public Style getStyle() {
    return universalAttributes.getStyle();
  }
  
  public String getTitle() {
    return universalAttributes.getTitle();
  }
  
  public void setCssClass( final String cssClass ) {
    universalAttributes.setCssClass( cssClass );
  }
  
  public void setDir( final String dir ) {
    universalAttributes.setDir( dir );
  }
  
  public void setLang( final String lang ) {
    universalAttributes.setLang( lang );
  }
  
  public void setStyle( final Style style ) {
    universalAttributes.setStyle( style );
  }
  
  public void setTitle( final String title ) {
    universalAttributes.setTitle( title );
  }

  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    universalAttributes.setIgnoreLocalStyle( ignoreLocalStyle );
  }
  
  public boolean isIgnoreLocalStyle() {
    return universalAttributes.isIgnoreLocalStyle();
  }

  private void resetStyle() {
    // Reset style to "no style settings"
    WebColor noColor = new WebColor( "" );
    universalAttributes.getStyle().setFontFamily( "" );
    universalAttributes.getStyle().setFontSize( Style.NOT_USED );
    universalAttributes.getStyle().setColor( noColor );
    universalAttributes.getStyle().setBorderColor( noColor );
    universalAttributes.getStyle().setBorderTopColor( noColor );
    universalAttributes.getStyle().setBorderBottomColor( noColor );
    universalAttributes.getStyle().setBorderLeftColor( noColor );
    universalAttributes.getStyle().setBorderRightColor( noColor );
    universalAttributes.getStyle().setBgColor( noColor );
  }
}
