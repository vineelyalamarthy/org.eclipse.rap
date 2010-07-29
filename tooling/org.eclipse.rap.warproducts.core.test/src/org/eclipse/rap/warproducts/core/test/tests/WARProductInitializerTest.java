/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core.test.tests;

import junit.framework.TestCase;

import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.rap.warproducts.core.WARProductInitializer;
import org.eclipse.rap.warproducts.core.WARProductModel;
import org.eclipse.rap.warproducts.core.WARProductModelFactory;
import org.eclipse.rap.warproducts.core.validation.Validation;
import org.eclipse.rap.warproducts.core.validation.ValidationError;
import org.eclipse.rap.warproducts.core.validation.Validator;


public class WARProductInitializerTest extends TestCase {
  
  public void testValidInitialization() {
    WARProductModel model = new WARProductModel();
    WARProductModelFactory factory = new WARProductModelFactory( model );
    IWARProduct product = ( IWARProduct )factory.createProduct();
    IProductPlugin javaxServletBundle = factory.createPlugin();
    javaxServletBundle.setId( "javax.servlet" );
    javaxServletBundle.setVersion( "0.0.0" );
    product.addPlugins( new IProductPlugin[] { javaxServletBundle } );
    WARProductInitializer initializer = new WARProductInitializer( product );
    initializer.initialize();
    Validator validator = new Validator( product );
    Validation validation = validator.validate();
    ValidationError[] errors = validation.getErrors();
    for( int i = 0; i < errors.length; i++ ) {
      System.out.println( i + " - " + errors[ i ].getMessage() );
    }
    assertTrue( validation.isValid() );
  }
  
}
