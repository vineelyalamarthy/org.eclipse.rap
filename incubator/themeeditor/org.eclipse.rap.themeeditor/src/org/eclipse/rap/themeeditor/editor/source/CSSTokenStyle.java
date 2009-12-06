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

import org.eclipse.swt.graphics.RGB;

public class CSSTokenStyle {

  public RGB rgb;
  public int style;

  public CSSTokenStyle( final RGB rgb, final int style ) {
    this.rgb = rgb;
    this.style = style;
  }
}
