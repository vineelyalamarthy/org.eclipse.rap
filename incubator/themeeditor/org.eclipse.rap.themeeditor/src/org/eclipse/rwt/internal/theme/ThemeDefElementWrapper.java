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
package org.eclipse.rwt.internal.theme;

import java.util.ArrayList;
import java.util.List;


public class ThemeDefElementWrapper {

  public final ThemeDefElement element;
  public final ThemeDefElementWrapper parent;
  private List children;

  public ThemeDefElementWrapper( final ThemeDefElement element,
                                 final ThemeDefElementWrapper parent )
  {
    this.element = element;
    this.parent = parent;
    children = new ArrayList();
  }

  public void addChildElement( final ThemeDefElementWrapper childElement ) {
    children.add( childElement );
  }

  public Object[] getChildren() {
    return children.toArray();
  }
}
