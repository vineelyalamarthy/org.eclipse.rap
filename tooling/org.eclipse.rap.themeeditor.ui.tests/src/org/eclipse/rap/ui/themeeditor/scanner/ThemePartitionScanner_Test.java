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

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import junit.framework.TestCase;

public class ThemePartitionScanner_Test extends TestCase {

  public void testSetRange() throws Exception {
    ThemePartitionScanner partitionScanner = new ThemePartitionScanner();
    String css = "Button[PUSH]{font:abc;}";
    Document document = new Document( css );
    partitionScanner.setRange( document, 0, css.length() );
    assertNotNull( partitionScanner.getScanner() );
  }
  
  public void testSetPartialRange() throws Exception {
    ThemePartitionScanner partitionScanner = new ThemePartitionScanner();
    String css = "Button[PUSH]{font:abc;}";
    Document document = new Document( css );
    partitionScanner.setPartialRange( document, 0, css.length(), null, 0 );
    assertNotNull( partitionScanner.getScanner() );
  }

  public void testTokenOffsets() throws Exception {
    ThemePartitionScanner partitionScanner = new ThemePartitionScanner();
    String css = "Button[PUSH]{font:abc;}";
    Document document = new Document( css );
    partitionScanner.setRange( document, 0, css.length() );
    partitionScanner.nextToken();
    assertEquals( "Button".length(), partitionScanner.getTokenLength() );
  }
  
  public void testTokenEoF() throws Exception {
    ThemePartitionScanner partitionScanner = new ThemePartitionScanner();
    String css = "Button{}";
    Document document = new Document( css );
    partitionScanner.setRange( document, 0, css.length() );
    partitionScanner.nextToken();
    partitionScanner.nextToken();
    assertEquals( Token.EOF, partitionScanner.nextToken() );
  }
  
  public void testTokenOffset() throws Exception {
    ThemePartitionScanner partitionScanner = new ThemePartitionScanner();
    String css = "Button{}Widget{}";
    Document document = new Document( css );
    partitionScanner.setRange( document, 0, css.length() );
    partitionScanner.nextToken();
    assertEquals( 0, partitionScanner.getTokenOffset() );
    partitionScanner.nextToken();
    partitionScanner.nextToken();
    assertEquals( 8, partitionScanner.getTokenOffset() );
  }
}
