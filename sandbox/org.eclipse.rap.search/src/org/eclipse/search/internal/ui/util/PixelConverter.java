/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.search.internal.ui.util;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;

public class PixelConverter {

  /**
   * Number of horizontal dialog units per character, value <code>4</code>.
   */
  private static final int HORIZONTAL_DIALOG_UNIT_PER_CHAR = 4;
  /**
   * Number of vertical dialog units per character, value <code>8</code>.
   */
  private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;
  // private FontMetrics fFontMetrics;
  private Font font;

  public PixelConverter( Control control ) {
    font = control.getFont();
    // GC gc = new GC(control);
    // gc.setFont(control.getFont());
    // fFontMetrics= gc.getFontMetrics();
    // gc.dispose();
  }

  /*
   * @see org.eclipse.jface.dialogs.DialogPage#convertHeightInCharsToPixels(int)
   */
  public int convertHeightInCharsToPixels( int chars ) {
    return Graphics.getCharHeight( font ) * chars;
    // return Dialog.convertHeightInCharsToPixels(fFontMetrics, chars);
  }

  /*
   * @see org.eclipse.jface.dialogs.DialogPage#convertHorizontalDLUsToPixels(int)
   */
  public int convertHorizontalDLUsToPixels( int dlus ) {
    return ( int )( Graphics.getAvgCharWidth( font ) * dlus + HORIZONTAL_DIALOG_UNIT_PER_CHAR / 2 )
           / HORIZONTAL_DIALOG_UNIT_PER_CHAR;
    // return Dialog.convertHorizontalDLUsToPixels(fFontMetrics, dlus);
  }

  /*
   * @see org.eclipse.jface.dialogs.DialogPage#convertVerticalDLUsToPixels(int)
   */
  public int convertVerticalDLUsToPixels( int dlus ) {
    return ( Graphics.getCharHeight( font ) * dlus + VERTICAL_DIALOG_UNITS_PER_CHAR / 2 )
           / VERTICAL_DIALOG_UNITS_PER_CHAR;
    // return Dialog.convertVerticalDLUsToPixels(fFontMetrics, dlus);
  }

  /*
   * @see org.eclipse.jface.dialogs.DialogPage#convertWidthInCharsToPixels(int)
   */
  public int convertWidthInCharsToPixels( int chars ) {
    return ( int )( Graphics.getAvgCharWidth( font ) * chars );
    // return Dialog.convertWidthInCharsToPixels(fFontMetrics, chars);
  }
}
