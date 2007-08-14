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

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.developer.AreaSelector;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.renderinfo.CardLayoutRenderInfo;
import com.w4t.types.TabConfig;
import com.w4t.weblayoutkit.LayoutRenderer;


/** <p>the superclass for all renderers that render 
  * {@link org.eclipse.rwt.WebCardLayout WebCardLayout}.</p>
  */
public abstract class WebCardLayoutRenderer extends LayoutRenderer {
  
  private static final String CARD_COUNT = "cardCount";
  private static final String CARD_LIST = "cardList";
  private static final String BORDER_SIZE = "borderSize";
  private static final String DESIGN_CARD = "designCard";
  
  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    getRenderInfoAdapter( parent ).clearRenderState();
    getLayout( parent ).setEnabled( parent.isEnabled() );
    checkBorderSizeInDesignTime( parent );
    // fill the cardList with the actual cards listed in 
    // the parent Webcontainer
    fillCardList( parent );
    // get the card to display
    WebComponent card = findCard( parent );
  
    WebTableCell contentCell = new WebTableCell();
    contentCell.setAlign( HTML.CENTER );
    contentCell.setVAlign( HTML.TOP );
    contentCell.setBgColor( getLayout( parent ).getCardColor() );
    contentCell.setStyle( getLayout( parent ).getCardStyle() );
  
    if( card != null ) {
      contentCell.addContentElement( card );
    }
  
    WebTableCell tabsCell = new WebTableCell();
    tabsCell.setValueEncoded( true );
    WebTableRow tableRow = new WebTableRow();
    createTableOpener( parent );
  
