/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.rwt.internal.theme.css;

import org.w3c.css.sac.DocumentHandler;

public class ParserUtil {

  public static void handlePropertyString( final DocumentHandler documentHandler,
                                           final String propertyName,
                                           final String propertyString,
                                           final int propertyLine )
  {
    if( propertyString != null ) {
      String trimmedString = propertyString.trim();
      if( documentHandler instanceof DocumentHandlerExt ) {
        int length = trimmedString.length();
        if( trimmedString.charAt( length - 1 ) == ';' ) {
          trimmedString = trimmedString.substring( 0, length - 1 );
        }
        ( ( DocumentHandlerExt )documentHandler ).propertyString( propertyName,
                                                                  trimmedString );
        ( ( DocumentHandlerExt )documentHandler ).propertyLine( propertyName,
                                                                propertyLine );
      }
    }
  }
}
