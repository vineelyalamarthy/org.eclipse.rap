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

import com.w4t.types.WebColor;
import com.w4t.types.WebTriState;
import com.w4t.util.DefaultColorScheme;


/** <p>This is a helping class for W4 components to create
  * Cascading Style Sheets entries.</p>
  * <p>It creates the values of the style attribute of every
  * html tag and is used in the WebObject class, which is the
  * base class of every WebComponent. The properties of this class
  * are named like the style properties, but leaving the '-' out and
  * the letter after the '-' is set to uppercase. The only exception are the
  * background properties, 'background' is shortend to 'bg'. Be careful
  * to use style combinations that work on all browsers quite good.</p>
  */
public class Style extends WebComponentProperties {
  
  /** <p>a placeholder value for int style attributes which indicates 
   * that the attribute in question should not be rendered.</p> */
  public static final int NOT_USED = -1;
  
  private static final WebTriState EMPTY_TRI_STATE = new WebTriState( "" );
  private static final WebColor DEFAULT_BORDER_RIGHT_COLOR 
    = new WebColor( getColor( DefaultColorScheme.STYLE_BORDER_RIGHT ) );
  private static final WebColor DEFAULT_BORDER_LEFT_COLOR
    = new WebColor( getColor( DefaultColorScheme.STYLE_BORDER_LEFT ) );
  private static final WebColor DEFAULT_BORDER_BOTTOM_COLOR 
    = new WebColor( getColor( DefaultColorScheme.STYLE_BORDER_BOTTOM ) );
  private static final WebColor DEFAULT_BORDER_TOP_COLOR 
    = new WebColor( getColor( DefaultColorScheme.STYLE_BORDER_TOP ) );
  private static final WebColor DEFAULT_BORDER_COLOR
    = new WebColor( getColor( DefaultColorScheme.STYLE_BORDER ) );
  private static final WebColor DEFAULT_COLOR 
    = new WebColor( getColor( DefaultColorScheme.STYLE_FONT ) );
  private static final WebColor DEFAULT_BG_COLOR
    = new WebColor( getColor( DefaultColorScheme.STYLE_BG ) );

  private static final String SEMI  = ";";
  private static final String COLON = ":";
  private static final String BLANK = " ";
  
  private static final int BG_ATTACHMENT = 0;
  private static final int BG_COLOR = 1;
  private static final int BG_IMAGE = 2;
  private static final int BG_POSITION = 3;
  private static final int BG_REPEAT = 4;
  private static final int FONT_FAMILY = 5;
  private static final int FONT_SIZE = 6;
  private static final int FONT_STYLE = 7;
  private static final int FONT_VARIANT = 8;
  private static final int FONT_WEIGHT = 9;
  private static final int COLOR = 10;
  private static final int TEXT_DECORATION = 11;
  private static final int TEXT_TRANSFORM = 12;
  private static final int TEXT_INDENT = 13;
  private static final int LETTER_SPACING = 14;
  private static final int LINE_HEIGHT = 15;
  private static final int TEXT_ALIGN = 16;
  private static final int WORD_SPACING = 17;
  private static final int VERTICAL_ALIGN = 18;
  private static final int BORDER_WIDTH = 19;
  private static final int BORDER_TOP_WIDTH = 20;
  private static final int BORDER_BOTTOM_WIDTH = 21;
  private static final int BORDER_LEFT_WIDTH = 22;
  private static final int BORDER_RIGHT_WIDTH = 23;
  private static final int BORDER_COLOR = 24;
  private static final int BORDER_TOP_COLOR = 25;
  private static final int BORDER_BOTTOM_COLOR = 26;
  private static final int BORDER_LEFT_COLOR = 27;
  private static final int BORDER_RIGHT_COLOR = 28;
  private static final int BORDER_STYLE = 29;
  private static final int BORDER_TOP = 30;
  private static final int BORDER_BOTTOM = 31;
  private static final int BORDER_LEFT = 32;
  private static final int BORDER_RIGHT = 33;
  private static final int BORDER = 34;
  private static final int MARGIN = 35;
  private static final int MARGIN_TOP = 36;
  private static final int MARGIN_BOTTOM = 37;
  private static final int MARGIN_LEFT = 38;
  private static final int MARGIN_RIGHT = 39;
  private static final int PADDING = 40;
  private static final int PADDING_TOP = 41;
  private static final int PADDING_BOTTOM = 42;
  private static final int PADDING_LEFT = 43;
  private static final int PADDING_RIGHT = 44;
  private static final int CLEAR = 45;
  private static final int FLOAT = 46;
  private static final int HEIGHT = 47;
  private static final int WIDTH = 48;
  private static final int LEFT = 49;
  private static final int RIGHT = 50;
  private static final int TOP = 51;
  private static final int BOTTOM = 52;
  private static final int POSITION = 53;
  private static final int VISIBILITY = 54;
  private static final int ZINDEX = 55;
  private static final int CURSOR = 56;
  private static final int DISPLAY = 57;
  private static final int WHITESPACE = 58;
  private static final int OVERFLOW = 59;
  
