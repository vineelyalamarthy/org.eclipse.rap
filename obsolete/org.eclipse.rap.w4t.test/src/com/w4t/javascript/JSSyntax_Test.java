///*******************************************************************************
// * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     Innoopract Informationssysteme GmbH - initial API and implementation
// ******************************************************************************/
//package com.w4t.javascript;
//
//import java.io.*;
//import java.net.URL;
//import java.net.URLConnection;
//
//import junit.framework.TestCase;
//
//import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
//import org.eclipse.rwt.internal.util.HTML;
//import org.eclipse.rwt.resources.IResourceManager;
//import org.mozilla.javascript.*;
//
//
//
//public class JSSyntax_Test extends TestCase {
//  
//  public void testLibraries() throws Exception {
//    // eventhandler
//    doTestLibrary( "resources/js/eventhandler/eventhandler_default.js" );
//    doTestLibrary( "resources/js/eventhandler/eventhandler_ie.js" );
//    // menubar
//    doTestLibrary( "resources/js/menubar/menubar_default.js" );
//    doTestLibrary( "resources/js/menubar/menubar_ie.js" );
//    doTestLibrary( "resources/js/menubar/menubar_nav6.js" );
//    // scrollpane
//    doTestLibrary( "resources/js/scrollpane/scrollpane.js" );
//    // treview
//    doTestLibrary( "resources/js/treeview/treeview_ie_gecko.js" );
//    doTestLibrary( "resources/js/treeview/treeview_default.js" );
//    // windowmanager
//    doTestLibrary( "resources/js/windowmanager/windowmanager.js" );
//  }
//
//  /** 
//   * <p>Tries to compile the JavaScript file denoted by <code>name</code>. An 
//   * exception is thrown when a syntax error occurs.</p>
//   */
//  private static void doTestLibrary( final String name ) throws Exception {
//    if( !ContextFactory.hasExplicitGlobal() ) {
//      ContextFactory.initGlobal( new TestContextFactory() );
//    }
//    Context context = Context.enter();
//    try {
//      context.setLanguageVersion( Context.VERSION_1_5 );
//      Scriptable scope = createScope( context );
//      String script = readText( name, HTML.CHARSET_NAME_ISO_8859_1 );
//      context.evaluateString( scope, script, name, 1, null );
//    } finally {
//      Context.exit();
//    }
//  }
//
//  private static Scriptable createScope( Context context ) throws Exception {
//    Scriptable scope = context.initStandardObjects();
//    // DOMDocument
//    ScriptableObject.defineClass( scope, DOMDocument.class );
//    Scriptable document = context.newObject( scope, "DOMDocument" );
//    scope.put( "document", scope, document );
//    // Event
//    ScriptableObject.defineClass( scope, Event.class );
//    return scope;
//  }
//  
//  private static String readText( final String name, final String charset ) 
//    throws IOException
//  {
//    // read resource
//    StringBuffer buffer = new StringBuffer();
//    InputStream is = openStream( name );
//    try {
//      InputStreamReader reader = new InputStreamReader( is, charset );
//      BufferedReader br = new BufferedReader( reader );
//      try {
//        int character = br.read();    
//        while( character != -1 ) {
//          buffer.append( ( char )character );
//          character = br.read();
//        }
//      } finally {
//        br.close();
//      }
//    } finally {
//      is.close();
//    }
//    return buffer.toString();
//  }
//  
//  private static InputStream openStream( final String name ) 
//    throws IOException 
//  {
//    ClassLoader loader = ResourceManagerImpl.class.getClassLoader();
//    URL resource = loader.getResource( name );
//    if( resource == null ) {
//      IResourceManager manager = ResourceManagerImpl.getInstance();
//      resource = manager.getResource( name );
//    }
//    if( resource == null ) {
//      throw new IOException( "Resource to read not found: " + name );
//    }
//    URLConnection con = resource.openConnection();
//    con.setUseCaches( false );
//    InputStream result = con.getInputStream();
//    return result;
//  }
//}
