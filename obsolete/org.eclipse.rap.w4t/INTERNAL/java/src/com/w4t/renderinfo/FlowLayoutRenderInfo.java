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
package com.w4t.renderinfo;

import com.w4t.WebContainer;

/** <p>The FlowLayoutRenderInfo contains information for layouting a 
  * {@link org.eclipse.rwt.WebContainer WebContainer} using the 
  * {@link org.eclipse.rwt.WebFlowLayout WebFlowLayout}.</p>
  */
public class FlowLayoutRenderInfo extends LayoutRenderInfo {

  
  /** Creates a new instance of FlowLayoutRenderInfo */
  public FlowLayoutRenderInfo( final WebContainer parent ) {
    super( parent );
  }
}
