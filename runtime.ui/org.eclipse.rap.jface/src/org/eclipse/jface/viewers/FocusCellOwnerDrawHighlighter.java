/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	   IBM Corporation - initial API and implementation
 * 	   Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 * 												 - fix for bug 183850, 182652, 182800, 215069
 *     Innoopract - RAP adaption
 *******************************************************************************/
package org.eclipse.jface.viewers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;


/**
 * A concrete implementation of {@link FocusCellHighlighter} using by setting
 * the control into owner draw mode and highlighting the currently selected
 * cell. To make the use this class you should create the control with the
 * {@link SWT#FULL_SELECTION} bit set
 *
 * <p>
 * <strong>RAP specific:</strong> despite its name that was taken as-is from
 * JFace/RCP to ease single-sourcing, this class does not use custom drawing
 * to highlight the focused cell.
 * <br />
 * The focused cell is displayed using the selection colors of the system.
 * </p>
 *
 * This class can be subclassed to configure how the coloring of the selected
 * cell.
 *
 * @since 1.2
 *
 */
public class FocusCellOwnerDrawHighlighter extends FocusCellHighlighter {

  private final ColumnViewer viewer;

	/**
	 * Create a new instance which can be passed to a
	 *
	 * @param viewer
	 *            the viewer
	 */
	public FocusCellOwnerDrawHighlighter(ColumnViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		hookListener(viewer);
		// RAP [if]
        viewer.getControl().setData( Table.ALWAYS_HIDE_SELECTION,
                                     Boolean.TRUE );
	}

	private void markFocusedCell(Event event, ViewerCell cell) {
		Color background = (cell.getControl().isFocusControl()) ? getSelectedCellBackgroundColor(cell)
				: getSelectedCellBackgroundColorNoFocus(cell);
		Color foreground = (cell.getControl().isFocusControl()) ? getSelectedCellForegroundColor(cell)
				: getSelectedCellForegroundColorNoFocus(cell);

		if (foreground != null || background != null || onlyTextHighlighting(cell)) {
//			GC gc = event.gc;

			if (background == null) {
				background = cell.getItem().getDisplay().getSystemColor(
						SWT.COLOR_LIST_SELECTION);
			}

			if (foreground == null) {
				foreground = cell.getItem().getDisplay().getSystemColor(
						SWT.COLOR_LIST_SELECTION_TEXT);
			}

//			gc.setBackground(background);
//			gc.setForeground(foreground);
//
//			if (onlyTextHighlighting(cell)) {
//				Rectangle area = event.getBounds();
//				Rectangle rect = cell.getTextBounds();
//				if( rect != null ) {
//					area.x = rect.x;
//				}
//				gc.fillRectangle(area);
//			} else {
//				gc.fillRectangle(event.getBounds());
//			}
			cell.setBackground( background );
			cell.setForeground( foreground );

//			event.detail &= ~SWT.SELECTED;
		}
	}

	private void removeSelectionInformation(Event event, ViewerCell cell) {
//		GC gc = event.gc;
//		gc.setBackground(cell.getViewerRow().getBackground(
//				cell.getColumnIndex()));
	  Color tableBackground = cell.getControl().getBackground();
	  Color cellBackground
	    = cell.getViewerRow().getBackground( cell.getColumnIndex() );
	  if( !cellBackground.equals( tableBackground ) ) {
	    cell.setBackground( cellBackground );
	  } else {
	    cell.setBackground( null );
	  }
//		gc.setForeground(cell.getViewerRow().getForeground(
//				cell.getColumnIndex()));
	  Color tableForeground = cell.getControl().getForeground();
	  Color cellForeground
	    = cell.getViewerRow().getForeground( cell.getColumnIndex() );
	  if( !cellForeground.equals( tableForeground ) ) {
	    cell.setForeground( cellForeground );
	  } else {
	    cell.setForeground( null );
	  }
//		gc.fillRectangle(cell.getBounds());
//		event.detail &= ~SWT.SELECTED;
	}

    private void hookListener(final ColumnViewer viewer) {
//
//		Listener listener = new Listener() {
//
//			public void handleEvent(Event event) {
////				if ((event.detail & SWT.SELECTED) > 0) {
//					ViewerCell focusCell = getFocusCell();
//					ViewerRow row = viewer.getViewerRowFromItem(event.item);
//
//					Assert
//							.isNotNull(row,
//									"Internal structure invalid. Item without associated row is not possible."); //$NON-NLS-1$
//
////					ViewerCell cell = row.getCell(event.index);
////
////					if (focusCell == null || !cell.equals(focusCell)) {
////						removeSelectionInformation(event, cell);
////					} else {
////						markFocusedCell(event, cell);
////					}
//
//					focusCellChanged( focusCell );
//			}
//
//      };
////		viewer.getControl().addListener(SWT.EraseItem, listener);
//		viewer.getControl().addListener(SWT.KeyDown, listener);
//		viewer.getControl().addListener(SWT.MouseDown, listener);

      // [rst] We use a JFace SelectionChanged listener to keep track of
      //       programmatic selection changes and handle MULTI correctly.
      //       See https://bugs.eclipse.org/bugs/show_bug.cgi?id=261647
      viewer.addSelectionChangedListener( new ISelectionChangedListener() {

        private Widget[] oldSelection = getSelectedItems();
        private ViewerCell oldFocusCell = getFocusCell();

        public void selectionChanged( SelectionChangedEvent event ) {
          // erase old selection
          for( int i = 0; i < oldSelection.length; i++ ) {
            Widget item = oldSelection[ i ];
            if( !item.isDisposed() ) {
              ViewerRow row = viewer.getViewerRowFromItem( item );
              int count = row.getColumnCount();
              for( int j = 0; j < count; j++ ) {
                ViewerCell cell = row.getCell( j );
                removeSelectionInformation( null, cell );
              }
            }
          }
          if( oldFocusCell != null ) {
            Widget item = oldFocusCell.getItem();
            if( !item.isDisposed() ) {
              removeSelectionInformation( null, oldFocusCell );
            }
          }
          // colorize new selection
          Widget[] selection = getSelectedItems();
          ViewerCell focusCell = getFocusCell();
          int index = 0;
          if( focusCell != null ) {
            index = focusCell.getColumnIndex();
          } else if( oldFocusCell != null ) {
            index = oldFocusCell.getColumnIndex();
          }
          for( int i = 0; i < selection.length; i++ ) {
            Widget item = selection[ i ];
            ViewerRow row = viewer.getViewerRowFromItem( item );
            ViewerCell cell = row.getCell( index );
            markFocusedCell( null, cell );
          }
          oldSelection = selection;
          oldFocusCell = focusCell;
        }
      });
    }

