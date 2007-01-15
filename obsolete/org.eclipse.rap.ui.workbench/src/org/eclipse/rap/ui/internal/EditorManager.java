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

package org.eclipse.rap.ui.internal;


public class EditorManager {
  
  private final WorkbenchWindow window;
  private final WorkbenchPage page;
  private final EditorAreaHelper editorPresentation;

  public EditorManager( final WorkbenchWindow window, 
                        final WorkbenchPage page, 
                        final EditorAreaHelper editorPresentation )
  {
    this.window = window;
    this.page = page;
    this.editorPresentation = editorPresentation;
  }
}
