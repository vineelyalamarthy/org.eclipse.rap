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
package com.w4t.webborderlayoutkit;

import java.io.IOException;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.developer.AreaSelector;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.renderinfo.BorderLayoutRenderInfo;
import com.w4t.weblayoutkit.LayoutRenderer;


/** <p>the superclass for all renderers that render 
  * {@link org.eclipse.rwt.WebBorderLayout WebBorderLayout}.</p>
  */
public abstract class WebBorderLayoutRenderer extends LayoutRenderer {
  
  private static final String NORTH_COUNT = "northCount";
  private static final String WEST_COUNT = "westCount";
  private static final String CENTER_COUNT = "centerCount";
  private static final String EAST_COUNT = "eastCount";
  private static final String SOUTH_COUNT = "southCount";
  private static final String BORDER_SIZE = "borderSize";
  private static final String DESIGN_TIME = "designTime";
  private static final String NORTH_BUTTON = "northButton";
  private static final String EAST_BUTTON = "eastButton";
  private static final String CENTER_BUTTON = "centerButton";
  private static final String WEST_BUTTON = "westButton";
  private static final String SOUTH_BUTTON = "southButton";
  

  void setNorthCount( final WebContainer parent, final int northCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( NORTH_COUNT, new Integer( northCount ) );  
  }
  
  int getNorthCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( NORTH_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setWestCount( final WebContainer parent, final int westCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( WEST_COUNT, new Integer( westCount ) );  
  }
  
  int getWestCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( WEST_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setCenterCount( final WebContainer parent, final int centerCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( CENTER_COUNT, new Integer( centerCount ) );  
  }
  
  int getCenterCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( CENTER_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setEastCount( final WebContainer parent, final int eastCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( EAST_COUNT, new Integer( eastCount ) );  
  }
  
  int getEastCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( EAST_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setSouthCount( final WebContainer parent, final int southCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( SOUTH_COUNT, new Integer( southCount ) );  
  }
  
  int getSouthCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( SOUTH_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setBorderSize( final WebContainer parent, final String borderSize ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( BORDER_SIZE, borderSize );      
  }
  
  String getBorderSize( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( String )adapter.getRenderState( BORDER_SIZE );    
  }
  
  void setDesignTime( final WebContainer parent, final boolean designTime ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( DESIGN_TIME, Boolean.valueOf( designTime ) );  
  }
  
  boolean isDesignTime( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( DESIGN_TIME );
    boolean result = false;
    if( renderState != null ) {
      result = ( ( Boolean )renderState ).booleanValue();
    }
    return result;
  }
  
  void setNorthButton( final WebContainer parent,
                       final AreaSelector northButton )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( NORTH_BUTTON, northButton );
  }
  
  AreaSelector getNorthButton( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( NORTH_BUTTON );
  }
  
  void setEastButton( final WebContainer parent,
                       final AreaSelector eastButton )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( EAST_BUTTON, eastButton );
  }
  
  AreaSelector getEastButton( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( EAST_BUTTON );
  }
  
  void setCenterButton( final WebContainer parent,
                        final AreaSelector centerButton )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( CENTER_BUTTON, centerButton );
  }
  
  AreaSelector getCenterButton( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( CENTER_BUTTON );
  }
  
  void setWestButton( final WebContainer parent,
                      final AreaSelector westButton )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( WEST_BUTTON, westButton );
  }
  
  AreaSelector getWestButton( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( WEST_BUTTON );
  }
  
  void setSouthButton( final WebContainer parent,
                       final AreaSelector southButton )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( SOUTH_BUTTON, southButton );
  }
  
  AreaSelector getSouthButton( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( SOUTH_BUTTON );
  }
  
