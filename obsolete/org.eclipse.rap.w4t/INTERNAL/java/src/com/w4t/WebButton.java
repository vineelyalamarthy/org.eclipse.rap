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

import com.w4t.event.*;
import com.w4t.internal.simplecomponent.UniversalAttributes;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/**
  * <p>A WebButton is used for submitting forms.</p>
  * <p>A WebButton submits a WebForm to the server if a
  * WebActionListener is added to the WebButton instance.
  * If a WebButton is pressed a WebActionEvent occurs
  * and the listeners webActionperformed methods are
  * executed.</p>
  * <p>Code example:
  * <pre>
  * package test;
  *
  * import org.eclipse.rap.*;
  * import org.eclipse.rap.event.*;
  * import org.eclipse.rap.types.*;
  *
  * public class Test extends WebForm {
  *
  *   // declarations
  *   WebBorderLayout wbbl;
  *   WebLabel        wblTextTop;
  *   WebButton       wbtAction;
  *
  *   public void setWebComponents() throws Exception {
  *
  *     // define label and set some attributes
  *     wbbl = ( WebBorderLayout ) this.getWebLayout();
  *     wblTextTop = new WebLabel( "white on black" );
  *     wblTextTop.getStyle().setBgColor( new WebColor( "black" ) );
  *     wblTextTop.getStyle().setColor( new WebColor( "white" ) );
  *
  *     wbtAction = new WebButton( "Start" );
  *
  *     // define the action on button pressed
  *     wbtAction.addWebActionListener(new WebActionListener() {
  *       public void webActionPerformed( WebActionEvent e ){
  *         WebColor background = wblTextTop.getStyle().getBgColor();
  *         if( background.toString().equals( "white" ) ){
  *           wblTextTop.getStyle().setBgColor( new WebColor( "black" ) );
  *           wblTextTop.getStyle().setColor( new WebColor( "white" ) );
  *           wblTextTop.setValue( "white on black" );
  *         }
  *         else{
  *           wblTextTop.getStyle().setBgColor( new WebColor( "white" ) );
  *           wblTextTop.getStyle().setColor( new WebColor( "black" ) );
  *           wblTextTop.setValue( "black on white" );
  *         }
  *       }
  *     } );
  *
  *     // add label and button
  *     this.add( wblTextTop, "NORTH" );
  *     this.add( wbtAction, "SOUTH" );
  *   }
  * }
  * </pre>
  */
