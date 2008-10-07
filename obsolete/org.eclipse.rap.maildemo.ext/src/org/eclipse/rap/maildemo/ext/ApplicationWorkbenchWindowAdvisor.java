/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.IAction;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.*;


/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

  private static final Image IMAGE_BANNER_BG
    = Activator.getImageDescriptor( "icons/banner_bg.png" ).createImage();
  private static final Image IMAGE_BANNER_ROUNDED_RIGHT
    = Activator.getImageDescriptor( "icons/banner_rounded_right.png" ).createImage();
  private static final Image IMAGE_BANNER_ROUNDED_LEFT
    = Activator.getImageDescriptor( "icons/banner_rounded_left.png" ).createImage();
  private static final Image IMAGE_CONTENT_ROUNDED_RIGHT
    = Activator.getImageDescriptor( "icons/content_rounded_right.png" ).createImage();
  private static final Image IMAGE_CONTENT_ROUNDED_LEFT
    = Activator.getImageDescriptor( "icons/content_rounded_left.png" ).createImage();
  private static final Color COLOR_BANNER_FG
    = Graphics.getColor( 255, 255, 255 );
  private static final Color COLOR_SHELL_BG
    = Graphics.getColor( 229, 236, 243 );
  private ApplicationActionBarAdvisor actionBarAdvisor;
  private Composite banner;

  public ApplicationWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer configurer )
  {
    super( configurer );
  }

  public ActionBarAdvisor createActionBarAdvisor(
    final IActionBarConfigurer configurer )
  {
    actionBarAdvisor = new ApplicationActionBarAdvisor( configurer );
    return actionBarAdvisor;
  }

  public void preWindowOpen() {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.setShowCoolBar( false );
    configurer.setShowStatusLine( false );
    configurer.setTitle( "RAP Mail Template" );
    configurer.setShellStyle( SWT.NO_TRIM );
  }

  public void postWindowOpen() {
    final IWorkbenchWindow window = getWindowConfigurer().getWindow();
    Shell shell = window.getShell();
    shell.setMaximized( true );
    shell.setBackground( COLOR_SHELL_BG );
  }
  
  public void createWindowContents( final Shell shell ) {
    shell.setLayout( new FormLayout() );
    createBanner( shell );
    createPageComposite( shell );
  }

  private void createBanner( final Shell shell ) {
    banner = new Composite( shell, SWT.NONE );
    banner.setBackgroundMode( SWT.INHERIT_DEFAULT );
    banner.setBackgroundImage( IMAGE_BANNER_BG );
    FormData fdBanner = new FormData();
    banner.setLayoutData( fdBanner );
    fdBanner.top = new FormAttachment( 0, 0 );
    fdBanner.left = new FormAttachment( 0, 50 );
    fdBanner.height = 88;
    fdBanner.right = new FormAttachment( 100, -50 );
    banner.setLayout( new FormLayout() );

    Label label = new Label( banner, SWT.NONE );
    label.setText( "Banner" );
    label.setForeground( COLOR_BANNER_FG );
    label.setData( WidgetUtil.CUSTOM_VARIANT, "bannerlabel" );
    label.setFont( Graphics.getFont( "Verdana", 24, SWT.BOLD ) );
    label.pack();
    FormData fdLabel = new FormData();
    label.setLayoutData( fdLabel );
    fdLabel.top = new FormAttachment( 0, 5 );
    fdLabel.left = new FormAttachment( 0, 10 );
    
    Label roundedCornerLeft = new Label( banner, SWT.NONE );
    roundedCornerLeft.setImage( IMAGE_BANNER_ROUNDED_LEFT );
    roundedCornerLeft.pack();
    FormData fdRoundedCornerLeft = new FormData();
    roundedCornerLeft.setLayoutData( fdRoundedCornerLeft );
    fdRoundedCornerLeft.top = new FormAttachment( 100, -5 );
    fdRoundedCornerLeft.left = new FormAttachment( 0, 0 );
    roundedCornerLeft.moveAbove( banner );
    
    Label roundedCornerRight = new Label( banner, SWT.NONE );
    roundedCornerRight.setImage( IMAGE_BANNER_ROUNDED_RIGHT );
    roundedCornerRight.pack();
    FormData fdRoundedCornerRight = new FormData();
    roundedCornerRight.setLayoutData( fdRoundedCornerRight );
    fdRoundedCornerRight.top = new FormAttachment( 100, -5 );
    fdRoundedCornerRight.left = new FormAttachment( 100, -5 );
    roundedCornerRight.moveAbove( banner );

    createBannerActionBar( banner );
    createPerspectiveSwitcher( banner );
    createSearch( banner );
  }

  private void createBannerActionBar( final Composite banner ) {
    IAction[] commands = actionBarAdvisor.getActions();
    BannerActionBar actionBar = new BannerActionBar( banner, commands );
    FormData fdComposite = new FormData();
    fdComposite.top = new FormAttachment( 0, 8 );
    fdComposite.left = new FormAttachment( 0, 200 );
    fdComposite.bottom = new FormAttachment( 0, 36 );
    actionBar.setLayoutData( fdComposite );
  }

  private void createPerspectiveSwitcher( final Composite banner ) {
    PerspectiveSwitcher switcher = new PerspectiveSwitcher( banner );
    FormData fdActionBar = new FormData();
    fdActionBar.top = new FormAttachment( 0, 45 );
    fdActionBar.left = new FormAttachment( 0, 10 );
    switcher.setLayoutData( fdActionBar );
  }

  private void createSearch( final Composite banner ) {
    SearchBar search = new SearchBar( banner );
    FormData fdSearch = new FormData();
    fdSearch.top = new FormAttachment( 0, 10 );
    fdSearch.left = new FormAttachment( 100, -175 );
    search.setLayoutData( fdSearch );
  }

  private void createPageComposite( final Shell shell ) {
    Composite contentTop = new Composite( shell, SWT.NONE );
    FormData fdContentTop = new FormData();
    contentTop.setLayoutData( fdContentTop );
    fdContentTop.top = new FormAttachment( 0, 98 );
    fdContentTop.left = new FormAttachment( 0, 50 );
    fdContentTop.right = new FormAttachment( 100, -50 );
    fdContentTop.bottom = new FormAttachment( 0, 103 );
    contentTop.setBackground( Graphics.getColor( 255, 255, 255 ) );
    
    Label roundedCornerLeft = new Label( shell, SWT.NONE );
    roundedCornerLeft.setImage( IMAGE_CONTENT_ROUNDED_LEFT );
    roundedCornerLeft.pack();
    FormData fdRoundedCornerLeft = new FormData();
    roundedCornerLeft.setLayoutData( fdRoundedCornerLeft );
    fdRoundedCornerLeft.top = new FormAttachment( 0, 98 );
    fdRoundedCornerLeft.left = new FormAttachment( 0, 50 );
    roundedCornerLeft.moveAbove( contentTop );
    
    Label roundedCornerRight = new Label( shell, SWT.NONE );
    roundedCornerRight.setImage( IMAGE_CONTENT_ROUNDED_RIGHT );
    roundedCornerRight.pack();
    FormData fdRoundedCornerRight = new FormData();
    roundedCornerRight.setLayoutData( fdRoundedCornerRight );
    fdRoundedCornerRight.top = new FormAttachment( 0, 98 );
    fdRoundedCornerRight.left = new FormAttachment( 100, -55 );
    roundedCornerRight.moveAbove( contentTop );

    Composite content = new Composite( shell, SWT.NONE );
    content.setBackground( Graphics.getColor( 255, 255, 255 ) );
    FormData fdContent = new FormData();
    content.setLayoutData( fdContent );
    fdContent.top = new FormAttachment( 0, 103 );
    fdContent.left = new FormAttachment( 0, 50 );
    fdContent.right = new FormAttachment( 100, -50 );
    fdContent.bottom = new FormAttachment( 100, 0 );
    FillLayout fillLayout = new FillLayout();
    fillLayout.marginWidth = 3;
    content.setLayout( fillLayout );
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.createPageComposite( content );
  }
}
