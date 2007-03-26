/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.ui.internal;

import org.eclipse.rap.ui.*;
import org.eclipse.rap.ui.views.IViewDescriptor;

public class ViewSite extends PartSite implements IViewSite {

  public ViewSite( final ViewReference reference,
                   final IViewPart view,
                   final WorkbenchPage page,
                   final IViewDescriptor desc )
  {
    super( reference, view, page );
    setId( reference.getId() );
  }

  public String getSecondaryId() {
    return ( ( IViewReference )getPartReference() ).getSecondaryId();
  }
}
