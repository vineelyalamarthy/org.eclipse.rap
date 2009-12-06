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
package org.eclipse.rap.themeeditor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Manager for holding instance of SWT Color object, and has the responsibility
 * to dispose them when method dispose() is called.
 */
public class ColorRegistry {

  private Display display;
  private Map colorMap;

  public ColorRegistry( final Display display ) {
    colorMap = new HashMap();
    this.display = display;
  }

  public Color getColor( final RGB rgb ) {
    Color result = ( Color )colorMap.get( rgb );
    if( result == null ) {
      result = new Color( display, rgb );
      colorMap.put( rgb, result );
    }
    return result;
  }

  public void dispose() {
    Iterator iterator = colorMap.values().iterator();
    while( iterator.hasNext() ) {
      Color color = ( Color )iterator.next();
      color.dispose();
    }
  }
}
