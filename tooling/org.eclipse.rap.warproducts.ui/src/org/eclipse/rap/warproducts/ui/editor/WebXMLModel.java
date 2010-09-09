/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.IWritable;
import org.eclipse.pde.internal.core.text.XMLEditingModel;
import org.xml.sax.helpers.DefaultHandler;


public class WebXMLModel extends XMLEditingModel {

  public WebXMLModel( final IDocument document, final boolean isReconciling ) {
    super( document, isReconciling );
  }

  protected DefaultHandler createDocumentHandler( final IModel model,
                                                  final boolean reconciling )
  {
    return null;
  }

  protected IWritable getRoot() {
    return null;
  }
  
}
