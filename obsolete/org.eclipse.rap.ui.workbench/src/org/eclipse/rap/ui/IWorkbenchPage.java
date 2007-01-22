/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.ui;

public interface IWorkbenchPage extends IPartService, ISelectionService {
  IWorkbenchWindow getWorkbenchWindow();
  IViewReference[] getViewReferences();
  String getLabel();
  IViewPart findView( String viewId );
  IViewReference findViewReference( String viewId );
  IViewReference findViewReference( String viewId, String secondaryId );
  IWorkbenchPartReference getReference( IWorkbenchPart part );
  boolean isPartVisible( IWorkbenchPart part );
  public void toggleZoom( IWorkbenchPartReference ref );
}
