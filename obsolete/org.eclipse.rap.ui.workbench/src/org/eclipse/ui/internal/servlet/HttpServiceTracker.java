package org.eclipse.ui.internal.servlet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class HttpServiceTracker extends ServiceTracker {

  public static final String DEFAULT_SERVLET = "rap";
  
  private HttpService httpService;
  private ArrayList servletAliases = new ArrayList();

  public HttpServiceTracker( BundleContext context ) {
    super( context, HttpService.class.getName(), null );
  }

  public Object addingService( ServiceReference reference ) {
    httpService = ( HttpService )context.getService( reference );
    if(servletAliases.size() == 0) {
    	// register default servlet
    	servletAliases.add(DEFAULT_SERVLET);
    }
    for( Iterator it = servletAliases.iterator(); it.hasNext(); ) {
      String name = ( String )it.next();
      try {
        httpService.registerServlet( "/" + name, new RequestHandler(), null, null ); //$NON-NLS-1$
      } catch( Exception e ) {
        String text =   "Could not register servlet mapping ''{0}''.";
        Object[] param = new Object[] { name };
        String msg = MessageFormat.format( text, param );
        Status status = new Status( IStatus.ERROR,
                                    PlatformUI.PLUGIN_ID,
                                    IStatus.OK,
                                    msg,
                                    e );
        WorkbenchPlugin.getDefault().getLog().log( status );
      }
    }
    return httpService;
  }

  public void removedService( ServiceReference reference, Object service ) {
    for( Iterator iterator = servletAliases.iterator(); iterator.hasNext(); ) {
      String name = ( String )iterator.next();
      httpService.unregister( "/" + name ); //$NON-NLS-1$
    }
    super.removedService( reference, service );
  }
  
  public void addServletAlias(String name) {
    servletAliases.add( name );
  }
}
