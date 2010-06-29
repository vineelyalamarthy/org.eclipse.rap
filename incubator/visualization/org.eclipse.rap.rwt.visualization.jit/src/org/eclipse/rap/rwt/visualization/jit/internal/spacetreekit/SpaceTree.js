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
qx.Class.define("org.eclipse.rap.rwt.visualization.jit.SpaceTree",
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
				this.info("Creating spacetree.");
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
			        'height': this.getHeight()
			    });
				qParent._canvas = canvas;
//				
//				if (qx.core.Client.isMshtml()) {
//					vizParent = G_vmlCanvasManager.initElement(vizParent);
//				}
				var widgetId = this._id;
				vizParent.width = this.getWidth();
				vizParent.height = this.getHeight();
			    var st = new ST(canvas, {
			    	orientation: "left",  
			    	  levelsToShow: 2,  
			    	  subtreeOffset: 8,  
			    	  siblingOffset: 5,  
			    	  levelDistance: 30,  
			    	  withLabels: true,  
			    	  align: "center",  
			    	  multitree: false,  
			    	  indent: 10,  
			    	  //set distance between node and its children
			          levelDistance: 50,
			    	  Node: {  
			    	    overridable: true,  
			    	    type: 'rectangle',  
			    	    color: '#ccb',  
			    	    lineWidth: 1,  
			    	    height: 20,  
			    	    width: 90,  
			    	    dim: 15,  
			    	    align: "center"  
			    	  },  
			    	  Edge: {  
			    	    overridable: true,  
			    	    type: 'bezier',  
			    	    color: '#ccc',  
			    	    dim: 15,  
			    	    lineWidth: 1  
			    	  },  
			    	  duration: 700,  
			    	  fps: 25,  
			    	  transition: Trans.Quart.easeInOut,  
			    	  clearCanvas: true,  
			    	  
			    	  onBeforeCompute: function(node) {  
			    	    //do something onBeforeCompute  
			    	  },  
			    	  onAfterCompute: function(){
			        	  var node = Graph.Util.getClosestNodeToOrigin(st.graph, "pos");
			        	  qParent.selection = node;
			        	  //fire selection event
			        	  var req = org.eclipse.swt.Request.getInstance();
			        	  req.addParameter(widgetId + ".selectedNode", node.id);
			        	  req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
			        	  req.send();
			          },  
			    	  onCreateLabel:   function(domElement, node) {  
			    		  domElement.id = node.id;            
			    		  domElement.innerHTML = node.name;
			    		  domElement.onclick = function(){
			                  st.onClick(node.id);
			              };
			              //set label styles
			              var style = domElement.style;
			              var font = parent.getFont();
			              style.fontFamily = font.getFamily();
			              style.fontStyle = font.generateStyle();
			              style.fontSize = font.getSize();
			              style.width = 60 + 'px';
			              style.height = 17 + 'px';            
			              style.cursor = 'pointer';
			              style.textAlign= 'center';
			              style.paddingTop = '3px';  
			              var color = parent.getTextColor();
		            	  style.color = color;
			    	  },  
			    	  onPlaceLabel:    function(domElement, node) {  
			    	    //do something onPlaceLabel  
			    	  },  
			    	  onBeforePlotNode:function(node) {  
			    		//add some color to the nodes in the path between the
			              //root node and the selected node.
			              if (node.selected) {
			                  node.data.$color = "#ff7";
			              }
			              else {
			                  delete node.data.$color;
			                  var GUtil = Graph.Util;
			                  //if the node belongs to the last plotted level
			                  if(!GUtil.anySubnode(node, "exist")) {
			                      //count children number
			                      var count = 0;
			                      GUtil.eachSubnode(node, function(n) { count++; });
			                      //assign a node color based on
			                      //how many children it has
			                      node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];                    
			                  }
			              }  
			    	  },  
			    	  onAfterPlotNode: function(node) {  
			    	    //do something onAfterPlotNode  
			    	  },  
			    	  onBeforePlotLine:function(adj) {  
			    		  if (adj.nodeFrom.selected && adj.nodeTo.selected) {
			                  adj.data.$color = "#eed";
			                  adj.data.$lineWidth = 3;
			              }
			              else {
			                  delete adj.data.$color;
			                  delete adj.data.$lineWidth;
			              }  
			    	  },  
			    	  onAfterPlotLine: function(adj) {  
			    	    //do something onAfterPlotLine  
			    	  },
			    	  request:         false
			    });
			    
			    this.addEventListener("changeWidth", function(e) {
					if (canvas != null) {
						canvas.width = e.getValue();
						vizParent.width = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
	             		st.refresh();
					}
				});
				this.addEventListener("changeHeight", function(e) {
					if (canvas != null) {
						canvas.height = e.getValue();
						vizParent.height = e.getValue();
						canvas.resize(vizParent.width, vizParent.height);
						st.refresh();
					}
				});
			    
				this._viz = st;
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
		      var st = this._viz;
		      if (st != null) {
		    	  this.info("Loading spacetree data.");
		    	  var data = this.getWidgetData();
		    	  if (data != null) {
	//	    	    this.info(data);
		    	    st.loadJSON(data);
		    	    this.info("Refreshing spacetree.");
		    	    //compute node positions and layout  
		    	    st.compute();  
		    	    //Emulate a click on the root node.  
		    	    st.onClick(st.root); 
//				    st.refresh();
//	  		        ht.controller.onAfterCompute();
		    	  }
		      }
			}
			catch (e) {
				this.info(e);
			}
		},
		
		applyNodeColor : function (color) {
			try {
				var st = this._viz;
				if (st != null) {
					st.controller.Node.color = color;
					st.refresh();
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		applyEdgeColor : function (color) {
			try {
				var st = this._viz;
				if (st != null) {
					st.controller.Edge.color = color;
					st.refresh();
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		selectNode : function (id) {
			try {
				var st = this._viz;
				if (st != null) {
					st.onClick(id);
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