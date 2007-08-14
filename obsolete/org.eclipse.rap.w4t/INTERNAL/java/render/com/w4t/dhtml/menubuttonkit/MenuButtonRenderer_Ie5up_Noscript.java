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


/** <p>The renderer for {@link org.eclipse.rwt.dhtml.MenuButton MenuButton} on 
  * Microsoft Internet Explorer 5 and higher without javascript support.</p>
  */
public class MenuButtonRenderer_Ie5up_Noscript extends MenuButtonRenderer {

  private static final String STYLE = "top:2;";
  
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
        writeLabeledDefaultSubmitter( menuButton );
      } else {        
        writeActionSubmitter( menuButton, imageName );
      }
    } catch( Exception e ) {
      System.out.println( "\nException creating submitter image:\n" + e );
      e.printStackTrace();
      writeLabeledDefaultSubmitter( menuButton );
    }    
  }
  
  void createInactiveButton( final MenuButton menuButton ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    String clas = "disabledMenuButton";
    out.startElement( HTML.SPAN, null );
    out.writeAttribute( HTML.STYLE, STYLE, null );
    out.writeAttribute( HTML.CLASS, clas, null );
    out.writeText( getLabel( menuButton ), null );
    out.endElement( HTML.SPAN );    
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
  
  private void writeActionSubmitter( final MenuButton menuButton,
                                     final String imageName ) 
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    out.writeAttribute( HTML.STYLE, STYLE, null );
    out.writeAttribute( HTML.CLASS, getStyleClass( menuButton ), null );
    RenderUtil.writeActionSubmitter( imageName, 
                                     getMenu( menuButton ).getUniqueID(),
                                     getLabel( menuButton ), 
                                     "" );  
    out.endElement( HTML.SPAN );
  }
  
  private void writeLabeledDefaultSubmitter( final MenuButton menuButton ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.SPAN, null );
    out.writeAttribute( HTML.STYLE, STYLE, null );
    out.writeAttribute( HTML.CLASS, getStyleClass( menuButton ), null );
    out.writeText( getLabel( menuButton ), null );
    out.writeNBSP();
    RenderUtil.writeActionSubmitter( getMenu( menuButton ).getUniqueID() );
    out.endElement( HTML.SPAN );
  }  
  
  private String getStyleClass( final MenuButton menuButton ) {
    String result = "menuButton";
    if( getMenu( menuButton ).isExpanded() ) {
      result = "menuButtonActive";
    }
    return result;
  }
}