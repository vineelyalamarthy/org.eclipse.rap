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

import com.w4t.event.WebFocusGainedEvent;
import com.w4t.event.WebFocusGainedListener;
import com.w4t.internal.simplecomponent.UniversalAttributes;


/** 
 * <p>A WebRadioButton is a graphical WebComponent which can be either in
 * an 'on' (selected) or 'off' (unselected) state. Clicking on a radio
 * button changes its state to 'on'.</p>
 * 
 * <p>A WebRadioButton must belong to a {@link WebRadioButtonGroup 
 * <code>WebRadioButtonGroup</code>} in order to work properly; that is, one of 
 * its parents (see {@link WebRadioButton#getParent() <code>getParent()</code>})
 * must be a WebRadioButtonGroup. To obtain the group to which a WebRadioButton
 * belongs to use {@link org.eclipse.rwt.WebRadioButtonUtil#findGroup(WebRadioButton)
 * <code>WebRadioButtonUtil.findGroup(WebRadioButton)</code>}</p>
 * <p>
 * WebRadioButtons which do not belong to a group appear disabled and thus 
 * cannot be selected. 
 * </p>
 * <p>The WebRadioButton appears unchecked by default, the
 * {@link #setValue(String) <code>value</code>} is set to its 
 * {@link org.eclipse.rwt.WebComponent#getUniqueID()
 * <code>unique component id</code>}.</p>
 */
public class WebRadioButton extends WebComponent implements SimpleComponent {

  /** the displayed text, is taken from value, if not explicitly set */
  protected String label = "";
  /** <p>the value of this WebRadioButton, if it is databound.</p> */
  protected String value = "";
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;
  
  /** 
   * Creates a new WebRadioButton which belongs to a newly created
   * group. The group can be obtained with getGroup().
   */
  public WebRadioButton() {
    super();
    this.value = getUniqueID();
  }

  /** 
   * <p>Returns a clone of this WebRadioButton.</p>
   */ 
  public Object clone() throws CloneNotSupportedException {
    WebRadioButton result = ( WebRadioButton )super.clone();
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    return result;
  }  

  // event handling
  /////////////////
  
  /** 
   * Adds the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebRadioButton. WebFocusGainedEvents
   * occur if the depending html radio button gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /** 
   * Removes the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebRadioButton. WebFocusGainedEvents
   * occur if the depending html radio button gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }
  

  // attribute getters and setters
  ////////////////////////////////

  /**
   * {@inheritDoc}
   * <p>
   * A WebRadioButton is also not enabled if it does not belong (has one 
   * among its parents) to a WebRadioButtonGroup or the WebRadioButtonGroup 
   * is disabled itself.
   * </p>
   */
  public boolean isEnabled() {
    WebRadioButtonGroup buttonGroup = WebRadioButtonUtil.findGroup( this );
    return super.isEnabled() && buttonGroup != null && buttonGroup.isEnabled();
  }

  /**
   * {@inheritDoc}
   * <p>
   * A WebRadioButton is also not visible if it does not belong (has one 
   * among its parents) to a WebRadioButtonGroup or the WebRadioButtonGroup 
   * is invisible itself.
   * </p>
   */
  public boolean isVisible() {
    WebRadioButtonGroup buttonGroup = WebRadioButtonUtil.findGroup( this );
    return    super.isVisible() 
           && ( buttonGroup == null || buttonGroup.isVisible() );
  }
  
  /** 
   * <p>Sets the value for this WebRadioButton.</p>
   * <p>The value must be unique among all WebRadioButtons that belong to the 
   * same WebRadioButtonGroup.</p>
   * @param value the new value. Will be converted to an empty string ("") if 
   * <code>null</code>. 
   */
  public void setValue( final String value ) {
    this.value = value == null ? "" : value;
  }

  /** <p>Returns the value for this WebRadioButton.</p> */
  public String getValue() {
    return value;
  }

  /** 
   * <p>Sets the text to be displayed near the radionbutton.</p>
   * 
   * <p>If label is null, the displayed text is taken from the value attribute.
   * Set label to empty string, if you don't want any text to be displayed.</p>
   */
  public void setLabel( final String label ) {
    this.label = label;
  }

  /** 
   * Returns the text which is displayed near the radionbutton.
   */
  public String getLabel() {
    return label;
  }

  /** 
   * <p>Returns a path to an image that represents this WebComponent
   * (widget icon).</p>
   */
  public static String retrieveIconName() {
    return "resources/images/icons/radiobutton.gif";
  }    
  
  /** 
   * <p>Returns whether this WebRadioButton is the selected one of its group.
   * </p>
   */
  public boolean isSelected() {
    WebRadioButtonGroup group = WebRadioButtonUtil.findGroup( this );
    return group != null && group.getValue().equals( getValue() );
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

}