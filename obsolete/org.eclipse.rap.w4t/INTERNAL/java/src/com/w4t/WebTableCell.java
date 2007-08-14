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
import java.util.ArrayList;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.internal.simplecomponent.UniversalAttributes;
import com.w4t.internal.tablecell.DefaultSpacingHelper;
import com.w4t.internal.tablecell.SpacingHelper;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/** <p>This is a helping class for positioning the WebComponents. It
  * builds a html table cell.</p>
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
  *   WebBorderLayout wbbl;
  *
  *   WebLabel wblTextTop;
  *   WebLabel wblTextLeft;
  *   WebLabel wblTextRight;
  *   WebLabel wblTextBottom;
  *   WebLabel wblTextCenter;
  *
  *   public void setWebComponents() throws Exception {
  *
  *     wbbl = ( WebBorderLayout ) this.getWebLayout();
  *
  *     // set attributes for one special region (see the set-methods in
  *     // this class. To get the current values of the attributes use the
  *     // corresponding get-methods.
  *     wbbl.getArea( "NORTH" ).setBgColor( new WebColor( "yellow" ) );
  *     wbbl.getArea( "SOUTH" ).setBgColor( new WebColor( "yellow" ) );
  *     wbbl.getArea( "WEST" ).setBgColor( new WebColor( "C0C0C0" ) );
  *     wbbl.getArea( "EAST" ).setBgColor( new WebColor( "C0C0C0" ) );
  *     wbbl.getArea( "NORTH" ).setWidth( "800" );
  *     wbbl.getArea( "CENTER" ).setWidth( "50%" );
  *     wbbl.getArea( "WEST" ).setAlign( "left" );
  *     wbbl.getArea( "EAST" ).setAlign( "right" );
  *
  *
  *     // define the label and add it to the form
  *     wblTextTop    = new WebLabel( "-header text-" );
  *     wblTextLeft   = new WebLabel( "-left text-" );
  *     wblTextRight  = new WebLabel( "-right text-" );
  *     wblTextBottom = new WebLabel( "-bottom text-" );
  *     wblTextCenter = new WebLabel( "-center text-" );
  *     this.add( wblTextTop, "NORTH" );
  *     this.add( wblTextLeft, "WEST" );
  *     this.add( wblTextRight, "EAST" );
  *     this.add( wblTextBottom, "SOUTH" );
  *     this.add( wblTextCenter, "CENTER" );
  *   }
  * }
  * </pre>
  */
public class WebTableCell extends WebObject implements Area, SimpleComponent {

  private static final WebColor DEFAULT_BG_COLOR 
   = new WebColor( DefaultColorScheme.get( DefaultColorScheme.TABLE_BG ) );

  private final static UniversalAttributes DEFAULT_UNIVERSAL_ATTRIBUTES
    = createUniversalAttributes();
  
  /** WebComponents to build in this WebTableCell */
  protected ArrayList content = new ArrayList( 2 );
  /** value to show in this cell */
  protected String value = "";
  /** whether <code>value</code> is already encoded */
  protected boolean valueEncoded;
  /** the html attribute settings of the table cell */
  protected String align = "";
  /** the html attribute settings of the table cell */
  protected String valign = "";
  /** the html attribute settings of the table cell */
  protected boolean useNowrap = false;
  /** the html attribute settings of the table cell */
  protected WebColor bgColor = DEFAULT_BG_COLOR;
  /** the html attribute settings of the table cell */
  protected String colspan = "";
  /** the html attribute settings of the table cell */
  protected String rowspan = "";
  /** the html attribute settings of the table cell */
  protected String width = "";
  /** the html attribute settings of the table cell */
  protected String height = "";
  /** the cell spacing of a single cell */
  protected String spacing = "";
  /** the cell padding of a single cell */
  protected String padding = "";
  /** the path of a image, which is used as backgroud image of this cell */
  protected String background = "";
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes;

  private SpacingHelper spacingHelper;

  public WebTableCell() {
    this( new DefaultSpacingHelper() );
  }

  public WebTableCell( final SpacingHelper spacingHelper ) {
    this.spacingHelper = spacingHelper;
  }

