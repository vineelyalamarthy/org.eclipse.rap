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
package com.w4t.webcardlayoutkit;

import java.io.IOException;

import com.w4t.*;
import com.w4t.ajax.AjaxStatus;
import com.w4t.ajax.AjaxStatusUtil;


/** <p>The default renderer for {@link org.eclipse.rwt.WebCardLayout
 * <code>WebCardLayout</code>} on AJaX-enabled browsers.</p>
 */
public class WebCardLayoutRenderer_Ie5up_Ajax 
  extends WebCardLayoutRenderer_Ie5up_Script
{
  
  public void render( final WebComponent component ) throws IOException {
    WebContainer parent = ( WebContainer )component;
    // needs to be rendered?
    if(    AjaxStatusUtil.mustRender( parent )
        || childChangedVisibleOrEnableState( parent ) )
    {
      super.render( parent );
    } else {
      WebCardLayout layout = ( WebCardLayout )parent.getWebLayout();
      // give the active child component a chance to render itself
      WebComponent child;
      for( int i = 0; i < parent.getWebComponentCount(); i++ ) {
        child = parent.get( i );
        if( parent.getConstraint( child ).equals( layout.getDisplayCard() ) ) {
          LifeCycleHelper.render( child, child );
        }
      }
    }
  }
  
  private boolean childChangedVisibleOrEnableState( final WebContainer parent )
    throws IOException
  {
    int count = parent.getWebComponentCount();
    boolean result = false;
    for( int i = 0; !result && i < count; i++ ) {
      WebComponent cmp = parent.get( i );
      result =    getAjaxStatus( cmp ).isWasVisible() != cmp.isVisible() 
               || getAjaxStatus( cmp ).isWasEnabled() != cmp.isEnabled();
    }
    // TODO: [fappel] visibility state of cards are not handled by 
    //                hashcode algorithm, therefore also the envelope mechanism 
    //                fails...
    if( result ) {
      if( !getAjaxStatus( parent ).mustRender() ) {
        getAjaxStatus( parent ).updateStatus( true );
        AjaxStatusUtil.startEnvelope( parent );
      }
    }
    return result;
  }

  private AjaxStatus getAjaxStatus( final WebComponent component ) {
    return ( AjaxStatus )component.getAdapter( AjaxStatus.class );
  }

  WebButton createCardButton( final String label, final WebContainer parent ) {
    // get the button as defined by super
    WebButton result = super.createCardButton( label, parent );
    // set its AjaxStatus (which does not exist yet) so that it will be rendered
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )result.getAdapter( AjaxStatus.class );
    ajaxStatus.updateStatus( true );
    return result;
  }
  

}
