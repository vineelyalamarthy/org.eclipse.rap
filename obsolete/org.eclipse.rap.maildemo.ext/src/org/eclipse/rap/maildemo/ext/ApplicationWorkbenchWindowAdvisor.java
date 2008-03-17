package org.eclipse.rap.maildemo.ext;

import java.util.ArrayList;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.commands.ICommandService;

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
    configurer.setInitialSize( new Point( 600, 400 ) );
    configurer.setShowCoolBar( true );
    configurer.setShowStatusLine( false );
    configurer.setTitle( "RAP Mail Template" );
    configurer.setShellStyle( SWT.NONE );
    Rectangle bounds = Display.getDefault().getBounds();
    configurer.setInitialSize( new Point( bounds.width, bounds.height ) );
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
    
    Composite composite = new Composite( shell, SWT.NONE );
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.createCoolBarControl( composite );
    
    createPageComposite( shell );    
  }

  private void createBanner( final Shell shell ) {
    banner = new Composite( shell, SWT.NONE );
    // banner.setBackgroundMode( SWT.INHERIT_DEFAULT );
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
    
    bannerActionButtons( banner );
    createPerspectiveSwitcher( banner );
    createSearch( banner );
  }

  private void createSearch( final Composite banner ) {
    new Search( banner );
  }


  private void createPerspectiveSwitcher( final Composite banner ) {
    new PerspectiveSwitcher( banner );
  }
  

  private void bannerActionButtons( final Composite banner ) {
    IAction[] commands = actionBarAdvisor.getActions();
    new BannerActions( banner, commands );
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
