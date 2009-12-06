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
package org.eclipse.rap.themeeditor.editor.source.region;

/**
 * Interface implemented by Region Classes that provide access to a parent
 * region. This parent region is basically the selector, which a style, state or
 * variant belongs to.
 */
public interface IHasParentRegion {

  public void setParentRegion( final SelectorRegion region );

  public SelectorRegion getParentRegion();
}
