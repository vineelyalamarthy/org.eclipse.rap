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
package com.w4t.webimagekit;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.browser.Default;
import org.eclipse.rwt.internal.browser.Ie5up;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.W4TFixture;
import com.w4t.WebImage;
import com.w4t.ajax.AjaxStatus;

/**
 * <p>
 * Unit tests for WebImageRenderer.
 * </p>
 */
public class WebImageRenderer_Test extends TestCase {

  public void testAjaxRenderer() throws Exception {
    WebImage image = new WebImage();
    image.setValue( "my-image.jpg" );
    W4TFixture.setWebComponentUniqueId( image, "page1" );
    AjaxStatus ajaxStatus = ( AjaxStatus )image.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Ie5up( true, true ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    String markup;
    markup = getBodyMarkup( writer );
    String expected;
    expected = "<img id=\"page1\" "
             + "src=\"my-image.jpg\" alt=\"\" border=\"0\" />";
    assertEquals( expected, markup );
    
    // add more attributes
    writer = new HtmlResponseWriter();
    setResponseWriter (writer);
    image.setWidth( "100" );
    image.setHeight( "100" );
    image.setAlt( "alt_text" );
    image.setBorder( "5" );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup( writer );
    expected =   "<img id=\"page1\" " 
               + "src=\"my-image.jpg\" alt=\"alt_text\" " 
               + "border=\"5\" width=\"100\" height=\"100\" />";
    assertEquals( expected, markup );
    
    // make disabled
    image.setEnabled( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup( writer );
    expected = "<img id=\"page1\" src=\"disabledmy-image.jpg\" " 
             + "alt=\"alt_text\" " 
             + "border=\"5\" width=\"100\" height=\"100\" />";
    assertEquals( expected, markup );    
    
    // TODO: [fappel] move this to a special WebImageRenderer in wdbc project
    // showTextField
    // image.setShowTextField( true );
    // writer = new HtmlResponseWriter();
    // setResponseWriter( writer );
    // W4TFixture.renderComponent( image );
    // markup = getBodyMarkup( writer );
    // expected = "<img id=\"page1_label1_img\" src=\"my-image.jpg\" alt=\"\"
    // border=\"0\" />"
    // + "<input type=\"text\" id=\"page1_label1_txt\" name=\"page1_label1\" "
    // + "value=\"my-image.jpg\" "
    // + "size=\"10\" />";
    // assertEquals( expected, markup );
    // // data-bound
    // image.setWebDataSource( new WebDataBaseTable() );
    // image.setDataField( new WebDataField( "field1" ) );
    // writer = new HtmlResponseWriter();
    // setResponseWriter( writer );
    // W4TFixture.renderComponent( image );
    // markup = getBodyMarkup( writer );
    // expected = "<img id=\"page1_label1_img\" src=\"my-image.jpg\" alt=\"\"
    // border=\"0\" />"
    // + "<a id=\"page1_label1_a\" "
    // + "href=\"javascript:eventHandler."
    // + "changeImagePath('page1_label1_a')\">"
    // + "<img id=\"page1_label1_img\" src=\"my-image.jpg\" alt=\"\"
    // border=\"0\" /></a>";
    // assertEquals( expected, markup );
  }

  public void testScriptRenderer() throws Exception {
    WebImage image = new WebImage();
    image.setValue( "my-image.jpg" );
    W4TFixture.setWebComponentUniqueId( image, "page1" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Ie5up( true, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    String markup = getBodyMarkup( writer );
    String expected;
    expected =   "<img id=\"page1\" " 
               + "src=\"my-image.jpg\" alt=\"\" border=\"0\" />";
    assertEquals( expected, markup );
    
    // add more attributes
    writer = new HtmlResponseWriter();
    setResponseWriter (writer);
    image.setWidth( "100" );
    image.setHeight( "100" );
    image.setAlt( "alt_text" );
    image.setBorder( "5" );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup( writer );
    expected =   "<img id=\"page1\" " 
               + "src=\"my-image.jpg\" alt=\"alt_text\" " 
               + "border=\"5\" height=\"100\" width=\"100\" />";
    assertEquals( expected, markup );

    // make disabled
    image.setEnabled( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup ( writer );
    expected =   "<img id=\"page1\" " 
               + "src=\"disabledmy-image.jpg\" alt=\"alt_text\" " 
               + "border=\"5\" height=\"100\" width=\"100\" />";
    assertEquals( expected, markup );
    
// TODO: [fappel] move this to a special WebImageRenderer in wdbc project
    // showTextField
// image.setShowTextField( true );
// writer = new HtmlResponseWriter();
// setResponseWriter( writer );
// W4TFixture.renderComponent( image );
// markup = getBodyMarkup( writer );
// expected = "<img src=\"my-image.jpg\" alt=\"\" border=\"0\" />"
// + "<input type=\"text\" name=\"page1_label1\" "
// + "value=\"my-image.jpg\" "
// + "size=\"10\" />";
// assertEquals( expected, markup );
  }  

  public void testNoScriptRenderer() throws Exception {
    WebImage image = new WebImage();
    image.setValue( "my-image.jpg" );
    HtmlResponseWriter writer = new HtmlResponseWriter();
    W4TFixture.fakeBrowser( new Default( false, false ) );
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    String markup = getBodyMarkup( writer );
    String expected;
    expected = "<img src=\"my-image.jpg\" alt=\"\" border=\"0\">";
    assertEquals( expected, markup );

    // add more attributes
    image.setHeight( "20" );
    image.setWidth( "30" );
    image.setAlt( "alt_text" );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup( writer );
    expected = "<img src=\"my-image.jpg\" alt=\"alt_text\" border=\"0\" "
             + "height=\"20\" width=\"30\">";
    assertEquals( expected, markup );
    
    // make disabled
    image.setEnabled( false );
    writer = new HtmlResponseWriter();
    setResponseWriter( writer );
    W4TFixture.renderComponent( image );
    markup = getBodyMarkup( writer );
    expected = "<img src=\"disabledmy-image.jpg\" alt=\"alt_text\" " 
             + "border=\"0\" height=\"20\" width=\"30\">";
    assertEquals( expected, markup );
  }

  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }

  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }

  private void setResponseWriter( final HtmlResponseWriter writer ) {
    ContextProvider.getStateInfo().setResponseWriter( writer );
  }

  private String getBodyMarkup( final HtmlResponseWriter writer ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < writer.getBodySize(); i++ ) {
      buffer.append( writer.getBodyToken( i ) );
    }
    return buffer.toString();
  }
}
