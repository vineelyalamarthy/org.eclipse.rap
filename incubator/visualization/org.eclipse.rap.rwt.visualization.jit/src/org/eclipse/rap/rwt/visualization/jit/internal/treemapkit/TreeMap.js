/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle and others.
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *       data passing and widget scaling
 *     
 * 
 *****************************************************************************/
qx.Class.define("org.eclipse.rap.rwt.visualization.jit.TreeMap",
{ extend :qx.ui.layout.CanvasLayout,
	
	construct : function(id,type) {
		this.base(arguments);
		this.setHtmlProperty("id", id);
		this._id = id;
		this._viz = null;
		this._type = type;
	},

	properties : {
		visible : {
			init :"",
			apply :"load"
	    },
	    widgetData : {
	    	init :"",
			apply :"refreshData"
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
				this.info("Creating treemap.");
				
				var qParent = document.getElementById(this._id);
				var vizParent = document.createElement("div");
				var vizId = "vizParent"+this._id;
				vizParent.setAttribute("id", vizId);
				vizParent.setAttribute("style","position:absolute;overflow:hidden;display:table-cell;width:100%;vertical-align:middle;height:100%;z-order:auto;");
				qParent.appendChild(vizParent);
				
				var vizStyle = "#infovis div {position:absolute;overflow:hidden;font-size:11px;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;}#infovis .content {background-color:#333;border:0px solid #111;} #infovis .head {color:white;background-color:#444;} #infovis .head.in-path {background-color:#655;} #infovis .body {background-color:black;} #infovis .leaf {color:white;background-color:#111;display:table-cell;vertical-align:middle;border:1px solid #000;} #infovis .over-leaf {border:1px solid #9FD4FF;} #infovis .over-content {background-color: #9FD4FF;} #infovis .over-head {background-color:#A4D9FF;color:black;} .tip {color: #fff;width: 139px;background-color: black;opacity:0.9;filter:alpha(opacity=90);font-size:10px;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;padding:7px;} .album {width:100px;margin:3px;} input {font-size:10px;font-family:Verdana, Geneva, Arial, Helvetica, sans-serif;}";
				vizStyle = vizStyle.replace(/infovis/gi,vizId);
				qx.html.StyleSheet.createElement(vizStyle);
//				
//				if (qx.core.Client.isMshtml()) {
//					vizParent = G_vmlCanvasManager.initElement(vizParent);
//				}
				
				var config = {
				    titleHeight: 13,
			        //The id of the treemap container
			        rootId: vizId,
			        //Set the max. depth to be shown for a subtree
			        levelsToShow: 1,
			        offset:1,

			        //Add click handlers for
			        //zooming the Treemap in and out
			        addLeftClickHandler: true,
			        addRightClickHandler: true,
			        
			        //When hovering a node highlight the nodes
			        //between the root node and the hovered node. This
			        //is done by adding the 'in-path' CSS class to each node.
			        selectPathOnHover: true,
			        Color: {  
			            //Allow coloring  
			            allow: true,  
			            //Set min value and max value constraints  
			            //for the *$color* property value.  
			            //Default's to -100 and 100.  
			            minValue: 1,  
			            maxValue: 50,  
			            //Set color range. Default's to reddish and greenish.  
			            //It takes an array of three  
			            //integers as R, G and B values.  
			            minColorValue: [0, 255, 50],  
			            maxColorValue: [255, 0, 50]  
			        },
			        
			        //Allow tips
			        Tips: {
			          allow: true,
			          //add positioning offsets
			          offsetX: 20,
			          offsetY: 20,
			          //implement the onShow method to
			          //add content to the tooltip when a node
			          //is hovered
			          onShow: function(tip, node, isLeaf, domElement) {
			              tip.innerHTML = "<div class=\"tip-title\">" + node.name + "</div>" + 
			                "<div class=\"tip-text\">" + this.makeHTMLFromData(node.data) + "</div>"; 
			          },  

			          //Aux method: Build the tooltip inner html by using the data property
			          makeHTMLFromData: function(data){
			              var html = '';
			              html += "playcount" + ': ' + data.$area + '<br />';
			              if ("$color" in data) 
			                  html += "rank" + ': ' + data.$color + '<br />';
			              if ("image" in data) 
			                  html += "<img class=\"album\" src=\"" + data.image + "\" />";
			              return html;
			          }
			        },

			        //Implement this method for retrieving a requested
			        //subtree that has as root a node with id = nodeId,
			        //and level as depth. This method could also make a server-side
			        //call for the requested subtree. When completed, the onComplete 
			        //callback method should be called.
//			        request: function(nodeId, level, onComplete){
//			        	this.info("Refreshing treemap child.");
//			        	var data = this.getWidgetData();
////				    	  this.info(data);
////			        	if (data != undefined) {
////					    	var root = data.substring(0,data.length); 
////				            var subtree = TreeUtil.getSubtree(root, nodeId);
////				            TreeUtil.prune(subtree, 1);
////				            onComplete.onComplete(nodeId, subtree);
////			        	}
//			        },
			        //Remove all events for the element before destroying it.
			        onDestroyElement: function(content, tree, isLeaf, leaf){
			            if(leaf.clearAttributes) leaf.clearAttributes();
			        }
			    };
				var tm = null;
				if (this._type == 2) {
					tm = new TM.Strip(config);
				}
				else if (this._type == 1) {
					tm = new TM.Squarified(config);
				}
				else {
					tm = new TM.SliceAndDice(config);
				}
				
//				tm.onLeftClick = function(elem) {
//			        this.enter(elem);
//			        var node = elem;
//		        	qParent.selection = node;
//		        	//fire selection event
//		        	var req = org.eclipse.swt.Request.getInstance();
//		        	req.addParameter(widgetId + ".selectedNode", node.id);
//		        	req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
//		        	req.send();
//			    };
//			    
//			    tm.onRightClick = function() {
//			        this.out();
//		        	qParent.selection = node;
//		        	//fire selection event
//		        	var req = org.eclipse.swt.Request.getInstance();
//		        	req.addParameter(widgetId + ".selectedNode", this.shownTree.id);
//		        	req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
//		        	req.send();
//			        
//			    };
				
			    this.addEventListener("changeWidth", function(e) {
					vizParent.width = this.getWidth();
					parent.refreshData();
				});
				this.addEventListener("changeHeight", function(e) {
					vizParent.height = this.getHeight();
					parent.refreshData();
				});
			    
				this._viz = tm;
				this._vizParent = vizParent;
				//This is a hack to ensure that a refresh is called after the style tag above is 
				//initialized
				qx.client.Timer.once(function() {
					this.refreshData();
				},this,100);
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
				var tm = this._viz;
				if (tm != null) {
					var data = this.getWidgetData();
					if (data != null) {
						this.info("Loading treemap data.");
						tm.loadJSON(data);
						this.info("Refreshing treemap.");
					}
				}
			}
			catch (e) {
				this.info(e);
			}
		},
		
		selectNode : function (id) {
			try {
				var tm = this._viz;
				if (tm != null) {
					tm.onLeftClick(id);
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