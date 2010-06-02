/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.rwt.internal.protocol;

import org.eclipse.swt.widgets.Widget;

public interface IChunkAdapter {

  public abstract void readData( final Widget widget, final Chunk chunk );
  
  public void processEvent( final Widget widget, final String eventName );
  
}