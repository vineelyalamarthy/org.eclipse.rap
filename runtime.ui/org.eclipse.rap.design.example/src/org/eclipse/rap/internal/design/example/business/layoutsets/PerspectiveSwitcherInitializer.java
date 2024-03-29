/******************************************************************************* 
* Copyright (c) 2009, 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.internal.design.example.business.layoutsets;

import org.eclipse.rap.internal.design.example.ILayoutSetConstants;
import org.eclipse.rap.ui.interactiondesign.layout.model.ILayoutSetInitializer;
import org.eclipse.rap.ui.interactiondesign.layout.model.LayoutSet;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;


public class PerspectiveSwitcherInitializer implements ILayoutSetInitializer {  

  public void initializeLayoutSet( final LayoutSet layoutSet ) {
    String path = ILayoutSetConstants.IMAGE_PATH_BUSINESS;
    layoutSet.addImagePath( ILayoutSetConstants.PERSP_CLOSE, 
                            path + "close.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.PERSP_LEFT_ACTIVE, 
                            path + "perspective_left_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.PERSP_RIGHT_ACTIVE,  
                            path + "perspective_right_active.png" ); //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.PERSP_BG,  
                            path + "perspective_bg.png" );    //$NON-NLS-1$
    layoutSet.addImagePath( ILayoutSetConstants.PERSP_BG_ACTIVE, 
                            ILayoutSetConstants.IMAGE_PATH_BUSINESS 
                            + "perspective_bg_active.png" ); //$NON-NLS-1$
    FormData fdButton = new FormData();
    fdButton.top = new FormAttachment( 0, 9 );
    layoutSet.addPosition( ILayoutSetConstants.PERSP_BUTTON_POS, 
                           fdButton );
  }
}
