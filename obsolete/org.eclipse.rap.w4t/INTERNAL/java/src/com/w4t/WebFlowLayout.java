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
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.internal.adaptable.RenderInfoAdapter;
import com.w4t.renderinfo.FlowLayoutRenderInfo;


/** <p>Defines the WebFlowLayout.</p>
 * 
  * <p>The WebFlowLayout adds the WebComponents of the WebContainer
  * in one row, till there's no more place for the next WebComponent.
  * The next WebComponent is placed into the next line.</p>
  * 
  * <p>With default settings the WebLayoutManager simply renders the markup of
  * the WebComponents sequentially into the WebForms markup.<p>
  * 
  * <p>If the default settings of this WebLayoutManager and/or its
  * WebContainer are changed a blind table with only one row and one table 
  * cell, which contains the containers elements, is added to the
  * markup to visualise these settings.</p>
  * 
  * <p>Code example:
  * <pre>
  * package test;
  *
  * import org.eclipse.rap.*;
  * import org.eclipse.rap.event.*;
  *
  * public class Test extends WebForm {
  *
  *   // declarations
  *   WebFlowLayout wbfl;
  *   WebLabel wblText;
  *
  *   public void setWebComponents() throws Exception {
  *
  *     // define the WebFlowLayout
  *     wbfl = new WebFlowLayout();
  *     this.setWebLayout( wbfl );
  *
  *     // set some attributes
  *     wbfl.setBgColor( new WebColor( "yellow" ) );
  *     wbfl.setWidth( "500" );
  *     wbfl.setBorder( "10" );
  *     wbfl.setAlign( "right" );
  *     wbfl.setCellpadding( "50" );
  *     wbfl.setCellspacing( "10" );
  *
  *     // define the text to be shown
  *     String text 
  *       =   " This is a little example of a simple Flow Layout" 
  *         + " containing some text. You see a " 
  *         + " black text on yellow background with a border around it." 
  *         + " The WebFlowLayout adds the WebComponents of the" 
  *         + " WebContainer in one row, till there's no more place for" 
  *         + " the next WebComponent. The next WebComponent is placed" 
  *         + " into the next line.";
  *
  *     wblText = new WebLabel( text );
  *     this.add( wblText );
  *   }
  * }
  * </pre>
  */
public class WebFlowLayout extends WebTable implements WebLayout {

  /** the one and only row of the WebFlowLayout */
  private WebTableRow tableRow = null;
  /** the one and only cell of the WebFlowLayout */
  private WebTableCell tableCell = null;
  /** the WebContainer which is layouted by this WebFlowLayout. This is 
    * only temporarily set during rendering. */
  private WebContainer parent = null;
  private Object renderInfoAdapter;
  
  /** Constructor */
  public WebFlowLayout() {
    initCells();
  }

  /**
    * inits all cells in this WebFlowLayout.<br>
    * This is needed not only in the constructor, but also while cloning.
    */
  protected void initCells() {
    tableRow = new WebTableRow();
    tableCell = new WebTableCell();
    tableRow.addCell( tableCell );
  }

  /**  checks if the constraint parameter in the add method of a WebContainer
   *  has the correct type */
  public boolean checkConstraint( final Object constraint ) {
    return constraint == null;
  }

  /** returns a clone of this WebFlowLayout.<br>
    * Cloning a WebLayout involves a copy of all settings and inits, but no
    * cloning or copying added components ( see @link WebContainer.clone() ).*/
  public Object clone() throws CloneNotSupportedException {
    WebFlowLayout result = ( WebFlowLayout )super.clone();
    result.tableRow = new WebTableRow();
    WebTableCell cloneTableCell = ( WebTableCell )this.tableCell.clone();
    result.tableCell = cloneTableCell;
    result.tableRow.addCell( cloneTableCell );
    return result;
  }

  /** <p>for internal use in rendering this LifeCycleHelper.</p> */
  protected Object createRenderInfo() {
    return new FlowLayoutRenderInfo( parent );
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
        private FlowLayoutRenderInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = new FlowLayoutRenderInfo( parent );
        }
      };
    }
    return renderInfoAdapter;
  }

  /** <p>Returns the Area specified in the constraint object. Used e.g. for
    * setting the format of the WebTableCell that is the Area.</p>
    * <p>Note: better don't use this, use {@link #getRegion() getRegion} 
    * instead. Constraints are not supported in WebFlowLayout and will be 
    * ignored.</p>
    * @param constraint specifies the Area */
  public Area getArea( final Object constraint ) {
    return tableCell;
  }

  /** <p>returns the Area specified in the constraint object. Used e.g. for
    * setting the format of the WebTableCell that is the Area.</p> */
  public Area getArea() {
    return tableCell;
  }

  /** 
   * <p>Gets the region specified in the constraints object used; e.g. for 
   * setting the format of the region.</p>
   * <p>Don't use this, use {@link #getRegion() getRegion} instead! Constraints 
   * are not supported in WebFlowLayout and will be ignored.</p>
   * @param constraints specifies the region
   * @deprecated replaced by {@link #getArea(Object)} 
   */
  public WebTableCell getRegion( final Object constraints ) {
    return tableCell;
  }

  /** gets the region of the FlowLayout.
    * used e.g. for setting the format
    * of the region
    * @deprecated replaced by {@link #getArea()} */
  public WebTableCell getRegion() {
    return tableCell;
  }

  /** <p>creates the layout for the WebContainer.</p>
    *
    * @param parent the WebContainer which uses this layout manager */
  public void layoutWebContainer( final WebContainer parent ) throws IOException {
    LifeCycleHelper.render( this, parent );
  }
  
  public WebTableRow getTableRow() {
    return tableRow;
  }
}