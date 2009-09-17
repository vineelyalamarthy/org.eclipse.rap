package org.eclipse.rap.internal.product;

import org.eclipse.equinox.internal.app.IBranding;
import org.osgi.framework.Bundle;


public class NullBranding implements IBranding {

  public String getApplication() {
    return null;
  }

  public Bundle getDefiningBundle() {
    return null;
  }

  public String getDescription() {
    return null;
  }

  public String getId() {
    return null;
  }

  public String getName() {
    return null;
  }

  public Object getProduct() {
    return null;
  }

  public String getProperty( String key ) {
    return null;
  }
}
