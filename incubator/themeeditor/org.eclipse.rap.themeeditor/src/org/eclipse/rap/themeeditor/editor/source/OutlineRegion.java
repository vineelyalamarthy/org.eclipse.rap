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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.rap.themeeditor.editor.source.region.SelectorRegion;

/**
 * Special type of region for Outline items. It's basically the region in the
 * text containing the whole selector list of a rule.
 */
public class OutlineRegion implements IRegion {

  private int offset;
  private int length;
  private String content;
  private List elements;

  public OutlineRegion( final int offset,
                        final int length,
                        final String content,
                        final List elements )
  {
    this.offset = offset;
    this.length = length;
    this.content = content;
    this.elements = elements;
  }

  public int getLength() {
    return length;
  }

  public int getOffset() {
    return offset;
  }

  public String getContent() {
    return content;
  }

  public String toString() {
    return content == null
                          ? ""
                          : content;
  }

  /**
   * Returns the array of selectors this outline region consists of. Namely the
   * selector list of a rule.
   */
  public SelectorRegion[] getElements() {
    List resultList = new ArrayList();
    Iterator it = elements.iterator();
    while( it.hasNext() ) {
      Object object = it.next();
      if( object instanceof SelectorRegion ) {
        resultList.add( ( SelectorRegion )object );
      }
    }
    SelectorRegion[] result = new SelectorRegion[ resultList.size() ];
    return ( SelectorRegion[] )resultList.toArray( result );
  }
}
