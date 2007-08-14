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
package com.w4t.dhtml;



/** <p>A MenuItem is an entry in a dropdown menu that is visible when the
  * user selects a {@link org.eclipse.rwt.dhtml.Menu Menu} and can be clicked in 
  * order to trigger some action.</p>
  */
public class MenuItem extends Leaf {

  /** <p>tooltip displayed on mouseover with this MenuItem.</p> */
  private String title = "";
  
  
  /** <p>constructs a new MenuItem.</p> */
  public MenuItem() {
    super();
  }

  /** <p>constructs  a new MenuItem with the specified item label.</p> */
  public MenuItem( final String label ) {
    this();
    this.label = label;
  }
  

  // attribute getters and setters
  ////////////////////////////////
  
  /** <p>sets the tooltip displayed on mouseover with this MenuItem.</p> */
  public void setTitle( final String title ) {
    this.title = title;
  }
  
  /** <p>returns the tooltip displayed on mouseover with this MenuItem.</p> */
  public String getTitle() {
    return title;
  }
}

