/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.pde.internal.core.iproduct.*;
import org.eclipse.pde.internal.ui.editor.FormOutlinePage;
import org.eclipse.pde.internal.ui.editor.PDEFormEditor;

public class WARProductOutlinePage extends FormOutlinePage {

  public WARProductOutlinePage( final PDEFormEditor editor ) {
    super( editor );
  }

  public void sort( final boolean sorting ) {
  }

  protected Object[] getChildren( final Object parent ) {
    Object[] result = new Object[ 0 ];
    if( parent instanceof ConfigurationPage ) {
      ConfigurationPage page = ( ConfigurationPage )parent;
      IProduct product = ( ( IProductModel )page.getModel() ).getProduct();
      result =  product.getPlugins();
    }
    return result;
  }

  protected String getParentPageId( final Object item ) {
    String result = super.getParentPageId( item );
    if( item instanceof IProductPlugin ) {
      result = ConfigurationPage.PLUGIN_ID;
    }
    if( item instanceof IProductFeature ) {
      result = ConfigurationPage.FEATURE_ID;
    }
    return result;
  }
  
}