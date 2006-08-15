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
package com.w4t.custom;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import com.w4t.W4TContext;


/** 
 * <p>Helper class for reading the xml files of the custom components.</p> 
 */
class InitialisationReader {
  
  /** this contains the DOM of the initialisation file of the web application */
  private Document doc = null;
  
  /**
   * a content node (2. level) of the applications
   * initialisation file e.g. '<initialisation>'
   * ({@link #selectContent selectContent})
   */
  private Node content = null;
  
  /**
   * if more than on node belongs to a specified content name
   * the nodes are stored in this contentList
   */
  private NodeList contentList = null;
  /** this tells how many nodes are in the contentList */
  int contentCount  = -1;
  /** tells which content is actually selected in the contentList */
  int contentCursor = -1;
  
  
  
  /** Constructor, reads the initialisation file of the application
   * and builds the DOM tree
   * @param fileName name of the applications initialisation file
   */
  public InitialisationReader( final String fileName ) throws Exception {
    initialise( fileName );
  }
  
  private void initialise( final String fileName ) throws Exception {
    String errMSG
      = "Exception in the constructor call of 'InitialisationReader()': \n";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    try {
      InputStream is;
      if( new File( fileName ).exists() ) {
        is = new FileInputStream( fileName );        
      } else {
        URL resource = W4TContext.getResourceManager().getResource( fileName );
        if( resource == null ) {
          String txt = "File \"{0}\" does not exist or is not accessible.";
          String msg = MessageFormat.format( txt, new Object[] { fileName } );
          throw new IllegalArgumentException( msg );
        }
        URLConnection con = resource.openConnection();
        con.setUseCaches( false );
        is = con.getInputStream();
      }
      doc = builder.parse( is );
    } catch( IOException io ) {
      throw new Exception(  errMSG
                          + "could not access the file '" 
                          + fileName 
                          + "'!\n"
                          + io.toString() );
    } catch( SAXException sax ) {
      throw new Exception(  errMSG
                          + "could not parse the file '" 
                          + fileName 
                          + "'!\n"
                          + sax.toString() );
    }
  }
  
  /** selects a content node (2. level) of the applications
   * initialisation file e.g. '<initialisation>'. The method
   * returns how many nodes of this kind exists
   * @param contentName name of the content to select
   * @return content count
   * @throws Exception if no content with the specified contentName is found
   */
  public int selectContent( final String contentName ) throws Exception {
    contentList = doc.getElementsByTagName( contentName );
    if( contentList != null ) {
      contentCount = contentList.getLength();
      contentCursor = 1;
      content = contentList.item( 0 );
    } else {
      throw new Exception(   "Error in 'InitialisationReader.selectContent()': "
                           + "the content '" + contentName 
                           + "' was not found!" );
    }
    return contentCount;
  }
  
  /** select the next content of the content list, which was selected with
   * the selectContent method
   * @throws Exception if no more contents in the list
   */
  public void nextContent() throws Exception {
    if( contentCursor > contentCount ) {
      throw new Exception(   "Error in 'InitialisationReader.nextContent()':\n"
                           + "no more contents in the content list!" );
    }
    content = contentList.item( contentCursor );
    contentCursor++;
  }
  
  /** tests if the content list has more elements. */
  boolean hasMoreContents() {
    return contentCursor < contentCount;
  }
  
  /** reads a attribut value (3. level) of the applications
   * initialisation file e.g. '<initialisation>'
   * @param attributeName name of the attribute to read
   * @throws Exception if no attribute with the specified attributeName is found
   */
  public String getAttribute( final String attributeName ) throws Exception {
    String attributeValue = "";
    if( content == null ) {
      throw new Exception( "Error in 'InitialisationReader.getAttribute()':\n"
      + "no content selected" );
    }
    
    NodeList attributeList = content.getChildNodes();
    int attributeListLength = attributeList.getLength();
    boolean found = false;
    for( int i = 0; !found && i < attributeListLength; i++ ) {
      Node node = attributeList.item( i );
      found = node.getNodeName().equals( attributeName );
      if( found ) {
        Element element = ( Element )node;
        attributeValue = ( element.getFirstChild() == null )
        ? ""
        : element.getFirstChild().getNodeValue();
      }
    }
    if( !found ) {
      throw new Exception(   "Error in 'InitialisationReader.getAttribute()':\n"
                           + "the attribute '" 
                           + attributeName 
                           + "' was not found!" );
    }
    return attributeValue.trim();
  }
}
