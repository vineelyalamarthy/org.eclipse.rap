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

import org.eclipse.rwt.internal.lifecycle.LifeCyclePhase;
import org.eclipse.rwt.lifecycle.PhaseId;

import com.w4t.WebComponent;


/** <p>The abstract superclass for the phases in the 'Standard' lifecycle.</p>
  */
abstract class Phase implements LifeCyclePhase {
  abstract PhaseId getPhaseID();

  static ILifeCycleAdapter getLifeCycleAdapter( final WebComponent component ) {
    return ( ILifeCycleAdapter )component.getAdapter( ILifeCycleAdapter.class );
  }
}