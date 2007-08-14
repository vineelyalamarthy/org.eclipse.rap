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

import org.eclipse.rwt.AdapterFactory;

import com.w4t.WebForm;


/**
 * <p>This AdapterFactory provides <code>LifeCycleAdapter</code>s for 
 * <code>WebForm</code>s.</p>
 */
public class LifeCycleAdapterFactory implements AdapterFactory {

  private static final Class[] ADAPTER_LIST 
    = new Class[] { ILifeCycleAdapter.class };

  public Object getAdapter( final Object adaptable, 
                            final Class adapter )
  {
    // TODO [rh] shouldn't we check for instanceof WebForm here?
    return new LifeCycleAdapter( ( WebForm )adaptable );
  }

  public Class[] getAdapterList() {
    return ADAPTER_LIST;
  }
}
