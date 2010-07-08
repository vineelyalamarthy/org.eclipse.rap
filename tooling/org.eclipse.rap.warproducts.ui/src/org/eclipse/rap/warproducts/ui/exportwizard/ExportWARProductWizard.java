package org.eclipse.rap.warproducts.ui.exportwizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.pde.internal.ui.wizards.exports.ProductExportWizard;


public class ExportWARProductWizard extends ProductExportWizard {


  private WARProductExportWizardPage page;
  
  
  public ExportWARProductWizard() {
    this( null );
  }

  public ExportWARProductWizard( final IProject project ) {
    super( project );
  }

  public void addPages() {
    page = new WARProductExportWizardPage( getSelection() );
    addPage( page );

  }
  
  public boolean canFinish() {
    return page.isPageComplete();
}

  
}
