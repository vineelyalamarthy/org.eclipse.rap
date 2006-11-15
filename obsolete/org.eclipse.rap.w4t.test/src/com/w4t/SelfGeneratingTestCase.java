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

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import junit.framework.TestCase;


/** <p>the superclass for TestCases that rewrite their own source files in
 * order to integrate their comparation resources. When set to generation 
 * mode, a subclass of SelfGeneratingTestCase will append package private 
 * classes that provide comparation resources like strings etc.</p>
 * 
 * How to create a self-generating TestCase:
 * =========================================
 * 
 * - extend this class
 * - call <code>setGenerateResources( false );</code> in the constructor
 *     whenever resources are generated, switch 'false' to true, after 
 *     generating set it back to false
 * - in the testing code, do sth. like this:
 * 
 * <code>
 *   String[] tokens          = ...    // collect whatever you want to compare
 *   String resourceClassName = ...    // assign a name for the resource class  
 * 
 *   if( isGenerateResources() ) {
 *     addResource( resourceClassName, tokens );
 *   } else {
 *     Class resourceClass = Class.forName( resourceClassName );
 *     Method getRes 
 *       = resourceClass.getDeclaredMethod( "getRes", new Class[ 0 ] );
 *     getRes.setAccessible( true );
 *     String[] tokensRes = ( String[] )getRes.invoke( null, new Object[ 0 ] );
 *     compare( tokens, tokensRes );
 * </code>
 */
public abstract class SelfGeneratingTestCase extends TestCase {

  private boolean generateResources;
  private final static String MARKER = "//$endOfPublicClass";


  /** contains the resource classes (key: name, element: content as String[])
    * that are rewritten after the test has been run (only when in generation
    * mode). */
  private Hashtable htResources;

  
  public SelfGeneratingTestCase( final String name ) {
    super( name );
    htResources = new Hashtable();
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  public void tearDown() throws Exception {
    Fixture.removeContext();
    Fixture.tearDown();
    if( isGenerateResources() ) {
      StringBuffer sbContent = readPublicClass();
      Enumeration keys = htResources.keys();
      while( keys.hasMoreElements() ) {
        String key = ( String )keys.nextElement();
        Object[] resource = ( Object[] )htResources.get( key );
        appendResource( key, resource, sbContent );
      }
      writeFile( sbContent );
    }
  }
  
  protected void addResource( final String key, final Object[] resource ) {
    htResources.put( key, resource );
  }

  protected void setGenerateResources( final boolean generateResources ) {
    this.generateResources = generateResources;
  }
  
  protected boolean isGenerateResources() {
    return generateResources;
  }
  
  protected void compare( final Object[] names, final String[] namesRes ) {
    StringBuffer namesBuffer = new StringBuffer();
    for( int i = 0; i < names.length; i++ ) {
      namesBuffer.append( names[ i ] );
    }
    
    StringBuffer namesResBuffer = new StringBuffer();
    for( int i = 0; i < namesRes.length; i++ ) {
      namesResBuffer.append( namesRes[ i ] );
    }
    assertEquals( namesResBuffer.toString(), namesBuffer.toString() );
  }


  // helping methods
  //////////////////
  
  private String getSourceFileName() {
    String userDir = System.getProperty( "user.dir" );
    return   userDir + "/INTERNAL/java/test/"
           + this.getClass().getName().replace( '.', '/' )
           + ".java";
  }
  
  private StringBuffer readPublicClass() throws Exception {
    StringBuffer result = new StringBuffer();
    File sourceFile = new File( getSourceFileName() );
    BufferedReader br = null;
    try {
      br = new BufferedReader( new FileReader( sourceFile ) );
    } catch( FileNotFoundException e ) {
      throw new Exception(   "You have probably set generateResources true;\n"
                           + "but you are not on drive T:\\ actually.\n"
                           + "so what had to happen happened:\n"
                           + e.toString() );
    }
    String line = br.readLine();
    while(    line != null
           && line.indexOf( MARKER ) == -1 ) {
      result.append( line );
      result.append( "\r\n" );
      line = br.readLine();
    }
    result.append( MARKER );
    result.append( "\r\n" );
    br.close();
    return result;
  }
  
  private void writeFile( final StringBuffer sbContent ) throws Exception {
    FileWriter fr = new FileWriter( getSourceFileName() );
    fr.write( sbContent.toString() );
    fr.close();
  }
 
  private void appendResource( final String className, 
                               final Object[] resource, 
                               final StringBuffer sb ) {
    String nl = "\r\n";
    sb.append( "class " + className + " {" );
    sb.append( nl );    
    sb.append( nl ); 
    sb.append( "  private static String[] res = new String[] {" );
    sb.append( nl );
    sb.append( createStringList( resource ) );
    sb.append( "  };" );
    sb.append( nl );
    sb.append( nl ); 
    sb.append( "  static String[] getRes() {" );
    sb.append( nl );    
    sb.append( "    return res;" );
    sb.append( nl );    
    sb.append( "  }" );
    sb.append( nl );
    sb.append( "}" );
    sb.append( nl );
    sb.append( nl );
  }
  
  private StringBuffer createStringList( final Object[] resource ) {
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < resource.length; i++ ) {
      result.append( "    \"" );
      result.append( resource[ i ].toString() );
      result.append( "\"" );
      if( i < resource.length - 1 ) {
        result.append( "," );
      } 
      result.append( "\r\n" );
    }
    return result;
  }
}