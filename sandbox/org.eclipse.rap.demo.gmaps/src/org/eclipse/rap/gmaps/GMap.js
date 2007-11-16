/*******************************************************************************
 * Copyright (c) 2002-2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
 
qx.Class.define( "org.eclipse.rap.gmaps.GMap", {
  extend: qx.ui.layout.CanvasLayout,
  
  construct: function( id ) {
    this.base( arguments );
    this.setHtmlAttribute( "id", id );
    this._id = id;
    this._map = null;
    if( GBrowserIsCompatible() ) {
      this._geocoder = new GClientGeocoder();
      this.addEventListener( "changeHeight", this._doResize, this );
      this.addEventListener( "changeWidth", this._doResize, this ); 
      //this.addEventListener( "changeAddress", this.load, this ); 
    }
  },
  
  properties : {
    address : {
      init : "",
      apply : "load"
    }
  },
  
  destruct : function() {
    
  },
  
  members : {
    
    _doActivate : function() {
      var shell = null;
      var parent = this.getParent();
      while( shell == null && parent != null ) {
        if( parent.classname == "org.eclipse.swt.widgets.Shell" ) {
          shell = parent;
        }
        parent = parent.getParent();
      }
      if( shell != null ) {
        shell.setActiveChild( this );
      }
    },
    
    load : function() {
      var current = this.getAddress();
      if( GBrowserIsCompatible() && current != null && current != "" ) {
        qx.ui.core.Widget.flushGlobalQueues();
        if( this._map == null ) {
          this._map = new GMap2( document.getElementById( this._id ) );
          this._map.addControl( new GSmallMapControl() );
          this._map.addControl( new GMapTypeControl() );
          GEvent.bind( this._map, "click", this, this._doActivate );
          
        }
        var map = this._map;
        map.clearOverlays();
        this._geocoder.getLatLng(
          current,
          function( point ) {
            if( !point ) {
              alert( "'" + current + "' not found" );
            } else {
              map.setCenter( point, 13 );
              var marker = new GMarker( point );
              map.addOverlay( marker );
              marker.openInfoWindowHtml( current );
            }
          }
        );
      }
    },
    
    _doResize : function() {
      qx.ui.core.Widget.flushGlobalQueues();
      if( this._map != null ) {
        this._map.checkResize();
      }
    }
  }
} );