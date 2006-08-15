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
package com.w4t.webtextareakit;

import com.w4t.*;

final class WebTextAreaUtil {

  private WebTextAreaUtil() {
    // prevent instantiation
  }

  static String getValue( final WebTextArea wta ) {
    String value = wta.getValue();
    if( wta.getFormatter() != null ) {
      value = ConverterUtil.format( wta.getFormatter(), value );
    }
    value = RenderUtil.resolve( value );
    return value;
  }
}
