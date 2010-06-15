/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.ui.wizards.product.ProductFromConfigOperation;
import org.eclipse.rap.warproducts.core.InfrastructreCreator;


public class WARProductFromConfigOperation extends ProductFromConfigOperation {

  private IContainer productParent;

  public WARProductFromConfigOperation( final IFile file, 
                                        final ILaunchConfiguration config )
  {
    super( file, config );  
    productParent = file.getParent();
  }
  
  protected void initializeProduct( IProduct product ) {
    super.initializeProduct( product );
    InfrastructreCreator creator = new InfrastructreCreator( productParent );
    creator.createWebInf();
    creator.createLaunchIni();
    creator.createWebXml();
  }
}
