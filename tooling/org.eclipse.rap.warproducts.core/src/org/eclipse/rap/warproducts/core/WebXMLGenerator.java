/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


public class WebXMLGenerator {
  
  private List commandLineArguments;
  private Map initParameter;
  private List urlPatterns;
  private String servletName;
  private String displayName;
  private String description;
  private String servletClass;
  private String customXML;
  
  
  public WebXMLGenerator( final String servletName, final String urlPattern ) {
    this.servletName = servletName;
    servletClass = "org.eclipse.equinox.servletbridge.BridgeServlet";    
    urlPatterns = new ArrayList();
    urlPatterns.add( urlPattern );
  }

  public void addCommandLineArgument( final String argument ) {
    if( commandLineArguments == null ) {
      commandLineArguments = new ArrayList();
    }
    commandLineArguments.add( argument );
  }
  
  public void addInitParameter( final String paramName, 
                                final String paramValue ) 
  {
    if( initParameter == null ) {
      initParameter = new HashMap();
    }
    initParameter.put( paramName, paramValue );
  }
  
  public void setServletName( final String servletName ) {
    this.servletName = servletName;
  }
  
  public void setServletClass( final String servletClass ) {
    this.servletClass = servletClass;
  }
  
  public void setDisplayName( final String displayName ) {
    this.displayName = displayName;
  }
  
  public void setDescription( final String description ) {
    this.description = description;
  }
  
  public void addURLPattern( final String urlPattern ) {
    if( !urlPatterns.contains( urlPattern ) ) {
      urlPatterns.add( urlPattern );
    }
  }
  
  public void addXMLContentToWebApp( final String customXML ) {
    this.customXML = customXML;
  }

  public String createWebXML() {
    String result = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
    try {
      Document document = createDocumentForWebXML( factory );
      DOMSource domSource = new DOMSource( document );
      Element webAppElement = createWebAppElement( document );
      Element servletElement = createServletElement( document, webAppElement );      
      hookConfiguration( document, webAppElement, servletElement );      
      result = transformXMLToString( domSource );
    } catch( final Exception e ) {
      System.err.println( "Could not create web.xml" );
      e.printStackTrace();
    }
    return result;
  }

  private Document createDocumentForWebXML( 
    final DocumentBuilderFactory factory )
    throws ParserConfigurationException
  {
    DocumentBuilder documentBuilder = factory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();
    return document;
  }
  
  private void hookConfiguration( final Document document,
                                  final Element webAppElement,
                                  final Element servletElement )
  {
    hookServletName( document, servletElement );
    hookServletClass( document, servletElement );
    hookDisplayName( document, servletElement );
    hookDescription( document, servletElement );
    hookCommandLineArguments( document, servletElement );
    hookInitParameter( document, servletElement );
    hookServletMapping( document, webAppElement );
    hookCustomXML( document, webAppElement );
  }
  
  private void hookServletName( final Document document, 
                                final Element servletElement ) 
  {
    createElement( document, servletElement, "servlet-name", servletName );
  }
  
  private void hookServletClass( final Document document, 
                                 final Element parentElement ) 
  {
    createElement( document, parentElement, "servlet-class", servletClass );
  }
  
  private void hookDisplayName( final Document document, 
                                final Element parentElement ) 
  {
    if( displayName != null ) {
      createElement( document, parentElement, "display-name", displayName );
    }
  }
  
  private void hookDescription( final Document document, 
                                final Element parentElement ) 
  {
    if( description != null ) {
      createElement( document, parentElement, "description", description );
    }
  }

  private Element createWebAppElement( Document document ) {
    Element webAppElement = createElement( document, 
                                           document, 
                                           "web-app", 
                                           null );
    webAppElement.setAttribute( "id", "WebApp" );
    return webAppElement;
  }
  