  private static final int MAX_ATTRIBUTE_ID = OVERFLOW;
  private static final int ATTRIBUTE_COUNT = MAX_ATTRIBUTE_ID + 1;
  
  private static final StyleManager styleManager;
  private static Integer defaultKey;

  static {
    styleManager = new StyleManager( ATTRIBUTE_COUNT );
  }
  
  private Integer currentKey;
  
  public Style() {
    init();
  }

  private void init() {
    if( defaultKey == null ) {
      setBgAttachment( "" );
      setBgColor( DEFAULT_BG_COLOR );
      setBgImage( "" );
      setBgPosition( "" );
      setBgRepeat( "" );
      setFontFamily( "arial,verdana" );
      setFontSize( 8 );
      setFontStyle( "" );
      setFontVariant( "" );
      setFontWeight( "" );
      setColor( DEFAULT_COLOR );
      setTextDecoration( "" );
      setTextTransform( "" );
      setTextIndent( "" );
      setLetterSpacing( "" );
      setLineHeight( "" );
      setTextAlign( "" );
      setWordSpacing( "" );
      setVerticalAlign( "" );
      setBorderWidth( "" );
      setBorderTopWidth( "" );
      setBorderBottomWidth( "" );
      setBorderLeftWidth( "" );
      setBorderRightWidth( "" );
      setBorderColor( DEFAULT_BORDER_COLOR );
      setBorderTopColor( DEFAULT_BORDER_TOP_COLOR );
      setBorderBottomColor( DEFAULT_BORDER_BOTTOM_COLOR );
      setBorderLeftColor( DEFAULT_BORDER_LEFT_COLOR );
      setBorderRightColor( DEFAULT_BORDER_RIGHT_COLOR );
      setBorderStyle( "" );
      setBorderTop( "" );
      setBorderBottom( "" );
      setBorderLeft( "" );
      setBorderRight( "" );
      setBorder( "" );
      setMarginTop( "" );
      setMarginBottom( "" );
      setMarginLeft( "" );
      setMarginRight( "" );
      setMargin( "" );
      setPaddingTop( "" );
      setPaddingBottom( "" );
      setPaddingLeft( "" );
      setPaddingRight( "" );
      setPadding( "" );
      setBottom( "" );
      setClear( "" );
      setFloat( "" );
      setHeight( "" );
      setWidth( "" );
      setLeft( "" );
      setRight( "" );
      setTop( "" );
      setPosition( "" );
      setVisibility( EMPTY_TRI_STATE );
      setZIndex( "" );
      setCursor( "" );
      setDisplay( "" );
      setWhiteSpace( "" );
      setOverflow( "" );
      defaultKey = currentKey;
    } else {
      currentKey = defaultKey;
    }
  }
  
  public Object clone() throws CloneNotSupportedException {
    Style result = ( Style )super.clone();
    result.currentKey = currentKey;
    return result;
  }
  
  /** <p>this builds the style String depending of the attribute 
    * settings.</p> */
  public String toString() {
    if( styleManager.getBufferedStyleText( currentKey ) == null ) {
      StringBuffer style = new StringBuffer();
      createBackground( style );
      createFontAndText( style );
      createBorders( style );
      createMargins( style );
      createPadding( style );
      createElementPositioning( style );
      styleManager.bufferStyleText( currentKey, style.toString() );
    }
    return styleManager.getBufferedStyleText( currentKey );
  }

