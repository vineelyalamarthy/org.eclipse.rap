/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core.test;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.eclipse.rap.warproducts.core.WebXMLGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class WebXMLGeneratorTest extends TestCase {
  
  public void testDocumentStructure() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList webAppElement = document.getElementsByTagName( "web-app" );
    assertNotNull( webAppElement );
    Node node = webAppElement.item( 0 );
    NamedNodeMap attributes = node.getAttributes();
    assertEquals( 1, attributes.getLength() );
    Node attribute = attributes.item( 0 );
    assertEquals( "id", attribute.getNodeName() );
    assertEquals( "WebApp", attribute.getTextContent() );
    NodeList nodeList = node.getChildNodes();
    assertEquals( 2, nodeList.getLength() );
    Node servletElement = nodeList.item( 0 );
    attributes = servletElement.getAttributes();
    assertEquals( 1, attributes.getLength() );
    attribute = attributes.item( 0 );
    assertEquals( "id", attribute.getNodeName() );
    assertEquals( "bridge", attribute.getTextContent() );
  }
  
  public void testAddCommandLineArgument() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String argument = "console 9999";
    generator.addCommandLineArgument( argument );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "init-param" );
    Node node = nodeList.item( 0 );
    NodeList children = node.getChildNodes();
    Node paramNameNode = children.item( 0 );
    assertEquals( "param-name", paramNameNode.getNodeName() ); 
    assertEquals( "commandline", paramNameNode.getTextContent() );
    Node paramValueNode = children.item( 1 );
    assertEquals( "param-value", paramValueNode.getNodeName() );
    assertEquals( "-" + argument, paramValueNode.getTextContent() );    
  }
  
  public void testAddCommandLineArgumentWithTwoArguments() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String argument1 = "console 9999";
    String argument2 = "registryMultiLanguage";
    generator.addCommandLineArgument( argument1 );
    generator.addCommandLineArgument( argument2 );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "init-param" );
    Node node = nodeList.item( 0 );
    NodeList children = node.getChildNodes();
    Node paramNameNode = children.item( 0 );
    assertEquals( "param-name", paramNameNode.getNodeName() ); 
    assertEquals( "commandline", paramNameNode.getTextContent() );
    Node paramValueNode = children.item( 1 );
    assertEquals( "param-value", paramValueNode.getNodeName() );
    String expectedValue = "-" + argument1 + " -" + argument2;
    assertEquals( expectedValue, paramValueNode.getTextContent() );    
  }
  
  public void testAddInitParam() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String paramName = "extendedFrameworkExports";
    String paramValue = "true";
    generator.addInitParameter( paramName, paramValue );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "init-param" );
    Node node = nodeList.item( 0 );
    NodeList children = node.getChildNodes();
    Node paramNameNode = children.item( 0 );
    assertEquals( "param-name", paramNameNode.getNodeName() ); 
    assertEquals( paramName, paramNameNode.getTextContent() );
    Node paramValueNode = children.item( 1 );
    assertEquals( "param-value", paramValueNode.getNodeName() );
    assertEquals( paramValue, paramValueNode.getTextContent() );    
  }
  
  public void testAddTwoInitParams() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String paramName = "extendedFrameworkExports";
    String paramValue = "true";
    String paramName2 = "frameworkLauncherClass";
    String paramValue2 = "org.eclipse.equinox.servletbridge.FrameworkLauncher";
    generator.addInitParameter( paramName, paramValue );
    generator.addInitParameter( paramName2, paramValue2 );
    String xml = generator.createWebXML();    
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "init-param" );
    Node node = nodeList.item( 0 );
    NodeList children = node.getChildNodes();
    Node paramNameNode = children.item( 0 );
    assertEquals( "param-name", paramNameNode.getNodeName() ); 
    Node paramValueNode = children.item( 1 );
    assertEquals( "param-value", paramValueNode.getNodeName() );
    if( paramNameNode.getTextContent().equals( paramName ) ) {
      assertEquals( paramValue, paramValueNode.getTextContent() );
    } else {
      assertEquals( paramValue2, paramValueNode.getTextContent() );
    }    
    node = nodeList.item( 1 );
    children = node.getChildNodes();
    paramNameNode = children.item( 0 );
    assertEquals( "param-name", paramNameNode.getNodeName() ); 
    paramValueNode = children.item( 1 );
    assertEquals( "param-value", paramValueNode.getNodeName() );
    if( paramNameNode.getTextContent().equals( paramName ) ) {
      assertEquals( paramValue, paramValueNode.getTextContent() );
    } else {
      assertEquals( paramValue2, paramValueNode.getTextContent() );
    }
  }
  
  public void testSetServletName() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String servletName = "equinoxbridgeservlet";
    generator.setServletName( servletName );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "servlet-name" );
    Node node = nodeList.item( 0 );
    assertEquals( servletName, node.getTextContent() );
  }
  
  public void testSetDisplayName() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String displayName = "Equinox Bridge Servlet";
    generator.setDisplayName( displayName );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "display-name" );
    Node node = nodeList.item( 0 );
    assertEquals( displayName, node.getTextContent() );
  }
  
  public void testSetDescription() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );;
    String description = "Equinox Bridge Servlet";
    generator.setDescription( description );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "description" );
    Node node = nodeList.item( 0 );
    assertEquals( description, node.getTextContent() );
  }
  
  public void testSetServletClass() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String servletClass = "org.eclipse.equinox.servletbridge.BridgeServlet";
    generator.setServletClass( servletClass );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "servlet-class" );
    Node node = nodeList.item( 0 );
    assertEquals( servletClass, node.getTextContent() );
  }
  
  public void testAddURLPattern() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String urlPattern = "/*";
    generator.addURLPattern( urlPattern );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "servlet-mapping" );
    Node mapping = nodeList.item( 0 );
    NodeList children = mapping.getChildNodes();
    Node servletName = children.item( 0 );
    assertEquals( "servlet-name", servletName.getNodeName() );
    assertEquals( "equinoxbridgeservlet", servletName.getTextContent() );
    Node pattern = children.item( 1 );
    assertEquals( "url-pattern", pattern.getNodeName() );
    assertEquals( urlPattern, pattern.getTextContent() );
  }
  
  public void testAddTwoURLPatterns() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String defaultPattern = "/*";
    String jspPattern = "/*.jsp";
    generator.addURLPattern( defaultPattern );
    generator.addURLPattern( jspPattern );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "servlet-mapping" );
    Node mapping = nodeList.item( 0 );
    NodeList children = mapping.getChildNodes();
    Node servletName = children.item( 0 );
    assertEquals( "servlet-name", servletName.getNodeName() );
    assertEquals( "equinoxbridgeservlet", servletName.getTextContent() );
    Node pattern = children.item( 1 );
    assertEquals( "url-pattern", pattern.getNodeName() );
    assertEquals( defaultPattern, pattern.getTextContent() );
    mapping = nodeList.item( 1 );
    children = mapping.getChildNodes();
    servletName = children.item( 0 );
    assertEquals( "servlet-name", servletName.getNodeName() );
    assertEquals( "equinoxbridgeservlet", servletName.getTextContent() );
    pattern = children.item( 1 );
    assertEquals( "url-pattern", pattern.getNodeName() );
    assertEquals( jspPattern, pattern.getTextContent() );
  }
  
  public void testAddCustomXMLToWebApp() 
    throws ParserConfigurationException, SAXException, IOException 
  {
    WebXMLGenerator generator 
      = new WebXMLGenerator( "equinoxbridgeservlet", "/*" );
    String customXml = "<session-config enable-cookies='false' " +
    		           "enable-url-rewriting='true'/>";
    generator.addXMLContentToWebApp( customXml );
    String xml = generator.createWebXML();
    Document document = getDocumentFromXML( xml );
    NodeList nodeList = document.getElementsByTagName( "session-config" );
    Node config = nodeList.item( 0 );
    NamedNodeMap attributes = config.getAttributes();
    assertEquals( 2, attributes.getLength() );
    Node cookies = attributes.item( 0 );
    assertEquals( "enable-cookies", cookies.getNodeName() );
    assertEquals( "false", cookies.getTextContent() );
    Node rewriting = attributes.item( 1 );
    assertEquals( "enable-url-rewriting", rewriting.getNodeName() );
    assertEquals( "true", rewriting.getTextContent() );
  }
  
  

  private Document getDocumentFromXML( final String xml )
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
    DocumentBuilder documentBuilder = factory.newDocumentBuilder();
    // eliminating whitespace
    StringReader reader = new StringReader( xml.replaceAll( "\n", "" ) );
    InputSource source = new InputSource( reader );    
    Document document = documentBuilder.parse( source );
    document.getDocumentElement().normalize();
    return document;
  }
  
}
