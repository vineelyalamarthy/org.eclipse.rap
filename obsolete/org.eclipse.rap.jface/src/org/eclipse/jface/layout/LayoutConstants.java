/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.layout;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.graphics.FontSizeCalculator;

/**
 * Contains various layout constants
 * 
 * @since 3.2
 */
public final class LayoutConstants {
	private static Point dialogMargins = null;
	private static Point dialogSpacing = null;
	private static Point minButtonSize = null;
	
	private static void initializeConstants() {
		if (dialogMargins != null) {
			return;
		}
		
		dialogFont = JFaceResources.getDialogFont();
		
//		GC gc = new GC(Display.getCurrent());
//		gc.setFont(JFaceResources.getDialogFont());
//		FontMetrics fontMetrics = gc.getFontMetrics();

		dialogMargins = new Point(convertHorizontalDLUsToPixels(/*fontMetrics,*/ IDialogConstants.HORIZONTAL_MARGIN),
				convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN));

		dialogSpacing = new Point(convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING),
				convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING));

		minButtonSize  = new Point(convertHorizontalDLUsToPixels( IDialogConstants.BUTTON_WIDTH), 0);
		
//		gc.dispose();
	}
	
	/**
	 * Returns the default dialog margins, in pixels
	 * 
	 * @return the default dialog margins, in pixels
	 */
    public static final Point getMargins() {
    	initializeConstants();
    	return dialogMargins;
    }

    /**
     * Returns the default dialog spacing, in pixels
     * 
     * @return the default dialog spacing, in pixels
     */
    public static final Point getSpacing() {
    	initializeConstants();
    	return dialogSpacing;
    }

    /**
     * Returns the default minimum button size, in pixels
     * 
     * @return the default minimum button size, in pixels
     */
    public static final Point getMinButtonSize() {
    	initializeConstants();
    	return minButtonSize;
    }
    
    // copied from DialogPage
    private static Font dialogFont;

    /**
     * Number of horizontal dialog units per character, value <code>4</code>.
     */
    private static final int HORIZONTAL_DIALOG_UNIT_PER_CHAR = 4;

    /**
     * Number of vertical dialog units per character, value <code>8</code>.
     */
    private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;
    
    private static int convertHorizontalDLUsToPixels(int dlus) {
        // test for failure to initialize for backward compatibility
//        if (fontMetrics == null) {
//			return 0;
//		}
//        return Dialog.convertHorizontalDLUsToPixels(fontMetrics, dlus);
        int avgCharWidth 
        = Math.round( FontSizeCalculator.getAvgCharWidth( dialogFont ) );
        int result = ( avgCharWidth * dlus + HORIZONTAL_DIALOG_UNIT_PER_CHAR / 2 )
        / HORIZONTAL_DIALOG_UNIT_PER_CHAR;
        return result;
    }
    
    private static int convertVerticalDLUsToPixels(int dlus) {
        // test for failure to initialize for backward compatibility
//        if (fontMetrics == null) {
//			return 0;
//		}
//        return Dialog.convertVerticalDLUsToPixels(fontMetrics, dlus);
        int charHeight = FontSizeCalculator.getCharHeight( dialogFont );
        int result = ( charHeight * dlus + VERTICAL_DIALOG_UNITS_PER_CHAR / 2 )
        / VERTICAL_DIALOG_UNITS_PER_CHAR;
        return result;
    }
}
