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

import org.eclipse.rap.ui.internal.presentations.PresentationFactoryUtil;

public class EditorStack extends PartStack {

  private EditorSashContainer editorArea;
  private WorkbenchPage page;
 
  public EditorStack( final EditorSashContainer editorArea,
                      final WorkbenchPage page )
  {
    super( PresentationFactoryUtil.ROLE_EDITOR );
    this.editorArea = editorArea;
    setID( this.toString() );
    // TODO: [fappel] copied from original EditorStack implementation...
    // Each folder has a unique ID so relative positioning is unambiguous.
    // save off a ref to the page
    // @issue is it okay to do this??
    // I think so since a ViewStack is
    // not used on more than one page.
    this.page = page;
  }
  
  /**
   * Factory method for editor workbooks.
   */
  public static EditorStack newEditorWorkbook(
    final EditorSashContainer editorArea,
    final WorkbenchPage page )
  {
    return new EditorStack( editorArea, page );
  }
  
  protected WorkbenchPage getPage() {
    return page;
  }
  
  
  ////////////////////////////
  // implementations PartStack

  public boolean supportsState( int state ) {
//    if( page.isFixedLayout() ) {
//      return false;
//    }
    return true;
  }
}
