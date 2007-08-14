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
package com.w4t.dhtml.menuitemkit;

import java.io.IOException;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;

import com.w4t.*;
import com.w4t.dhtml.MenuItem;
import com.w4t.dhtml.menustyle.MenuItemDisabledStyle;
import com.w4t.dhtml.menustyle.MenuItemEnabledStyle;
import com.w4t.util.image.*;

/** <p>The renderer for org.eclipse.rap.dhtml.MenuItem on browsers without
  * javascript support.</p>
  */
public class MenuItemRenderer_Default_Noscript extends Renderer {
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    MenuItem menuItem = ( MenuItem )component;
    if( MenuItemUtil.isActive( menuItem ) ) {
      String label = MenuItemUtil.getLabel( menuItem );
      try {
        String imageName = retrieveImageName( menuItem );
        if( ImageCache.getInstance().isStandardSubmitterImage( imageName ) ) {
          getResponseWriter().writeText( label, null );
          RenderUtil.writeActionSubmitter( menuItem.getUniqueID() );    
        } else {
          RenderUtil.writeActionSubmitter( imageName, 
                                           menuItem.getUniqueID(),
                                           label, 
                                           "" );
        }
      } catch( Exception e ) {
        System.out.println( "\nException creating submitter image:\n" + e );
        e.printStackTrace();
        getResponseWriter().writeText( label, null );
        RenderUtil.writeActionSubmitter( menuItem.getUniqueID() );    
      }
    } else {
      writeDisabledLabelContent( menuItem );
    }
  }
  
  void writeDisabledLabelContent( final MenuItem menuItem ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    MenuItemDisabledStyle style = MenuItemUtil.getMIDStyle( menuItem );
    RenderUtil.writeFontOpener( style.getFontFamily(), 
                                style.getColor(), 
                                style.getFontSize() );
    out.writeText( MenuItemUtil.getLabel( menuItem ), null );
    RenderUtil.writeFontCloser();
  }  

  String retrieveImageName( final MenuItem menuItem ) {
    MenuItemEnabledStyle style = MenuItemUtil.getMIEStyle( menuItem );
    ImageDescriptor imgDesc 
      = ImageDescriptorFactory.create( MenuItemUtil.getLabel( menuItem ), 
                                       style.getColor(),
                                       style.getBgColor(),
                                       style.getFontFamily(),
                                       style.getFontSize(),
                                       style.getFontWeight(),
                                       style.getFontStyle() );
    return ImageCache.getInstance().getImageName( imgDesc );
  }
}