/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.exportwizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.internal.ui.util.FileExtensionFilter;
import org.eclipse.pde.internal.ui.wizards.exports.AbstractExportWizardPage;
import org.eclipse.rap.warproducts.ui.Messages;
import org.eclipse.rap.warproducts.ui.WARProductConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;


public class SelectionPage extends AbstractExportWizardPage {

  protected IFile warProductFile;

  public SelectionPage() {
    super( Messages.SelectionPageTitle );
    setTitle( Messages.SelectionPageTitle );
    setDescription( Messages.SelectionPageSelect +
    		        Messages.SelectionPageListProjects );
  }

  public void createControl( final Composite parent ) {
    Composite container = new Composite( parent, SWT.NONE );
    container.setLayout( new FormLayout() );
    int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
    TreeViewer viewer = new TreeViewer( container, style );
    viewer.setContentProvider( new WorkbenchContentProvider() );
    viewer.setLabelProvider( new WorkbenchLabelProvider() );
    viewer.setInput( ResourcesPlugin.getWorkspace() );
    FileExtensionFilter filter 
      = new FileExtensionFilter( WARProductConstants.FILE_EXTENSION );
    viewer.addFilter( filter );
    hookSelectionListener( viewer );
    FormData fdViewer = new FormData();
    viewer.getTree().setLayoutData( fdViewer );
    fdViewer.left = new FormAttachment( 0 );
    fdViewer.top = new FormAttachment( 0 );
    fdViewer.right = new FormAttachment( 100 );
    fdViewer.bottom = new FormAttachment( 100 );
    setControl( container );
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchHelpSystem helpSystem = workbench.getHelpSystem();
    String contextId = WARProductConstants.HELP_CONTEXT_SELECTION_PAGE;
    helpSystem.setHelp( container, contextId );
  }

  private void hookSelectionListener( final TreeViewer viewer ) {
    viewer.addSelectionChangedListener( new ISelectionChangedListener() {
      
      public void selectionChanged( final SelectionChangedEvent event ) {
        TreeSelection selection = ( TreeSelection )event.getSelection();
        Object firstElement = selection.getFirstElement();
        if( firstElement != null && firstElement instanceof IFile ) {
          IFile file = ( IFile )firstElement;
          String fileExtension = file.getFileExtension();
          if( fileExtension.equals( WARProductConstants.FILE_EXTENSION ) ) {
            warProductFile = file;
            ExportWARProductWizard wizard 
              = ( ExportWARProductWizard )getWizard();
            wizard.loadProductFromFile( file );
            wizard.getContainer().updateButtons();
          }
        } else {
          warProductFile = null;
        }
      }
      
    } );
  }

  protected void pageChanged() {

  }

  protected void saveSettings( final IDialogSettings settings ) {
  }
  
  public boolean isPageComplete() {
    return warProductFile != null;
  }
  
  public boolean canFlipToNextPage() {
    return isPageComplete();
  }
  
}
