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
import org.eclipse.rwt.lifecycle.ProcessActionRunner;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.events.SetDataEvent;
import org.eclipse.swt.internal.widgets.*;
import org.eclipse.swt.internal.widgets.tablekit.TableThemeAdapter;


/**
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issues
 * notification when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Table</code> whose
 * <code>TableItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * tables that are very large or for which <code>TableItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:
 * <code><pre>
 *  final Table table = new Table (parent, SWT.VIRTUAL | SWT.BORDER);
 *  table.setItemCount (1000000);
 *  table.addListener (SWT.SetData, new Listener () {
 *      public void handleEvent (Event event) {
 *          TableItem item = (TableItem) event.item;
 *          int index = table.indexOf (item);
 *          item.setText ("Item " + index);
 *          System.out.println (item.getText ());
 *      }
 *  });
 * </pre></code>
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData<!--, MeasureItem, EraseItem, PaintItem--></dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @since 1.0
 */
public class Table extends Composite {

  // handle the fact that we have two item types to deal with
  private final class CompositeItemHolder implements IItemHolderAdapter {
    public void add( final Item item ) {
      if( item instanceof TableColumn ) {
        columnHolder.add( item );
      } else {
        String msg = "Only TableColumns may be added to CompositeItemHolder";
        throw new IllegalArgumentException( msg );
      }
    }
    public void insert( final Item item, final int index ) {
      if( item instanceof TableColumn ) {
        columnHolder.insert( item, index );
      } else {
        String msg = "Only TableColumns may be inserted to CompositeItemHolder";
        throw new IllegalArgumentException( msg );
      }
    }
    public void remove( final Item item ) {
      if( item instanceof TableColumn ) {
        columnHolder.remove( item );
      } else {
        String msg
          = "Only TableColumns may be removed from CompositeItemHolder";
        throw new IllegalArgumentException( msg );
      }
    }
    public Item[] getItems() {
      TableItem[] items = getCreatedItems();
      Item[] columns = columnHolder.getItems();
      Item[] result = new Item[ columns.length + items.length ];
      System.arraycopy( columns, 0, result, 0, columns.length );
      System.arraycopy( items, 0, result, columns.length, items.length );
      return result;
    }
  }

  private final class TableAdapter implements ITableAdapter {

    private String toolTipText;
    private ICellToolTipProvider provider;

    public int getCheckWidth() {
      return Table.this.getCheckWidth();
    }

    public int getItemImageWidth( final int columnIndex ) {
      int result = 0;
      if( Table.this.hasColumnImages( columnIndex ) ) {
        result = Table.this.getItemImageSize().x;
      }
      return result;
    }

    public int getFocusIndex() {
      return Table.this.focusIndex;
    }

    public void setFocusIndex( final int focusIndex ) {
      Table.this.setFocusIndex( focusIndex );
    }

    public int getLeftOffset() {
      return Table.this.leftOffset;
    }

    public void setLeftOffset( final int leftOffset ) {
      Table.this.leftOffset = leftOffset;
    }

    public void checkData() {
      Table.this.checkData();
    }

    public void checkData( final int index ) {
      if( ( Table.this.style & SWT.VIRTUAL ) != 0  ) {
        ProcessActionRunner.add( new Runnable() {
          public void run() {
            if( index >= 0 && index < Table.this.itemCount ) {
              TableItem item = Table.this._getItem( index );
              if( !item.isDisposed() ) {
                Table.this.checkData( item, index );
              }
            }
          }
        } );
      }
    }

    public int getDefaultColumnWidth() {
      int result = 0;
      TableItem[] items = Table.this.getCachedItems();
      for( int i = 0; i < items.length; i++ ) {
        result = Math.max( result, items[ i ].getPackWidth( 0 ) );
      }
      return result;
    }

    public int getColumnLeft( final TableColumn column ) {
      int index = Table.this.indexOf( column );
      return Table.this.getColumn( index ).getLeft();
    }

    public boolean isItemVisible( final TableItem item ) {
      int index = Table.this.indexOf( item );
      return index != -1 && Table.this.isItemVisible( index );
    }

    public boolean isItemVirtual( final int index ) {
      boolean result = false;
      if( ( style & SWT.VIRTUAL ) != 0 ) {
        TableItem item = Table.this.items[ index ];
        result = item == null || !item.cached;
      }
      return result;
    }

    public TableItem[] getCachedItems() {
      return Table.this.getCachedItems();
    }

    public TableItem[] getCreatedItems() {
      return Table.this.getCreatedItems();
    }

    public boolean hasHScrollBar() {
      return Table.this.hasHScrollBar();
    }

    public boolean hasVScrollBar() {
      return Table.this.hasVScrollBar();
    }

    public TableItem getMeasureItem() {
      return Table.this.getMeasureItem();
    }

    public ICellToolTipProvider getCellToolTipProvider() {
      return provider;
    }

    public void setCellToolTipProvider( final ICellToolTipProvider provider ) {
      this.provider = provider;
    }

    public String getToolTipText() {
      return toolTipText;
    }

    public void setToolTipText( final String toolTipText ) {
      this.toolTipText = toolTipText;
    }
  }

  private final class ResizeListener extends ControlAdapter {
    public void controlResized( final ControlEvent event ) {
      if( ( Table.this.style & SWT.VIRTUAL ) != 0 ) {
        Table.this.checkData();
      }
      Table.this.updateScrollBars();
    }
  }

  /**
   * <strong>IMPORTANT:</strong> This field is <em>not</em> part of the SWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by SWT. It should never be accessed from
   * application code.
   */
  public static final String ALWAYS_HIDE_SELECTION
    = Table.class.getName() + "#alwaysHideSelection";

  /**
   * <strong>IMPORTANT:</strong> This field is <em>not</em> part of the SWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by SWT. It should never be accessed from
   * application code.
   */
  public static final String ENABLE_CELL_TOOLTIP
    = Table.class.getName() + "#enableCellToolTip";

  private static final int GRID_WIDTH = 1;

  private static final int[] EMPTY_SELECTION = new int[ 0 ];

  final private CompositeItemHolder itemHolder;
  private final ITableAdapter tableAdapter;
  private final ResizeListener resizeListener;
  private int itemCount;
  private TableItem[] items;
  private final ItemHolder columnHolder;
  private int[] columnImageCount;
  private int[] columnOrder;
  private int[] selection;
  private boolean linesVisible;
  private boolean headerVisible;
  private boolean hasVScrollBar;
  private boolean hasHScrollBar;
  private ScrollBar verticalBar;
  private ScrollBar horizontalBar;
  private int topIndex;
  int leftOffset;
  private int focusIndex;
  private TableColumn sortColumn;
  private int sortDirection;
  private Point itemImageSize;
  private Rectangle bufferedCellPadding = null;
  private int bufferedCellSpacing = -1;

  /**
   * Constructs a new instance of this class given its parent
   * and a style value describing its behavior and appearance.
   * <p>
   * The style value is either one of the style constants defined in
   * class <code>SWT</code> which is applicable to instances of this
   * class, or must be built by <em>bitwise OR</em>'ing together
   * (that is, using the <code>int</code> "|" operator) two or more
   * of those <code>SWT</code> style constants. The class description
   * lists the style constants that are applicable to the class.
   * Style bits are also inherited from superclasses.
   * </p>
   *
   * @param parent a composite control which will be the parent of the new instance (cannot be null)
   * @param style the style of control to construct
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
   *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
   * </ul>
   *
   * @see SWT#SINGLE
   * @see SWT#MULTI
   * @see SWT#CHECK
   * @see SWT#FULL_SELECTION
   * @see SWT#HIDE_SELECTION
   * @see SWT#VIRTUAL
   * @see Widget#checkSubclass
   * @see Widget#getStyle
   *
   * @since 1.0
   */
  public Table( final Composite parent, final int style ) {
    super( parent, checkStyle( style ) );
    focusIndex = -1;
    sortDirection = SWT.NONE;
    tableAdapter = new TableAdapter();
    itemHolder = new CompositeItemHolder();
    columnHolder = new ItemHolder( TableColumn.class );
    setTableEmpty();
    createScrollBars();
    selection = EMPTY_SELECTION;
    resizeListener = new ResizeListener();
    addControlListener( resizeListener );
  }

