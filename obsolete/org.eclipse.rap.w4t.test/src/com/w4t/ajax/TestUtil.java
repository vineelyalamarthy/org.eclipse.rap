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

import com.w4t.W4TContext;
import com.w4t.WebComponent;


public final class TestUtil {
  
  private static AjaxStatusAdapterFactory ajaxStatusAdapterFactory 
      = new AjaxStatusAdapterFactory();

  private TestUtil() {
    // prevent instantiation
  }
  
  public static void registerAdapter() {
    W4TContext.getAdapterManager().registerAdapters( ajaxStatusAdapterFactory, 
                                                     WebComponent.class );
  }
  
  public static void deregisterAdapter() {
    W4TContext.getAdapterManager().deregisterAdapters( ajaxStatusAdapterFactory, 
                                                       WebComponent.class );
  }
}
