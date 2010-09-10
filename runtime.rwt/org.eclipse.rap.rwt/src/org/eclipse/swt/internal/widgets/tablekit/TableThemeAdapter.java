/*******************************************************************************
 * Copyright (c) 2007, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

package org.eclipse.swt.internal.widgets.tablekit;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.widgets.controlkit.ControlThemeAdapter;
import org.eclipse.swt.widgets.Control;


public final class TableThemeAdapter extends ControlThemeAdapter {

  public Rectangle getCheckBoxMargin( final Control control ) {
    return getCssBoxDimensions( "Table-Checkbox", "margin", control );
  }

  public int getCheckBoxWidth( final Control control ) {
    return getCssDimension( "Table-Checkbox", "width", control );
  }

  public Point getCheckBoxImageSize( final Control control ) {
    return getCssImageDimension( "Table-Checkbox", "background-image", control );
  }

  public Rectangle getCellPadding( final Control control ) {
    return getCssBoxDimensions( "Table-Cell", "padding", control );
  }

  public int getCellSpacing( final Control control ) {
    return Math.max( 0, getCssDimension( "Table-Cell", "spacing", control ) );
  }

  public int getHeaderBorderBottomWidth( final Control control ) {
    return getCssBorderWidth( "TableColumn", "border-bottom", control );
  }

  public Rectangle getHeaderPadding( final Control control ) {
    return getCssBoxDimensions( "TableColumn", "padding", control );
  }

  public Font getHeaderFont( final Control control ) {
    return getCssFont( "TableColumn", "font", control );
  }
}
