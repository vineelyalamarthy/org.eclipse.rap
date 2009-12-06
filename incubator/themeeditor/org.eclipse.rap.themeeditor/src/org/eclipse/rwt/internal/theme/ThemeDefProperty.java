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

package org.eclipse.rwt.internal.theme;

public class ThemeDefProperty {

  public final String name;
  public final String description;
  public final String[] styles;
  public final String[] states;

  public ThemeDefProperty( final String name,
                           final String description,
                           final String[] styles,
                           final String[] states )
  {
    this.name = name;
    this.description = description;
    this.styles = styles;
    this.states = states;
  }
}
