/******************************************************************************* 
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.rms.ui.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rwt.RWT;
import org.eclipse.ui.AbstractSourceProvider;

/**
 * Provides value for the variable VARIABLE_NAME
 */
public class UrlParamsSourceProvider extends AbstractSourceProvider {

  public final static String VARIABLE_NAME = "org.eclipse.rap.demo.activities.entryPoint";
  public final static String URL_PARAM_NAME = "startup";
  private final static String[] PROVIDED_SOURCE_NAMES = new String[]{
    VARIABLE_NAME
  };

  public Map getCurrentState() {
    Map result = new HashMap();
    String urlParam = RWT.getRequest().getParameter( URL_PARAM_NAME );
    List foo = new ArrayList();
    foo.add( urlParam );
    result.put( VARIABLE_NAME, foo );
    return result;
  }

  public String[] getProvidedSourceNames() {
    return PROVIDED_SOURCE_NAMES;
  }
  
  public void dispose() {
	  // 
  }
}
