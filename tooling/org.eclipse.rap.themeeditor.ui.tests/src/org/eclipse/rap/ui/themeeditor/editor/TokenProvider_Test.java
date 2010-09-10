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

import org.eclipse.rap.ui.themeeditor.scanner.TokenStyleProvider;

import junit.framework.TestCase;

public class TokenProvider_Test extends TestCase {

  public void testCreateToken() {
    TokenStyleProvider provider = new TokenStyleProvider();
    assertNotNull( provider.createToken( TokenStyleProvider.PROPERTY_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.SELECTOR_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.STYLE_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.STATE_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.VARIANT_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.STRING_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.COMMENT_TOKEN ) );
    assertNotNull( provider.createToken( TokenStyleProvider.DEFAULT_TOKEN ) );
  }
}
