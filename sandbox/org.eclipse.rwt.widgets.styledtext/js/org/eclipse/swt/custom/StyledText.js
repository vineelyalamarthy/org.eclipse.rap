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

qx.Class.define( "org.eclipse.swt.custom.StyledText", {
  extend : qx.ui.embed.Iframe,
  
  construct : function() {
    this.base( arguments, "org/eclipse/swt/custom/blank.html" );
    this.setAppearance( "styled-text" );
    this.setTabIndex(1);
    this.addEventListener( "load", this._onLoad, this );
    this.addEventListener( "changeFont", this._setDocumentStyle, this );
    this.addEventListener( "changeTextColor", this._setDocumentStyle, this );
    this.addEventListener( "changeBackgroundColor", this._setDocumentStyle, this );
    this.__handleMouseDownEvent
      = qx.lang.Function.bind( this._handleMouseDownEvent, this );
    this.__handleMouseUpEvent
      = qx.lang.Function.bind( this._handleMouseUpEvent, this );
    this._loaded = false;
    this._hasMouseListener = false;    
    this._hasSelectionListener = false;    
    this._readyToSendChanges = true;    
    this._selectionStart = 0;
    this._selectionEnd = 0;
    this._caretOffset = 0;
    
    String.prototype.startsWith = function( str ) {
      return ( this.match( "^" + str ) == str );
    }
  },
  
  destruct : function() {
    this.removeEventListener( "load", this._onLoad, this );
    this.removeEventListener( "changeFont", this._setDocumentStyle, this );
    this.removeEventListener( "changeTextColor", this._setDocumentStyle, this );
    this.removeEventListener( "changeBackgroundColor", this._setDocumentStyle, this );
    this._removeEventListeners();
  },
  
  statics : {
    EVENT_WIDGET_SELECTED : "org.eclipse.swt.events.widgetSelected",
    EVENT_MOUSE_UP : "org.eclipse.swt.events.mouseUp",
    EVENT_MOUSE_UP_BUTTON : "org.eclipse.swt.events.mouseUp.button",
    EVENT_MOUSE_UP_X : "org.eclipse.swt.events.mouseUp.x",
    EVENT_MOUSE_UP_Y : "org.eclipse.swt.events.mouseUp.y",
    EVENT_MOUSE_UP_TIME : "org.eclipse.swt.events.mouseUp.time",
    EVENT_MOUSE_DOWN : "org.eclipse.swt.events.mouseDown",
    EVENT_MOUSE_DOWN_BUTTON : "org.eclipse.swt.events.mouseDown.button",
    EVENT_MOUSE_DOWN_X : "org.eclipse.swt.events.mouseDown.x",
    EVENT_MOUSE_DOWN_Y : "org.eclipse.swt.events.mouseDown.y",
    EVENT_MOUSE_DOWN_TIME : "org.eclipse.swt.events.mouseDown.time"
  },

  members : {
    
    _addEventListeners : function() {
      try {
        var doc = this.getContentDocument();
        if( doc ) {
          qx.html.EventRegistration.addEventListener(
            doc, "mousedown", this.__handleMouseDownEvent );
          qx.html.EventRegistration.addEventListener(
            doc, "mouseup", this.__handleMouseUpEvent );
          doc.body.style.backgroundColor = this.getBackgroundColor();  
        }
      } catch( ex ) {}  
    },
    
    _removeEventListeners : function() {
      try {
        var doc = this.getContentDocument();
        if( doc ) {
          qx.html.EventRegistration.removeEventListener(
            doc, "mousedown", this.__handleMouseDownEvent );
          qx.html.EventRegistration.removeEventListener(
            doc, "mouseup", this.__handleMouseUpEvent );
        }
      } catch( ex ) {}
    },
    
    _setDocumentStyle : function( evt ) {
      try {
        var doc = this.getContentDocument();
        if( doc ) {
          var font = this.getFont();
          doc.body.style.fontFamily = font.getFamily();
          doc.body.style.fontSize = font.getSize();
          doc.body.style.paddingTop = this.getPaddingTop();
          doc.body.style.paddingRight = this.getPaddingRight();
          doc.body.style.paddingBottom = this.getPaddingBottom();
          doc.body.style.paddingLeft = this.getPaddingLeft();
          doc.body.style.color = this.getTextColor();
          doc.body.style.backgroundColor = this.getBackgroundColor();          
        }
      } catch( ex ) {}  
    },
        
    _onLoad : function( evt ) {
      this._setDocumentStyle();
      this._addEventListeners();      
      this._loaded = true;
    },
    
    _handleMouseDownEvent : function( evt ) {      
      this._selectionStart = this._caretOffset;
      this._selectionEnd = this._caretOffset;
      var eventId = org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN;      
      if( this._readyToSendChanges ) {
        this._readyToSendChanges = false;
        this._sendChanges( eventId, evt );
      }
    },
    
    _handleMouseUpEvent : function( evt ) {
      this._selectionStart = this._caretOffset;
      this._selectionEnd = this._caretOffset;
      var eventId = org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP;
      var selection = this._getSelection();      
      var range = this._getRange( selection );
      if( qx.core.Client.getInstance().isMshtml() ) {
        var text = range.text;
        var length = range.text.length;
        var countNewLines = 0;
        for( var i = 0; i < text.length; i++ ) {
          if( text.charAt( i ) == "\n" ) {
            countNewLines++;
          }
        }
        length -= countNewLines;
        range.collapse( false );        
        var parentElementId = range.parentElement().id;
        if( parentElementId.length > 0 
            && parentElementId.startsWith( 'sr' ) ) {
          var rng = range.duplicate();
          rng.collapse( false );        
          var offset = 0;        
          var tmpElementId = parentElementId;
          var move = rng.moveEnd( 'character', -1 );
          while( tmpElementId == parentElementId && move == -1) {
            offset++;
            move = rng.moveEnd( 'character', -1 );
            rng.collapse( false );          
            tmpElementId = rng.parentElement().id;
          }
          this._selectionEnd = parseInt( parentElementId.substr( 2 ) ) + offset; 
          this._selectionStart = this._selectionEnd - length;
        }
      } else {        
        if( range.startContainer.parentNode ) {
          var startElementId = range.startContainer.parentNode.id;
          var endElementId = range.endContainer.parentNode.id;
          if(    startElementId.length > 0 
              && startElementId.startsWith( 'sr' )
              && endElementId.length > 0 
              && endElementId.startsWith( 'sr' )
              ) {
            this._selectionStart = parseInt( startElementId.substr( 2 ) )
                                 + parseInt( range.startOffset );
            this._selectionEnd = parseInt( endElementId.substr( 2 ) )
                               + parseInt( range.endOffset );            
          }
        }
      }
      this._caretOffset = this._selectionEnd;
      if( this._readyToSendChanges ) {
        this._readyToSendChanges = false;
        this._sendChanges( eventId, evt );
      }
    },
    
    _getSelection : qx.core.Variant.select( "qx.client", {
      "mshtml" : function() {
        return this.getContentDocument().selection;
      },

      "default" : function() {
        return this.getContentWindow().getSelection();
      }
    }),
    
    _getRange : qx.core.Variant.select( "qx.client", {
      "mshtml" : function( sel ) {
        var doc = this.getContentDocument();        

        if( qx.util.Validation.isValid( sel ) ) {
          try {
            return sel.createRange();
          } catch( ex ) {
            return doc.createTextRange();
          }
        } else {
          return doc.createTextRange();
        }
      },

      "default" : function( sel ) {
        var doc = this.getContentDocument();
        this.setFocused( true );        

        if( qx.util.Validation.isValid( sel ) ) {
          try {
            return sel.getRangeAt( 0 );
          } catch( ex ) {
            return doc.createRange();
          }
        } else {
          return doc.createRange();
        }
      }
    }),
    
    _sendChanges : function( eventId, evt ) {
      if( !org_eclipse_rap_rwt_EventUtil_suspend ) {
        var widgetManager = org.eclipse.swt.WidgetManager.getInstance();
        var req = org.eclipse.swt.Request.getInstance();
        var id = widgetManager.findIdByWidget( this );        
        req.addParameter( id + ".caretOffset", this._caretOffset );
        if( this._hasMouseListener ) {
          if( eventId == org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN ) {            
            req.addEvent( org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN, id );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN_BUTTON,
                              this._determineMouseButton( evt ) );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN_X,
                              evt.clientX );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN_Y,
                              evt.clientY );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_DOWN_TIME,
                              this._eventTimestamp() );
          }
          if( eventId == org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP ) {
            req.addEvent( org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP, id );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP_BUTTON,
                              this._determineMouseButton( evt ) );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP_X,
                              evt.clientX );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP_Y,
                              evt.clientY );
            req.addParameter( id + "." + org.eclipse.swt.custom.StyledText.EVENT_MOUSE_UP_TIME,
                              this._eventTimestamp() );
          }            
        }
        if(    this._hasSelectionListener 
            && this._selectionStart != this._selectionEnd ) {
          req.addEvent( org.eclipse.swt.custom.StyledText.EVENT_WIDGET_SELECTED,
                        id );
          req.addParameter( id + ".selectionStart", this._selectionStart );
          req.addParameter( id + ".selectionEnd", this._selectionEnd );
        }
        if( this._hasSelectionListener || this._hasMouseListener ) {          
          req.send();
        }  
        this._readyToSendChanges = true;
      }
    },
    
    _eventTimestamp : function() {
      var app = qx.core.Init.getInstance().getApplication();
      return new Date().getTime() - app.getStartupTime();
    },
    
    _determineMouseButton : qx.core.Variant.select( "qx.client", {
      "mshtml" : function( evt ) {
        var result = 0;
        switch( evt.button ) {
          case 1:
            result = 1;
            break;
          case 2:
            result = 3;
            break;
          case 4:
            result = 2;
            break;          
        }
        return result;
      },
      
      "default" : function( evt ) {
        var result = 0;
        switch( evt.button ) {
          case 0:
            result = 1;
            break;
          case 1:
            result = 2;
            break;
          case 2:
            result = 3;
            break;          
        }
        return result;
      }
    }),
    
    _deselectAll : qx.core.Variant.select( "qx.client", {
      "mshtml" : function() {
        this._getSelection().empty(); 
      },
      
      "default" : function() {
        this._getSelection().removeAllRanges();
      }
    }),
    
    _select : qx.core.Variant.select( "qx.client", {
      "mshtml" : function() {
        var doc = this.getContentDocument();
        var elm = doc.getElementById( "sel" );
        if( elm ) {
          this._deselectAll();
          var range = doc.body.createTextRange();
          range.moveToElementText( elm );
          range.select();
          elm.scrollIntoView( true );
        }
      },
      
      "default" : function( objId ) {
        var doc = this.getContentDocument();
        var elm = doc.getElementById( "sel" );
        if( elm ) {
          this._deselectAll();
          var range = document.createRange();
          range.selectNode( elm );
          this._getSelection().addRange( range );
          elm.scrollIntoView( true );
          this.getContentWindow().focus();
        }
      }
    }),
    
    setHtml : function( value ) {      
      var doc = this.getContentDocument();
      if( !doc || !this._loaded ) {
        qx.client.Timer.once( function() {
          this.setHtml( value );
        }, this, 200 );
      } else {        
        doc.body.innerHTML = value;
      }
    },
    
    setSelection : function( selStart, selEnd ) {
      var doc = this.getContentDocument();
      if( !doc || !this._loaded ) {
        qx.client.Timer.once( function() {
          this.setSelection( selStart, selEnd );
        }, this, 200 );
      } else {
        this._selectionStart = selStart;
        this._selectionEnd = selEnd;
        if( this._selectionStart != this._selectionEnd ) {
          this._select();
        }
      }      
    },
    
    setCaretOffset : function( value ) {
      this._caretOffset = value;
    },
    
    setHasSelectionListener : function( value ) {
      this._hasSelectionListener = value;
    },
    
    setHasMouseListener : function( value ) {
      this._hasMouseListener = value;
    }
  }
});
