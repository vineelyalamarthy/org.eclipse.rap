/*******************************************************************************
 * Copyright (c) 2009 EclipseSource Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.rap.warproducts.core.validation.Validation;
import org.eclipse.rap.warproducts.core.validation.ValidationError;
import org.eclipse.rap.warproducts.core.validation.Validator;

public class WARProductValidateAction extends Action {

  IWARProduct product;

  public WARProductValidateAction( final IWARProduct product ) {
    super( "Vaidate WAR product", IAction.AS_PUSH_BUTTON );
    setImageDescriptor( PDEPluginImages.DESC_VALIDATE_TOOL );
    this.product = ( IWARProduct )product;
  }

  public void run() {
    Validator validator = new Validator( product );
    Validation validation = validator.validate();
    System.out.println( "===========================" );
    if( validation.isValid() ) {
      System.out.println( "Product is valid" );
    } else {
      ValidationError[] errors = validation.getErrors();
      for( int i = 0; i < errors.length; i++ ) {
        System.out.println( i + " - " + errors[ i ].getMessage() );
      }
    }
    System.out.println( "===========================" );
  }

}
