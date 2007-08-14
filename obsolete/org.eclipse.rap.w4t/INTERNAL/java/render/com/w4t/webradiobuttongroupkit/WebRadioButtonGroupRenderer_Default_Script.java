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
package com.w4t.webradiobuttongroupkit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.webcontainerkit.WebContainerRenderer_Default_Script;

public class WebRadioButtonGroupRenderer_Default_Script
  extends WebContainerRenderer_Default_Script
{

  public void readData( final WebComponent component ) {
    ReadDataUtil.applyValue( component );
  }

  public void processAction( final WebComponent component ) {
    ProcessActionUtil.processItemStateChangedScript( component );
  }

  public void render( final WebComponent component ) throws IOException {
    // TODO [rh] check state of group and its radioButtons
    super.render( component );
  }
}
