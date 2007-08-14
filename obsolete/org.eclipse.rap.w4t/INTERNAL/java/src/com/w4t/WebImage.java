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

import java.text.Format;
import com.w4t.internal.simplecomponent.UniversalAttributes;


/** 
 * <p>A WebImage encapsulates the HTML &lt;img&gt; element.</p>
 * <p><strong>Note:</strong> The recommended way to use images is to deploy 
 * them within the applications' JAR file and register them using the 
 * {@link org.eclipse.rwt.resources.IResourceManager#register(String) 
 * <code>IResourceManager</code>s register} method. From thereon they can be
 * referenced by the same name as inside the JAR. 
 * <strong>Example:</strong>
 * <pre>
 * W4TContext.getResourceManager().register( "resources/myimage.gif" );
 * ...
 * new WebImage( "resources/myimage.gif" );
 * </pre>
 * </p>
 */
public class WebImage 
  extends WebComponent
  implements SimpleComponent, IValueHolder
{

  /** alternative description if image not found */
  private String alt = "";
  /** border of the image */
  private String border = "0";
  /** height of the image in pixel or procent */
  private String height = "";
  /** width of the image in pixel or procent */
  private String width = "";
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  private String value = "";
  private Format formatter = null;

  
  /** 
   * Creates a new instance of the <code>WebImage</code>.
   */
  public WebImage() {
  }

  /** 
   * Creates a new instance of the <code>WebImage</code> and sets
   * the source path of the image.
   */
  public WebImage( final String value ) {
    this();
    setValue( value );
  }

  /** 
   * Returns a copy of this WebImage object.
   */
  public Object clone() throws CloneNotSupportedException {
    WebImage result = ( WebImage )super.clone();
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
  }
  
  /** 
   * Sets the alternative description of the image.
   */
  public void setAlt( final String alt ) {
    this.alt = alt;
  }

  /** 
   * Returns the alternative description of the image.
   */
  public String getAlt() {
    return alt;
  }

  /**
   * Sets the border of the image.
   */
  public void setBorder( final String border ) {
    this.border = border;
  }

  /** 
   * Returns the border of the image.
   */
  public String getBorder() {
    return border;
  }

  /**
   * Sets the height of the image.
   */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /** 
   * Returns the height of the image.
   */
  public String getHeight() {
    return height;
  }

  /** 
   * Sets the width of the image.
   */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** 
   * Returns the width of the image.
   */
  public String getWidth() {
    return width;
  }

  /** 
   * <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p>
   */
  public static String retrieveIconName() {
    return "resources/images/icons/image.gif";
  }
  
  
  // interface methods of org.eclipse.rap.SimpleComponent
  ///////////////////////////////////////////////////////////////
  
  public String getCssClass() {
    return getUniversalAttributes().getCssClass();
  }
  
  public String getDir() {
    return getUniversalAttributes().getDir();
  }
  
  public String getLang() {
    return getUniversalAttributes().getLang();
  }
  
  public Style getStyle() {
    return getUniversalAttributes().getStyle();
  }
  
  public String getTitle() {
    return getUniversalAttributes().getTitle();
  }
  
  public void setCssClass( final String cssClass ) {
    getUniversalAttributes().setCssClass( cssClass );
  }
  
  public void setDir( final String dir ) {
    getUniversalAttributes().setDir( dir );
  }
  
  public void setLang( final String lang ) {
    getUniversalAttributes().setLang( lang );
  }
  
  public void setStyle( final Style style ) {
    getUniversalAttributes().setStyle( style );
  }
  
  public void setTitle( final String title ) {
    getUniversalAttributes().setTitle( title );
  }

  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    getUniversalAttributes().setIgnoreLocalStyle( ignoreLocalStyle );
  }
  
  public boolean isIgnoreLocalStyle() {
    return getUniversalAttributes().isIgnoreLocalStyle();
  }

  private UniversalAttributes getUniversalAttributes() {
    if( universalAttributes == null ) {
      universalAttributes = new UniversalAttributes();
      universalAttributes.getStyle().setFontFamily( "" );
      universalAttributes.getStyle().setFontSize( Style.NOT_USED );
    }
    return universalAttributes;
  }
  
  
  ////////////////////////////////////
  // interface methods of IValueHolder 

  public void setValue( final String value ) {
    this.value = value == null ? "" : value;
  }

  public String getValue() {
    return value;
  }
  
  /**
   * @deprecated
   */
  public void setFormatter( final Format formatter ) {
    this.formatter = formatter;
  }

  /**
   * @deprecated
   */
  public Format getFormatter() {
    return formatter;
  }
}