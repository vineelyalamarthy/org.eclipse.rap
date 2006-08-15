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

import java.io.IOException;


/**
 * <p>An <code>ILifeCycleAdapter</code> implementation is used to connect
 * the component tree to the life cycle's phases control.</p>
 */
public interface ILifeCycleAdapter {
  
  /**
   * Relays the responsibility of the lifecycle's <code>ReadData</code> phase
   * to the component tree, and does whatever is necessary so that at the
   * end the values of html form elements are read out of the request and 
   * applied to the corresponding components.
   */
  void readData();
  
  /**
   * Relays the responsibility of the lifecycle's <code>ProcessAction</code>
   * phase to the component tree and invokes the action handler, which is
   * associated with the component that caused the reqeust.
   */
  void processAction();
  
  /**
   * Relays the responsibility of the lifecycle's <code>Render</code> phase
   * to the component tree, and does whatever is nessecary to create the
   * markup with which the client's UI-representation can be synchronized
   * with the serverside state. 
   */
  void render() throws IOException;
  
}
