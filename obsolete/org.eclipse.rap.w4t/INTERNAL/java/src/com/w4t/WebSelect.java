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
 * <p>A <code>WebSelect</code> presents a list of items for selection as a 
 * pop-up (default) or a as a list box with scrollbars.</p>
 * 
 * <p>A WebSelect encapsulates the HTML &lt;select&gt; element.</p>
 */
public class WebSelect
  extends WebComponent 
  implements SimpleComponent, IInputValueHolder, IFocusable
{

  private static final String[] EMPTY_ITEMS = new String[ 0 ];
  
  /** list values for manual initialisation */
  private String[] item = EMPTY_ITEMS;
  /** flag: if the first item of the choice is empty */
  private boolean useEmptyItem = false;
  /** Number of items which are displayed in the select box */
  private int size = 1;
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;  
  
  private String value = "";

  private boolean updatable = true;
  private Format formatter = null;

  
  /** 
   * Returns a clone of this WebSelect.
   */
  public Object clone() throws CloneNotSupportedException {
    WebSelect result = ( WebSelect )super.clone();
    result.item = ( String[] )item.clone();
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

  /** 
   * Adds the specified WebItemListener to receive WebItemEvents from
   * this WebSelect. WebItemEvents occur when a user changes the
   * value in the select field of this WebSelect
   * 
   * @param listener the WebItemListener
   */
  public void addWebItemListener( final WebItemListener listener ) {
    WebItemEvent.addListener( this, listener );
  }

  /** 
   * Removes the specified WebItemListener to receive WebItemEvents from
   * this WebSelect. WebItemEvents occur when a user changes the
   * value in the select field of this WebSelect
   * 
   * @param listener the WebItemListener
   */
  public void removeWebItemListener( final WebItemListener listener ) {
    WebItemEvent.removeListener( this, listener );
  }

  /**
   * Adds the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebSelect. WebFocusGainedEvents
   * occur if the depending html select field gets the focus
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void addWebFocusGainedListener( final WebFocusGainedListener lsnr ) {
    WebFocusGainedEvent.addListener( this, lsnr );
  }

  /**
   * Removes the specified WebFocusGainedListener to receive
   * WebFocusGainedEvents from this WebSelect. WebFocusGainedEvents
   * occur if the depending html select field gets the focus.
   * 
   * @param lsnr the WebFocusGainedListener
   */
  public void removeWebFocusGainedListener( final WebFocusGainedListener lsnr ){
    WebFocusGainedEvent.removeListener( this, lsnr );
  }
  
  
  // attribute getters and setters
  ////////////////////////////////


  /** 
   * Sets a list of name=value pairs used as list entries of the WebSelect.
   * the name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   * If no value should be used, only enter a String for the name.
   */
  public void setItem( final String[] item ) {
    ParamCheck.notNull( item, "item" );
    for( int i = 0; i < item.length; i++ ) {
      if( item[ i ] == null ) {
        String msg = "Parameter 'item' must not contain null fields.";
        throw new IllegalArgumentException( msg );
      }
    }
    this.item = item;
  }

  /** 
   * Returns the list of name=value pairs used as list entries of the WebSelect.
   * the name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   */
  public String[] getItem() {
    return ( String[] )item.clone();
  }

  /**
   * Sets a name=value pair to the specified index
   * used as a list entry of the WebSelect.
   * the name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   * If no value should be used, only enter a String for the name.
   */
  public void setItem( final int index, final String item ) {
    ParamCheck.notNull( item, "item" );
    this.item[ index ] = item;
  }

  /** 
   * Returns the name=value pair at the specified index, which is
   * used as a list entry of the WebSelect.
   * the name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   * If no value should be used, only enter a String for the name.
   */
  public String getItem( final int index ) {
    return item[ index ];
  }

  /** 
   * Adds the name=value pair, which is used as a list entry of the WebSelect.
   * the name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   * If no value should be used, only enter a String for the name.
   */
  public void addItem( final String item ) {
    ParamCheck.notNull( item, "item" );
    String[] buffer = new String[ this.item.length + 1 ];
    System.arraycopy( this.item, 0, buffer, 0, this.item.length );
    buffer[ this.item.length ] = item;
    this.item = buffer;
  }

  /** 
   * Removes the name=value pair, which is used as a list entry of the
   * WebSelect. the name is used for the entry after the option tag of the
   * select, the value is used for the attribute value of the option tag.
   * If no value should be used, only enter a String for the name.
   */
  public void removeItem( final String item ) {
    ParamCheck.notNull( item, "item" );
    boolean found = false;
    int foundIndex = -1;
    for( int i = 0; !found && i < this.item.length; i++ ) {
      if( this.item[ i ].equals( item ) ) {
        found = true;
        foundIndex = i;
      }
    }
    if( found ) {
      String[] buffer = new String[ this.item.length - 1 ];
      int bufferCounter = 0;
      for( int i = 0; i < this.item.length; i++ ) {
        if( i != foundIndex ) {
          buffer[ bufferCounter ] = this.item[ i ];
          bufferCounter++;
        }
      }
      this.item = buffer;
    }
  }

  /** 
   * Returns the number of item entries of this WebSelect. 
   */
  public int getItemCount() {
    return item.length;
  }

  /** 
   * Returns the name of the name=value pair at the specified index.
   * The name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   */
  public String getItemName( final int index ) {
    String itemEntry = item[ index ];
    String result = "";
    int pos = itemEntry.indexOf( "=" );
    if( pos != -1 ) {
      result = itemEntry.substring( 0, pos );
    }
    return result;
  }

  /** 
   * Returns the value of the name=value pair at the specified index
   * The name is used for the entry after the option tag of the select,
   * the value is used for the attribute value of the option tag.
   */
  public String getItemValue( final int index ) {
    String itemEntry = item[ index ];
    String result = "";
    int pos = itemEntry.indexOf( "=" );
    if( pos == -1 ) {
      result = itemEntry;
    } else {
      result = itemEntry.substring( pos + 1, itemEntry.length() );
    }
    return result;
  }

  /** 
   * Sets whether the first item of this WebSelect's item list is an empty
   * item. 
   */
  public void setUseEmptyItem( final boolean useEmptyItem ) {
    this.useEmptyItem = useEmptyItem;
  }

  /** 
   * Returns whether the first item of this WebSelect's item list is an empty
   * item.
   */
  public boolean isUseEmptyItem() {
    return useEmptyItem;
  }

  /** 
   * Returns the index of the selected item of this WebSelect or
   * -1 if no item is selected.
   */
  public int getSelectedItemIndex() {
    String selectedItem = getValue();
    int selectedItemIndex = -1;
    for( int i = 0; selectedItemIndex == -1 && i < item.length; i++ ) {
      if( getItemValue( i ).equals( selectedItem ) ) {
        selectedItemIndex = i;
      }
    }
    return selectedItemIndex;
  }

  /**
   * Sets the number of items which are displayed in the select box.
   */
  public void setSize( final int size ) {
   if( size < 1 ) {
     this.size = 1;
   } else {
     this.size = size;
   }
  }

  /** 
   * Returns the number of items which are displayed in the select box.
   */
  public int getSize() {
   return size;
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
    return "resources/images/icons/select.gif";
  }

  
  ////////////////////////////////////
  // interface methods of IValueHolder 

  public void setValue( final String value ) {
    this.value = value == null ? "" : value;
  }

  public String getValue() {
    String result = value;
    if( !isUseEmptyItem() && "".equals( value ) ) {
      if( item.length > 0 ) {
        result = getItemValue( 0 );
      }
    }
    return result;
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