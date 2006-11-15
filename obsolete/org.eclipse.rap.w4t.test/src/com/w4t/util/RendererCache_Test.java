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
package com.w4t.util;

import junit.framework.TestCase;
import com.w4t.*;
import com.w4t.mockup.NonAjaxComponent;
import com.w4t.mockup.TestComponentWithoutRenderer;
import com.w4t.mockup.nonajaxcomponentkit.NonAjaxComponentRenderer_Default_Script;
import com.w4t.util.browser.Mozilla1_6;
import com.w4t.util.browser.Mozilla1_7up;

/**
 * <p>Unit tests for RendererCache</p>
 */
public class RendererCache_Test extends TestCase {

  public void testNoRenderer() {
    RendererCache cache = RendererCache.getInstance();
    Fixture.fakeBrowser( new Mozilla1_7up( true, true ) );
    // WebComponent
    try {
      cache.retrieveRenderer( WebComponent.class );
      fail();
    } catch( IllegalStateException ise ) {
    }
    // TestComponentWithoutRenderer
    try {
      cache.retrieveRenderer( TestComponentWithoutRenderer.class );
      fail();
    } catch( final IllegalStateException ise ) {
    }
  }

  public void testGetRendererClass() {
    Fixture.fakeBrowser( new Mozilla1_6( true, true ) );
    String name;
    RendererCache rendererCache = RendererCache.getInstance();
    name = rendererCache.getRendererClass( WebComponent.class );
    assertEquals( Renderer.class.getName(), name );
    name = rendererCache.getRendererClass( NonAjaxComponent.class );
    assertEquals( NonAjaxComponentRenderer_Default_Script.class.getName(),
                  name );
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
