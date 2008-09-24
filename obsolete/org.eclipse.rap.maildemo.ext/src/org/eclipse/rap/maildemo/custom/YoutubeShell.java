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

package org.eclipse.rap.maildemo.custom;

import org.eclipse.rap.maildemo.ext.Activator;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class YoutubeShell {
  
  private final Color bgColor;
  private final Color fgColor;
  private final Image closeImage;
  private Shell shield;
  protected Shell shell;
  private Browser browser;
  private final Display display;
  
  public YoutubeShell( final Display display ) {
    this.display = display;
    bgColor = display.getSystemColor( SWT.COLOR_BLACK );
    fgColor = display.getSystemColor( SWT.COLOR_WHITE );
    closeImage = Activator.getImageDescriptor( "icons/close.png" ).createImage();
    createShield();
    createShell();
  }
  
  public void setId( final String id ) {
    browser.setText( getHtml( id ) );
  }

  public void show() {
    shield.setVisible( true );
    shield.open();
    centerShell();
    shell.setVisible( true );
    shell.open();
  }

  public void hide() {
    shield.setVisible( false );
    shell.setVisible( false );
  }

  public void dispose() {
    if( shell != null && !shell.isDisposed() ) {
      shell.dispose();
    }
    if( shield != null && !shield.isDisposed() ) {
      shield.dispose();
    }
  }

  private void createShield() {
    shield = new Shell( display, SWT.NONE );
    shield.setBackground( Graphics.getColor( 0, 0, 0 ) );
    shield.setAlpha( 150 );
    shield.setMaximized( true );
  }

  private void createShell() {
    shell = new Shell( shield, SWT.APPLICATION_MODAL );
    FillLayout shellLayout = new FillLayout();
    shell.setLayout( shellLayout );
    shell.setBackground( bgColor );
    
    Composite comp = new Composite( shell, SWT.NONE );
    comp.setBackground( bgColor );
    GridLayout compLayout = new GridLayout();
    compLayout.marginWidth = 0;
    compLayout.marginHeight = 0;
    compLayout.marginLeft = 10;
    compLayout.marginRight = 8;
    compLayout.marginBottom = 8;
    comp.setLayout( compLayout );
    
    Button closeButton = new Button( comp, SWT.PUSH | SWT.FLAT | SWT.RIGHT );
//    closeButton.setText( "close" ); // label overwrites our custom cursor
    closeButton.setBackground( bgColor );
    closeButton.setForeground( fgColor );
//    closeButton.setData( WidgetUtil.CUSTOM_APPEARANCE, "black-shell-button" );
    closeButton.setImage( closeImage );
    closeButton.setLayoutData( new GridData( SWT.RIGHT, SWT.BOTTOM, false, false ) );
    closeButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        hide();
      }
    } );
    
    browser = new Browser( comp, SWT.NONE );
    browser.setBackground( bgColor );
    browser.setLayoutData( new GridData( 425, 373 ) );
  }

  private void centerShell() {
    shell.pack();
    Rectangle displayBounds = display.getBounds();
    Rectangle shellBounds = shell.getBounds();
    int x = ( displayBounds.width - shellBounds.width ) / 2;
    int y = ( displayBounds.height - shellBounds.height ) / 2;
    shell.setLocation( x, y );
  }

  private static String getHtml( final String id ) {
    String html = "<html><body>"
      + "<object width=\"425\" height=\"373\">"
      + "<param name=\"movie\" value=\"http://www.youtube.com/v/" + id + "&rel=1&border=1\"></param>"
      + "<param name=\"wmode\" value=\"transparent\"></param>"
      + "<embed src=\"http://www.youtube.com/v/" + id + "&rel=1&border=1\" type=\"application/x-shockwave-flash\" wmode=\"transparent\" width=\"425\" height=\"373\"></embed>"
      + "</object>"
      + "</body></html>";
    return html;
  }
}
