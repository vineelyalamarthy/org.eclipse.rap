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
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.MenuItem;
import com.w4t.dhtml.menustyle.MenuItemEnabledStyle;
import com.w4t.util.image.*;

/** <p>The renderer for {@link org.eclipse.rwt.dhtml.MenuItem MenuItem} on 
  * Microsoft Internet Explorer 5 and higher without javascript support.</p>
  */
public class MenuItemRenderer_Ie5up_Noscript extends MenuItemRenderer {
  
  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processActionPerformedNoScript( component );
  }
  
  void renderActiveItem( final MenuItem menuItem ) throws IOException {
    try {
      String imageName = retrieveImageName( menuItem );        
      if( ImageCache.getInstance().isStandardSubmitterImage( imageName ) ) {
        createLabeledDefaultSubmitter( menuItem );
      } else {
        createActionSubmitter( menuItem, imageName );
      }
    } catch( Exception e ) {
      System.out.println( "\nException creating submitter image:\n" + e );
      createLabeledDefaultSubmitter( menuItem );
    }       
  }
  
  void renderInactiveItem( final MenuItem menuItem ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.TITLE, MenuItemUtil.getTitle( menuItem ), null );
    out.writeAttribute( HTML.CLASS, MenuItemUtil.CSS_CLASS_NAME, null );
    out.writeAttribute( HTML.STYLE, 
                        MenuItemUtil.getMIDStyle( menuItem ).toString(), 
                        null );
    out.writeText( MenuItemUtil.getLabel( menuItem ), null ); 
    out.endElement( HTML.A );    
}
  
  private String retrieveImageName( final MenuItem menuItem ) {
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
  
  private void createLabeledDefaultSubmitter( final MenuItem menuItem ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, "menuItem", null );
    out.writeText( MenuItemUtil.getLabel( menuItem ), null );
    out.writeNBSP();
    RenderUtil.writeActionSubmitter( menuItem.getUniqueID() );
    out.endElement( HTML.A );
  }
  
  private void createActionSubmitter( final MenuItem menuItem,
                                      final String imageName ) 
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.A, null );
    out.writeAttribute( HTML.CLASS, "menuItem", null );
    RenderUtil.writeActionSubmitter( imageName, 
                                     menuItem.getUniqueID(),
                                     MenuItemUtil.getLabel( menuItem ), 
                                     "" ); 
    out.endElement( HTML.A );
  } 
}