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
 * <p>A WebLabel represents a user interface object that displays a string
 * which cannot be changed by the user.</p>
 */
public class WebLabel 
  extends WebComponent 
  implements SimpleComponent, IValueHolder
{

  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  private String value = "";
  private Format formatter = null;
  
  /** Creates a new instance of <code>WebLabel</code>. */
  public WebLabel() {
  }

  /** 
   * Creates a new instance of <code>WebLabel</code> and sets the given
   * <code>value</code> as its label text.
   */
  public WebLabel( final String value ) {
    this();
    setValue( value );
  }
    
  public Object clone() throws CloneNotSupportedException {
    WebLabel result = ( WebLabel )super.clone();
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
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
    }
    return universalAttributes;
  }

  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/label.gif";
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