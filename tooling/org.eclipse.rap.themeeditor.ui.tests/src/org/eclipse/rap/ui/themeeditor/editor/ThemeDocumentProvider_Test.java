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

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ThemeDocumentProvider_Test extends TestCase {

  public class MockEditorInput implements IFileEditorInput {

    private IFile cssFile;

    public MockEditorInput() {
      NullProgressMonitor nullMonitor = new NullProgressMonitor();
      IProject project = ResourcesPlugin.getWorkspace()
        .getRoot()
        .getProject( "foo" );
      cssFile = project.getFile( "foo.css" );
      try {
        project.create( nullMonitor );
        project.open( nullMonitor );
        cssFile.create( new ByteArrayInputStream( new byte[0] ), false, nullMonitor );
      } catch( CoreException e ) {
        e.printStackTrace();
      }
    }

    public Object getAdapter( Class adapter ) {
      return null;
    }

    public boolean exists() {
      return false;
    }

    public ImageDescriptor getImageDescriptor() {
      return null;
    }

    public String getName() {
      return null;
    }

    public IPersistableElement getPersistable() {
      return null;
    }

    public String getToolTipText() {
      return null;
    }

    public IStorage getStorage() throws CoreException {
      return null;
    }

    public IFile getFile() {
      return cssFile;
    }
  }
  class AccessibleThemeDocumentProvider extends ThemeDocumentProvider {

    public IDocument createDocument() throws CoreException {
      return super.createDocument( new MockEditorInput() );
    }
  };

  public void testGetPartitionScanner() throws Exception {
    ThemeDocumentProvider documentProvider = new ThemeDocumentProvider();
    documentProvider.connect( new MockEditorInput() );
    assertNotNull( documentProvider.getPartitionScanner() );
  }
}
