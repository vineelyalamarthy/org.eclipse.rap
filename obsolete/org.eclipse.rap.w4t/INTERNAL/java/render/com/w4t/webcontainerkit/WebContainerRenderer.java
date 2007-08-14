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
package com.w4t.webcontainerkit;

import java.io.IOException;

import org.eclipse.rwt.internal.browser.*;
import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.util.HTML;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.*;
import com.w4t.util.RendererCache;


/** <p>the superclass of all Renderers that render org.eclipse.rap.WebContainer.</p>
  */
public abstract class WebContainerRenderer extends Renderer {

  private static final String WINDOWMANAGER_JS
    = "resources/js/windowmanager/windowmanager.js";
    
  private static final String EVENTHANDLER_IE_JS 
    = "resources/js/eventhandler/eventhandler_ie.js";
  private static final String EVENTHANDLER_DEFAULT_JS 
    = "resources/js/eventhandler/eventhandler_default.js";
  
  // TODO: [fappel] move this to the respective component renderer
  static {
    IResourceManager manager = W4TContext.getResourceManager();
    manager.register( "resources/images/add.gif" );
    manager.register( "resources/images/blindlight.gif" );
    manager.register( "resources/images/commit.gif" );
    manager.register( "resources/images/copy.gif" );
    manager.register( "resources/images/cut.gif" );
    manager.register( "resources/images/del.gif" );
    manager.register( "resources/images/delete.gif" );
    manager.register( "resources/images/disabledcopy.gif" );
    manager.register( "resources/images/disabledcut.gif" );
    manager.register( "resources/images/disableddelete.gif" );
    manager.register( "resources/images/disabledfirstrec.gif" );
    manager.register( "resources/images/disabledlastrec.gif" );
    manager.register( "resources/images/disablednextrec.gif" );
    manager.register( "resources/images/disabledpointer.gif" );
    manager.register( "resources/images/disabledprevrec.gif" );
    manager.register( "resources/images/disabledtransparent.gif" );
    manager.register( "resources/images/doubleclick.gif" );
    manager.register( "resources/images/dragDrop.gif" );
    manager.register( "resources/images/favicon.ico" );
    manager.register( "resources/images/firstrec.gif" );
    manager.register( "resources/images/firstrecd.gif" );
    manager.register( "resources/images/greenlight.gif" );
    manager.register( "resources/images/insert.gif" );
    manager.register( "resources/images/lastrec.gif" );
    manager.register( "resources/images/lastrecd.gif" );
    manager.register( "resources/images/nextpage.gif" );
    manager.register( "resources/images/nextpaged.gif" );
    manager.register( "resources/images/nextrec.gif" );
    manager.register( "resources/images/open.gif" );
    manager.register( "resources/images/pointer.gif" );
    manager.register( "resources/images/prevpage.gif" );
    manager.register( "resources/images/prevpaged.gif" );
    manager.register( "resources/images/prevrec.gif" );
    manager.register( "resources/images/redlight.gif" );
    manager.register( "resources/images/reset.gif" );
    manager.register( "resources/images/save.gif" );
    manager.register( "resources/images/saveCode.gif" );
    manager.register( "resources/images/submitter.gif" );
    manager.register( "resources/images/transparent.gif" );
    manager.register( "resources/images/treeview/closedFolder.gif" );
    manager.register( "resources/images/treeview/document.gif" );
    manager.register( "resources/images/treeview/documentCol.gif" );
    manager.register( "resources/images/treeview/documentExp.gif" );
    manager.register( "resources/images/treeview/docWithChildsCol.gif" );
    manager.register( "resources/images/treeview/docWithChildsExp.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_Empty.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_IconCol.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_IconExpWithChildren.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_IconExpWithoutChildren.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_Inner.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_Last.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_LeafIcon.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_Line.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_MinusInner.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_MinusLast.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_PlusInner.gif" );
    manager.register( "resources/images/treeview/explorer/Explorer_PlusLast.gif" );
    manager.register( "resources/images/treeview/folderCol.gif" );
    manager.register( "resources/images/treeview/folderExp.gif" );
    manager.register( "resources/images/treeview/leaf.gif" );
    manager.register( "resources/images/treeview/minus.gif" );
    manager.register( "resources/images/treeview/modern/Modern_Empty.gif" );
    manager.register( "resources/images/treeview/modern/Modern_IconCol.gif" );
    manager.register( "resources/images/treeview/modern/Modern_IconExpWithChildren.gif" );
    manager.register( "resources/images/treeview/modern/Modern_IconExpWithoutChildren.gif" );
    manager.register( "resources/images/treeview/modern/Modern_Inner.gif" );
    manager.register( "resources/images/treeview/modern/Modern_Last.gif" );
    manager.register( "resources/images/treeview/modern/Modern_LeafIcon.gif" );
    manager.register( "resources/images/treeview/modern/Modern_Line.gif" );
    manager.register( "resources/images/treeview/modern/Modern_MinusInner.gif" );
    manager.register( "resources/images/treeview/modern/Modern_MinusLast.gif" );
    manager.register( "resources/images/treeview/modern/Modern_PlusInner.gif" );
    manager.register( "resources/images/treeview/modern/Modern_PlusLast.gif" );
    manager.register( "resources/images/treeview/openFolder.gif" );
    manager.register( "resources/images/treeview/plus.gif" );
    manager.register( "resources/images/treeview/swing/Swing_Empty.gif" );
    manager.register( "resources/images/treeview/swing/Swing_IconCol.gif" );
    manager.register( "resources/images/treeview/swing/Swing_IconExpWithChildren.gif" );
    manager.register( "resources/images/treeview/swing/Swing_IconExpWithoutChildren.gif" );
    manager.register( "resources/images/treeview/swing/Swing_Inner.gif" );
    manager.register( "resources/images/treeview/swing/Swing_Last.gif" );
    manager.register( "resources/images/treeview/swing/Swing_LeafIcon.gif" );
    manager.register( "resources/images/treeview/swing/Swing_Line.gif" );
    manager.register( "resources/images/treeview/swing/Swing_MinusInner.gif" );
    manager.register( "resources/images/treeview/swing/Swing_MinusLast.gif" );
    manager.register( "resources/images/treeview/swing/Swing_PlusInner.gif" );
    manager.register( "resources/images/treeview/swing/Swing_PlusLast.gif" );
    manager.register( "resources/images/update.gif" );
    manager.register( "resources/images/yellowlight.gif" );
  }

