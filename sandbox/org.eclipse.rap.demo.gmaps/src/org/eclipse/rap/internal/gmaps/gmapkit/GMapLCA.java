/*******************************************************************************
 * Copyright (c) 2002-2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.internal.gmaps.gmapkit;

import java.io.IOException;

import org.eclipse.rap.gmaps.GMap;
import org.eclipse.rwt.lifecycle.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;


public class GMapLCA extends AbstractWidgetLCA {
  
  private static final String JS_PROP_ADDRESS = "address";
  private static final String PROP_ADDRESS = "address";

  public void preserveValues( final Widget widget ) {
    ControlLCAUtil.preserveValues( ( Control )widget );
    IWidgetAdapter adapter = WidgetUtil.getAdapter( widget );
    adapter.preserve( PROP_ADDRESS, ( ( GMap )widget ).getAddress() );
  }
  
  public void readData( final Widget widget ) {
  }
  
  public void renderInitialization( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    String id = WidgetUtil.getId( widget );
    writer.newWidget( "org.eclipse.rap.gmaps.GMap", new Object[] { id } );
    writer.set( "appearance", "composite" );
    writer.set( "overflow", "hidden" );
    ControlLCAUtil.writeStyleFlags( (GMap)widget );
  }

  public void renderChanges( final Widget widget ) throws IOException {
    GMap gmap = ( GMap )widget;
    ControlLCAUtil.writeChanges( gmap );
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.set( PROP_ADDRESS, JS_PROP_ADDRESS, gmap.getAddress() );
  }

  public void renderDispose( final Widget widget ) throws IOException {
    JSWriter writer = JSWriter.getWriterFor( widget );
    writer.dispose();
  }

  public void createResetHandlerCalls( String typePoolId ) throws IOException
  {
    // TODO Auto-generated method stub
    
  }

  public String getTypePoolId( Widget widget )
  {
    // TODO Auto-generated method stub
    return null;
  }
}
