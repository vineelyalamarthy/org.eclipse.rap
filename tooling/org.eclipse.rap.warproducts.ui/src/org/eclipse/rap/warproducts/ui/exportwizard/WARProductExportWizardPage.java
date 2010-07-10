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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.wizards.exports.ProductExportWizardPage;
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


public class WARProductExportWizardPage extends ProductExportWizardPage {

  private WARProductConfigurationSection fConfigurationGroup;
  private IStructuredSelection fSelection;
  private boolean fPageInitialized;
  private WARProductDestinationGroup fExportGroup;

  public WARProductExportWizardPage( final IStructuredSelection selection ) {
    super( selection );
    fSelection = selection;
    setTitle( "WAR product" );
    setDescription( "Use an existing WAR product configuration to " +
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
    createConfigurationSection( group );
    createDestinationSection( group );
    initialize();
    pageChanged();
    setControl( group );
    hookHelpContext( group );
    Dialog.applyDialogFont( group );
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchHelpSystem helpSystem = workbench.getHelpSystem();
    helpSystem.setHelp( group, IHelpContextIds.PRODUCT_EXPORT_WIZARD );
    fPageInitialized = true;
  }

  private void createConfigurationSection( final Composite parent ) {
    fConfigurationGroup = new WARProductConfigurationSection( this );
    fConfigurationGroup.createControl( parent );
  }

  private void createDestinationSection( final Composite container ) {
    fExportGroup = new WARProductDestinationGroup( this );
    fExportGroup.createControl( container );
  }

  protected void updateProductFields() {
    fExportGroup.updateDestination( fConfigurationGroup.getProductFile() );
  }

  protected String getRootDirectory() {
    return fConfigurationGroup.getRootDirectory();
  }

  protected IFile getProductFile() {
    return fConfigurationGroup.getProductFile();
  }

  protected void initialize() {
    IDialogSettings settings = getDialogSettings();
    fConfigurationGroup.initialize( fSelection, settings );
    fExportGroup.initialize( settings, fConfigurationGroup.getProductFile() );
  }

  protected void saveSettings( final IDialogSettings settings ) {
    fConfigurationGroup.saveSettings( settings );
    fExportGroup.saveSettings( settings );
  }

  protected void pageChanged() {
    if( getMessage() != null ) {
      setMessage( null );
    }
    String error = fConfigurationGroup.validate();
    if( error == null ) {
      error = fExportGroup.validate();
    }
    if( fPageInitialized ) {
      setErrorMessage( error );
    } else {
      setMessage( error );
    }
    setPageComplete( error == null );
  }

  protected boolean doExportToDirectory() {
    return false;
  }

  protected String getFileName() {
    return fExportGroup.getFileName();
  }

  protected String getDestination() {
    return fExportGroup.getDestination();
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

}
