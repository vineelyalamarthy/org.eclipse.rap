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
package com.w4t.util;


/** <p>Encapsulates a URI to a property in a resource (which belongs to 
  * a resource bundle, usually for i18n).</p>
  * 
  * <p>URIs used within W4 Toolkit have a form like:</p>
  * 
  * <pre>
  *   property://&lt;propertyKey&gt;@&lt;bundleBaseName&gt;
  * </pre>
  * 
  * <p>In a lean pseudo-BNF notation, this means:
  *   <pre>
  *     propertyURI 
  *             ::= &lt;scheme&gt;://&lt;propertyKey&gt;@&lt;bundleBaseName&gt;
  *     scheme  ::= property
  *     name    ::= &lt;identifier&gt;
  *     bundleBaseName ::= &lt;identifier&gt;
  *   </pre>
  * </p>
  * 
  * <p>See also RFC 2396 - Uniform Resource Identifiers (URI): 
  *                        Generic Syntax.</p>
  */
public class PropertyURI {

  /** <p>The schema identifier for property URIs within W4 Toolkit
    * (see RFC 2396 - Uniform Resource Identifiers (URI): Generic Syntax).</p>
    */
  public static final String PROTOCOL = "property";
  private static final String SCHEME_SEPARATOR = "://";
  private static final String NAME_SEPARATOR = "@";

  private String name;
  private String bundleBaseName;
  
  public PropertyURI( final String name, final String bundleBaseName ) {
    this.name = name;
    this.bundleBaseName = bundleBaseName;
  }
  
  /** a convenience constructor that takes an URL String. */ 
  public PropertyURI( final String urlString ) 
    throws InvalidPropertyURIException
  {
    parse( urlString.trim() );
  }

  // no javadoc here (inherited from java.lang.Object)
  public String toString() {
    return PROTOCOL + SCHEME_SEPARATOR + name + NAME_SEPARATOR + bundleBaseName;
  }

  public static boolean isValid( final String urlString ) {
    boolean result = false;
    if( urlString != null ) {
      int index = urlString.indexOf( SCHEME_SEPARATOR );
      result =    urlString.startsWith( PROTOCOL )
               && index != -1
               && urlString.indexOf( NAME_SEPARATOR, index ) != -1;
    }
    return result;
  }


  // helping methods
  //////////////////

  private void parse( final String urlString ) 
                                            throws InvalidPropertyURIException {
    if( !isValid( urlString ) ) {
      throw new InvalidPropertyURIException( urlString );
    }
    int index = getOffset( urlString, SCHEME_SEPARATOR ); 
    String content = urlString.substring( index );
    this.name = parseName( content );
    this.bundleBaseName = parseBundleBaseName( content );
  }
  
  private String parseBundleBaseName( final String urlString ) {
    int start = getOffset( urlString, NAME_SEPARATOR );
    return urlString.substring( start );
  }

  private int getOffset( final String content, final String delim ) {
    return content.indexOf( delim ) + delim.length();
  }

  private String parseName( final String urlString ) {
    return urlString.substring( 0, urlString.indexOf( NAME_SEPARATOR ) );
  }


  // attribute getters and setters
  ////////////////////////////////

  public String getBundleBaseName() {
    return bundleBaseName;
  }

  public String getName() {
    return name;
  }

  public void setBundleBaseName( final String bundleBaseName ) {
    this.bundleBaseName = bundleBaseName;
  }

  public void setName( final String name ) {
    this.name = name;
  }
}
