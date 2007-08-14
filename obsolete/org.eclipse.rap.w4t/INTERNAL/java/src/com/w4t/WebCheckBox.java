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

import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.event.*;
import com.w4t.internal.simplecomponent.UniversalAttributes;


/** 
 * <p>A WebCheckBox is a graphical WebComponent which can be either in
 * an "on" (checked) or "off" (unchecked) state. Clicking on a checkbox
 * changes its state from "on" to "off," or from "off" to "on."</p>
 * 
 * <p>A text (label) may be specified which is displayed near 
 * the checkbox.</p>
 */
public class WebCheckBox
  extends WebComponent 
  implements SimpleComponent, IInputValueHolder, IFocusable
{
                           
  /** the displayed text near the checkbox */
  private String label = "";
  /** whether this WebCheckBox is checked */
  private boolean selected;
  /** return value of getValue(), if the WebCheckBox is checked (default -1) */
  private String valCheck = "-1";
  /** return value of getValue(), if the WebCheckBox is unchecked (default 0) */
  private String valUnCheck = "0";
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;

  private String value = valUnCheck;
  private boolean updatable = true;
  private Format formatter = null;


  /** 
   * Creates a new instance of <code>WebCheckBox</code>.
   */
  public WebCheckBox() {
    setValue( valUnCheck );
    selected = false;
  }

  /** 
   * Creates a new instance of <code>WebCheckBox</code> with 
   * the specified label.
   */
  public WebCheckBox( final String label ) {
    this();
    ParamCheck.notNull( label, "label" );
    this.label = label;
  }
  
  /** 
   * Returns a clone of this WebCheckBox.
   */
  public Object clone() throws CloneNotSupportedException {
    WebCheckBox result = ( WebCheckBox )super.clone();
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
   * Adds the specified WebItemListener to receive WebItemEvents from
   * this WebCheckBox. WebItemEvents occur on user click.
   * 
   * @param listener the WebItemListener
   */
  public void addWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  /** 
   * Removes the specified WebItemListener to receive WebItemEvents from
   * this WebCheckBox. WebItemEvents occur on user click.
   * 
   * @param listener the WebItemListener
   */
  public void removeWebItemListener( final WebItemListener listener ) {
    WebItemEvent.removeListener( this, listener );
  }

  /**
   * Adds the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebCheckBox. WebFocusGainedEvents
   * occur if the depending html checkbox gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /** 
   * Removes the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebCheckBox. WebFocusGainedEvents
   * occur if the depending html checkbox gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }

  
  // attribute getters and setters
  ////////////////////////////////

  /** 
   * <p>Sets the text to be displayed near the checkbox.
   * If label is null, the displayed text is taken from the value 
   * attribute.</p>
   * <p>Set label to empty string, if you don't want any text to be 
   * displayed.</p>
   */
  public void setLabel( final String label ) {
    this.label = label;
  }

  /** 
   * <p>returns the text which is displayed near the checkbox.</p> 
   */
  public String getLabel() {
    return label;
  }

  /** 
   * <p>selects or unselects this WebCheckBox.</p>
   * 
   * @param selected     whether this WebCheckBox should appear checked 
   *                     (true) or unchecked (false).
   */
  public void setSelected( final boolean selected ) {
    this.selected = selected;
    if( selected ) {
      setValue( valCheck );
    } else {
      setValue( valUnCheck );
    }
  }

  /** 
   * Returns whether this WebCheckBox is selected.
   */
  public boolean isSelected() {
    return selected;
  }

  /** 
   * Sets the return value of getValue(), if the WebCheckBox is checked 
   * (default "-1", doesn't accept "" !).
   */
  public void setValCheck( final String valCheck ) {
    if( valCheck.equals( "" ) ) {
      String msg = "Empty String is not allowed for parameter 'valCheck'.";
      throw new IllegalArgumentException( msg ); 
    }
    this.valCheck = valCheck;
  }

  /** 
   * Returns the return value of getValue(),
   * if the WebCheckBox is checked (default "-1").
   */
  public String getValCheck() {
    return valCheck;
  }

  /** 
   * Sets the return value of getValue(),
   * if the WebCheckBox is unchecked (default "0").
   */
  public void setValUnCheck( final String valUnCheck ) {
    ParamCheck.notNull( valUnCheck, "valUnCheck" );
    this.valUnCheck = valUnCheck;
  }

  /**
   * Returns the return value of getValue(),
   * if the WebCheckBox is unchecked (default "0").
   */
  public String getValUnCheck(){
    return valUnCheck;
  }
  
  /** 
   * <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p>
   */
  public static String retrieveIconName() {
    return "resources/images/icons/checkbox.gif";
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
    if ( this.value.equals( valUnCheck ) ) {
      selected = false;
    } else {
      selected = true;
    }
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