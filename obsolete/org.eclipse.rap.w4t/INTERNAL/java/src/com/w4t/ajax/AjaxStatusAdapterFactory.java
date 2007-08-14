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
package com.w4t.ajax;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.rwt.AdapterFactory;

import com.w4t.WebComponent;

/**
 * <p>Retrieves an AjaxStatus for any given <code>WebComponent</code>.</p>
 */
public final class AjaxStatusAdapterFactory implements AdapterFactory {

  private static final Class[] ADAPTER_LIST = new Class[] { AjaxStatus.class };
  
  /** <p>Maps WebComponents to their AjaxStatus-adapters.</p>
   * <p>Key: WebComponent, Value: AjaxStatus</p> */
  private final Map map = new WeakHashMap();
  
  public Object getAdapter( final Object object, final Class clazz ) {
    Object result = null;
    if ( object instanceof WebComponent && clazz == AjaxStatus.class ) {
      result = map.get( object );
      if ( result == null ) {
        result = new AjaxStatusImpl( ( WebComponent )object );
        map.put( object, result );
      }
    }
    return result;
  }

  public Class[] getAdapterList() {
    return ADAPTER_LIST;
  }
}
