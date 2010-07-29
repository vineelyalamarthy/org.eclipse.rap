/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import java.io.File;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.ISortableContentOutlinePage;
import org.eclipse.pde.internal.ui.editor.context.InputContextManager;
import org.eclipse.pde.internal.ui.editor.product.ProductEditor;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.rap.warproducts.ui.WARProductConstants;
import org.eclipse.rap.warproducts.ui.validation.IValidationListener;
import org.eclipse.rap.warproducts.ui.validation.PluginStatusDialog;
import org.eclipse.rap.warproducts.ui.validation.WARProductValidateAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class WARProductEditor extends ProductEditor 
  implements IValidationListener 
{

  private static final String WARPRODUCT_FILE_EXTENSION = ".warproduct";
  private WARProductExportAction exportAction;

  protected void addEditorPages() {
    try {
      addPage( new OverviewPage( this ) );
      addPage( new ConfigurationPage( this, useFeatures() ) );
    } catch( final PartInitException e ) {
      PDEPlugin.logException( e );
    }
  }
  
  public String getContextIDForSaveAs() {
    return WARProductInputContext.CONTEXT_ID;
  }
  
  protected String getEditorID() {
    return WARProductConstants.EDITOR_ID;
  }

  public void contributeToToolbar( final IToolBarManager manager ) {
    IProductModel model = ( IProductModel )getAggregateModel();
    IProduct product = model.getProduct();
    WARProductValidateAction validationAction 
      = new WARProductValidateAction( ( IWARProduct )product );
    validationAction.addValidationListener( this );
    manager.add( validationAction );
    manager.add( getExportAction() );
  }

  private WARProductExportAction getExportAction() {
    if( exportAction == null ) {
      exportAction = new WARProductExportAction( this );
      exportAction.setToolTipText( "Export WAR product" );
      ImageDescriptor descExportProductTool 
        = PDEPluginImages.DESC_EXPORT_PRODUCT_TOOL;
      exportAction.setImageDescriptor( descExportProductTool );
    }
    return exportAction;
  }

  protected InputContextManager createInputContextManager() {
    return new WARProductInputContextManager( this );
  }

  protected void createSystemFileContexts( final InputContextManager manager,
                                           final FileStoreEditorInput input )
  {
    File file = new File( input.getURI() );
    if( file != null ) {
      String name = file.getName();
      if( name.endsWith( WARPRODUCT_FILE_EXTENSION ) ) { //$NON-NLS-1$
        IFileStore store;
        try {
          store = EFS.getStore( file.toURI() );
          IEditorInput in = new FileStoreEditorInput( store );
          manager.putContext( in, new WARProductInputContext( this, in, true ) );
        } catch( final CoreException e ) {
          PDEPlugin.logException( e );
        }
      }
    }
  }

  protected void createStorageContexts( final InputContextManager manager,
                                        final IStorageEditorInput input )
  {
    if( input.getName().endsWith( WARPRODUCT_FILE_EXTENSION ) ) { //$NON-NLS-1$
      WARProductInputContext context 
        = new WARProductInputContext( this, input, true );
      manager.putContext( input, context );
    }
  }

  protected void createResourceContexts( final InputContextManager manager,
                                         final IFileEditorInput input )
  {
    WARProductInputContext context 
      = new WARProductInputContext( this, input, true );
    manager.putContext( input, context );
    manager.monitorFile( input.getFile() );
  }
  
  public boolean useFeatures() {
    return false;
  }
  
  protected ISortableContentOutlinePage createContentOutline() {
    return new WARProductOutlinePage( this );
  }

  public void validationFinished( final Map errors ) {
    Shell shell = PDEPlugin.getActiveWorkbenchShell();
    if( !errors.isEmpty() ) {
      PluginStatusDialog dialog = new PluginStatusDialog( shell );
      dialog.setInput( errors );
      dialog.open();
    } else {
      String pluginValidationMessage 
        = PDEUIMessages.PluginStatusDialog_pluginValidation;
      String noProblemsMessage 
        = PDEUIMessages.AbstractLauncherToolbar_noProblems;
      MessageDialog.openInformation( shell,
                                     pluginValidationMessage,
                                     noProblemsMessage );
    }
  }
  
}
