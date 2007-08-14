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
import com.w4t.dhtml.MenuButton;
import com.w4t.dhtml.menustyle.*;
import com.w4t.util.image.*;


/** <p>The renderer for org.eclipse.rap.dhtml.MenuButton on browsers without
  * javascript support.</p>
  */
public class MenuButtonRenderer_Default_Noscript extends MenuButtonRenderer {
  
  void createActiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    if( getMenu( menuButton ).isExpanded() ) {
      MenuButtonActiveStyle buttonActiveStyle
        = getMenuBar( menuButton ).getButtonActiveStyle();
      out.startElement( HTML.TD,null );
      out.writeAttribute( HTML.BGCOLOR, 
                          buttonActiveStyle.getBgColor().toString(), 
                          null );
      out.closeElementIfStarted();
      createSubmitter( menuButton );
      out.endElement( HTML.TD );
    } else {
      out.startElement( HTML.TD, null );
      out.closeElementIfStarted();
      createSubmitter( menuButton );
      out.endElement( HTML.TD );
    }    
  }

  void createInactiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TD, null );
    RenderUtil.writeFontOpener( getMBDStyle( menuButton ).getFontFamily(),
                                getMBDStyle( menuButton ).getColor(),
                                getMBDStyle( menuButton ).getFontSize() );
    out.writeText( getLabel( menuButton ), null ); 
    RenderUtil.writeFontCloser();
    out.endElement( HTML.TD );
  }
  
  
  // helping methods
  //////////////////
  
  private void createSubmitter( final MenuButton menuButton ) 
    throws IOException 
  {
    String id = getMenu( menuButton ).getUniqueID();
    HtmlResponseWriter out = getResponseWriter();
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
        out.writeText( getLabel( menuButton ), null );    
        RenderUtil.writeActionSubmitter( id );    
      } else {
        RenderUtil.writeActionSubmitter( imageName, 
                                         id,
                                         "", 
                                         "" );
      }
    } catch( Exception e ) {
      System.out.println( "\nException creating submitter image:\n" + e );
      e.printStackTrace();
      out.writeText( getLabel( menuButton ), null );    
      RenderUtil.writeActionSubmitter( id );
    }
  }

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
  
  private MenuButtonDisabledStyle getMBDStyle( final MenuButton menuButton ) {
    return getMenuBar( menuButton ).getButtonDisabledStyle();
  }  

  private MenuButtonActiveStyle getMBAStyle( final MenuButton menuButton ) {
    return getMenuBar( menuButton ).getButtonActiveStyle();
  }
}