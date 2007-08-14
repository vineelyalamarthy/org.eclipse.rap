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
 * <p>A <code>WebTextArea</code> is a text WebComponent that allows the 
 * editing of a multiple line of text.</p>
 * 
 * <p>This encapsulates the &lt;textarea&gt; html tag.</p>
 */
public class WebTextArea
  extends WebComponent
  implements ValidatableComponent, 
             SimpleComponent, 
             IInputValueHolder,
             IFocusable
{

  /** the vertical size of this textarea in characters */
  private int rows = 10;
  /** the horizontal size of this textarea in characters */
  private int cols = 15;
  /** line wrap definition */
  private String wrap = "virtual";

  /** the validator object against which validation of the value of 
   * this WebTextArea is performed. */
  private Validator validator;
  /** <p>whether the validation on this WebTextArea is active, i.e. 
    * whether validation is actually performed on validate() call.</p> */
  private boolean validationActive = true;
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  private String value = "";
  private boolean updatable = true;
  private Format formatter = null;

  
  /** returns a clone of this WebTextArea. */
  public Object clone() throws CloneNotSupportedException {
    WebTextArea result = ( WebTextArea )super.clone();
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
  
  
  // event handling
  /////////////////

  /** 
   * Adds the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebTextArea. WebFocusGainedEvents
   * occur if the depending html text area field gets the focus
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /**
   * Removes the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebTextArea. WebFocusGainedEvents
   * occur if the depending html text area field gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }

  /** 
   * Adds the specified WebItemListener to receive WebItemEvents from
   * this WebTextArea. WebItemEvents occur when a user changes the
   * value in the textarea field of this WebTextArea.
   * 
   * @param listener the WebItemListener
   */
  public void addWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  /**
   * Removes the specified WebItemListener to receive WebItemEvents from
   * this WebTextArea. WebItemEvents occur when a user changes the
   * value in the textarea field of this WebTextArea.
   * 
   * @param listener the WebItemListener
   */
  public void removeWebItemListener( final WebItemListener listener ) {
    WebItemEvent.removeListener( this, listener );
  }

  
  // attribute getters and setters
  ////////////////////////////////

  /**
   * Sets the vertical size of this textarea as an amount of characters
   */
  public void setRows( final int rows ) {
    this.rows = rows;
  }

  /** 
   * Returns the vertical size of this textarea as an amount of characters
   */
  public int getRows() {
    return rows;
  }

  /** 
   * Sets the horizontal size of this textarea as an amount of characters
   */
  public void setCols( final int cols ) {
    this.cols = cols;
  }

  /** 
   * Returns the horizontal size of this textarea as an amount of characters
   */
  public int getCols() {
    return cols;
  }

  /**
   * Sets the wrap mechanism of the textarea
   * 
   * @param wrap posibilities: 
   *             <ul><li>virtual - line wrap is displayed only</li>
   *                 <li>physical - line wrap is submitted</li>
   *                 <li>off - no line wrap</li></ul>
   *             default is virtual
   */
  public void setWrap( final String wrap ) {
    this.wrap = wrap;
  }

  /**
   * Returns the wrap mechanism of the textarea
   * @return wrap posibilities: 
   *             <ul><li>virtual - line wrap is displayed only</li>
   *                 <li>physical - line wrap is submitted</li>
   *                 <li>off - no line wrap</li></ul>
   *             default is virtual
   */
  public String getWrap() {
    return wrap;
  }
  
  /**
   * <p>Returns a path to an image that represents this WebComponent
   * (widget icon).</p> 
   */
  public static String retrieveIconName() {
    return "resources/images/icons/textarea.gif";
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
        result = validator.validate( value );
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