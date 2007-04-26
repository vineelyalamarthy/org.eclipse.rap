/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.*;

public abstract class PartSite implements IWorkbenchPartSite {

  private String extensionID;
  private ISelectionProvider selectionProvider;
  private IWorkbenchPartReference partReference;
  private IWorkbenchPart part;
  private IWorkbenchPage page;
  private SubActionBars actionBars;

  public PartSite( final IWorkbenchPartReference ref,
                   final IWorkbenchPart part,
                   final IWorkbenchPage page )
  {
    this.partReference = ref;
    this.part = part;
    this.page = page;
  }

  public void setSelectionProvider( final ISelectionProvider provider ) {
    this.selectionProvider = provider;
  }

  public ISelectionProvider getSelectionProvider() {
    return selectionProvider;
  }

  public String getId() {
    return extensionID;
  }

  public IWorkbenchPartReference getPartReference() {
    return partReference;
  }

  public PartPane getPane() {
    return ( ( WorkbenchPartReference )partReference ).getPane();
  }

  /////////////
  // ActionBars
  
  public IActionBars getActionBars() {
    return actionBars;
  }

  public void setActionBars( final SubActionBars bars ) {
    actionBars = bars;
  }

  public void activateActionBars( final boolean forceVisibility ) {
    if( actionBars != null ) {
      actionBars.activate( forceVisibility );
    }
  }

  public void deactivateActionBars( final boolean forceHide ) {
    if( actionBars != null ) {
      actionBars.deactivate( forceHide );
    }
  }
  
  //////////////////////////////////////////
  // Methods to be used by inherited classes
  
  protected void setId( final String id ) {
    extensionID = id;
  }
}
