/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.exportwizard;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.core.TargetPlatformHelper;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.core.product.WorkspaceProductModel;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.wizards.exports.ProductExportWizard;
import org.eclipse.rap.warproducts.core.WARProductExportOperation;
import org.eclipse.rap.warproducts.core.WARWorkspaceProductModel;
import org.eclipse.ui.progress.IProgressConstants;
import org.osgi.framework.Version;

public class ExportWARProductWizard extends ProductExportWizard {

  private WARProductExportWizardPage page;
  private WorkspaceProductModel fProductModel;

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

  protected boolean performPreliminaryChecks() {
    boolean result = true;
    fProductModel = new WARWorkspaceProductModel( page.getProductFile(), false );
    try {
      fProductModel.load();
      if( !fProductModel.isLoaded() ) {
        MessageDialog.openError( getContainer().getShell(),
                                 PDEUIMessages.ProductExportWizard_error,
                                 PDEUIMessages.ProductExportWizard_corrupt ); 
        result = false;
      }
    } catch( final CoreException e ) {
      MessageDialog.openError( getContainer().getShell(),
                               PDEUIMessages.ProductExportWizard_error,
                               PDEUIMessages.ProductExportWizard_corrupt ); 
      result = false;
    }
    return result;
  }

  protected boolean confirmDelete() {
    boolean result = true;
    if( !page.doExportToDirectory() ) {
      File zipFile = new File( page.getDestination(), page.getFileName() );
      if( zipFile.exists() ) {
        String bind 
          = NLS.bind( PDEUIMessages.BaseExportWizard_confirmReplace_desc, 
                      zipFile.getAbsolutePath() );
        String confirmReplaceTitle 
          = PDEUIMessages.BaseExportWizard_confirmReplace_title;
        boolean openQuestion 
          = MessageDialog.openQuestion( getContainer().getShell(),
                                        confirmReplaceTitle,
                                        bind );
        if( !openQuestion ) {
          result = false;
        } else {
          zipFile.delete();
        }
      }
    }
    return result;
  }

  protected void scheduleExportJob() {
    FeatureExportInfo info = new FeatureExportInfo();
    info.toDirectory = page.doExportToDirectory();
    info.exportSource = false;
    info.exportSourceBundle = false;
    info.allowBinaryCycles = false;
    info.exportMetadata = false;
    info.destinationDirectory = page.getDestination();
    info.zipFileName = page.getFileName();
    info.items = getPluginModels();
    String rootDirectory = page.getRootDirectory();
    if( "".equals( rootDirectory.trim() ) ) //$NON-NLS-1$
      rootDirectory = "."; //$NON-NLS-1$
    WARProductExportOperation job 
      = new WARProductExportOperation( info,
                                       PDEUIMessages.ProductExportJob_name,
                                       fProductModel.getProduct(),
                                       rootDirectory );
    job.setUser( true );
    job.setRule( ResourcesPlugin.getWorkspace().getRoot() );
    job.schedule();
    job.setProperty( IProgressConstants.ICON_PROPERTY,
                     PDEPluginImages.DESC_FEATURE_OBJ );
  }

  private BundleDescription[] getPluginModels() {
    ArrayList list = new ArrayList();
    State state = TargetPlatformHelper.getState();
    IProduct product = fProductModel.getProduct();
    IProductPlugin[] plugins = product.getPlugins();
    for( int i = 0; i < plugins.length; i++ ) {
      BundleDescription bundle = null;
      String v = plugins[ i ].getVersion();
      if( v != null && v.length() > 0 ) {
        bundle = state.getBundle( plugins[ i ].getId(),
                                  Version.parseVersion( v ) );
      }
      // if there's no version, just grab a bundle like before
      if( bundle == null ) {
        bundle = state.getBundle( plugins[ i ].getId(), null );
      }
      if( bundle != null ) {
        list.add( bundle );
      }
    }
    Object[] bundleArray = list.toArray( new BundleDescription[ list.size() ] );
    return ( BundleDescription[] )bundleArray;
  }
  
}
