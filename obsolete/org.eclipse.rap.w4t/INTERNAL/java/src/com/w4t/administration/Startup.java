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
import com.w4t.event.*;
import com.w4t.types.WebColor;
import com.w4t.util.DefaultColorScheme;


/**
  * A helping class for startup the administration tools.
  */
public class Startup extends AdminBase {
  
  protected final WebForm previous;

  WebButton wbtRegistryMonitor;
  WebButton wbtLicenseMonitor;
  WebButton wbtPreloadBufferMonitor;
  WebPanel wplCenter;

  private RegistryMonitor registryMonitor;
  
  public Startup() {
    super();
    previous = this;

    this.addWebFormListener( new WebFormAdapter() {
      public void webFormClosing( WebFormEvent e ) {
        if( registryMonitor != null ) {
          registryMonitor.unload();
        }
        self.unload();
      }
    } );
  }
  
  // component initialisation
  ///////////////////////////
  
  public void setWebComponents()  {
    super.setWebComponents();
    initMenu();
    initCenter();
  }

  private void initMenu() {
    addRegistryMonitor();
    addSeparator();
    addSeparator();
  }

  private void addSeparator() {
    wplMenu.add( Separator.create(), posMenuLeft );
  }
  
  private void addRegistryMonitor() {  
    wbtRegistryMonitor = new LinkButton( "Registry Monitor" );
    wbtRegistryMonitor.addWebActionListener( new WebActionListener() {
        public void webActionPerformed( WebActionEvent evt ) {
          if( registryMonitor == null ) {
            String formName = RegistryMonitor.class.getName();
            registryMonitor 
              = ( RegistryMonitor )W4TContext.loadForm( formName );
            W4TContext.showInNewWindow( registryMonitor );
          } else {
            registryMonitor.refresh();
            registryMonitor.refreshWindow();
          }
        }
    } );
    wplMenu.add( wbtRegistryMonitor, posMenuLeft );
  }
  
  private void initCenter() {
    wplCenter = new WebPanel();
    WebGridLayout wgl = new WebGridLayout( 1, 2 );
    wplCenter.setWebLayout( wgl );
    wgl.setWidth( "100%" );
    wgl.getArea( new Position( 1, 1 ) ).setVAlign( "top" );
    String centerColor 
      = DefaultColorScheme.get( DefaultColorScheme.ADMIN_CENTER );
    wgl.setBgColor( new WebColor( centerColor ) );

    WebPanel wplPools = new WebPanel();
    WebGridLayout wglPools = new WebGridLayout( 3, 2 );
    wglPools.setBgColor( new WebColor( centerColor ) );
    Position posHead = new Position( 1, 1 );
    wglPools.setCellpadding( "8" );
    ( ( WebTableCell )wglPools.getArea( posHead ) ).setColspan( "2" );
    wplPools.setWebLayout( wglPools );
    wplCenter.add( wplPools, new Position( 1, 1 ) );
  }
}

