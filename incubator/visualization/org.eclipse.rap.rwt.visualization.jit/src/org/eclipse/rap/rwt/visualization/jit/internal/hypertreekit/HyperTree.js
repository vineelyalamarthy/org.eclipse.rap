/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle.
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 * 
 *****************************************************************************/
qx.Class.define("org.eclipse.rap.rwt.visualization.jit.HyperTree",
{ extend :qx.ui.layout.CanvasLayout,
	
	construct : function(id) {
		this.base(arguments);
		this.setHtmlProperty("id", id);
		this._id = id;
		this._viz = null;
	},

	properties : {
		visible : {
			init :"",
			apply :"load"
	    },
	    widgetData : {
	    	init :"",
			apply :"refreshData"
	    },
	    nodeColor : {
	    	init :"",
			apply :"applyNodeColor"
	    },
	    edgeColor : {
	    	init :"",
			apply :"applyEdgeColor"
	    }
	},

	destruct : function() {

	},

	members : {

		_doActivate : function() {
			var shell = null;
			var parent = this.getParent();
			while (shell == null && parent != null) {
				if (parent.classname == "org.eclipse.swt.widgets.Shell") {
					shell = parent;
				}
				parent = parent.getParent();
			}
			if (shell != null) {
				shell.setActiveChild(this);
			}
		},
		
		load : function() {
	      var parent = this;
		  try {
			var vis = this.getVisible();
			if (vis == "false") {
				// make invisible
				return;
			}
			qx.ui.core.Widget.flushGlobalQueues();
			if (this._viz == null) {
				this.info("Creating hypertree.");
				
				var qParent = document.getElementById(this._id);
				var vizParent = document.createElement("div");
//				var vizParent = document.createElement("canvas");
				vizParent.setAttribute("id", "vizParent"+this._id);
				qParent.appendChild(vizParent);
				var canvas = new Canvas('vizCanvas'+this._id, {
			        'injectInto': "vizParent"+this._id,
			        'width': this.getWidth(),
			        'height': this.getHeight()
			    });
				qParent._canvas = canvas;
//				
//				if (qx.core.Client.isMshtml()) {
//					vizParent = G_vmlCanvasManager.initElement(vizParent);
//				}
				vizParent.width = this.getWidth();
				vizParent.height = this.getHeight();
				var widgetId = this._id;
				var config = {
				        //Change node and edge styles such as
				        //color, width and dimensions.
				        Node: {
				            dim: 9,
				            overridable: true,
				            color: "#f00"
				        },
				        
				        Edge: {
				            overridable: true,
				            lineWidth: 2,
				            color: "#088"
				        },
				        
				        onBeforeCompute: function(node){
				            //Log.write("centering");
				        },
				        //Attach event handlers and add text to the
				        //labels. This method is only triggered on label
				        //creation
				        onCreateLabel: function(domElement, node){
				            domElement.innerHTML = node.name;
				            domElement.style.cursor = "pointer";
				            parent.addTreeEvent(domElement, 'click', function () {
				                ht.onClick(node.id);
				            });
				        },
				        
				        //This method is called right before plotting an
				        //edge. This method is useful for adding individual
				        //styles to edges.
				        onBeforePlotLine: function(adj){
				            //Set lineWidth for edges.
//				            if (!adj.data.$lineWidth) 
//				                adj.data.$lineWidth = Math.random() * 5 + 1;
				        },
				        //Change node styles when labels are placed
				        //or moved.
				        onPlaceLabel: function(domElement, node){
				            var style = domElement.style;
				            style.display = '';
				            style.cursor = 'pointer';
				            var font = parent.getFont();
				            style.fontFamily = font.getFamily();
				            style.fontStyle = font.generateStyle();
				            var color = parent.getTextColor();
				            if (node._depth <= 1) {
				                style.fontSize = font.getSize();
				                style.color = color;

				            } else if(node._depth == 2){
				            	style.fontSize = font.getSize()-2;
				                style.color = "#555";

				            } else {
				                style.display = 'none';
				            }

				            var left = parseInt(style.left);
				            var w = domElement.offsetWidth;
				            style.left = (left - w / 2) + 'px';
				        },
				        
				        onAfterCompute: function(){
				        	var node = Graph.Util.getClosestNodeToOrigin(ht.graph, "pos");
				        	qParent.selection = node;
			            	//fire selection event
			            	var req = org.eclipse.swt.Request.getInstance();
			            	req.addParameter(widgetId + ".selectedNode", node.id);
			            	req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
			            	req.send();
				        }
				    };
			    var ht = new Hypertree(canvas, config);
			    
			    this.addEventListener("changeWidth", function(e) {
					if (canvas != null) {
						canvas.width = e.getValue();
						vizParent.width = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
						ht.refresh(true);
					}
				});
				this.addEventListener("changeHeight", function(e) {
					if (canvas != null) {
						canvas.height = e.getValue();
						vizParent.height = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
						ht.refresh(true);
					}
				});
			    
				this._viz = ht;
				this._vizParent = vizParent;
			}
		  }
		  catch (e) {
			 this.info(e);
		  }
		},
		
		
		
		addTreeEvent : function (obj, type, fn) {
		    if (obj.addEventListener) obj.addEventListener(type, fn, false);
		    else obj.attachEvent('on' + type, fn);
		},
		
		refreshData : function (data) {
			try {
				var ht = this._viz;
				if (ht != null) {
					if (data != null) {
						this.info("Loading hypertree data.");
						ht.loadJSON(data);
					    this.info("Refreshing hypertree.");
					    ht.refresh();
					    ht.controller.onAfterCompute();
					}
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		applyNodeColor : function (color) {
			try {
				this.info("Setting node color: "+color);
				var ht = this._viz;
				if (ht != null) {
					ht.controller.Node.color = color;
					ht.refresh(true);
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		applyEdgeColor : function (color) {
			try {
				this.info("Setting edge color: "+color);
				var ht = this._viz;
				if (ht != null) {
					ht.controller.Edge.color = color;
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		selectNode : function (id) {
			try {
				this.info("Forcing selection to : "+id);
				var ht = this._viz;
				if (ht != null) {
					ht.onClick(id);
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		_doResize : function() {
			qx.ui.core.Widget.flushGlobalQueues();
			
		}
	}
});