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
package com.w4t.webgridlayoutkit;

import java.io.IOException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.developer.AreaSelector;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.renderinfo.GridLayoutRenderInfo;
import com.w4t.weblayoutkit.LayoutRenderer;


/** <p>the superclass for all renderers that render org.eclipse.rap.WebGridLayout.</p>
  */
public abstract class WebGridLayoutRenderer extends LayoutRenderer {
  
  private static final String BORDER_SIZE = "borderSize";
  private static final String DESIGN_TIME = "designTime";
  private static final String DESIGN_MATRIX = "designMatrix";
    
  void setDesignMatrix( final WebContainer parent,
                        final AreaSelector[][] designMatrix )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( DESIGN_MATRIX, designMatrix );
  }
  
  AreaSelector[][] getDesignMatrix( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector[][] )adapter.getRenderState( DESIGN_MATRIX );
  }
  
  void setDesignTime( final WebContainer parent, final boolean designTime ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( DESIGN_TIME, Boolean.valueOf( designTime ) );  
  }
  
  boolean isDesignTime( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( DESIGN_TIME );
    boolean result = false;
    if( renderState != null ) {
      result = ( ( Boolean )renderState ).booleanValue();      
    }
    return result;
  }
  
  void setBorderSize( final WebContainer parent, final String borderSize ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( BORDER_SIZE, borderSize );      
  }
  
  String getBorderSize( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( String )adapter.getRenderState( BORDER_SIZE );    
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    getRenderInfoAdapter( parent ).clearRenderState();
    boolean[][] usedMatrix = null;

    try {
      setDesignTime( parent, parent.isDesignTime() );
      checkBorderSizeInDesignTime( parent );

      // put the WebComponents into the Table Cells
      Position pos = null;
      for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
        pos = ( Position )parent.getConstraint( i );
        try {
          WebTableCell tableCell
            = ( WebTableCell )getLayout( parent ).getArea( pos );
          tableCell.addContentElement( parent.get( i ) );
        } catch( Exception e ) {
          WebComponent wc = parent.get( i ) ;
          parent.remove( wc );
        }
      }

      /* initialize the TableCells and tests if the tablecells are used 
       * for row- or/and colspan. this test is realized with a matrix of 
       * boolean values */
      usedMatrix = createUsedMatrix( parent );
      setDesignMatrix( parent, null );
      if( isDesignTime( parent ) ) {
        setDesignMatrix( parent, createDesignMatrix( parent ) );
      }
      for( int i = 0; i < getLayout( parent ).getRowCount(); i++ ) {
        WebTableCell[] row = getInfo( parent ).getTableCells( i );
        for( int j = 0; j < getLayout( parent ).getColCount(); j++ ) {
          WebTableCell tableCell = row[ j ];
          initUsedMatrix( parent, tableCell, i, j, usedMatrix );
        }
      }

      // build the html Table
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.TABLE, null );
      out.writeAttribute( HTML.ID, parent.getUniqueID(), null );
      RenderUtil.writeUniversalAttributes( parent );
      RenderUtil.writeTableAttributes( getLayout( parent ) );
      out.closeElementIfStarted();

      // loops over the matrix
      for( int i = 0; i < getLayout( parent ).getRowCount(); i++ ) {
        WebTableRow rowToRender = new WebTableRow();
        WebTableCell[] row = getInfo( parent ).getTableCells( i );
        for( int j = 0; j < getLayout( parent ).getColCount(); j++ ) {
          if( usedMatrix[ i ][ j ] ) {
            rowToRender.addCell( row[ j ] );
            if( isDesignTime( parent ) ) {
              WebTableCell cell = row[ j ];
              getDesignMatrix( parent )[ i ][ j ].setRegion( cell );
              cell.addContentElement( getDesignMatrix( parent )[ i ][ j ] );
              parent.add( getDesignMatrix( parent )[ i ][ j ], 
                          new Position( i, j ) );
            }
          }
        }
        rowToRender.render();
      }
      out.endElement( HTML.TABLE );
    } finally {
      removeComponents( parent, usedMatrix );
    }
  }

  GridLayoutRenderInfo getInfo( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( GridLayoutRenderInfo )adapter.getInfo();
  }

  IRenderInfoAdapter getRenderInfoAdapter( final WebContainer parent ) {
    Adaptable adaptable = getLayout( parent );
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )adaptable.getAdapter( clazz );
  }

  WebGridLayout getLayout( final WebContainer parent ) {
    return ( WebGridLayout )parent.getWebLayout();
  }
  
  private void removeComponents( final WebContainer parent,
                                 final boolean[][] used )
  {
    WebGridLayout layout = getLayout( parent );
    for( int i = 0; i < layout.getRowCount(); i++ ) {
      WebTableCell[] row = getInfo( parent ).getTableCells( i );
      for( int j = 0; j < layout.getColCount(); j++ ) {
        if( used[ i ][ j ] ) {
          if( isDesignTime( parent ) ) {
            AreaSelector areaSelector = getDesignMatrix( parent )[ i ][ j ];
            row[ j ].removeContentElement( areaSelector );
            parent.remove( areaSelector );
          }
        }
      }
    }
    for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
      WebTableCell tableCell 
        = ( WebTableCell )layout.getArea( parent.getConstraint( i ) );
      tableCell.removeContentElement( parent.get( i ) );
    }
    restoreBorderBuffer( parent );
  }
  
  private void checkBorderSizeInDesignTime( final WebContainer parent ) {
    // buffer border size
    setBorderSize( parent, getLayout( parent ).getBorder() );
    boolean useBorderSize_1 = false;
    if( isDesignTime( parent ) ) {
      useBorderSize_1 = true;
      try {
        if( Integer.parseInt( getLayout( parent ).getBorder() ) > 1 ) {
          useBorderSize_1 = false;
        }
      } catch( Exception e ) {
      }
    }
    if( useBorderSize_1 ) {
      getLayout( parent ).setBorder( "1" );
    }
  }

  private void restoreBorderBuffer( final WebContainer parent ) {
    getLayout( parent ).setBorder( getBorderSize( parent ) );
  }

  /** creates a matrix with Boolean objects, which contains the value true */
  private boolean[][] createUsedMatrix( final WebContainer parent ) {
    WebGridLayout layout = getLayout( parent );
    int rowCount = layout.getRowCount();
    int colCount = layout.getColCount();
    boolean[][] result = new boolean[ rowCount ][ colCount ];
    for( int i = 0; i < rowCount; i++ ) {
      for( int j = 0; j < colCount; j++ ) {
        result[ i ][ j ] = true;
      }
    }
    return result;
  }

  /** creates a matrix with WebDesignButtons for design time */
  private AreaSelector[][] createDesignMatrix( final WebContainer parent ) {
    WebGridLayout layout = getLayout( parent );
    AreaSelector[][] designMatrix =
      new AreaSelector[ layout.getRowCount() ][ layout.getColCount() ];
    for( int i = 0; i < layout.getRowCount(); i++ ) {
      for( int j = 0; j < layout.getColCount(); j++ ) {
        designMatrix[ i ][ j ] = new AreaSelector();
        designMatrix[ i ][ j ].setConstraint( new Position( i + 1, j + 1 ) );
        designMatrix[ i ][ j ].setContainer( parent );
      }
    }
    return designMatrix;
  }  

  /** initialises the usedMatrix which sets the cells of the table
    * to unused, which belongs to a row- or a colspan
    * of another cell */
  private void initUsedMatrix( final WebContainer parent,
                               final WebTableCell tableCell,
                               final int row,
                               final int col,
                               final boolean[][] usedMatrix ) {
    // read the row- and colspan out of the tableCell
    if( usedMatrix[ row ][ col ] ) {
      int rowspan = 1;
      int colspan = 1;
      try {
        rowspan = Integer.parseInt( tableCell.getRowspan() );
        if( row + rowspan > getLayout( parent ).getRowCount() ) {
          rowspan = getLayout( parent ).getRowCount() - row;
        }
      } catch( Exception e ) {}
      try {
        colspan = Integer.parseInt( tableCell.getColspan() );
        if( col + colspan > getLayout( parent ).getColCount() ) {
          colspan = getLayout( parent ).getColCount() - col;
        }
      } catch( Exception e ) {}
      rowspan = ( rowspan < 1 ) ? 1 : rowspan;
      colspan = ( colspan < 1 ) ? 1 : colspan;

      if( colspan > 1 ) {
        tableCell.setColspan( String.valueOf( colspan ) );
      }
      if( rowspan > 1 ) {
        tableCell.setRowspan( String.valueOf( rowspan ) );
      }

      // disable the usage of all cells which belong to the row- or colspan
      for( int i = row; i < row + rowspan; i++ ) {
        for( int j = col; j < col + colspan; j++ ) {
          usedMatrix[ i ][ j ] = false;
        }
      }
      // enable the usage of the current table cell
      usedMatrix[ row ][ col ] = true;
    }
  }  
}