  IRenderInfoAdapter getRenderInfoAdapter( final WebContainer parent ) {
    Adaptable adaptable = getLayout( parent );
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )adaptable.getAdapter( clazz );
  }
  
  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    BorderLayoutRenderInfo info = getInfo( parent );
    try {
      setDesignTime( parent, parent.isDesignTime() );
      checkBorderSizeInDesignTime( parent );

      for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
        String region = ( String )parent.getConstraint( i );
        if( region.equalsIgnoreCase( "NORTH" ) ) {
          info.getNorthCell().addContentElement( parent.get( i ) );
          setNorthCount( parent, getNorthCount( parent ) + 1 );
        } else if( region.equalsIgnoreCase( "WEST" ) ) {
          info.getWestCell().addContentElement( parent.get( i ) );
          setWestCount( parent, getWestCount( parent ) + 1 );
        } else if( region.equalsIgnoreCase( "CENTER" ) ) {
          info.getCenterCell().addContentElement( parent.get( i ) );
          setCenterCount( parent, getCenterCount( parent ) + 1 );
        } else if( region.equalsIgnoreCase( "EAST" ) ) {
          info.getEastCell().addContentElement( parent.get( i ) );
          setEastCount( parent, getEastCount( parent ) + 1 );
        } else if( region.equalsIgnoreCase( "SOUTH" ) ) {
          info.getSouthCell().addContentElement( parent.get( i ) );
          setSouthCount( parent, getSouthCount( parent ) + 1 );
        } else {
            String msg =   "Not a valid constraint: \n "
                         + " '" 
                         + region
                         + "'.";
            throw new IllegalStateException(  msg );
        }
      }

      initDesignButtons( parent );
      addDesignButtons( parent );

      // build the html Table
      HtmlResponseWriter out = getResponseWriter();
      out.startElement( HTML.TABLE, null );
      out.writeAttribute( HTML.ID, parent.getUniqueID(), null );
      RenderUtil.writeTableAttributes( getLayout( parent ) );
      RenderUtil.writeUniversalAttributes( parent );
      if( getNorthCount( parent ) != 0 ) {
        info.getTopRow().render();
      }
      
      if(    getEastCount( parent ) != 0 
          && getCenterCount( parent ) != 0 
          && getWestCount( parent ) != 0 )
      {
        info.getMiddleRow().render();
      } else if(    getCenterCount( parent ) != 0
                 && getEastCount( parent ) != 0 )
      {
        info.getMiddleRow().removeCell( 0 );
        info.getCenterCell().setColspan( "2" );
        info.getMiddleRow().render();
        info.getCenterCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getWestCell(), 0 );
      } else if(    getWestCount( parent ) != 0 
                 && getEastCount( parent ) != 0 )
      {
        info.getMiddleRow().removeCell( 1 );
        info.getWestCell().setColspan( "2" );
        info.getMiddleRow().render();
        info.getWestCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getCenterCell(), 1 );
      } else if(    getWestCount( parent ) != 0
                 && getCenterCount( parent ) != 0 )
      {
        info.getMiddleRow().removeCell( 2 );
        info.getCenterCell().setColspan( "2" );
        info.getMiddleRow().render();
        info.getCenterCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getEastCell(), 2 );
      } else if( getWestCount( parent ) != 0 ) {
        info.getMiddleRow().removeCell( 1 );
        info.getMiddleRow().removeCell( 1 );
        info.getWestCell().setColspan( "3" );
        info.getMiddleRow().render();
        info.getWestCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getCenterCell(), 1 );
        info.getMiddleRow().addCell( info.getEastCell(), 2 );
      } else if( getCenterCount( parent ) != 0 ) {
        info.getMiddleRow().removeCell( 0 );
        info.getMiddleRow().removeCell( 1 );
        info.getCenterCell().setColspan( "3" );
        info.getMiddleRow().render();
        info.getCenterCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getWestCell(), 0 );
        info.getMiddleRow().addCell( info.getEastCell(), 2 );
      } else if( getEastCount( parent ) != 0 ) {
        info.getMiddleRow().removeCell( 0 );
        info.getMiddleRow().removeCell( 0 );
        info.getEastCell().setColspan( "3" );
        info.getMiddleRow().render();
        info.getEastCell().setColspan( "" );
        info.getMiddleRow().addCell( info.getCenterCell(), 0 );
        info.getMiddleRow().addCell( info.getWestCell(), 0 );
      }

      if( getSouthCount( parent ) != 0 ) {
        info.getBottomRow().render();
      }
      out.endElement( HTML.TABLE );
    } finally {
      // after the layout remove the components
      removeDesignButtons( parent );
      for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
        String region = ( String )parent.getConstraint( i );
        if( region.equalsIgnoreCase( "NORTH" ) ) {
          info.getNorthCell().removeContentElement( parent.get( i ) );
        } else if( region.equalsIgnoreCase( "WEST" ) ) {
          info.getWestCell().removeContentElement( parent.get( i ) );
        } else if( region.equalsIgnoreCase( "CENTER" ) ) {
          info.getCenterCell().removeContentElement( parent.get( i ) );
        } else if( region.equalsIgnoreCase( "EAST" ) ) {
          info.getEastCell().removeContentElement( parent.get( i ) );
        } else if( region.equalsIgnoreCase( "SOUTH" ) ) {
          info.getSouthCell().removeContentElement( parent.get( i ) );
        } else {
          info.getCenterCell().removeContentElement( parent.get( i ) );
        }
      }
      restoreBorderBuffer( parent );
      setNorthCount( parent, 0 );
      setWestCount( parent, 0 );
      setCenterCount( parent, 0 );
      setEastCount( parent, 0 );
      setSouthCount( parent, 0 );      
    }
  }
  
  BorderLayoutRenderInfo getInfo( final WebContainer parent ) {
    return ( BorderLayoutRenderInfo )getRenderInfoAdapter( parent ).getInfo();
  }

  WebBorderLayout getLayout( final WebContainer parent ) {
    return ( WebBorderLayout )parent.getWebLayout();
  }

  private void checkBorderSizeInDesignTime( final WebContainer parent ) {
    // buffer border size
    setBorderSize( parent, getLayout( parent ).getBorder() );
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
  
  private void restoreBorderBuffer( final WebContainer parent ) {
    getLayout( parent ).setBorder( getBorderSize( parent ) );
  }

  private void addDesignButtons( final WebContainer parent ) {
    if( isDesignTime( parent ) ) {
      parent.add( getNorthButton( parent ), "NORTH" );
      BorderLayoutRenderInfo info = getInfo( parent );
      info.getNorthCell().addContentElement( getNorthButton( parent ) );
      getNorthButton( parent ).setContainer( parent );
      setNorthCount( parent, getNorthCount( parent ) + 1 );

      parent.add( getEastButton( parent ), "EAST" );
      info.getEastCell().addContentElement( getEastButton( parent ) );
      getEastButton( parent ).setContainer( parent );
      setEastCount( parent, getEastCount( parent ) + 1 );

      parent.add( getCenterButton( parent ), "CENTER" );
      info.getCenterCell().addContentElement( getCenterButton( parent ) );
      getCenterButton( parent ).setContainer( parent );
      setCenterCount( parent, getCenterCount( parent ) + 1 );

      parent.add( getWestButton( parent ), "WEST" );
      info.getWestCell().addContentElement( getWestButton( parent ) );
      getWestButton( parent ).setContainer( parent );
      setWestCount( parent, getWestCount( parent ) + 1 );

      parent.add( getSouthButton( parent ), "SOUTH" );
      info.getSouthCell().addContentElement( getSouthButton( parent ) );
      getSouthButton( parent ).setContainer( parent );
      setSouthCount( parent, getSouthCount( parent ) + 1 );
    }
  }

  private void removeDesignButtons( final WebContainer parent ) {
    if( isDesignTime( parent ) ) {
      parent.remove( getNorthButton( parent ) );
      BorderLayoutRenderInfo info = getInfo( parent );
      info.getNorthCell().removeContentElement( getNorthButton( parent ) );
      setNorthCount( parent, getNorthCount( parent ) - 1 );

      parent.remove( getEastButton( parent ) );
      info.getEastCell().removeContentElement( getEastButton( parent ) );
      setEastCount( parent, getEastCount( parent ) - 1 );

      parent.remove( getCenterButton( parent ) );
      info.getCenterCell().removeContentElement( getCenterButton( parent ) );
      setCenterCount( parent, getCenterCount( parent ) - 1 );

      parent.remove( getWestButton( parent ) );
      info.getWestCell().removeContentElement( getWestButton( parent ) );
      setWestCount( parent, getWestCount( parent ) - 1 );

      parent.remove( getSouthButton( parent ) );
      info.getSouthCell().removeContentElement( getSouthButton( parent ) );
      setSouthCount( parent, getSouthCount( parent ) - 1 );
    }
  }
  
  private void initDesignButtons( final WebContainer parent ) {
    if( isDesignTime( parent ) ) {
      setNorthButton( parent, new AreaSelector() );
      setEastButton( parent, new AreaSelector() );
      setCenterButton( parent, new AreaSelector() );
      setWestButton( parent, new AreaSelector() );
      setSouthButton( parent, new AreaSelector() );

      getNorthButton( parent ).setTitle( "NORTH" );
      getEastButton( parent ).setTitle( "EAST" );
      getCenterButton( parent ).setTitle( "CENTER" );
      getWestButton( parent ).setTitle( "WEST" );
      getSouthButton( parent ).setTitle( "SOUTH" );

      BorderLayoutRenderInfo info = getInfo( parent );
      getNorthButton( parent ).setRegion( info.getNorthCell() );
      getEastButton( parent ).setRegion( info.getEastCell() );
      getCenterButton( parent ).setRegion( info.getCenterCell() );
      getWestButton( parent ).setRegion( info.getWestCell() );
      getSouthButton( parent ).setRegion( info.getSouthCell() );

      getNorthButton( parent ).setConstraint( "NORTH" );
      getEastButton( parent ).setConstraint( "EAST" );
      getCenterButton( parent ).setConstraint( "CENTER" );
      getWestButton( parent ).setConstraint( "WEST" );
      getSouthButton( parent ).setConstraint( "SOUTH" );      
    }
  }
}