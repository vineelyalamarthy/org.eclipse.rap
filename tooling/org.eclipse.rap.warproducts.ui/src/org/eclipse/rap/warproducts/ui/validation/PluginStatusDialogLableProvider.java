/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.validation;

import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.rap.warproducts.core.validation.ValidationError;
import org.eclipse.swt.graphics.Image;

public class PluginStatusDialogLableProvider extends DefaultLabelProvider {

  public Image getImage( final Object element ) {
    PDELabelProvider pdeLabelProvider 
      = PDEPlugin.getDefault().getLabelProvider();
    Image result = null;
    if( element instanceof String ) {
      result = pdeLabelProvider.get( PDEPluginImages.DESC_ERROR_ST_OBJ );
    } else if( element instanceof ValidationError ) {
      result = getValidationErrorImage( element );
    } else {
      result = pdeLabelProvider.getImage( element );
    }
    return result;
  }

  private Image getValidationErrorImage( final Object element ) {
    PDELabelProvider pdeLabelProvider 
      = PDEPlugin.getDefault().getLabelProvider();
    Image result;
    ValidationError error = ( ValidationError )element;
    int type = error.getType();
    if( type == ValidationError.LIBRARY_MISSING 
        || type == ValidationError.LIBRARY_DOESNT_EXIST ) 
    {
      result = pdeLabelProvider.get( PDEPluginImages.DESC_JAR_LIB_OBJ );
    } else {
      result = pdeLabelProvider.get( PDEPluginImages.DESC_PLUGIN_OBJ );
    }
    return result;
  }
  
  public String getText( final Object element ) {
    return PDEPlugin.getDefault().getLabelProvider().getText( element );
  }
  
}