/*******************************************************************************
 * Copyright (c) 2007, 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package org.eclipse.rwt.internal.theme;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Reader for theme definition files. These are the "*.theme.xml" files that
 * define themeable properties of a certain widget.
 */
public class NewThemeDefinitionReader {

  private static final String[] EMPTY_STRING_ARRAY = new String[ 0 ];
  public static interface ThemeDefHandler {

    public abstract void readThemeProperty( ThemeProperty def );
  }
  private static final String NODE_ROOT = "theme";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_DESCRIPTION = "description";
  private static final String THEME_DEF_SCHEMA = "themedef.xsd";
  private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
  private final InputStream inputStream;
  private final String fileName;

  /**
   * An instance of this class reads theme definitions from an XML resource.
   * 
   * @param inputStream input stream from a theme definition XML
   */
  public NewThemeDefinitionReader( final InputStream inputStream,
                                   final String fileName )
  {
    this.fileName = fileName;
    if( inputStream == null ) {
      throw new NullPointerException( "null argument" );
    }
    this.inputStream = inputStream;
  }

  public ThemeDefElement[] read() throws SAXException, IOException {
    Document document;
    document = parseThemeDefinition( inputStream );
    Node root = document.getElementsByTagName( NODE_ROOT ).item( 0 );
    NodeList childNodes = root.getChildNodes();
    List elementList = new ArrayList();
    for( int i = 0; i < childNodes.getLength(); i++ ) {
      Node node = childNodes.item( i );
      if( node.getNodeType() == Node.ELEMENT_NODE ) {
        if( "element".equals( node.getNodeName() ) ) {
          ThemeDefElement element = readThemeDefElement( node );
          elementList.add( element );
        }
      }
    }
    ThemeDefElement[] result = new ThemeDefElement[ elementList.size() ];
    elementList.toArray( result );
    return result;
  }

  private ThemeDefElement readThemeDefElement( final Node node ) {
    String name = getAttributeValue( node, ATTR_NAME );
    String description = getAttributeValue( node, ATTR_DESCRIPTION );
    NodeList childNodes = node.getChildNodes();
    List propertyList = new ArrayList();
    Map styleMap = new HashMap();
    Map stateMap = new HashMap();
    for( int i = 0; i < childNodes.getLength(); i++ ) {
      Node childNode = childNodes.item( i );
      if( childNode.getNodeType() == Node.ELEMENT_NODE ) {
        if( "property".equals( childNode.getNodeName() ) ) {
          ThemeDefProperty property = readThemeDefProperty( childNode );
          propertyList.add( property );
        } else if( "style".equals( childNode.getNodeName() ) ) {
          styleMap.put( getAttributeValue( childNode, ATTR_NAME ),
                        getAttributeValue( childNode, ATTR_DESCRIPTION ) );
        } else if( "state".equals( childNode.getNodeName() ) ) {
          stateMap.put( getAttributeValue( childNode, ATTR_NAME ),
                        getAttributeValue( childNode, ATTR_DESCRIPTION ) );
        }
      }
    }
    ThemeDefProperty[] properties = new ThemeDefProperty[ propertyList.size() ];
    propertyList.toArray( properties );
    return new ThemeDefElement( name,
                                description,
                                properties,
                                styleMap,
                                stateMap );
  }

  private ThemeDefProperty readThemeDefProperty( final Node node ) {
    String name = getAttributeValue( node, ATTR_NAME );
    String description = getAttributeValue( node, ATTR_DESCRIPTION );
    String stylesStr = getAttributeValue( node, "styles" );
    String[] styles;
    if( stylesStr != null ) {
      styles = stylesStr.split( "\\s+" );
    } else {
      styles = EMPTY_STRING_ARRAY;
    }
    String statesStr = getAttributeValue( node, "states" );
    String[] states = null;
    if( statesStr != null ) {
      states = statesStr.split( "\\s+" );
    } else {
      states = EMPTY_STRING_ARRAY;
    }
    return new ThemeDefProperty( name, description, styles, states );
  }

  private static String getAttributeValue( final Node node, final String name )
  {
    String result = null;
    NamedNodeMap attributes = node.getAttributes();
    if( attributes != null ) {
      Node namedItem = attributes.getNamedItem( name );
      if( namedItem != null ) {
        result = namedItem.getNodeValue();
      }
    }
    return result;
  }

  private Document parseThemeDefinition( final InputStream is )
    throws SAXException, IOException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware( true );
    ClassLoader loader = NewThemeDefinitionReader.class.getClassLoader();
    final URL schema = loader.getResource( THEME_DEF_SCHEMA );
    factory.setValidating( schema != null );
    try {
      factory.setAttribute( JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA );
    } catch( final IllegalArgumentException iae ) {
      // XML-Processing does not support JAXP 1.2 or greater
      factory.setNamespaceAware( false );
      factory.setValidating( false );
    }
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
    } catch( final ParserConfigurationException e ) {
      String message = "Failed to initialize parser for theme definition files";
      throw new RuntimeException( message, e );
    }
    // builder.setEntityResolver( new EntityResolver() {
    // public InputSource resolveEntity( final String publicID,
    // final String systemID )
    // throws IOException, SAXException
    // {
    // InputSource result = null;
    // if( schema != null && systemID.endsWith( THEME_DEF_SCHEMA ) ) {
    // URLConnection connection = schema.openConnection();
    // connection.setUseCaches( false );
    // result = new InputSource( connection.getInputStream() );
    // }
    // return result;
    // }
    // } );
    builder.setErrorHandler( new ThemeDefinitionErrorHandler() );
    return builder.parse( is );
  }
  // TODO: Logging instead of sysout
  private class ThemeDefinitionErrorHandler implements ErrorHandler {

    public void error( final SAXParseException spe ) throws SAXException {
      System.err.println( "Error parsing theme definition "
                          + getPosition( spe )
                          + ":" );
      System.err.println( spe.getMessage() );
    }

    public void fatalError( final SAXParseException spe ) throws SAXException {
      System.err.println( "Fatal error parsing theme definition "
                          + getPosition( spe )
                          + ":" );
      System.err.println( spe.getMessage() );
    }

    public void warning( final SAXParseException spe ) throws SAXException {
      System.err.println( "Warning parsing theme definition "
                          + getPosition( spe )
                          + ":" );
      System.err.println( spe.getMessage() );
    }

    private String getPosition( final SAXParseException spe ) {
      return "in file '"
             + fileName
             + "' at line "
             + spe.getLineNumber()
             + ", col "
             + spe.getColumnNumber();
    }
  }
}
