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

import java.io.IOException;
import java.util.Vector;
import com.w4t.ajax.*;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;
import com.w4t.renderinfo.GridLayoutRenderInfo;


/** <p>Defines the WebGridLayout.</p>
  * <p>The WebGridLayout differs in its semantic from the GridLayoutManager
  * of the AWT. The WebGridLayout divides its surrounding WebContainer
  * in rectangle areas, which can be filled with WebComponents. These
  * rectangle areas are basically table cells, which can be identified
  * by the numbers of rows and columns. To join two or more cells
  * the spanCol or spanRow are used.</p>
  */
public class WebGridLayout extends WebTable implements WebLayout {

  /** contains the WebTableCells stored in kind of dynamic a matrix. */
  private Vector tableCells = null;
  /** the count of columns in the grid */
  private int colCount = 2;
  /** the count of rows in the grid */
  private int rowCount = 2;
  /** the WebContainer which is layouted by this WebBorderLayout. This is 
    * only temporarily set during rendering. */
  private WebContainer parent = null;
  private Object renderInfoAdapter;
  
    
  /** Constructor´*/
  public WebGridLayout() {
    tableCells = new Vector();
    initCells( rowCount, colCount );
  }

  /** Constructor which initialise the rows and cols */
  public WebGridLayout( final int rowCount, final int colCount ) {
    tableCells = new Vector();
    initCells( rowCount, colCount );
  }
  
  // override hashcode due to ajax-hashcode detection of WebGridLayout
  public int hashCode() {
    int result = 17;
    result = 73 * result + ( parent == null ? 0 : parent.hashCode() );
    HashCodeBuilderSupport support = AjaxStatusUtil.newHashCodeBuilderSupport();
    HashCodeBuilder builder 
      = HashCodeBuilderFactory.getBuilder( WebTableCell.class );            
    for( int i = 0; i < rowCount; i++ ) {
      Vector row = ( Vector )tableCells.get( i );
      for( int j = 0; j < colCount; j++ ) {
        result = 73 * result + builder.compute( support, row.get( j ) );
      }
    }
    return result;
  }
  /**
    * inits all cells in this WebGridLayout.<br>
    * This is needed not only in the constructor, but also while cloning.
    */
  protected void initCells( final int rowCount, final int colCount ) {
    for( int i = 0; i < rowCount; i++ ) {
      Vector row = new Vector();
      for( int j = 0; j < colCount; j++ ) {
        row.add( new WebTableCell() );
      }
      tableCells.add( row );
    }
    this.colCount = colCount;
    this.rowCount = rowCount;
  }
  
  /**
   * checks if the constraint parameter in the add method of a WebContainer
   * has the correct type
   */
  public boolean checkConstraint( final Object constraint ) {
    return constraint instanceof Position;
  }

  /**
    * returns a clone of this WebGridLayout.<br>
    * Cloning a WebLayout involves a copy of all settings and inits, but no
    * cloning or copying added components ( see @link WebContainer.clone() ).
    */
  public Object clone() throws CloneNotSupportedException {
    WebGridLayout clone = ( WebGridLayout )super.clone();

    // we must perform some inits, since no constructors are called in clone()
    clone.tableCells = new Vector();

    int rowCount = this.getRowCount();
    int colCount = this.getColCount();
    // clone the regions
    for( int i = 0; i < rowCount; i++ ) {
      Vector row = ( Vector )tableCells.get( i );
      Vector cloneRow = new Vector();
      for( int j = 0; j < colCount; j++ ) {
        cloneRow.add( ( ( WebTableCell )row.get( j ) ).clone() );
      }
      clone.tableCells.add( cloneRow );
    }
    return clone;
  }

  /** adds a row at the row-end of the grids Matrix */
  public void addRow() {
    addRow( rowCount + 1 );
  }

  /** sets the count of columns in the grid */
  public void setColCount( final int colCount ) {
    if( colCount > 0 ) {
      if( colCount < this.colCount ) {
        // remove cols
        int colsToRemove = this.colCount - colCount;
        for( int i = 0; i < colsToRemove; i++ ) {
          this.removeCol();
        }
      } else {
        // add cols
        int colsToAdd = colCount - this.colCount;
        for( int i = 0; i < colsToAdd; i++ ) {
          this.addCol();
        }
      }
    }
  }

  /** gets the count of columns in the grid */
  public int getColCount() {
    return colCount;
  }

  /** sets the count of rows in the grid */
  public void setRowCount( final int rowCount ) {
    if( rowCount > 0 ) {
      if( rowCount < this.rowCount ) {
        // remove cols
        int rowsToRemove = this.rowCount - rowCount;
        for( int i = 0; i < rowsToRemove; i++ ) {
          this.removeRow();
        }
      } else {
        // add cols
        int rowsToAdd = rowCount - this.rowCount;
        for( int i = 0; i < rowsToAdd; i++ ) {
          this.addRow();
        }
      }
    }
  }

  /** gets the count of rows in the grid */
  public int getRowCount() {
    return rowCount;
  }

