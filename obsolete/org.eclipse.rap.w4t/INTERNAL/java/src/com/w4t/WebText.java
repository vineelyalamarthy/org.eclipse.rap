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

import com.w4t.event.*;
import com.w4t.internal.simplecomponent.UniversalAttributes;

/** 
 * <p>A <code>WebText</code> is a text WebComponent that allows the editing of a
 * single line of text.</p>
 *  
 * <p>A <code>WebText</code> encapsulates the &lt;input&gt; type text 
 * element.</p>
 */
public class WebText 
  extends WebComponent
  implements ValidatableComponent, 
             SimpleComponent, 
             IInputValueHolder,
             IFocusable
{

  /** if this is true the characters in the textfield are replaced with '*' */
  private boolean useAsPassword = false;
  /** the size of this textfield in characters */
  private int size = 0;
  /** the maximum count of characters this textfield can display */
  private int maxLength = 0;

  /** the validator object against which validation of the value of 
   * this WebText is performed. */
  private Validator validator;
  /** <p>whether the validation on this WebText is active, i.e. 
    * whether validation is actually performed on validate() call.</p> */
  private boolean validationActive = true;
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  private String value = "";
  private boolean updatable = true;
  private Format formatter = null;


  /** 
   * Returns a clone of this WebText. 
   */
  public Object clone() throws CloneNotSupportedException {
    WebText result = ( WebText )super.clone();
    result.setValidator( getValidator() );
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
  }
  
  public void setFocus( final boolean focus ) {
    FocusHelper.setFocus( this, focus );
  }
  
  public boolean hasFocus() {
    return FocusHelper.hasFocus( this );
  }
  
  public void remove() {
    setFocus( false );
    super.remove();
  }

  
  // attribute getters and setters
  ////////////////////////////////

  /** 
   * Returns whether the characters in the textfield are replaced with '*'. 
   */
  public boolean isPassword() {
    return useAsPassword;
  }

  /** 
   * Sets whether the characters in the textfield are replaced with '*'.
   */
  public void setPassword( final boolean password ) {
    this.useAsPassword = password;
  }

  /** 
   * Sets the number of characters that define this textfield's
   * display width. 
   */
  public void setSize( final int size ) {
    this.size = size;
  }

  /** 
   * Returns the number of characters that define this textfield's
   * display width. 
   */
  public int getSize() {
    return size;
  }

  /** 
   * Sets the maximum number of characters this textfield can display.
   */
  public void setMaxLength( final int maxLength ) {
    this.maxLength = maxLength;
  }

  /** 
   * Returns the maximum number of characters this textfield can display.
   */
  public int getMaxLength() {
    return maxLength;
  }

  /** 
   * <p>Returns a path to an image that represents this WebComponent
   * (widget icon).</p> 
   */
  public static String retrieveIconName() {
    return "resources/images/icons/text.gif";
  }
 
  
  // event handling methods
  /////////////////////////
  
  /** 
   * Adds the specified WebItemListener to receive WebItemEvents from
   * this WebText. WebItemEvents occur when a user changes the
   * value in the text field of this WebText
   * 
   * @param listener the WebItemListener
   */
  public void addWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  /**
   * Removes the specified WebItemListener to receive WebItemEvents from
   * this WebText. WebItemEvents occur when a user changes the
   * value in the text field of this WebText
   * 
   * @param listener the WebItemListener
   */
  public void removeWebItemListener( final WebItemListener listener ) {
    WebItemEvent.removeListener( this, listener );
  }

  /**
   * Adds the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebText. WebFocusGainedEvents
   * occur if the depending html text field gets the focus
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /**
   * Removes the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebText. WebFocusGainedEvents
   * occur if the depending html text field gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }

  
  // interface methods of org.eclipse.rap.ValidatableComponent
  ///////////////////////////////////////////////////////////////
  
  public void setValidator( final Validator validator ) {
    this.validator = validator;
  }
  
  public Validator getValidator() {
    return validator;
  }

  public void setValidationActive( final boolean validationActive ) {
    this.validationActive = validationActive;
  }

  public boolean isValidationActive() {
    return validationActive;
  }
  
  public void addValidationListener( final ValidationListener listener ) {
    ValidationEvent.addListener( this, listener );
  }

  public void removeValidationListener( final ValidationListener listener ) {
    ValidationEvent.removeListener( this, listener );
  }

  public boolean validate() {
    boolean result = true;
    if( validationActive ) {    
      if( validator != null ) {
        result = validator.validate( getValue() );
      }
      int evtId = ValidationEvent.VALIDATED;
      ValidationEvent evt
        = new ValidationEvent( this, evtId, validator, result );
      evt.processEvent();
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
      universalAttributes.getStyle().setWidth( "90" );
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
  
  public void addWebDataListener( final WebDataListener listener ) {
    WebDataEvent.addListener( this, listener );
  }

  public void removeWebDataListener( final WebDataListener listener ) {
    WebDataEvent.removeListener( this, listener );
  }

  public void setUpdatable( final boolean updatable ) {
    this.updatable = updatable;
  }

  public boolean isUpdatable() {
    return updatable;
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