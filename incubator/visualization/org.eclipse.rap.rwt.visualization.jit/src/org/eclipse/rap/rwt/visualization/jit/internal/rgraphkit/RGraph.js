/******************************************************************************
 * Copyright © 2010-2011 Texas Center for Applied Technology
 * Texas Engineering Experiment Station
 * The Texas A&M University System
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle (Texas Center for Applied Technology) -
 *       initial API and implementation
 * 
 *****************************************************************************/
qx.Class.define("org.eclipse.rap.rwt.visualization.jit.RGraph",
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
				this.info("Creating rgraph.");
				var qParent = document.getElementById(this._id);
				var vizParent = document.createElement("div");
				var vizId = "vizParent"+this._id;
				vizParent.setAttribute("id", vizId);
				qParent.appendChild(vizParent);
				/* style for node labels */
//				var vizStyle = ".node {color: white;background-color:transparent;cursor:pointer;font-weight:bold;opacity:0.9;} .node:hover {cursor:pointer;color: #222;background-color:white;font-weight:bold;opacity:1;}";
//				qx.html.StyleSheet.createElement(vizStyle);
				
				var canvas = new Canvas('vizCanvas'+this._id, {
			        'injectInto': "vizParent"+this._id,
			        'width': this.getWidth(),
			        'height': this.getHeight(),
			        //Optional: create a background canvas and plot
			        //concentric circles in it.
			        'backgroundCanvas': {
			            'styles': {
			                'strokeStyle': '#555'
			            },
			            
			            'impl': {
			                'init': function(){},
			                'plot': function(canvas, ctx){
			                    var times = 6, d = 100;
			                    var pi2 = Math.PI * 2;
			                    for (var i = 1; i <= times; i++) {
			                        ctx.beginPath();
			                        ctx.arc(0, 0, i * d, 0, pi2, true);
			                        ctx.stroke();
			                        ctx.closePath();
			                    }
			                }
			            }
				    }
			    });
				qParent._canvas = canvas;
//				
//				if (qx.core.Client.isMshtml()) {
//					vizParent = G_vmlCanvasManager.initElement(vizParent);
//				}
				var widgetId = this._id;
				vizParent.width = this.getWidth();
				vizParent.height = this.getHeight();
			    var rg = new RGraph(canvas, {
			    	 //interpolation type, can be linear or polar  
			        //interpolation: 'linear',  
			        //parent-children distance  
			        //levelDistance: 100,  
			          //withLabels: true,
			    	  duration: 1000,  
			    	  fps: 25,
			          Node: {  
//			            overridable: false,  
//			            type: 'circle',
			            color: '#ccddee'
//			            lineWidth: 1,  
//			            height: 5,  
//			            width: 5,  
//			            dim: 3  
			          },  
			          Edge: {  
//			            overridable: false,  
//			            type: 'line',  
			            color: '#772277' 
//			            lineWidth: 1  
			          },  
			          onBeforeCompute: function(node) {  
			            //do something onBeforeCompute  
			          },  
			          onAfterCompute: function(){
			        	  var node = Graph.Util.getClosestNodeToOrigin(rg.graph, "pos");
			        	  qParent.selection = node;
			        	  //fire selection event
			        	  var req = org.eclipse.swt.Request.getInstance();
			        	  req.addParameter(widgetId + ".selectedNode", node.id);
			        	  req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
			        	  req.send();
			          },  
			        //Change some label dom properties.
			          //This method is called each time a label is plotted.
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
			          
			          //Add a controller to make the tree move on click.  
			          onCreateLabel: function(domElement, node) {  
			        	  domElement.innerHTML = node.name;
			        	  domElement.onclick = function() {
			        		  rg.onClick(node.id);  
			        	  };  
			          },   
			          onBeforePlotNode:function(node) {  
			            //do something onBeforePlotNode  
			          },  
			          onAfterPlotNode: function(node) {  
			            //do something onAfterPlotNode  
			          },  
			          onBeforePlotLine:function(adj) {  
			            //do something onBeforePlotLine  
			          },  
			          onAfterPlotLine: function(adj) {  
			            //do something onAfterPlotLine  
			          }  
			    });
			    
			    this.addEventListener("changeWidth", function(e) {
					if (canvas != null) {
						canvas.width = e.getValue();
						vizParent.width = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
	             		rg.refresh();
					}
				});
				this.addEventListener("changeHeight", function(e) {
					if (canvas != null) {
						canvas.height = e.getValue();
						vizParent.height = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
						rg.refresh();
					}
				});
			    
				this._viz = rg;
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
		
		refreshData : function () {
			try {
		      var rg = this._viz;
		      if (rg != null) {
		    	  var data = this.getWidgetData();
		    	  if (data != null) {
		    		this.info("Loading rgraph data.");
	//	    	    this.info(data);
		    	    rg.loadJSON(data);
		    	    this.info("Refreshing rgraph.");
				    rg.refresh();
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
				var rg = this._viz;
				if (rg != null) {
					rg.controller.Node.color = color;
					rg.refresh();
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		applyEdgeColor : function (color) {
			try {
				var rg = this._viz;
				if (rg != null) {
					rg.controller.Edge.color = color;
					rg.refresh();
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		selectNode : function (id) {
			try {
				var rg = this._viz;
				if (rg != null) {
					rg.onClick(id);
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