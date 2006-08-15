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
import java.text.Format;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.Item;
import com.w4t.engine.lifecycle.standard.EventQueue;
import com.w4t.engine.lifecycle.standard.IRenderingSchedule;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.event.WebDataEvent;
import com.w4t.internal.adaptable.IRenderInfoAdapter;
import com.w4t.util.RendererCache;

/**
 * <p>This class contains utility methods used by the lifecycle 
 * implementation.</p> 
 * <p>This class is not inteded to be used by clients.</p>
 */
// TODO [rh] JavaDoc necessary?
public class LifeCycleHelper {
  
  private LifeCycleHelper() {
  }
  
  public static void readData( final WebComponent component ) {
    Browser browser = W4TContext.getBrowser();
    retrieveRenderer( component, browser ).readData( component );
  }
  
  public static void applyRequestValue( final IInputValueHolder valueHolder, 
                                        final String value )
  {
    String oldValue = valueHolder.getValue();
    String newValue = value == null ? "" : value;
    if( !oldValue.equals( newValue ) ) {
      Format formatter = valueHolder.getFormatter();
      if( formatter != null ) {
        valueHolder.setValue( ConverterUtil.parse( formatter, newValue ) );
      } else {          
        valueHolder.setValue( newValue );
      }
      if( WebDataEvent.hasListener( valueHolder ) ) {
        WebDataEvent evt = new WebDataEvent( valueHolder, 
                                             WebDataEvent.VALUE_CHANGED,
                                             oldValue,
                                             newValue );
        EventQueue.getEventQueue().addToQueue( evt );
      }
    }
  }
  
  public static void processAction( final WebComponent component ) {
    Browser browser = W4TContext.getBrowser();
    retrieveRenderer( component, browser ).processAction( component );
  }
  
  public final static void render( final WebComponent component )
    throws IOException
  {
    render( component, component );
  }
  
  public final static void render( final Adaptable questioner,
                                   final WebComponent component ) 
    throws IOException
  {
    Browser browser = W4TContext.getBrowser();
    Browser detectedBrowser = getStateInfo().getDetectedBrowser();
    WebComponent parentComponent = getParentComponent( component );
    if( component.isVisible() ) {
      createRenderInfo( questioner );
      AjaxStatusUtil.startEnvelope( questioner );
      Renderer renderer = retrieveRenderer( questioner, browser );
      renderer.renderStateInfoMarkup( component );
      renderer.render( component );
      AjaxStatusUtil.endEnvelope( questioner );
    } else if(    detectedBrowser.isAjaxEnabled()
               && getSchedule().isScheduled( parentComponent ) )
    {
      AjaxStatusUtil.startEnvelope( component );
      HtmlResponseWriter out = getStateInfo().getResponseWriter();
      RenderUtil.appendAjaxPlaceholder( out, component, false );
      AjaxStatusUtil.endEnvelope( component );
    }
  }

  private static WebComponent getParentComponent( final WebComponent cmp ) {
    WebComponent result = null;
    if( cmp instanceof Item ) {
      result = ( ( Item )cmp ).getParentNode();
    } else {
      result = cmp.getParent();
    }
    return result;
  }

  private static IRenderingSchedule getSchedule() {
    return getStateInfo().getRenderingSchedule();
  }
  
  private static IServiceStateInfo getStateInfo() {
    return ContextProvider.getStateInfo();
  }

  private static void createRenderInfo( final Adaptable questioner ) {
    Class clazz = IRenderInfoAdapter.class;
    Object adapter = questioner.getAdapter( clazz );
    IRenderInfoAdapter infoAdapter = ( IRenderInfoAdapter )adapter;
    if( infoAdapter != null ) {
      infoAdapter.createInfo();
    }
  }

  private static Renderer retrieveRenderer( final Object questioner, 
                                            final Browser browser )
  {
    RendererCache cache = RendererCache.getInstance();
    return cache.retrieveRenderer( questioner.getClass(), browser );
  }
}