  private Element createServletElement( final Document document, 
                                        final Element parentElement )
  {
    Element servletElement = createElement( document, 
                                            parentElement, 
                                            "servlet", 
                                            null );
    servletElement.setAttribute( "id", "bridge" );
    return servletElement;
  }
  
  private Element createElement( final Document document,
                                 final Node parentElement,
                                 final String elementName,
                                 final String elementValue )
  {
    Element element = document.createElement( elementName );
    if( elementValue != null ) {
      element.setTextContent( elementValue );
    }
    parentElement.appendChild( element );
    return element;
  }
  
  private void hookInitParameter( final Document document, 
                                  final Element parentElement ) 
  {
    if( initParameter != null ) {
      Set keySet = initParameter.keySet();
      Object[] keys = new Object[ keySet.size() ];
      keySet.toArray( keys );
      for( int i = 0; i < keys.length; i++ ) {
        String key = ( String )keys[ i ];
        String value = ( String )initParameter.get( key );
        internalAddInitParameter( document, parentElement, key, value );
      }
    }
  }
  
  private void hookCommandLineArguments( final Document document, 
                                         final Element parentElement ) 
  {
    if( commandLineArguments != null ) {
      String argumentsString = createCommandLineArgumentsString();
      internalAddInitParameter( document, 
                                parentElement, 
                                "commandline", 
                                argumentsString );
    }
  }

  private void internalAddInitParameter( final Document document,
                                         final Element parentElement,
                                         final String paramName,
                                         final String paramValue )
  {
    Element initParamElement = createElement( document, 
                                              parentElement, 
                                              "init-param", 
                                              null );
    createElement( document, 
                   initParamElement, 
                   "param-name", 
                   paramName );
    createElement( document, 
                   initParamElement, 
                   "param-value", 
                   paramValue );
  }
  
  private String createCommandLineArgumentsString() {
    String result = "";
    for( int i = 0; i < commandLineArguments.size(); i++ ) {
      String hyphen = getCorrectHyphen( i );
      result += hyphen + commandLineArguments.get( i );
    }
    return result;
  }

  private String getCorrectHyphen( final int i ) {
    String hyphen = " -";
    if( i == 0 ) {
      hyphen = "-";
    }
    return hyphen;
  }
  
  private void hookServletMapping( final Document document, 
                                   final Element parentElement) 
  {
    if( urlPatterns != null ) {
      createUrlPatternMappings( document, parentElement );
    } else {
      throw new IllegalStateException( "No url pattern defined" );
    }
  }

  private void createUrlPatternMappings( final Document document,
                                         final Element parentElement )
  {
    for( int i = 0; i < urlPatterns.size(); i++ ) {
      String pattern = ( String )urlPatterns.get( i );
      Element mapping 
        = createElement( document, parentElement, "servlet-mapping", null );
      createElement( document, mapping, "servlet-name", servletName );
      createElement( document, mapping, "url-pattern", pattern );
    }
  }
  
  private void hookCustomXML( final Document document, 
                              final Element parentElement ) 
  {
    if( customXML != null ) {
      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        StringReader reader = new StringReader( customXML );
        InputSource source = new InputSource( reader );
        Document parsedDocument = documentBuilder.parse( source );
        Element documentElement = parsedDocument.getDocumentElement();
        Node newNode = document.adoptNode( documentElement );
        parentElement.appendChild( newNode );
      } catch( final Exception e ) {
        System.err.println( "Could not parse custom XML." );
        e.printStackTrace();
      }
    }
  }

  private String transformXMLToString( final DOMSource domSource )
    throws TransformerFactoryConfigurationError,
           TransformerConfigurationException, 
           TransformerException, 
           IOException
  {    
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    StringWriter writer = new StringWriter();
    StreamResult streamResult = new StreamResult( writer );
    transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
    transformer.setOutputProperty( OutputKeys.STANDALONE, "yes" );
    transformer.transform( domSource, streamResult );
    writer.close();
    String result = writer.toString();
    return result;
  }
  
}
