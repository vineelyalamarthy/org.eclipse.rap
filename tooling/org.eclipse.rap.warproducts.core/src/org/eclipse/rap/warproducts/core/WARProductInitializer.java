/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.rap.warproducts.core.validation.Validation;
import org.eclipse.rap.warproducts.core.validation.ValidationError;
import org.eclipse.rap.warproducts.core.validation.Validator;


public class WARProductInitializer {

  private IWARProduct product;

  public WARProductInitializer( final IWARProduct product ) {
    this.product = product;
  }

  public void initialize() {
    Validator validator = new Validator( product );
    Validation validation = validator.validate();
    if( !validation.isValid() ) {
      makeProductValid( validation.getErrors() );
    }
  }

  private void makeProductValid( final ValidationError[] errors ) {
    for( int i = 0; i < errors.length; i++ ) {
      ValidationError error = errors[ i ];
      if( error.getType() == ValidationError.BUNDLE_BANNED ) {
        IProductPlugin bundle = ( IProductPlugin )error.getCausingObject();
        removeBannedBundle( bundle );
      } else if( error.getType() == ValidationError.BUNDLE_MISSING ) {
        IProductPlugin bundle = ( IProductPlugin )error.getCausingObject();
        addRequiredBundle( bundle );
      } else if( error.getType() == ValidationError.LIBRARY_MISSING ) {
        addServletBridge();
      }
    }
  }

  private void removeBannedBundle( final IProductPlugin bundle ) {
    product.removePlugins( new IProductPlugin[] { bundle } );
  }
  
  private void addRequiredBundle( final IProductPlugin requiredBundle ) {
    ModelEntry entry = PluginRegistry.findEntry( requiredBundle.getId() );
    if( entry != null ) {
      WARProductModelFactory factory 
        = new WARProductModelFactory( product.getModel() );
      IProductPlugin bundle = factory.createPlugin();
      bundle.setId( requiredBundle.getId() );
      bundle.setVersion( requiredBundle.getVersion() );
      product.addPlugins( new IProductPlugin[] { bundle } );
    }
  }
  
  private void addServletBridge() {
    ModelEntry entry = PluginRegistry.findEntry( Validator.SERVLET_BRIDGE_ID );
    if( entry != null ) {
      IPluginModelBase[] targetModels = entry.getExternalModels();
      boolean foundBridge = false;
      for( int i = 0; i < targetModels.length && !foundBridge; i++ ) {
        IPluginModelBase bridgeModel = targetModels[ i ];
        String location = bridgeModel.getInstallLocation();
        if( location != null && location.indexOf( ".jar" ) != -1 ) {
          IPath bridgePath = new Path( location );
          product.addLibrary( bridgePath );
          foundBridge = true;
        }
      }
    }
  }
  
}
