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

import com.w4t.*;
import com.w4t.engine.classloader.ClassLoaderCache;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;


/** <p>An administration WebForm that displays information about the
  * classloader buffer of the currently running web application.</p>
  */
public class PreloadBufferMonitor extends AdminBase {
  
  private static final String MIN_THRESHOLD 
   = "The minimal (threshold) value.<br>Refilling the preload buffer "
   + "starts when this value is reached.";
  private static final String BUFFER_SIZE  = "The current size of the buffer.";
  private static final String MAX_SIZE     = "The maximal size of the buffer.";
  
  private WebPanel wplHeadLine;
  private WebPanel wplBufferInfo;

  private String[] bufferInfo;
  
  
  /** <p>constructs a new LicenseMonitor (initialization of the gui is done in
    * setWebComponents).</p> */
  public PreloadBufferMonitor() {
    super();
    headline = "Preload Buffer Monitor";
  }

  void refresh() {
    initialiseInfoGrid();
  }

  
  // component initialisation
  ///////////////////////////
  protected void setWebComponents() {
    super.setWebComponents();
    initialiseWplHeadLine();
    initialisewplBufferInfo();
    initRefreshButton();
  }
  
  private void initialiseWplHeadLine() {
    wplHeadLine = new WebPanel();
    wplCenterBorder.add( wplHeadLine, WebBorderLayout.CENTER );
  }
  
  private void initialisewplBufferInfo() {
    wplBufferInfo = new WebPanel();
    WebGridLayout wgl = new WebGridLayout( 3, 2 );
    wgl.setWidth( "60%" );
    wgl.setCellpadding( "5" );
    ( ( WebTableCell )wgl.getArea( new Position( 1, 2 ) ) ).setAlign( "right" );
    ( ( WebTableCell )wgl.getArea( new Position( 2, 2 ) ) ).setAlign( "right" );
    ( ( WebTableCell )wgl.getArea( new Position( 3, 2 ) ) ).setAlign( "right" );
    wplBufferInfo.setWebLayout( wgl );
    initialiseInfoGrid();    
    wplCenterBorder.add( wplBufferInfo, WebBorderLayout.CENTER );
  }
  
  private void initialiseInfoGrid() {
    readBufferInfo();
    wplBufferInfo.removeAll();

    WebLabel wlbMinThresholdTitle = new WebLabel( MIN_THRESHOLD );
    wplBufferInfo.add( wlbMinThresholdTitle, new Position( 1, 1 ) );
    WebLabel wlbMinThreshold = new WebLabel( bufferInfo[ 0 ] );
    wplBufferInfo.add( wlbMinThreshold, new Position( 1, 2 ) );
    
    WebLabel wlbBufferSizeTitle = new WebLabel( BUFFER_SIZE );
    wplBufferInfo.add( wlbBufferSizeTitle, new Position( 2, 1 ) );
    WebLabel wlbBufferSize = new WebLabel( bufferInfo[ 1 ] );
    wplBufferInfo.add( wlbBufferSize, new Position( 2, 2 ) );
    
    WebLabel wlbMaxSizeTitle = new WebLabel( MAX_SIZE );
    wplBufferInfo.add( wlbMaxSizeTitle, new Position( 3, 1 ) );
    WebLabel wlbMaxSize = new WebLabel( bufferInfo[ 2 ] );
    wplBufferInfo.add( wlbMaxSize, new Position( 3, 2 ) );
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

  private void doRefreshButton() {
    refresh();
  }  
  
  // helping methods
  //////////////////
  
  private void readBufferInfo() {
    bufferInfo = new String[ 3 ];
    ClassLoaderCache clc = ClassLoaderCache.getInstance();
    bufferInfo[ 0 ] = String.valueOf( clc.getMinThreshold() );
    bufferInfo[ 1 ] = String.valueOf( clc.getSize() );
    bufferInfo[ 2 ] = String.valueOf( clc.getMaxSize() );
  }
}
