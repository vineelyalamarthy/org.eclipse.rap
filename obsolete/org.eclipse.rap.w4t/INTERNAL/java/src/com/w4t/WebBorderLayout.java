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
import com.w4t.renderinfo.BorderLayoutRenderInfo;


/** <p>Defines the WebBorderLayout.</p>
  * <p>The WebBorderLayout divides the WebContainer which uses this
  * layout into five Region 'NORTH', 'SOUTH', 'CENTER', 'EAST' and 'WEST'.
  * To each region you can add WebComponents by specifing the
  * Region by its name.</p>
  * <p>Code Example:
  * <pre>
  * package test;
  *
  * import org.eclipse.rap.*;
  * import org.eclipse.rap.event.*;
  *
  * public class Test extends WebForm {
  *
  * // declarations
  * WebBorderLayout wbbl;
  *
  * WebLabel wblTextTop;
  * WebLabel wblTextBottom;
  * WebLabel wblTextLeft;
  * WebLabel wblTextRight;
  * WebLabel wblTextCenter;
  *
  * public void setWebComponents() throws Exception {
  *
  *    wbbl = ( WebBorderLayout ) this.getWebLayout();
  *
  *    // set attributes for the entire BorderLayout
  *    wbbl.setBgColor( new WebColor( "yellow" ) );
  *    wbbl.setCellpadding( "10" );
  *    wbbl.setBorder( "3" );
  *
  *    // set attributes for one special region
  *    wbbl.getRegion( "NORTH" ).setBgColor( new WebColor( "CCFFFF" ) );
  *    wbbl.getRegion( "CENTER" ).setWidth( "50%" );
  *
  *    // define the labels
  *    // The Constructor of the class {@link WebLabel WebLabel} takes as
  *    // parameter the text to be shown.
  *    wblTextTop   = new WebLabel( "-place for the header-" );
  *    wblTextBottom  = new WebLabel( "-place for the footer-" );
  *    wblTextLeft  = new WebLabel( "-left Border-" );
  *    wblTextRight = new WebLabel( "-right Border-" );
  *    // the center region is often used to put another panel with a different
  *    // layout manager, e.g. a gridLayout.
  *    wblTextCenter = new WebLabel( "-center region-" );
  *
  *    // add the labels to the form and position them with the method
  *    // add(WebComponent,Object) of the Class
  *    // {@link WebContainer WebContainer}
  *    this.add( wblTextTop, "NORTH" );
  *    this.add( wblTextBottom, "SOUTH" );
  *    this.add( wblTextLeft, "WEST" );
  *    this.add( wblTextRight, "EAST" );
  *    this.add( wblTextCenter, "CENTER" );
  *  }
  *}
  * </pre>
  */
public class WebBorderLayout extends WebTable implements WebLayout {

  
  // public fields
  ////////////////
  
  /** <p>constraint for the "North" area of the WebBorderLayout.</p> */
  public static final String NORTH  = "North";
  /** <p>constraint for the "East" area of the WebBorderLayout.</p> */
  public static final String EAST   = "East";
  /** <p>constraint for the "Center" area of the WebBorderLayout.</p> */
  public static final String CENTER = "Center";
  /** <p>constraint for the "West" area of the WebBorderLayout.</p> */
  public static final String WEST   = "West";
  /** <p>constraint for the "South" area of the WebBorderLayout.</p> */
  public static final String SOUTH  = "South";

  
  /** the WebTableRow which contains the 'NORTH' cell */
  private WebTableRow topRow = null;
  /** the WebTableRow which contains the 'WEST', 'CENTER' and 'EAST' cell */
  private WebTableRow middleRow = null;
  /** the WebTableRow which contains the 'SOUTH' cell */
  private WebTableRow bottomRow = null;
  /** the 'NORTH' cell */
  private WebTableCell northCell = null;
  /** the 'EAST' cell */
  private WebTableCell eastCell = null;
  /** the 'CENTER' cell */
  private WebTableCell centerCell = null;
  /** the 'WEST' cell */
  private WebTableCell westCell = null;
  /** the 'SOUTH' cell */
  private WebTableCell southCell = null;

  /** the WebContainer which is layouted by this WebBorderLayout. This is 
    * only temporarily set during rendering. */
  private WebContainer parent = null;
  private Object renderInfoAdapter;

  
  /** Constructor */
  public WebBorderLayout() {
    super();
    initCells();
    addCells();
  }

