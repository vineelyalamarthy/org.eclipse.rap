/*******************************************************************************
 * Copyright (c) 2002-2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.gmaps;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;


public class GMap extends Composite {
  
  private String address = "";

  public GMap( final Composite parent, final int style ) {
    super( parent, style );
  }


  public String getAddress() {
    return address;
  }
  
  public void setAddress( final String address ) {
    if( address == null ) {
      this.address = "";
    } else {
      this.address = address;
    }
  }
  
  public void setLayout( final Layout layout ) {
  }
}
