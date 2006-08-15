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
package com.w4t.webcomponentkit;

import com.w4t.Renderer;


/** <p>a renderer that is set to components for which no renderer (not 
  * even a default or superclass renderer) could be found.</p>
  *
  * <p>This is only a dummy that prevents NullPointerExceptions in 
  * components.</p>
  */
public abstract class WebComponentRenderer extends Renderer {
}
