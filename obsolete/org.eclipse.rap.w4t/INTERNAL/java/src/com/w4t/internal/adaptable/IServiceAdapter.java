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
package com.w4t.internal.adaptable;

import javax.servlet.ServletException;
import com.w4t.engine.requests.RequestCancelledException;

// TODO [rh] JavaDoc necessary?
public interface IServiceAdapter {

  void execute() throws RequestCancelledException, ServletException;
  
}
