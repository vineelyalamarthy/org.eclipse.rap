/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.theme.IThemeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.swt.internal.widgets.treekit.TreeThemeAdapter;


/**
 * Instances of this class represent a column in a tree widget.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Move, Resize, Selection</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @since 1.0
 */
public class TreeColumn extends Item {

  Tree parent;
  String displayText = "";
  int width, itemImageWidth;
  boolean moveable, resizable = true;
  int sort = SWT.NONE;
  String toolTipText;
  int itemImageCount = 0;
  static final int SORT_INDICATOR_WIDTH = 10;
  static final int MARGIN_IMAGE = 3;

  /**
   * Constructs a new instance of this class given its parent (which must be a
   * <code>Tree</code>) and a style value describing its behavior and
   * appearance. The item is added to the end of the items maintained by its
   * parent.
   * <p>
   * The style value is either one of the style constants defined in class
   * <code>SWT</code> which is applicable to instances of this class, or must be
   * built by <em>bitwise OR</em>'ing together (that is, using the
   * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
   * constants. The class description lists the style constants that are
   * applicable to the class. Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a composite control which will be the parent of the new
   *          instance (cannot be null)
   * @param style the style of control to construct
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the parent</li>
   *              <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed
   *              subclass</li>
   *              </ul>
   * @see SWT#LEFT
   * @see SWT#RIGHT
   * @see SWT#CENTER
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public TreeColumn( final Tree parent, final int style ) {
    this( parent, style, checkNull( parent ).columnHolder.size() );
  }

  /**
   * Constructs a new instance of this class given its parent (which must be a
   * <code>Tree</code>), a style value describing its behavior and appearance,
   * and the index at which to place it in the items maintained by its parent.
   * <p>
   * The style value is either one of the style constants defined in class
   * <code>SWT</code> which is applicable to instances of this class, or must be
   * built by <em>bitwise OR</em>'ing together (that is, using the
   * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
   * constants. The class description lists the style constants that are
   * applicable to the class. Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a composite control which will be the parent of the new
   *          instance (cannot be null)
   * @param style the style of control to construct
   * @param index the zero-relative index to store the receiver in its parent
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   *              <li>ERROR_INVALID_RANGE - if the index is not between 0 and
   *              the number of elements in the parent (inclusive)</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the parent</li>
   *              <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed
   *              subclass</li>
   *              </ul>
   * @see SWT#LEFT
   * @see SWT#RIGHT
   * @see SWT#CENTER
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   */
  public TreeColumn( final Tree parent, final int style, final int index ) {
    super( parent, checkStyle( style ) );
    if( !( 0 <= index && index <= parent.columnHolder.size() ) ) {
      error( SWT.ERROR_INVALID_RANGE );
    }
    this.parent = parent;
    parent.createItem( this, index );
  }

  /**
   * Adds the listener to the collection of listeners who will be notified when
   * the control is moved or resized, by sending it one of the messages defined
   * in the <code>ControlListener</code> interface.
   *
   * @param listener the listener which should be notified
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see ControlListener
   * @see #removeControlListener
   */
  public void addControlListener( final ControlListener listener ) {
    checkWidget();
    ControlEvent.addListener( this, listener );
  }

  /**
   * Adds the listener to the collection of listeners who will be notified when
   * the control is selected by the user, by sending it one of the messages
   * defined in the <code>SelectionListener</code> interface.
   * <p>
   * <code>widgetSelected</code> is called when the column header is selected.
   * <code>widgetDefaultSelected</code> is not called.
   * </p>
   *
   * @param listener the listener which should be notified when the control is
   *          selected by the user
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see SelectionListener
   * @see #removeSelectionListener
   * @see SelectionEvent
   */
  public void addSelectionListener( final SelectionListener listener ) {
    checkWidget();
    SelectionEvent.addListener( this, listener );
  }

