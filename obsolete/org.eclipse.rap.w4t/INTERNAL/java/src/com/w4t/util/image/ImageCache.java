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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;

// TODO [rh] JavaDoc necessary?
public class ImageCache {
  
  /** <p>download path of the default image used for event triggering in
    * noscript modus</p> */
  public final static String STANDARD_SUBMITTER_IMAGE
    = "resources/images/submitter.gif";
  public final static String NOSCRIPT_SUBMITTERS_NONE = "none";
  public final static String NOSCRIPT_SUBMITTERS_CREATE = "create";
  public final static String NOSCRIPT_SUBMITTERS_USE = "use";

  /** <p>whether a graphics system is available for this web application.</p> */
  private static boolean isXAvailable = true;
  /** <p>the singleton instance of ImageCache. </p> */
  private static ImageCache _instance;
  /** <p>the internal data structure for the ImageCache.</p> */
  private HashSet cache;
  /** <p> webapplication home directory <p>*/
  private String webAppRoot;
  /** <p>whether special submitter images are created for 
    * browsers that have javascript disabled.</p> */
  private String noscriptSubmitters;
    
  /** <p>constructs a new ImageCache. Private in order to ensure the 
    * singleton pattern.</p> */
  private ImageCache( final String webAppRoot, 
                      final String noscriptSubmitters ) {
    cache = new HashSet();
    this.webAppRoot = webAppRoot;
    determineNoscriptSubmitters( noscriptSubmitters );
  }
  
  private void determineNoscriptSubmitters( final String noscriptSubmitters ) {
    String nsString = noscriptSubmitters.toLowerCase();
    if(    nsString.equals( NOSCRIPT_SUBMITTERS_USE )
        || nsString.equals( NOSCRIPT_SUBMITTERS_CREATE ) )
    {
      this.noscriptSubmitters = nsString;
    } else {
      this.noscriptSubmitters = NOSCRIPT_SUBMITTERS_NONE;
    }    
  }
  
  /** <p>returns the singleton instance of ImageCache.</p> */
  public static synchronized void createInstance( final String webAppRoot, 
                                                  final String noscriptSubm ) {
    if( _instance == null ) {
      _instance = new ImageCache( webAppRoot, noscriptSubm );
    }
    createGeneratedImageDir( webAppRoot );
  }
  
  private static void createGeneratedImageDir( final String webAppRoot ) {
    File generatedImageDir = new File( getGeneratedDirName( webAppRoot ) );
    if( !generatedImageDir.exists() ) {
      generatedImageDir.mkdirs();
    }
    if( !generatedImageDir.exists() ) {
      System.out.println(   "Could not create the directory for generated "
                          + "images.\nShould be " + generatedImageDir.toString()
                          + ".\n" );
    }
  }

  private static String getGeneratedDirName( final String webAppRoot ) {
    return   webAppRoot
           + File.separator
           + "resources"
           + File.separator 
           + "images" 
           + File.separator  
           + "generated";
  }
  
  /** <p> returns the singleton instance of ImageCache </p>*/
  public static ImageCache getInstance() {
    return _instance;
  }  
  
  public String getImageName( final ImageDescriptor descriptor ) {
    String result = STANDARD_SUBMITTER_IMAGE;
    if( useNoscriptSubmitters() ) {
      ImageCreator creator = ( ImageCreator )descriptor;
      String imageName = creator.getImageName();      
      String fileName = createFileName( imageName );
      File file = new File( fileName );
      if(   !cache.contains( imageName ) 
         && createNoscriptSubmitters()
         && isXAvailable ) {
        BufferedImage image = null;
        try {
          image = creator.createBufferedImage();
        } catch( Error err ) {
          err.printStackTrace();
          isXAvailable = false;
        }
        if( !file.exists() && isXAvailable ) {
          saveGIF( fileName, image, creator.getTransparentColor() );
        }
        cache.add( imageName );
      }
      if( file.exists() ) {
        result = createDownloadName( imageName );
      }
    }
    return result;
  }
  
  public String getNoscriptSubmitters() {
    return noscriptSubmitters;
  }
  
  private String createFileName( final String imageName ) {
    return getGeneratedDirName( webAppRoot ) + File.separator + imageName;
  }
  
  private String createDownloadName( final String imageName ) {
    return "resources/images/generated/" + imageName;
  }
  
  private void saveGIF( final String fileName, 
                        final BufferedImage bufferedImage,
                        final Color transparentColor ) 
  {
    try {
      FileOutputStream fos = new FileOutputStream( fileName );
      try {
        GifEncoder encoder;
        if( transparentColor == null ) {
          encoder = new GifEncoder( bufferedImage, fos );        
        } else {
          encoder = new GifEncoder( bufferedImage, 
                                    fos, 
                                    false, 
                                    transparentColor );
        }
        encoder.encode();
      } finally {
        fos.close();
      }
    } catch( IOException ioex ) {
      System.out.println( "Failed to write GIF file: " + ioex.getMessage() );
    }
  }
  
  private boolean useNoscriptSubmitters() {
    return noscriptSubmitters.equals( NOSCRIPT_SUBMITTERS_USE )
        || noscriptSubmitters.equals( NOSCRIPT_SUBMITTERS_CREATE );
  }
  
  private boolean createNoscriptSubmitters() {
    return noscriptSubmitters.equals( NOSCRIPT_SUBMITTERS_CREATE );
  }
 
  public boolean isStandardSubmitterImage( final String imageName ) {
    return imageName.equals( STANDARD_SUBMITTER_IMAGE );
  }
}