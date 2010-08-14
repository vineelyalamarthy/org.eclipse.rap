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
package org.eclipse.rap.ui.themeeditor.scanner.region;

import org.eclipse.jface.text.IRegion;

/**
 * An IRegionExt is a logic unit within the theme file, so that the editor can
 * provide syntax color or content assists based on the structure of the regions
 * of the theme file.
 */
public interface IRegionExt extends IRegion {

  /**
   * Returns the type of a region. Each region implementation has its own type.
   */
  public int getTokenType();

  /**
   * Returns the keyword type. These are the ones supported by RAP. So this type
   * can be Selector, Property, Style, State, or default otherwise.
   */
  public int getKeywordType();

  /**
   * Returns the next state, namely the next instance of a region. Therefore an
   * implementation has to evaluate the given character and decide if it shall
   * switch the state (so instantiate an new region and return it) or if keeps
   * the current state (so just return itself).
   */
  public IRegionExt getNextState( final char character );

  /**
   * Returns a new instance of a region that has got the same type as the
   * current one. Used to continue with the same region type as before after a
   * comment region has finished.
   */
  public IRegionExt getCopy( final int offset );

  /**
   * Returns a String containing all characters that were given to evaluate.
   * This means the content is the part of text in the theme file from position
   * offset until position offset+length.
   */
  public String getContent();
}
