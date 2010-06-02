/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.rwt.protocol.impl;

import java.io.IOException;

import org.eclipse.rwt.internal.protocol.Chunk;
import org.eclipse.rwt.internal.protocol.IChunkAdapter;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.swt.widgets.Widget;


public class FakeLCA extends AbstractWidgetLCA implements IChunkAdapter {
  
  private boolean readDataWasCalled = false;
  private boolean processEventWasCalled;

  public void readData( final Widget widget ) {
  }

  public void preserveValues( final Widget widget ) {
  }

  public void renderInitialization( final Widget widget ) throws IOException {
  }

  public void renderChanges( final Widget widget ) throws IOException {
  }

  public void renderDispose( final Widget widget ) throws IOException {
  }
  
  public void readData( final Widget widget, final Chunk chunk ) {
    readDataWasCalled = true;
  }
  
  public void processEvent( final Widget widget, final String eventName ) {
    processEventWasCalled = true;
  }
  
  public boolean wasReadDataCalled() {
    return readDataWasCalled;
  }

  public boolean wasProcessEventcalled() {
    return processEventWasCalled;
  }
  
  
  
}
