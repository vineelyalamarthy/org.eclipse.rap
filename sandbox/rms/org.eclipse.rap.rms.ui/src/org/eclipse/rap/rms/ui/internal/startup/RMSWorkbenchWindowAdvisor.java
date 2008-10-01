// Created on 10.09.2007
package org.eclipse.rap.rms.ui.internal.startup;

import org.eclipse.rap.rms.ui.internal.Activator;
import org.eclipse.rap.rms.ui.internal.RMSMessages;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.internal.WorkbenchPage;


public class RMSWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
  private static final Color COLOR_WHITE
    = Display.getDefault().getSystemColor( SWT.COLOR_WHITE );
  
  private boolean introActive = true;

  public RMSWorkbenchWindowAdvisor(
    final IWorkbenchWindowConfigurer configurer )
  {
    super( configurer );
  }
  
  @Override
  public ActionBarAdvisor createActionBarAdvisor(
    final IActionBarConfigurer configurer )
  {
    return new RMSActionBarAdvisor( configurer );
  }
  
  @Override
  public void preWindowOpen() {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.setShowPerspectiveBar( true );
    configurer.setShowMenuBar( true );
    configurer.setShowCoolBar( true );
    configurer.setShowStatusLine( false );
    configurer.setTitle( RMSMessages.get().RMSWorkbenchWindowAdvisor_RAPWorkbenchDemo );
    configurer.setShellStyle( SWT.NONE );
    Rectangle bounds = Display.getDefault().getBounds();
    configurer.setInitialSize( new Point( bounds.width, bounds.height ) );
  }
  
  @Override
  public void postWindowOpen() {
    final IWorkbenchWindow window = getWindowConfigurer().getWindow();
    Shell shell = window.getShell();
    shell.setMaximized( true );
    
    final Composite mainBanner = createMainBanner( shell );
    setMainBannerSize( mainBanner );
    
    final Composite secondaryBanner = createSecondaryBanner( shell );
    final WorkbenchPage wPage = ( WorkbenchPage )window.getActivePage();
    setSecondaryBannerSize( secondaryBanner, wPage );
    
    shell.addControlListener( new ControlAdapter() {
      @Override
      public void controlResized( final ControlEvent evt ) {
        setMainBannerSize( mainBanner );
        setSecondaryBannerSize( secondaryBanner, wPage );
        setPageBounds( wPage );
      }
    } );
    
    window.addPerspectiveListener( new PerspectiveAdapter() {
      public void perspectiveActivated( IWorkbenchPage page,
                                        IPerspectiveDescriptor perspective )
      {
        String idIntro
          = "org.eclipse.rap.rms.ui.internal.startup.IntroPerspective"; //$NON-NLS-1$
        introActive = idIntro.equals( perspective.getId() );
        secondaryBanner.setVisible( !introActive );
        setPageBounds( page );
      }
    } );

  }

  private void setSecondaryBannerSize( final Composite secondaryBanner,
                                       final WorkbenchPage page )
  {
    Composite clientComposite = page.getClientComposite();
    Rectangle bounds = clientComposite.getBounds();
    Display display = Display.getCurrent();
    Point location = display.map( clientComposite.getParent(), 
                                  secondaryBanner.getShell(), 
                                  new Point( 1, 1 ) );
    secondaryBanner.setBounds( location.x, 
                               location.y,
                               bounds.width, 
                               bounds.height );
    
  }

  private Composite createSecondaryBanner( final Shell shell ) {
    final Composite result = new Composite( shell, SWT.NONE );
    result.setLayout( new FormLayout() );

    Label lblBackground = new Label( result, SWT.NONE );
    String imgBanner = Activator.IMG_BANNER_SECONDARY;
    Activator activator = Activator.getDefault();
    lblBackground.setImage( activator.getImage( imgBanner ) );
    FormData fdBackground = new FormData();
    fdBackground.top = new FormAttachment( 0, 0 );
    fdBackground.left = new FormAttachment( 0, 0 );
    fdBackground.right = new FormAttachment( 100, 0 );
    fdBackground.bottom = new FormAttachment( 100, 0 );
    lblBackground.setLayoutData( fdBackground );

    String imgOverview = Activator.IMG_INTRO_OVERVIEW;
    Label lblOverview = new Label( result, SWT.NONE );
    lblOverview.setImage( activator.getImage( imgOverview ) );
    FormData fdOverview = new FormData();
    fdOverview.top = new FormAttachment( 0, 2 );
    fdOverview.left = new FormAttachment( 0, 4 );
    fdOverview.right = new FormAttachment( 0, 68 );
    fdOverview.bottom = new FormAttachment( 0, 66 );
    lblOverview.setLayoutData( fdOverview );
    lblOverview.moveAbove( lblBackground );
    lblOverview.setBackground( Graphics.getColor( 225, 234, 241 ) );
    
    String imgOvTxt = Activator.IMG_BANNER_SECONDARY_TXT;
    Label lblOvTxt = new Label( result, SWT.NONE );
    lblOvTxt.setImage( activator.getImage( imgOvTxt ) );
    lblOvTxt.pack();
    FormData fdOvTxt = new FormData();
    fdOvTxt.top = new FormAttachment( 0, 26 );
    fdOvTxt.left = new FormAttachment( 0, 79 );
    lblOvTxt.setLayoutData( fdOvTxt );
    lblOvTxt.moveAbove( lblBackground );

    return result;
  }

  private Composite createMainBanner( final Shell shell ) {
    final Composite result = new Composite( shell, SWT.NONE );
    result.setBackground( COLOR_WHITE );
    result.setLayout( new FormLayout() );
    
    Label lblBackground = new Label( result, SWT.NONE );
    String imgBanner = Activator.IMG_INTRO_BANNER;
    Activator activator = Activator.getDefault();
    lblBackground.setImage( activator.getImage( imgBanner ) );
    lblBackground.setBackground( COLOR_WHITE );
    FormData fdBackground = new FormData();
    fdBackground.top = new FormAttachment( 0, 0 );
    fdBackground.left = new FormAttachment( 0, 0 );
    fdBackground.right = new FormAttachment( 100, 0 );
    fdBackground.bottom = new FormAttachment( 100, 0 );
    lblBackground.setLayoutData( fdBackground );
    return result;
  }

  private void setMainBannerSize( final Composite banner ) {
    Rectangle clientArea = banner.getShell().getClientArea();
    banner.setBounds( clientArea.x + 3,
                      0,
                      clientArea.width - 6, 
                      clientArea.y - 1 );
  }

  private void setPageBounds( final IWorkbenchPage page ) {
    WorkbenchPage wPage = ( WorkbenchPage )page;
    Composite clientComposite = wPage.getClientComposite();
    if( !introActive ) {
      Rectangle bounds = clientComposite.getParent().getBounds();
      clientComposite.getParent().setBounds( bounds.x + 100, 
                                             bounds.y + 100,
                                             bounds.width - 100, 
                                             bounds.height - 100 );
    } else {
      Composite parent = clientComposite.getParent();
      clientComposite.setBounds( parent.getClientArea() );
    }
  }
}
