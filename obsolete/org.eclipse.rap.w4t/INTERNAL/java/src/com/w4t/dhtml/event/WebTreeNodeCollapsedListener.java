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
  * <p>The listener interface for receiving WebTreeNodeCollapsedEvents.</p>
  */
public interface WebTreeNodeCollapsedListener extends EventListener {

  /** invoked when a tree node expanding occurs. */
  public void webTreeNodeCollapsed( WebTreeNodeCollapsedEvent evt );
}

