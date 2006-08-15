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

    /* ******************************************************* *
     * declare member variables of the TreeViewHandler object. *
     * ******************************************************* */
    this.VIS_HIDE = "vis_hide";
    this.VIS_SHOW = "vis_show";
    this.VIS_INHERIT = "vis_inherit";

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
    var node = document.getElementsByName( "treeNodeToggleStateInfo" + branchLayerID )[ 0 ];
    if( node.value == this.VIS_HIDE ) {
      node.value = this.VIS_SHOW;
      if( hasEvent > 1 ) {
        eventHandler.webTreeNodeExpanded( branchLayerID );
      } else {
        document.W4TForm.submit();
      }
    } else {
      node.value = this.VIS_HIDE;
      if( hasEvent % 2 == 1 ) {
        eventHandler.webTreeNodeCollapsed( branchLayerID );
      } else {
        document.W4TForm.submit();
      }
    }
  }   
    
  /****************************************************************** */ 
  /*               create the treeview handler instance               */
  /****************************************************************** */ 
  var treeViewHandler = new tv_TreeViewHandler();
  

  function dd_DragDropHandler() {
    this.dragSourceID = '';
    this.mouseDown = dd_mouseDown;
    this.mouseUp = dd_mouseUp;
    this.doDragDrop = dd_doDragDrop;
    this.clearDragDrop = dd_clearDragDrop;
  }

  /*********************************************************** */ 
  /* the section with the standard DnD handler functions       */
  /*********************************************************** */ 

  function dd_mouseDown( id ) {
    getForm().dragSource.value = id;
  }

  /* triggers an actionPerformed or dragDropReceived, depending
   * on whether the last mousedown was on a possible drag source. */
  function dd_mouseUp( id, evtType ) {
    if( getForm().dragSource.value != "" ) {
      if( getForm().dragSource.value == id && evtType % 2 == 1 )  {
        getForm().dragSource.value = "";          
        eventHandler.webActionPerformed( id );
      } else if ( getForm().dragSource.value != id && evtType > 1 ) {
        getForm().dragDestination.value = id;
        eventHandler.submitDocument();
      }
    }
    getForm().dragSource.value = "";
    getForm().dragDestination.value = "";
  }
  
  /** clears all form elements that are involed in DnD handling. */
  function dd_clearDragDrop() {
    if(    getForm().dragSource.value == ""
        || getForm().dragDestination.value == "" ) 
    {
      getForm().dragSource.value = "";
      getForm().dragDestination.value = "";
    }
  }
  
  function dd_doDragDrop( ID ) {
    if( this.dragSourceID == '' ) {
      this.dragSourceID = ID;	
    } else {
      if( this.dragSourceID != ID ) {
      	document.W4TForm.dragSource.value = this.dragSourceID;
      	document.W4TForm.dragDestination.value = ID;
      }
      eventHandler.submitDocument();
      this.dragSourceID = '';
    }  	
  }
  
  function getForm() {
    return document.getElementsByTagName( "form" )[ 0 ];
  }
    
  var dragDropHandler = new dd_DragDropHandler();
  