  private void createElementPositioning( final StringBuffer style ) {
    createAttribute( style, "bottom", getBottom() );
    createAttribute( style, "clear", getClear() );
    createAttribute( style, "float", getFloat() );
    createAttribute( style, "height", getHeight() );
    createAttribute( style, "left", getLeft() );
    createAttribute( style, "position", getPosition() );
    createAttribute( style, "right", getRight() );
    createAttribute( style, "top", getTop() );
    createAttribute( style, "visibility", getVisibility().toString() );
    createAttribute( style, "width", getWidth() );
    createAttribute( style, "z-index", getZIndex() );
    createAttribute( style, "cursor", getCursor() );
    createAttribute( style, "display", getDisplay() );
    createAttribute( style, "white-space", getWhiteSpace() );
    createAttribute( style, "overflow", getOverflow() );
  }

  private void createPadding( final StringBuffer style ) {
    createAttribute( style, "padding", getPadding() );
    createAttribute( style, "padding-bottom", getPaddingBottom() );
    createAttribute( style, "padding-left", getPaddingLeft() );
    createAttribute( style, "padding-right", getPaddingRight() );
    createAttribute( style, "padding-top", getPaddingTop() );
  }

  private void createMargins( final StringBuffer style ) {
    createAttribute( style, "margin-bottom", getMarginBottom() );
    createAttribute( style, "margin-left", getMarginLeft() );
    createAttribute( style, "margin-right", getMarginRight() );
    createAttribute( style, "margin-top", getMarginTop() );
    createAttribute( style, "margin", getMargin() );
  }

  private void createBorders( final StringBuffer style ) {
    createAttribute( style, "border-style", getBorderStyle() );
    createAttribute( style, "border", getBorder() );
    createAttribute( style, "border-width", getBorderWidth() );
    createAttribute( style, "border-top-width", getBorderTopWidth() );
    createAttribute( style, "border-bottom-width", getBorderBottomWidth() );
    createAttribute( style, "border-left-width", getBorderLeftWidth() );
    createAttribute( style, "border-right-width", getBorderRightWidth() );
    createAttribute( style, "border-color", getBorderColor().toString() );
    createBorderColors( style );
    createAttribute( style, "border-top", getBorderTop() );
    createAttribute( style, "border-bottom", getBorderBottom() );
    createAttribute( style, "border-left", getBorderLeft() );
    createAttribute( style, "border-right", getBorderRight() );
  }

  private void createFontAndText( final StringBuffer style ) {
    createAttribute( style, "font-family", getFontFamily() );
    createFontSize( style );
    createAttribute( style, "font-style", getFontStyle() );
    createAttribute( style, "font-variant", getFontVariant() );
    createAttribute( style, "font-weight", getFontWeight() );
    createAttribute( style, "color", getColor().toString() );
    createAttribute( style, "text-decoration", getTextDecoration() );
    createAttribute( style, "text-transform", getTextTransform() );
    createAttribute( style, "text-indent", getTextIndent() );
    createAttribute( style, "letter-spacing", getLetterSpacing() );
    createAttribute( style, "line-height", getLineHeight() );
    createAttribute( style, "text-align", getTextAlign() );
    createAttribute( style, "word-spacing", getWordSpacing() );
    createAttribute( style, "vertical-align", getVerticalAlign() );
  }

  private void createBackground( final StringBuffer style ) {
    createAttribute( style, "background-attachment", getBgAttachment() );
    createAttribute( style, "background-color", getBgColor().toString() );
    createAttribute( style, "background-image", getBgImage() );
    createAttribute( style, "background-position", getBgPosition() );
    createAttribute( style, "background-repeat", getBgRepeat() );
  }


  // helping methods
  //////////////////
  
  private void createAttribute( final StringBuffer style, 
                                final String key, 
                                final String value ) {
    if( !"".equals( value ) ) {
      style.append( key );
      style.append( COLON );
      style.append( value );
      style.append( SEMI );
    }
  }
  
