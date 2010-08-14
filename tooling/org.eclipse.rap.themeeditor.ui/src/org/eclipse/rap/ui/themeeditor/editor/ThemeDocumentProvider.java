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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.rap.ui.themeeditor.scanner.ThemePartitionScanner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class ThemeDocumentProvider extends FileDocumentProvider {

  protected IDocument createDocument( Object element ) throws CoreException {
    IDocument document = super.createDocument( element );
    String[] contentTypes = new String[]{
      IDocument.DEFAULT_CONTENT_TYPE,
    };
    ThemePartitionScanner scanner = new ThemePartitionScanner();
    IDocumentPartitioner partitioner = new FastPartitioner( scanner,
                                                            contentTypes );
    partitioner.connect( document );
    document.setDocumentPartitioner( partitioner );
    return document;
  }
}
