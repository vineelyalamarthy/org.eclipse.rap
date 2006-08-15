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

import java.lang.reflect.Method;
import com.w4t.types.WebColor;
import com.w4t.types.WebTriState;


public class Style_Test extends SelfGeneratingTestCase {

  public Style_Test( final String name ) {
    super( name );
    super.setGenerateResources( false );
  }


  // actual testing code
  //////////////////////
  
  public void testToString() throws Exception {
    Style style = new Style();
    checkStyle( 0, style );
    configureSamples( style );
    checkStyle( 1, style );
    
    Style style2 = new Style();
    configureComplete( style2 );
    checkStyle( 2, style2 );
  }

  private void configureComplete( final Style style ) {
    style.setBgAttachment( "42" );
    style.setBgColor( new WebColor( "42" ) );
    style.setBgImage( "42" );
    style.setBgPosition( "42" );
    style.setBorder( "42" );
    style.setBorderBottom( "42" );
    style.setBorderBottomColor( new WebColor( "42" ) );
    style.setBorderBottomWidth( "42" );
    style.setBorderColor( new WebColor( "42" ) );
    style.setBorderLeft( "42" );
    style.setBorderLeftColor( new WebColor( "42" ) );
    style.setBorderLeftWidth( "42" );
    style.setBorderRight( "42" );
    style.setBorderRightColor( new WebColor( "42" ) );
    style.setBorderRightWidth( "42" );
    style.setBorderStyle( "42"  );
    style.setBorderTop( "42" );
    style.setBorderTopColor( new WebColor( "hjk" ) );
    style.setBorderTopWidth( "42" );
    style.setBorderWidth( "42" );
    style.setBottom( "42" );
    style.setClear( "42" );
    style.setColor( new WebColor( "kj" ) );
    style.setCursor( "42" );
    style.setDisplay( "42" );
    style.setFloat( "42" );
    style.setFontFamily( "42" );
    style.setFontSize( 42 );
    style.setFontStyle( "42" );
    style.setFontVariant( "42" );
    style.setFontWeight( "42" );
    style.setHeight( "42" );
    style.setLeft( "42" );
    style.setLetterSpacing( "42" );
    style.setLineHeight( "42" );
    style.setMargin( "42" );
    style.setMarginBottom( "42" );
    style.setMarginLeft( "42" );
    style.setMarginRight( "42" );
    style.setMarginTop( "42" );
    style.setPadding( "3" );
    style.setPaddingBottom( "42" );
    style.setPaddingLeft( "42" );
    style.setPaddingRight( "42" );
    style.setPaddingTop( "42" );
    style.setPosition( "42" );
    style.setRight( "42" );
    style.setTextAlign( "42" );
    style.setTextDecoration( "42" );
    style.setTextIndent( "42" );
    style.setTextTransform( "42" );
    style.setTop( "42" );
    style.setVerticalAlign( "42" );
    style.setVisibility( new WebTriState( "42" ) );
    style.setWhiteSpace( "42" );
    style.setWidth( "42" );
    style.setWordSpacing( "42" );
    style.setZIndex( "42" );
  }

  private void configureSamples( final Style style ) {
    style.setBgAttachment( "right" );
    style.setBgColor( new WebColor( "green" ) );
    style.setBgPosition( "center" );
    style.setClear( "soup" );
    style.setCursor( "" );
    style.setFontSize( 8 );
    style.setFontWeight( "bold" );
    style.setLetterSpacing( "1.6" );
    style.setPadding( "13" );
    style.setTextAlign( "right" );
    style.setWidth( "499" );
    style.setVisibility( new WebTriState( "false" ) );
    style.setZIndex( "4" );
  }
  
  private void checkStyle( final int id, final Style style ) throws Exception {
    String[] tokens = new String[] { style.toString() };    
    String resourceClassName = "StyleTest_" + String.valueOf( id );

    if( isGenerateResources() ) {
      addResource( resourceClassName, tokens );
    } else {
      String fullName =   this.getClass().getPackage().getName()
                        + "." 
                        + resourceClassName;
      Class resourceClass = Class.forName( fullName );
      Method getRes 
        = resourceClass.getDeclaredMethod( "getRes", new Class[ 0 ] );
      getRes.setAccessible( true );
      String[] tokensRes = ( String[] )getRes.invoke( null, new Object[ 0 ] );
      compare( tokens, tokensRes );
    }    
  }  
}
//$endOfPublicClass
class StyleTest_2 {

  private static String[] res = new String[] {
    "background-attachment:42;background-color:42;background-image:42;background-position:42;font-family:42;font-size:42pt;font-style:42;font-variant:42;font-weight:42;color:kj;text-decoration:42;text-transform:42;text-indent:42;letter-spacing:42;line-height:42;text-align:42;word-spacing:42;vertical-align:42;border-style:42;border:42;border-width:42;border-top-width:42;border-bottom-width:42;border-left-width:42;border-right-width:42;border-color:42;border-color:hjk 42 42 42;border-top:42;border-bottom:42;border-left:42;border-right:42;margin-bottom:42;margin-left:42;margin-right:42;margin-top:42;margin:42;padding:3;padding-bottom:42;padding-left:42;padding-right:42;padding-top:42;bottom:42;clear:42;float:42;height:42;left:42;position:42;right:42;top:42;visibility:42;width:42;z-index:42;cursor:42;display:42;white-space:42;"
  };

  static String[] getRes() {
    return res;
  }
}

class StyleTest_1 {

  private static String[] res = new String[] {
    "background-attachment:right;background-color:#008000;background-position:center;font-family:arial,verdana;font-size:8pt;font-weight:bold;letter-spacing:1.6;text-align:right;padding:13;clear:soup;visibility:false;width:499;z-index:4;"
  };

  static String[] getRes() {
    return res;
  }
}

class StyleTest_0 {

  private static String[] res = new String[] {
    "font-family:arial,verdana;font-size:8pt;"
  };

  static String[] getRes() {
    return res;
  }
}

