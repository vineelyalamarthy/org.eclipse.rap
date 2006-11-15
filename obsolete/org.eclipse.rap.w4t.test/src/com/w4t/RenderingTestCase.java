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
package com.w4t;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;
import com.w4t.engine.service.ContextProvider;
import com.w4t.util.CssClass;
import com.w4t.util.WebComponentCounter;

/** <p>The superclass for all tests that concern the rendering 
  * functionalities.</p>
  * 
  * <p>Encapsulates some common functionality.</p>
  */
public abstract class RenderingTestCase extends SelfGeneratingTestCase {

  public RenderingTestCase( final String name ) {
    super( name );
  }


  // common functionality used in subclasses
  //////////////////////////////////////////

  protected void resetWebComponentCounter() throws Exception {
    WebComponentCounter wcc = WebComponentCounter.getInstance();
    Field countField = null;
    int newValue = 0;
    try {
      countField = WebComponentCounter.class.getDeclaredField( "count" );
    } catch( NoSuchFieldException nsfex ) {
      countField = WebComponentCounter.class.getDeclaredField( "b" );
    }
    countField.setAccessible( true );
    countField.set( wcc, new Integer( newValue ) );
  }

  protected void escapeAll( final String[] tokens ) {
//  TODO: check whether there are any problems removing this 
    for( int i = 0; i < tokens.length; i++ ) {
      tokens[ i ] = tokens[ i ].replaceAll( "\"", "\\\\\"" );
      tokens[ i ] = tokens[ i ].replaceAll( "\r", "\\\\r" );
      tokens[ i ] = tokens[ i ].replaceAll( "\n", "\\\\n" );
    }
  }
  
  protected String[] getTokens( final HtmlResponseWriter buffer ) {
    Vector vecTokens = new Vector();
    for( int i = 0; i < buffer.getBodySize(); i++ ) {
      vecTokens.add( buffer.getBodyToken( i ).toString() );
    }
    String[] result = new String[ vecTokens.size() ];
    vecTokens.toArray( result );
    return result;
  }

  protected void doRenderTest( final WebComponent comp, 
                               final int id ) throws Exception 
  {
    HtmlResponseWriter buffer = new HtmlResponseWriter();
    ContextProvider.getStateInfo().setResponseWriter( buffer );
    LifeCycleHelper.render( comp );
    String[] tokens = getTokens( buffer );
    escapeAll( tokens );
    CssClass[] cssClasses = buffer.getCssClasses(); 
    
    String className =   this.getClass().getPackage().getName() 
                       + ".Render_" + String.valueOf( id );
    if( isGenerateResources() ) {
      addResource( "Render_" + String.valueOf( id ), tokens );
      addResource( "Render_" + String.valueOf( id ) + "_css", cssClasses );
    } else {
      compare( tokens, loadEscapedRessources( className ) );
      compare( cssClasses, loadEscapedRessources( className + "_css" ) );
    }
  }

  private String[] loadEscapedRessources( final String className )
                                                              throws Exception {
    Class resourceClass = Class.forName( className );
    Method getRes = resourceClass.getDeclaredMethod( "getRes", new Class[ 0 ] );
    getRes.setAccessible( true );
    String[] tokensRes = ( String[] )getRes.invoke( null, new Object[ 0 ] );
    escapeAll( tokensRes );
    return tokensRes;
  }
}