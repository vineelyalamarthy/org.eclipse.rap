package org.eclipse.rap.rms.ui.internal.startup;

import java.text.MessageFormat;
import java.util.Locale;

import org.eclipse.rap.rms.ui.internal.Activator;
import org.eclipse.rap.rms.ui.internal.datamodel.EntityAdapter;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * This class controls all aspects of the application's execution
 */
public class RMS implements IEntryPoint {

  static {
    EntityAdapter.editorInputRegistry = new EditorInputRegistry();
    Locale.setDefault( Locale.ENGLISH );
  }

  public int createUI() {
    String locale = RWT.getRequest().getParameter( "locale" );
    if( locale != null ) {
      if( "de".equals( locale ) ) {
        RWT.setLocale( Locale.GERMAN );
      } else if( "zh".equals( locale ) ) {
        RWT.setLocale( Locale.CHINESE );
      } else {
        String txt = "Warning: Locale parameter ''{0}'' not supported.";
        String msg = MessageFormat.format( txt, new Object[] { locale } );
        System.out.println( msg );
      }
    }
  
    Activator.getDefault().initializeImageRegistry( "org.eclipse.rap.rms.ui" );
    UICallBack.activate( RMS.class.getName() );
    Display display = PlatformUI.createDisplay();
    RMSWorkbenchAdvisor workbenchAdvisor = new RMSWorkbenchAdvisor();
    return PlatformUI.createAndRunWorkbench( display, workbenchAdvisor );
  }
}
