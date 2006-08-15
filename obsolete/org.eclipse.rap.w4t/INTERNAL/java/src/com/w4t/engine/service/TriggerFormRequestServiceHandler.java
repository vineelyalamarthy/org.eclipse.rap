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
package com.w4t.engine.service;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;


class TriggerFormRequestServiceHandler extends FormRequestServiceHandler {

  private final static int[] PLACE_HOLDER = new int[] {
    71,73,70,56,57,97,1,0,1,0,128,0,0,
    0,0,0,255,255,255,33,249,4,9,0,0,1,0,
    44,0,0,0,0,1,0,1,0,0,2,2,76,1,0,59
  };

  public void service() throws ServletException, IOException {
    super.service();
    ContextProvider.getResponse().setContentType( CONTENT_TYPE_IMAGE_GIF );
    OutputStream out = ContextProvider.getResponse().getOutputStream();
    try {
      for( int i = 0; i < PLACE_HOLDER.length; i++ ) {
        out.write( PLACE_HOLDER[ i ] );
      }
      out.flush();
    } finally {
      out.close();
    }
  }
}