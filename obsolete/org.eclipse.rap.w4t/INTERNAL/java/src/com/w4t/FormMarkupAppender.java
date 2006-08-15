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
package com.w4t;

import java.io.IOException;
import com.w4t.event.WebRenderAdapter;
import com.w4t.event.WebRenderEvent;

// TODO [rh] JavaDoc?
public abstract class FormMarkupAppender extends WebRenderAdapter {

  protected final WebComponent component;

  public FormMarkupAppender( final WebComponent component ) {
    this.component = component;
  }

  public final void afterRender( final WebRenderEvent evt ) {
    WebForm webForm = component.getWebForm();
    if( webForm != null ) {
      webForm.removeWebRenderListener( this );
      try {
        doAfterRender( evt );
      } catch( IOException e ) {
        // TODO [rh] Exception handling
        throw new RuntimeException( e );
      }
    }
  }

  protected abstract void doAfterRender( final WebRenderEvent evt ) 
    throws IOException;
}