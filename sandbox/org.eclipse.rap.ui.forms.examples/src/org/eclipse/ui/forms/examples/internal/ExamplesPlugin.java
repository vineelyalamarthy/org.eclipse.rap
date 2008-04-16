/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.forms.examples.internal;
import java.net.URL;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
/**
 * The main plugin class to be used in the desktop.
 */
public class ExamplesPlugin extends AbstractUIPlugin {
	
	public static final String IMG_FORM_BG = "formBg";
	public static final String IMG_LARGE = "large";
	public static final String IMG_HORIZONTAL = "horizontal";
	public static final String IMG_VERTICAL = "vertical";
	public static final String IMG_SAMPLE = "sample";
	public static final String IMG_WIZBAN = "wizban";
	public static final String IMG_LINKTO_HELP = "linkto_help";
	public static final String IMG_HELP_TOPIC = "help_topic";
	public static final String IMG_CLOSE = "close";
	public static final String IMG_HELP_CONTAINER = "container_obj";
	public static final String IMG_HELP_TOC_OPEN = "toc_open";
	public static final String IMG_HELP_TOC_CLOSED = "toc_closed";
	public static final String IMG_HELP_SEARCH = "e_search_menu";
	public static final String IMG_CLEAR = "clear";
	public static final String IMG_NW = "nw";

	//The shared instance.
	private static ExamplesPlugin plugin;
	
	private FormColors formColors;

  public static ExamplesPlugin getDefault() {
    return plugin;
  }

	public void start( BundleContext context ) throws Exception {
	  super.start( context );
	  plugin = this;
	}
	
	public void stop( BundleContext context ) throws Exception {
    try {
      if( formColors != null ) {
        formColors.dispose();
        formColors = null;
      }
    } finally {
      super.stop( context );
    }
  }

  public FormColors getFormColors( Display display ) {
    if( formColors == null ) {
      formColors = new FormColors( display );
      formColors.markShared();
    }
    return formColors;
  }

	public ImageRegistry getImageRegistry() {
	  return ImageRegistryStore.getInstance().imageRegistry;
	}
	
	public Image getImage(String key) {
	  return getImageRegistry().get( key );
	}
	
	public ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}
	
  private static final class ImageRegistryStore extends SessionSingletonBase {
    
    public static ImageRegistryStore getInstance() {
      return ( ImageRegistryStore )getInstance( ImageRegistryStore.class );
    }
    
    private final ImageRegistry imageRegistry;
    
    public ImageRegistryStore() {
      imageRegistry = new ImageRegistry();
      initialize();
    }
    
    public ImageRegistry getImageRegistry() {
      return imageRegistry;
    }
    
    private void initialize() {
      registerImage( imageRegistry, IMG_FORM_BG, "form_banner.gif" );
      registerImage( imageRegistry, IMG_LARGE, "large_image.gif" );
      registerImage( imageRegistry, IMG_HORIZONTAL, "th_horizontal.gif" );
      registerImage( imageRegistry, IMG_VERTICAL, "th_vertical.gif" );
      registerImage( imageRegistry, IMG_SAMPLE, "sample.gif" );
      registerImage( imageRegistry, IMG_WIZBAN, "newprj_wiz.gif" );
      registerImage( imageRegistry, IMG_LINKTO_HELP, "linkto_help.gif" );
      registerImage( imageRegistry, IMG_HELP_TOPIC, "topic.gif" );
      registerImage( imageRegistry, IMG_HELP_CONTAINER, "container_obj.gif" );
      registerImage( imageRegistry, IMG_HELP_TOC_CLOSED, "toc_closed.gif" );
      registerImage( imageRegistry, IMG_HELP_TOC_OPEN, "toc_open.gif" );
      registerImage( imageRegistry, IMG_CLOSE, "close_view.gif" );
      registerImage( imageRegistry, IMG_HELP_SEARCH, "e_search_menu.gif" );
      registerImage( imageRegistry, IMG_CLEAR, "clear.gif" );
      registerImage( imageRegistry, IMG_NW, "nw.gif" );
    }

    private static void registerImage( ImageRegistry registry,
                                       String key,
                                       String fileName )
    {
      try {
        IPath path = new Path( "icons/" + fileName );
        Bundle bundle = getDefault().getBundle();
        URL url = FileLocator.find( bundle, path, null );
        if( url != null ) {
          ImageDescriptor desc = ImageDescriptor.createFromURL( url );
          registry.put( key, desc );
        }
      } catch( Exception e ) {
      }
    }
  }
}
