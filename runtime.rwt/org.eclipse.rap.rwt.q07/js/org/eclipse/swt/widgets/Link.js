/*******************************************************************************
 * Copyright (c) 2009, 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/

/**
 * This class provides the client-side implementation for
 * org.eclipse.swt.widgets.Link
 */
qx.Class.define( "org.eclipse.swt.widgets.Link", {
  extend : qx.ui.layout.CanvasLayout,

  construct : function() {
    this.base( arguments );
    this.setAppearance( "link" );
    // Default values
    this._text = "";
    this._hasSelectionListener = false;
    this._hyperlinksHaveListeners = false;
    this._linkColor;
    // indicates that the next request can be sent
    this._readyToSendChanges = true;
    // innerTab handling
    this._currentFocusedLink = -1;
    this._linksCount = 0;
    //
    this._link = new qx.ui.embed.HtmlEmbed();
    this._link.setAppearance( "link-text" );
    this.add( this._link );
    //
    this.setSelectable( false );
    this.setHideFocus( true );
    //
    this.__onMouseDown = qx.lang.Function.bindEvent( this._onMouseDown, this );
    this.__onKeyDown = qx.lang.Function.bindEvent( this._onKeyDown, this );
    //
    this.addEventListener( "appear", this._onAppear, this );
    this.addEventListener( "changeEnabled", this._onChangeEnabled, this );
    this.addEventListener( "contextmenu", this._onContextMenu, this );
    // Event listener used for inner TabIndex change
    this.addEventListener( "keypress", this._onKeyPress );
    this.addEventListener( "focusout", this._onFocusOut );
    //
    this._link.addEventListener( "changeHtml", this._onChangeHtml, this );
  },

  destruct : function() {
    this._removeEventListeners();
    delete this.__onMouseDown;
    delete this.__onKeyDown;
    this.removeEventListener( "appear", this._onAppear, this );
    this.removeEventListener( "contextmenu", this._onContextMenu, this );
    this.removeEventListener( "changeEnabled", this._onChangeEnabled, this );
    this.removeEventListener( "keypress", this._onKeyPress );
    this.removeEventListener( "focusout", this._onFocusOut );
    this._link.removeEventListener( "changeHtml", this._onChangeHtml, this );
    this._link.dispose();
  },

  members : {
    _onContextMenu : function( evt ) {
      var menu = this.getContextMenu();
      if( menu != null ) {
        menu.setLocation( evt.getPageX(), evt.getPageY() );
        menu.setOpener( this );
        menu.show();
        evt.stopPropagation();
      }
    },

    _onAppear : function( evt ) {
      this._link.setTabIndex( null );
      this._link.setHideFocus( true );
      this._applyHyperlinksStyleProperties();
      this._addEventListeners();
    },

    _onChangeHtml : function( evt ) {
      this._applyHyperlinksStyleProperties();
      this._addEventListeners();
    },

    _applyTextColor : function( value, old ) {
      this.base( arguments, value, old );
      var themeValues
        = new org.eclipse.swt.theme.ThemeValues( this._getStates() );
      this._linkColor = themeValues.getCssColor( "Link-Hyperlink", "color" );
      themeValues.dispose();
      this._applyHyperlinksStyleProperties();
    },

    _onChangeEnabled : function( evt ) {
      this._applyHyperlinksStyleProperties();
      this._changeHyperlinksTabIndexProperty();
    },

    _getStates : function() {
      if( !this.__states ) {
        this.__states = {};
      }
      return this.__states;
    },

    addState : function( state ) {
      this.base( arguments, state );
      this._link.addState( state );
    },

    removeState : function( state ) {
      this.base( arguments, state );
      this._link.removeState( state );
    },

    setHasSelectionListener : function( value ) {
      this._hasSelectionListener = value;
    },

    addText : function( text ) {
      this._text += text;
    },

    addLink : function( text, index ) {
      this._text += "<span tabIndex=\"1\" ";
      this._text += "style=\"";
      this._text += "text-decoration:underline; ";
      this._text += "\" ";
      this._text += "id=\"" + index + "\"";
      this._text += ">";
      this._text += text;
      this._text += "</span>";
      this._linksCount++;
    },

    applyText : function() {
      this._link.setHtml( this._text );
      if ( this._linksCount === 0 ) {
        this.setTabIndex( null );
      } else {
        this.setTabIndex( 1 );
      }
    },

    clear : function() {
      this._removeEventListeners();
      this._text = "";
      this._linksCount = 0;
    },

    _applyHyperlinksStyleProperties : function() {
      var hyperlinks = this._getHyperlinkElements();
      for( i = 0; i < hyperlinks.length; i++ ) {
        if( this._linkColor ) {
          if( this.isEnabled() ) {
            hyperlinks[ i ].style.color = this._linkColor;
          } else {
            hyperlinks[ i ].style.color = "";
          }
        }
        if( this.isEnabled() ) {
          hyperlinks[ i ].style.cursor = "pointer";
        } else {
          hyperlinks[ i ].style.cursor = "default";
        }
      }
    },

    _changeHyperlinksTabIndexProperty : function() {
      var hyperlinks = this._getHyperlinkElements();
      for( i = 0; i < hyperlinks.length; i++ ) {
        if( this.isEnabled() ) {
          hyperlinks[ i ].tabIndex = "1";
        } else {
          hyperlinks[ i ].tabIndex = "-1";
        }
      }
    },

    _addEventListeners : function() {
      var hyperlinks = this._getHyperlinkElements();
      if( hyperlinks.length > 0 && !this._hyperlinksHaveListeners ) {
        for( i = 0; i < hyperlinks.length; i++ ) {
          qx.html.EventRegistration.addEventListener( hyperlinks[ i ],
                                                      "mousedown",
                                                      this.__onMouseDown );
          qx.html.EventRegistration.addEventListener( hyperlinks[ i ],
                                                      "keydown",
                                                      this.__onKeyDown );
        }
        this._hyperlinksHaveListeners = true;
      }
    },

    _removeEventListeners : function() {
      var hyperlinks = this._getHyperlinkElements();
      if( hyperlinks.length > 0 && this._hyperlinksHaveListeners ) {
        for( i = 0; i < hyperlinks.length; i++ ) {
          qx.html.EventRegistration.removeEventListener( hyperlinks[ i ],
                                                         "mousedown",
                                                         this.__onMouseDown );
          qx.html.EventRegistration.removeEventListener( hyperlinks[ i ],
                                                         "keydown",
                                                         this.__onKeyDown );
        }
        this._hyperlinksHaveListeners = false;
      }
    },

    _onMouseDown : function( e ) {
      var target = this._getEventTarget( e );
      var index = parseInt( target.id );
      this._setFocusedLink( index );
      var leftBtnPressed = this._isLeftMouseButtonPressed( e );
      if( this.isEnabled() && leftBtnPressed && this._readyToSendChanges ) {
        // [if] Fix for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=252559
        this._readyToSendChanges = false;
        qx.client.Timer.once( function() {
          this._sendChanges( index );
        }, this, org.eclipse.swt.EventUtil.DOUBLE_CLICK_TIME );
      }
    },

    _isLeftMouseButtonPressed : function( e ) {
      var leftBtnPressed;
      if( e.which ) {
        leftBtnPressed = ( e.which === 1 );
      } else if ( e.button ) {
        if( qx.core.Variant.isSet( "qx.client", "mshtml" ) ) {
          leftBtnPressed = ( e.button === 1 );
        } else {
          leftBtnPressed = ( e.button === 0 );
        }
      }
      return leftBtnPressed;
    },

    _onKeyDown : function( e ) {
      if( this.isEnabled() && e.keyCode === 13 ) {
        var target = this._getEventTarget( e );
        var index = target.id;
        this._sendChanges( index );
      }
    },

    _getEventTarget : function( e ) {
      var target;
      if( qx.core.Variant.isSet( "qx.client", "mshtml" ) ) {
        target = window.event.srcElement;
      } else {
        target = e.target;
      }
      return target;
    },

    // Override of the _ontabfocus method from qx.ui.core.Widget
    _ontabfocus : function() {
      if( this._currentFocusedLink === -1 && this._linksCount > 0 ) {
        this._setFocusedLink( 0 );
      }
    },

    _onKeyPress : function( evt ) {
      if(    this.isFocused()
          && evt.getKeyIdentifier() === "Tab"
          && this._linksCount > 0 )
      {
        if(    !evt.isShiftPressed()
            && this._currentFocusedLink >= 0
            && this._currentFocusedLink < this._linksCount - 1 )
        {
          evt.stopPropagation();
          evt.preventDefault();
          this._setFocusedLink( this._currentFocusedLink + 1 );
        } else if(    !evt.isShiftPressed()
                   && this._currentFocusedLink === -1 )
        {
          evt.stopPropagation();
          evt.preventDefault();
          this._setFocusedLink( 0 );
        } else if(    evt.isShiftPressed()
                   && this._currentFocusedLink > 0
                   && this._currentFocusedLink <= this._linksCount - 1 )
        {
          evt.stopPropagation();
          evt.preventDefault();
          this._setFocusedLink( this._currentFocusedLink - 1 );
        }
      }
    },

    _onFocusOut : function( evt ) {
      this._setFocusedLink( -1 );
    },

    _setFocusedLink : function( id ) {
      var hyperlink = this._getFocusedHyperlinkElement();
      if( hyperlink !== null ) {
        hyperlink.blur();
        if( qx.core.Variant.isSet( "qx.client", "webkit" ) ) {
          hyperlink.style.outline = "none";
        }
      }
      this._currentFocusedLink = id;
      hyperlink = this._getFocusedHyperlinkElement();
      if( hyperlink !== null ) {
        hyperlink.focus();
        if( qx.core.Variant.isSet( "qx.client", "webkit" ) ) {
          hyperlink.style.outline = "1px dotted";
        }
      }
    },

    _getFocusedHyperlinkElement : function() {
      var result = null;
      var hyperlinks = this._getHyperlinkElements();
      var number =  this._currentFocusedLink;
      if( number >= 0 && number < hyperlinks.length ) {
        result = hyperlinks[ number ];
      }
      return result;
    },

    _getHyperlinkElements : function() {
      var result;
      var linkElement = this.getElement();
      if( linkElement ) {
        result = linkElement.getElementsByTagName( "span" );
      } else {
      	result = [];
      }
      return result;
    },

    _sendChanges : function( index ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var id = widgetManager.findIdByWidget( this );
        var req = org.eclipse.swt.Request.getInstance();
        if( this._hasSelectionListener ) {
          req.addEvent( "org.eclipse.swt.events.widgetSelected", id );
          org.eclipse.swt.EventUtil.addWidgetSelectedModifier();
          req.addEvent( "org.eclipse.swt.events.widgetSelected.index", index );
          req.send();
        }
      }
      this._readyToSendChanges = true;
    }
  }
} );
