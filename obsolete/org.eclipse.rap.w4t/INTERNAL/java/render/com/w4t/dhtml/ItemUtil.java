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
package com.w4t.dhtml;

import java.io.IOException;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.internal.adaptable.IRenderInfoAdapter;


public class ItemUtil {
  
  private static final String MARKED = "marked";
  public static final String IMG_TRANSPARENT
    = "resources/images/transparent.gif";
  public static final String STYLE_CONTENT = "font-size:0px;white-space:nowrap";
  
  private ItemUtil() {
  }

  // TODO: [fappel] marked state is not handled by hashcode algorithm, therefore 
  //                also the envelope mechanism fails...
  public static void checkMarkState( final Item item ) throws IOException {
    if( markedStateChanged( item ) ) {
      AjaxStatus ajaxStatus = ( AjaxStatus )item.getAdapter( AjaxStatus.class );
      boolean mustRenderBuffer = ajaxStatus.mustRender();
      ajaxStatus.updateStatus( true );
      if( !mustRenderBuffer ) {
        AjaxStatusUtil.startEnvelope( item );
      }
    }
  }
    
  public static void bufferMarkedState( final Item item ) {
    Boolean marked = Boolean.valueOf( item.isMarked() );
    getRenderInfoAdapter( item ).addRenderState( MARKED, marked );
  }
  
  
  //////////////////
  // helping methods
  
  private static boolean markedStateChanged( final Item item ) {
    IRenderInfoAdapter renderInfoAdapter = getRenderInfoAdapter( item );
    Boolean marked = ( Boolean )renderInfoAdapter.getRenderState( MARKED );
    return marked != null && marked.booleanValue() != item.isMarked();
  }
  
  private static IRenderInfoAdapter getRenderInfoAdapter( final Item item ) {
    return ( IRenderInfoAdapter )item.getAdapter( IRenderInfoAdapter.class );
  }
}