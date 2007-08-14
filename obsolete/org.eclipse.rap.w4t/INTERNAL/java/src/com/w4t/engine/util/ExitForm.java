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
package com.w4t.engine.util;

import com.w4t.WebForm;

/**
 * <p>This form is used to render the (last) reponse for an invalidated 
 * session.</p> 
 * <p>This class is not intended to be used by clients.</p>
 * @see org.eclipse.rwt.W4TContext#invalidate()
 */
public final class ExitForm extends WebForm {

  protected void setWebComponents() throws Exception {
    // do nothing
  }
}
