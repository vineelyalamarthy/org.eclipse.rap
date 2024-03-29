/* ************************************************************************

   qooxdoo - the new era of web development

   http://qooxdoo.org

   Copyright:
     2004-2008 1&1 Internet AG, Germany, http://www.1und1.de

   License:
     LGPL: http://www.gnu.org/licenses/lgpl.html
     EPL: http://www.eclipse.org/org/documents/epl-v10.php
     See the LICENSE file in the project's top-level directory for details.

   Authors:
     * Sebastian Werner (wpbasti)
     * Andreas Ecker (ecker)

   Contributors:
     * EclipseSource

************************************************************************ */

 ///////////////////////////////////////////////////////////////////////
 // This manager registers and manage all incoming key and mouse events.

 
qx.Class.define( "qx.event.handler.EventHandler", {
  type : "singleton",
  extend : qx.core.Target,

  construct : function() {
    this.base( arguments );
    this.__onmouseevent = qx.lang.Function.bind( this._onmouseevent, this );
    this.__ondragevent = qx.lang.Function.bind( this._ondragevent, this );
    this.__onselectevent = qx.lang.Function.bind( this._onselectevent, this );
    this.__onwindowblur = qx.lang.Function.bind( this._onwindowblur, this );
    this.__onwindowfocus = qx.lang.Function.bind( this._onwindowfocus, this );
    this.__onwindowresize = qx.lang.Function.bind( this._onwindowresize, this );
    this._commands = {};
    this._filter = {};
  },

  events : {
    "error" : "qx.event.type.DataEvent"
  },

  statics : {
    mouseEventTypes : [
      "mouseover", 
      "mousemove", 
      "mouseout", 
      "mousedown", 
      "mouseup", 
      "click",
      "dblclick", 
      "contextmenu",
      qx.core.Variant.isSet( "qx.client", "gecko" ) ? "DOMMouseScroll" : "mousewheel"
    ],

    keyEventTypes : [ "keydown", "keypress", "keyup" ],

    dragEventTypes : qx.core.Variant.select("qx.client", {
      "gecko" : [ 
        "dragdrop", 
        "dragover", 
        "dragenter", 
        "dragexit", 
        "draggesture" 
       ],
      "mshtml" : [ 
        "dragend", 
        "dragover", 
        "dragstart", 
        "drag", 
        "dragenter", 
        "dragleave" 
      ],
      "default" : [ 
        "dragstart", 
        "dragdrop", 
        "dragover", 
        "drag", 
        "dragleave", 
        "dragenter", 
        "dragexit", 
        "draggesture" 
      ]
    } ),

    //////////
    // Helpers

    getDomTarget : qx.core.Variant.select("qx.client",
    {
      "mshtml" : function(vDomEvent) {
        return vDomEvent.target || vDomEvent.srcElement;
      },

      "webkit" : function(vDomEvent)
      {
        var vNode = vDomEvent.target || vDomEvent.srcElement;

        // Safari takes text nodes as targets for events
        if (vNode && (vNode.nodeType == qx.dom.Node.TEXT)) {
          vNode = vNode.parentNode;
        }

        return vNode;
      },

      "default" : function(vDomEvent) {
        return vDomEvent.target;
      }
    } ),

    stopDomEvent : function(vDomEvent)
    {
      if (vDomEvent.preventDefault) {
        vDomEvent.preventDefault();
      }

      try
      {
        // this allows us to prevent some key press events in IE and Firefox.
        // See bug #1049
        vDomEvent.keyCode = 0;
      } catch(ex) {}

      vDomEvent.returnValue = false;
    },

    // BUG: http://xscroll.mozdev.org/
    // If your Mozilla was built with an option `--enable-default-toolkit=gtk2',
    // it can not return the correct event target for DOMMouseScroll.

    getOriginalTargetObject : function(vNode)
    {
      // Events on the HTML element, when using absolute locations which
      // are outside the HTML element. Opera does not seem to fire events
      // on the HTML element.
      if (vNode == document.documentElement) {
        vNode = document.body;
      }

      // Walk up the tree and search for an qx.ui.core.Widget
      while (vNode != null && vNode.qx_Widget == null)
      {
        try {
          vNode = vNode.parentNode;
        } catch(vDomEvent) {
          vNode = null;
        }
      }

      return vNode ? vNode.qx_Widget : null;
    },


    getOriginalTargetObjectFromEvent : function(vDomEvent, vWindow)
    {
      var vNode = qx.event.handler.EventHandler.getDomTarget(vDomEvent);

      // Especially to fix key events.
      // 'vWindow' is the window reference then
      if (vWindow)
      {
        var vDocument = vWindow.document;

        if (vNode == vWindow || vNode == vDocument || vNode == vDocument.documentElement || vNode == vDocument.body) {
          return vDocument.body.qx_Widget;
        }
      }

      return qx.event.handler.EventHandler.getOriginalTargetObject(vNode);
    },


    getRelatedOriginalTargetObjectFromEvent : function(vDomEvent) {
      return qx.event.handler.EventHandler.getOriginalTargetObject(
        vDomEvent.relatedTarget ||
        (vDomEvent.type == "mouseover" ? vDomEvent.fromElement : vDomEvent.toElement)
      );
    },


    getTargetObject : function(vNode, vObject, allowDisabled)
    {
      if (!vObject)
      {
        var vObject = qx.event.handler.EventHandler.getOriginalTargetObject(vNode);

        if (!vObject) {
          return null;
        }
      }

      // Search parent tree
      while (vObject)
      {
        // Break if current object is disabled -
        // event should be ignored then.
        if (!allowDisabled && !vObject.getEnabled()) {
          return null;
        }

        // If object is anonymous, search for
        // first parent which is not anonymous
        // and not disabled
        if (!vObject.getAnonymous()) {
          break;
        }

        vObject = vObject.getParent();
      }

      return vObject;
    },

    getTargetObjectFromEvent : function(vDomEvent) {
      return qx.event.handler.EventHandler.getTargetObject(qx.event.handler.EventHandler.getDomTarget(vDomEvent));
    },

    getRelatedTargetObjectFromEvent : function(vDomEvent)
    {
      var target = vDomEvent.relatedTarget;

      if (!target)
      {
        if (vDomEvent.type == "mouseover") {
          target = vDomEvent.fromElement;
        } else {
          target = vDomEvent.toElement;
        }
      }

      return qx.event.handler.EventHandler.getTargetObject(target);
    }
  },

  properties : {

    allowClientContextMenu : {
      check : "Boolean",
      init : false
    },

    allowClientSelectAll : {
      check : "Boolean",
      init : false
    },

    captureWidget : {
      check : "qx.ui.core.Widget",
      nullable : true,
      apply : "_applyCaptureWidget"
    },

    focusRoot : {
      check : "qx.ui.core.Parent",
      nullable : true,
      apply : "_applyFocusRoot"
    }

  },

  members : {

    _allowContextMenu : qx.lang.Function.returnFalse,

    /**
     * Sets a callback-function to decide if the native context- 
     * menu is displayed. It will be called on DOM-events of the type 
     * "contextmenu". The target-Widget of the event will be given as
     * the first argument, the dom-target as the second. 
     * It must return a boolean. Null is not allowed.
     *
     */    
    setAllowContextMenu : function( fun ) {
      this._allowContextMenu = fun;
    },

    _menuManager : null,
    
    setMenuManager : function( manager ) {
      this._menuManager = manager;
    },
    
    getMenuManager : function( manager ) {
      return this._menuManager; 
    },
    
    //////////////
    // STATE FLAGS

    _lastMouseEventType : null,
    _lastMouseDown : false,
    _lastMouseEventDate : 0,

    ////////////
    // MODIFIERS

    _applyCaptureWidget : function(value, old)
    {
      if (old) {
        old.setCapture(false);
      }

      if (value) {
        value.setCapture(true);
      }
    },

    _applyFocusRoot : function(value, old)
    {
      // this.debug("FocusRoot: " + value + "(from:" + old + ")");

      if (old) {
        old.setFocusedChild(null);
      }

      if (value && value.getFocusedChild() == null) {
        value.setFocusedChild(value);
      }
    },

    ////////////////////
    // COMMAND INTERFACE

    addCommand : function(vCommand) {
      this._commands[vCommand.toHashCode()] = vCommand;
    },

    removeCommand : function(vCommand)
    {
      delete this._commands[vCommand.toHashCode()];

      // reset list if it is empty. This frees some browser memory
      if (qx.lang.Object.isEmpty(this._commands)) {
        this._commands = {};
      }
    },

    _checkKeyEventMatch : function(e)
    {
      var vCommand;

      for (var vHash in this._commands)
      {
        vCommand = this._commands[vHash];

        if (vCommand.getEnabled() && vCommand.matchesKeyEvent(e))
        {
          // allow the user to stop the event
          // through the execute event.
          if (!vCommand.execute(e.getTarget())) {
            e.preventDefault();
          }

          break;
        }
      }
    },
  
   ////////////////
   // EVENT-MAPPING

    attachEvents : function()
    {
      this.attachEventTypes(qx.event.handler.EventHandler.mouseEventTypes, this.__onmouseevent);
      this.attachEventTypes(qx.event.handler.EventHandler.dragEventTypes, this.__ondragevent);
      qx.event.handler.KeyEventHandler.getInstance()._attachEvents();
      qx.html.EventRegistration.addEventListener(window, "blur", this.__onwindowblur);
      qx.html.EventRegistration.addEventListener(window, "focus", this.__onwindowfocus);
      qx.html.EventRegistration.addEventListener(window, "resize", this.__onwindowresize);
      document.body.onselect = document.onselectstart = document.onselectionchange = this.__onselectevent;
    },

    detachEvents : function()
    {
      this.detachEventTypes(qx.event.handler.EventHandler.mouseEventTypes, this.__onmouseevent);
      this.detachEventTypes(qx.event.handler.EventHandler.dragEventTypes, this.__ondragevent);
      qx.event.handler.KeyEventHandler.getInstance()._detachEvents();
      qx.html.EventRegistration.removeEventListener(window, "blur", this.__onwindowblur);
      qx.html.EventRegistration.removeEventListener(window, "focus", this.__onwindowfocus);
      qx.html.EventRegistration.removeEventListener(window, "resize", this.__onwindowresize);
      document.body.onselect = document.onselectstart = document.onselectionchange = null;
    },

    ////////////////////////
    //  EVENT-MAPPING HELPER

    attachEventTypes : function(vEventTypes, vFunctionPointer)
    {
      try
      {
        // Gecko is a bit buggy to handle key events on document if not previously focused
        // I think they will fix this sometimes, and we should add a version check here.
        // Internet Explorer has problems to use 'window', so there we use the 'body' element
        // as previously.
        var el = qx.core.Variant.isSet("qx.client", "gecko") ? window : document.body;

        for (var i=0, l=vEventTypes.length; i<l; i++) {
          qx.html.EventRegistration.addEventListener(el, vEventTypes[i], vFunctionPointer);
        }
      }
      catch(ex)
      {
        throw new Error("qx.event.handler.EventHandler: Failed to attach window event types: " + vEventTypes + ": " + ex);
      }
    },

    detachEventTypes : function(vEventTypes, vFunctionPointer)
    {
      try
      {
        var el = qx.core.Variant.isSet("qx.client", "gecko") ? window : document.body;

        for (var i=0, l=vEventTypes.length; i<l; i++) {
          qx.html.EventRegistration.removeEventListener(el, vEventTypes[i], vFunctionPointer);
        }
      }
      catch(ex)
      {
        throw new Error("qx.event.handler.EventHandler: Failed to detach window event types: " + vEventTypes + ": " + ex);
      }
    },
     
    /////////////
    // KEY EVENTS

    _onkeyevent_post : function(vDomEvent, vType, vKeyCode, vCharCode, vKeyIdentifier)
    {
      var vDomTarget = qx.event.handler.EventHandler.getDomTarget(vDomEvent);

      // Find current active qooxdoo object
      var vFocusRoot = this.getFocusRoot();
      var vTarget = this.getCaptureWidget() || (vFocusRoot == null ? null : vFocusRoot.getActiveChild());

      // Create Event Object
      var vKeyEventObject = new qx.event.type.KeyEvent(vType, vDomEvent, vDomTarget, vTarget, null, vKeyCode, vCharCode, vKeyIdentifier);

      // Check for commands
      if (vType == "keydown") {
        this._checkKeyEventMatch(vKeyEventObject);
      }

      if (vTarget != null && vTarget.getEnabled())
      {
        // Hide Menus
        switch(vKeyIdentifier)
        {
          case "Escape":
          case "Tab":
            if ( this._menuManager != null ) {
              this._menuManager.update(vTarget, vType);
            }

            break;
        }

        // TODO: Move this to KeyEvent?
        // Prohibit CTRL+A
        if (!this.getAllowClientSelectAll())
        {
          if (vDomEvent.ctrlKey && vKeyIdentifier == "A")
          {
            switch(vDomTarget.tagName.toLowerCase())
            {
              case "input":
              case "textarea":
              case "iframe":
                break;

              default:
                qx.event.handler.EventHandler.stopDomEvent(vDomEvent);
            }
          }
        }

        // Starting Objects Internal Event Dispatcher
        // This handles the real event action
        vTarget.dispatchEvent(vKeyEventObject);

        // Send event to qx.event.handler.DragAndDropHandler
        if (qx.Class.isDefined("qx.event.handler.DragAndDropHandler")) {
          qx.event.handler.DragAndDropHandler.getInstance().handleKeyEvent(vKeyEventObject);
        }
      }

      // Cleanup Event Object
      vKeyEventObject.dispose();
    },
 
    ///////////////
    // MOUSE EVENTS

    setMouseEventFilter : function( filter, context ) {
      this._filter[ "mouseevent" ] = [ filter, context ];
    },

    /** 
     * This one handle all mouse events
     *
     *  When a user double clicks on a qx.ui.core.Widget the
     *  order of the mouse events is the following:
     *
     *  1. mousedown
     *  2. mouseup
     *  3. click
     *  4. mousedown
     *  5. mouseup
     *  6. click
     *  7. dblclick
     *
     */
     
     _onmouseevent : function( event ) {
       var process = true;
       if( typeof this._filter[ "mouseevent" ] !== "undefined" ) {
         var context = this._filter[ "mouseevent" ][ 1 ];
         process = this._filter[ "mouseevent" ][ 0 ].call( context, event );
       }
       if( process ) {
         this._processMouseEvent( event ); 
       }
     },

    _processMouseEvent : qx.core.Variant.select("qx.client",  {
      "mshtml" : function(vDomEvent)
      {
        if (!vDomEvent) {
          vDomEvent = window.event;
        }

        var vDomTarget = qx.event.handler.EventHandler.getDomTarget(vDomEvent);
        var vType = vDomEvent.type;

        if (vType == "mousemove")
        {
          if (this._mouseIsDown && vDomEvent.button == 0)
          {
            this._onmouseevent_post(vDomEvent, "mouseup", vDomTarget);
            this._mouseIsDown = false;
          }
        }
        else
        {
          if (vType == "mousedown") {
            this._mouseIsDown = true;
          } else if (vType == "mouseup") {
            this._mouseIsDown = false;
          }

          // Fix MSHTML Mouseup, should be after a normal click or contextmenu event, like Mozilla does this
          if (vType == "mouseup" && !this._lastMouseDown && ((new Date).valueOf() - this._lastMouseEventDate) < 250) {
            this._onmouseevent_post(vDomEvent, "mousedown", vDomTarget);
          }

          // Fix MSHTML Doubleclick, should be after a normal click event, like Mozilla does this
          else if (vType == "dblclick" && this._lastMouseEventType == "mouseup" && ((new Date).valueOf() - this._lastMouseEventDate) < 250) {
            this._onmouseevent_post(vDomEvent, "click", vDomTarget);
          }

          switch(vType)
          {
            case "mousedown":
            case "mouseup":
            case "click":
            case "dblclick":
            case "contextmenu":
              this._lastMouseEventType = vType;
              this._lastMouseEventDate = (new Date).valueOf();
              this._lastMouseDown = vType == "mousedown";
          }
        }

        this._onmouseevent_post(vDomEvent, vType, vDomTarget);
      },

      "default" : function(vDomEvent)
      {
        var vDomTarget = qx.event.handler.EventHandler.getDomTarget(vDomEvent);
        var vType = vDomEvent.type;

        switch(vType)
        {
          case "DOMMouseScroll":
            // normalize mousewheel event
            vType = "mousewheel";
            break;

          case "click":
          case "dblclick":
            // ignore click or dblclick events with other then the left mouse button
            if (vDomEvent.which !== 1) {
              return;
            }
        }

        this._onmouseevent_post(vDomEvent, vType, vDomTarget);
      }
    } ),

    /** 
     * Fixes browser quirks with 'click' detection
     *
     * Firefox 1.5.0.6: The DOM-targets are different. The click event only fires, if the target of the
     *   mousedown is the same than with the mouseup. If the content moved away, the click isn't fired.
     *
     * Internet Explorer 6.0: The DOM-targets are identical and the click fires fine.
     *
     * Opera 9.01: The DOM-targets are different, but the click fires fine. Fires click successfull,
     *   even if the content under the cursor was moved away.
     */
    _onmouseevent_click_fix : qx.core.Variant.select("qx.client", {
      "gecko" : function(vDomTarget, vType, vDispatchTarget)
      {
        var vReturn = false;

        switch(vType)
        {
          case "mousedown":
            this._lastMouseDownDomTarget = vDomTarget;
            this._lastMouseDownDispatchTarget = vDispatchTarget;
            break;

          case "mouseup":
            // Add additional click event if the dispatch target is the same, but the dom target is different
            if (this._lastMouseDownDispatchTarget === vDispatchTarget && vDomTarget !== this._lastMouseDownDomTarget) {
              vReturn = true;
            }
            else
            {
              this._lastMouseDownDomTarget = null;
              this._lastMouseDownDispatchTarget = null;
            }
        }

        return vReturn;
      },

      "default" : null
    } ),

    /** 
     * This is the crossbrowser post handler for all mouse events.
     */
    _onmouseevent_post : function( vDomEvent, vType, vDomTarget ) {
      var vCaptureTarget = this.getCaptureWidget();
      var vOriginalTarget 
        = qx.event.handler.EventHandler.getOriginalTargetObject( vDomTarget );
      var vTarget = qx.event.handler.EventHandler.getTargetObject( null, vOriginalTarget, true );
      if (!vTarget) {
        return;
      }
      var vDispatchTarget = vCaptureTarget ? vCaptureTarget : vTarget;
      var vFixClick = false;
      if( qx.core.Variant.isSet( "qx.client", "gecko" ) ) {
        vFixClick 
          = this._onmouseevent_click_fix( vDomTarget, vType, vDispatchTarget );
      }
      // Prevent the browser's native context menu
      if(    vType == "contextmenu" 
          && !this._allowContextMenu( vOriginalTarget, vDomTarget ) ) {
        qx.event.handler.EventHandler.stopDomEvent( vDomEvent );
      }
      // Update focus
      if( vTarget.getEnabled() && vType == "mousedown" ) {
        qx.event.handler.FocusHandler.mouseFocus = true;
        var vRoot = vTarget.getFocusRoot();
        if( vRoot ) {
          this.setFocusRoot( vRoot );
          var vFocusTarget = vTarget;
          while( !vFocusTarget.isFocusable() && vFocusTarget != vRoot ) {
            vFocusTarget = vFocusTarget.getParent();
          }
          // We need to focus first and active afterwards.
          // Otherwise the focus will activate another widget if the
          // active one is not tabable.
          vRoot.setFocusedChild( vFocusTarget );
          vRoot.setActiveChild( vTarget );
        }
      }
      // handle related target object
      if( vType == "mouseover" || vType == "mouseout" ) {
        var vRelatedTarget = qx.event.handler.EventHandler.getRelatedTargetObjectFromEvent( vDomEvent );
        var elementEventType = vType == "mouseover" ? "elementOver" : "elementOut";
        this._fireElementHoverEvents( elementEventType,
                                      vDomEvent,
                                      vDomTarget, 
                                      vTarget,
                                      vOriginalTarget, 
                                      vRelatedTarget,
                                      vDispatchTarget );
        // Ignore events where the related target and
        // the real target are equal - from our sight
        if( vRelatedTarget == vTarget ) {
          return;
        }
      }
      var vEventObject = new qx.event.type.MouseEvent( vType, 
                                                       vDomEvent, 
                                                       vDomTarget, 
                                                       vTarget, 
                                                       vOriginalTarget, 
                                                       vRelatedTarget );
      // Store last Event in MouseEvent Constructor. Needed for Tooltips, ...
      qx.event.type.MouseEvent.storeEventState( vEventObject );
      if( vDispatchTarget.getEnabled() ) {
        vDispatchTarget.dispatchEvent( vEventObject );
        this._onmouseevent_special_post( vType, 
                                         vTarget, 
                                         vOriginalTarget, 
                                         vDispatchTarget, 
                                         vEventObject, 
                                         vDomEvent );
      } else if( vType == "mouseover" ) {
        if( qx.Class.isDefined( "qx.ui.popup.ToolTipManager" ) ) {
          var toolTipManager = qx.ui.popup.ToolTipManager.getInstance();
          toolTipManager.handleMouseOver( vEventObject );
        }
      }
      vEventObject.dispose();
      qx.ui.core.Widget.flushGlobalQueues();
      // Fix Click (Gecko Bug, see above)
      if( vFixClick ) {
        this._onmouseevent_post( vDomEvent, 
                                 "click", 
                                 this._lastMouseDownDomTarget );
        this._lastMouseDownDomTarget = null;
        this._lastMouseDownDispatchTarget = null;
      }
    },
    
    _fireElementHoverEvents : function( type,
                                        domEvent,
                                        domTarget, 
                                        target,
                                        originalTarget, 
                                        relatedTarget,
                                        dispatchTarget )
    {
      if( dispatchTarget.getEnabled() ) {
        var eventObject = new qx.event.type.MouseEvent( type, 
                                                        domEvent, 
                                                        domTarget, 
                                                        target, 
                                                        originalTarget, 
                                                        relatedTarget );
        dispatchTarget.dispatchEvent( eventObject );
      }
    },

    _onmouseevent_special_post : function(vType, vTarget, vOriginalTarget, vDispatchTarget, vEventObject, vDomEvent)
    {
      switch(vType)
      {
        case "mousedown":
          if (qx.Class.isDefined("qx.ui.popup.PopupManager")) {
            qx.ui.popup.PopupManager.getInstance().update(vTarget);
          }

          if ( this._menuManager != null ) {
            this._menuManager.update(vTarget, vType);
          }

          if (qx.Class.isDefined("qx.ui.embed.IframeManager")) {
            qx.ui.embed.IframeManager.getInstance().handleMouseDown(vEventObject);
          }

          break;

        case "mouseup":
          // Mouseup event should always hide, independed of target, so don't send a target
          if ( this._menuManager != null ) {
            this._menuManager.update(vTarget, vType);
          }

          if (qx.Class.isDefined("qx.ui.embed.IframeManager")) {
            qx.ui.embed.IframeManager.getInstance().handleMouseUp(vEventObject);
          }

          break;

        case "mouseover":
          if (qx.Class.isDefined("qx.ui.popup.ToolTipManager")) {
            qx.ui.popup.ToolTipManager.getInstance().handleMouseOver(vEventObject);
          }

          break;

        case "mouseout":
          if (qx.Class.isDefined("qx.ui.popup.ToolTipManager")) {
            qx.ui.popup.ToolTipManager.getInstance().handleMouseOut(vEventObject);
          }

          break;

        /*
        case "mousewheel":
          if (qx.core.Variant.isSet("qx.client", "gecko"))
          {
            // priority for the real target not the (eventually captured) dispatch target
            vEventWasProcessed ? this._onmousewheel(vOriginalTarget || vDispatchTarget, vEventObject) : qx.event.handler.EventHandler.stopDomEvent(vDomEvent);
          }

          break;
        */
      }

      this._ignoreWindowBlur = vType === "mousedown";

      // Send Event Object to Drag&Drop Manager
      if (qx.Class.isDefined("qx.event.handler.DragAndDropHandler") && vTarget) {
        qx.event.handler.DragAndDropHandler.getInstance().handleMouseEvent(vEventObject);
      }
    },

    //////////////
    // DRAG EVENTS

    _ondragevent : function(vEvent)
    {
      if (!vEvent) {
        vEvent = window.event;
      }

      qx.event.handler.EventHandler.stopDomEvent(vEvent);
    },

    ////////////////
    // SELECT EVENTS

    _onselectevent : function(e)
    {
      if (!e) {
        e = window.event;
      }

      var target = qx.event.handler.EventHandler.getOriginalTargetObjectFromEvent(e);

      while (target)
      {
        if (target.getSelectable() != null)
        {
          if (!target.getSelectable())
          {
            qx.event.handler.EventHandler.stopDomEvent(e);
          }

          break;
        }

        target = target.getParent();
      }
    },

    ////////////////
    // WINDOW EVENTS

    _focused : false,

    _onwindowblur : function(e) {
      if (!this._focused || this._ignoreWindowBlur || e.originalTarget != window) {
        return;
      }

      this._focused = false;

      // Disable capturing
      this.setCaptureWidget(null);

      // Hide Popups, Tooltips, ...
      if (qx.Class.isDefined("qx.ui.popup.PopupManager")) {
        qx.ui.popup.PopupManager.getInstance().update();
      }

      // Hide Menus
      if ( this._menuManager ) {
        this._menuManager.update();
      }

      // Cancel Drag Operations
      if (qx.Class.isDefined("qx.event.handler.DragAndDropHandler")) {
        qx.event.handler.DragAndDropHandler.getInstance().globalCancelDrag();
      }

      // Send blur event to client document
      qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowblur");
    },


    _onwindowfocus : function(e)
    {
      if (this._focused) {
        return;
      }

      this._focused = true;

      // Send focus event to client document
      qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowfocus");
    },

    _onwindowresize : function(e)
    {
      // Send resize event to client document
      qx.ui.core.ClientDocument.getInstance().createDispatchEvent("windowresize");
    }
  },

  ///////////// 
  // DESTRUCTOR

  destruct : function() {
    this.detachEvents();
    this._disposeObjectDeep( "_commands", 1 );
    this._disposeFields(
      "__onmouseevent", 
      "__ondragevent", 
      "__onselectevent",
      "__onwindowblur", 
      "__onwindowfocus", 
      "__onwindowresize"
    );
    this._disposeFields("_lastMouseEventType", "_lastMouseDown", "_lastMouseEventDate",
      "_lastMouseDownDomTarget", "_lastMouseDownDispatchTarget");
  }
} );