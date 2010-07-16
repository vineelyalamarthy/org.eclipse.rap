/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

'use strict';

org.eclipse.rwt.protocol.controlStatusUtil = ( function() {
  
  return {
    
    synchronize : function( widget, payload ) {
      if( 'zIndex' in payload ) {
        widget.setZIndex( payload[ 'zIndex' ] );
      }
      if( 'visibility' in payload ) {
        widget.setVisibility( payload[ 'visibility' ] );
      }
      if( 'backgroundImage' in payload ) {
        widget.setBackgroundImage( payload[ 'backgroundImage' ] );
      }
    }
    
  }
  
}() )