  /** adds a row at the row-position specified with the index */
  public void addRow( final int index ) {
    Vector row = new Vector();
    for( int j = 0; j < colCount; j++ ) {
      row.add( new WebTableCell() );
    }
    tableCells.add( index - 1, row );
    rowCount++;
  }

  /** removes a row at the row-position specified with the index */
  public void removeRow( final int index ) {
    tableCells.remove( index - 1 );
    if( rowCount > 0 ) {
      rowCount--;
    }
  }

  /** adds a column at the column-end of the grids Matrix */
  public void addCol() {
    addCol( colCount + 1 );
  }

  /** adds a column at the column-position specified with the index */
  public void addCol( final int index ) {
    for( int i = 0; i < rowCount; i++ ) {
      Vector row = ( Vector )tableCells.get( i );
      row.add( index - 1, new WebTableCell() );
    }
    colCount++;
  }

  /** removes a column at the column-position specified with the index */
  public void removeCol( final int index ) {
    for( int i = 0; i < rowCount; i++ ) {
      Vector row = ( Vector )tableCells.get( i );
      row.remove( index - 1 );
    }
    if( colCount > 0 ) {
      colCount--;
    }
  }

  public void removeCol() {
    removeCol( colCount  );
  }

  public void removeRow() {
    removeRow( rowCount );
  }

  /** <p>returns the Area specified in the constraint object. Used e.g. for
    * setting the format of the WebTableCell that is the Area.</p>
    * @param constraint specifies the Area
    */
  public Area getArea( final Object constraint ) {
    Position cellPos = ( Position ) constraint;
    Vector tableRow = ( Vector )tableCells.get( cellPos.getRowPosition() - 1 );
    return ( WebTableCell )tableRow.get( cellPos.getColPosition() - 1 );
  }

  /** <p>sets the Area specified in the constraint object on this
    * WebGridLayout.</p>
    * <p>Note: usually you will not need this method, it is intended for some
    * special tool-based purposes.</p>
    * @param constraints specifies the region
    * @param area the WebTableCell used as Area in the WebGridLayout
    */
  public void setArea( final Object constraints, final WebTableCell area ) {
    Position cellPos = ( Position ) constraints;
    Vector tableRow = ( Vector )tableCells.get( cellPos.getRowPosition() - 1 );
    tableRow.remove( cellPos.getColPosition() - 1 );
    tableRow.add(  cellPos.getColPosition() - 1 , area );
  }

  /** <p>gets the region specified in the constraints object. used e.g.
    * for setting the format of the region.</p>
    * @param constraints specifies the region
    * @deprecated replaced by {@link #getArea(Object)}
    */
  public WebTableCell getRegion( final Object constraints ) {
    Position cellPos = ( Position ) constraints;
    Vector tableRow = ( Vector )tableCells.get( cellPos.getRowPosition() - 1 );
    return ( WebTableCell )tableRow.get( cellPos.getColPosition() - 1 );
  }

  /** <p>sets the region specified in the constraints object.</p>
    * @param constraints specifies the region
    * @param region the WebTableCell used as region in the WebGridLayout
    * @deprecated replaced by {@link #setArea(Object,WebTableCell)}
    */
  public void setRegion( final Object constraints, final WebTableCell region ) {
    Position cellPos = ( Position ) constraints;
    Vector tableRow = ( Vector )tableCells.get( cellPos.getRowPosition() - 1 );
    tableRow.remove( cellPos.getColPosition() - 1 );
    tableRow.add(  cellPos.getColPosition() - 1 , region );
  }
  
  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IRenderInfoAdapter.class ) {
      result = getRenderInfoAdapter();
    } else {
      result = super.getAdapter( adapter );      
    }
    return result;
  }

  private Object getRenderInfoAdapter() {
    if( renderInfoAdapter == null ) {
      renderInfoAdapter = new RenderInfoAdapter() {
        private GridLayoutRenderInfo renderInfo;
        public void createInfo() {
          renderInfo = new GridLayoutRenderInfo( parent, tableCells );
        }
        public Object getInfo() {
          return renderInfo;
        }
      };
    }
    return renderInfoAdapter;
  }
  
  /** <p>creates the layout for the WebContainer.</p>
    *
    * @param parent the WebContainer which uses this layout manager */
  public void layoutWebContainer( final WebContainer parent ) throws IOException {
    LifeCycleHelper.render( this, parent );
  }
  
  /** <p>inserts a row at the top of the table grid.</p>
   *  <p>Note: this has no effect for the constraints used for positioning
   *  elements on the grid, but all cells of the grid are moved one 
   *  row down.</p> */
  public void insertHeaderRow() {
    Vector header = new Vector();
    for( int j = 0; j < colCount; j++ ) {
      header.add( new WebTableCell() );
    }
    tableCells.add( 0, header );
    rowCount++;
  }
  
  /** <p>removes a row at the top of the table grid.</p>
   *  <p>Note: this has no effect for the constraints used for positioning
   *  elements on the grid, but all cells of the grid are moved one 
   *  row up.</p> */
  public void removeHeaderRow() {
    if( tableCells.size() > 0 ) {
      tableCells.remove( 0 );
      rowCount--;      
    }
  }
}