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
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.browser.Browser;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.*;

import com.w4t.IWindowManager.IWindow;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.dhtml.Item;
import com.w4t.engine.lifecycle.standard.EventQueue;
import com.w4t.engine.lifecycle.standard.IRenderingSchedule;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
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
  
  private static final String ATTR_RENDERING_SCHEDULE 
    = IRenderingSchedule.class.getName();

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

  public static void setSchedule( final IRenderingSchedule schedule ) {
    getStateInfo().setAttribute( ATTR_RENDERING_SCHEDULE, schedule );
  }
  
  public static IRenderingSchedule getSchedule() {
    Object attribute = getStateInfo().getAttribute( ATTR_RENDERING_SCHEDULE );
    return ( IRenderingSchedule )attribute;
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

  public static String createUIRootId() {
    String windowId = WindowManager.getActive().getId();
    String formId = FormManager.getActive().getUniqueID();
    return createUIRootId( windowId, formId );
  }

  public static String getRequestWindowId() {
    HttpServletRequest request = ContextProvider.getRequest();
    String uiRoot = request.getParameter( RequestParams.UIROOT );
    String result = null;
    if( uiRoot != null ) {
      String[] parts = uiRoot.split( ";" );
      result = parts[ 0 ];
    }
    return result;
  }

  public static String getRequestFormId() {
    HttpServletRequest request = ContextProvider.getRequest();
    String uiRoot = request.getParameter( RequestParams.UIROOT );
    String result = null;
    if( uiRoot != null ) {
      String[] parts = uiRoot.split( ";" );
      result = parts[ 1 ];
    }
    return result;
  }

  public static String createUIRootId( final WebForm form ) {
    String formId = form.getUniqueID();
    IWindow window = WindowManager.getInstance().findWindow( form );
    if( window == null ) {
      String text = "The form with id ''{0}'' is not associated with a window.";
      Object[] args = new Object[] { form.getUniqueID() };
      String msg = MessageFormat.format( text, args );
      throw new IllegalStateException( msg );
    }
    return createUIRootId( window.getId(), formId );
  }

  private static String createUIRootId( final String windowId, 
                                        final String formId ) 
  {
    StringBuffer uiRoot = new StringBuffer();
    uiRoot.append( windowId );
    uiRoot.append( ";" );
    uiRoot.append( formId );
    return uiRoot.toString();
  }

}
