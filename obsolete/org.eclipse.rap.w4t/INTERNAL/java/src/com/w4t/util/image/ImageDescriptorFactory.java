/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t.util.image;

import com.w4t.types.WebColor;


/** <p>Factory for {@link org.eclipse.rwt.internal.util.image.ImageDescriptor 
  * <code>ImageDescriptor</code>} objects.</p>
  */
public class ImageDescriptorFactory {
  
  /** <p>Returns a new ImageDescriptor.</p> */
  public static ImageDescriptor create( final String label, 
                                        final WebColor color, 
                                        final WebColor bgColor, 
                                        final String fontFamily, 
                                        final int fontSize, 
                                        final String fontWeight, 
                                        final String fontStyle ) {
    return new ImageDescriptorImpl( label,
                                    color,
                                    bgColor,                                    
                                    fontFamily,
                                    fontSize,
                                    fontWeight,
                                    fontStyle );
  }
}
