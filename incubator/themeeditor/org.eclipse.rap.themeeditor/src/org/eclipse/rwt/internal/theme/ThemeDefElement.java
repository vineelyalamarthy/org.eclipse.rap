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

import java.util.Map;

public class ThemeDefElement {

  public final String name;
  public final String description;
  public final ThemeDefProperty[] properties;
  public final Map styleMap;
  public final Map stateMap;

  public ThemeDefElement( final String name,
                          final String description,
                          final ThemeDefProperty[] properties,
                          final Map styleMap,
                          final Map stateMap )
  {
    this.name = name;
    this.description = description;
    this.properties = properties;
    this.styleMap = styleMap;
    this.stateMap = stateMap;
  }
  
  
  
}
