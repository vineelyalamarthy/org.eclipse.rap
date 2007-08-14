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
 * <p>The rendering schedule is used to decide and determine whether a 
 * particular component has to be rendered.</p>
 * <p>This interface is not inteded to be implemented by clients.</p>
 * @see org.eclipse.rwt.Renderer#scheduleRendering(org.eclipse.rwt.WebComponent)
 */
public interface IRenderingSchedule {

  /**
   * <p>Returns whether the given <code>component</code> is scheduled for 
   * rendering.</p> 
   * @param component the component to be queried, must not be <code>null</code> 
   */
  boolean isScheduled( Object component );
  
  /**
   * <p>Adds the given <code>component</code> to the list of components that 
   * will be scheduled for rendering.</p>
   * @param component the component to be scheduled.
   */
  void schedule( Object component );
}
