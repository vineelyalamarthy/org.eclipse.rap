/*******************************************************************************
 * Copyright (c) 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
appearances = {
// BEGIN TEMPLATE //
    
    /*
    ---------------------------------------------------------------------------
      STYLED TEXT
    ---------------------------------------------------------------------------
    */

    "styled-text" : {
      style : function( states ) {
        var tv = new org.eclipse.swt.theme.ThemeValues( states );
        return {          
          textColor : tv.getCssColor( "StyledText", "color" ),
          backgroundColor : tv.getCssColor( "StyledText", "background-color" ),
          font : tv.getCssFont( "StyledText", "font" ),
          padding : tv.getCssBoxDimensions( "StyledText", "padding" ),
          border : tv.getCssBorder( "StyledText", "border" )
        };
      }
    }
    
// END TEMPLATE //
};