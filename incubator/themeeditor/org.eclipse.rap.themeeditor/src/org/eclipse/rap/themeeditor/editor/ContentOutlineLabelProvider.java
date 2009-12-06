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
package org.eclipse.rap.themeeditor.editor;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.rap.themeeditor.ThemeEditorPlugin;
import org.eclipse.rwt.internal.theme.css.StyleRule;
import org.eclipse.swt.graphics.Image;

/**
 * JFace LabelProvider for elements displayed in Outline view.
 */
public class ContentOutlineLabelProvider extends LabelProvider {

  public String getText( final Object element ) {
    String result;
    if( element instanceof StyleRule ) {
      result = ( ( StyleRule )element ).getName();
    } else {
      result = element == null
                              ? ""
                              : element.toString();
    }
    return result;
  }

  public Image getImage( final Object element ) {
    return ThemeEditorPlugin.getDefault()
      .getImage( ThemeEditorPlugin.IMG_PUBLIC );
  }
}
