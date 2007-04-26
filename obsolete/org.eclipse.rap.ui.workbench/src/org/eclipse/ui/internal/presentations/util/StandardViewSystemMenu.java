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

package org.eclipse.ui.internal.presentations.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;


public class StandardViewSystemMenu implements ISystemMenu {

  public StandardViewSystemMenu( IStackPresentationSite site ) {
  }

  public void show( final Control control, 
                    final Point displayCoordinates, 
                    final IPresentablePart currentSelection )
  {
    throw new UnsupportedOperationException();
  }
}
