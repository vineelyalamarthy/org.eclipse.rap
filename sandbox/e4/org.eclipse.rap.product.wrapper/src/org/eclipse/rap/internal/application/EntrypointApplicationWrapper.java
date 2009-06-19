/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.internal.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.rwt.lifecycle.IEntryPoint;

public class EntrypointApplicationWrapper implements IEntryPoint {

  public int createUI() {
    Object exitCode = null;
    int result = 0;
    try {
      final IApplication application = ApplicationRegistry.getApplication();
      final IApplicationContext context = new RAPApplicationContext( application );
      exitCode = application.start( context );
      if( exitCode instanceof Integer ) {
        result = ( ( Integer )exitCode ).intValue();
      }
    } catch( final Exception e ) {
      e.printStackTrace();
    }
    return result;
  }
}
