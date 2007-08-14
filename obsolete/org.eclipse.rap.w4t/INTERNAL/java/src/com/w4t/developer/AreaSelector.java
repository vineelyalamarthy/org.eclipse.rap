/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.developer;

import com.w4t.*;


/**
 * A helping class for the WebLayoutManger in design time
 */
public class AreaSelector extends WebButton {

  private static final String AREASELECTORCONTROLFACTORYIMPL
    = "org.eclipse.rap.factory.AreaSelectorControlFactoryImpl";

  /** 
    * manages the mechanism for inserting and copying WebComponents 
    * to the Designer or showing RegionInspector 
    */
  private AreaSelectorControl areaSelectorControl = null;

  /** Constructor */
  public AreaSelector() {
    super();
    try {
      areaSelectorControl 
        = getAreaSelectorControlFactory().createAreaSelectorControl( this );
    } catch ( Exception ignored ) {
      // we proceed only, if the factory has been found, if not, we
      // are quiet and ignore the exception
    }
  }  
  
  private AreaSelectorControlFactory getAreaSelectorControlFactory() 
    throws Exception 
  {
    return ( AreaSelectorControlFactory )Class.forName( 
                                 AREASELECTORCONTROLFACTORYIMPL ).newInstance();
  }

  public void setRegion( final WebTableCell region ) {
    areaSelectorControl.setRegion( region );
  }

  public void setConstraint( final Object constraint ) {
    areaSelectorControl.setConstraint( constraint );
  }

  public void setContainer( final WebContainer container ) {
    areaSelectorControl.setContainer( container );
  }
}