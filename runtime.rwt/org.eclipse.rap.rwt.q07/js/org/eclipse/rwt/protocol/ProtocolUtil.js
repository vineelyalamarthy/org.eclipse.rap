/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/


org.eclipse.rwt.protocol.util = {};

org.eclipse.rwt.protocol.util.convertStyleArrayToObject 
  = function( styleArray ) 
{
  var StyleObject = function() {
    var styleObject = {};
    for( var i = 0; i < styleArray.length; i++ ) {
      styleObject[ styleArray[ i ] ] = i;
    }      
    return styleObject;
  };
  return new StyleObject();
}