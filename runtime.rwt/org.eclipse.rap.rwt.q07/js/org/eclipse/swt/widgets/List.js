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

/**
 * This class extends qx.ui.form.List to make its API more SWT-like.
 */
qx.Class.define( "org.eclipse.swt.widgets.List", {
  extend : qx.ui.form.List,

  construct : function( multiSelection ) {
    this.base( arguments );
    this.setMarkLeadingItem( true );
    // Should changeSelection events passed to the server-side?
    // state == no, action == yes
    this._changeSelectionNotification = "state";
    this._topIndex = 0;
    var selMgr = this.getManager();
    selMgr.addEventListener( "changeLeadItem", this._onChangeLeadItem, this );
    selMgr.addEventListener( "changeSelection", this._onSelectionChange, this );
    selMgr.setMultiSelection( multiSelection );
    selMgr.setDragSelection( false );
    this.addEventListener( "focus", this._onFocusIn, this );
    this.addEventListener( "blur", this._onFocusOut, this );
    this.addEventListener( "click", this._onClick, this );
    this.addEventListener( "dblclick", this._onDblClick, this );
    this.addEventListener( "appear", this._onAppear, this );
    // Listen to send event of request to report topIndex
    var req = org.eclipse.swt.Request.getInstance();
    req.addEventListener( "send", this._onSendRequest, this );
  },
  
  destruct : function() {
    var req = org.eclipse.swt.Request.getInstance();
    req.removeEventListener( "send", this._onSendRequest, this );
    var selMgr = this.getManager();
    selMgr.removeEventListener( "changeLeadItem", this._onChangeLeadItem, this );
    selMgr.removeEventListener( "changeSelection", this._onSelectionChange, this );
    this.removeEventListener( "focus", this._onFocusIn, this );
    this.removeEventListener( "blur", this._onFocusOut, this );
    this.removeEventListener( "click", this._onClick, this );
    this.removeEventListener( "dblclick", this._onDblClick, this );
    this.removeEventListener( "appear", this._onAppear, this );
  },

  members : {
    
    /** Sets the given array of items. */
    setItems : function( items ) {
      // preserve selection and focused item
      var manager = this.getManager();
      var oldLeadItem = manager.getLeadItem();
      var oldAnchorItem = manager.getAnchorItem();
      var oldSelection = manager.getSelectedItems();
      // exchange/add/remove items
      var oldItems = this.getChildren();
      for( var i = 0; i < items.length; i++ ) {
        if( i < oldItems.length ) {
          oldItems[ i ].setLabel( items[ i ] );
        } else {
          // TODO [rh] optimize this: context menu should be handled by the List
          //      itself for all its ListItems
          var item = new qx.ui.form.ListItem();
          item.addEventListener( "mouseover", this._onListItemMouseOver, this );
          item.addEventListener( "mouseout", this._onListItemMouseOut, this );
          // [if] Omit the focused item outline border - see bug 286902
          item.setStyleProperty( "outline", "0px none" );
          item.handleStateChange = function() {};
          // prevent items from being drawn outside the list
          item.setOverflow( qx.constant.Style.OVERFLOW_HIDDEN );
          item.setContextMenu( this.getContextMenu() );
          item.setTabIndex( null );
          item.setLabel( "(empty)" );
          item.getLabelObject().setMode( qx.constant.Style.LABEL_MODE_HTML );
          item.setLabel( items[ i ] );
          if( i % 2 == 0 ) {
            item.addState( "even" );
          }
          this.add( item );
        }
      }
      var child = null;
      while( this.getChildrenLength() > items.length ) {
        child = this.getLastChild();
        this.remove( child );
        child.removeEventListener( "mouseover", this._onListItemMouseOver, this );
        child.removeEventListener( "mouseout", this._onListItemMouseOut, this );
        child.dispose();
      }
      // restore previous selection and focusItem
      manager.setSelectedItems( oldSelection );
      manager.setLeadItem( oldLeadItem );
      if( manager.getMultiSelection() ) {
        manager.setAnchorItem( oldAnchorItem );
      }
    },

    /**
     * Sets the single selection for the List to the item specified by the given 
     * itemIndex (-1 to clear selection).
     */
    selectItem : function( itemIndex ) {
      if( itemIndex == -1 ) {
        this.getManager().deselectAll();
      } else {
        var item = this.getChildren()[ itemIndex ];
        this.getManager().setSelectedItem( item );
        // avoid warning message. scrollIntoView works only for visible widgets
        // the assumtion is that if 'this' is visible, the item to scroll into
        // view is also visible
        if ( this.isCreated() && this.isDisplayable() ) {
          this.getManager().scrollItemIntoView( item, true );
        }
      }
    },

    /**
     * Sets the multi selection for the List to the items specified by the given 
     * itemIndices array (empty array to clear selection).
     */
    selectItems : function( itemIndices ) {
      var manager = this.getManager(); 
      manager.deselectAll();
      for( var i = 0; i < itemIndices.length; i++ ) {
        var item = this.getChildren()[ itemIndices[ i ] ];
        manager.setItemSelected( item, true );
      }
    },

    /**
     * Sets the focused item the List to the item specified by the given 
     * itemIndex (-1 for no focused item).
     */
    focusItem : function( itemIndex ) {
      if( itemIndex == -1 ) {
        this.getManager().setLeadItem( null );
      } else {
        var items = this.getManager().getItems();
        this.getManager().setLeadItem( items[ itemIndex ] );
      }
    },

    /**
     * Selects all item if the List is multi-select. Does nothing for single-
     * select Lists.
     */
    selectAll : function() {
      if( this.getManager().getMultiSelection() == true ) {
        this.getManager().selectAll();
      }
    },

    setChangeSelectionNotification : function( value ) {
      this._changeSelectionNotification = value;
    },
    
    setTopIndex : function( value ) {
      this._topIndex = value;
      this._applyTopIndex( value );
    },
    
    addState : function( state ) {
      this.base( arguments, state );
      if( state.substr( 0, 8 ) == "variant_" ) {
        var items = this.getManager().getItems();
        for( var i = 0; i < items.length; i++ ) {
          items[ i ].addState( state );
        }
      }
    },

    removeState : function( state ) {
      this.base( arguments, state );
      if( state.substr( 0, 8 ) == "variant_" ) {
        var items = this.getManager().getItems();
        for( var i = 0; i < items.length; i++ ) {
          items[ i ].removeState( state );
        }
      }
    },

    _applyTopIndex : function( newIndex ) {
      var items = this.getManager().getItems();
      if( items.length > 0 && items[ 0 ].isCreated() ) {
        var itemHeight = this.getManager().getItemHeight( items[ 0 ] );
        if( itemHeight > 0 ) {
          this.setScrollTop( newIndex * itemHeight );
        }
      }
    },
    
    _getTopIndex : function() {
      var topIndex = 0;
      var scrollTop = this.getScrollTop();
      var items = this.getManager().getItems();
      if( items.length > 0 ) {
        var itemHeight = this.getManager().getItemHeight( items[ 0 ] );
        if( itemHeight > 0 ) {
          topIndex = Math.round( scrollTop / itemHeight );
        }
      }
      return topIndex;
    },
    
    _onAppear : function( evt ) {
      // [ad] Fix for Bug 277678 
      // when #showSelection() is called for invisible widget
      this._applyTopIndex( this._topIndex );
    },
    
    _onSendRequest : function( evt ) {
      var topIndex = this._isCreated ? this._getTopIndex() : 0;
      if( this._topIndex != topIndex ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var id = widgetManager.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        req.addParameter( id + ".topIndex", topIndex );
        this._topIndex = topIndex;
      }
    },

    _getSelectionIndices : function() {
      var wm = org.eclipse.swt.WidgetManager.getInstance();
      var id = wm.findIdByWidget( this );
      var selectionIndices = "";
      var selectedItems = this.getManager().getSelectedItems();
      for( var i = 0; i < selectedItems.length; i++ ) {
        var index = this.indexOf( selectedItems[ i ] );
        // TODO [rh] find out why sometimes index == -1, cannot be reproduced
        //      in standalone qooxdoo application
        if( index >= 0 ) {
          if( selectionIndices != "" ) {
            selectionIndices += ",";
          }
          selectionIndices += String( index );
        }
      }
      return selectionIndices;
    },

    _onChangeLeadItem : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var wm = org.eclipse.swt.WidgetManager.getInstance();
        var id = wm.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        var focusIndex = this.indexOf( this.getManager().getLeadItem() );
        req.addParameter( id + ".focusIndex", focusIndex );
      }
    },

    _onClick : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        this._updateSelectedItemState();
        if( !this.__clicksSuspended ) {
          this._suspendClicks();
//          TODO [rst] Replaced by _onSelectionChange, the stub remains here for
//                     mouse listeners
//          var wm = org.eclipse.swt.WidgetManager.getInstance();
//          var id = wm.findIdByWidget( this );
//          var req = org.eclipse.swt.Request.getInstance();
//          req.addParameter( id + ".selection", this._getSelectionIndices() );
//          if( this._changeSelectionNotification == "action" ) {
//            req.addEvent( "org.eclipse.swt.events.widgetSelected", id );
//            req.send();
//          }
        }
      }
    },

    _onDblClick : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        if( this._changeSelectionNotification == "action" ) {
          var wm = org.eclipse.swt.WidgetManager.getInstance();
          var id = wm.findIdByWidget( this );
          var req = org.eclipse.swt.Request.getInstance();
          req.addEvent( "org.eclipse.swt.events.widgetDefaultSelected", id );
          org.eclipse.swt.EventUtil.addWidgetSelectedModifier();
          req.send();
        }
      }
    },
    
    // Fix for bug# 288344
    _onkeyinput : function( evt ) {
      if( !evt.isAltPressed() && !evt.isCtrlPressed() ) {
        this.base( arguments, evt );
      } 
    },
    
    _onSelectionChange : function( evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var wm = org.eclipse.swt.WidgetManager.getInstance();
        var id = wm.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        req.addParameter( id + ".selection", this._getSelectionIndices() );
        if( this._changeSelectionNotification == "action" ) {
          req.addEvent( "org.eclipse.swt.events.widgetSelected", id );
          org.eclipse.swt.EventUtil.addWidgetSelectedModifier();
          req.send();
        }
      }
      this._updateSelectedItemState();
    },

    /**
     * Suspends the processing of click events to avoid sending multiple
     * widgetSelected events to the server.
     */
    _suspendClicks : function() {
      this.__clicksSuspended = true;
      qx.client.Timer.once( this._enableClicks, 
                            this, 
                            org.eclipse.swt.EventUtil.DOUBLE_CLICK_TIME );
    },

    _enableClicks : function() {
      this.__clicksSuspended = false;
    },

    _onFocusIn : function( evt ) {
      this._updateSelectedItemState();
    },

    _onFocusOut : function( evt ) {
      this._updateSelectedItemState();
    },
    
    _onListItemMouseOver : function( evt ) {
      evt.getTarget().addState( "over" );
    },
    
    _onListItemMouseOut : function( evt ) {
      evt.getTarget().removeState( "over" );
    },

    _updateSelectedItemState : function() {
      var selectedItems = this.getManager().getSelectedItems();
      // Set a flag that signals unfocused state on every item.
      // Note: Setting a flag that signals focused state would not work as the
      // list is reused by other widgets e.g. ComboBox, whose items would then
      // appear as unfocused by default.
      for( var i = 0; i < selectedItems.length; i++ ) {
        if( this.getFocused() ) {
          selectedItems[ i ].removeState( "parent_unfocused" );
        } else {
          selectedItems[ i ].addState( "parent_unfocused" );
        }
      }
    }
  }
});
