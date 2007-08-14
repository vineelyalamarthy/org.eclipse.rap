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
package com.w4t.dhtml.webscrollpanekit;

import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.internal.util.HTMLUtil;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.*;
import com.w4t.dhtml.WebScrollPane;
import com.w4t.event.WebRenderEvent;

/**
 * <p>The renderer for all DOM enabled Browser types.</p>
 */
public class WebScrollPaneRenderer_DOM_Script extends DecoratorRenderer {

  private final class ScrollPaneFormMarkupAppender extends FormMarkupAppender {

    private static final String JS_CODE 
      = "try'{'setScrollPosition(''{0}'');'}'catch(e)'{' '}'";
    
    private ScrollPaneFormMarkupAppender( final WebComponent component ) {
      super( component );
    }

    protected void doAfterRender( final WebRenderEvent evt ) 
      throws IOException 
    {
      String id = WebScrollPaneUtil.getSrollDivId( ( WebScrollPane )component );
      String code = MessageFormat.format( JS_CODE, new Object[] { id } );
      HtmlResponseWriter out = getResponseWriter();
      RenderUtil.writeJavaScriptInline( out, code ); 
    }
  }
  
  public void readData( final WebComponent component ) {
    WebScrollPaneUtil.readData( ( WebScrollPane )component );
  }

  public void render( final WebComponent component ) throws IOException {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    manager.register( WebScrollPaneUtil.JS,
                      HTML.CHARSET_NAME_ISO_8859_1,
                      IResourceManager.RegisterOptions.VERSION_AND_COMPRESS );
    getResponseWriter().useJSLibrary( WebScrollPaneUtil.JS );
    super.render( component );    
  }

  /** this method will render the opening tag of the scrollpane */
  protected void createDecoratorHead( final Decorator dec ) throws IOException {
    WebScrollPane wsp = ( WebScrollPane )dec;
    HtmlResponseWriter out = getResponseWriter();
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ID, wsp.getUniqueID(), null );
    out.closeElementIfStarted();
    String scrollDivId = WebScrollPaneUtil.getSrollDivId( wsp );
    HTMLUtil.hiddenInput( out, 
                          WebScrollPaneUtil.PREFIX_SCROLL_X + scrollDivId,
                          String.valueOf( wsp.getScrollX() ) );
    HTMLUtil.hiddenInput( out,
                          WebScrollPaneUtil.PREFIX_SCROLL_Y + scrollDivId,
                          String.valueOf( wsp.getScrollY() ) );
    out.startElement( HTML.DIV, null );
    out.writeAttribute( HTML.ALIGN, HTML.LEFT, null );
    out.writeAttribute( HTML.ID, scrollDivId, null );
    String style = out.registerCssClass( WebScrollPaneUtil.createStyle( wsp ) );
    out.writeAttribute( HTML.CLASS, style, null );
    String code = WebScrollPaneUtil.createGetScrollPositionCode( scrollDivId );
    out.writeAttribute( HTML.ON_MOUSE_MOVE, code, null ) ;
    out.closeElementIfStarted();
  }

  /** this method will render the closing tag of the scrollpane */
  protected void createDecoratorFoot( final Decorator dec ) throws IOException {
    WebScrollPane wsp = ( WebScrollPane )dec;
    HtmlResponseWriter out = getResponseWriter();
    out.endElement( HTML.DIV );
    out.endElement( HTML.DIV );
    ScrollPaneFormMarkupAppender adapter 
      = new ScrollPaneFormMarkupAppender( wsp );
    wsp.getWebForm().addWebRenderListener( adapter );
  }
}