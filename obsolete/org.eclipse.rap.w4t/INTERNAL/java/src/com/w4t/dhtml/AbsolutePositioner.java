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
package com.w4t.dhtml;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/**
  * <p>An AbsolutePositioner is a helping class that can be used to position
  * WebComponents absolutely on a WebContainer which has an AbsoluteLayout as
  * layout manager.</p>
  * <p>An AbsolutePositioner is also the Area object that is returned by the
  * <code>AbsoluteLayout.getArea(Object)</code> method.</p>
  */
public class AbsolutePositioner extends WebObject implements Area {

  /** <p>the id of the div tag element that is wrapped around the content of
    * this positioner</p> */
  private String id = "";
  private WebComponent contentComponent;

  // attributes for inherited getters and setters from org.eclipse.rap.Area
  /////////////////////////////////////////////////////////////////

  /** <p>the horizontal alignment of this Positioner, specified by a string,
    * which may be for example "left", "right" or "center". See an html
    * reference for more information about alinments.</p> */
  private String align = "";
  /** <p>the path of an image that is used as backgroud image of
    * this Positioner.</p> */
  private String background = "";
  /** <p>the background color of this Positioner, can be a WebColor containing
    * either a hexadecimal RGB-value (red/green/blue-value of the color) or
    * one of 16 color names (like "black", "white", "red" etc.) See an html
    * reference for more information about colors.</p> */
  private WebColor bgColor;
  /** <p>the height of this Positioner, specified the height either by a
    * positive integer value or by a percentage. See an html reference for
    * more information.</p> */
  private String height = "";
  /** <p>whether linebreaks are avoided in the text of this Positioner.</p> */
  private boolean nowrap = false;
  /** <p>a style object containing css attributes for this Positioner.</p> */
  private Style style;
  /** <p>the vertical alignment of this Positioner, specifid by a string,
    * which may be for example "top", "bottom" or "middle". See an html
    * reference for more information about alignments.</p> */
  private String vAlign = "";
  /** <p>sets the width of this Positioner, specified either by a positive
    * integer value or by a percentage. See an html reference for more
    * information. */
  private String width = "";


  /** <p>constructs a new Positioner.</p> */
  public AbsolutePositioner() {
    String key = DefaultColorScheme.ABSOLUTE_LAYOUT_BG;
    this.bgColor = new WebColor( DefaultColorScheme.get( key ) );
    this.style = new Style();
    contentComponent = null;
  }


  // rendering methods
  ////////////////////

  /** build the actual html representation of the component that is
    * positioned absolutely by this AbsolutePositioner.</p> */
  void render( final HtmlResponseWriter out ) throws IOException {
    createRenderHeader( out );
    LifeCycleHelper.render( contentComponent );
    createRenderFooter( out );
  }

