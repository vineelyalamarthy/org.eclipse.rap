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
package org.eclipse.rap.themeeditor.editor;

/**
 * Listener interface for GUI parts that want to be notified if the Outline
 * selection has been changed.
 */
public interface IOutlineSelectionChangedListener {

  public void outlineSelectionChanged( final int newIndex, final Object item );
}
