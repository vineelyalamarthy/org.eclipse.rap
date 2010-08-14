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
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.rap.ui.themeeditor.scanner.ThemePartitionScanner;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class ThemeViewerConfiguration extends TextSourceViewerConfiguration {

  public String[] getConfiguredContentTypes( final ISourceViewer sourceViewer )
  {
    return new String[]{
      IDocument.DEFAULT_CONTENT_TYPE
    };
  }

  public IPresentationReconciler getPresentationReconciler( final ISourceViewer viewer )
  {
    PresentationReconciler reconciler = new PresentationReconciler();
    DefaultDamagerRepairer repairer = new DefaultDamagerRepairer( new ThemePartitionScanner() );
    reconciler.setDamager( repairer, IDocument.DEFAULT_CONTENT_TYPE );
    reconciler.setRepairer( repairer, IDocument.DEFAULT_CONTENT_TYPE );
    return reconciler;
  }
}
