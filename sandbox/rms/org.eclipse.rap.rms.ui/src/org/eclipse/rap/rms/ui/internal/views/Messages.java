package org.eclipse.rap.rms.ui.internal.views;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

  private static final String BUNDLE_NAME
    = "org.eclipse.rap.rms.ui.internal.views.messages";
  private static final ResourceBundle RESOURCE_BUNDLE
    = ResourceBundle.getBundle( BUNDLE_NAME );

  private Messages() {
  }

  public static String getString( final String key ) {
    String result;
    try {
      result = RESOURCE_BUNDLE.getString( key );
      return result;
    } catch( MissingResourceException e ) {
      result = '!' + key + '!';
    }
    return result;
  }
}
