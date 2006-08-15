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
package com.w4t.engine.lifecycle.standard;



/** 
 * <p>The superclass of specific EventQueueFilter classes for browser-dependent
 * algorithms of the filtering of the data event queue (which is a part of
 * the ReadData phase).</p>
 */
public abstract class EventQueueFilter {
    
  public static final String ATTRIBUTE_KEY = "w4t_event_queue_filter";


  /** 
   * <p>performs the filtering implemented in the subclass for the event 
   * queue in the specified request.</p>
   */
  public abstract void filter();

}
