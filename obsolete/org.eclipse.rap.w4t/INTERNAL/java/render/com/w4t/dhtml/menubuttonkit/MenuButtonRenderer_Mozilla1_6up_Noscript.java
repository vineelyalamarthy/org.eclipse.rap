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
package com.w4t.dhtml.menubuttonkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.RenderUtil;
import com.w4t.dhtml.Menu;
import com.w4t.dhtml.MenuButton;
import com.w4t.dhtml.menustyle.*;
import com.w4t.util.image.*;

/** 
 * <p>The renderer for org.eclipse.rap.dhtml.MenuButton on Mozilla 1.6 
 * and higher without javascript support.</p>
 */
public class MenuButtonRenderer_Mozilla1_6up_Noscript extends MenuButtonRenderer {

  void createActiveButton( final MenuButton menuButton ) throws IOException {
    try {
      ImageDescriptor imgDesc = null;
      if( getMenu( menuButton ).isExpanded() ) {
        imgDesc
          = createImageDescriptor( menuButton, getMBAStyle( menuButton ) );
      } else {
        imgDesc
          = createImageDescriptor( menuButton, getMBEStyle( menuButton ) );
      }
      ImageCache cache = ImageCache.getInstance();
      String imageName = cache.getImageName( imgDesc );
      if( cache.isStandardSubmitterImage( imageName ) ) {
        createLabeledDefaultSubmitter( menuButton );
      } else {        
        createActionSubmitter( menuButton, imageName );
      }
    } catch( Exception e ) {
      System.out.println( "\nException creating submitter image:\n" + e );
      e.printStackTrace();
      createLabeledDefaultSubmitter( menuButton );
    }    
  }
  
  void createInactiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, "disabledMenuButton", null );
    out.writeText( getLabel( menuButton ), null );
    out.endElement( HTML.A );
  }
  
  // helping methods
  //////////////////
  
  private ImageDescriptor createImageDescriptor( 
    final MenuButton menuButton,
    final MenuLabeledProperties style )
  {
    return ImageDescriptorFactory.create( getLabel( menuButton ), 
                                          style.getColor(),
                                          style.getBgColor(),                                    
                                          style.getFontFamily(),
                                          style.getFontSize(),
                                          style.getFontWeight(),
                                          style.getFontStyle() );
  }
  
  private MenuButtonEnabledStyle getMBEStyle( final MenuButton menuButton ) {
    return getMenuBar( menuButton ).getButtonEnabledStyle();
  }
  
  private MenuButtonActiveStyle getMBAStyle( final MenuButton menuButton ) {
    return getMenuBar( menuButton ).getButtonActiveStyle();
  }
  
  private void createActionSubmitter( final MenuButton menuButton,
                                      final String imageName ) 
    throws IOException
  {
    Menu menu = getMenu( menuButton );
    RenderUtil.writeActionSubmitter( imageName, 
                                     menu.getUniqueID(),
                                     getLabel( menuButton ), 
                                     getStyleClassName(menuButton) );  
  }
  
  private void createLabeledDefaultSubmitter( final MenuButton menuButton ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, getStyleClassName( menuButton ), null );
    out.writeText( getLabel( menuButton ), null );
    out.writeNBSP();
    RenderUtil.writeActionSubmitter( getMenu( menuButton ).getUniqueID() );
    out.endElement( HTML.A );
  }

  private static String getStyleClassName( final MenuButton menuButton ) {
    String styleClass = "menuButton";
    if( getMenu( menuButton ).isExpanded() ) {
      styleClass = "menuButtonActive";
    }
    return styleClass;
  }
  
}