  public void processAction( final WebComponent component ) {
    WebContainer container = ( WebContainer )component;
    WebLayout layout = container.getWebLayout();
    RendererCache rendererCache = RendererCache.getInstance();
    
    if( rendererCache.getRendererClass( layout.getClass() ) != null ) {
      Renderer renderer = rendererCache.retrieveRenderer( layout.getClass() );
      renderer.processAction( component );
    }
  }
  
  public void scheduleRendering( final WebComponent component ) {
    WebContainer container = ( WebContainer )component;
    WebLayout layout = container.getWebLayout();
    RendererCache rendererCache = RendererCache.getInstance();
    
    if( rendererCache.getRendererClass( layout.getClass() ) != null ) {
      Renderer renderer = rendererCache.retrieveRenderer( layout.getClass() );
      renderer.scheduleRendering( container );
    } else {
      for( int i = 0; i < container.getWebComponentCount(); i++ ) {
        if( container.get( i ).isVisible() ) {
          getRenderingSchedule().schedule( container.get( i ) );
        }
      }
    }
  }
  
  public void render( final WebComponent component ) throws IOException {
    setJSLibraries();
    WebContainer wct = ( WebContainer )component;
    wct.getWebLayout().layoutWebContainer( wct );
  }
  
  // TODO [rh] Find a better solution than hardwired browser to js mapping
  private void setJSLibraries() {
    IResourceManager manager = ResourceManagerImpl.getInstance();
    // EventHandler
    Browser browser = W4TContext.getBrowser();
    String eventHandler;
    if( browser instanceof Mozilla ) {
      eventHandler = EVENTHANDLER_DEFAULT_JS;
    } else if( browser instanceof Opera8 || browser instanceof Opera8up ) {
      eventHandler = EVENTHANDLER_DEFAULT_JS;
    } else if( browser instanceof Safari ) {
      eventHandler = EVENTHANDLER_DEFAULT_JS;
    } else {
      eventHandler = EVENTHANDLER_IE_JS;
    }
    manager.register( eventHandler, 
                      HTML.CHARSET_NAME_ISO_8859_1,
                      IResourceManager.RegisterOptions.VERSION_AND_COMPRESS );
    getResponseWriter().useJSLibrary( eventHandler );
    // WindowManager
    manager.register( WINDOWMANAGER_JS, 
                      HTML.CHARSET_NAME_ISO_8859_1, 
                      IResourceManager.RegisterOptions.VERSION_AND_COMPRESS );
    getResponseWriter().useJSLibrary( WINDOWMANAGER_JS );
  }
}