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

qx.Class.define( "org.eclipse.swt.Request", {
  type : "singleton",
  extend : qx.core.Target,

  construct : function() {
    this.base( arguments );
    // the URL to which the requests are sent
    this._url = "";
    // the map of parameters that will be posted with the next call to 'send()'
    this._parameters = {};
    // instance variables that hold the essential request parameters
    this._uiRootId = "";
    this._requestCounter;
    // Number of currently running or scheduled requests, used to determine when
    // to show the wait hint (e.g. hour-glass cursor)
    this._runningRequestCount = 0;
    // Flag that is set to true if send() was called but the delay timeout
    // has not yet timed out
    this._inDelayedSend = false;
    // As the CallBackRequests get blocked at the server to wait for
    // background activity I choose a large timeout...
    var requestQueue = qx.io.remote.RequestQueue.getInstance();
    requestQueue.setDefaultTimeout( 60000 * 60 * 24 ); // 24h
    // Initialize the request queue to allow only one request at a time
    requestQueue.setMaxConcurrentRequests( 1 );
    // References the currently running request or null if no request is active
    this._currentRequest = null;
    // References the last running transport
    this._lastTransport = null;
    this._timeoutPage = "";
  },

  destruct : function() {
    this._currentRequest = null;
  },

  events : {
    "send" : "qx.event.type.DataEvent",
    "received" : "qx.event.type.DataEvent"
  },

  members : {
    setUrl : function( url ) {
      this._url = url;
    },

    getUrl : function() {
      return this._url;
    },

    setUIRootId : function( uiRootId ) {
      this._uiRootId = uiRootId;
    },

    getUIRootId : function() {
      return this._uiRootId;
    },

    setRequestCounter : function( requestCounter ) {
      this._requestCounter = requestCounter;
    },

    getRequestCounter : function() {
      return this._requestCounter;
    },

    setTimeoutPage : function( content ) {
      this._timeoutPage = content;
    },

    /**
     * Adds a request parameter to this request with the given name and value
     */
    addParameter : function( name, value ) {
      this._parameters[ name ] = value;
    },

    /**
     * Removes the parameter denoted by name from this request.
     */
    removeParameter : function( name ) {
      delete this._parameters[ name ];
    },

    /**
     * Returns the parameter value for the given name or null if no parameter
     * with such a name exists.
     */
    getParameter : function( name ) {
      var result = this._parameters[ name ];
      if( result === undefined ) {
        result = null;
      }
      return result;
    },

    /**
     * Adds the given eventType to this request. The sourceId denotes the id of
     * the widget that caused the event.
     */
    addEvent : function( eventType, sourceId ) {
      this._parameters[ eventType ] = sourceId;
    },

    /**
     * To enable server callbacks to the UI this method sends a request
     * that will be blocked by the server till background activities
     * require UI updates.
     */
    enableUICallBack : function() {
      var request = new qx.io.remote.Request( this._url,
                                              qx.net.Http.METHOD_GET,
                                              qx.util.Mime.JAVASCRIPT );
      request.setParameter(
        "custom_service_handler",
        "org.eclipse.rwt.internal.lifecycle.UICallBackServiceHandler" );
      this._sendStandalone( request );
    },

    /**
     * Sends this request asynchronously. All parameters that were added since
     * the last 'send()' will now be sent.
     */
    send : function() {
      if( !this._inDelayedSend ) {
        this._inDelayedSend = true;
        // Wait and then actually send the request
        // TODO [rh] optimize wait interval (below 60ms seems to not work
        //      reliable)
        var func = function() { this._sendImmediate( true ) };
        qx.client.Timer.once( func, this, 60 );
      }
    },

    sendSyncronous : function() {
      this._sendImmediate( false );
    },

    _sendImmediate : function( async ) {
      this._dispatchSendEvent();
      // set mandatory parameters; do this after regular params to override them
      // in case of conflict
      this._parameters[ "uiRoot" ] = this._uiRootId;
      if( this._requestCounter == -1 ) {
        // TODO [fappel]: This is a workaround that prevents sending a request
        // without a valid request id. Needed for background proccessing.
        this._inDelayedSend = false;
        this.send();
      } else {
        if( this._requestCounter != null ) {
          this._parameters[ "requestCounter" ] = this._requestCounter;
          this._requestCounter = -1;
        }
        // create and configure request object
        var request = this._createRequest();
        request.setAsynchronous( async );
        // copy the _parameters map which was filled during client interaction
        // to the request
        this._inDelayedSend = false;
        this._copyParameters( request );
        this._logSend();
        this._runningRequestCount++;
        // notify user when request takes longer than 500 ms
        if( this._runningRequestCount === 1 ) {
          qx.client.Timer.once( this._showWaitHint, this, 500 );
        }
        // clear the parameter list
        this._parameters = {};
        // queue request to be sent (async) or send and block (sync)
        if( async ) {
          request.send();
        } else {
          this._sendStandalone( request );
        }
      }
    },

    _copyParameters : function( request ) {
      var data = new Array();
      for( var parameterName in this._parameters ) {
        data.push(   encodeURIComponent( parameterName )
                   + "="
                   + encodeURIComponent( this._parameters[ parameterName ] ) );
      }
      request.setData( data.join( "&" ) );
    },

    _createRequest : function() {
      var result = new qx.io.remote.Request( this._url,
                                             qx.net.Http.METHOD_POST,
                                             qx.util.Mime.TEXT );
      result.addEventListener( "sending", this._handleSending, this );
      result.addEventListener( "completed", this._handleCompleted, this );
      result.addEventListener( "failed", this._handleFailed, this );
      return result;
    },

    _logSend : function() {
      if( qx.core.Variant.isSet( "qx.debug", "on" ) ) {
        var msg = "sending request [ ";
        for( var parameterName in this._parameters ) {
          msg += parameterName + "=" + this._parameters[ parameterName ] + "; ";
        }
        msg += "]";
        this.debug( msg );
      }
    },

    _sendStandalone : function( request ) {
      // [if] Dispose the last transport before creating the new one
      // see bug 301261
      if( this._lastTransport != null ) {
        this._lastTransport.getRequest().dispose();
        this._lastTransport.dispose();
      }
      // TODO [rh] WORKAROUND
      //      we would need two requestQueues (one for 'normal' requests that
      //      is limited to 1 concurrent request and one for the 'independant'
      //      requests created here
      //      Until qooxdoo supports multiple requestQueues we create and
      //      send this kind of request without knownledge of the request queue
      var vRequest = request;
      var vTransport = new qx.io.remote.Exchange(vRequest);
      // Establish event connection between qx.io.remote.Exchange instance and
      // qx.io.remote.Request
      vTransport.addEventListener("sending", vRequest._onsending, vRequest);
      vTransport.addEventListener("receiving", vRequest._onreceiving, vRequest);
      vTransport.addEventListener("completed", vRequest._oncompleted, vRequest);
      vTransport.addEventListener("aborted", vRequest._onaborted, vRequest);
      vTransport.addEventListener("timeout", vRequest._ontimeout, vRequest);
      vTransport.addEventListener("failed", vRequest._onfailed, vRequest);
      vTransport._start = (new Date).valueOf();
      this._lastTransport = vTransport;
      vTransport.send();
      // END WORKAROUND
    },

    ////////////////////////
    // Handle request events

    _handleSending : function( evt ) {
      var exchange = evt.getTarget();
      this._currentRequest = exchange.getRequest();
    },

    _handleFailed : function( evt ) {
      // [vt] workaround for bug #253756: Copied code from _handleSending since
      // the sending phase is skipped on failure in IE
      // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=253756
      var exchange = evt.getTarget();
      this._currentRequest = exchange.getRequest();
      var giveUp = true;
      if( this._isConnectionError( evt.getStatusCode() ) ) {
        giveUp = !this._handleConnectionError( evt );
      }
      if( giveUp ) {
        this._hideWaitHint();
        var content;
        var text = null;
        var request = evt.getTarget().getImplementation().getRequest();
        // [if] typeof(..) == "unknown" is IE specific. Used to prevent error:
        // "The data  necessary to complete this operation is not yet available"
        if( typeof( request.responseText ) != "unknown" ) {
          text = request.responseText;
        }
        if( text == "" || text == null ) {
          content
            = "<html><head><title>Error Page</title></head><body>"
            + "<p>Request failed.</p><pre>"
            + "HTTP Status Code: "
            + String( evt.getStatusCode() )
            + "</pre></body></html>";
        } else {
          content = text;
        }
        this._writeErrorPage( content );
      }
    },

    _handleCompleted : function( evt ) {
      var text = evt.getTarget().getImplementation().getRequest().responseText;
      if( text && text.indexOf( "<!DOCTYPE" ) === 0 ) {
        // Handle request to timed out session: write info page and offer
        // link to restart application. This way was chosen for two reasons:
        // - with rendering an anchor tag we can restart the same entry point as
        //   is currently used
        // - as clicking the link issues a regular request, we can be sure that
        //   the stale application will be cleaned up properly by the browser
        var hrefAttr = "href=\"" + window.location + "\"";
        var content = this._timeoutPage.replace( /{HREF_URL}/, hrefAttr );
        this._writeErrorPage( content );
      } else {
        var errorOccured = false;
        try {
          if( text && text.length > 0 ) {
            org.eclipse.swt.EventUtil.suspendEventHandling();
            window.eval( text );
            qx.ui.core.Widget.flushGlobalQueues();
            org.eclipse.swt.EventUtil.resumeEventHandling();
          }
          this._runningRequestCount--;
          this._hideWaitHint();
        } catch( ex ) {
          this.error( "Could not execute javascript: [" + text + "]", ex );
          var content
            = "<html><head><title>Error Page</title></head><body>"
            + "<p>Could not evaluate javascript response:</p><pre>"
            + ex
            + "\n\n"
            + text
            + "</pre></body></html>";
          this._writeErrorPage( content );
          errorOccured = true;
        }
        if( !errorOccured ) {
          this._dispatchReceivedEvent();
        }
      }
    },

    ///////////////////////////////
    // Handling connection problems

    _handleConnectionError : function( evt ) {
      var msg
        = "The server seems to be temporarily unavailable.\n"
        + "Would you like to retry?";
      var result = confirm( msg );
      if( result ) {
        var request = this._createRequest();
        var failedRequest = this._currentRequest;
        request.setAsynchronous( failedRequest.getAsynchronous() );
        // Reusing the same request object causes strange behaviour, therefore
        // create a new request and copy the relevant parts from the failed one
        var failedHeaders = failedRequest.getRequestHeaders();
        for( var headerName in failedHeaders ) {
          request.setRequestHeader( headerName, failedHeaders[ headerName ] );
        }
        var failedParameters = failedRequest.getParameters();
        for( var parameterName in failedParameters ) {
          request.setParameter( parameterName,
                                failedParameters[ parameterName ] );
        }
        request.setData( failedRequest.getData() );
        this._restartRequest( request );
      }
      return result;
    },

    _restartRequest : function( request ) {
      // TODO [rh] this is adapted from qx.io.remote.RequestQueue#add as there
      //      is no official way to insert a new request as the first one in
      //      RequestQueue
      request.setState( "queued" );
      var requestQueue = qx.io.remote.RequestQueue.getInstance();
      qx.lang.Array.insertAt( requestQueue._queue, request, 0 );
      requestQueue._check();
      if( requestQueue.getEnabled() ) {
        requestQueue._timer.start();
      }
    },

    _isConnectionError : function( statusCode ) {
      var result;
      if( qx.core.Variant.isSet( "qx.client", "mshtml" ) ) {
        // for a description of the IE status codes, see
        // http://support.microsoft.com/kb/193625
        result = (    statusCode === 12007    // ERROR_INTERNET_NAME_NOT_RESOLVED
                   || statusCode === 12029    // ERROR_INTERNET_CANNOT_CONNECT
                   || statusCode === 12030    // ERROR_INTERNET_CONNECTION_ABORTED
                   || statusCode === 12031    // ERROR_INTERNET_CONNECTION_RESET
                   || statusCode === 12152 ); // ERROR_HTTP_INVALID_SERVER_RESPONSE
      } else if( qx.core.Variant.isSet( "qx.client", "gecko" ) ) {
        // Firefox 3 reports other statusCode than oder versions (bug #249814)
        // Check if Gecko > 1.9 is running (used in FF 3)
        // Gecko/app integration overview: http://developer.mozilla.org/en/Gecko
        if( qx.core.Client.getMajor() * 10 + qx.core.Client.getMinor() >= 19 ) {
          result = ( statusCode === 0 );
        } else {
          result = ( statusCode === -1 );
        }
      } else if( qx.core.Variant.isSet( "qx.client", "webkit" ) ) {
        result = ( statusCode === 0 );
      } else if( qx.core.Variant.isSet( "qx.client", "opera" ) ) {
        result = ( statusCode === 0 );
      } else {
        result = false;
      }
      return result;
    },

    ///////////////////////////////////////////////////
    // Wait hint - UI feedback while request is running

    _showWaitHint : function() {
      if( this._runningRequestCount > 0 ) {
        var doc = qx.ui.core.ClientDocument.getInstance();
        doc.setGlobalCursor( qx.constant.Style.CURSOR_PROGRESS );
      }
    },

    _hideWaitHint : function() {
      if( this._runningRequestCount === 0 ) {
        var doc = qx.ui.core.ClientDocument.getInstance();
        doc.setGlobalCursor( null );
      }
    },

    _dispatchSendEvent : function() {
      if( this.hasEventListeners( "send" ) ) {
        var event = new qx.event.type.DataEvent( "send", this );
        this.dispatchEvent( event, true );
      }
    },

    _dispatchReceivedEvent : function() {
      if( this.hasEventListeners( "received" ) ) {
        var event = new qx.event.type.DataEvent( "received", this );
        this.dispatchEvent( event, true );
      }
    },

    _writeErrorPage : function( content ) {
      // shutdown or disable all things that could interfere with showing the
      // error page
      var app = qx.core.Init.getInstance().getApplication();
      app.setExitConfirmation( null );
      qx.io.remote.RequestQueue.getInstance().setEnabled( false );
      // write the error page content
      var client = qx.core.Client.getInstance();
      if( client.isMshtml() && client.getMajor() == 6 ) {
        // do nothing for IE6, see bug 264150: Error page is broken in IE6
      } else {
        document.open( "text/html", true );
      }
      document.write( content );
      document.close();
    }
  }
});
