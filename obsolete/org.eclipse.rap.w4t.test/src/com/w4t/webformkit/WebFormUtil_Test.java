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
package com.w4t.webformkit;

import junit.framework.TestCase;
import com.w4t.Fixture;

public class WebFormUtil_Test extends TestCase {
  
  public void testRenderTitle() {
    StringBuffer buffer = new StringBuffer();
    WebFormUtil.renderTitle( buffer, "hello world" );
    assertEquals( "<title>hello world</title>", buffer.toString() );
    buffer.setLength( 0 );
    WebFormUtil.renderTitle( buffer, "ä" );
    assertEquals( "<title>&auml;</title>", buffer.toString() );
  }
  
  public void testRenderCacheControl() {
    StringBuffer buffer = new StringBuffer();
    WebFormUtil.renderCacheControl( buffer );
    assertEquals( "<meta http-equiv=\"cache-control\" content=\"no-cache\" />", 
                  buffer.toString() );
    
  }
  
  /** <p>Content type in the meta tag is always UTF-8.</p> */
  public void testContentType() {
    StringBuffer buffer = new StringBuffer();
    WebFormUtil.renderContentType( buffer );
    assertEquals(   "<meta http-equiv=\"content-type\" "
                  + "content=\"text/html; charset=UTF-8\" />", 
                  buffer.toString() );
  }
  
  public void testCreateEventHandlerFields() {
    String fields = WebFormUtil.createEventHandlerFields();
    String expected 
      = "<input type=\"hidden\" id=\"webActionEvent\" name=\"webActionEvent\" " 
      + "value=\"not_occured\" />"
      + "<input type=\"hidden\" id=\"webItemEvent\" name=\"webItemEvent\" " 
      + "value=\"not_occured\" />"
      + "<input type=\"hidden\" id=\"webFocusGainedEvent\" " 
      + "name=\"webFocusGainedEvent\" value=\"not_occured\" />"
      + "<input type=\"hidden\" id=\"webTreeNodeExpandedEvent\" " 
      + "name=\"webTreeNodeExpandedEvent\" value=\"not_occured\" />"
      + "<input type=\"hidden\" id=\"webTreeNodeCollapsedEvent\" " 
      + "name=\"webTreeNodeCollapsedEvent\" value=\"not_occured\" />"
      + "<input type=\"hidden\" id=\"changeImage\" name=\"changeImage\" " 
      + "value=\"\" />"
      + "<input type=\"hidden\" id=\"dragSource\" name=\"dragSource\" " 
      + "value=\"\" />"
      + "<input type=\"hidden\" id=\"dragDestination\" " 
      + "name=\"dragDestination\" value=\"\" />"
      + "<input type=\"hidden\" id=\"w4tDoubleClickEvent\" " 
      + "name=\"w4tDoubleClickEvent\" value=\"not_occured\" />";        
    assertEquals( expected, fields );
  }
  
  public void testCreateFocusElement() {
    String markup = WebFormUtil.createFocusElement();
    String expected 
      = "<input type=\"hidden\" id=\"focusElement\" " 
      + "name=\"focusElement\" value=\"\" />";
    assertEquals( expected, markup );
  }
  
  public void testRenderFavIcon() {
    String favIcon = WebFormUtil.renderFavIcon();
    String expected 
      =   "<link rel=\"SHORTCUT ICON\" "
        + "href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" />"
        + "<link rel=\"icon\" href=\"http://fooserver:8080/fooapp/"
        + "resources/images/favicon.ico\" "
        + "type=\"image/ico\" />";
    assertEquals( expected, favIcon );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
