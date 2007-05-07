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

package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


public class EditorSashContainer extends PartSashContainer {
  
  private static final String DEFAULT_WORKBOOK_ID = "DefaultEditorWorkbook";
  
  private List editorWorkbooks = new ArrayList(3); 
  
  public EditorSashContainer( final String editorId, 
                              final WorkbenchPage page, 
                              final Composite parent )
  {
    super( editorId, page, parent );
    createDefaultWorkbook();
  }

  protected EditorStack createDefaultWorkbook() {
    EditorStack newWorkbook = EditorStack.newEditorWorkbook( this, page );
    newWorkbook.setID( DEFAULT_WORKBOOK_ID );
    add( newWorkbook );
    return newWorkbook;
  }
  
  protected Composite createParent( final Composite parentWidget ) {
    return new Composite( parentWidget, SWT.NONE );
  }
  
  public boolean isCompressible() {
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.internal.PartSashContainer#isStackType(org.eclipse.ui.internal.LayoutPart)
   */
  public boolean isStackType(LayoutPart toTest) {
      return (toTest instanceof EditorStack);
  }
  
  /**
   * Find the sashs around the specified part.
   */
  public void findSashes(LayoutPart pane, PartPane.Sashes sashes) {
      //Find the sashes around the current editor and
      //then the sashes around the editor area.
      super.findSashes(pane, sashes);

      ILayoutContainer container = getContainer();
      if (container != null) {
          container.findSashes(this, sashes);
      }
  }
}
