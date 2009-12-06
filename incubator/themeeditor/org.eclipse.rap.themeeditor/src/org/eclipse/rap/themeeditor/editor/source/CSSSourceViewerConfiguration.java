/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor.editor.source;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class CSSSourceViewerConfiguration extends TextSourceViewerConfiguration
{

  public final static String CSS_PARTITIONING = "__css_partitioning";
  private CSSTokenScanner tokenScanner;

  public CSSSourceViewerConfiguration( final CSSTokenScanner tokenScanner ) {
    super();
    this.tokenScanner = tokenScanner;
  }

  public String getConfiguredDocumentPartitioning( final ISourceViewer sourceViewer )
  {
    return CSS_PARTITIONING;
  }

  public String[] getConfiguredContentTypes( final ISourceViewer sourceViewer )
  {
    return new String[]{
      IDocument.DEFAULT_CONTENT_TYPE
    };
  }

  public IPresentationReconciler getPresentationReconciler( final ISourceViewer sourceViewer )
  {
    PresentationReconciler result = new PresentationReconciler();
    result.setDocumentPartitioning( getConfiguredDocumentPartitioning( sourceViewer ) );
    CSSDamagerRepairer repairer = new CSSDamagerRepairer( tokenScanner );
    result.setDamager( repairer, IDocument.DEFAULT_CONTENT_TYPE );
    result.setRepairer( repairer, IDocument.DEFAULT_CONTENT_TYPE );
    return result;
  }

  public ITextHover getTextHover( final ISourceViewer sourceViewer,
                                  final String contentType )
  {
    return new CSSTextHover( tokenScanner );
  }

  public IContentAssistant getContentAssistant( final ISourceViewer sourceViewer )
  {
    ContentAssistant assistant = new ContentAssistant();
    assistant.setContentAssistProcessor( new CSSCompletionProcessor( tokenScanner ),
                                         IDocument.DEFAULT_CONTENT_TYPE );
//    assistant.setContentAssistProcessor( new JavaDocCompletionProcessor(),
//                                         JavaPartitionScanner.JAVA_DOC );
    assistant.enableAutoActivation( true );
    assistant.setAutoActivationDelay( 500 );
    assistant.setProposalPopupOrientation( IContentAssistant.PROPOSAL_OVERLAY );
    assistant.setContextInformationPopupOrientation( IContentAssistant.CONTEXT_INFO_ABOVE );
//    assistant.setContextInformationPopupBackground(JavaEditorEnvironment.getJavaColorProvider().getColor(new RGB(150, 150, 0)));
    assistant.setInformationControlCreator( new IInformationControlCreator() {

      public IInformationControl createInformationControl( Shell parent ) {
        return new DefaultInformationControl( parent );
      }
      
    });
    return assistant;
  }
}
