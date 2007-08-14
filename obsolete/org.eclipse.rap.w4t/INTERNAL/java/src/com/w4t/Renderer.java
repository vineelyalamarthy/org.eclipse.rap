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

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTMLUtil;

import com.w4t.engine.lifecycle.standard.IRenderingSchedule;



/** <p>The superclass for all renderers that render subclasses of 
  * {@link org.eclipse.rwt.WebComponent WebComponent}.</p>
  */
public abstract class Renderer {
  
  /**
   * <p>This implementation does nothing. Clients should override to apply
   * client-side changes to the given <code>component</code>.</p>
   * @param component the component to be rendered. Will never be
   * <code>null</code>.
   */
  public void readData( final WebComponent component ) {
  }

  /**
   * <p>This implementation does nothing. Clients should override to propagate
   * events that occured on the given <code>component</code>.</p>
   * @param component the component to be rendered. Will never be
   * <code>null</code>.
   */
  public void processAction( final WebComponent component ) {
  }
  
  /**
   * <p>This implementation does nothing. Clients should override to
   * output markup for the given <code>component</code>.</p>
   * <p>Use {@link #getResponseWriter() getResponseWriter} to send the
   * markup to the response stream.</p>
   * @param component the component to be rendered. Will never be
   * <code>null</code>.
   * @throws IOException if an I/O error occurs
   */
  public void render( final WebComponent component ) throws IOException {
  }
  
  /**
   * <p>This implementation does nothing; clients may override. It is called 
   * before the actual rendering phase starts and unsed to specify whether 
   * components are to be rendered or not. Only for scheduled components the 
   * before- and afterRender-events are fired.</p>
   * <p>A common usage is for containers that decide whether their children 
   * will be rendered. For example the WebCardLayoutRenderer uses this
   * to exclude all invisible cards.</p>
   * <p>The rendering schedule can be obtained with 
   * {@link #getRenderingSchedule() getRenderingSchedule()}.</p>
   * @param component the component to be rendered. Will never be
   * <code>null</code>.
   */
  public void scheduleRendering( final WebComponent component ) {
  }
  
  /**
   * <p>Returns the response writer for the current reponse.</p>
   * @see HtmlResponseWriter
   */
  protected HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }

  /**
   * @deprecated this is only available for compatibility reasons 
   *             and will be removed.
   */
  public void renderStateInfoMarkup( final WebComponent component ) {
    Object adapter = component.getAdapter( IReadDataAdapter.class );
    if( adapter instanceof IReadDataAdapter ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out;
      out = stateInfo.getResponseWriter();
      String name = RenderUtil.PREFIX_STATE_INFO + component.getUniqueID();
      try {
        HTMLUtil.hiddenInput( out, name, component.getUniqueID() );
      } catch( IOException e ) {
        // TODO [rh] exception handling
        throw new RuntimeException( e );
      }
    }
  }

  /**
   * @deprecated this is only available for compatibility reasons and
   *             will be removed.
   */
  public void applyStateInfo( final WebComponent component ) throws Exception {
    String prefix = RenderUtil.PREFIX_STATE_INFO;
    String name = prefix + component.getUniqueID();
    String value = ContextProvider.getRequest().getParameter( name );
    if( value != null ) {
      value = value.trim();
      Object adapter = component.getAdapter( IReadDataAdapter.class );
      if( adapter instanceof IReadDataAdapter ) {
        ( ( IReadDataAdapter )adapter ).readData( component );
      }
    }
  }
  
  /**
   * <p>Returns the <code>IRenderingSchedule</code> for the current 
   * request/response.</p>
   */
  public static IRenderingSchedule getRenderingSchedule() {
    return LifeCycleHelper.getSchedule();
  }

}