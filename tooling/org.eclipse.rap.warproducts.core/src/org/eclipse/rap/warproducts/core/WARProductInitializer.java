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
    // TODO: remove passed bundle form product
  }
  
  private void addRequiredBundle( final IProductPlugin bundle ) {
    // TODO: copy passed bundle and add it to product with the right model (not the fakemodel)
  }
  
  private void addServletBridge() {
    // TODO: search servletbridge.jar in workspace and target, add the path to the libraries
  }
  
}