  private void createBorderColors( final StringBuffer style ) {
    String topColor = getBorderTopColor().toString();
    String bottomColor = getBorderBottomColor().toString();
    String leftColor = getBorderLeftColor().toString();
    String rightColor = getBorderRightColor().toString();
    if(    !topColor.equals( "" ) 
        && !bottomColor.equals( "" )
        && !leftColor.equals( "" ) 
        && !rightColor.equals( "" ) )
    {
      style.append( "border-color:" );
      style.append( topColor ); 
      style.append( BLANK );
      style.append( rightColor );
      style.append( BLANK );             
      style.append( bottomColor );
      style.append( BLANK );
      style.append( leftColor );
      style.append( SEMI );            
    }
  }

  private void createFontSize( final StringBuffer style ) {
    if( getFontSize() > NOT_USED ) {
      style.append( "font-size:" + String.valueOf( getFontSize() ) + "pt;" );
    }
  }

  /** sets style attribute bottom */
  public void setBottom( final String bottom ) {
    updateAttribute( BOTTOM, bottom );
  }

  /** gets style attribute bottom */
  public String getBottom() {
    return ( String )styleManager.find( currentKey, BOTTOM );
  }

  /** sets style attribute clear */
  public void setClear( final String clear ) {
    updateAttribute( CLEAR, clear );
  }

  /** gets style attribute clear */
  public String getClear() {
    return ( String )styleManager.find( currentKey, CLEAR );
  }

  /** sets style attribute float */
  public void setFloat( final String strFloat ) {
    updateAttribute( FLOAT, strFloat );
  }

  /** gets the style attribut float */
  public String getFloat() {
    return ( String )styleManager.find( currentKey, FLOAT );
  }

  /** sets the style attribute height */
  public void setHeight( final String height ) {
    updateAttribute( HEIGHT, height );
  }

  /** gets the style attribut height */
  public String getHeight() {
    return ( String )styleManager.find( currentKey, HEIGHT );
  }

  /** sets the style attribute left */
  public void setLeft( final String left ) {
    updateAttribute( LEFT, left );
  }

  /** gets the style attribute left */
  public String getLeft() {
    return ( String )styleManager.find( currentKey, LEFT );
  }

  /** sets the style attribute position */
  public void setPosition( final String position ) {
    updateAttribute( POSITION, position );
  }

  /** gets the style attribute position */
  public String getPosition() {
    return ( String )styleManager.find( currentKey, POSITION );
  }

  /** sets the style attribute right */
  public void setRight( final String right ) {
    updateAttribute( RIGHT, right );
  }

  /** gets the style attribute right */
  public String getRight() {
    return ( String )styleManager.find( currentKey, RIGHT );
  }

  /** sets the style attribute top */
  public void setTop( final String top ) {
    updateAttribute( TOP, top );
  }

  /** gets the style attribute top */
  public String getTop() {
    return ( String )styleManager.find( currentKey, TOP );
  }

  /** sets the style attribute visibility */
  public void setVisibility( final WebTriState visibility ) {
    updateAttribute( VISIBILITY, visibility );
  }

  /** gets the style attribute visibility */
  public WebTriState getVisibility() {
    return ( WebTriState )styleManager.find( currentKey, VISIBILITY );
  }

  /** sets the style attribute width */
  public void setWidth( final String width ) {
    updateAttribute( WIDTH, width );
  }

  /** gets the style attribute width */
  public String getWidth() {
    return ( String )styleManager.find( currentKey, WIDTH );
  }

  /** sets the style attribute z-index */
  public void setZIndex( final String zIndex ) {
    updateAttribute( ZINDEX, zIndex );
  }

  /** gets the style attribute z-index */
  public String getZIndex() {
    return ( String )styleManager.find( currentKey, ZINDEX );
  }

  /** sets the style atrribute overflow
   * possible values are: auto, scroll, hidden, visible  */
  public void setOverflow( final String overflow ) {
    updateAttribute( OVERFLOW, overflow );
  }  
  
  /** gets the style atrribute overflow */
  public String getOverflow() {
    return ( String )styleManager.find( currentKey, OVERFLOW );
  }  

