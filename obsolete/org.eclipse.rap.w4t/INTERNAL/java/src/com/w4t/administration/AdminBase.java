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
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;

/**
  * The base class for all administration tools for the WebComponent
  * project.
  */
public abstract class AdminBase extends WebForm {

  public final static String FONT = "arial,verdana";
  
  protected String administration = "Administration";
  protected String headline = "Base class";
  protected String footer = "";

  WebBorderPanel wplHeaderBorder;
  WebPanel wplHeader;
  Position posHeaderLeft;
  Position posHeaderRight;
  Position posHeaderMenu;

  WebPanel wplMenu;
  Position posMenuLeft;
  Position posMenuRight;

  WebButton wbtClose;  

  WebLabel wlbAdmin;
  WebLabel wlbHeadline;

  WebBorderPanel wplCenterBorder;

  WebLabel wlbFooter;

  public AdminBase() {
    super();
    WindowProperties windowProperties = getWindowProperties();
    windowProperties.setWidth( "800" );
    windowProperties.setHeight( "600" );
  }

  protected void setWebComponents() {
    initialiseHeader();
    initialiseCenter();
    initialiseFooter();
    initCloseButton();
  }

  private void initialiseFooter() {
    MarkupEmbedder wlbBr = new MarkupEmbedder( "<br />" );
    this.add( wlbBr, "SOUTH" );
    wlbFooter = new WebLabel( footer );
    wlbFooter.getStyle().setFontFamily( FONT );
    this.add( wlbFooter, "SOUTH" );
    WebBorderLayout wblBaseForm = ( WebBorderLayout )this.getWebLayout();
    wblBaseForm.getArea( "SOUTH" ).setAlign( "left" );
  }

  private void initialiseCenter() {
    MarkupEmbedder wlbNbsp = new MarkupEmbedder();
    wlbNbsp.setContent( HTML.NBSP );
    add( wlbNbsp, "CENTER" );
    wplCenterBorder = new WebBorderPanel();
    add( wplCenterBorder, "CENTER" );
  }

  private void initialiseHeader() {
    WebBorderLayout wbl = ( WebBorderLayout )getWebLayout();
    wbl.getArea( "NORTH" ).setBgColor( 
      new WebColor( DefaultColorScheme.get( DefaultColorScheme.ADMIN_MENU ) ) );
    wbl.getArea( "CENTER" ).setVAlign( "top" );
    wbl.setWidth( "100%" );
    setBgColor( 
      new WebColor( DefaultColorScheme.get( DefaultColorScheme.ADMIN_BG ) ) );

    wplHeaderBorder = new WebBorderPanel();
    add( wplHeaderBorder, "NORTH" );

    wplHeader = new WebPanel();
    posHeaderLeft = new Position( 1, 1 );
    posHeaderRight = new Position( 1, 2 );
    posHeaderMenu = new Position( 2, 1 );
    WebGridLayout wglHeader = new WebGridLayout( 2, 2 );
    wglHeader.setBgColor( 
      new WebColor( DefaultColorScheme.get( DefaultColorScheme.ADMIN_MENU ) ) );
    wglHeader.setWidth( "100%" );
    wglHeader.getArea( posHeaderRight ).setAlign( "right" );
    wglHeader.setCellpadding( "8" );
    ( ( WebTableCell )wglHeader.getArea( posHeaderMenu ) ).setColspan( "2" );
    wplHeader.setWebLayout( wglHeader );
    wplHeaderBorder.add( wplHeader, "CENTER" );

    MarkupEmbedder mebHr = new MarkupEmbedder( "<hr>" );
    wplHeader.add( mebHr, posHeaderMenu );
    wplMenu = new WebPanel();
    WebGridLayout wglMenu = new WebGridLayout( 1, 2 );
    wglMenu.setBgColor( 
      new WebColor( DefaultColorScheme.get( DefaultColorScheme.ADMIN_MENU ) ) );
    posMenuLeft = new Position( 1, 1 );
    posMenuRight = new Position( 1, 2 );
    wglMenu.getArea( posMenuLeft ).setAlign( "left" );
    wglMenu.getArea( posMenuRight ).setAlign( "right" );
    wglMenu.setWidth( "100%" );
    wplMenu.setWebLayout( wglMenu );
    wplHeader.add( wplMenu, posHeaderMenu );

    wlbAdmin = new WebLabel( administration );
    wlbAdmin.getStyle().setFontFamily( FONT );
    wlbAdmin.getStyle().setFontSize( 22 );
    String fontColor 
      = DefaultColorScheme.get( DefaultColorScheme.ADMIN_HEADER_FONT );
    wlbAdmin.getStyle().setColor( new WebColor( fontColor ) );
    wplHeader.add( wlbAdmin, posHeaderLeft );

    WebLabel wlbHeadline = new WebLabel( headline );
    wlbHeadline.getStyle().setFontSize( 14 );
    String headColor 
      = DefaultColorScheme.get( DefaultColorScheme.ADMIN_HEADER_FONT );
    wlbHeadline.getStyle().setColor( new WebColor( headColor ) );
    wlbHeadline.getStyle().setFontFamily( FONT );
    wplHeader.add( wlbHeadline, posHeaderRight );
  }

  protected void initCloseButton() {
    wbtClose = new LinkButton( "close" );
    wbtClose.addWebActionListener( new WebActionListener() {
      public void webActionPerformed( final WebActionEvent e ) {
        closeWindow();
      }
    } );
    wplMenu.add( wbtClose, posMenuRight );
  }
}

