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
package com.w4t.dhtml.treeleafkit;

import java.io.IOException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.TreeLeaf;
import com.w4t.dhtml.TreeNodeShift;
import com.w4t.dhtml.renderinfo.TreeLeafInfo;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/** <p>the superclass of all Renderers that render org.eclipse.rap.dhtml.TreeLeaf.</p>
  */
public abstract class TreeLeafRenderer extends Renderer {
  
  private static final String BG_COLOR_BUFFER = "bgColorBuffer";
  private static final String COLOR_BUFFER = "colorBuffer";
  private static final String VERTICAL_ALIGN_BUFFER = "verticalAlignBuffer";

  
  void setBgColorBuffer( final TreeLeaf treeLeaf, 
                         final WebColor bgColorBuffer )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    adapter.addRenderState( BG_COLOR_BUFFER, bgColorBuffer );
  }
  
  WebColor getBgColorBuffer( final TreeLeaf treeLeaf ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    return ( WebColor )adapter.getRenderState( BG_COLOR_BUFFER );
  }
  
  void setColorBuffer( final TreeLeaf treeLeaf, final WebColor colorBuffer ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    adapter.addRenderState( COLOR_BUFFER, colorBuffer );  
  }
  
  WebColor getColorBuffer( final TreeLeaf treeLeaf ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    return ( WebColor )adapter.getRenderState( COLOR_BUFFER );
  }
  
  void setVerticalAlignBuffer( final TreeLeaf treeLeaf, 
                               final String verticalAlignBuffer )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    adapter.addRenderState( VERTICAL_ALIGN_BUFFER, verticalAlignBuffer );      
  }
  
  String getVerticalAlignBuffer( final TreeLeaf treeLeaf) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( treeLeaf );
    return ( String )adapter.getRenderState( VERTICAL_ALIGN_BUFFER );    
  }
  
  IRenderInfoAdapter getRenderInfoAdapter( final TreeLeaf treeLeaf ) {
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )treeLeaf.getAdapter( clazz );
  }
  
  TreeLeafInfo getInfo( final TreeLeaf treeLeaf ) {
    Adaptable adaptable = treeLeaf;
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )adaptable.getAdapter( IRenderInfoAdapter.class );
    return ( TreeLeafInfo )adapter.getInfo();
  }
  
  Style getStyle( final TreeLeaf treeLeaf ) {
    return treeLeaf.getStyle();
  }

  protected void createDivOpen( final String styleContent, 
                                final TreeLeaf treeLeaf ) 
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, treeLeaf.getUniqueID(), null );
    out.writeAttribute( HTML.CLASS, 
                        getResponseWriter().registerCssClass( styleContent ), 
                        null );
  }

  void createUniversalAttributes( final TreeLeaf treeLeaf ) 
    throws IOException 
  {
    HtmlResponseWriter out = getResponseWriter();
    String style = getStyle( treeLeaf ).toString();
    if( !"".equals( style ) ) {
      out.writeAttribute( HTML.CLASS,
                          out.registerCssClass( style ),
                          null );
    }
    out.writeAttribute( HTML.TITLE, getTitle( treeLeaf ), null );
  }
  
  String getLeafIcon( final TreeLeaf treeLeaf ) {
    return getShift( treeLeaf ).getLeafIcon( treeLeaf.getImageSetName() );
  }
  
  String getInner( final TreeLeaf treeLeaf ) {
    return getShift( treeLeaf ).getInner( treeLeaf.getImageSetName() );
  }

  String getLast( final TreeLeaf treeLeaf ) {
    return getShift( treeLeaf ).getLast( treeLeaf.getImageSetName() );
  }
  
  String getLabel( final TreeLeaf treeLeaf ) {
    return RenderUtil.resolve( treeLeaf.getLabel() );
  }
  
  String getTitle( final TreeLeaf treeLeaf ) {
    return RenderUtil.resolve( treeLeaf.getTitle() );
  }
  
  
  // helping methods
  //////////////////
  
  private TreeNodeShift getShift( final TreeLeaf treeLeaf ) {
    return getInfo( treeLeaf ).getTreeNodeShift();
  }

  void prepare( final TreeLeaf treeLeaf ) {
    // check if the current item is marked
    setBgColorBuffer( treeLeaf, getStyle( treeLeaf ).getBgColor() );
    setColorBuffer( treeLeaf, getStyle( treeLeaf ).getColor() );
    if( getInfo( treeLeaf ).isMarked() ) {
      getStyle( treeLeaf ).setColor( treeLeaf.getMarkedColor() );
      getStyle( treeLeaf ).setBgColor( treeLeaf.getMarkedBgColor() );
    }
    setVerticalAlignBuffer( treeLeaf, getStyle( treeLeaf ).getVerticalAlign() );
    getStyle( treeLeaf ).setVerticalAlign( getVerticalAlign() );
    if( !treeLeaf.isEnabled() ) {
      String color 
        = DefaultColorScheme.get( DefaultColorScheme.ITEM_DISABLED_FONT );
      getStyle( treeLeaf ).setColor( new WebColor( color ) );
    }    
  }

  abstract String getVerticalAlign(); 

  void postpare( final TreeLeaf treeLeaf ) {
    getStyle( treeLeaf ).setColor( getColorBuffer( treeLeaf ) );
    getStyle( treeLeaf ).setBgColor( getBgColorBuffer( treeLeaf ) );
    getStyle( treeLeaf ).setVerticalAlign( getVerticalAlignBuffer( treeLeaf ) );
  }
}