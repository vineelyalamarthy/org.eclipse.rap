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
package org.eclipse.rap.ui.themeeditor.editor;

import junit.framework.TestCase;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.rules.IToken;

public class ThemePartitionScannerTest extends TestCase {

  public void testCommentToken() throws Exception {
    ThemePartitionScanner scanner = new ThemePartitionScanner();
    String content = "/* foo */";
    scanner.setRange( new Document( content ),
                      0,
                      content.length() );
    IToken token = scanner.nextToken();
    assertEquals( ThemePartitionScanner.CSS_COMMENT, token.getData() );
    
    content = "a/*foo*/";
    scanner.setRange( new Document( content ),
                      0,
                      content.length() );
    
    token = scanner.nextToken();
    assertEquals( null, token.getData() );
    token = scanner.nextToken();
    assertEquals( ThemePartitionScanner.CSS_COMMENT, token.getData() );

  }
}
