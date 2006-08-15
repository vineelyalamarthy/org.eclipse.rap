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
package com.w4t.engine.lifecycle;

import javax.servlet.ServletException;
import com.w4t.engine.requests.RequestCancelledException;


/** <p>The common interface of all phases for a lifecycle.</p>
  */
public interface LifeCyclePhase {
  
  /** <p>Executes this LifeCyclePhase.</p>
    * @return the phase id of the phase which is to be executed next. 
    */
  PhaseId execute() throws RequestCancelledException, ServletException;

}
