package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.editor.product.DependenciesPage;
import org.eclipse.pde.internal.ui.editor.product.OverviewPage;
import org.eclipse.pde.internal.ui.editor.product.ProductEditor;
import org.eclipse.ui.PartInitException;

public class WARProductEditor extends ProductEditor {

  private WARProductExportAction exportAction;

  public WARProductEditor() {
    System.out.println( "Editor started" );
  }

  protected void addEditorPages() {
    try {
      addPage( new OverviewPage( this ) );
      addPage( new DependenciesPage( this, useFeatures() ) );
      // addPage(new ConfigurationPage(this, false));
      // addPage(new LaunchingPage(this));
      // addPage(new SplashPage(this));
      // addPage(new BrandingPage(this));
      // addPage(new LicensingPage(this));
    } catch( final PartInitException e ) {
      PDEPlugin.logException( e );
    }
  }

  public void contributeToToolbar( IToolBarManager manager ) {
    //super.contributeToToolbar( manager );
    manager.add( getExportAction() );
  }

  private WARProductExportAction getExportAction() {
    if( exportAction == null ) {
      exportAction = new WARProductExportAction( this );
      exportAction.setToolTipText( "Export WAR product" );
      exportAction.setImageDescriptor( PDEPluginImages.DESC_EXPORT_PRODUCT_TOOL );
    }
    return exportAction;
  }
}
