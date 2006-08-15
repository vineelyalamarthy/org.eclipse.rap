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
package com.w4t.dhtml.event;

import java.util.EventListener;

/**
  * <p>The listener interface for receiving DragDropEvents.</p>
  * <p>A DragDropEvent is fired when the user holds the mouse down on an
  * Item (e.g. a TreeNode), moves it over another Item (which must be a
  * Node, e.g. a TreeNode) and releases the mouse. The first
  * Item is then considered to be "dragged" to the latter Item, which is
  * notified by a DragDropEvent.</p>
  */
public interface DragDropListener extends EventListener {

  /** <p>invoked when a Node gets an Item "dragged" to it.</p> */
  public void receivedDragDrop( DragDropEvent evt );
}

 