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

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class CSSDocumentSetupParticipant implements IDocumentSetupParticipant {

  public CSSDocumentSetupParticipant() {
  }

  public void setup( final IDocument document ) {
    if( document instanceof IDocumentExtension3 ) {
      IDocumentExtension3 extension3 = ( IDocumentExtension3 )document;
      IDocumentPartitioner partitioner = new FastPartitioner( new RuleBasedPartitionScanner(),
                                                              new String[]{} );
      extension3.setDocumentPartitioner( CSSSourceViewerConfiguration.CSS_PARTITIONING,
                                         partitioner );
      partitioner.connect( document );
    }
  }
}