  /** sets the style attribute padding-bottom */
  public void setPaddingBottom( final String paddingBottom ) {
    updateAttribute( PADDING_BOTTOM, paddingBottom );
  }

  /** gets the style attribute padding-bottom */
  public String getPaddingBottom() {
    return ( String )styleManager.find( currentKey, PADDING_BOTTOM );
  }

  /** sets the style attribute padding-left */
  public void setPaddingLeft( final String paddingLeft ) {
    updateAttribute( PADDING_LEFT, paddingLeft );
  }

  /** gets the style attribute padding-left */
  public String getPaddingLeft() {
    return ( String )styleManager.find( currentKey, PADDING_LEFT );
  }

  /** sets the style attribute padding-right */
  public void setPaddingRight( final String paddingRight ) {
    updateAttribute( PADDING_RIGHT, paddingRight );
  }

  /** gets the style attribute padding-right */
  public String getPaddingRight() {
    return ( String )styleManager.find( currentKey, PADDING_RIGHT );
  }

  /** sets the style attribute padding-top */
  public void setPaddingTop( final String paddingTop ) {
    updateAttribute( PADDING_TOP, paddingTop );
  }

  /** gets the style attribute padding-top */
  public String getPaddingTop() {
    return ( String )styleManager.find( currentKey, PADDING_TOP );
  }

  /** sets the style attribute padding */
  public void setPadding( final String padding ) {
    updateAttribute( PADDING, padding );
  }

  /** gets the style attribute padding */
  public String getPadding() {
    return ( String )styleManager.find( currentKey, PADDING );
  }

  /** sets the style attribute margin-bottom */
  public void setMarginBottom( final String marginBottom ) {
    updateAttribute( MARGIN_BOTTOM, marginBottom );
  }

  /** gets the style attribute margin bottom */
  public String getMarginBottom() {
    return ( String )styleManager.find( currentKey, MARGIN_BOTTOM );
  }

  /** sets the style attribute margin-left */
  public void setMarginLeft( final String marginLeft ) {
    updateAttribute( MARGIN_LEFT, marginLeft );
  }

  /** gets the style attribute margin-left */
  public String getMarginLeft() {
    return ( String )styleManager.find( currentKey, MARGIN_LEFT );
  }

  /** gets the style attribute margin-right */
  public void setMarginRight( final String marginRight ) {
    updateAttribute( MARGIN_RIGHT, marginRight );
  }

  /** gets the style attribute margin-right */
  public String getMarginRight() {
    return ( String )styleManager.find( currentKey, MARGIN_RIGHT );
  }

  /** sets the style attribute margin-top */
  public void setMarginTop( final String marginTop ) {
    updateAttribute( MARGIN_TOP, marginTop );
  }

  /** gets the style attribute margin-top */
  public String getMarginTop() {
    return ( String )styleManager.find( currentKey, MARGIN_TOP );
  }

  /** sets the style attribute margin */
  public void setMargin( final String margin ) {
    updateAttribute( MARGIN, margin );
  }

  /** gets the style attribute margin */
  public String getMargin() {
    return ( String )styleManager.find( currentKey, MARGIN );
  }

  /** sets the style attribute border-width */
  public void setBorderWidth( final String borderWidth ) {
    updateAttribute( BORDER_WIDTH, borderWidth );
  }

  /** gets the style attribute border-width */
  public String getBorderWidth() {
    return ( String )styleManager.find( currentKey, BORDER_WIDTH );
  }

  /** sets the style attribute border-top-width */
  public void setBorderTopWidth( final String borderTopWidth ) {
    updateAttribute( BORDER_TOP_WIDTH, borderTopWidth );
  }

  /** gets the style attribute border-top-width */
  public String getBorderTopWidth() {
    return ( String )styleManager.find( currentKey, BORDER_TOP_WIDTH );
  }

  /** sets the style attribute border-bottom-width */
  public void setBorderBottomWidth( final String borderBottomWidth ) {
    updateAttribute( BORDER_BOTTOM_WIDTH, borderBottomWidth );
  }

  /** gets the style attribute border-bottom-width */
  public String getBorderBottomWidth() {
    return ( String )styleManager.find( currentKey, BORDER_BOTTOM_WIDTH );
  }

