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

/** 
 * <p>A WebRadioButtonGroup is a logical grouping of WebRadioButtons.</p>
 * <p>From all {@link org.eclipse.rwt.WebRadioButton <code>WebRadioButton</code>s}
 * contained in a group only one can be selected at a time. A click on one of 
 * the WebRadioButtons in the group causes a WebItemChangedEvent. The clicked 
 * WebRadioButton appears selected, all other WebRadioButtons belonging to the 
 * group become unselected.</p>
 * <p>The default layout for the WebRadioButtonGroup is the 
 * {@link WebFlowLayout <code>WebFlowLayout</code>}.</p>
 */
public class WebRadioButtonGroup
  extends WebContainer
  implements IInputValueHolder
{

  private String value = "";
  private boolean updatable = true;

  /** 
   * <p>Creates a new instance of <code>WebRadioButtonGroup</code>.</p>
   */
  public WebRadioButtonGroup() {
    super();
    setWebLayout( new WebFlowLayout() );
  }

  /** 
   * <p>Returns a clone of this WebRadioButtonGroup.</p>
   */ 
  public final Object clone() throws CloneNotSupportedException {
    return ( WebRadioButtonGroup )super.clone();
  }

  /** 
   * Adds the specified WebItemListener to receive WebItemEvents from
   * the WebRadioButtons of this WebRadioButtonGroup. WebItemEvents occur
   * when a user clicks a WebRadioButton of the group.
   * 
   * @param listener the WebItemListener
   * */
  public void addWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  /**
   * Removes the specified WebItemListener to receive WebItemEvents from
   * the WebRadioButtons of this WebRadioButtonGroup. WebItemEvents occur
   * when a user clicks a WebRadioButton of the group.
   * 
   * @param listener the WebItemListener
   * */
  public void removeWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  ////////////////////////////////////
  // interface methods of IValueHolder 

  /**
   * {@inheritDoc}
   * <p>Note: For the WebRadioButtonGroup and its buttons to cooperate
   * properly the value has to be one of the values of the buttons belonging 
   * to this group.</p>
   */
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
  }

  /**
   * @deprecated
   */
  public Format getFormatter() {
    return null;
  }

  /** 
   * <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p>
   */
  public static String retrieveIconName() {
    return "resources/images/icons/radiobuttongroup.gif";
  }    
  
}