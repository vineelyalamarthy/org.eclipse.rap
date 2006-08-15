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

import java.util.HashSet;
import java.util.Set;


class RenderingSchedule implements IRenderingSchedule {

  private final Set schedule = new HashSet();
  
  public boolean isScheduled( final Object component ) {
    return schedule.contains( component );
  }

  public void schedule( final Object component ) {
    schedule.add( component );
  }
}