  void initState() {
    state &= ~( /* CANVAS | */ THEME_BACKGROUND );
  }

  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IItemHolderAdapter.class ) {
      result = itemHolder;
    } else if( adapter == ITableAdapter.class ) {
      result = tableAdapter;
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  ///////////////////////////
  // Column handling methods

  /**
   * Returns the number of columns contained in the receiver.
   * If no <code>TableColumn</code>s were created by the programmer,
   * this value is zero, despite the fact that visually, one column
   * of items may be visible. This occurs when the programmer uses
   * the table like a list, adding items but never creating a column.
   *
   * @return the number of columns
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getColumnCount() {
    checkWidget();
    return columnHolder.size();
  }

  /**
   * Returns an array of <code>TableColumn</code>s which are the
   * columns in the receiver.  Columns are returned in the order
   * that they were created.  If no <code>TableColumn</code>s were
   * created by the programmer, the array is empty, despite the fact
   * that visually, one column of items may be visible. This occurs
   * when the programmer uses the table like a list, adding items but
   * never creating a column.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of items, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return the items in the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#getColumnOrder()
   * @see Table#setColumnOrder(int[])
   * @see TableColumn#getMoveable()
   * @see TableColumn#setMoveable(boolean)
   * @see SWT#Move
   */
  public TableColumn[] getColumns() {
    checkWidget();
    return ( TableColumn[] )columnHolder.getItems();
  }

  /**
   * Returns the column at the given, zero-relative index in the
   * receiver. Throws an exception if the index is out of range.
   * Columns are returned in the order that they were created.
   * If no <code>TableColumn</code>s were created by the programmer,
   * this method will throw <code>ERROR_INVALID_RANGE</code> despite
   * the fact that a single column of data may be visible in the table.
   * This occurs when the programmer uses the table like a list, adding
   * items but never creating a column.
   *
   * @param index the index of the column to return
   * @return the column at the given index
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#getColumnOrder()
   * @see Table#setColumnOrder(int[])
   * @see TableColumn#getMoveable()
   * @see TableColumn#setMoveable(boolean)
   * @see SWT#Move
   */
  public TableColumn getColumn( final int index ) {
    checkWidget();
    return ( TableColumn )columnHolder.getItem( index );
  }

  /**
   * Searches the receiver's list starting at the first column
   * (index 0) until a column is found that is equal to the
   * argument, and returns the index of that column. If no column
   * is found, returns -1.
   *
   * @param tableColumn the search column
   * @return the index of the column
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf( final TableColumn tableColumn ) {
    checkWidget();
    if( tableColumn == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    return columnHolder.indexOf( tableColumn );
  }

  /**
   * Sets the order that the items in the receiver should
   * be displayed in to the given argument which is described
   * in terms of the zero-relative ordering of when the items
   * were added.
   *
   * @param order the new order to display the items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
   * </ul>
   *
   * @see Table#getColumnOrder()
   * @see TableColumn#getMoveable()
   * @see TableColumn#setMoveable(boolean)
   * @see SWT#Move
   */
  public void setColumnOrder( final int[] order ) {
    checkWidget();
    if( order == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    int columnCount = getColumnCount();
    if( order.length != columnCount ) {
      SWT.error( SWT.ERROR_INVALID_ARGUMENT );
    }
    if( columnCount > 0 ) {
      int[] oldOrder = new int[ columnCount ];
      System.arraycopy( columnOrder, 0, oldOrder, 0, columnOrder.length );
      boolean reorder = false;
      boolean[] seen = new boolean[ columnCount ];
      for( int i = 0; i < order.length; i++ ) {
        int index = order[ i ];
        if( index < 0 || index >= columnCount ) {
          SWT.error( SWT.ERROR_INVALID_RANGE );
        }
        if( seen[ index ] ) {
          SWT.error( SWT.ERROR_INVALID_ARGUMENT );
        }
        seen[ index ] = true;
        if( index != oldOrder[ i ] ) {
          reorder = true;
        }
      }
      if( reorder ) {
        System.arraycopy( order, 0, columnOrder, 0, columnOrder.length );
        for( int i = 0; i < seen.length; i++ ) {
          if( oldOrder[ i ] != columnOrder[ i ] ) {
            TableColumn column = getColumn( columnOrder[ i ] );
            int controlMoved = ControlEvent.CONTROL_MOVED;
            ControlEvent controlEvent = new ControlEvent( column, controlMoved );
            controlEvent.processEvent();
          }
        }
      }
    }
  }

  /**
   * Returns an array of zero-relative integers that map
   * the creation order of the receiver's items to the
   * order in which they are currently being displayed.
   * <p>
   * Specifically, the indices of the returned array represent
   * the current visual order of the items, and the contents
   * of the array represent the creation order of the items.
   * </p><p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of items, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return the current visual order of the receiver's items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#setColumnOrder(int[])
   * @see TableColumn#getMoveable()
   * @see TableColumn#setMoveable(boolean)
   * @see SWT#Move
   */
  public int[] getColumnOrder() {
    checkWidget();
    int[] result;
    if( columnHolder.size() == 0 ) {
      result = new int[ 0 ];
    } else {
      result = new int[ columnOrder.length ];
      System.arraycopy( columnOrder, 0, result, 0, columnOrder.length );
    }
    return result;
  }

  ////////////////////////
  // Item handling methods

  /**
   * Sets the number of items contained in the receiver.
   *
   * @param newCount the number of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  // TODO [rh] Consider calling RWTLifeCycle#fakeRedraw at the end of this
  //      method to ensure that items are drawn when inside the visible bounds
  public void setItemCount( final int newCount ) {
    checkWidget();
    int count = Math.max( 0, newCount );
    if( count != itemCount ) {
      while( count < itemCount ) {
        TableItem item = items[ count ];
        if( item != null && !item.isDisposed() ) {
          item.dispose();
        } else {
          destroyItem( null, count );
        }
      }
      int length = Math.max( 4, ( count + 3 ) / 4 * 4 );
      TableItem[] newItems = new TableItem[ length ];
      System.arraycopy( items, 0, newItems, 0, Math.min( count, itemCount ) );
      items = newItems;
      if( ( style & SWT.VIRTUAL ) == 0 ) {
        for( int i = itemCount; i < count; i++ ) {
          items[ i ] = new TableItem( this, SWT.NONE, i, true );
        }
      }
      itemCount = count;
      adjustTopIndex();
      if( focusIndex > itemCount - 1 ) {
        adjustFocusIndex();
      }
      if( ( style & SWT.VIRTUAL ) != 0 ) {
        updateScrollBars();
      }
    }
  }

  /**
   * Returns the number of items contained in the receiver.
   *
   * @return the number of items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getItemCount() {
    checkWidget();
    return itemCount;
  }

  /**
   * Returns a (possibly empty) array of <code>TableItem</code>s which
   * are the items in the receiver.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its list of items, so modifying the array will
   * not affect the receiver.
   * </p>
   *
   * @return the items in the receiver
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem[] getItems() {
    checkWidget();
    TableItem[] result = new TableItem[ itemCount ];
    if( ( style & SWT.VIRTUAL ) != 0 ) {
      for( int i = 0; i < itemCount; i++ ) {
        result[ i ] = _getItem( i );
      }
    } else {
      System.arraycopy( items, 0, result, 0, itemCount );
    }
    return result;
  }

  /**
   * Returns the item at the given, zero-relative index in the
   * receiver. Throws an exception if the index is out of range.
   *
   * @param index the index of the item to return
   * @return the item at the given index
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem getItem( final int index ) {
    checkWidget();
    if( !( 0 <= index && index < itemCount ) ) {
      SWT.error( SWT.ERROR_INVALID_RANGE );
    }
    return _getItem( index );
  }

  /**
   * Returns the item at the given point in the receiver
   * or null if no such item exists. The point is in the
   * coordinate system of the receiver.
   * <p>
   * The item that is returned represents an item that could be selected by the user.
   * For example, if selection only occurs in items in the first column, then null is
   * returned if the point is outside of the item.
   * Note that the SWT.FULL_SELECTION style hint, which specifies the selection policy,
   * determines the extent of the selection.
   * </p>
   *
   * @param point the point used to locate the item
   * @return the item at the given point, or null if the point is not in a selectable item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem getItem( final Point point ) {
    checkWidget();
    if( point == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    TableItem result = null;
    int headerHeight = getHeaderHeight();
    Rectangle itemArea = getClientArea();
    itemArea.y += headerHeight;
    if( itemArea.contains( point ) ) {
      int itemHeight = getItemHeight();
      int index = ( ( point.y - headerHeight ) / itemHeight ) - 1;
      if( point.y == headerHeight || point.y % itemHeight != 0 ) {
        index++;
      }
      index += topIndex;
      if( index >= 0 && index < itemCount ) {
        result = _getItem( index );
      }
    }
    return result;
  }

  /**
   * Searches the receiver's list starting at the first item
   * (index 0) until an item is found that is equal to the
   * argument, and returns the index of that item. If no item
   * is found, returns -1.
   *
   * @param item the search item
   * @return the index of the item
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int indexOf( final TableItem item ) {
    checkWidget();
    if( item == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    return item.parent == this ? item.index : -1;
  }

  /**
   * Removes all of the items from the receiver.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void removeAll() {
    checkWidget();
    while( itemCount > 0 ) {
      removeItem( 0 );
    }
  }

  /**
   * Removes the items from the receiver which are between the given
   * zero-relative start and end indices (inclusive).
   *
   * @param start the start of the range
   * @param end the end of the range
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_INVALID_RANGE - if either the start or end are not
   *              between 0 and the number of elements in the list minus 1
   *              (inclusive)</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void remove( final int start, final int end ) {
    checkWidget();
    if( start <= end ) {
      if( !( 0 <= start && start <= end && end < itemCount ) ) {
        error( SWT.ERROR_INVALID_RANGE );
      }
      for( int i = end; i >= start; i-- ) {
        removeItem( i );
      }
    }
  }

  /**
   * Removes the item from the receiver at the given zero-relative index.
   *
   * @param index the index for the item
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_INVALID_RANGE - if the index is not between 0 and
   *              the number of elements in the list minus 1 (inclusive)</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void remove( final int index ) {
    checkWidget();
    if( !( 0 <= index && index < itemCount ) ) {
      SWT.error( SWT.ERROR_ITEM_NOT_REMOVED );
    }
    removeItem( index );
  }

  /**
   * Removes the items from the receiver's list at the given zero-relative
   * indices.
   *
   * @param indices the array of indices of the items
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_INVALID_RANGE - if the index is not between 0 and
   *              the number of elements in the list minus 1 (inclusive)</li>
   *              <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void remove( final int[] indices ) {
    checkWidget();
    if( indices == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    if( indices.length > 0 ) {
      int[] sortedIndices = new int[ indices.length ];
      System.arraycopy( indices, 0, sortedIndices, 0, indices.length );
      sort( sortedIndices );
      int start = sortedIndices[ sortedIndices.length - 1 ];
      int end = sortedIndices[ 0 ];
      if( !( 0 <= start && start <= end && end < itemCount ) ) {
        SWT.error( SWT.ERROR_INVALID_RANGE );
      }
      int lastValue = -1;
      for( int i = 0; i < sortedIndices.length; i++ ) {
        if( sortedIndices[ i ] != lastValue ) {
          lastValue = sortedIndices[ i ];
          removeItem( sortedIndices[ i ] );
        }
      }
      if( itemCount == 0 ) {
        setTableEmpty();
      }
    }
  }

  /**
   * Clears the item at the given zero-relative index in the receiver.
   * The text, icon and other attributes of the item are set to the default
   * value.  If the table was created with the <code>SWT.VIRTUAL</code> style,
   * these attributes are requested again as needed.
   *
   * @param index the index of the item to clear
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SWT#VIRTUAL
   * @see SWT#SetData
   */
  public void clear( final int index ) {
    checkWidget();
    if( !( 0 <= index && index < itemCount ) ) {
      SWT.error( SWT.ERROR_INVALID_RANGE );
    }
    TableItem item = items[ index ];
    if( item != null ) {
      item.clear();
    }
  }

  /**
   * Removes the items from the receiver which are between the given
   * zero-relative start and end indices (inclusive).  The text, icon
   * and other attributes of the items are set to their default values.
   * If the table was created with the <code>SWT.VIRTUAL</code> style,
   * these attributes are requested again as needed.
   *
   * @param start the start index of the item to clear
   * @param end the end index of the item to clear
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SWT#VIRTUAL
   * @see SWT#SetData
   */
  public void clear( final int start, final int end ) {
    checkWidget();
    if( start <= end ) {
      if( !( 0 <= start && start <= end && end < itemCount ) ) {
        SWT.error( SWT.ERROR_INVALID_RANGE );
      }
      if( start == 0 && end == itemCount - 1 ) {
        clearAll();
      } else {
        for( int i = start; i <= end; i++ ) {
          TableItem item = items[ i ];
          if( item != null ) {
            item.clear();
          }
        }
      }
    }
  }

  /**
   * Clears all the items in the receiver. The text, icon and other
   * attributes of the items are set to their default values. If the
   * table was created with the <code>SWT.VIRTUAL</code> style, these
   * attributes are requested again as needed.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SWT#VIRTUAL
   * @see SWT#SetData
   */
  public void clearAll() {
    checkWidget();
    for( int i = 0; i < itemCount; i++ ) {
      TableItem item = items[ i ];
      if( item != null ) {
        item.clear();
      }
    }
    clearItemImageSize();
  }

  /**
   * Clears the items at the given zero-relative indices in the receiver.
   * The text, icon and other attributes of the items are set to their default
   * values.  If the table was created with the <code>SWT.VIRTUAL</code> style,
   * these attributes are requested again as needed.
   *
   * @param indices the array of indices of the items
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
   *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SWT#VIRTUAL
   * @see SWT#SetData
   */
  public void clear( final int[] indices ) {
    checkWidget();
    if( indices == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    if( indices.length > 0 ) {
      for( int i = 0; i < indices.length; i++ ) {
        if( !( 0 <= indices[ i ] && indices[ i ] < itemCount ) ) {
          SWT.error( SWT.ERROR_INVALID_RANGE );
        }
      }
      for( int i = 0; i < indices.length; i++ ) {
        TableItem item = items[ indices[ i ] ];
        if( item != null ) {
          item.clear();
        }
      }
    }
  }

  /////////////////////////////
  // Selection handling methods

  /**
   * Returns the zero-relative index of the item which is currently
   * selected in the receiver, or -1 if no item is selected.
   *
   * @return the index of the selected item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSelectionIndex() {
    checkWidget();
    int result = -1;
    int topSelectedIndex = -1;
    for( int i = 0; i < selection.length; i++ ) {
      if( focusIndex == selection[ i ] ) {
        result = selection[ i ];
      }
      if( topSelectedIndex == -1 ) {
        topSelectedIndex = selection[ i ];
      } else {
        topSelectedIndex = Math.min( topSelectedIndex, selection[ i ] );
      }
    }
    if( result == -1 ) {
      result = topSelectedIndex;
    }
    return result;
  }

  /**
   * Selects the item at the given zero-relative index in the receiver.
   * The current selection is first cleared, then the new item is selected.
   *
   * @param index the index of the item to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int)
   */
  public void setSelection( final int index ) {
    checkWidget();
    deselectAll();
    select( index );
    if( index < itemCount ) {
      setFocusIndex( index );
    }
    showSelection();
  }

  /**
   * Returns the number of selected items contained in the receiver.
   *
   * @return the number of selected items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getSelectionCount() {
    checkWidget();
    return selection.length;
  }

  /**
   * Selects the items in the range specified by the given zero-relative
   * indices in the receiver. The range of indices is inclusive.
   * The current selection is cleared before the new items are selected.
   * <p>
   * Indices that are out of range are ignored and no items will be selected
   * if start is greater than end.
   * If the receiver is single-select and there is more than one item in the
   * given range, then all indices are ignored.
   * </p>
   *
   * @param start the start index of the items to select
   * @param end the end index of the items to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int,int)
   */
  public void setSelection( final int start, final int end ) {
    checkWidget();
    deselectAll();
    select( start, end );
    if(    end >= 0
        && start <= end
        && ( ( style & SWT.SINGLE ) == 0 || start == end )
        && itemCount != 0
        && start < itemCount )
    {
      setFocusIndex( Math.max( 0, start ) );
    }
    showSelection();
  }

  /**
   * Returns an array of <code>TableItem</code>s that are currently
   * selected in the receiver. The order of the items is unspecified.
   * An empty array indicates that no items are selected.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its selection, so modifying the array will
   * not affect the receiver.
   * </p>
   * @return an array representing the selection
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public TableItem[] getSelection() {
    checkWidget();
    int length = selection.length;
    TableItem[] result = new TableItem[ length ];
    for( int i = 0; i < selection.length; i++ ) {
      result[ i ] = _getItem( selection[ i ] );
    }
    return result;
  }

  /**
   * Selects the items at the given zero-relative indices in the receiver.
   * The current selection is cleared before the new items are selected.
   * <p>
   * Indices that are out of range and duplicate indices are ignored.
   * If the receiver is single-select and multiple indices are specified,
   * then all indices are ignored.
   * </p>
   *
   * @param indices the indices of the items to select
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int[])
   */
  public void setSelection( final int[] indices ) {
    checkWidget();
    if( indices == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    deselectAll();
    select( indices );
    int length = indices.length;
    if( length != 0 && ( ( style & SWT.SINGLE ) == 0 || length <= 1 ) ) {
      setFocusIndex( indices[ 0 ] );
    }
    showSelection();
  }

  /**
   * Sets the receiver's selection to the given item.
   * The current selection is cleared before the new item is selected.
   * <p>
   * If the item is not in the receiver, then it is ignored.
   * </p>
   *
   * @param item the item to select
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSelection( final TableItem item ) {
    checkWidget();
    if( item == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    setSelection( new TableItem[]{ item } );
  }

  /**
   * Sets the receiver's selection to be the given array of items.
   * The current selection is cleared before the new items are selected.
   * <p>
   * Items that are not in the receiver are ignored.
   * If the receiver is single-select and multiple items are specified,
   * then all items are ignored.
   * </p>
   *
   * @param items the array of items
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#deselectAll()
   * @see Table#select(int[])
   * @see Table#setSelection(int[])
   */
  public void setSelection( final TableItem[] items ) {
    checkWidget();
    if( items == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    int[] indices = new int[ items.length ];
    for( int i = 0; i < items.length; i++ ) {
      indices[ i ] = indexOf( items[ i ] );
    }
    setSelection( indices );
  }

  /**
   * Returns the zero-relative indices of the items which are currently
   * selected in the receiver. The order of the indices is unspecified.
   * The array is empty if no items are selected.
   * <p>
   * Note: This is not the actual structure used by the receiver
   * to maintain its selection, so modifying the array will
   * not affect the receiver.
   * </p>
   * @return the array of indices of the selected items
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int[] getSelectionIndices() {
    checkWidget();
    // TODO [rh] directly return a copy of the selection array
    TableItem[] currentSelection = getSelection();
    int[] result = new int[ currentSelection.length ];
    for( int i = 0; i < currentSelection.length; i++ ) {
      result[ i ] = indexOf( currentSelection[ i ] );
    }
    return result;
  }

  /**
   * Returns <code>true</code> if the item is selected,
   * and <code>false</code> otherwise.  Indices out of
   * range are ignored.
   *
   * @param index the index of the item
   * @return the visibility state of the item at the index
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean isSelected( final int index ) {
    checkWidget();
    boolean result = false;
    if( index >= 0 && index < itemCount ) {
      for( int i = 0; !result && i < selection.length; i++ ) {
        result = selection[ i ] == index;
      }
    }
    return result;
  }

  /**
   * Selects the item at the given zero-relative index in the receiver.
   * If the item at the index was already selected, it remains
   * selected. Indices that are out of range are ignored.
   *
   * @param index the index of the item to select
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void select( final int index ) {
    checkWidget();
    if( index >= 0 && index < itemCount ) {
      if( ( style & SWT.SINGLE ) != 0 ) {
        selection = new int[] { index };
      } else {
        if( !isSelected( index ) ) {
          int length = selection.length;
          int[] newSelection = new int[ length + 1 ];
          System.arraycopy( selection, 0, newSelection, 0, length );
          newSelection[ length ] = index;
          selection = newSelection;
        }
      }
    }
  }

  /**
   * Selects the items in the range specified by the given zero-relative
   * indices in the receiver. The range of indices is inclusive.
   * The current selection is not cleared before the new items are selected.
   * <p>
   * If an item in the given range is not selected, it is selected.
   * If an item in the given range was already selected, it remains selected.
   * Indices that are out of range are ignored and no items will be selected
   * if start is greater than end.
   * If the receiver is single-select and there is more than one item in the
   * given range, then all indices are ignored.
   * </p>
   *
   * @param start the start of the range
   * @param end the end of the range
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#setSelection(int,int)
   */
  public void select( final int start, final int end ) {
    checkWidget();
    if(    end >= 0
        && start <= end
        && ( ( style & SWT.SINGLE ) == 0 || start == end ) )
    {
      if( itemCount != 0 && start < itemCount ) {
        int adjustedStart = Math.max( 0, start );
        int adjustedEnd = Math.min( end, itemCount - 1 );
        if( adjustedStart == 0 && adjustedEnd == itemCount - 1 ) {
          selectAll();
        } else {
          for( int i = adjustedStart; i <= adjustedEnd; i++ ) {
            select( i );
          }
        }
      }
    }
  }

  /**
   * Selects the items at the given zero-relative indices in the receiver.
   * The current selection is not cleared before the new items are selected.
   * <p>
   * If the item at a given index is not selected, it is selected.
   * If the item at a given index was already selected, it remains selected.
   * Indices that are out of range and duplicate indices are ignored.
   * If the receiver is single-select and multiple indices are specified,
   * then all indices are ignored.
   * </p>
   *
   * @param indices the array of indices for the items to select
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#setSelection(int[])
   */
  public void select( final int[] indices ) {
    checkWidget();
    if( indices == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    int length = indices.length;
    if( length != 0 && ( ( style & SWT.SINGLE ) == 0 || length <= 1 ) ) {
      for( int i = length - 1; i >= 0; --i ) {
        select( indices[ i ] );
      }
    }
  }

  /**
   * Selects all of the items in the receiver.
   * <p>
   * If the receiver is single-select, do nothing.
   * </p>
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  // TODO [rh] revise: a VIRTUAL table would resolve all its items when
  //      selectAll is called. Compare how SWT handles this.
  public void selectAll() {
    checkWidget();
    if( ( style & SWT.SINGLE ) == 0 ) {
      setSelection( getItems() );
    }
  }

  /**
   * Deselects the item at the given zero-relative index in the receiver.
   * If the item at the index was already deselected, it remains
   * deselected. Indices that are out of range are ignored.
   *
   * @param index the index of the item to deselect
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect( final int index ) {
    checkWidget();
    removeFromSelection( index );
  }

  /**
   * Deselects the items at the given zero-relative indices in the receiver.
   * If the item at the given zero-relative index in the receiver
   * is selected, it is deselected.  If the item at the index
   * was not selected, it remains deselected.  The range of the
   * indices is inclusive. Indices that are out of range are ignored.
   *
   * @param start the start index of the items to deselect
   * @param end the end index of the items to deselect
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect( final int start, final int end ) {
    checkWidget();
    if( start == 0 && end == itemCount - 1 ) {
      deselectAll();
    } else {
      int actualStart = Math.max( 0, start );
      for( int i = actualStart; i <= end; i++ ) {
        removeFromSelection( i );
      }
    }
  }

  /**
   * Deselects the items at the given zero-relative indices in the receiver.
   * If the item at the given zero-relative index in the receiver
   * is selected, it is deselected.  If the item at the index
   * was not selected, it remains deselected. Indices that are out
   * of range and duplicate indices are ignored.
   *
   * @param indices the array of indices for the items to deselect
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselect( final int[] indices ) {
    checkWidget();
    if( indices == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    for( int i = 0; i < indices.length; i++ ) {
      removeFromSelection( indices[ i ] );
    }
  }

  /**
   * Deselects all selected items in the receiver.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void deselectAll() {
    checkWidget();
    selection = EMPTY_SELECTION;
  }

  //////////////////////////////////
  // TopIndex and showItem/Selection

  /**
   * Sets the zero-relative index of the item which is currently
   * at the top of the receiver. This index can change when items
   * are scrolled or new items are added and removed.
   *
   * @param topIndex the index of the top item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setTopIndex( final int topIndex ) {
    checkWidget();
    if( this.topIndex != topIndex && topIndex >= 0 && topIndex < itemCount ) {
      this.topIndex = topIndex;
      if( ( style & SWT.VIRTUAL ) != 0 ) {
        redraw();
      }
    }
  }

  /**
   * Returns the zero-relative index of the item which is currently
   * at the top of the receiver. This index can change when items are
   * scrolled or new items are added or removed.
   *
   * @return the index of the top item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getTopIndex() {
    checkWidget();
    return topIndex;
  }

  /**
   * Shows the item.  If the item is already showing in the receiver,
   * this method simply returns.  Otherwise, the items are scrolled until
   * the item is visible.
   *
   * @param item the item to be shown
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#showSelection()
   */
  public void showItem( final TableItem item ) {
    checkWidget();
    if( item == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    if( item.isDisposed() ) {
      error( SWT.ERROR_INVALID_ARGUMENT );
    }
    int itemIndex = indexOf( item );
    int itemCount = getVisibleItemCount( false );
    if( itemIndex < topIndex ) {
      // Show item as top item
      setTopIndex( itemIndex );
    } else if( itemCount > 0 && itemIndex >= topIndex + itemCount ) {
      // Show item as last item
      setTopIndex( itemIndex - itemCount + 1 );
    }
  }

  /**
   * Shows the column.  If the column is already showing in the receiver,
   * this method simply returns.  Otherwise, the columns are scrolled until
   * the column is visible.
   *
   * @param column the column to be shown
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
   *    <li>ERROR_INVALID_ARGUMENT - if the column has been disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 1.3
   */
  public void showColumn( final TableColumn column ) {
    checkWidget();
    if( column == null ) {
      error( SWT.ERROR_NULL_ARGUMENT );
    }
    if( column.isDisposed() ) {
      error( SWT.ERROR_INVALID_ARGUMENT );
    }
    if( column.getParent() == this ) {
      int index = indexOf( column );
      if( 0 <= index && index < getColumnCount() ) {
        int leftColumnsWidth = 0;
        int columnWidth = column.getWidth();
        int clientWidth = getClientArea().width;
        int[] columnOrder = getColumnOrder();
        boolean found = false;
        for( int i = 0; i < columnOrder.length && !found; i++ ) {
          found = index == columnOrder[ i ];
          if( !found ) {
            leftColumnsWidth += getColumn( columnOrder[ i ] ).getWidth();
          }
        }
        if( leftOffset > leftColumnsWidth ) {
          leftOffset = leftColumnsWidth;
        } else if( leftOffset < leftColumnsWidth + columnWidth - clientWidth ) {
          leftOffset = leftColumnsWidth + columnWidth - clientWidth;
        }
      }
    }
  }

  /**
   * Shows the selection.  If the selection is already showing in the receiver,
   * this method simply returns.  Otherwise, the items are scrolled until
   * the selection is visible.
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see Table#showItem(TableItem)
   */
  public void showSelection() {
    checkWidget();
    int index = getSelectionIndex();
    if( index != -1 ) {
      showItem( _getItem( index ) );
    }
  }

  ////////////////////
  // Visual appearance

  /**
   * Marks the receiver's header as visible if the argument is <code>true</code>,
   * and marks it invisible otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, marking
   * it visible may not actually cause it to be displayed.
   * </p>
   *
   * @param headerVisible the new visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setHeaderVisible( final boolean headerVisible ) {
    checkWidget();
    boolean changed = headerVisible != this.headerVisible;
    this.headerVisible = headerVisible;
    if( changed ) {
      updateScrollBars();
    }
  }

  /**
   * Returns <code>true</code> if the receiver's header is visible,
   * and <code>false</code> otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, this method
   * may still indicate that it is considered visible even though
   * it may not actually be showing.
   * </p>
   *
   * @return the receiver's header's visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getHeaderVisible() {
    checkWidget();
    return headerVisible;
  }

  /**
   * Returns <code>true</code> if the receiver's lines are visible,
   * and <code>false</code> otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, this method
   * may still indicate that it is considered visible even though
   * it may not actually be showing.
   * </p>
   *
   * @return the visibility state of the lines
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public boolean getLinesVisible() {
    checkWidget();
    return linesVisible;
  }

  /**
   * Marks the receiver's lines as visible if the argument is <code>true</code>,
   * and marks it invisible otherwise.
   * <p>
   * If one of the receiver's ancestors is not visible or some
   * other condition makes the receiver not visible, marking
   * it visible may not actually cause it to be displayed.
   * </p>
   *
   * @param linesVisible the new visibility state
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setLinesVisible( final boolean linesVisible ) {
    checkWidget();
    this.linesVisible = linesVisible;
  }

  /**
   * Sets the column used by the sort indicator for the receiver. A null
   * value will clear the sort indicator.  The current sort column is cleared
   * before the new column is set.
   *
   * @param column the column used by the sort indicator or <code>null</code>
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSortColumn( final TableColumn column ) {
    checkWidget();
    if( column != null && column.isDisposed() ) {
      error( SWT.ERROR_INVALID_ARGUMENT );
    }
    sortColumn = column;
  }

  /**
   * Returns the column which shows the sort indicator for
   * the receiver. The value may be null if no column shows
   * the sort indicator.
   *
   * @return the sort indicator
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #setSortColumn(TableColumn)
   */
  public TableColumn getSortColumn() {
    checkWidget();
    return sortColumn;
  }

  /**
   * Sets the direction of the sort indicator for the receiver. The value
   * can be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
   *
   * @param direction the direction of the sort indicator
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public void setSortDirection( final int direction ) {
    checkWidget();
    if( ( direction & ( SWT.UP | SWT.DOWN ) ) != 0 || direction == SWT.NONE ) {
      sortDirection = direction;
    }
  }

  /**
   * Returns the direction of the sort indicator for the receiver.
   * The value will be one of <code>UP</code>, <code>DOWN</code>
   * or <code>NONE</code>.
   *
   * @return the sort direction
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see #setSortDirection(int)
   */
  public int getSortDirection() {
    checkWidget();
    return sortDirection;
  }

  ///////////////////////////////////
  // Dimensions and size calculations

  /**
   * Returns the height of the area which would be used to
   * display <em>one</em> of the items in the receiver's.
   *
   * @return the height of one item
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getItemHeight() {
    checkWidget();
    int textHeight = Graphics.getCharHeight( getFont() );
    int paddingHeight = getCellPadding().height;
    textHeight += Math.max( paddingHeight, 4 );
    int itemImageHeight = getItemImageSize().y + paddingHeight;
    int result = Math.max( itemImageHeight, textHeight );
    if( ( style & SWT.CHECK ) != 0 ) {
      TableThemeAdapter adapter
        = ( TableThemeAdapter )getAdapter( IThemeAdapter.class );
      result = Math.max( adapter.getCheckBoxImageSize( this ).y, result );
    }
    return result;
  }

  /**
   * Returns the height of the receiver's header
   *
   * @return the height of the header or zero if the header is not visible
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getHeaderHeight() {
    checkWidget();
    int result = 0;
    if( headerVisible ) {
      TableThemeAdapter themeAdapter
        = ( TableThemeAdapter )getAdapter( IThemeAdapter.class );
      Font headerFont = themeAdapter.getHeaderFont( this );
      int textHeight = Graphics.getCharHeight( headerFont );
      int imageHeight = 0;
      for( int i = 0; i < getColumnCount(); i++ ) {
        Image image = getColumn( i ).getImage();
        int height = image == null ? 0 : image.getBounds().height;
        if( height > imageHeight ) {
          imageHeight = height;
        }
      }
      result = Math.max( textHeight, imageHeight );
      result += themeAdapter.getHeaderBorderBottomWidth( this );
      result += themeAdapter.getHeaderPadding( this ).height;
    }
    return result;
  }

  /**
   * Returns the width in pixels of a grid line.
   *
   * @return the width of a grid line in pixels
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   */
  public int getGridLineWidth () {
    checkWidget();
    return GRID_WIDTH;
  }

  //////////////////
  // Selection event

  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's selection changes, by sending
   * it one of the messages defined in the <code>SelectionListener</code>
   * interface.
   * <p>
   * When <code>widgetSelected</code> is called, the item field of the event object is valid.
   * If the receiver has <code>SWT.CHECK</code> style set and the check selection changes,
   * the event object detail field contains the value <code>SWT.CHECK</code>.
   * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
   * The item field of the event object is valid for default selection, but the detail field is not used.
   * </p>
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #removeSelectionListener
   * @see SelectionEvent
   */
  public void addSelectionListener( final SelectionListener listener ) {
    checkWidget();
    SelectionEvent.addListener( this, listener );
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver's selection changes.
   *
   * @param listener the listener which should no longer be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see SelectionListener
   * @see #addSelectionListener(SelectionListener)
   */
  public void removeSelectionListener( final SelectionListener listener ) {
    checkWidget();
    SelectionEvent.removeListener( this, listener );
  }

  public void setFont( final Font font ) {
    super.setFont( font );
    updateScrollBars();
  }

  ////////////////////
  // Widget dimensions

  public Point computeSize( final int wHint,
                            final int hHint,
                            final boolean changed )
  {
    checkWidget();
    int width = 0;
    int height = 0;

    if( getColumnCount() > 0 ) {
      for( int i = 0; i < getColumnCount(); i++ ) {
        width += getColumn( i ).getWidth();
      }
    } else {
      width = getItemsPreferredWidth( 0 );
    }
    height += getHeaderHeight();
    height += getItemCount() * getItemHeight();

    if( width == 0 ) {
      width = DEFAULT_WIDTH;
    }
    if( height == 0 ) {
      height = DEFAULT_HEIGHT;
    }
    if( wHint != SWT.DEFAULT ) {
      width = wHint;
    }
    if( hHint != SWT.DEFAULT ) {
      height = hHint;
    }
    int border = getBorderWidth();
    width += border * 2;
    height += border * 2;
    if( ( style & SWT.V_SCROLL ) != 0 ) {
      width += getScrollBarSize();
    }
    if( ( style & SWT.H_SCROLL ) != 0 ) {
      height += getScrollBarSize();
    }
    return new Point( width, height );
  }

  final int getItemsPreferredWidth( final int columnIndex ) {
    // Mimic Windows behaviour that has a minimal width
    int width = getCheckWidth( columnIndex ) + 12;
    // dont't access virtual items, they would get resolved unintentionally
    TableItem[] items = getCachedItems();
    for( int i = 0; i < items.length; i++ ) {
      int itemWidth
        = items[ i ].getCheckWidth( columnIndex )
        + items[ i ].getPackWidth( columnIndex );
      if( itemWidth > width ) {
        width = itemWidth;
      }
    }
    return width;
  }

  /////////////////////////////
  // Create and destroy columns

  final void createColumn( final TableColumn column, final int index ) {
    columnHolder.insert( column, index );
    if( columnOrder == null ) {
      columnOrder = new int[] { index };
    } else {
      int length = columnOrder.length;
      for( int i = index; i < length; i++ ) {
        columnOrder[ i ]++;
      }
      int[] newColumnOrder = new int[ length + 1 ];
      System.arraycopy( columnOrder, 0, newColumnOrder, 0, index );
      System.arraycopy( columnOrder,
                        index,
                        newColumnOrder,
                        index + 1,
                        length - index );
      columnOrder = newColumnOrder;
      columnOrder[ index ] = index;
    }
    if( columnImageCount == null ) {
      columnImageCount = new int[] { 0 };
    } else if( getColumnCount() > 1 ) {
      int length = columnImageCount.length;
      int[] newColumnImageCount = new int[ length + 1 ];
      System.arraycopy( columnImageCount, 0, newColumnImageCount, 0, index );
      System.arraycopy( columnImageCount,
                        index,
                        newColumnImageCount,
                        index + 1,
                        length - index );
      columnImageCount = newColumnImageCount;
    }
    updateScrollBars();
  }

  final void destroyColumn( final TableColumn column ) {
    int index = indexOf( column );
    // Remove data from TableItems
    for( int i = 0; i < itemCount; i++ ) {
      if( items[ i ] != null ) {
        items[ i ].removeData( index );
      }
    }
    // Reset sort column if necessary
    if( column == sortColumn ) {
      sortColumn = null;
    }
    // Remove from column holder
    columnHolder.remove( column );
    // Remove from column order
    int length = columnOrder.length;
    int[] newColumnOrder = new int[ length - 1 ];
    int count = 0;
    for( int i = 0; i < length; i++ ) {
      if( columnOrder[ i ] != index ) {
        int newOrder = columnOrder[ i ];
        if( index < newOrder ) {
          newOrder--;
        }
        newColumnOrder[ count ] = newOrder;
        count++;
      }
    }
    columnOrder = newColumnOrder;
    // Remove from columnImageCount
    if( columnImageCount.length == 1 ) {
      columnImageCount = null;
    } else {
      count = 0;
      int[] newColumnImageCount = new int[ columnImageCount.length - 1 ];
      for( int i = 0; i < columnImageCount.length; i++ ) {
        if( i != index ) {
          newColumnImageCount[ count ] = columnImageCount[ i ];
          count++;
        }
      }
      columnImageCount = newColumnImageCount;
    }
    updateScrollBars();
  }

  ////////////////////////////
  // Create and destroy items

  final void createItem( final TableItem item, final int index ) {
    int count = itemCount;
    if( !( 0 <= index && index <= count ) ) {
      error( SWT.ERROR_INVALID_RANGE );
    }
    if( count == items.length ) {
      /*
       * Grow the array faster when redraw is off or the table is not visible.
       * When the table is painted, the items array is resized to be smaller to
       * reduce memory usage.
       */
      boolean small = /* drawCount == 0 && */isVisible();
      int length
        = small ? items.length + 4 : Math.max( 4, items.length * 3 / 2 );
      TableItem[] newItems = new TableItem[ length ];
      System.arraycopy( items, 0, newItems, 0, items.length );
      items = newItems;
    }
    /* Insert the item */
    System.arraycopy( items, index, items, index + 1, count - index );
    items[ index ] = item;
    itemCount++;
    adjustItemIndices( index );
    // adjust the selection indices
    for( int i = 0; i < selection.length; i++ ) {
      if( selection[ i ] >= index ) {
        selection[ i ] = selection[ i ] + 1;
      }
    }
    // advance focusIndex when an item is inserted before the focused item
    if( index <= focusIndex ) {
      focusIndex++;
    }
    updateScrollBars();
  }

  final void destroyItem( final TableItem item, final int index ) {
    removeFromSelection( index );
    adjustSelectionIdices( index );
    if( item != null ) {
      int columnCount = Math.max( 1, columnHolder.size() );
      for( int i = 0; i < columnCount; i++ ) {
        updateColumnImageCount( i, item.getImageInternal( i ), null );
      }
    }
    itemCount--;
    if( item != null ) {
      item.index = -1;
    }
    if( itemCount == 0 ) {
      setTableEmpty();
    } else {
      System.arraycopy( items, index + 1, items, index, itemCount - index );
      items[ itemCount ] = null;
      adjustItemIndices( index );
    }
    adjustTopIndex();
    if( index == focusIndex || focusIndex > itemCount - 1 ) {
      adjustFocusIndex();
    }
    updateScrollBars();
    if( ( style & SWT.VIRTUAL ) != 0 ) {
      redraw();
    }
  }

  ////////////////
  // Destroy table

  void releaseChildren() {
    // TODO [rh] optimize: only execute the necessary mimimum from destroyItem
    //      when disposing of the table itself
    Item[] tableItems = new TableItem[ this.items.length ];
    System.arraycopy( this.items, 0, tableItems, 0, this.items.length );
    for( int i = 0; i < tableItems.length; i++ ) {
      if( tableItems[ i ] != null ) {
        tableItems[ i ].dispose();
      }
    }
    Item[] tableColumns = columnHolder.getItems();
    for( int i = 0; i < tableColumns.length; i++ ) {
      tableColumns[ i ].dispose();
    }
  }

  void releaseWidget() {
    removeControlListener( resizeListener );
    super.releaseWidget();
  }

  //////////////////////////////////
  // Helping methods - item retrival

  private TableItem _getItem( final int index ) {
    if( ( style & SWT.VIRTUAL ) != 0 && items[ index ] == null ) {
      items[ index ] = new TableItem( this, SWT.NONE, index, false );
    }
    return items[ index ];
  }

  final TableItem[] getCachedItems() {
    TableItem[] result;
    if( ( style & SWT.VIRTUAL ) != 0 ) {
      int count = 0;
      for( int i = 0; i < itemCount; i++ ) {
        if( items[ i ] != null && items[ i ].cached ) {
          count++;
        }
      }
      result = new TableItem[ count ];
      count = 0;
      for( int i = 0; i < itemCount; i++ ) {
        if( items[ i ] != null && items[ i ].cached ) {
          result[ count ] = items[ i ];
          count++;
        }
      }
    } else {
      result = new TableItem[ itemCount ];
      System.arraycopy( items, 0, result, 0, itemCount );
    }
    return result;
  }

  final TableItem[] getCreatedItems() {
    TableItem[] result;
    if( ( style & SWT.VIRTUAL ) != 0 ) {
      int count = 0;
      for( int i = 0; i < itemCount; i++ ) {
        if( items[ i ] != null ) {
          count++;
        }
      }
      result = new TableItem[ count ];
      count = 0;
      for( int i = 0; i < itemCount; i++ ) {
        if( items[ i ] != null ) {
          result[ count ] = items[ i ];
          count++;
        }
      }
    } else {
      result = new TableItem[ itemCount ];
      System.arraycopy( items, 0, result, 0, itemCount );
    }
    return result;
  }

  ///////////////////////////////////////////////
  // Helping methods - resolving of virtual items

  private void checkData() {
    boolean visible = true;
    int index = Math.max( 0, topIndex );
    while( visible && index < itemCount ) {
      visible = isItemVisible( index );
      if( visible ) {
        TableItem item = _getItem( index );
        checkData( item, index );
      }
      index++;
    }
  }

  final boolean checkData( final TableItem item, final int index ) {
    boolean result = true;
    boolean virtual = ( style & SWT.VIRTUAL ) != 0;
    if( virtual && !item.cached && index >= 0 && index < itemCount ) {
      item.cached = true;
      SetDataEvent event = new SetDataEvent( Table.this, item, index );
      event.processEvent();
      // widget could be disposed at this point
      if( isDisposed() || item.isDisposed() ) {
        result = false;
      }
    }
    return result;
  }

  ////////////////////////////////////
  // Helping methods - item image size

  final void updateColumnImageCount( final int columnIndex,
                                     final Image oldImage,
                                     final Image newImage )
  {
    int delta = 0;
    if( oldImage == null && newImage != null ) {
      delta = +1;
    } else if( oldImage != null && newImage == null ) {
      delta = -1;
    }
    if( delta != 0 ) {
      if( columnImageCount == null ) {
        int columnCount = Math.max( 1, columnHolder.size() );
        columnImageCount = new int[ columnCount ];
      }
      columnImageCount[ columnIndex ] += delta;
    }
  }

  final boolean hasColumnImages( final int columnIndex ) {
    return columnImageCount == null
      ? false
      : columnImageCount[ columnIndex ] > 0;
  }

  final void updateItemImageSize( final Image image ) {
    if( image != null && itemImageSize == null ) {
      Rectangle imageBounds = image.getBounds();
      itemImageSize = new Point( imageBounds.width, imageBounds.height );
    }
  }

  final Point getItemImageSize() {
    return itemImageSize == null ? new Point( 0, 0 ) : itemImageSize;
  }

  final void clearItemImageSize() {
    itemImageSize = null;
  }

  Rectangle getCellPadding() {
    if( bufferedCellPadding == null ) {
      TableThemeAdapter themeAdapter
        = ( TableThemeAdapter )getAdapter( IThemeAdapter.class );
      bufferedCellPadding = themeAdapter.getCellPadding( this );
    }
    return bufferedCellPadding;
  }

  int getCellSpacing() {
    if( bufferedCellSpacing < 0 ) {
      TableThemeAdapter themeAdapter
        = ( TableThemeAdapter )getAdapter( IThemeAdapter.class );
      bufferedCellSpacing = themeAdapter.getCellSpacing( parent );
    }
    return bufferedCellSpacing;
  }

  /////////////
  // ScrollBars

  // TODO [if] move to Scrollable as in SWT
  private void createScrollBars() {
    if( ( style & SWT.H_SCROLL ) != 0 ) {
      horizontalBar = new ScrollBar( this, SWT.H_SCROLL );
      horizontalBar.setVisible( false );
    }
    if( ( style & SWT.V_SCROLL ) != 0 ) {
      verticalBar = new ScrollBar( this, SWT.V_SCROLL );
      verticalBar.setVisible( false );
    }
  }

  /**
   * Returns the receiver's horizontal scroll bar if it has
   * one, and null if it does not.
   *
   * @return the horizontal scroll bar (or null)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 1.3
   */
  // TODO [if] move to Scrollable as in SWT
  public ScrollBar getHorizontalBar() {
    checkWidget();
    return horizontalBar;
  }

  /**
   * Returns the receiver's vertical scroll bar if it has
   * one, and null if it does not.
   *
   * @return the vertical scroll bar (or null)
   *
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @since 1.3
   */
  //  TODO [if] move to Scrollable as in SWT
  public ScrollBar getVerticalBar() {
    checkWidget();
    return verticalBar;
  }

  ///////////////////////////////////////
  // Helping methods - dynamic scrollbars

  boolean hasVScrollBar() {
    return hasVScrollBar;
  }

  boolean hasHScrollBar() {
    return hasHScrollBar;
  }

  int getVScrollBarWidth() {
    int result = 0;
    if( hasVScrollBar() ) {
      result = getScrollBarSize();
    }
    return result;
  }

  int getHScrollBarHeight() {
    int result = 0;
    if( hasHScrollBar() ) {
      result = getScrollBarSize();
    }
    return result;
  }

  void updateScrollBars() {
    if( ( style & SWT.NO_SCROLL ) == 0 ) {
      hasVScrollBar = false;
      hasHScrollBar = needsHScrollBar();
      if( needsVScrollBar() ) {
        hasVScrollBar = true;
        hasHScrollBar = needsHScrollBar();
      }
      horizontalBar.setVisible( hasHScrollBar );
      verticalBar.setVisible( hasVScrollBar );
    }
  }

  private int getScrollBarSize() {
    Object object = getDisplay().getAdapter( IDisplayAdapter.class );
    IDisplayAdapter adapter = ( IDisplayAdapter )object;
    return adapter.getScrollBarSize();
  }

  ////////////////////////////
  // Helping methods - various

  final int getCheckWidth() {
    int result = 0;
    if( ( style & SWT.CHECK ) != 0 ) {
      TableThemeAdapter themeAdapter
        = ( TableThemeAdapter )getAdapter( IThemeAdapter.class );
      result = themeAdapter.getCheckBoxWidth( this );
    }
    return result;
  }

  final int getCheckWidth( final int index ) {
    int result = 0;
    if( index == 0 && getColumnCount() == 0 ) {
      result = getCheckWidth();
    } else {
      int[] columnOrder = getColumnOrder();
      if( columnOrder[ 0 ] == index ) {
        result = getCheckWidth();
      }
    }
    return result;
  }

  final int getVisibleItemCount( final boolean includePartlyVisible ) {
    int clientHeight = getBounds().height
                     - getHeaderHeight()
                     - getHScrollBarHeight();
    int result = 0;
    if( clientHeight >= 0 ) {
      int itemHeight = getItemHeight();
      result = clientHeight / itemHeight;
      if( includePartlyVisible && clientHeight % itemHeight != 0 ) {
        result++;
      }
    }
    return result;
  }

  private void setFocusIndex( final int focusIndex ) {
    if( focusIndex >= 0 ) {
      this.focusIndex = focusIndex;
    }
  }

  private void removeItem( final int index ) {
    TableItem item = items[ index ];
    if( item != null && !item.isDisposed() ) {
      item.dispose();
    } else {
      destroyItem( null, index );
    }
  }

  private void removeFromSelection( final int index ) {
    if( index >= 0 && index < itemCount ) {
      boolean found = false;
      for( int i = 0; !found && i < selection.length; i++ ) {
        if( index == selection[ i ] ) {
          int length = selection.length;
          int[] newSel = new int[ length - 1 ];
          System.arraycopy( selection, 0, newSel, 0, i );
          if( i < length - 1 ) {
            System.arraycopy( selection, i + 1, newSel, i, length - i - 1 );
          }
          selection = newSel;
          found = true;
        }
      }
    }
  }

  private void adjustSelectionIdices( final int removedIndex ) {
    for( int i = 0; i < selection.length; i++ ) {
      if( selection[ i ] >= removedIndex ) {
        selection[ i ] = selection[ i ] - 1;
      }
    }
  }

  private void adjustTopIndex() {
    int visibleItemCount = getVisibleItemCount( false );
    if( topIndex > itemCount - visibleItemCount - 1 ) {
      topIndex = Math.max( 0, itemCount - visibleItemCount - 1 );
    }
  }

  private void adjustFocusIndex() {
    // Must reset focusIndex before calling getSelectionIndex
    focusIndex = -1;
    focusIndex = getSelectionIndex();
  }

  private void adjustItemIndices( final int start ) {
    for( int i = start; i < itemCount; i++ ) {
      if( items[ i ] != null ) {
        items[ i ].index = i;
      }
    }
  }

  private boolean isItemVisible( final int index ) {
    boolean result = false;
    int visibleItemCount = getVisibleItemCount( true );
    if( visibleItemCount > 0 ) {
      result = index >= topIndex && index < topIndex + visibleItemCount;
    }
    return result;
  }

  private static void sort( final int[] items ) {
    /* Shell Sort from K&R, pg 108 */
    int length = items.length;
    for( int gap = length / 2; gap > 0; gap /= 2 ) {
      for( int i = gap; i < length; i++ ) {
        for( int j = i - gap; j >= 0; j -= gap ) {
          if( items[ j ] <= items[ j + gap ] ) {
            int swap = items[ j ];
            items[ j ] = items[ j + gap ];
            items[ j + gap ] = swap;
          }
        }
      }
    }
  }

  private void setTableEmpty() {
    items = new TableItem[ 4 ];
    clearItemImageSize();
  }

  private static int checkStyle( final int style ) {
    int result = style;
    if( ( style & SWT.NO_SCROLL ) == 0 ) {
      result |= SWT.H_SCROLL | SWT.V_SCROLL;
    }
    return checkBits( result, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0 );
  }

  boolean needsVScrollBar() {
    int availableHeight = getClientArea().height;
    int height = getHeaderHeight();
    height += getItemCount() * getItemHeight();
    return height > availableHeight;
  }

  boolean needsHScrollBar() {
    boolean result = false;
    int availableWidth = getClientArea().width;
    int columnCount = getColumnCount();
    if( columnCount > 0 ) {
      int totalWidth = 0;
      for( int i = 0; i < columnCount; i++ ) {
        TableColumn column = getColumn( i );
        totalWidth += column.getWidth();
      }
      result = totalWidth > availableWidth;
    } else {
      TableItem measureItem = getMeasureItem();
      if( measureItem != null ) {
        int itemWidth = measureItem.getBounds().width;
        result = itemWidth > availableWidth;
      }
    }
    return result;
  }

  TableItem getMeasureItem() {
    TableItem[] items = tableAdapter.getCachedItems();
    TableItem result = null;
    if( getColumnCount() == 0 ) {
      // Find item with longest text because the imaginary only column stretches
      // as wide as the longest item (images cannot differ in width)
      for( int i = 0; i < items.length; i++ ) {
        if( result == null ) {
          result = items[ i ];
        } else {
          result = max( result, items[ i ] );
        }
      }
    } else {
      // Take the first item if any
      if( items.length > 0 ) {
        result = items[ 0 ];
      }
    }
    return result;
  }

  private static TableItem max( final TableItem item1, final TableItem item2 ) {
    TableItem result;
    if( item1.getText( 0 ).length() > item2.getText( 0 ).length() ) {
      result = item1;
    } else {
      result = item2;
    }
    return result;
  }

  ///////////////////
  // Skinning support

  void reskinChildren( final int flags ) {
    if( items != null ) {
      for( int i = 0; i < items.length; i++ ) {
        TableItem item = items[ i ];
        if( item != null ) {
          item.reskin( flags );
        }
      }
    }
    TableColumn[] columns = getColumns();
    if (columns != null) {
      for( int i = 0; i < columns.length; i++ ) {
        TableColumn column = columns[ i ];
        if( !column.isDisposed() ) {
          column.reskin( flags );
        }
      }
    }
    super.reskinChildren( flags );
  }

}