package org.eclipse.rap.themeeditor.editor.rule;
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


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * A basic JFace ContentProvider with no parent element and no children
 * elements.
 */
public class DefaultContentProvider extends ArrayContentProvider
  implements ITreeContentProvider
{

  public DefaultContentProvider() {
  }

  public Object[] getChildren( final Object parentElement ) {
    return new Object[ 0 ];
  }

  public Object getParent( final Object element ) {
    return null;
  }

  public boolean hasChildren( final Object element ) {
    return false;
  }
}
