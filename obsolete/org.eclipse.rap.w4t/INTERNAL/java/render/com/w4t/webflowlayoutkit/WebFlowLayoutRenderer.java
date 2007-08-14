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
package com.w4t.webflowlayoutkit;

import java.io.IOException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.developer.AreaSelector;
import com.w4t.developer.PanelCreator;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.renderinfo.FlowLayoutRenderInfo;
import com.w4t.weblayoutkit.LayoutRenderer;

/** <p>the superclass for all renderers that render org.eclipse.rap.WebFlowLayout.</p>
  *
  * @author Leif Frenzel
  */
public abstract class WebFlowLayoutRenderer extends LayoutRenderer {
    
  private static final String BORDER_SIZE = "borderSize";
  private static final String AREA_SELECOR = "arealSelector";
  private static final String USE_CELL_ALIGNMENTS = "useCellAlignments";

  
  void setUseSurroundCellAlignments( final WebContainer parent,
                                     final boolean useSurroundCellAlignments )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( USE_CELL_ALIGNMENTS, 
                            Boolean.valueOf( useSurroundCellAlignments ) );  
  }
  
  boolean isUseSurroundCellAlignments( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( USE_CELL_ALIGNMENTS );
    boolean result = false;
    if( renderState != null ) {
      result = ( ( Boolean )renderState ).booleanValue();
    }
    return result;
  }
  
  void setAreaSelector( final WebContainer parent,
                        final AreaSelector areaSelector )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( AREA_SELECOR, areaSelector );
  }
  
  AreaSelector getAreaSelector( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( AREA_SELECOR );
  }
  
  void setBorderSize( final WebContainer parent, final String borderSize ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( BORDER_SIZE, borderSize );      
  }
  
  String getBorderSize( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( String )adapter.getRenderState( BORDER_SIZE );    
  }
  
  IRenderInfoAdapter getRenderInfoAdapter( final WebContainer parent ) {
    Adaptable adaptable = getLayout( parent );
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )adaptable.getAdapter( clazz );
  }

  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    getRenderInfoAdapter( parent ).clearRenderState();
    if( isDesignTime( parent ) || needTable( parent ) ) {
      checkBorderSizeInDesignTime( parent );
      try {
        prepare( parent );
        buildTable( parent );
      } finally {
        postPare( parent );
      }
    } else {
      renderWithoutTable( parent );
    }
  }

  void renderWithoutTable( final WebContainer parent )
    throws IOException
  {
    for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
      LifeCycleHelper.render( parent.get( i ) );
    }
  }

  private boolean needTable( final WebContainer parent ) {
    WebTableCell cell = getCell( parent );
    WebFlowLayout layout = getLayout( parent );
    return    RenderUtil.hasUniversalAttributes( cell )
           ||  cell.isNowrap()
           || !cell.getAlign().equals( "" )
           || !cell.getBackground().equals( "" )
           || !cell.getBgColor().toString().equals( "" )
           || !cell.getHeight().toString().equals( "" )
           || !cell.getPadding().equals( "" )
           || !cell.getSpacing().equals( "" )
           || !cell.getVAlign().equals( "" )
           || !cell.getWidth().equals( "" )
           || !layout.getAlign().equals( "" )
           || !layout.getBgColor().toString().equals( "" )
           || (    !layout.getBorder().equals( "0" ) 
                && !isDesignTime( parent ) )
           || !layout.getCellpadding().equals( "0" )
           || !layout.getCellspacing().equals( "0" )
           || !layout.getHeight().equals( "" )
           || !layout.getWidth().equals( "100%" )
           || RenderUtil.hasUniversalAttributes( parent );
  }


  private void prepare( final WebContainer parent ) {
    if( parent.isDesignTime() ) {
      AreaSelector areaSelector = new AreaSelector();
      setAreaSelector( parent, areaSelector );    
    }
    addComponentsToCell( parent );
    if( parent.isDesignTime() ) {
      addDesignSpecifics( parent );
    }
  }
  
  private void buildTable( final WebContainer parent ) throws IOException {
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.TABLE, null );
    RenderUtil.writeUniversalAttributes( parent );
    RenderUtil.writeTableAttributes( getLayout( parent ) );
    out.writeAttribute( HTML.ID, parent.getUniqueID(), null );
    getLayout( parent ).getTableRow().render();
    out.endElement( HTML.TABLE );
  }

  private void postPare( final WebContainer parent ) {
    if( parent.isDesignTime() ) {
      removeDesignSpecifics( parent );
    }
    removeAllComponentsFromCell( parent );
    restoreBorderSize( parent );
  }
  
  private void checkBorderSizeInDesignTime( final WebContainer parent ) {
    bufferBorderSize( parent );
    boolean useBorderSize_1 = false;
    if( isDesignTime( parent ) ) {
      useBorderSize_1 = true;
      try {
        if( Integer.parseInt( getLayout( parent ).getBorder() ) > 1 ) {
          useBorderSize_1 = false;
        }
      } catch( Exception e ) {
      }
    }
    if( useBorderSize_1 ) {
      getLayout( parent ).setBorder( "1" );
    }
  }

  WebFlowLayout getLayout( final WebContainer parent ) {
    return ( WebFlowLayout )parent.getWebLayout();
  }

  FlowLayoutRenderInfo getInfo( final WebContainer parent ) {
    Adaptable adaptable = getLayout( parent );
    IRenderInfoAdapter adapter 
      = ( IRenderInfoAdapter )adaptable.getAdapter( IRenderInfoAdapter.class );
    return ( FlowLayoutRenderInfo )adapter.getInfo();
  }
  
  
  // helping methods
  //////////////////
  
  private WebTableCell getCell( final WebContainer parent ) {
    return ( WebTableCell )getLayout( parent ).getArea();
  }

  private void addComponentsToCell( final WebContainer parent ) {
    for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
      getCell( parent ).addContentElement( parent.get( i ) );
    }
  }
  
  private void removeAllComponentsFromCell( final WebContainer parent ) {
    for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
      getCell( parent ).removeContentElement( parent.get( i ) );
    }
  }

  private boolean isDesignTime( final WebContainer parent ) {
    return parent.isDesignTime();
  }
  
  private boolean isDesignSpecific( final WebContainer parent ) {
    return     isDesignTime( parent )
            && !( parent instanceof PanelCreator );
  }
  
  private void addDesignSpecifics( final WebContainer parent ) {
    if( isDesignSpecific( parent ) ) {
      if( !needTable( parent ) && parent.getParent() != null ) {              
        WebContainer container = parent.getParent();
        Object constraint = container.getConstraint( parent );
        Area area = container.getWebLayout().getArea( constraint );
        if( area != null ) {
          getCell( parent ).setAlign( area.getAlign() );
          getCell( parent ).setVAlign( area.getVAlign() );
          setUseSurroundCellAlignments( parent, true );
        }
      }      
      AreaSelector areaSelector = getAreaSelector( parent );
      getCell( parent ).addContentElement( areaSelector );
      areaSelector.setContainer( parent );
      areaSelector.setRegion( getCell( parent ) );
      parent.add( areaSelector  );
    }
  }
  
  private void removeDesignSpecifics( final WebContainer parent ) {  
    if( isDesignSpecific( parent ) ) {
      AreaSelector areaSelector = getAreaSelector( parent );
      getCell( parent ).removeContentElement( areaSelector );
      parent.remove( areaSelector );
      if( isUseSurroundCellAlignments( parent ) ) {      
        getCell( parent ).setAlign( "" );
        getCell( parent ).setVAlign( "" );
        setUseSurroundCellAlignments( parent, false );
      }            
    }
  }
  
  private void bufferBorderSize( final WebContainer parent ) {
    setBorderSize( parent, getLayout( parent ).getBorder() );
  }
  
  private void restoreBorderSize( final WebContainer parent ) {
    getLayout( parent ).setBorder( getBorderSize( parent ) );
  }     
}