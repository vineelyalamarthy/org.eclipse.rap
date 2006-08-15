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

import com.w4t.WebContainer;
import com.w4t.WebTableCell;

/**
 * interface for the mechanism for inserting and copying WebComponents
 * or showing RegionInspector
 */
public interface AreaSelectorControl {

  void setRegion( WebTableCell region );
  
  void setConstraint( Object constraint );
  
  void setContainer( WebContainer container );
}