  /** sets the style attribute border-left-width */
  public void setBorderLeftWidth( final String borderLeftWidth ) {
    updateAttribute( BORDER_LEFT_WIDTH, borderLeftWidth );
  }

  /** gets the style attribute border-left-width */
  public String getBorderLeftWidth() {
    return ( String )styleManager.find( currentKey, BORDER_LEFT_WIDTH );
  }

  /** set the style attribute border-right-width */
  public void setBorderRightWidth( final String borderRightWidth ) {
    updateAttribute( BORDER_RIGHT_WIDTH, borderRightWidth );
  }
  
  /** gets the style attribute border-right-width */
  public String getBorderRightWidth() {
    return ( String )styleManager.find( currentKey, BORDER_RIGHT_WIDTH );
  }

  /** sets the style attribute border-color */
  public void setBorderColor( final WebColor borderColor ) {
    updateAttribute( BORDER_COLOR, borderColor );
  }

  /** gets the style attribute border-color */
  public WebColor getBorderColor() {
    return ( WebColor )styleManager.find( currentKey, BORDER_COLOR );
  }
  
  /** sets top border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderTopColor( final WebColor borderTopColor ) {
    updateAttribute( BORDER_TOP_COLOR, borderTopColor );
  }
  
  /** sets bottom border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderBottomColor( final WebColor borderBottomColor ) {
    updateAttribute( BORDER_BOTTOM_COLOR, borderBottomColor );
  }

  /** sets left border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderLeftColor( final WebColor borderLeftColor ) {
    updateAttribute( BORDER_LEFT_COLOR, borderLeftColor );
  }

  /** sets right border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public void setBorderRightColor( final WebColor borderRightColor ) {
    updateAttribute( BORDER_RIGHT_COLOR, borderRightColor );
  }

  /** gets bottom border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderBottomColor() {
    return ( WebColor )styleManager.find( currentKey, BORDER_BOTTOM_COLOR );
  }

  /** gets left border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderLeftColor() {
    return ( WebColor )styleManager.find( currentKey, BORDER_LEFT_COLOR );
  }

  /** gets right border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderRightColor() {
    return ( WebColor )styleManager.find( currentKey, BORDER_RIGHT_COLOR );
  }

  /** gets top border color of the css-style attribute border-color.
   *  Note: This only takes effect if all borders attributes are set */
  public WebColor getBorderTopColor() {
    return ( WebColor )styleManager.find( currentKey, BORDER_TOP_COLOR );
  }
  
  /** sets the style attribute border-style */
  public void setBorderStyle( final String borderStyle ) {
    updateAttribute( BORDER_STYLE, borderStyle );
  }

  /** gets the style attribute border-style */
  public String getBorderStyle() {
    return ( String )styleManager.find( currentKey, BORDER_STYLE );
  }

  /** sets the style attribue border-top */
  public void setBorderTop( final String borderTop ) {
    updateAttribute( BORDER_TOP, borderTop );
  }

  /** gets the style attribute border-top */
  public String getBorderTop() {
    return ( String )styleManager.find( currentKey, BORDER_TOP );
  }

  /** sets the style attribute border-bottom */
  public void setBorderBottom( final String borderBottom ) {
    updateAttribute( BORDER_BOTTOM, borderBottom );
  }

  /** gets the style attribute border-bottom */
  public String getBorderBottom() {
    return ( String )styleManager.find( currentKey, BORDER_BOTTOM );
  }

  /** sets the style attribute border-left */
  public void setBorderLeft( final String borderLeft ) {
    updateAttribute( BORDER_LEFT, borderLeft );
  }

  /** gets the style attribute border-left */
  public String getBorderLeft() {
    return ( String )styleManager.find( currentKey, BORDER_LEFT );
  }

  /** sets the style attribute border-right */
  public void setBorderRight( final String borderRight ) {
    updateAttribute( BORDER_RIGHT, borderRight );
  }

  /** gets the style attribute border-right */
  public String getBorderRight() {
    return ( String )styleManager.find( currentKey, BORDER_RIGHT );
  }

