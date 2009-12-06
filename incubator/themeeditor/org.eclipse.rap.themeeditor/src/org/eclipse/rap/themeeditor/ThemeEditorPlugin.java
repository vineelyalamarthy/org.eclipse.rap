package org.eclipse.rap.themeeditor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class ThemeEditorPlugin extends AbstractUIPlugin {

  public static final String IMG_WARNING = "warning_obj.gif";
  public static final String IMG_ERROR = "error_obj.gif";
  public static final String IMG_FIELD_PRIVATE = "field_private_obj.gif";
  public static final String IMG_PUBLIC = "public_co.gif";

  public static final String PLUGIN_ID = "org.eclipse.rap.themeeditor";

  private static ThemeEditorPlugin sharedInstance;

  private ColorRegistry colorRegistry;

  public void start( BundleContext context ) throws Exception {
    super.start( context );
    sharedInstance = this;
  }

  public void stop( BundleContext context ) throws Exception {
    sharedInstance = null;
    super.stop( context );
  }

  public static ThemeEditorPlugin getDefault() {
    return sharedInstance;
  }

  public ColorRegistry getColorRegistry() {
    if( colorRegistry == null ) {
      colorRegistry = new ColorRegistry( Display.getDefault() );
    }
    return colorRegistry;
  }

  public Image getImage( String key ) {
    return getImageRegistry().get( key );
  }

  public static ImageDescriptor getImageDescriptor( final String path ) {
    return imageDescriptorFromPlugin( PLUGIN_ID, path );
  }

  protected void initializeImageRegistry( final ImageRegistry registry ) {
    registerImage( registry, IMG_WARNING, IMG_WARNING );
    registerImage( registry, IMG_ERROR, IMG_ERROR );
    registerImage( registry, IMG_FIELD_PRIVATE, IMG_FIELD_PRIVATE );
    registerImage( registry, IMG_PUBLIC, IMG_PUBLIC );
  }

  private void registerImage( final ImageRegistry registry,
                              final String key,
                              final String fileName )
  {
    String path = "icons/" + fileName;
    ImageDescriptor descriptor = getImageDescriptor( path );
    registry.put( key, descriptor );
  }
}