public class WebButton
  extends WebComponent
  implements SimpleComponent, IFocusable
{

  private static final String BUTTON_DISABLED 
    = DefaultColorScheme.BUTTON_DISABLED;
  private static final WebColor BUTTON_DISABLED_COLOR
    = new WebColor( DefaultColorScheme.get( BUTTON_DISABLED ) );
  
  /** the display text of the WebButton */
  private String label = "";
  /** flag, if the WebButton should appear as a link */
  private boolean asLink = false;
  /** the name of the image, which is used as button */
  private String image = "";
  /** tells, if clicking this button opens a new window */
  private boolean newWindow = false;
  /** flag: whether a click on this WebButton causes a reset of all form
    * elements visible on the html document */
  private boolean reset = false;
  /** flag true: a click on this button causes a print */
  private boolean print = false;
  /** tells, if blanks are used before and behind a button link.
    * only used if asLink is true. Default: blanks (non breaking space) 
    * are used */
  private boolean trim = true;
  /** the label color of a disabled button */
  protected WebColor disabledColor;
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;   
  

  protected WebFocusGainedListener designFocusListener = null;

  /** creates a new instance of the WebButton */
  public WebButton() {
    super();
    disabledColor = BUTTON_DISABLED_COLOR;
  }

  /** creates a new instance of the WebButton and sets its label field */
  public WebButton( final String label ) {
    this();
    this.label = label;
  }

  /** creates a new instance of the WebButton and sets its label field
    * and its parent container */
  public WebButton( final String label, final WebContainer parent ) {
    this( label );
    this.parent = parent;
  }
  
  /** returns a clone of this WebButton */
  public Object clone() throws CloneNotSupportedException {
    WebButton result = ( WebButton )super.clone();
    result.setDisabledColor( new WebColor( disabledColor.toString() ) );
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
  }
  
  public void setFocus( final boolean focus ) {
    // TODO [rh] why can focus only be set on enabled buttons?
    FocusHelper.setFocus( this, focus && !asLink && isEnabled() );
  }
  
  public boolean hasFocus() {
    return FocusHelper.hasFocus( this );
  }
  
  public void remove() {
    setFocus( false );
    super.remove();
  }

  // event handling
  /////////////////
  
  /** Adds the specified WebActionListener to receive action events from
    * this WebButton. Action events occur when a user presses and releases
    * the mouse over this Button.
    * @param listener the WebActionListener
    */
  public void addWebActionListener( final WebActionListener listener ) {
    WebActionEvent.addListener( this, listener );
  }

  /** Removes the specified WebActionListener so that it no longer
    * receives action events from this WebButton. Action events occur
    * when a user presses and releases the mouse over this WebButton.
    * @param listener the WebActionListener
    */
  public void removeWebActionListener( final WebActionListener listener ) {
    WebActionEvent.removeListener( this, listener );
  }

  /**
    * Adds the specified WebFocusGainedListener to receive
    * WebFocusGainedEvents from this WebButton. WebFocusGainedEvents
    * occur if the depending html button gets the focus
    * @param lsnr the WebFocusGainedListener
    */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /**
    * Removes the specified WebFocusGainedListener to receive
    * WebFocusGainedEvents from this WebButton. WebFocusGainedEvents
    * occur if the depending html button gets the focus.
    * @param lsnr the WebFocusGainedListener
    */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }


  // attribute getters and setters
  ////////////////////////////////

  /**
    * sets if the link in the button has blanks (non breaking space)
    * before and behind its position (default). It only has an effect if
    * {@link #isLink() isLink} is true
    */
  public void setUseTrim( final boolean useTrim ) {
    this.trim = useTrim;
  }

  /**
    * gets if the link in the button has blanks (non breaking space)
    * before and behind its position (default). It only has an effect if
    * {@link #isLink() isLink} is true
    */
  public boolean isUseTrim() {
    return trim;
  }
  
  /** sets the display text of this WebButton */
  public void setLabel( final String label ) {
    this.label = label;
  }

  /** gets the display text of this WebButton */
  public String getLabel() {
    return label;
  }

  /**
   * returns, if the WebButton should appear as a hyper link
   * @deprecated replaced by {@link #setLink(boolean)}
   */
  public void asLink( final boolean asLink ) {
    if( image.equals( "" ) ) {
      this.asLink = asLink;
    }
  }

  /**
   * defines if the WebButton should appear as a hyper link
   * @deprecated replaced by {@link #isLink()}
   */
  public boolean asLink() {
    return asLink;
  }

  /** defines if the WebButton should appear as a hyper link */
  public void setLink( final boolean link ) {
    if( image.equals( "" ) ) {
      this.asLink = link;
    }
  }

  /** returns, if the WebButton appear as a hyper link */
  public boolean isLink() {
    return asLink;
  }

  /** <p>sets the name of the image which is used as display button. 
    * If set, the asLink property is automatically set to true!</p>
    * 
    * <p>The path to the image is regarded as relative to the web 
    * application context.</p>
    * 
    * @param image the name of a image file( jpeg, gif ) which is used
    *              as button. */
  public void setImage( final String image ) {
    asLink = true;
    this.image = image;
  }

  /** <p>gets the name of the image which is used as display button.</p>
   * 
    * <p>The path to the image is regarded as relative to the web 
    * application context.</p>
    * 
    * @return the name of a image file( jpeg, gif ) which is used
    *         as button. */
  public String getImage() {
    return image;
  }

  /**
    * sets if clicking this button should open a new window. If set, the
    * asLink property is automatically set to true!
    * @deprecated use window methods on WebForm instead
    */
  public void openNewWindow( final boolean newWindow ) {
    asLink = true;
    this.newWindow = newWindow;
  }

  /** gets if clicking this button opens a new window
    * @deprecated use window methods on WebForm instead
    */
  public boolean openNewWindow() {
    return newWindow;
  }

  /** sets whether a click on this WebButton causes a reset of all form
    * elements visible on the html document */
  public void setReset( final boolean reset ) {
    this.reset = reset;
  }

  /** returns whether a click on this WebButton causes a reset of all form
    * elements visible on the html document */
  public boolean isReset() {
    return reset;
  }

  /** sets print flag, if true a click on this button
    * causes a print of this page */
  public void setPrint( final boolean print ) {
    this.print = print;
  }

  /** returns true if a click on this button causes a print of this page */
  public boolean isPrint() {
    return print;
  }

  /** sets the label to name, if  label equals "" and designTime is true */
  public void setName( final String name ) {
    super.setName( name );
    if( label.equals( "" ) && designTime ) {
      label = name;
    }
  }

  /** set the label color of a disabled button */
  public void setDisabledColor( final WebColor disabledColor ) {
    this.disabledColor = disabledColor;
  }

  /** get the label color of a disabled button */
  public WebColor getDisabledColor() {
    return disabledColor;
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
  
  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/button.gif";
  }    
}