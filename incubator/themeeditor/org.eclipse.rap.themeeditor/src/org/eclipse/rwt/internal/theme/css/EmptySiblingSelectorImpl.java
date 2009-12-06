/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.rwt.internal.theme.css;

import org.w3c.css.sac.*;

public class EmptySiblingSelectorImpl implements SiblingSelector, SelectorExt {

  private final SimpleSelector directAdjacent;
  private final Selector child;
  private short nodeType;

  public EmptySiblingSelectorImpl( final short nodeType,
                                   final Selector child,
                                   final SimpleSelector directAdjacent )
  {
    this.directAdjacent = directAdjacent;
    this.child = child;
    this.nodeType = nodeType;
  }

  public Selector getSelector() {
    return child;
  }

  public SimpleSelector getSiblingSelector() {
    return directAdjacent;
  }

  public short getSelectorType() {
    return SAC_DIRECT_ADJACENT_SELECTOR;
  }

  public String[] getClasses() {
    return null;
  }

  public String getElementName() {
    return ( ( SelectorExt )child ).getElementName();
  }

  public boolean matches( final Element element ) {
    return false;
  }

  public int getSpecificity() {
    return 0;
  }

  public String toString() {
    return child.toString() + " + " + directAdjacent.toString();
  }

  public short getNodeType() {
    return nodeType;
  }
}