  /** returns a clone of this WebTableCell */
  public Object clone() throws CloneNotSupportedException {
    WebTableCell result = ( WebTableCell )super.clone();
    result.spacingHelper = this.spacingHelper;
    result.content = new ArrayList( 2 );
    result.value = "";
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes 
        = ( UniversalAttributes )universalAttributes.clone();
    }
    result.bgColor = ( WebColor )this.bgColor.clone();
    return result;
  }

  /** build the html table cell with the specified properties */
  public void render() throws IOException {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    HtmlResponseWriter out = stateInfo.getResponseWriter();
    
    spacingHelper.getSpacingStart( this );
    out.startElement( HTML.TD, null );
    writeTableCellAttributes();
    if( universalAttributes != null ) {
      universalAttributes.writeUniversalAttributes();
    } else {
      DEFAULT_UNIVERSAL_ATTRIBUTES.writeUniversalAttributes();
    }
    
    // required until it's sure that all component-rendering kits are
    // converted to the new API
    // makes sure that the tag gets closed 
    out.writeText( "", null );
    
    // now build the html code for the WebComponents in this cell
    int outSize = out.getBodySize();
    boolean insertNonBreakingSpace = true;
    if( content.size() > 0 ) {
      for( int i = 0; i < content.size(); i++ ) {
        WebComponent component = ( WebComponent )content.get( i );
        if( component != null ) {
          if (   !component.isVisible() 
              && stateInfo.getDetectedBrowser().isAjaxEnabled() ) 
          {
            RenderUtil.appendAjaxPlaceholder( out, component, true );
          } else {
            LifeCycleHelper.render( component );
            insertNonBreakingSpace = outSize == out.getBodySize();
          }
        }
      }
    }
    if( value.toString().equals( "" ) && insertNonBreakingSpace ) {
      out.writeNBSP();
    } else {
      if( valueEncoded ) {
        out.append( value );
      } else {
        out.writeText( value, null );
      }
    }
    out.endElement( HTML.TD );
    spacingHelper.getSpacingEnd( this );
  }

  /** adds a WebComponent to the content of the WebTableCell
    * @param contentElement shown in this WebTableCell */
  public void addContentElement( final WebComponent contentElement ) {
    content.add( contentElement );
  }

  /** remove a WebComponent from the content of the WebTableCell
    * @param contentElement WebComponent to remove */
  public void removeContentElement( final WebComponent contentElement ) {
    boolean removed = false;
    for( int i = 0; !removed && i < content.size(); i++ ) {
      WebComponent wc = ( WebComponent )content.get( i );
      if( wc.getUniqueID().equals( contentElement.getUniqueID() ) ) {
        content.remove( i );
        removed = true;
      }
    }
  }

  /** sets the value, which should be shown in this cell */
  public void setValue( final String value ) {
    this.value = value;
  }
  
  // TODO [rh] This is a hack. find better solution and remove this 
  public void setValueEncoded( final boolean valueEncoded ) {
    this.valueEncoded = valueEncoded;
  }
  
  public boolean isValueEncoded() {
    return valueEncoded;
  }

  /** returns a String which contains the settings of the HTML
   * attributes which are used in this TableCell 
   * @throws IOException */
  protected void writeTableCellAttributes() throws IOException {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    if( align != "" ) {
      out.writeAttribute( HTML.ALIGN, align, null );
    }
    if( valign != "" ) {
      out.writeAttribute( HTML.VALIGN, valign, null );
    }
    if( useNowrap ) {
      out.writeAttribute( HTML.NOWRAP, null, null );
      // if ( W4TContext.getBrowser().isAjaxEnabled() ) {
      // out.writeAttribute( HTML.NOWRAP, HTML.NOWRAP, null );
      // } else {
      // out.writeAttribute( HTML.NOWRAP, null, null );
      // }
    }
    if( !"".equals( bgColor.toString() ) ) {
      out.writeAttribute( HTML.BGCOLOR, bgColor.toString(), null );
    }
    if( colspan != "" ) {
      out.writeAttribute( HTML.COLSPAN, colspan, null );
    }
    if( rowspan != "" ) {
      out.writeAttribute( HTML.ROWSPAN, rowspan, null );
    }
    if( width != "" ) {
      out.writeAttribute( HTML.WIDTH, width, null );
    }
    if( height != "" ) {
      out.writeAttribute( HTML.HEIGHT, height, null );
    }
    if( background != "" ) {
      out.writeAttribute( HTML.BACKGROUND, background, null );
    }
  }

  /** sets the alignment of this cell (horizontal). Corresponds to the HTML
    * attribute align of the tag &lt;td&gt;.
    * For a short example see the beginning of {@link WebTableCell WebTableCell}
    * @param align specifies the alignment by a string. This can be for example
    * "left", "right" or "center". */
  public void setAlign( final String align ) {
    this.align = align;
  }

  /** returns a string which contains the (horizontal) alignment of this cell.
    * Corresponds to the HTML attribute align of the tag &lt;td&gt;. */
  public String getAlign() {
    return align;
  }

  /** sets the vertical alignment of this cell. Corresponds to the HTML
    * attribute valign of the tag &lsaquo;td&rsaquo;.
    * @param valign specifies the alignment by a string. This can be for
    * example "top", "bottom" or "middle". */
  public void setVAlign( final String valign ) {
    this.valign = valign;
  }

  /** returns a string which contains the vertical alignment of this cell.
    * Corresponds to the HTML attribute align of the tag &lt;td&gt;.*/
  public String getVAlign() {
    return valign;
  }

  /** flag to avoid linefeed in a table cell*/
  public void setNowrap( final boolean nowrap ) {
    this.useNowrap = nowrap;
  }

  /** flag to avoid linefeed in a table cell*/
  public boolean isNowrap() {
    return useNowrap;
  }

  /** sets the background color of this cell. Corresponds to the HTML attribute
    * bgcolor of the tag &lt;td&gt;.
    * For a short example see the beginning of {@link WebTableCell WebTableCell}
    * @param bgColor specifies the chosen color. bgColor can either be a
    * hexadecimal RGB-value (red/green/blue-value of the color) or one of 16
    * color names (like "black", "white", "red" etc.) */
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /** returns a string which contains the background color of this cell.
    * Corresponds to the HTML attribute bgcolor of the tag &lt;td&gt;.*/
  public WebColor getBgColor() {
    return bgColor;
  }

  /** joins several cells to one cell in horizontal direction. Corresponds to
    * the HTML attribute colspan of the tag &lsaquo;td&rsaquo;.
    * @param colspan specifies the number of cells to be joined (positive
    * integer value). */
  public void setColspan( final String colspan ) {
    this.colspan = colspan;
  }

  /** returns a String which contains the number of joined cells (horizontal
    * direction).
    * Corresponds to the HTML attribute colspan of the tag &lt;td&gt;.*/
  public String getColspan() {
    return colspan;
  }

  /** joins several cells to one cell in vertical direction. Corresponds to the
    * HTML attribute rowspan of the tag &lsaquo;td&rsaquo;.
    * @param rowspan specifies the number of cells to be joined (positive
    * integer value). */
  public void setRowspan( final String rowspan ) {
    this.rowspan = rowspan;
  }

  /** returns a string which contains the number of joined cells (vertical
    * direction).
    * Corresponds to the HTML attribute rowspan of the tag &lsaquo;td&rsaquo;.*/
  public String getRowspan() {
    return rowspan;
  }

  /** sets the width of the cell.
    * Corresponds to the HTML attribute width of the tag &lt;td&gt;.
    * For a short example see the beginning of {@link WebTableCell WebTableCell}
    * @param width specifies the width either by a positive integer value or by
    * a percentage. */
  public void setWidth( final String width ) {
    this.width = width;
  }

  /** returns a string which contains the width of the cell.
    * Corresponds to the HTML attribute width of the tag &lt;td&gt;.*/
  public String getWidth() {
    return width;
  }

  /** sets the height of the cell.
    * Attention! This attribute doesn't work well with some browsers!
    * Instead of setting this attribute you can place a blind gif into this
    * cell to force the required height.
    * Corresponds to the HTML attribute height of the tag &lt;td&gt;.
    * @param height specifies the height either by a positive integer value or
    * by a percentage. */
  public void setHeight( final String height ) {
    this.height = height;
  }

  /** returns a string which contains the height of the cell.
    * Corresponds to the HTML attribute height of the tag &lt;td&gt;.*/
  public String getHeight() {
    return height;
  }

  /** sets the thickness of the borders of this cell.
    * Similar to the HTML attribute cellspacing of the tag
    * &lt;table&gt;. The difference is that in HTML the cellspacing
    * refers to all cells of a table; here the spacing refers only to this
    * single cell.
    * @param spacing specifies the thickness by a positive integer value
    * (pixel). */
  public void setSpacing( final String spacing ) {
    this.spacing = spacing;
  }

  /** returns a string which contains the thickness of the borders of this cell.
    * Similar to the HTML attribute cellspacing of the tag
    * &lsaquo;table&rsaquo;. The diffence is that in HTML the cellspacing refers
    * to all cells of a table; here the spacing refers only 
    * to this single cell.*/
  public String getSpacing() {
    return spacing;
  }

  /** sets the distance of the content of this cell to its border.
    * Similar to the HTML attribute cellpadding of the tag
    * &lt;table&gt;. The difference is that in HTML the cellpadding
    * refers to all cells of a table; here the padding refers only to this
    * single cell.
    * @param padding specifies the distance by a positive 
    * integer value (pixel). */
  public void setPadding( final String padding ) {
    this.padding = padding;
  }

  /** returns a string which contains the distance of the content of this cell
    * to its border.
    * Similar to the HTML attribute cellpadding of the tag
    * &lt;table&gt;. The difference is that in HTML the cellpadding
    * refers to all cells of a table; here the padding refers only to this
    * single cell. */
  public String getPadding() {
    return padding;
  }

  /** the path of a image, which is used as backgroud image of this cell */
  public void setBackground( final String background ) {
    this.background = background;
  }

  /** the path of a image, which is used as backgroud image of this cell */
  public String getBackground() {
    return background;
  }
  
  // interface methods of org.eclipse.rap.SimpleComponent
  // (no javadoc comments, so they are copied from the interface)
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
      universalAttributes = createUniversalAttributes();
    }
    return universalAttributes;
  }
  
  private static UniversalAttributes createUniversalAttributes() {
    UniversalAttributes result = new UniversalAttributes();
    result.getStyle().setFontFamily( "" );
    result.getStyle().setFontSize( Style.NOT_USED );
    return result;
  }
}