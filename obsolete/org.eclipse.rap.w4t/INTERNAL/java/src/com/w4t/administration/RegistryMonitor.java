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
package com.w4t.administration;

import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;


/**
  * <p>This is a Module for the standard W4 Toolkit administration tool that
  * provides information about a web application instance running on the
  * w4t engine.</p>
  */
public class RegistryMonitor extends AdminBase {

  /** the statistics object, which is retrieved from the WebComponentRegistry
    * and contains all info that is displayed in this RegistryMonitor. */
  private WebComponentStatistics stats;


  /** <p>constructs a new RegistryMonitor (initialization of the gui is done in
    * setWebComponents).</p> */
  public RegistryMonitor() {
    super();
    headline = "WebComponent Registry Monitor";
  }

  /** <p>retrieves an up-to-date statistics object from the registry and 
    * refreshes the gui components of this RegistryMonitor.</p> */
  public void refresh() {
    this.removeAll();
    try {
      this.setWebComponents();
    } catch( final Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

  protected void setWebComponents() {
    super.setWebComponents();
    stats = W4TContext.getStatistics( false );
    
    wplCenterBorder.add( createHeadLinePanel(), WebBorderLayout.CENTER );
    wplCenterBorder.add( createWplComponentTable(), WebBorderLayout.CENTER );

    wplCenterBorder
      .add( createInfoPanel( "<br>" + stats.getSessionCountText() ),
            WebBorderLayout.CENTER );
    wplCenterBorder.add( createInfoPanel( stats.getApplicationUptimeText() ),
              WebBorderLayout.CENTER );
    wplCenterBorder.add( createInfoPanel( stats.getOccupiedMemoryKBText() ),
              WebBorderLayout.CENTER );
    wplCenterBorder.add( createInfoPanel( stats.getStatisticsTimeText() ),
              WebBorderLayout.CENTER );

    initRefreshButton();
    initSeparator();
    initGCButton();
  }

  private WebPanel createHeadLinePanel() {
    WebPanel wplHeadLine = new WebPanel();

    WebFlowLayout wflHeadLine = ( WebFlowLayout )wplHeadLine.getWebLayout();
    wflHeadLine.getArea().setAlign( "left" );
    String headline =   "<br><b>&nbsp;WebComponentStatistics</b> "
                      + stats.getCreationTime() + "<br><hr>";
    MarkupEmbedder wlbHeadline = new MarkupEmbedder( headline );
    wplHeadLine.add( wlbHeadline );

    return wplHeadLine;
  }


  /** <p>creates a panel that contains a table with the components counts from
    * the registry.</p> */
  private WebPanel createWplComponentTable() {
    // get the data and init the grid
    WebPanel wplComponentTable = new WebPanel();
    String[] componentCounts = stats.getComponentCountsText();
    int size = componentCounts.length;
    WebGridLayout wglComponentTable = new WebGridLayout( size + 1, 2 );
    wplComponentTable.setWebLayout( wglComponentTable );

    // the counts for the various types of components
    for( int i = 0; i < size - 1; i++ ) {
      int colonIndex = componentCounts[ i ].indexOf( ":" );
      int rowNumber = i + 1;
      Area area = wglComponentTable.getArea( new Position( rowNumber, 1 ) );
      area.setAlign( "right" );
      String numComponents = componentCounts[ i ].substring( 0, colonIndex );
      WebLabel wlbNumComponents = new WebLabel( numComponents.trim() );
      wplComponentTable.add( wlbNumComponents, new Position( rowNumber, 1 ) );
      String cmpName = componentCounts[ i ].substring( colonIndex + 1 ).trim();
      String nameComponent =   "<code>&nbsp;"
                             + cmpName
                             + "</code><br>";
      MarkupEmbedder wlbNameComponent = new MarkupEmbedder( nameComponent );
      wplComponentTable.add( wlbNameComponent, new Position( rowNumber, 2 ) );
    }

    // an empty row
    Position pos = new Position( size, 1 );
    ( ( WebTableCell )wglComponentTable.getArea( pos ) ).setColspan( "2" );
    MarkupEmbedder wlbNbsp = new MarkupEmbedder( HTML.NBSP );
    wplComponentTable.add( wlbNbsp, pos );

    // the count for all components altogether
    pos = new Position( size + 1, 1 );
    wglComponentTable.getArea( pos ).setAlign( "right" );
    String compAltogether 
      = String.valueOf( stats.getComponentCountAltogetherText() );
    int colonIndex = compAltogether.indexOf( ":" );
    String compCount = "<code>"
                     + compAltogether.substring( 0, colonIndex )
                     + "</code>";
    MarkupEmbedder wlbNumAltogether = new MarkupEmbedder( compCount );
    wplComponentTable.add( wlbNumAltogether, pos );
    MarkupEmbedder wlbAltogether 
      = new MarkupEmbedder( "&nbsp;Components altogether in the registry." );
    wplComponentTable.add(
              wlbAltogether,
              new Position( size + 1, 2 ) );
    return wplComponentTable;
  }

  private WebPanel createInfoPanel( final String content ) {
    WebPanel wplInfo = new WebPanel();
    WebFlowLayout wflInfo = ( WebFlowLayout )wplInfo.getWebLayout();
    wflInfo.getArea().setAlign( "left" );
    MarkupEmbedder wlbContent = new MarkupEmbedder( HTML.NBSP + content );
    wplInfo.add( wlbContent );
    return wplInfo;
  }

  private void initRefreshButton() {
    WebButton wbtRefresh = new LinkButton( "refresh" );
    wbtRefresh.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        doRefreshButton();
      }
    } );
    wplMenu.add( wbtRefresh, posMenuLeft );
  }

  private void initSeparator() {
    WebComponent separator = Separator.create();
    wplMenu.add( separator, posMenuLeft );
  }

  private void initGCButton() {
    WebButton wbtGC = new LinkButton( "gc" );
    wbtGC.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent evt ) {
        System.gc();
        refresh();
      }
    } );
    wplMenu.add( wbtGC, posMenuLeft );
  }
  
  private void doRefreshButton() {
    refresh();
  }
}