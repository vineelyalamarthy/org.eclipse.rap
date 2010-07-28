/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

qx.Class.define( "org.eclipse.swt.custom.CTabItem", {
  extend : qx.ui.basic.Atom,

  construct : function( parent, canClose ) {
    this.base( arguments );
    if( parent.classname != "org.eclipse.swt.custom.CTabFolder" ) {
      this.error( "illegal parent, must be a CTabFolder" );
    }
    this._parent = parent;
    this.setAppearance( "ctab-item" );
    this.setVerticalChildrenAlign( qx.constant.Layout.ALIGN_MIDDLE );
    this.setHorizontalChildrenAlign( qx.constant.Layout.ALIGN_LEFT );
    this.setOverflow( qx.constant.Style.OVERFLOW_HIDDEN );
    this.setTabIndex( null );
    // Set the label part to 'html mode'
    this.setLabel( "(empty)" );
    this.getLabelObject().setMode( qx.constant.Style.LABEL_MODE_HTML );
    this.getLabelObject().setVerticalAlign( qx.constant.Layout.ALIGN_MIDDLE );
    this.setLabel( "" );
    this._selected = false;
    this._showClose = false;
    this._canClose = canClose;
    this._unselectedCloseVisible = true;
    this.updateForeground();
    this.updateBackground();
    this.updateBackgroundImage();
    this.updateBackgroundGradient();
    this.setTabPosition( parent.getTabPosition() );
    // TODO [rst] change when a proper state inheritance concept exists
    if( parent.hasState( "rwt_BORDER" ) ) {
      this.addState( "rwt_BORDER" );
    }
    this._closeButton = new qx.ui.basic.Image();
    this._closeButton.setAppearance( "ctab-close-button" );
    this._closeButton.setWidth( 20 );
    // TODO [rh] center image vertically in tab item
    this._closeButton.setHeight( "80%" );
    this._closeButton.addEventListener( "click", this._onClose, this );
    var wm = org.eclipse.swt.WidgetManager.getInstance();
    wm.setToolTip( this._closeButton,
                   org.eclipse.swt.custom.CTabFolder.CLOSE_TOOLTIP );
    this.add( this._closeButton );
    this._updateCloseButton();
    this.addEventListener( "mouseover", this._onMouseOver, this );
    this.addEventListener( "mouseout", this._onMouseOut, this );
    this.addEventListener( "click", this._onClick, this );
    this.addEventListener( "dblclick", this._onDblClick, this );
  },

  destruct : function() {
    this.removeEventListener( "mouseover", this._onMouseOver, this );
    this.removeEventListener( "mouseout", this._onMouseOut, this );
    this.removeEventListener( "click", this._onClick, this );
    this.removeEventListener( "dblclick", this._onDblClick, this );    
    this._closeButton.removeEventListener( "click", this._onClose, this );
    var wm = org.eclipse.swt.WidgetManager.getInstance();
    wm.setToolTip( this._closeButton, null );
    this._closeButton.dispose();
    this._closeButton = null;
  },

  statics : {
    STATE_OVER : "over",
    STATE_SELECTED : "selected",
    
    IMG_CLOSE : "widget/ctabfolder/close.gif",
    IMG_CLOSE_HOVER : "widget/ctabfolder/close_hover.gif"
  },

  members : {
    setTabPosition : function( tabPosition ) {
      if( tabPosition === "top" ) {
        this.addState( "barTop" );
      } else {
        this.removeState( "barTop" );
      }
    },

    setSelected : function( selected ) {
      this._selected = selected;
      var prevItem = this._getPrevItem();
      if( prevItem != null ) {
        if( selected ) {
          prevItem.addState( "nextSelected" );
        } else {
          prevItem.removeState( "nextSelected" );
        }
      }
      if( selected ) {
        this.addState( org.eclipse.swt.custom.CTabItem.STATE_SELECTED );
      } else {
        this.removeState( org.eclipse.swt.custom.CTabItem.STATE_SELECTED );
      }
      this.updateForeground();
      this.updateBackground();
      this.updateBackgroundImage();
      this.updateBackgroundGradient();
      this._updateCloseButton();
    },

    _getPrevItem : function() {
      var result = null;
      var children = this._parent.getChildren();
      for( var i = 0; i < children.length && children[ i ] != this; i++ ) {
        if( children[ i ].classname === "org.eclipse.swt.custom.CTabItem" ) {
          result = children[ i ];
        }
      }
      return result;
    },

    isSelected : function() {
      return this._selected;
    },
    
    setShowClose : function( value ) {
      this._showClose = value;
      this._updateCloseButton();
    },

    setUnselectedCloseVisible : function( value ) {
      this._unselectedCloseVisible = value;
      this._updateCloseButton();
    },

    updateForeground : function() {
      var color = this.isSelected()
                  ? this._parent.getSelectionForeground()
                  : this._parent.getTextColor();
      if( color != null ) {
        this.setTextColor( color );
      } else {
        this.resetTextColor();
      }
    },

    updateBackground : function() {
      var color = this.isSelected()
                  ? this._parent.getSelectionBackground()
                  : null;
      if( color != null ) {
        this.setBackgroundColor( color );
      } else {
        this.resetBackgroundColor();
      }
    },

    updateBackgroundImage : function() {
      var image = null;
      if( this.isSelected() ) {
        image = this._parent.getSelectionBackgroundImage();
      }
      if( image != null ) {
        this.setBackgroundImage( image );
      } else {
        this.resetBackgroundImage();
      }
    },

    updateBackgroundGradient : function() {
      var colors = null;
      var percents = null;
      if( this.isSelected() ) {
        colors = this._parent.getSelectionBackgroundGradientColors();
        percents = this._parent.getSelectionBackgroundGradientPercents();
      }
      var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
      widgetManager.setBackgroundGradient( this, colors, percents );
    },

    _updateCloseButton : function() {
      var visible = false;
      if( this._canClose || this._showClose ) {
        visible
          =  this.isSelected()
          || ( this._unselectedCloseVisible
               && this.hasState( org.eclipse.swt.custom.CTabItem.STATE_OVER ) );
      }
      this._closeButton.setVisibility( visible );
    },

    _onMouseOver : function( evt ) {
      this.addState( org.eclipse.swt.custom.CTabItem.STATE_OVER );
      if( evt.getTarget() == this._closeButton ) {
        this._closeButton.addState( org.eclipse.swt.custom.CTabItem.STATE_OVER );
      }
      this._updateCloseButton();
    },

    _onMouseOut : function( evt ) {
      this.removeState( org.eclipse.swt.custom.CTabItem.STATE_OVER );
      if( evt.getTarget() == this._closeButton ) {
        this._closeButton.removeState( org.eclipse.swt.custom.CTabItem.STATE_OVER );
      }
      this._updateCloseButton();
    },

    _onClick : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        if( evt.getTarget() != this._closeButton ) {
          evt.getTarget().getParent()._notifyItemClick( evt.getTarget() );
        }
      }
    },

    _onDblClick : function( evt ) {
      if( evt.getTarget() != this._closeButton ) {
        evt.getTarget().getParent()._notifyItemDblClick( evt.getTarget() );
      }
    },

    _onClose : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var req = org.eclipse.swt.Request.getInstance();
        var id = widgetManager.findIdByWidget( this );
        req.addEvent( "org.eclipse.swt.events.ctabItemClosed", id );
        req.send();
      }
    }
  }
} );
