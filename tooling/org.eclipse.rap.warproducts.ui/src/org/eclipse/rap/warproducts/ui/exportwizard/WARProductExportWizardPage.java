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
import org.eclipse.ui.PlatformUI;


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
  
  public void createControl( Composite parent ) {
    Composite container = new Composite( parent, SWT.NONE );
    container.setLayout( new FormLayout() );
    Group group = new Group( container, SWT.NONE );
    group.setText( "Export Options" );
    GridLayout layout = new GridLayout();
//    layout.verticalSpacing = 2;
    group.setLayout( layout );
    
    FormData fdGroup = new FormData();
    group.setLayoutData( fdGroup );
    fdGroup.left = new FormAttachment( 0 );
    fdGroup.top = new FormAttachment( 0 );
    fdGroup.right = new FormAttachment( 100 );
    
    createConfigurationSection( group );
    // createSynchronizationSection( container );
    createDestinationSection( group );
    // createOptionsSection( container );
    initialize();
    pageChanged();
    setControl( group );
    hookHelpContext( group );
    Dialog.applyDialogFont( group );
    PlatformUI.getWorkbench()
      .getHelpSystem()
      .setHelp( group, IHelpContextIds.PRODUCT_EXPORT_WIZARD );
    fPageInitialized = true;
  }

  private void createConfigurationSection( Composite parent ) {
    fConfigurationGroup = new WARProductConfigurationSection( this );
    fConfigurationGroup.createControl( parent );
  }

  private void createDestinationSection( Composite container ) {
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
    // String value = settings.get(S_SYNC_PRODUCT);
    // fSyncButton.setSelection(value == null ? true :
    // settings.getBoolean(S_SYNC_PRODUCT));
    //
    fExportGroup.initialize( settings, fConfigurationGroup.getProductFile() );
    //
    // fExportSourceButton.setSelection(settings.getBoolean(S_EXPORT_SOURCE));
    // fExportSourceCombo.setItems(new String[]
    // {PDEUIMessages.ExportWizard_generateAssociatedSourceBundles,
    // PDEUIMessages.ExportWizard_includeSourceInBinaryBundles});
    // String sourceComboValue = settings.get(S_EXPORT_SOURCE_FORMAT) != null ?
    // settings.get(S_EXPORT_SOURCE_FORMAT) :
    // PDEUIMessages.ExportWizard_generateAssociatedSourceBundles;
    // fExportSourceCombo.setText(sourceComboValue);
    // fExportSourceCombo.setEnabled(fExportSourceButton.getSelection());
    //
    // String selected = settings.get(S_EXPORT_METADATA);
    // fExportMetadata.setSelection(selected == null ? true :
    // Boolean.TRUE.toString().equals(selected));
    //
    // selected = settings.get(S_ALLOW_BINARY_CYCLES);
    // fAllowBinaryCycles.setSelection(selected == null ? true :
    // Boolean.TRUE.toString().equals(selected));
    //
    // if (fMultiPlatform != null)
    // fMultiPlatform.setSelection(settings.getBoolean(S_MULTI_PLATFORM));
    //
    // hookListeners();
  }

  protected void saveSettings( IDialogSettings settings ) {
    fConfigurationGroup.saveSettings( settings );
    // settings.put(S_SYNC_PRODUCT, fSyncButton.getSelection());
    fExportGroup.saveSettings( settings );
    // settings.put(S_EXPORT_SOURCE, doExportSource());
    // settings.put(S_EXPORT_SOURCE_FORMAT,
    // fExportSourceCombo.getItem(fExportSourceCombo.getSelectionIndex()));
    // settings.put(S_EXPORT_METADATA, doExportMetadata());
    // settings.put(S_ALLOW_BINARY_CYCLES, doBinaryCycles());
    //
    // if (fMultiPlatform != null)
    // settings.put(S_MULTI_PLATFORM, fMultiPlatform.getSelection());
  }

  protected void pageChanged() {
    if( getMessage() != null )
      setMessage( null );
    String error = fConfigurationGroup.validate();
    if( error == null )
      error = fExportGroup.validate();
    if( fPageInitialized )
      setErrorMessage( error );
    else
      setMessage( error );
    setPageComplete( error == null );
  }

  protected boolean doExportToDirectory() {
//    return fExportGroup.doExportToDirectory();
    return false;
  }

  protected String getFileName() {
    return fExportGroup.getFileName();
  }

  protected String getDestination() {
    return fExportGroup.getDestination();
  }
  
  

}
