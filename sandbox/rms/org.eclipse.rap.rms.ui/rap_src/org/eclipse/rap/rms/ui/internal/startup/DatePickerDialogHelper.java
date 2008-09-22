// Created on 02.10.2007
package org.eclipse.rap.rms.ui.internal.startup;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


public class DatePickerDialogHelper {
  
  private static final Color COLOR_WEEK_DAYS_BG
    = Graphics.getColor( 211, 231, 223 );
  private static final Color COLOR_WEEK_END_FG
    = Graphics.getColor( 255, 0, 0 );
  private static final Color COLOR_NAVIGATION_BG
    = Display.getDefault().getSystemColor( SWT.COLOR_LIST_SELECTION );
  private static final Color COLOR_NAVIGATION_FG
    = Graphics.getColor( 255, 255, 255 );

  private DatePickerDialogHelper() {
    // prevent instance creation
  }
  
  public static Color getWeekDaysBgColor() {
    return COLOR_WEEK_DAYS_BG;
  }

  public static Color getWeekEndFgColor() {
    return COLOR_WEEK_END_FG;
  }

  public static Color getNavigationBgColor() {
    return COLOR_NAVIGATION_BG;
  }
  
  public static Color getNavigationFgColor() {
      return COLOR_NAVIGATION_FG;
  }
}
