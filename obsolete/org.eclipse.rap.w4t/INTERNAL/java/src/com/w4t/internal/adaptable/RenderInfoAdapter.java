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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rwt.internal.util.ParamCheck;


//TODO [rh] JavaDoc necessary?
public abstract class RenderInfoAdapter implements IRenderInfoAdapter {

  private Map renderState;
  
  public void addRenderState( final Object key, final Object value ) {
    ParamCheck.notNull( key, "key" );
    initRenderStateBuffer();
    renderState.put( key, value );
  }

  public Object getRenderState( final Object key ) {
    ParamCheck.notNull( key, "key" );
    initRenderStateBuffer();
    return renderState.get( key );
  }
  
  public void clearRenderState() {
    if( renderState != null ) {
      renderState.clear();
    }
  }
  
  private void initRenderStateBuffer() {
    if( renderState == null ) {
      renderState = new HashMap( 4, 1f );
    }
  }
}
