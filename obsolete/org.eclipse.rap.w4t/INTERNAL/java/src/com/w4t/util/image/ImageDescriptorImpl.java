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
package com.w4t.util.image;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import com.w4t.types.WebColor;


//TODO [rh] JavaDoc necessary?
public class ImageDescriptorImpl implements ImageCreator, ImageDescriptor {

  private static final float FONT_SIZE_FACTOR = 1.4f;
  private static final String DEFAULT_FONT_FAMILY = "Arial";
  
  private static final String EXTENSION_GIF = ".gif";
  
  private static final String ITALIC   = "italic";
  private static final String OBLIQUE  = "oblique";
  private static final String BOLD     = "bold";
  private static final String BOLDER   = "bolder";  
  
  private static Hashtable htFontFamilies;
  
  
  /** <p>the text which is rendered to the image (label of the 
    * component).</p> */
  private final String label;
  /** <p>the color of the text in the image.</p> */
  private final WebColor color;
  /** <p>the background color of the image.</p> */
  private final WebColor bgColor;
  
  private final String fontFamily;
  private final int fontSize;
  private final String fontWeight;
  private final String fontStyle;
  

  /** <p>constructs a new ImageDescriptor.</p> */
  ImageDescriptorImpl( final String label, 
                       final WebColor color, 
                       final WebColor bgColor, 
                       final String fontFamily, 
                       final int fontSize, 
                       final String fontWeight, 
                       final String fontStyle ) 
  {
    this.label = label;
    if( color.toString().equals( "" ) ) {
      this.color = new WebColor( "#000000" );
    } else {
      this.color = color;
    }
    this.bgColor = bgColor;
    this.fontFamily = fontFamily;
    this.fontSize = ( int )( fontSize * FONT_SIZE_FACTOR );
    this.fontWeight = fontWeight;
    this.fontStyle = fontStyle;
  }

  public String getLabel() {
    return label;
  }
  
  public WebColor getColor() {
    return color;
  }
  
  public WebColor getBgColor() {
    return bgColor;
  }
  
  public String getFontFamily() {
    return fontFamily;
  }
  
  public int getFontSize() {
    return fontSize;
  }
  
  public String getFontWeight() {
    return fontWeight;
  }
  
  public String getFontStyle() {
    return fontStyle;
  }  
  
  private static Color createColor( final WebColor webColor ) {
    String content = webColor.toString();
    Color result = null;    
    if( content.startsWith( "#" ) ) {
      try {
        result = new Color( Integer.parseInt( content.substring( 1 ), 16 ) );
      } catch( Exception shouldNotHappen ) {
        System.out.println( "\nException parsing WebColor content '" + content
                          + "':\n" + shouldNotHappen.toString() );
      }
    }    
    return result;
  }

  private String determineFamily( final String fontFamily ) {
    fillInFamilies();
    String result = DEFAULT_FONT_FAMILY;
    StringTokenizer tokenizer = new StringTokenizer( fontFamily, ",", false );
    while( tokenizer.hasMoreElements() ) {
      String toCheck = strip( tokenizer.nextToken().toLowerCase().trim() );
      if( htFontFamilies.contains( toCheck ) ) {
        result = ( String )htFontFamilies.get( toCheck );
      }
    }
    return result;
  }

  private String strip( final String toStrip ) {
    String result = toStrip;
    if( toStrip.startsWith( "'" ) ) {
      result = toStrip.substring( 1, toStrip.length() - 1 );
    }
    return result;
  }
  