  /** <p>helping method for render(); prepares the content that is set in
    * this Positioner to be positioned absolutely on its
    * parent WebContainer.</p> 
    * @throws IOException */
  private void createRenderHeader( final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, id, null );
    out.writeAttribute( HTML.STYLE, createStyle(), null );
    out.closeElementIfStarted();
  }
  
  private String createStyle() {
    StringBuffer result = new StringBuffer();
    result.append( "position:absolute;background-color:white;" );
    if( !"".equals( style.getTop() ) ) {
      result.append( "top:" );
      result.append( style.getTop() );
      result.append( ";" );
    }
    if( !"".equals( style.getLeft() ) ) {
      result.append( "left:" );
      result.append( style.getLeft() );
      result.append( ";" );
    }
    if( !"".equals( style.getWidth() ) ) {
      result.append( "width:" );
      result.append( style.getWidth() );
      result.append( ";" );
    }
    return result.toString();
  }

  /** <p>helping method for render(); prepares the content that is set in
    * this Positioner to be positioned absolutely on its parent
    * WebContainer.</p> 
   * @throws IOException */
  private void createRenderFooter( final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.endElement( HTML.DIV );
  }


  // setters and getters for positioning etc.
  ///////////////////////////////////////////

  /** TODO [rh]JavaDoc? */
  public void setContent( final WebComponent contentComponent ) {
    this.contentComponent = contentComponent;
  }

  /** TODO [rh]JavaDoc? */
  public void setPosition( final AbsoluteConstraint constraint ) {
    Point pPos = constraint.getTopLeftCoordinates();
    style.setLeft( String.valueOf( pPos.getX() + "px" ) );
    style.setTop( String.valueOf( pPos.getY() + "px" ) );
    style.setWidth( constraint.getWidth() );
  }


  /** TODO [rh]JavaDoc? */
  public void setPosition( final Rectangle recPosition ) {
    style.setLeft( String.valueOf( recPosition.getTopLeftX() + "px" ) );
    style.setTop( String.valueOf( recPosition.getTopLeftY() + "px" ) );
    style.setWidth( String.valueOf( recPosition.getWidth() + "px" ) );
    style.setHeight( String.valueOf( recPosition.getHeight() + "px" ) );
  }

  /** TODO [rh]JavaDoc? 
    * @deprecated don't use it */
  public void setPosition( final Point pPos ) {
    style.setLeft( String.valueOf( pPos.getX() + "px" ) );
    style.setTop( String.valueOf( pPos.getY() + "px" ) );
  }

  /** <p>sets the id of this Positioner and its content on the layouted
    * container.</p> */
  void setID( final String id ) {
    this.id = id;
  }

  /** TODO [rh]JavaDoc? */
  public void setClipping( final Rectangle recClipping ) {
    // TODO
  }


  // inherited attribute setters and getters from org.eclipse.rap.Area
  ////////////////////////////////////////////////////////////

  /** <p>sets the horizontal alignment of this Area.</p>
    * @param align specifies the alignment by a string. This can be for example
    *              "left", "right" or "center". See an html reference
    *              for more information about alignments.
    */
  public void setAlign( final String align ) {
    this.align = align;
  }


  /** <p>returns the horizontal alignment of this Area.</p> */
  public String getAlign() {
    return align;
  }

  /** <p>sets the path of an image that is used as backgroud image of
    * this Area.</p> */
  public void setBackground( final String background ) {
    this.background = background;
  }

  /** <p>returns the path of an image that is used as backgroud image of
    * this Area.</p> */
  public String getBackground() {
    return background;
  }

  /** <p>sets the background color of this Area.</p>
    * @param bgColor specifies the chosen color. bgColor can be a webColor
    *                containing either a hexadecimal RGB-value
    *                (red/green/blue-value of the color) or one of 16 color
    *                names (like "black", "white", "red" etc.) See an html
    *                reference for more information about colors. */
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /** <p>returns the background color of this Area.</p> */
  public WebColor getBgColor() {
    return bgColor;
  }

  /** <p>sets the height of this Area.</p>
    * @param height specifies the height either by a positive integer value or
    *               by a percentage. See an html reference for more
    *               information. */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /** <p>returns the height of this Area.</p> */
  public String getHeight() {
    return height;
  }

  /** <p>sets whether linebreaks are avoided in the text of this Area.</p> */
  public void setNowrap( final boolean nowrap ) {
    this.nowrap = nowrap;
  }

  /** <p>returns whether linebreaks are avoided in the text of this
    * Area.</p> */
  public boolean isNowrap() {
    return nowrap;
  }

  /** <p>sets the vertical alignment of this Area.</p>
    * @param vAlign specifies the alignment by a string. This can be for
    *               example "top", "bottom" or "middle". See an html reference
    *               for more information about alignments.
    */
  public void setVAlign( final String vAlign ) {
    this.vAlign = vAlign;
  }

  /** <p>returns the vertical alignment of this Area.</p> */
  public String getVAlign() {
    return vAlign;
  }

  /** <p>sets the width of this Area.</p>
    * @param width specifies the width either by a positive integer value or by
    *              a percentage. See an html reference for more information. */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** <p>returns the width of this Area.</p> */
  public String getWidth() {
    return width;
  }
  
  /** <p>returns the name of the cascading stylesheet class for
   * this Area.</p>  */
  public String getCssClass() {
    return "";
  }  
  
  /** <p>returns a style object containing the css attributes for
   * this Area.</p>  */
  public Style getStyle() {
    return null;
  }
  
  /** <p>returns the tooltip text for this Area.</p>  */
  public String getTitle() {
    return "";
  }
  
  /** <p>sets the name of the cascading stylesheet class for this Area.</p>  */
  public void setCssClass( final String cssClass ) {
  }
  
  /** <p>sets a style object containing css attributes for this Area.</p>  */
  public void setStyle( final Style style ) {
  }
  
  /** <p>sets the tooltip text for this Area.</p>  */
  public void setTitle( final String title ) {
  }
  
}
