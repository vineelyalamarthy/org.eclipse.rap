/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.internal.widgets.buttonkit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.*;
import org.eclipse.rwt.protocol.IWidgetSynchronizer;
import org.eclipse.rwt.protocol.WidgetSynchronizerFactory;
import org.eclipse.swt.widgets.Button;


final class CheckButtonDelegateLCA extends ButtonDelegateLCA {

  private static final String QX_TYPE = "org.eclipse.rwt.widgets.Button";
  private static final Object[] PARAM_CHECK = new Object[] { "check" };

  static final String PROP_GRAYED = "grayed";

  void preserveValues( final Button button ) {
    IWidgetAdapter adapter = WidgetUtil.getAdapter( button );
    adapter.preserve( PROP_GRAYED, Boolean.valueOf( button.getGrayed() ) );
    ButtonLCAUtil.preserveValues( button );
  }

  void readData( final Button button ) {
    ButtonLCAUtil.readSelection( button );
    ControlLCAUtil.processSelection( button, null, true );
    ControlLCAUtil.processMouseEvents( button );
    ControlLCAUtil.processKeyEvents( button );
    ControlLCAUtil.processMenuDetect( button );
    WidgetLCAUtil.processHelp( button );
  }

  void renderInitialization( final Button button )
    throws IOException
  {
    String[] styles = new String[] { "CHECK" };
    IWidgetSynchronizer synchronizer 
      = WidgetSynchronizerFactory.getSynchronizerForWidget( button );
    synchronizer.newWidget( styles );   
//    JSWriter writer = JSWriter.getWriterFor( button );
//    writer.newWidget( QX_TYPE, PARAM_CHECK );
//    ControlLCAUtil.writeStyleFlags( button ); // done
//    WidgetLCAUtil.writeStyleFlag( button, SWT.CHECK, "CHECK" ); // done
  }

  void renderChanges( final Button button ) throws IOException {
    // TODO [rh] the JSConst.JS_WIDGET_SELECTED does unnecessarily send
    // bounds of the widget that was clicked -> In the SelectionEvent
    // for Button the bounds are undefined
    ButtonLCAUtil.writeChanges( button ); // done
    writeGrayed( button ); // done
  }

  private static void writeGrayed( final Button button ) throws IOException {
    Boolean newValue = Boolean.valueOf( button.getGrayed() );
    String prop = PROP_GRAYED;
    if( WidgetLCAUtil.hasChanged( button, prop, newValue, Boolean.FALSE ) ) {
      JSWriter writer = JSWriter.getWriterFor( button );
      writer.set( prop, newValue );
    }
  }
}
