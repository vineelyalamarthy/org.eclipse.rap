/*******************************************************************************
 * Copyright (c) 2009, 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.swt.internal.widgets.treekit;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.widgets.controlkit.ControlThemeAdapter;
import org.eclipse.swt.widgets.Control;


public final class TreeThemeAdapter extends ControlThemeAdapter {

  public Rectangle getCheckBoxMargin( final Control control ) {
    return getCssBoxDimensions( "Tree-Checkbox", "margin", control );
  }

  public Point getCheckBoxImageSize( final Control control ) {
    return getCssImageDimension( "Tree-Checkbox", "background-image", control );
  }

  public Rectangle getCellPadding( final Control control ) {
    return getCssBoxDimensions( "Tree-Cell", "padding", control );
  }

  public int getCellSpacing( final Control control ) {
    return Math.max( 0, getCssDimension( "Tree-Cell", "spacing", control ) );
  }

  public int getHeaderBorderBottomWidth( final Control control ) {
    return getCssBorderWidth( "TreeColumn", "border-bottom", control );
  }

  public Rectangle getHeaderPadding( final Control control ) {
    return getCssBoxDimensions( "TreeColumn", "padding", control );
  }

  public int getIndentionWidth( Control control ) {
    return getCssDimension( "Tree-Indent", "width", control );
  }
}