	/**
	 * The color to use when rendering the background of the selected cell when
	 * the control has the input focus
	 *
	 * @param cell
	 *            the cell which is colored
	 * @return the color or <code>null</code> to use the default
	 */
	protected Color getSelectedCellBackgroundColor(ViewerCell cell) {
		return null;
	}

	/**
	 * The color to use when rendering the foreground (=text) of the selected
	 * cell when the control has the input focus
	 *
	 * @param cell
	 *            the cell which is colored
	 * @return the color or <code>null</code> to use the default
	 */
	protected Color getSelectedCellForegroundColor(ViewerCell cell) {
		return null;
	}

	/**
	 * The color to use when rendering the foreground (=text) of the selected
	 * cell when the control has <b>no</b> input focus
	 *
	 * @param cell
	 *            the cell which is colored
	 * @return the color or <code>null</code> to use the same used when
	 *         control has focus
	 */
	protected Color getSelectedCellForegroundColorNoFocus(ViewerCell cell) {
		return null;
	}

	/**
	 * The color to use when rendering the background of the selected cell when
	 * the control has <b>no</b> input focus
	 *
	 * @param cell
	 *            the cell which is colored
	 * @return the color or <code>null</code> to use the same used when
	 *         control has focus
	 */
	protected Color getSelectedCellBackgroundColorNoFocus(ViewerCell cell) {
		return null;
	}

	/**
	 * Controls whether the whole cell or only the text-area is highlighted
	 *
	 * @param cell
	 *            the cell which is highlighted
	 * @return <code>true</code> if only the text area should be highlighted
	 */
	protected boolean onlyTextHighlighting(ViewerCell cell) {
//		return false;
		return true;
	}

	 protected void init() {
    ColumnViewerEditorActivationListener listener
      = new ColumnViewerEditorActivationListener() {
      public void afterEditorActivated( ColumnViewerEditorActivationEvent e )
      {
      }
      public void afterEditorDeactivated( ColumnViewerEditorDeactivationEvent e )
      {
        focusCellChanged( getFocusCell(), null );
      }
      public void beforeEditorActivated( ColumnViewerEditorActivationEvent e )
      {
      }
      public void beforeEditorDeactivated( ColumnViewerEditorDeactivationEvent e )
      {
      }
    };
    ColumnViewerEditor editor = viewer.getColumnViewerEditor();
    editor.addEditorActivationListener( listener );
  }

	protected void focusCellChanged(ViewerCell newCell, ViewerCell oldCell) {
//		super.focusCellChanged(newCell, oldCell);
//
//		// Redraw new area
//		if (newCell != null) {
//			Rectangle rect = newCell.getBounds();
//			int x = newCell.getColumnIndex() == 0 ? 0 : rect.x;
//			int width = newCell.getColumnIndex() == 0 ? rect.x + rect.width
//					: rect.width;
//			// 1 is a fix for Linux-GTK
//			newCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1,
//					true);
//		}
//
//		if (oldCell != null) {
//			Rectangle rect = oldCell.getBounds();
//			int x = oldCell.getColumnIndex() == 0 ? 0 : rect.x;
//			int width = oldCell.getColumnIndex() == 0 ? rect.x + rect.width
//					: rect.width;
//			// 1 is a fix for Linux-GTK
//			oldCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1,
//					true);
//		}
        if( oldCell != null ) {
          oldCell.setBackground( null );
          oldCell.setForeground( null );
// [if] Fix for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=264226
// When selected item is deleted, the oldCell item and element are out of sync
//          viewer.updateItem( oldCell.getItem(), oldCell.getElement() );
        }
        if( newCell != null && isItemSelected( newCell.getItem() ) ) {
          markFocusedCell( null, newCell );
        }
	}

// RAP [if] Helping methods
    private Widget[] getSelectedItems() {
      Control control = viewer.getControl();
      Widget[] result = null;
      if( control instanceof Table ) {
        result = ( ( Table )control ).getSelection();
      } else if( control instanceof Tree ) {
        result = ( ( Tree )control ).getSelection();
      }
      return result;
    }

	private boolean isItemSelected( final Widget item ) {
	  boolean result = false;
      Widget[] selection = getSelectedItems();
      for( int i = 0; i < selection.length && !result; i++ ) {
        result = item == selection[ i ];
      }
	  return result;
	}
// RAPEND
}