  /**
    * inits all cells in this WebBorderLayout.<br>
    * This is needed not only in the constructor, but also while cloning.
    */
  protected void initCells() {
    northCell = new WebTableCell();
    northCell.setColspan( "3" );
    northCell.setAlign( "center" );
    northCell.setVAlign( "middle" );

    westCell = new WebTableCell();
    westCell.setAlign( "center" );
    westCell.setVAlign( "middle" );

    centerCell = new WebTableCell();
    centerCell.setAlign( "center" );
    centerCell.setVAlign( "middle" );

    eastCell = new WebTableCell();
    eastCell.setAlign( "center" );
    eastCell.setVAlign( "middle" );

    southCell = new WebTableCell();
    southCell.setColspan( "3" );
    southCell.setAlign( "center" );
    southCell.setVAlign( "middle" );
  }

  protected void addCells() {
    topRow = new WebTableRow();
    topRow.addCell( northCell );

    middleRow = new WebTableRow();
    middleRow.addCell( westCell );
    middleRow.addCell( centerCell );
    middleRow.addCell( eastCell );

    bottomRow = new WebTableRow();
    bottomRow.addCell( southCell );
  }

  /**
   * checks if the constraint parameter in the add method of a WebContainer
   * has the correct type
   */
  public boolean checkConstraint( final Object constraint ) {
    boolean isCorrect = false;

    if( constraint instanceof String
     && ( ( ( String )constraint ).equalsIgnoreCase( "NORTH" )
       || ( ( String )constraint ).equalsIgnoreCase( "WEST" )
       || ( ( String )constraint ).equalsIgnoreCase( "CENTER" )
       || ( ( String )constraint ).equalsIgnoreCase( "EAST" )
       || ( ( String )constraint ).equalsIgnoreCase( "SOUTH" ) ) ) {
      isCorrect = true;
    }
    return isCorrect;
  }

  /**
    * returns a clone of this WebBorderLayout.<br>
    * Cloning a WebLayout involves a copy of all settings and inits, but no
    * cloning or copying added components ( see @link WebContainer.clone() ).
    */
  public Object clone() throws CloneNotSupportedException {
    WebBorderLayout clone = ( WebBorderLayout )super.clone();

    // inits
    clone.northCell = ( WebTableCell )this.northCell.clone();
    clone.westCell = ( WebTableCell )this.westCell.clone();
    clone.centerCell = ( WebTableCell )this.centerCell.clone();
    clone.eastCell = ( WebTableCell )this.eastCell.clone();
    clone.southCell = ( WebTableCell )this.southCell.clone();

    clone.addCells();
    return clone;
  }

  /** <p>returns the region (of type WebTableCell) specified in the
    * constraints object. Used e.g. for setting the format
    * of the WebTableCell that is the Area.</p>
    * @param constraint specifies the region. Must be one of the
    *                   String constants 'NORTH', 'SOUTH', 'CENTER',
    *                   'EAST' or 'WEST'
    */
  public Area getArea( final Object constraint ) {
    String region = ( String )constraint;
    WebTableCell wtc = null;
    if( region.equalsIgnoreCase( "NORTH" ) ) {
      wtc = northCell;
    }
    if( region.equalsIgnoreCase( "WEST" ) ) {
      wtc = westCell;
    }
    if( region.equalsIgnoreCase( "CENTER" ) ) {
      wtc = centerCell;
    }
    if( region.equalsIgnoreCase( "EAST" ) ) {
      wtc = eastCell;
    }
    if( region.equalsIgnoreCase( "SOUTH" ) ) {
      wtc = southCell;
    }
    return wtc;
  }

  /**
    * gets the region (type WebTableCell) specified in the
    * constraints object. used e.g. for setting the format
    * of the region
    * @param constraint specifies the region. One of these
    * Stringconstants 'NORTH', 'SOUTH', 'CENTER', 'EAST' or 'WEST'
    * @deprecated replaced by {@link #getArea(Object)}
    */
  public WebTableCell getRegion( final Object constraint ) {
    return ( WebTableCell )getArea( constraint );
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
        private BorderLayoutRenderInfo renderInfo;
        public Object getInfo() {
          return renderInfo;
        }
        public void createInfo() {
          renderInfo = new BorderLayoutRenderInfo( parent, 
                                                   northCell, 
                                                   westCell, 
                                                   centerCell, 
                                                   eastCell, 
                                                   southCell,
                                                   topRow,
                                                   middleRow,
                                                   bottomRow );
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
}