/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rwt.protocol;

import org.eclipse.rwt.internal.protocol.WidgetSynchronizer;
import org.eclipse.swt.widgets.Widget;


public class WidgetSynchronizerFactory {
  
  // prevent instantiation
  private WidgetSynchronizerFactory() {
    
  }

  public static IWidgetSynchronizer getSynchronizerForWidget( 
    final Widget widget ) 
  {
    return WidgetSynchronizer.getSynchronizerForWidget( widget );
  }
  
}