  static Tree checkNull( final Tree tree ) {
    if( tree == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    return tree;
  }

  static int checkStyle( final int style ) {
    return checkBits( style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0 );
  }

  public void dispose() {
    if( isDisposed() ) {
      return;
    }
    dispose( true );
  }

  void dispose( final boolean notifyParent ) {
    super.dispose(); /* super is intentional here */
    // if (notifyParent) parent.destroyItem (this);
    parent = null;
  }

  /**
   * Returns a value which describes the position of the text or image in the
   * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code> or
   * <code>CENTER</code>.
   *
   * @return the alignment
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int getAlignment() {
    checkWidget();
    if( ( style & SWT.CENTER ) != 0 ) {
      return SWT.CENTER;
    }
    if( ( style & SWT.RIGHT ) != 0 ) {
      return SWT.RIGHT;
    }
    return SWT.LEFT;
  }

  /*
   * Returns the width of the header's content (image + text + sort arrow +
   * internal margins)
   */
  int getContentWidth() {
    int contentWidth = 0;
    if( text.length() > 0 ) {
      contentWidth += TextSizeDetermination.textExtent( parent.getFont(),
                                                        text,
                                                        0 ).x;
    }
    if( image != null ) {
      contentWidth += image.getBounds().width;
      if( text.length() > 0 ) {
        contentWidth += MARGIN_IMAGE;
      }
    }
    if( sort != SWT.NONE ) {
      contentWidth += SORT_INDICATOR_WIDTH;
      if( text.length() > 0 || image != null ) {
        contentWidth += MARGIN_IMAGE;
      }
    }
    TreeThemeAdapter themeAdapter
      = ( TreeThemeAdapter )parent.getAdapter( IThemeAdapter.class );
    contentWidth += themeAdapter.getHeaderPadding( parent ).width;
    return contentWidth;
  }

  /**
   * Gets the moveable attribute. A column that is not moveable cannot be
   * reordered by the user by dragging the header but may be reordered by the
   * programmer.
   *
   * @return the moveable attribute
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see Tree#getColumnOrder()
   * @see Tree#setColumnOrder(int[])
   * @see TreeColumn#setMoveable(boolean)
   * @see SWT#Move
   * @since 1.0
   */
  public boolean getMoveable() {
    checkWidget();
    return moveable;
  }

  /**
   * Returns the receiver's parent, which must be a <code>Tree</code>.
   *
   * @return the receiver's parent
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public Tree getParent() {
    checkWidget();
    return parent;
  }

  int getIndex() {
    for( int i = 0; i < parent.columnHolder.size(); i++ ) {
      if( parent.columnHolder.getItem( i ) == this ) {
        return i;
      }
    }
    return -1;
  }

  int getPreferredWidth() {
    if( !parent.getHeaderVisible() ) {
      return 0;
    }
    int result = getContentWidth();
    return result;
  }

  /**
   * Gets the resizable attribute. A column that is not resizable cannot be
   * dragged by the user but may be resized by the programmer.
   *
   * @return the resizable attribute
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public boolean getResizable() {
    checkWidget();
    return resizable;
  }

  /**
   * Returns the receiver's tool tip text, or null if it has not been set.
   *
   * @return the receiver's tool tip text
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @since 1.0
   */
  public String getToolTipText() {
    checkWidget();
    return toolTipText;
  }

  /**
   * Gets the width of the receiver.
   *
   * @return the width
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int getWidth() {
    checkWidget();
    return width;
  }

  /**
   * Causes the receiver to be resized to its preferred size. For a composite,
   * this involves computing the preferred size from its layout, if there is
   * one.
   *
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void pack() {
    // TODO: [if] does not respect inner items width if expanded
    checkWidget();
    TreeItem[] availableItems = parent.getItems();
    int index = getIndex();
    int newWidth;
    newWidth = Graphics.stringExtent( parent.getFont(), getText() ).x;
    newWidth = Math.max( getPreferredWidth(), newWidth );
    if( image != null ) {
      newWidth += image.getBounds().width + MARGIN_IMAGE;
    }
    if( parent.getSortColumn() == this && parent.getSortDirection() != SWT.NONE )
    {
      newWidth += SORT_INDICATOR_WIDTH + MARGIN_IMAGE;
    }
    for( int i = 0; i < availableItems.length; i++ ) {
      int width = availableItems[ i ].getPreferredWidth( index, false );
      /* ensure that receiver and parent were not disposed in a callback */
      if( parent.isDisposed() || isDisposed() ) {
        return;
      }
      if( !availableItems[ i ].isDisposed() ) {
        newWidth = Math.max( newWidth, width );
      }
    }
    // Mimic Windows behaviour that has a minimal width
    if( newWidth < 12 ) {
      newWidth = 12;
    }
    // if (newWidth != width) parent.updateColumnWidth (this, newWidth);
    setWidth( newWidth );
  }

  /**
   * Removes the listener from the collection of listeners who will be notified
   * when the control is moved or resized.
   *
   * @param listener the listener which should no longer be notified
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see ControlListener
   * @see #addControlListener
   */
  public void removeControlListener( final ControlListener listener ) {
    checkWidget();
    ControlEvent.removeListener( this, listener );
  }

  /**
   * Removes the listener from the collection of listeners who will be notified
   * when the control is selected by the user.
   *
   * @param listener the listener which should no longer be notified
   * @exception IllegalArgumentException <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener( final SelectionListener listener ) {
    checkWidget();
    if( listener == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    SelectionEvent.removeListener( this, listener );
  }

  /**
   * Controls how text and images will be displayed in the receiver. The
   * argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
   * <code>CENTER</code>.
   *
   * @param alignment the new alignment
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void setAlignment( final int alignment ) {
    checkWidget();
    if( ( alignment & ( SWT.LEFT | SWT.RIGHT | SWT.CENTER ) ) == 0 ) {
      return;
    }
    int newAlignment = checkBits( alignment,
                                  SWT.LEFT,
                                  SWT.CENTER,
                                  SWT.RIGHT,
                                  0,
                                  0,
                                  0 );
    if( ( style & newAlignment ) != 0 ) {
      return; /* same value */
    }
    style &= ~( SWT.LEFT | SWT.CENTER | SWT.RIGHT );
    style |= newAlignment;
    // if (getOrderIndex () == 0) return; /* no update needed since first
    // ordered column appears left-aligned */
  }

  /**
   * Sets the moveable attribute. A column that is moveable can be reordered by
   * the user by dragging the header. A column that is not moveable cannot be
   * dragged by the user but may be reordered by the programmer.
   *
   * @param moveable the moveable attribute
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see Tree#setColumnOrder(int[])
   * @see Tree#getColumnOrder()
   * @see TreeColumn#getMoveable()
   * @see SWT#Move
   * @since 1.0
   */
  public void setMoveable( final boolean moveable ) {
    checkWidget();
    this.moveable = moveable;
  }

  /**
   * Sets the resizable attribute. A column that is not resizable cannot be
   * dragged by the user but may be resized by the programmer.
   *
   * @param value the resize attribute
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void setResizable( final boolean value ) {
    checkWidget();
    resizable = value;
  }

  void setSortDirection( final int value ) {
    if( value == sort ) {
      return;
    }
    sort = value;
  }

  public void setText( final String value ) {
    checkWidget();
    if( value == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    if( value.equals( text ) ) {
      return; /* same value */
    }
    super.setText( value );
  }

  /**
   * Sets the receiver's tool tip text to the argument, which may be null
   * indicating that no tool tip text should be shown.
   *
   * @param string the new tool tip text (or null)
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @since 1.0
   */
  public void setToolTipText( final String string ) {
    checkWidget();
    if( toolTipText == string ) {
      return;
    }
    if( toolTipText != null && toolTipText.equals( string ) ) {
      return;
    }
    toolTipText = string;
  }

  /**
   * Sets the width of the receiver.
   *
   * @param value the new width
   * @exception SWTException <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void setWidth( final int value ) {
    // TODO: [bm] add support for ellipsis
    checkWidget();
    if( value < 0 ) {
      return;
    }
    if( width == value ) {
      return; /* same value */
    }
    this.width = value;
    parent.updateScrollBars();
    int eventId = ControlEvent.CONTROL_RESIZED;
    ControlEvent event = new ControlEvent( this, eventId );
    event.processEvent();
    // parent.updateColumnWidth (this, value);
  }

  void releaseParent() {
    super.releaseParent();
    parent.destroyColumn( this );
  }

  //////////////
  // Left offset

  final int getLeft() {
    int result = 0;
    TreeColumn[] columns = parent.getColumns();
    int[] columnOrder = parent.getColumnOrder();
    int orderedIndex = -1;
    for( int i = 0; orderedIndex == -1 && i < columnOrder.length; i++ ) {
      if( columnOrder[ i ] == parent.indexOf( this ) ) {
        orderedIndex = i;
      }
    }
    for( int i = 0; i < orderedIndex; i++ ) {
      result += columns[ columnOrder[ i ] ].getWidth();
    }
    return result;
  }
}
