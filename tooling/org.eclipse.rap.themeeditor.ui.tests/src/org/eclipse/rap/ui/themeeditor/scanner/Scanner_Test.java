/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor.scanner;

import junit.framework.TestCase;

import org.eclipse.rap.ui.themeeditor.scanner.TokenStyleProvider;
import org.eclipse.rap.ui.themeeditor.scanner.Scanner;
import org.eclipse.rap.ui.themeeditor.scanner.region.AbstractRegion;

public class Scanner_Test extends TestCase {

  private Scanner scanner;

  public void testEmpty() {
    scanner.scanSheet( "" );
    assertEoF();
  }

  public void testComment() {
    scanner.scanSheet( "/*foo*/" );
    assertSelector( "" ); // TODO
    assertComment( "/*foo*/");
    assertEoF();
  }

  public void testAllSelector() {
    scanner.scanSheet( "*{}" );
    assertSelector( "*" );
    assertEoF();
  }

  public void testSelector() {
    scanner.scanSheet( "Button{}" );
    assertSelector( "Button" );
    assertEoF();
  }

  public void testProperty() {
    scanner.scanSheet( "Button{font:abc;}" );
    assertSelector( "Button" );
    assertProperty( "font" );
    assertPropertyValue( "abc" );
    assertProperty( "" ); // TODO
    assertEoF();
  }

  public void testMultiProperty() {
    scanner.scanSheet( "Button{font:abc;background:foo;}" );
    assertSelector( "Button" );
    assertProperty( "font" );
    assertPropertyValue( "abc" );
    assertProperty( "background" );
    assertPropertyValue( "foo" );
    assertProperty( "" ); // TODO
    assertEoF();
  }
  
  public void testMultiRule() {
    scanner.scanSheet( "Button{font:abc;}Widget{}" );
    assertSelector( "Button" );
    assertProperty( "font" );
    assertPropertyValue( "abc" );
    assertProperty( "" ); // TODO
    assertSelector( "Widget" );
    assertProperty( "" ); // TODO
    assertEoF();
  }

  public void testSelectorWithAttribute() {
    scanner.scanSheet( "Button[PUSH]{}" );
    assertSelector( "Button" );
    assertStyle( "PUSH" );
    assertSelector( "" ); // TODO
    assertEoF();
  }

  public void testSelectorWithState() {
    scanner.scanSheet( "Button:disabled{}" );
    assertSelector( "Button" );
    assertState( "disabled" );
    assertEoF();
  }

  public void testMultiSelector() {
    scanner.scanSheet( "Button,Widget{}" );
    assertSelector( "Button" );
    assertSelector( "Widget" );
    assertEoF();
  }
  
  public void testDescendantSelector() {
    scanner.scanSheet( "Shell Widget{}" );
    assertSelector( "Shell" );
    assertSelector( "Widget" );
    assertEoF();
  }
  
  
  public void testVariantSelector() {
    scanner.scanSheet( "Button.clearButton{}" );
    assertSelector( "Button" );
    assertVariant( "clearButton" );
    assertEoF();
  }
  

  public void testVariantSelectorWithState() {
    scanner.scanSheet( "Button.clearButton:pressed{}" );
    assertSelector( "Button" );
    assertVariant( "clearButton" );
    assertState( "pressed" );
    assertEoF();
  }
  
  public void testVariantSelectorWithAttribute() {
    scanner.scanSheet( "Button.clearButton[PUSH]{}" );
    assertSelector( "Button" );
    assertVariant( "clearButton" );
    assertStyle( "PUSH" );
    assertSelector( "" ); // TODO
    assertProperty( "" ); // TODO
    assertEoF();
  }
  
  private void assertVariant( String variant ) {
    assertToken( TokenStyleProvider.VARIANT_TOKEN, variant );
  }

  private void assertState( String state ) {
    assertToken( TokenStyleProvider.STATE_TOKEN, state );
  }

  private void assertToken( int tokenType, String value ) {
    AbstractRegion region = scanner.nextRegion();
    assertEquals( tokenType, region.getTokenType() );
    assertEquals( value, region.getContent() );
  }

  private void assertStyle( String attribute ) {
    assertToken( TokenStyleProvider.STYLE_TOKEN, attribute );
  }

  private void assertPropertyValue( String value ) {
    assertToken( TokenStyleProvider.DEFAULT_TOKEN, value );
  }

  private void assertProperty( String property ) {
    assertToken( TokenStyleProvider.PROPERTY_TOKEN, property );
  }

  private void assertSelector( final String selector ) {
    assertToken( TokenStyleProvider.SELECTOR_TOKEN, selector );
  }

  private void assertComment( String comment ) {
    assertToken( TokenStyleProvider.COMMENT_TOKEN, comment  );
  }

  private void assertEoF() {
    scanner.nextRegion();
    assertNull( scanner.nextRegion() );
  }

  protected void setUp() throws Exception {
    scanner = new Scanner();
  }
}
