/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor.editor;

import org.eclipse.rap.ui.themeeditor.editor.outline.ThemeOutlinePage;
import org.eclipse.rap.ui.themeeditor.scanner.Scanner;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class ThemeEditor extends TextEditor {

  private IContentOutlinePage outlinePage;

  protected void initializeEditor() {
    super.initializeEditor();
    setSourceViewerConfiguration( new ThemeViewerConfiguration() );
    setDocumentProvider( new ThemeDocumentProvider() );
  }

  public Object getAdapter( Class adapter ) {
    Object result;
    if( IContentOutlinePage.class.equals( adapter ) ) {
      if( outlinePage == null ) {
        outlinePage = new ThemeOutlinePage( getScanner() );
      }
      result = outlinePage;
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  private Scanner getScanner() {
    ThemeDocumentProvider documentProvider = ( ThemeDocumentProvider )getDocumentProvider();
    Scanner scanner = documentProvider.getPartitionScanner().getScanner();
    return scanner;
  }
  
}
