/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rwt.internal.theme.css;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class CssFileWriter {

  private BufferedWriter out;

  public CssFileWriter() {
  }

  public void write( StyleSheet styleSheet, String filename ) {
    ClassLoader classLoader = CssFileWriter.class.getClassLoader();
    URL url = classLoader.getResource( filename );
    try {
      File file = new File( new URI( url.toString() ) );
      out = new BufferedWriter( new FileWriter( file ) );
      writeRules( styleSheet );
      out.flush();
      out.close();
    } catch( URISyntaxException e ) {
      e.printStackTrace();
    } catch( IOException e ) {
      e.printStackTrace();
    }
  }

  public String getAsString( StyleSheet styleSheet ) {
    String result;
    if( styleSheet == null ) {
      result = "";
    } else {
      StringWriter stringWriter = new StringWriter();
      try {
        out = new BufferedWriter( stringWriter );
        writeRules( styleSheet );
        out.flush();
        out.close();
      } catch( IOException e ) {
        e.printStackTrace();
      }
      result = stringWriter.toString();
    }
    return result;
  }

  private void writeRules( StyleSheet styleSheet ) throws IOException {
    // write header comment first
    String headerComment = styleSheet.getHeaderComment();
    if( headerComment != null ) {
      out.write( "/*" + headerComment + "*/" );
      out.newLine();
    }
    // write each rule with comments
    StyleRule[] rules = styleSheet.getStyleRules();
    for( int i = 0; i < rules.length; i++ ) {
      StyleRule rule = rules[ i ];
      List comments = rule.getComments();
      for( int j = 0; j < comments.size(); j++ ) {
        String comment = ( String )comments.get( j );
        out.write( "/*" + comment + "*/" );
        out.newLine();
      }
      StylePropertyMap properties = rule.getProperties();
      out.write( rule.getSelectorText() );
      out.write( " {" );
      out.newLine();
      String[] keys = properties.getKeys();
      for( int j = 0; j < keys.length; j++ ) {
        String cssProperty = keys[ j ];
        
        String property = properties.getPropertyString( cssProperty );
        if ( property != null ) {
          out.write( "  " + cssProperty + ": " + property + ";" );
          out.newLine();
        }
        
        /*
        LexicalUnit property = properties.getProperty( cssProperty );
        if( property != null ) {
          out.write( getPropertyAsString( cssProperty ) );
          IPropertyWrapper wrapper = PropertyWrapperFactory.createPropertyWrapper( cssProperty,
                                                                                   property,
                                                                                   rule );
          if( wrapper != null ) {
            out.write( wrapper.getDefaultString() );
          } else {
            // handle unsupported properties
            if( property.getLexicalUnitType() == LexicalUnit.SAC_IDENT ) {
              out.write( property.getStringValue() );
            } else {
              out.write( "0" );
            }
          }
          out.write( ";" );
          out.newLine();
        }
        */
        
        
      }
      out.write( "}" );
      out.newLine();
      out.newLine();
    }
  }
}
