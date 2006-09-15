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

var clickedTreeItemId = null;
var clickedTreeItemTime = null;

var lastTreeItemClickedId = null;

function treeItemClicked( itemId ) {
  if( itemId != lastTreeItemClickedId ) {
    eventHandler.minRequestDuration = 150;
    eventHandler.webActionPerformed( itemId );
    setTimeout( 'lastTreeItemClickedId = null', 500 );
  }
  lastTreeItemClickedId = itemId;
}

function treeItemDblClicked( itemId ) {
  lastId = null;
  doDblClickSubmit( itemId );
}

function doDblClickSubmit( itemId ) {
  if( eventHandler.isRequestRunning ) {
    setTimeout( 'doDblClickSubmit("' + itemId +'")', 10 );
  } else {
    document.getElementById( "w4tDoubleClickEvent" ).value = itemId;
    eventHandler.submitDocument();
  }
}