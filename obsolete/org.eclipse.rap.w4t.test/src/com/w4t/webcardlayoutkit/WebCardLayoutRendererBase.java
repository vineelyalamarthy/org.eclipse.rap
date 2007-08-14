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
package com.w4t.webcardlayoutkit;

import org.eclipse.rwt.internal.IInitialization;
import org.eclipse.rwt.internal.browser.Browser;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.util.image.ImageCache;

/** <p>Base class for rendering tests of org.eclipse.rap.WebCardLayout.</p>
  */
abstract class WebCardLayoutRendererBase extends RenderingTestCase
{
  private WebPanel wpl;                                                
  private WebCardLayout wcl;

  /** <p>Derived classes must set this field in ctor to have differently
   * generated Render_XXX classes. </p> */
  protected int offset = -1;
  
  /** <p>Must be set to the to-be-tested browser.</p> */
  protected Browser browser;
  
  public WebCardLayoutRendererBase( final String name ) {
    super( name );
  }
  
  final public void runRenderTest() throws Exception {
    prepare();
    for( int i = 0; i < 12; i++ ) {
      wcl.setPosition( i );
      AjaxStatusUtil.preRender( wpl );
      doRenderTest( wpl, i + offset );
    }
  }

  protected void setUp() throws Exception {
    super.setUp();
    ImageCache.createInstance( W4TFixture.getWebAppBase().toString(), 
                               IInitialization.NOSCRIPT_SUBMITTERS_NONE );
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
    W4TFixture.setPrivateField( ImageCache.class, null, "_instance", null );
  }
  
  private void prepare() throws Exception {
    if( offset == -1 ) {
      fail( "Please set 'offset' to a yet unused value." );
    }
    // reset the component counter, so that we have always the same IDs
    resetWebComponentCounter();
    // needed 
    WebForm testForm = new WebForm() {    
      protected void setWebComponents() throws Exception {      
      }
    };
    createControls( testForm );
        
    // needed for renderer loading    
    W4TFixture.fakeBrowser( browser );
  }

  private void createControls( final WebForm testForm ) throws Exception {
    wpl = new WebPanel();
    wcl = new WebCardLayout();
    wpl.setWebLayout( wcl );
    testForm.add( wpl, "CENTER" );
    
    wpl.add( new PopulatedPanel(), "Card1" );
    wpl.add( new PopulatedPanel(), "Card2" );
    wpl.add( new PopulatedPanel(), "Card3" );
    wpl.add( new PopulatedPanel(), "Card4" );
    wcl.setDisplayCard( "Card1" );
  }

  private class PopulatedPanel extends WebPanel {
    PopulatedPanel() throws Exception {
      setWebLayout( new WebGridLayout( 4, 4 ) );
      add( new WebLabel( "Label1" ), new Position( 1, 1 ) );
      add( new WebButton( "Button1" ), new Position( 1, 2 ) );
      add( new WebText(), new Position( 2, 1 ) );
    }
  }
}
//$endOfPublicClass
