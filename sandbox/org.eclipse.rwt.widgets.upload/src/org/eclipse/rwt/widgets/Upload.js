/*******************************************************************************
 * Copyright (c) 2002-2007 Critical Software S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tiago Rodrigues (Critical Software S.A.) - initial implementation
 *     Joel Oliveira (Critical Software S.A.) - initial commit
 ******************************************************************************/
qx.Class.define( "org.eclipse.rwt.widgets.Upload", {
  extend: qx.ui.layout.VerticalBoxLayout,

  construct : function( servlet, showProgress ) {
    this.base( arguments );

    this._servlet = servlet;
    this._isStarted = false;

    this.initHeight();
    this.initOverflow();

    var topLayout = new qx.ui.layout.HorizontalBoxLayout();
    topLayout.set({left:0,right:0,height:'auto'});

    // Upload Form
    this._uploadForm = new qx.ui.custom.UploadForm("uploadForm", this._servlet);
    this._uploadForm.set({top:0,left:0,width:"1*"});
    topLayout.add(this._uploadForm);

    // Browse File Button
    this._uploadField = new qx.ui.custom.UploadField("uploadFile", "Browse");
    this._uploadField.set({left:0,right:0});
    this._uploadForm.add(this._uploadField);

    // Upload Button
    this._uploadButton = new qx.ui.form.Button("Upload");
    this._uploadButton.addEventListener("click", this._uploadFile, this);
    topLayout.add(this._uploadButton);

    this.add(topLayout);

    if (showProgress) {
        var bottomLayout = new qx.ui.layout.CanvasLayout();
        bottomLayout.set({left:0,height:'auto',marginTop:2});
        bottomLayout.setOverflow("hidden");
        bottomLayout.setAppearance( "progressbar" );

        // Progress Bar
        this._progressBar = new qx.ui.layout.CanvasLayout();
        this._progressBar.setParent( bottomLayout );
        this._progressBar.set({left:0,height:16});
        this._progressBar.setAppearance( "progressbar-bar" );

        this.add(bottomLayout);

        this._uploadForm.addEventListener("sending", this._monitorUpload, this);
        this._uploadForm.addEventListener("completed", this._cleanUp, this);
    }

    this.addEventListener("upload", this._fireEvent, this);
  },

  events: {
    "upload" : "qx.event.type.DataEvent"
  },

  properties :
  {
    /**
     * The last file that was uploaded.
     */
    lastFileUploaded :
    {
      check : "String",
      init  : ""
    },
    /**
     * The parcial size of the file already uploaded.
     */
    uploadParcial :
    {
      check : "String",
      init  : "0"
    },
    /**
     * The total size of the uploaded file.
     */
    uploadTotal :
    {
      check : "String",
      init : "0"
    }
  },

  members : {
    _fireEvent : function (e) {
        var wm = org.eclipse.swt.WidgetManager.getInstance();
        var id = wm.findIdByWidget(this);
        var req = org.eclipse.swt.Request.getInstance();
        req.addParameter(id + ".finished", e.getData());
        req.addParameter(id + ".uploadParcial", this.getUploadParcial());
        req.addParameter(id + ".uploadTotal", this.getUploadTotal());
        req.send();
    },

    _cleanUp : function () {
        this.setLastFileUploaded(this._uploadField.getValue());
        this._progressBar.setWidth("0%");
        this._uploadField.setValue("");
        this._isStarted = false;

        this.createDispatchDataEvent("upload", "true");
    },

    _uploadFile : function () {
        if (this._uploadField.getValue() != "") {
            this._isStarted = true;
            this._uploadForm.send();
        }
    },

    _monitorUpload : function () {
        qx.ui.core.Widget.flushGlobalQueues();

        this._req = qx.net.HttpRequest.create();
        this._req.onreadystatechange = qx.lang.Function.bind(this._processStateChange, this);
        this._req.open("GET", this._servlet, true);
        this._req.send(null);
    },

    _processStateChange : function () {
        if (this._req.readyState == 4) {
            if (this._req.status == 200) {
                var xml = this._req.responseXML;

                var notFinished = xml.getElementsByTagName("finished")[0];
                var percentCompleted = xml.getElementsByTagName("percent_complete")[0];

                // Check to see if it's even started yet
                if ((notFinished == null) && (percentCompleted == null)) {
                    qx.client.Timer.once(this._monitorUpload, this, 100);
                }
                else {
                    var bytesRead = xml.getElementsByTagName("bytes_read")[0];
                    var contentLength = xml.getElementsByTagName("content_length")[0];

                    this.setUploadParcial(bytesRead.firstChild.data);
                    this.setUploadTotal(contentLength.firstChild.data);

                    // Started, get the status of the upload
                    if (percentCompleted != null) {
                        this._progressBar.setWidth(percentCompleted.firstChild.data + "%");

                        qx.client.Timer.once(this._monitorUpload, this, 100);
                    }
                    else {
                        // Finished!
                    }

                    this.createDispatchDataEvent("upload", "false");
                }
            }
            else {
                this.debug("HTTP Response NOK: "+ this._req.statusText);
            }
        }
    }
  }
});
