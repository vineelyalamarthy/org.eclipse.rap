/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

qx.Class.define( "org.eclipse.swt.widgets.Shell", {
  extend : qx.ui.window.Window,

  construct : function() {
    this.base( arguments );
    this.setOverflow( qx.constant.Style.OVERFLOW_HIDDEN );
    // Note: This prevents a laoyut-glitch on the ipad:
    this.setRestrictToPageOnOpen( false );
    // TODO [rh] HACK to set mode on Label that shows the caption, _captionTitle
    //      is a 'protected' field on class Window
    this._captionTitle.setMode( "html" );
    this._activeControl = null;
    this._focusControl = null;
    this._activateListenerWidgets = new Array();
    this._parentShell = null;
    this._renderZIndex = true;
    // TODO [rh] check whether these listeners must be removed upon disposal
    this.addEventListener( "changeActiveChild", this._onChangeActiveChild );
    this.addEventListener( "changeFocusedChild", this._onChangeFocusedChild );
    this.addEventListener( "changeActive", this._onChangeActive );
    this.addEventListener( "changeMode", this._onChangeMode );
    this.addEventListener( "changeLeft", this._onChangeLocation );
    this.addEventListener( "changeTop", this._onChangeLocation );
    this.addEventListener( "changeWidth", this._onChangeSize );
    this.addEventListener( "changeHeight", this._onChangeSize );
    this.addEventListener( "keydown", this._onKeydown );
    var req = org.eclipse.swt.Request.getInstance();
    req.addEventListener( "send", this._onSend, this );
    this.getCaptionBar().setWidth( "100%" );
    // [if] Listen for DOM event instead of qooxdoo event - see bug 294846.
    this.removeEventListener( "mousedown", this._onwindowmousedown );
    this.addEventListener( "create", this._onCreate, this );
    this.__onwindowmousedown
      = qx.lang.Function.bind( this._onwindowmousedown, this );
  },

  statics : {
    TOP_LEFT : "topLeft",
    TOP_RIGHT : "topRight",
    BOTTOM_LEFT : "bottomLeft",
    BOTTOM_RIGHT : "bottomRight",
    CORNER_NAMES : [
      "topLeft",
      "topRight",
      "bottomLeft",
      "bottomRight"
    ],

    _onParentClose : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        this.doClose();
      }
    },

    _appendCloseRequestParam : function( shell ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var id = widgetManager.findIdByWidget( shell );
        var req = org.eclipse.swt.Request.getInstance();
        req.addEvent( "org.eclipse.swt.widgets.Shell_close", id );
      }
    },

    reorderShells : function( vWindowManager ) {
      var shells = qx.lang.Object.getValues( vWindowManager.getAll() );
      shells = shells.sort( org.eclipse.swt.widgets.Shell._compareShells );
      var vLength = shells.length;
      var upperModalShell = null;
      if( vLength > 0 ) {
        var vTop = shells[ 0 ].getTopLevelWidget();
        var vZIndex = org.eclipse.swt.widgets.Shell.MIN_ZINDEX;
        for( var i = 0; i < vLength; i++ ) {
          vZIndex += 10;
          shells[ i ].setZIndex( vZIndex );
          if( shells[ i ]._appModal ) {
            upperModalShell = shells[ i ];
          }
        }
        if( upperModalShell != null ) {
          this._copyStates( upperModalShell, vTop._getBlocker() );
          vTop._getBlocker().show();
          vTop._getBlocker().setZIndex( upperModalShell.getZIndex() - 1 );
        } else {
          vTop._getBlocker().hide();
        }
      }
      org.eclipse.swt.widgets.Shell._upperModalShell = upperModalShell;
    },
    
    _copyStates : function( source, target ) {
      target.__states = {};
      for( var state in source.__states ) {
        if( source._isRelevantState( state ) ) {
          target.addState( state );
        }
      }
      target._renderAppearance();
      qx.ui.core.Widget.removeFromGlobalLayoutQueue( target );
    },

    /*
     * Compares two Shells regarding their desired z-order.
     *
     * Result is
     * - positive if sh1 is higher
     * - negative if sh2 is higher
     * - zero if equal
     */
    _compareShells : function( sh1, sh2 ) {
      var result = 0;
      // check for dialog relationship
      if( sh1.isDialogOf( sh2 ) ) {
        result = 1;
      } else if( sh2.isDialogOf( sh1 ) ) {
        result = -1;
      }
      // compare by onTop property
      if( result == 0 ) {
        result = ( sh1._onTop ? 1 : 0 ) - ( sh2._onTop ? 1 : 0 );
      }
      // compare by appModal property
      if( result == 0 ) {
        result = ( sh1._appModal ? 1 : 0 ) - ( sh2._appModal ? 1 : 0 );
      }
      // compare by top-level parent's z-order
      if( result == 0 ) {
        var top1 = sh1.getTopLevelShell();
        var top2 = sh2.getTopLevelShell();
        result = top1.getZIndex() - top2.getZIndex();
      }
      // compare by actual z-order
      if( result == 0 ) {
        result = sh1.getZIndex() - sh2.getZIndex();
      }
      return result;
    },

    MIN_ZINDEX : 1e5,

    MAX_ZINDEX : 1e7
  },

  destruct : function() {
    this.setParentShell( null );
    this.removeEventListener( "changeActiveChild", this._onChangeActiveChild );
    this.removeEventListener( "changeFocusedChild", this._onChangeFocusedChild );
    this.removeEventListener( "changeActive", this._onChangeActive );
    this.removeEventListener( "changeMode", this._onChangeMode );
    this.removeEventListener( "changeLeft", this._onChangeLocation );
    this.removeEventListener( "changeTop", this._onChangeLocation );
    this.removeEventListener( "changeWidth", this._onChangeSize );
    this.removeEventListener( "changeHeight", this._onChangeSize );
    this.removeEventListener( "keydown", this._onKeydown );
    var req = org.eclipse.swt.Request.getInstance();
    req.removeEventListener( "send", this._onSend, this );
    if( this.isCreated() ) {
      qx.html.EventRegistration.removeEventListener( this.getElement(),
                                                     "mousedown",
                                                     this.__onwindowmousedown );
    }
    this._activateListenerWidgets = null;
  },

  events : {
    "close" : "qx.event.type.DataEvent"
  },

  members : {
    _onCreate : function( evt ) {
      qx.html.EventRegistration.addEventListener( this.getElement(),
                                                  "mousedown",
                                                  this.__onwindowmousedown );
      this.removeEventListener( "create", this._onCreate, this );
    },
    
    // [if] Override to prevent the new open shell to automaticaly become
    // an active shell (see bug 297167).
    _beforeAppear : function() {
      qx.ui.layout.CanvasLayout.prototype._beforeAppear.call( this );
      qx.ui.popup.PopupManager.getInstance().update();
      var activeWindow = this.getWindowManager().getActiveWindow();
      this.getWindowManager().add( this );
      this.getWindowManager().setActiveWindow( activeWindow );
    },
    
    setDefaultButton : function( value ) {
      this._defaultButton = value;
    },

    getDefaultButton : function() {
      return this._defaultButton;
    },

    setParentShell : function( parentShell ) {
      var oldParentShell = this._parentShell;
      this._parentShell = parentShell;
      var listener = org.eclipse.swt.widgets.Shell._onParentClose;
      if( oldParentShell != null ) {
        oldParentShell.removeEventListener( "close", listener, this );
      }
      if( parentShell != null ) {
        parentShell.addEventListener( "close", listener, this );
      }
    },
    
    // Fix for bug 306042
    setSpace : function( left, width, top, height ) {
      if( !this._disableResize ) {
        this.setLeft( left );
        this.setWidth( width );
        this.setTop( top );
        this.setHeight( height );
      }
    },

    setHasShellListener : function( hasListener ) {
      this._hasShellListener = hasListener;
    },

    setActiveControl : function( control ) {
      this._activeControl = control;
    },

    /** To be called after rwt_XXX states are set */
    initialize : function() {
      this.setShowCaption( this.hasState( "rwt_TITLE" ) );
      this._onTop = ( this._parentShell != null && this._parentShell._onTop )
                    || this.hasState( "rwt_ON_TOP" );
      this._appModal = this.hasState( "rwt_APPLICATION_MODAL" );
    },

    // TODO [rst] Find a generic solution for state inheritance
    addState : function( state ) {
      this.base( arguments, state );
      if( this._isRelevantState( state ) ) {
        this._captionBar.addState( state );
        this._minimizeButton.addState( state );
        this._maximizeButton.addState( state );
        this._restoreButton.addState( state );
        this._closeButton.addState( state );
        var blocker = this._getClientDocumentBlocker();
        if( blocker != null ) {
          blocker.addState( state );
        }
      }
    },

    removeState : function( state ) {
      this.base( arguments, state );
      if( this._isRelevantState( state ) ) {
        this._captionBar.removeState( state );
        this._minimizeButton.removeState( state );
        this._maximizeButton.removeState( state );
        this._restoreButton.removeState( state );
        this._closeButton.removeState( state );
        var blocker = this._getClientDocumentBlocker();
        if( blocker != null ) {
          blocker.removeState( state );
        }
      }
    },
    
    _getClientDocumentBlocker : function() {
      var result = null;
      if(    this._appModal 
          && org.eclipse.swt.widgets.Shell._upperModalShell == this ) 
      {
        result = this.getTopLevelWidget()._getBlocker();
      }
      return result;
    },
    
    _isRelevantState : function( state ) {
      var result =    state == "active"
                   || state == "maximized"
                   || state == "minimized"
                   || state.substr( 0, 8 ) == "variant_"
                   || state.substr( 0, 4 ) == "rwt_";
      return result;
    },

    /**
     * Overrides qx.ui.window.Window#close()
     *
     * Called when user tries to close the shell.
     */
    close : function() {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        org.eclipse.swt.widgets.Shell._appendCloseRequestParam( this );
        if( this._hasShellListener ) {
          org.eclipse.swt.Request.getInstance().send();
        } else {
          this.doClose();
        }
      }
    },

    /**
     * Really closes the shell.
     */
    doClose : function() {
      // Note [rst]: Fixes bug 232977
      // Background: There are situations where a shell is disposed twice, thus
      // doClose is called on an already disposed shell at the second time
      if( !this.isDisposed() ) {
        this.hide();
        if( this.hasEventListeners( "close" ) ) {
          var event = new qx.event.type.DataEvent( "close", this );
          this.dispatchEvent( event, true );
        }
        var wm = this.getWindowManager();
        org.eclipse.swt.widgets.Shell.reorderShells( wm );
      }
    },

    /**
     * Adds a widget that has a server-side ActivateListener. If this widget or
     * any of its children are activated, an org.eclipse.swt.events.controlActivated
     * is fired.
     */
    addActivateListenerWidget : function( widget ) {
      this._activateListenerWidgets.push( widget );
    },

    removeActivateListenerWidget : function( widget ) {
      qx.lang.Array.remove( this._activateListenerWidgets, widget );
    },

    _isRelevantActivateEvent : function( widget ) {
      var result = false;
      for( var i = 0; !result && i < this._activateListenerWidgets.length; i++ )
      {
        var listeningWidget = this._activateListenerWidgets[ i ];
        if(    !listeningWidget.contains( this._activeControl )
            && listeningWidget.contains( widget ) )
        {
          result = true;
        }
      }
      return result;
    },

    _onChangeActiveChild : function( evt ) {
      // Work around qooxdoo bug #254: the changeActiveChild is fired twice when
      // a widget was activated by keyboard (getData() is null in this case)
      var widget = this._getParentControl( evt.getValue() );
      if( !org_eclipse_rap_rwt_EventUtil_suspend && widget != null ) {
        var widgetMgr = org.eclipse.swt.WidgetManager.getInstance();
        var id = widgetMgr.findIdByWidget( widget );
        var shellId = widgetMgr.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        if( this._isRelevantActivateEvent( widget ) ) {
          this._activeControl = widget;
          req.removeParameter( shellId + ".activeControl" );
          req.addEvent( "org.eclipse.swt.events.controlActivated", id );
          req.send();
        } else {
          req.addParameter( shellId + ".activeControl", id );
        }
      }
    },
    
    _onChangeFocusedChild : function( evt ) {
      if( org_eclipse_rap_rwt_EventUtil_suspend ) {
        this._focusControl = this.getFocusedChild();
      }
    },

    _onChangeActive : function( evt ) {
      // TODO [rst] This hack is a workaround for bug 345 in qooxdoo, remove this
      //      block as soon as the bug is fixed.
      //      See http://bugzilla.qooxdoo.org/show_bug.cgi?id=345
      if( !this.getActive() && !isFinite( this.getZIndex() ) ) {
        this.setZIndex( 1e8 );
        // TODO [rst] Obsoleted by rewrite. Let the warning here for safety.
        this.warn( "--- INFINITE Z-ORDER ---" );
      }
      // end of workaround
      if( !org_eclipse_rap_rwt_EventUtil_suspend && this.getActive() ) {
        var widgetMgr = org.eclipse.swt.WidgetManager.getInstance();
        var id = widgetMgr.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        req.addEvent( "org.eclipse.swt.events.shellActivated", id );
        req.send();
      }
      var active = evt.getValue();
      if( active ) {
        // workaround: Do not activate Shells that are blocked by a modal Shell
        var modalShell = org.eclipse.swt.widgets.Shell._upperModalShell;
        if( modalShell != null && modalShell.getZIndex() > this.getZIndex() ) {
          this.setActive( false );
          modalShell.setActive( true );
        }
        // end of workaround
      }
    },

    _onChangeMode : function( evt ) {
      var value = evt.getValue();
      var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
      var id = widgetManager.findIdByWidget( evt.getTarget() );
      var req = org.eclipse.swt.Request.getInstance();
      req.addParameter( id + ".mode", value );
    },
        
    _onChangeSize : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var req = org.eclipse.swt.Request.getInstance();
        var id = widgetManager.findIdByWidget( evt.getTarget() );
        var height = evt.getTarget().getHeightValue();
        var width = evt.getTarget().getWidthValue();
        req.addParameter( id + ".bounds.height", height );
        req.addParameter( id + ".bounds.width", width );
        req.send();
      }
    },

    _onChangeLocation : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var req = org.eclipse.swt.Request.getInstance();
        var id = widgetManager.findIdByWidget( evt.getTarget() );
        var left = evt.getTarget().getLeft();
        var top = evt.getTarget().getTop();
        if( !isNaN( left ) && !isNaN( top ) ) {
          req.addParameter( id + ".bounds.x", left );
          req.addParameter( id + ".bounds.y", top );
        }
      req.send();
      }
    },
    
    _onKeydown : function( evt ) {
      var keyId = evt.getKeyIdentifier();
      if(    keyId == "Enter"
          && !evt.isShiftPressed()
          && !evt.isAltPressed()
          && !evt.isCtrlPressed()
          && !evt.isMetaPressed() )
      {
        var defButton = this.getDefaultButton();
        if( defButton != null && defButton.isSeeable() ) {
          defButton.execute();
        }
      } else if( keyId == "Escape" && this._parentShell != null ) {
        this.close();
      }
    },

    _onSend : function( evt ) {
      if( this.getActive() ) {
        var focusedChild = this.getFocusedChild();
        if( focusedChild != null && focusedChild != this._focusControl ) {
          this._focusControl = focusedChild;
          var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
          var focusedChildId = widgetManager.findIdByWidget( focusedChild );
          var req = org.eclipse.swt.Request.getInstance();
          req.addParameter( req.getUIRootId() + ".focusControl", focusedChildId );    
        }
      }
    },

    /**
     * Returns the parent Control for the given widget. If widget is a Control
     * itself, the widget is returned. Otherwise its parent is returned or null
     * if there is no parent
     */
    _getParentControl : function( widget ) {
      var widgetMgr = org.eclipse.swt.WidgetManager.getInstance();
      var result = widget;
      while( result != null && !widgetMgr.isControl( result ) ) {
        if( result.getParent ) {
          result = result.getParent();
        } else {
          result = null;
        }
      }
      return result;
    },

    /**
     * Returns true if the receiver is a dialog shell of the given parent shell,
     * directly or indirectly.
     */
    isDialogOf : function( shell ) {
      var result = false;
      var parentShell = this._parentShell;
      while( !result && parentShell != null ) {
        result = shell === parentShell;
        parentShell = parentShell._parentShell;
      }
      return result;
    },

    /**
     * Returns the top-level shell if the receiver is a dialog or the shell
     * itself if it is a top-level shell.
     */
    getTopLevelShell : function() {
      var result = this;
      while( result._parentShell != null ) {
        result = result._parentShell;
      }
      return result;
    },

    /* TODO [rst] Revise when upgrading: overrides the _sendTo() function in
     *      superclass Window to allow for always-on-top.
     *      --> http://bugzilla.qooxdoo.org/show_bug.cgi?id=367
     */
    _sendTo : function() {
      org.eclipse.swt.widgets.Shell.reorderShells( this.getWindowManager() );
    },

    /*
     * Overwrites Popup#bringToFront
     */
    bringToFront : function() {
      var targetShell = this;
      while( targetShell._parentShell != null ) {
        targetShell = targetShell._parentShell;
      }
      this._setRenderZIndex( false );
      this.setZIndex( org.eclipse.swt.widgets.Shell.MAX_ZINDEX + 1 );
      targetShell.setZIndex( org.eclipse.swt.widgets.Shell.MAX_ZINDEX + 1 );
      org.eclipse.swt.widgets.Shell.reorderShells( this.getWindowManager() );
      this._setRenderZIndex( true );
    },
    
    _applyZIndex : function( newValue, oldValue ) {
      if( this._renderZIndex ) {
        this.base( arguments, newValue, oldValue );
      }
    },
    
    _setRenderZIndex : function( value ) {
       // Needed to prevent flickering during display-overlay animations.
      this._renderZIndex = value;
      if( value ) {
        this._applyZIndex( this.getZIndex() );
      }
    },

    /*
     * Overwrites Popup#sendToBack
     */
    sendToBack : function() {
      var targetShell = this;
      while( targetShell._parentShell != null ) {
        targetShell = targetShell._parentShell;
      }
      this._setRenderZIndex( false );
      this.setZIndex( org.eclipse.swt.widgets.Shell.MIN_ZINDEX - 1 );
      targetShell.setZIndex( org.eclipse.swt.widgets.Shell.MIN_ZINDEX - 1 );
      org.eclipse.swt.widgets.Shell.reorderShells( this.getWindowManager() );
      this._setRenderZIndex( true );
    },

    /*
     * E X P E R I M E N T A L
     * (for future PRIMARY_MODAL support)
     */
    setBlocked : function( blocked ) {
      if( blocked ) {
        if( !this._blocker ) {
          this._blocker = new qx.ui.layout.CanvasLayout();
          this._blocker.setAppearance( "client-document-blocker" );
          this.add( this._blocker );
        }
        this._blocker.setSpace( 0, 0, 10000, 10000 );
        this._blocker.setZIndex( 1000 );
      } else {
        if( this._blocker ) {
          this.remove( this._blocker );
          this._blocker.destroy();
          this._blocker = null;
        }
      }
    },
    
    setFullScreen : function( fullScreen ) {
      if( fullScreen ) {
        this._captionBar.setDisplay( false );
      } else {
        this._captionBar.setDisplay( this.hasState( "rwt_TITLE" ) );
      }
    }
  }
} );
