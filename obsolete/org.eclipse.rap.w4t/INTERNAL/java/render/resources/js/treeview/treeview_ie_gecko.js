/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
 
  function tv_TreeViewHandler() {
    this.VIS_HIDE = "vis_hide";
    this.VIS_SHOW = "vis_show";
    this.VIS_INHERIT = "vis_inherit";
    this.PREFIX_STATE_INFO = "treeNodeToggleStateInfo";
    /* the suffix that is append to the passed name of the image
     *  for an expanded node of the treeview */
    this.OPEN_IMG_SUFFIX = "Exp.gif";
    /* the suffix that is append to the passed name of the image
     *  for a collapsed node of the treeview */
    this.CLOSED_IMG_SUFFIX = "Col.gif";

    
    /* ******************************************************* *
     * declare member functions of the TreeViewHandler object. *
     * ******************************************************* */

    /* changes the state of a node from expanded to collapsed,
     * or vice versa. */
    this.toggle = tv_toggle;
  }

  /*********************************************************** */ 
  /* the section with the standard treeview handler functions  */
  /*********************************************************** */ 


  /* compID   : the name of the hidden input field which
   *            registers the state of the tree nodes (for
   *            preserving the tree structure on reload)
   * hasEvent : whether this function must trigger a
   *            treeNodeExpandedEvent or treeNodeCollapsedEvent
   *            after reconfiguring the tree (may be 1 for
   *            collapsing event, 2 for expanding event, 0 for
   *            none or 3 for both). */
  function tv_toggle( branchLayerID, hasEvent, imageSet, isLastChild ) {
    var imageName = imageSet;
    var node = document.getElementById( 'div' + branchLayerID );
    var toggleImg = document.getElementById( 'tog' + branchLayerID );
    var iconImg = document.getElementById( 'ico' + branchLayerID );
    // expand the branch if it is not visible
    if( node.style.display == 'none' ) {
      // Change the image (if there is one)
      imageName += "_Minus";
      if( isLastChild ) {
        imageName += "Last.gif";
      } else {
        imageName += "Inner.gif";
      }
      toggleImg.src = imageName;
      // cannot be an expanded node without children here, for the script
      // could not be executed (no toogle image if no children)
      iconImg.src = imageSet + "_IconExpWithChildren.gif";
      // toggle the subtree
      node.style.display = 'block';
  
      // Every treenode has a hidden input field that registers,
      // whether the node is opened (preserve the tree state
      // on reload).
      var stateInfoId = this.PREFIX_STATE_INFO + branchLayerID;
      document.getElementsByName( stateInfoId )[ 0 ].value = this.VIS_SHOW;

      // after reconfiguring the tree, we trigger an expanded-
      // event for the node, if there is one implemented
      if( hasEvent > 1 ) {
        eventHandler.webTreeNodeExpanded( branchLayerID );
      }
    }

    // collapse the branch if it is visible
    else {
      // Change the image (if there is an image)
      imageName += "_Plus";
      if( isLastChild ) {
        imageName += "Last.gif";
      } else {
        imageName += "Inner.gif";
      }
      toggleImg.src = imageName;
      iconImg.src = imageSet + "_IconCol.gif";
            
      // toggle the subtree
      node.style.display = 'none';
      // Every treenode has a hidden input field that registers,
      // whether the node is opened (preserve the tree state
      // on reload).
      var stateInfoId = this.PREFIX_STATE_INFO + branchLayerID;
      document.getElementsByName( stateInfoId )[ 0 ].value = this.VIS_HIDE;
      // after reconfiguring the tree, we trigger an collapsed-
      // event for the node, if there is one implemented
      if( hasEvent % 2 == 1 ) {
        eventHandler.webTreeNodeCollapsed( branchLayerID );
      }
    }
  }   
    
  /****************************************************************** */ 
  /*               create the treeview handler instance               */
  /****************************************************************** */ 
  var treeViewHandler = new tv_TreeViewHandler();
  

  function dd_DragDropHandler() {
    /* ******************************************************* *
     * declare member functions of the DnD Handler object.     *
     * ******************************************************* */
    this.mouseDown = dd_mouseDown;
    this.mouseUp = dd_mouseUp;
    this.clearDragDrop = dd_clearDragDrop;
  }

  /*********************************************************** */ 
  /* the section with the standard DnD handler functions       */
  /*********************************************************** */ 

  function dd_mouseDown( id ) {
    var form = getForm();
    form.dragSource.value = id;
    // Opera-specific (version 8 up) hack to prevent selection while dragging
    if( window.opera ) {
  	  var field = document.getElementById( "w4t_hidden_focus" );
  	  if( field == null ) {
        field = document.createElement( "input" );
        field.setAttribute( "type", "hidden" );
        field.setAttribute( "id", "w4t_hidden_focus" );
        field.setAttribute( "name", "w4t_hidden_focus" );
        field.setAttribute( "value", " " );
        form.appendChild( field );
  	  }
      field.focus();
    }
  }

  /* triggers an actionPerformed or dragDropReceived, depending
   * on whether the last mousedown was on a possible drag source. */
  function dd_mouseUp( id, evtType ) {
    if(    getForm().dragSource.value != "" 
        && getForm().dragSource.value != id && evtType > 1 ) 
    {
      getForm().dragDestination.value = id;
      eventHandler.submitDocument();
    }
    getForm().dragSource.value = "";
    getForm().dragDestination.value = "";
  }
  
  /** clears all form elements that are involed in DnD handling. */
  function dd_clearDragDrop() {
    // only when Internet Explorer - do nothing on Mozilla/Firefox/Navigator
    if( document.all ) {
      if(    getForm().dragSource.value == ""
          || getForm().dragDestination.value == "" )
      {
        getForm().dragSource.value = "";
        getForm().dragDestination.value = "";
      }
    }
  }
  
  function getForm() {
    return document.getElementsByTagName( "form" )[ 0 ];
  }
    
    
  /****************************************************************** */ 
  /*               create the DnD handler instance                    */
  /****************************************************************** */ 
  var dragDropHandler = new dd_DragDropHandler();
  