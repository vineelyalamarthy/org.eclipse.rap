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

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.rwt.AdapterFactory;

//TODO [rh] JavaDoc necessary?
public class RenderInfoAdapterFactory implements AdapterFactory {

  private static final Class[] ADAPTER_LIST 
    = new Class[] { IRenderInfoAdapter.class };
  
  private Map buffer = new WeakHashMap();
  
  public Object getAdapter( final Object adaptable, final Class adapter ) {
    if( !buffer.containsKey( adaptable ) ) {
      buffer.put( adaptable, new RenderInfoAdapter() {
        public void createInfo() {
        }
        public Object getInfo() {
          return null;
        }
      } );
    } 
    return buffer.get( adaptable );
  }

  public Class[] getAdapterList() {
    return ADAPTER_LIST;
  }
}
