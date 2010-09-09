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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class ThemeViewerConfiguration extends TextSourceViewerConfiguration {

  public String[] getConfiguredContentTypes(
    final ISourceViewer sourceViewer )
  {
    return new String[]{
      IDocument.DEFAULT_CONTENT_TYPE, ThemePartitionScanner.CSS_COMMENT
    };
  }

  public IPresentationReconciler getPresentationReconciler(
    final ISourceViewer viewer )
  {
    PresentationReconciler reconciler = new PresentationReconciler();
    ThemePartitionScanner scanner = new ThemePartitionScanner();
    DefaultDamagerRepairer repairer = new DefaultDamagerRepairer( scanner ) {

      protected TextAttribute getTokenTextAttribute( IToken token ) {
        TextAttribute textAttribute = null;
        if( ThemePartitionScanner.CSS_COMMENT.equals( token.getData() ) ) {
          Color cyan = new Color( Display.getCurrent(), 63, 127, 95 );
          textAttribute = new TextAttribute( cyan );
        }
        return textAttribute;
      }
    };
    reconciler.setDamager( repairer, ThemePartitionScanner.CSS_COMMENT );
    reconciler.setRepairer( repairer, ThemePartitionScanner.CSS_COMMENT );
    return reconciler;
  }
  
}