  private void fillInFamilies() {
    if( htFontFamilies == null ) {
      htFontFamilies = new Hashtable();
      GraphicsEnvironment ge 
        = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] names = ge.getAvailableFontFamilyNames();
      for( int i = 0; i < names.length; i++ ) {
        htFontFamilies.put( names[ i ].toLowerCase(), names[ i ] );
      }
    }    
  }
  
  private int determineStyle() {
    int result;
    if( isItalic() ) {
      result = isBold() ? Font.BOLD | Font.ITALIC : Font.ITALIC;
    } else {
      result = isBold() ? Font.BOLD : Font.PLAIN;
    }
    return result;
  }
  
  private boolean isItalic() {
    return     fontStyle.equalsIgnoreCase( ITALIC ) 
            || fontStyle.equalsIgnoreCase( OBLIQUE );
  }

  private boolean isBold() {
    return    fontWeight.equalsIgnoreCase( BOLD ) 
           || fontWeight.equalsIgnoreCase( BOLDER );
  }
  
  public BufferedImage createBufferedImage() {
    Font font = new Font( determineFamily( fontFamily ), 
                          determineStyle(), 
                          fontSize );
    BufferedImage temp = new BufferedImage( 100, 
                                            100, 
                                            BufferedImage.TYPE_INT_ARGB );
    FontRenderContext frc = temp.createGraphics().getFontRenderContext();
    Rectangle2D rect = font.getStringBounds( label, frc );
    int imageHeight = ( int )rect.getHeight(); 
    BufferedImage image = new BufferedImage( ( int )rect.getWidth() + 2, 
                                             imageHeight, 
                                             BufferedImage.TYPE_INT_ARGB );
    Graphics2D graphics = image.createGraphics();
    graphics.setColor( createColor( determineBgColor() ) );
    graphics.fillRect( 0, 0, image.getWidth(), image.getHeight() );
    graphics.setColor( createColor( color ) );
    graphics.setFont( font );
    graphics.drawString( label, 1, ( int )( rect.getHeight() * 0.8 ) );
    graphics.dispose();
    return image;
  }
  
  public String getImageName() {
   String colorVal = color.toString();
   if( colorVal.startsWith( "#" ) ) {
     colorVal = colorVal.substring( 1 );
   }
   String bgColorVal = bgColor.toString();
   if( bgColorVal.startsWith( "#" ) ) {
     bgColorVal = bgColorVal.substring( 1 );
   }
   StringBuffer buffer = new StringBuffer();
   buffer.append( label );
   buffer.append( "_" ); 
   buffer.append( colorVal ); 
   buffer.append(  "_" ); 
   buffer.append( bgColorVal ); 
   buffer.append(  "_" ); 
   buffer.append(  fontFamily ); 
   buffer.append( "_" ); 
   buffer.append(  String.valueOf( fontSize ) ); 
   buffer.append( "_"  );
   buffer.append(  fontStyle ); 
   buffer.append( "_" ); 
   buffer.append(  fontWeight );
   return ImageDescriptorImpl.encrypt( buffer.toString() ) + EXTENSION_GIF;
  }
  
  public Color getTransparentColor() {
    Color result = null;
    if( bgColor.toString().equals( "" ) ) {
      result = createColor( determineBgColor() );
    }
    return result;
  }

  private WebColor determineBgColor() {
    WebColor result = bgColor;
    if( bgColor.toString().equals( "" ) ) {
      result = new WebColor( "#ffffff" );
      if( color.toString().equalsIgnoreCase( "#ffffff" ) ) {
         result = new WebColor( "#000000" );
      }
    }
    return result;
  }
  
  /** <p>encrypts the passed String using the MD5 alg.</p> */
  private static String encrypt( final String in ) {
    byte[] buf = in.getBytes();
    MessageDigest algorithm = null;
    try {
     algorithm = MessageDigest.getInstance( "MD5" );
    } catch( NoSuchAlgorithmException e ) {
      String msg = "Error in 'Crypto.encrypt( String )' "
                 + "while instantiating algorithm.";
      throw new IllegalStateException( msg );
    }
    algorithm.reset();
    algorithm.update( buf );
    byte[] digest = algorithm.digest();
    String dummy;
    StringBuffer out = new StringBuffer();
    for( int i = 0; i < digest.length; i++ ) {
      dummy = String.valueOf( Integer.toHexString( digest[ i ] ) );
      if( dummy.length() == 0 ) {
        out.append( "00" );
      } else if( dummy.length() == 1 ) {
        out.append( "0" );
        out.append( dummy );  
      } else if( dummy.length() > 2 ) {
        out.append( dummy.substring( 6, dummy.length() ) );
      }
    }
    return out.toString();
  }
}