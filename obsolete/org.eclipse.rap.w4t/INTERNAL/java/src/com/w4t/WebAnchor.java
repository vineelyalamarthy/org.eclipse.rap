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


/** <p>A WebAnchor is a component which branches to a location that is 
  * reachable for the displaying browser, as URL of files, websites, or 
  * other WebAnchors on the same form.</p>
  * <p>WebAnchor encapsulates the HTML &lt;a&gt; tag.</p>
  *
  * <p>To create links to pages within the W4 Toolkit application (i.e.
  * other WebForms), you normally use {@link org.eclipse.rwt.WebButton WebButton} 
  * instead of WebAnchor. But for some reasons it may be nessecary having
  * an &lt;a&gt;tag referring to a WebForm within an application. This is
  * realized with the targetForm attribute of the WebAnchor.</p>
  */
public class WebAnchor extends Decorator implements SimpleComponent {
                         
  /** the anchors identifier other anchors can refer to (html-attribute: name)*/
  private String anchorName = "";
  /** this attribute specifies the name of a frame where a document is 
   *  to be opened. Note: W4 Toolkit does not support frames, but a W4 Toolkit
   *  WebForm output could be opened in a frame, so you can use the target
   *  attribute to refer to a other frame */
  private String target = "";  
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  /** The URL to which this anchor component refers to */
  private String hRef = "";  
  /** the target WebForm which should be referred by a &lt;a&gt; tag. */ 
  private WebForm targetForm = null;
  
  /** <p>creates a new instance of WebAnchor.</p> */
  public WebAnchor() {
    try {
      this.setContent( new WebLabel( "linkTo..." ) );
    } catch( Exception shouldNotHappen ) {
      System.out.println( "\nException in constructor call of WebAnchor:\n"
                        + shouldNotHappen.toString() + "\n" );
    }    
  }
  
  /** <p>creates a new instance the WebAnchor with the specified 
    * destination.</p> */
  public WebAnchor( final String hRef ) {
    this();
    setHRef( hRef );
  }
  
  /** <p>returns a copy of WebAnchor.</p> */
  public Object clone() throws CloneNotSupportedException {
    WebAnchor result = ( WebAnchor )super.clone();
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    result.equalizeStyle( result.getContent() );
    return result;    
  }  
  
  /** <p>sets the anchors identifier, with which other anchors 
   * can refer to this anchor (html-attribute: name) */                         
  public void setAnchorName( final String anchorName ) {
    this.anchorName = anchorName;
  }
  
  /** <p>returns the anchors identifier, with which other anchors 
    * can refer to this anchor (html-attribute: name)</p> */
  public String getAnchorName() {
    return anchorName;
  }
    
  /** sets the html target attribute of the &lt;a&gt; tag which
   *  specifies the name of a frame where a document is to be opened. 
   *  Note: W4 Toolkit does not support frames, but a W4 Toolkit
   *  WebForm output could be opened in a frame, so you can use the target
   *  attribute to refer to a other frame */
  public void setTarget( final String target ) {
    this.target = target;
  }
  
  /** returns the html target attribute of the &lt;a&gt; tag which
   *  specifies the name of a frame where a document is to be opened. 
   *  Note: W4 Toolkit does not support frames, but a W4 Toolkit
   *  WebForm output could be opened in a frame, so you can use the target
   *  attribute to refer to a other frame */
  public String getTarget() {
    return target;
  }
  
  /** sets the URL to which this anchor component refers to */
  public void setHRef( final String hRef ) {
    this.hRef = hRef;
  }  

  /** returns the URL to which this anchor component refers to */
  public String getHRef() {
    return hRef;
  }

  /** <p>returns a path to an image that represents this WebComponent
   *  (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/anchor.gif";
  }
  
  public void setContent( final WebComponent content )  {
    super.setContent( content );
    equalizeStyle( content );
  }

  private void equalizeStyle( final WebComponent content ) {
    if(    universalAttributes != null
        &&  ( content instanceof SimpleComponent )
        && !( content instanceof WebImage ) ) {
      ( ( SimpleComponent )content ).setStyle( universalAttributes.getStyle() );
    }
  }
  
  /** sets the target WebForm which should be referred by 
   *  a &lt;a&gt; tag.<br>
   *  Note: If a targetForm is set, the settings of the hRef attribute
   *  are ignored! <br>
   *  Note: Branching to other WebForm instances in the same browser window
   *  which is realized with the WebAnchor causes a lost of just added values 
   *  in the input fields! */ 
  public void setTargetForm( final WebForm targetForm ) {
    this.targetForm = targetForm;
  }
  
  /** returns the target WebForm which should be referred by 
   *  a &lt;a&gt; tag.<br>
   *  Note: If a targetForm is set, the settings of the hRef attribute
   *  are ignored! 
   *  Note: Branching to other WebForm instances in the same browser window
   *  which is realized with the WebAnchor causes a lost of just added values 
   *  in the input fields! */ 
  public WebForm retrieveTargetForm() {
    return targetForm;
  }
  
  
  // interface methods of org.eclipse.rap.SimpleComponent
  // (no javadoc comments, so they are copied from the interface)
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
}