    HtmlResponseWriter out = getResponseWriter();
    if( getPos( parent ).equals( TabConfig.POSITION_TOP ) ) {
      tabsCell.setAlign( getAlign( parent ) );
      tabsCell.setValue( buildHorizontalSelection( parent ) );
      tableRow.addCell( tabsCell );
      tableRow.render();
      tableRow.removeCell( 0 );
      tableRow.addCell( contentCell );
    } else if( getPos( parent ).equals( TabConfig.POSITION_BOTTOM ) ) {
      tableRow.addCell( contentCell );
      tableRow.render();
      tableRow.removeCell( 0 );
      tabsCell.setAlign( getAlign( parent ) );
      tabsCell.setValue( buildHorizontalSelection( parent ) );
      tableRow.addCell( tabsCell );
    } else if( getPos( parent ).equals( TabConfig.POSITION_LEFT ) ) {
      tabsCell.setVAlign( getAlign( parent ) );
      tabsCell.setValue( buildVerticalSelection( parent ) );
      tableRow.addCell( tabsCell );
      tableRow.addCell( contentCell );
    } else if( getPos( parent ).equals( TabConfig.POSITION_RIGHT ) ) {
      tableRow.addCell( contentCell );
      tabsCell.setVAlign( getAlign( parent ) );
      tabsCell.setValue( buildVerticalSelection( parent ) );
      tableRow.addCell( tabsCell );
    }
    tableRow.render();    
    createTableCloser( out );
    restoreBorderBuffer( parent );
  }

  public void scheduleRendering( final WebComponent component ) {
    WebContainer container = ( WebContainer )component;
    WebCardLayout layout = ( WebCardLayout )container.getWebLayout();
    String displayCard = layout.getDisplayCard();
    for( int i = 0; i < container.getWebComponentCount(); i++ ) {
      Object constraint = null;
      try {
        constraint = container.getConstraint( container.get( i ) );
      } catch( final Exception shouldNotHappen ) {
        throw new RuntimeException( shouldNotHappen );
      }
      if( constraint.equals( displayCard ) ) {
        getRenderingSchedule().schedule( container.get( i ) );        
      }
    }
  }
  
  public static void applyBorderSettings( final WebContainer parent,
                                          final Style cellStyle )
  {
    // everything but the opposite
    String pos = getLayout( parent ).getTabConfig().getTabPosition();
    if( pos.equals( TabConfig.POSITION_TOP ) ) {
      cellStyle.setBorderTop( getBorderString( parent ) );
      cellStyle.setBorderRight( getBorderString( parent ) );
      cellStyle.setBorderLeft( getBorderString( parent ) );
    } else if( pos.equals( TabConfig.POSITION_LEFT ) ) {
      cellStyle.setBorderTop( getBorderString( parent ) );
      cellStyle.setBorderBottom( getBorderString( parent ) );
      cellStyle.setBorderLeft( getBorderString( parent ) );
    } else if( pos.equals( TabConfig.POSITION_RIGHT ) ) {
      cellStyle.setBorderTop( getBorderString( parent ) );
      cellStyle.setBorderBottom( getBorderString( parent ) );
      cellStyle.setBorderRight( getBorderString( parent ) );
    } else if( pos.equals( TabConfig.POSITION_BOTTOM ) ) {
      cellStyle.setBorderBottom( getBorderString( parent ) );
      cellStyle.setBorderRight( getBorderString( parent ) );
      cellStyle.setBorderLeft( getBorderString( parent ) );
    }
  }

  static String getBorderString( final WebContainer parent ) {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "solid " );
    buffer.append( String.valueOf( getLayout( parent ).getBorderWidth() ) );
    buffer.append( "px " );
    buffer.append( getLayout( parent ).getBorderColor().toString() );
    return buffer.toString();
  }

  void setCardCount( final WebContainer parent, final int cardCount ){
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( CARD_COUNT, new Integer( cardCount ) );  
  }
  
  int getCardCount( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    Object renderState = adapter.getRenderState( CARD_COUNT );
    int result = 0;
    if( renderState != null ) {
      result = ( ( Integer )renderState ).intValue();
    }
    return result;
  }
  
  void setCardList( final WebContainer parent, final WebButton[] cardList ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( CARD_LIST, cardList );
  }
  
  WebButton[] getCardList( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( WebButton[] )adapter.getRenderState( CARD_LIST );
  }
  
  void setBorderSize( final WebContainer parent, final String borderSize ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( BORDER_SIZE, borderSize );      
  }
  
  String getBorderSize( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( String )adapter.getRenderState( BORDER_SIZE );    
  }
  
  void setDesignCard( final WebContainer parent,
                      final AreaSelector designCard )
  {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    adapter.addRenderState( DESIGN_CARD, designCard );          
  }
  
  AreaSelector getDesignCard( final WebContainer parent ) {
    IRenderInfoAdapter adapter = getRenderInfoAdapter( parent );
    return ( AreaSelector )adapter.getRenderState( DESIGN_CARD );    
  }
  
  IRenderInfoAdapter getRenderInfoAdapter( final WebContainer parent ) {
    Adaptable adaptable = getLayout( parent );
    Class clazz = IRenderInfoAdapter.class;
    return ( IRenderInfoAdapter )adaptable.getAdapter( clazz );
  }

  static WebCardLayout getLayout( final WebContainer parent ) {
    return ( WebCardLayout )parent.getWebLayout();
  }
  
  CardLayoutRenderInfo getInfo( final WebContainer parent ) {
    return ( CardLayoutRenderInfo )getRenderInfoAdapter( parent ).getInfo();
  }

  /** <p>Creates the (intermediate) button that allows switching from one card
   * (aka tab page) to another. Subclasses may override.</p> */
  WebButton createCardButton( final String label, final WebContainer parent ) {
    WebButton result = new WebButton( label, parent );
    result.setEnabled( getLayout( parent ).isEnabled() );
    result.setLink( true );
    result.setUseTrim( false );
    return result;
  }
  
  void createLeftSpacer( final WebContainer parent,
                         final WebTableRow tableRow )
  {
    // do nothing here, but in subclasses
  }

  void createRightSpacer( final WebContainer parent,
                          final WebTableRow tableRow )
  {
    // do nothing here, but in subclasses
  }

  void addDesignCard( final WebContainer parent ,
                      final WebTableRow tableRow ) 
  {
    setDesignCard( parent, null );
    if( parent.isDesignTime() ) {
      setDesignCard( parent, new AreaSelector() );
      getDesignCard( parent ).setContainer( parent );
      String constraint =   "Card" 
                          + String.valueOf( getVisibleCardCount( parent ) );
      getDesignCard( parent ).setConstraint( constraint );
      parent.add( getDesignCard( parent ), "develop" );
  
      WebTableCell tableCell
        = createSelectionCell( parent, getDesignCard( parent ) );
      tableRow.addCell( tableCell );
    }
  }

  void removeDesignCard( final WebContainer parent ) {
    if( parent.isDesignTime() ) {
      parent.remove( getDesignCard( parent ) );
    }
  }

  /** <p>creates a selection bar tab cell, which has the correct styles, 
   *  borders etc.</p> */
  WebTableCell createSelectionCell( final WebContainer parent,
                                    final WebButton card ) 
  {
    WebTableCell result = new WebTableCell();
    if( isDisplayCard( parent, card ) ) {
      result.setBgColor( getLayout( parent ).getCardColor() );
      result.setStyle( getLayout( parent ).getActiveTabStyle() );
      card.setStyle( getLayout( parent ).getActiveTabLinkStyle() );
    } else {
      result.setBgColor( getLayout( parent ).getInactiveTabColor() );
      result.setStyle( getLayout( parent ).getInactiveTabStyle() );
      result.setSpacing( "0" );
      result.setPadding( "3" );
      card.setStyle( getLayout( parent ).getInactiveTabLinkStyle() );
    }
    result.addContentElement( card );
    return result;
  }

  boolean isDisplayCard( final WebContainer parent,
                         final WebButton card )
  {
    return getLayout( parent ).getDisplayCard().equals( card.getLabel() );
  }

  String getAlign( final WebContainer parent ) {
    return getLayout( parent ).getTabConfig().getTabAlignment();
  }

  boolean isModernType( final WebContainer parent ) {
    int type = getLayout( parent ).getTabConfig().getType();
    return type == TabConfig.TYPE_MODERN;
  }

  private static void createCardButtonEventListener( final WebContainer parent, 
                                                     final WebButton card ) 
  {
    final String label = card.getLabel();
    card.addWebActionListener( new WebActionListener() {
      
      WebCardLayout layout = getLayout( parent );
      WebContainer container = parent;
      
      public void webActionPerformed( final WebActionEvent e ) {
        layout.setDisplayCard( label );                      

        // if an actionlistener is set, fire the event
        if( WebActionEvent.hasListener( layout ) ) {
          // get a reference to the clicked tab
          for( int j = 0; j < container.getWebComponentCount(); j++ ) {
            String constraint = ( String )container.getConstraint( j );
            if( constraint.equals( label ) ) {
              WebComponent wc = container.get( j );
              WebActionEvent evt
                = new WebActionEvent( wc, WebActionEvent.ACTION_PERFORMED );
              Object[] lsnr = WebActionEvent.getListeners( layout );
              for( int i = 0; i < lsnr.length; i++ ) {
                // TODO: [fappel] Exception handling?
                ( ( WebActionListener )lsnr[ i ] ).webActionPerformed( evt );
              }
            }
          }
        }
      }
    } );
  }
  
  private void checkBorderSizeInDesignTime( final WebContainer parent ) {
    // buffer border size
    setBorderSize( parent, getLayout( parent ).getBorder() );
    boolean useBorderSize_1 = false;
    if( parent.isDesignTime() ) {
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

  /** lookup the WebComponent to display as card */
  private static WebComponent findCard( final WebContainer parent ) {
    WebComponent card = null;
    String displayCard;
    boolean found = false;
    for( int i = 0; !found && i < parent.getWebComponentCount(); i++ ) {
      displayCard = getLayout( parent ).getDisplayCard();
      if( displayCard.equals( parent.getConstraint( i ) ) ) {
        card = parent.get( i );
        found = true;
      }
    }
    return card;
  }

  /** fills a cardList with WebButtons for the selection part of
    * the WebCardLayout */
  private void fillCardList( final WebContainer parent ) {
    List vecTemp = new Vector();

    setCardCount( parent, parent.getWebComponentCount() );
    for( int i = 0; i < getCardCount( parent ); i++ ) {
      final String label = ( String )parent.getConstraint( i );
      
      WebButton card = createCardButton( label, parent );

      // if the WebButton is clicked, bring the selected card to front
      if( getLayout( parent ).isEnabled() ) {
        createCardButtonEventListener( parent, card );
      }
      // prepare all visible cards for rendering
      for( int j = 0; j < parent.getWebComponentCount(); j++ ) {
        String compConstraint = ( String )parent.getConstraint( j );
        if( compConstraint.equals( label ) ) {
          WebComponent wc = parent.get( j );
          if( wc.isVisible() ) {
            vecTemp.add( card );
            if( getLayout( parent ).isCardEnabledByComponent() ) {
              card.setEnabled( wc.isEnabled() );
            }
          }
        }
      }
    }
    setCardCount( parent, vecTemp.size() );
    setCardList( parent, new WebButton[ getCardCount( parent ) ] );
    for( int i = 0; i < getCardCount( parent ); i++ ) {
      getCardList( parent )[ i ] = ( WebButton )vecTemp.get( i );
    }
  }

  /** creates the selection part with horizontal alignment */
  private String buildHorizontalSelection( final WebContainer parent )
    throws IOException
  {
    WebTableRow tableRow  = new WebTableRow();

    // build the table
    HtmlResponseWriter tempOut = new HtmlResponseWriter();
    HtmlResponseWriter out = getResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( tempOut );
    try {
      createInnerTableOpener( tempOut );
  
      // spacer
      if( getVisibleCardCount( parent ) > 0 ) {
        createLeftSpacer( parent, tableRow );
      }
  
      for( int i = 0; i < getVisibleCardCount( parent ); i++ ) {
        WebButton card = getVisibleCard( parent, i );
        WebTableCell tableCell = createSelectionCell( parent, card );
        tableCell.setNowrap( true );
        tableRow.addCell( tableCell );
      }
      addDesignCard( parent, tableRow );
      
      // spacer
      if( getVisibleCardCount( parent ) > 0 ) {
        createRightSpacer( parent, tableRow );
      }
      
      tableRow.render();
      removeDesignCard( parent );
      createTableCloser( tempOut );
    } finally {
      ContextProvider.getStateInfo().setResponseWriter( out );      
    }
    out.mergeRegisteredCssClasses( tempOut.getCssClasses() );
    return toString( tempOut );
  }

  /** creates the selection part with vertical alignment */
  private String buildVerticalSelection( final WebContainer parent )
    throws IOException
  {
    HtmlResponseWriter tempOut = new HtmlResponseWriter();
    HtmlResponseWriter out = getResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( tempOut );
    try {
      // build the table
      createInnerTableOpener( tempOut );    
      for( int i = 0; i < getCardCount( parent ); i++ ) {
        WebTableRow tableRow  = new WebTableRow();
        WebButton card = getVisibleCard( parent, i );
        WebTableCell tableCell = createSelectionCell( parent, card );
        tableCell.setNowrap( true );
        tableRow.addCell( tableCell );
        tableRow.render();
      }
      createTableCloser( tempOut );
    } finally {
      ContextProvider.getStateInfo().setResponseWriter( out );      
    }
    getResponseWriter().mergeRegisteredCssClasses( tempOut.getCssClasses() );
    return toString( tempOut );
  }

  private int getVisibleCardCount( final WebContainer parent ) {
    return getCardList( parent ).length;
  }

  private WebButton getVisibleCard( final WebContainer parent, 
                                    final int index )
  {
    return getCardList( parent )[ index ];
  }

  private static void createTableOpener( final WebContainer parent ) 
    throws IOException
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.TABLE, null );
    RenderUtil.writeUniversalAttributes( parent );
    RenderUtil.writeTableAttributes( getLayout( parent ) );
    out.writeAttribute( HTML.ID, parent.getUniqueID(), null );
  }

  private static void createTableCloser( final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.endElement( HTML.TABLE );
  }

  private static void createInnerTableOpener( final HtmlResponseWriter out ) 
    throws IOException 
  {
    out.startElement( HTML.TABLE, null );
    out.writeAttribute( HTML.CELLSPACING, "0", null );
    out.writeAttribute( HTML.CELLPADDING, "1", null );
    out.writeAttribute( HTML.BORDER, "0", null );
  }

  private static String getPos( final WebContainer parent ) {
    return getLayout( parent ).getTabConfig().getTabPosition();
  }
  
  private static String toString( final HtmlResponseWriter out ) {
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < out.getBodySize(); i++ ) {
      result.append( out.getBodyToken( i ) );
    }
    return result.toString();
  }
}