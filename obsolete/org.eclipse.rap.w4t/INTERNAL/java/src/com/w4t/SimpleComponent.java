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

/** <p>WebComponents which are a representation of simple HTML tags
 *  implement this interface. The attribute getters and setters of the 
 *  interface are the counterpart to the universal attributes 
 *  usable on every tag.</p>
 */
public interface SimpleComponent {
  
  /** <p>sets a style descriptor.</p> */
  void setStyle( Style style );

  /** <p>returns the style descriptor.</p> */
  Style getStyle();

  /** <p>sets the HTML 'dir' attribute which specifies the text direction 
    * of the language in the HTML representation of this SimpleComponent.</p>
    * 
    * <p>Possible values are:</p>
    *  <ul>
    *   <li>ltr = left to right</li>
    *   <li>rtl = right to left</li>
    *  </ul>
    */
  void setDir( String dir );

  /** <p>returns the HTML 'dir' attribute which specifies the text direction 
    * of the language in the HTML representation of this SimpleComponent.</p>
    * 
    * <p>Possible values are:</p>
    *  <ul>
    *   <li>ltr = left to right</li>
    *   <li>rtl = right to left</li>
    *  </ul>
    */
  String getDir();

  /** <p>sets the cascading stylesheet (css) class.</p> */
  void setCssClass( String cssClass );

  /** <p>returns the cascading stylesheet (css) class.</p> */
  String getCssClass();

  /** <p>sets the HTML 'language' universal attribute.</p>
    * 
    * <p>Typically used in multilingual documents and for search engines 
    * in the web.</p>
    *  
    * <p>Possible values are standard abbreviations for languages, like:
    * 'de' for German, 'en' for English or 'fr' for French.</p> */  
  void setLang( String lang );
  
  /** <p>returns the HTML 'language' universal attribute.</p>
    * 
    * <p>Typically used in multilingual documents and for search engines 
    * in the web.</p>
    *  
    * <p>Possible values are standard abbreviations for languages, like:
    * 'de' for German, 'en' for English or 'fr' for French.</p> */  
  String getLang();

  /** <p>sets the HTML 'title' universal attribute, the passed String appears
    * as tooltip text on this SimpleComponent.</p> */
  void setTitle( String title );

  /** <p>returns the HTML 'title' universal attribute, the passed String 
    * appears as tooltip text on this SimpleComponent.</p> */
  String getTitle();
  
  /** <p>sets whether attributes in the style descriptor (see 
    * {@link org.eclipse.rwt.SimpleComponent#setStyle( org.eclipse.rwt.Style ) 
    * setStyle( Style )} that would override settings in a css class (see
    * {@link org.eclipse.rwt.SimpleComponent#setCssClass( String ) 
    * setCssClass( String ) } are ignored when rendering this 
    * SimpleComponent.</p>
    * 
    * <p>This is needed to control the actually applying style settings 
    * on this SimpleComponent when both style and css class settings are used. 
    * If css classes are used, some components render default styles in 
    * addition to those defined in the css class, when this is set to 
    * </b>false</b>. Most browsers let local style settings override the 
    * settings defined in a css class.</p>
    *  
    * <p>(Note: The default value used by the implementations in the W4T 
    * libraries is <b>true</b>). 
    */
  void setIgnoreLocalStyle( boolean ignoreLocalStyle );

  /** <p>returns whether attributes in the style descriptor (see 
    * {@link org.eclipse.rwt.SimpleComponent#setStyle( org.eclipse.rwt.Style ) 
    * setStyle( Style )} that would override settings in a css class (see
    * {@link org.eclipse.rwt.SimpleComponent#setCssClass( String ) 
    * setCssClass( String ) } are ignored when rendering this 
    * SimpleComponent.</p>
    * 
    * <p>This is needed to control the actually applying style settings 
    * on this SimpleComponent when both style and css class settings are used. 
    * If css classes are used, some components render default styles in 
    * addition to those defined in the css class, when this is set to 
    * </b>false</b>. Most browsers let local style settings override the 
    * settings defined in a css class.</p>
    */
  boolean isIgnoreLocalStyle(); 
}