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

/**
 * Super class of all supported region types.
 */
public abstract class AbstractRegion implements IRegionExt {

  protected int offset;
  protected int length;
  protected char lastCharacter;
  private String content;

  public AbstractRegion( final int offset ) {
    this.offset = offset + 1;
    this.length = 0;
  }

  public int getOffset() {
    return offset;
  }

  public int getLength() {
    return length - 1;
  }

  public String getContent() {
    return content;
  }

  public void setContent( String content ) {
    this.content = content;
  }

  public String toString() {
    return "AbstractRegion [offset="
           + offset
           + ", length="
           + length
           + ", lastCharacter="
           + lastCharacter
           + ", content="
           + content
           + "]";
  }
  
  
}
