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

function treeItemClicked( itemId ) {
  eventHandler.webActionPerformed( itemId );
}

function treeItemDblClicked( itemId ) {
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