  /** sets the style attribute border */
  public void setBorder( final String border ) {
    updateAttribute( BORDER, border );
  }

  /** gets the style attribute border */
  public String getBorder() {
    return ( String )styleManager.find( currentKey, BORDER );
  }

  /** sets the style attribute text-indent */
  public void setTextIndent( final String textIndent ) {
    updateAttribute( TEXT_INDENT, textIndent );
  }

  /** gets the style attribute text-indent */
  public String getTextIndent() {
    return ( String )styleManager.find( currentKey, TEXT_INDENT );
  }

  /** sets the style attribute letter-spacing */
  public void setLetterSpacing( final String letterSpacing ) {
    updateAttribute( LETTER_SPACING, letterSpacing );
  }

  /** gets the style attribute letter-spacing */
  public String getLetterSpacing() {
    return ( String )styleManager.find( currentKey, LETTER_SPACING );
  }

  /** sets the style attribute line-height */
  public void setLineHeight( final String lineHeight ) {
    updateAttribute( LINE_HEIGHT, lineHeight );
  }

  /** gets the style attribute line-height */
  public String getLineHeight() {
    return ( String )styleManager.find( currentKey, LINE_HEIGHT );
  }

  /** sets the style attribute text-align */
  public void setTextAlign( final String textAlign ) {
    updateAttribute( TEXT_ALIGN, textAlign );
  }

  /** gets the style attribute text-align */
  public String getTextAlign() {
    return ( String )styleManager.find( currentKey, TEXT_ALIGN );
  }

  /** sets the style attribute word-spacing */
  public void setWordSpacing( final String wordSpacing ) {
    updateAttribute( WORD_SPACING, wordSpacing );
  }

  /** gets the style attribute word-spacing */
  public String getWordSpacing() {
    return ( String )styleManager.find( currentKey, WORD_SPACING );
  }

  /** sets the style attribute vertical-align */
  public void setVerticalAlign( final String verticalAlign ) {
    updateAttribute( VERTICAL_ALIGN, verticalAlign );
  }

  /** gets the style attribute vertical-align */
  public String getVerticalAlign() {
    return ( String )styleManager.find( currentKey, VERTICAL_ALIGN );
  }

  /** sets the style attribute font-family */
  public void setFontFamily( final String fontFamily ) {
    updateAttribute( FONT_FAMILY, fontFamily );
  }

  /** gets the style attribute font-family */
  public String getFontFamily() {
    return ( String )styleManager.find( currentKey, FONT_FAMILY );
  }

  /** <p>sets the style attribute font-size, in points (pt).</p> */
  public void setFontSize( final int fontSize ) {
    updateAttribute( FONT_SIZE, new Integer( fontSize ) );
  }

  /** <p>gets the style attribute font-size, in points (pt).</p> */
  public int getFontSize() {
    return ( ( Integer )styleManager.find( currentKey, FONT_SIZE ) ).intValue();
  }

  /** sets the style attribute font-style */
  public void setFontStyle( final String fontStyle ) {
    updateAttribute( FONT_STYLE, fontStyle );
  }

  /** gets the style attribute font-style */
  public String getFontStyle() {
    return ( String )styleManager.find( currentKey, FONT_STYLE );
  }

  /** sets the file attribute font-variant */
  public void setFontVariant( final String fontVariant ) {
    updateAttribute( FONT_VARIANT, fontVariant );
  }

  /** gets the style attribute font-variant */
  public String getFontVariant() {
    return ( String )styleManager.find( currentKey, FONT_VARIANT );
  }

  /** sets the style attribute font-weight */
  public void setFontWeight( final String fontWeight ) {
    updateAttribute( FONT_WEIGHT, fontWeight );
  }

  /** gets the style attribute font-weight */
  public String getFontWeight() {
    return ( String )styleManager.find( currentKey, FONT_WEIGHT );
  }

  /** sets the style attribute color */
  public void setColor( final WebColor color ) {
    updateAttribute( COLOR, color );
  }

  /** gets the style attribute color */
  public WebColor getColor() {
    return ( WebColor )styleManager.find( currentKey, COLOR );
  }

