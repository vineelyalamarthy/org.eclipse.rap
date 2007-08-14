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

import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.internal.tablecell.BorderedSpacingHelper;
import com.w4t.internal.tablecell.SpacingHelper;
import com.w4t.types.TabConfig;


public class WebCardLayoutRenderer_DOM
  extends WebCardLayoutRenderer
{
  
  private static final Style EMPTY_STYLE = new Style();
    
  WebTableCell createSelectionCell( final WebContainer parent,
                                    final WebButton card )
  {
    WebTableCell result;
    if( isModernType( parent ) ) {
      result =   isDisplayCard( parent, card )
               ? createDisplayCell( parent, card )
               : createNonDisplayCell( parent, card );
      result.addContentElement( card );
    } else {
      result = super.createSelectionCell( parent, card );
    }
    return result;
  }

  void createLeftSpacer( final WebContainer parent,
                         final WebTableRow tableRow )
  {
    if( isModernType( parent ) ) {
      if( getAlign( parent ).equals( TabConfig.ALIGN_RIGHT ) ) {
        tableRow.addCell( createSpacerCell( parent, "100%" ) );
      } else if( getAlign( parent ).equals( TabConfig.ALIGN_CENTER ) ) {
        tableRow.addCell( createSpacerCell( parent, "50%" ) );
      }
    }
  }
  
  void createRightSpacer( final WebContainer parent,
                          final WebTableRow tableRow )
  {
    if( isModernType( parent ) ) {    
      if( getAlign( parent ).equals( TabConfig.ALIGN_LEFT ) ) {
        tableRow.addCell( createSpacerCell( parent, "100%" ) );
      } else if( getAlign( parent ).equals( TabConfig.ALIGN_CENTER ) ) {
        tableRow.addCell( createSpacerCell( parent, "50%" ) );
      }
    }
  }  

  // helping methods
  //////////////////
  
  private WebTableCell createSpacerCell( final WebContainer parent,
                                         final String width )
  {
    WebTableCell result
      = new WebTableCell( createSpacingHelper( parent, width ) );
    result.setSpacing( "0" );
    result.setPadding( "3" );
    result.setStyle( EMPTY_STYLE );
    result.setValue( HTML.NBSP_STRING );
    return result;
  }

  private WebTableCell createDisplayCell( final WebContainer parent,
                                          final WebButton cardButton )
  {
    WebTableCell result = new WebTableCell();
    cardButton.setStyle( getLayout( parent ).getActiveTabLinkStyle() );
    // TODO: disentangle here the use of style for two different cells!!
    result.setBgColor( getLayout( parent ).getCardColor() );
    result.setStyle( cloneStyle( getLayout( parent ).getActiveTabStyle() ) );
    
    applyBorderSettings( parent, result.getStyle() );
    return result;
  }
  
  // FIXME [rh] duplicate? also exists in Ie5up_Script
  private WebTableCell createNonDisplayCell( final WebContainer parent,
                                             final WebButton cardButton )
  {
    SpacingHelper helper
      = new BorderedSpacingHelper( getOppositePosition( parent ), 
                                   getLayout( parent ).getBorderWidth(), 
                                   getLayout( parent ).getBorderColor() );
    WebTableCell result = new WebTableCell( helper );
    Style style = cloneStyle( getLayout( parent ).getInactiveTabLinkStyle() );
    cardButton.setStyle( style );
    result.setBgColor( getLayout( parent ).getInactiveTabColor() );
    result.setStyle( getLayout( parent ).getInactiveTabStyle() );
    result.setSpacing( "0" );
    result.setPadding( "3" );
    return result;
  }
  
  private SpacingHelper createSpacingHelper( final WebContainer parent,
                                             final String width )
  {
    return new BorderedSpacingHelper( getOppositePosition( parent ), 
                                      getLayout( parent ).getBorderWidth(), 
                                      getLayout( parent ).getBorderColor(),
                                      width );
  }
  
  private String getOppositePosition( final WebContainer parent ) {
    String pos = getLayout( parent ).getTabConfig().getTabPosition();
    String result = "";
    if( pos.equals( TabConfig.POSITION_TOP ) ) {
      result = TabConfig.POSITION_BOTTOM;
    } else if( pos.equals( TabConfig.POSITION_LEFT ) ) {
      result = TabConfig.POSITION_RIGHT;
    } else if( pos.equals( TabConfig.POSITION_RIGHT ) ) {
      result = TabConfig.POSITION_LEFT;
    } else if( pos.equals( TabConfig.POSITION_BOTTOM ) ) {
      result = TabConfig.POSITION_TOP;
    }
    return result;
  }

  private static Style cloneStyle( final Style style ) {
    Style result = null;
    try {
      result = ( Style )style.clone();
    } catch( CloneNotSupportedException e ) {
      // ignore
    }
    return result;
  }
}