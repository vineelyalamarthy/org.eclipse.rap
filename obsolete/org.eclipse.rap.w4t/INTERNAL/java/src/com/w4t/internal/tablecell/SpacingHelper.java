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
package com.w4t.internal.tablecell;

import java.io.IOException;
import com.w4t.WebTableCell;

/** <p>A utility class for rendering in {@link org.eclipse.rwt.WebTableCell 
  * WebTableCell}s. Implementation of the abstract methods declared 
  * here provide additional rendering around the actual content of the 
  * cell. This can be used for cell-wise spacing, or border styles etc.</p>
  */
public abstract class SpacingHelper {

//  public abstract StringBuffer getSpacingStart( final WebTableCell cell );
  
  public abstract void getSpacingStart( final WebTableCell cell ) 
    throws IOException;

//  public abstract StringBuffer getSpacingEnd( final WebTableCell cell );
  
  public abstract void getSpacingEnd ( final WebTableCell cell ) 
    throws IOException;
}