  /** sets the style attribute text-decoration */
  public void setTextDecoration( final String textDecoration ) {
    updateAttribute( TEXT_DECORATION, textDecoration );
  }

  /** gets the style attribute text-decoration */
  public String getTextDecoration() {
    return ( String )styleManager.find( currentKey, TEXT_DECORATION );
  }

  /** sets the style attribute text-transform */
  public void setTextTransform( final String textTransform ) {
    updateAttribute( TEXT_TRANSFORM, textTransform );
  }

  /** gets the style attribute text-transform */
  public String getTextTransform() {
    return ( String )styleManager.find( currentKey, TEXT_TRANSFORM );
  }

  /** sets the style attribute background-attachment */
  public void setBgAttachment( final String bgAttachment ) {
    updateAttribute( BG_ATTACHMENT, bgAttachment );
  }

  /** gets the style attribute background-attachment */
  public String getBgAttachment() {
    return ( String )styleManager.find( currentKey, BG_ATTACHMENT );
  }

  /** sets the style attribute background-color */
  public void setBgColor( final WebColor bgColor ) {
    updateAttribute( BG_COLOR, bgColor );
    WebColor color = getBgColor();
    if( !color.equals( bgColor ) ) {
      new Exception().printStackTrace();
    }
  }

  /** gets the style attribute background-color */
  public WebColor getBgColor() {
    return ( WebColor )styleManager.find( currentKey, BG_COLOR );
  }

  /** sets the style attribute background-image */
  public void setBgImage( final String bgImage ) {
    updateAttribute( BG_IMAGE, bgImage );
  }

  /** gets the style attribute background-image*/
  public String getBgImage() {
    return ( String )styleManager.find( currentKey, BG_IMAGE );
  }

  /** sets the style attribute background-position */
  public void setBgPosition( final String bgPosition ) {
    updateAttribute( BG_POSITION, bgPosition );
  }

  /** gets the style attribute background-position */
  public String getBgPosition() {
    return ( String )styleManager.find( currentKey, BG_POSITION );
  }

  /** sets the style attribute background-repeat */
  public void setBgRepeat( final String bgRepeat ) {
    updateAttribute( BG_REPEAT, bgRepeat );
  }
  
  public String getBgRepeat() {
    return ( String )styleManager.find( currentKey, BG_REPEAT );
  }
  
  /** sets the style attribute cursor */
  public void setCursor( final String cursor ) {
    updateAttribute( CURSOR, cursor );
  }

  /** gets the style attribute cursor */
  public String getCursor() {
    return ( String )styleManager.find( currentKey, CURSOR );
  }
  
  /** sets the style attribute display */
  public void setDisplay( final String display ) {
    updateAttribute( DISPLAY, display );
  }
  
  /** gets the style attribute display */
  public String getDisplay() {
    return ( String )styleManager.find( currentKey, DISPLAY );
  }
  
  /** set the white-space style attribute */
  public void setWhiteSpace( final String whiteSpace ) {
    updateAttribute( WHITESPACE, whiteSpace );
  }
  
  /** gets the white-space style attribute */
  public String getWhiteSpace() {
    return ( String )styleManager.find( currentKey, WHITESPACE );
  }
  
  private void updateAttribute( final int attributeIndex, final Object value ) {
    Object oldValue = styleManager.find( currentKey, attributeIndex );
    if( !isEqual( oldValue, value ) ) {
      Integer newKey = styleManager.calculate( currentKey, 
                                               attributeIndex, 
                                               value );
      if( !styleManager.contains( newKey ) ) {
        styleManager.create( currentKey, newKey, attributeIndex, value );
      }
      currentKey = newKey;
    }
  }
  
  private static boolean isEqual( final Object oldValue, final Object newValue ) 
  {
    boolean result = true; 
    if(    oldValue != null 
        && newValue != null 
        && !oldValue.toString().equals( newValue.toString() ) ) 
    {
      result = false;
    } else if( oldValue == null && newValue != null ) {
      result = false;
    } else if( oldValue != null && newValue == null ) {
      result = false;
    }
    return result;
  }

  private static String getColor( final String key ) {
    return DefaultColorScheme.get( key );
  }
}