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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.wizards.exports.AbstractExportWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;


public class ExportPage extends AbstractExportWizardPage {

  private DestinationGroup exportGroup;
  private ExportWARProductWizard wizard;

  public ExportPage() {
	super( "exportPage" );
    setTitle( "WAR product export" );
    setDescription( "Use the selected WAR product configuration to " +
    		        "export the WAR product as a WAR file.");
  }
  
  public void createControl( final Composite parent ) {
    Composite container = new Composite( parent, SWT.NONE );
    container.setLayout( new FormLayout() );
    Group group = new Group( container, SWT.NONE );
    group.setText( "Export Options" );
    GridLayout layout = new GridLayout();
    group.setLayout( layout );
    FormData fdGroup = new FormData();
    group.setLayoutData( fdGroup );
    fdGroup.left = new FormAttachment( 0 );
    fdGroup.top = new FormAttachment( 0 );
    fdGroup.right = new FormAttachment( 100 );
    createDestinationSection( group );
    initialize();
    pageChanged();
    setControl( group );
    Dialog.applyDialogFont( group );
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchHelpSystem helpSystem = workbench.getHelpSystem();
    helpSystem.setHelp( group, IHelpContextIds.PRODUCT_EXPORT_WIZARD );
  }


  private void createDestinationSection( final Composite container ) {
    wizard = ( ExportWARProductWizard )getWizard();
    exportGroup = new DestinationGroup( this );
    exportGroup.createControl( container );
  }

  protected void updateProductFields() {
    exportGroup.updateDestination( wizard.getProductFile() );
  }

  protected void initialize() {
    IDialogSettings settings = getDialogSettings();
    exportGroup.initialize( settings, wizard.getProductFile() );
  }

  protected void saveSettings( final IDialogSettings settings ) {
    exportGroup.saveSettings( settings );
  }

  protected void pageChanged() {
  }

  protected boolean doExportToDirectory() {
    return false;
  }

  protected String getFileName() {
    return exportGroup.getFileName();
  }

  protected String getDestination() {
    return exportGroup.getDestination();
  }
  
  protected boolean doExportSource() {
    return false;
  }
  
  protected boolean doExportSourceBundles() {
    return false;
  }
  
  protected boolean doBinaryCycles() {
    return true;
  }
  
  protected boolean doExportMetadata() {
    return false;
  }

  public IFile getProductFile() {
    return wizard.getProductFile();
  }

}
