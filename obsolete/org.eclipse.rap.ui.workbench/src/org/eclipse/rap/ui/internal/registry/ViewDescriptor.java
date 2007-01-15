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

package org.eclipse.rap.ui.internal.registry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.rap.ui.Activator;
import org.eclipse.rap.ui.IViewPart;
import org.eclipse.rap.ui.views.IViewDescriptor;


public class ViewDescriptor implements IViewDescriptor {

  private final IConfigurationElement element;
  private String id;
  
  public ViewDescriptor( final IConfigurationElement element )
    throws CoreException
  {
    this.element = element;
    loadFromExtension();
  }

  public boolean getAllowMultiple() {
    return false;
  }

  public String[] getCategoryPath() {
    throw new UnsupportedOperationException();
  }

  public String getDescription() {
    throw new UnsupportedOperationException();
  }

  public float getFastViewWidthRatio() {
    throw new UnsupportedOperationException();
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return element.getAttribute( IWorkbenchRegistryConstants.ATT_NAME );
  }

  public Object getAdapter( Class adapter ) {
    throw new UnsupportedOperationException();
  }
  
  public IViewPart createView() throws CoreException {
    Object extension 
      = Activator.createExtension( element, 
                                   IWorkbenchRegistryConstants.ATT_CLASS );
    return ( IViewPart )extension;
  }
  
  
  // ////////////////
  // helping methods
 
  private void loadFromExtension() throws CoreException {
    id = element.getAttribute( IWorkbenchRegistryConstants.ATT_ID );
  }
}
