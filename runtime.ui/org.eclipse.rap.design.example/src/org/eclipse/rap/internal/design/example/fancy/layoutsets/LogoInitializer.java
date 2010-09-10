/******************************************************************************* 
* Copyright (c) 2009, 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.internal.design.example.fancy.layoutsets;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;


public class LogoInitializer implements ILayoutSetInitializer {

  public void initializeLayoutSet( final LayoutSet layoutSet ) {
    layoutSet.addImagePath( ILayoutSetConstants.LOGO, 
                            ILayoutSetConstants.IMAGE_PATH_FANCY + "logo.png" ); //$NON-NLS-1$
    
    // positions
    FormData fdLogo = new FormData();
    fdLogo.right = new FormAttachment( 100, -30 );
    fdLogo.top = new FormAttachment( 0, 57 );
    layoutSet.addPosition( ILayoutSetConstants.LOGO_POSITION, fdLogo );
  }
}
