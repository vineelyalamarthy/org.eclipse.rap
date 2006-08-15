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

import java.util.Vector;


/** <p>Provides a mapping between common color names (like 'red', 'green' etc.)
  * and HTML RGB color hex strings.</p>
  */
public class ColorMapper {

  /** <p>the private singleton instance of ColorMapper.</p> */
  private static ColorMapper _instance;

  /* the internal data structures of this ColorMapper singleton. The mappings
   * are in Vectors, which costs time for looping over them, but enables
   * searching ignoring case, which would not be possible with HashMaps. */
  private Vector netscapeColors;
  private Vector standardColors;

  /** <p>constructs the singleton instance of ColorMapper.</p> */
  private ColorMapper() {
    initStandardColors();
    initNetscapeColors();
  }

  /** <p>returns the correct hex string containing the RGB value for the 
    * passed color name.</p>
    * <p>This ColorMapper knows standard HTML color like 'red', 'black' etc. 
    * as well as the extended Netscape colors like 'darkmagenta', 
    * 'lightpink' etc. Case is ignored. If no color could be
    * found, the passed colorNamed is returned. */
  public static String getHexRGB( final String colorName ) {
    return getInstance().map( colorName );
  }


  // helping methods
  //////////////////

  /** returns a reference to the singleton instance of ColorMapper. */
  private synchronized static ColorMapper getInstance() {
    if( _instance == null ) {
      _instance = new ColorMapper();
    }
    return _instance;
  }

  /** performs the actual mapping. */
  private String map( final String colorName ) {
    String result = colorName;
    if( !isHex( colorName ) ) {
      String stripped = strip( colorName );
      boolean found = false;
      
      // html standard colors
      for( int i = 0; !found && i < standardColors.size(); i++ ) {
        Color color = ( Color )standardColors.get( i );
        if( color.getName().equalsIgnoreCase( stripped ) ) {
          result = color.getHex();
          found = true;
        }
      }
      
      // netscape colors
      for( int i = 0; !found && i < netscapeColors.size(); i++ ) {
        Color color = ( Color )netscapeColors.get( i );
        if( color.getName().equalsIgnoreCase( stripped ) ) {
          result = color.getHex();
          found = true;
        }
      }
    }    
    return result;
  }

  private boolean isHex( final String colorName ) {
    boolean result = true;
    String value = strip( colorName );
    try {
      Integer.parseInt( value, 16 );
    } catch( Exception ex ) {
      result = false;
    }
    return result;
  }

  private String strip( final String colorName ) {
    return ( colorName.startsWith( "#" ) ) ? colorName.substring( 1 ) 
                                           : colorName;
  }

  private void initStandardColors() {
    standardColors = new Vector();
    standardColors.add( new Color( "black", "#000000" ) );
    standardColors.add( new Color( "gray", "#808080" ) );
    standardColors.add( new Color( "maroon", "#800000" ) );
    standardColors.add( new Color( "red", "#FF0000" ) );
    standardColors.add( new Color( "green", "#008000" ) );
    standardColors.add( new Color( "lime", "#00FF00" ) );
    standardColors.add( new Color( "olive", "#808000" ) );
    standardColors.add( new Color( "yellow", "#FFFF00" ) );
    standardColors.add( new Color( "navy", "#000080" ) );
    standardColors.add( new Color( "blue", "#0000FF" ) );
    standardColors.add( new Color( "purple", "#800080" ) );
    standardColors.add( new Color( "fuchsia", "#FF00FF" ) );
    standardColors.add( new Color( "teal", "#008080" ) );
    standardColors.add( new Color( "aqua", "#00FFFF" ) );
    standardColors.add( new Color( "silver", "#C0C0C0" ) );
    standardColors.add( new Color( "white", "#FFFFFF" ) );
  }

  private void initNetscapeColors() {
    netscapeColors = new Vector();
    netscapeColors.add( new Color( "aliceblue", "#F0F8FF" ) );
    netscapeColors.add( new Color( "antiquewhite", "#FAEBD7" ) );
    netscapeColors.add( new Color( "aquamarine", "#7FFFD4" ) );
    netscapeColors.add( new Color( "azure", "#F0FFFF" ) );
    netscapeColors.add( new Color( "beige", "#F5F5DC" ) );
    netscapeColors.add( new Color( "blueviolet", "#8A2BE2" ) );
    netscapeColors.add( new Color( "brown", "#A52A2A" ) );
    netscapeColors.add( new Color( "burlywood", "#DEB887" ) );
    netscapeColors.add( new Color( "cadetblue", "#5F9EA0" ) );
    netscapeColors.add( new Color( "chartreuse", "#7FFF00" ) );
    netscapeColors.add( new Color( "chocolate", "#D2691E" ) );
    netscapeColors.add( new Color( "coral", "#FF7F50" ) );
    netscapeColors.add( new Color( "cornflowerblue", "#6495ED" ) );
    netscapeColors.add( new Color( "cornsilk", "#FFF8DC" ) );
    netscapeColors.add( new Color( "crimson", "#DC143C" ) );
    netscapeColors.add( new Color( "darkblue", "#00008B" ) );
    netscapeColors.add( new Color( "darkcyan", "#008B8B" ) );
    netscapeColors.add( new Color( "darkgoldenrod", "#B8860B" ) );
    netscapeColors.add( new Color( "darkgray", "#A9A9A9" ) );
    netscapeColors.add( new Color( "darkgreen", "#006400" ) );
    netscapeColors.add( new Color( "darkkhaki", "#BDB76B" ) );
    netscapeColors.add( new Color( "darkmagenta", "#8B008B" ) );
    netscapeColors.add( new Color( "darkolivegreen", "#556B2F" ) );
    netscapeColors.add( new Color( "darkorange", "#FF8C00" ) );
    netscapeColors.add( new Color( "darkorchid", "#9932CC" ) );
    netscapeColors.add( new Color( "darkred", "#8B0000" ) );
    netscapeColors.add( new Color( "darksalmon", "#E9967A" ) );
    netscapeColors.add( new Color( "darkseagreen", "#8FBC8F" ) );
    netscapeColors.add( new Color( "darkslateblue", "#483D8B" ) );
    netscapeColors.add( new Color( "darkslategray", "#2F4F4F" ) );
    netscapeColors.add( new Color( "darkturquoise", "#00CED1" ) );
    netscapeColors.add( new Color( "darkviolet", "#9400D3" ) );
    netscapeColors.add( new Color( "deeppink", "#FF1493" ) );
    netscapeColors.add( new Color( "deepskyblue", "#00BFFF" ) );
    netscapeColors.add( new Color( "dimgray", "#696969" ) );
    netscapeColors.add( new Color( "dodgerblue", "#1E90FF" ) );
    netscapeColors.add( new Color( "firebrick", "#B22222" ) );
    netscapeColors.add( new Color( "floralwhite", "#FFFAF0" ) );
    netscapeColors.add( new Color( "forestgreen", "#228B22" ) );
    netscapeColors.add( new Color( "gainsboro", "#DCDCDC" ) );
    netscapeColors.add( new Color( "ghostwhite", "#F8F8FF" ) );
    netscapeColors.add( new Color( "gold", "#FFD700" ) );
    netscapeColors.add( new Color( "goldenrod", "#DAA520" ) );
    netscapeColors.add( new Color( "greenyellow", "#ADFF2F" ) );
    netscapeColors.add( new Color( "honeydew", "#F0FFF0" ) );
    netscapeColors.add( new Color( "hotpink", "#FF69B4" ) );
    netscapeColors.add( new Color( "indianred", "#CD5C5C" ) );
    netscapeColors.add( new Color( "indigo", "#4B0082" ) );
    netscapeColors.add( new Color( "ivory", "#FFFFF0" ) );
    netscapeColors.add( new Color( "khaki", "#F0E68C" ) );
    netscapeColors.add( new Color( "lavender", "#E6E6FA" ) );
    netscapeColors.add( new Color( "lavenderblush", "#FFF0F5" ) );
    netscapeColors.add( new Color( "lawngreen", "#7CFC00" ) );
    netscapeColors.add( new Color( "lightblue", "#ADD8E6" ) );
    netscapeColors.add( new Color( "lightcoral", "#F08080" ) );
    netscapeColors.add( new Color( "lightcyan", "#E0FFFF" ) );
    netscapeColors.add( new Color( "lightgoldenrodyellow", "#FAFAD2" ) );
    netscapeColors.add( new Color( "lightgreen", "#90EE90" ) );
    netscapeColors.add( new Color( "lightgrey", "#D3D3D3" ) );
    netscapeColors.add( new Color( "lightpink", "#FFB6C1" ) );
    netscapeColors.add( new Color( "lightsalmon", "#FFA07A" ) );
    netscapeColors.add( new Color( "lightseagreen", "#20B2AA" ) );
    netscapeColors.add( new Color( "lightslategray", "#778899" ) );
    netscapeColors.add( new Color( "lightsteelblue", "#B0C4DE" ) );
    netscapeColors.add( new Color( "lightyellow", "#FFFFE0" ) );
    netscapeColors.add( new Color( "limegreen", "#32CD32" ) );
    netscapeColors.add( new Color( "linen", "#FAF0E6" ) );
    netscapeColors.add( new Color( "mediumaquamarine", "#66CDAA" ) );
    netscapeColors.add( new Color( "mediumblue", "#0000CD" ) );
    netscapeColors.add( new Color( "mediumorchid", "#BA55D3" ) );
    netscapeColors.add( new Color( "mediumpurple", "#9370DB" ) );
    netscapeColors.add( new Color( "mediumseagreen", "#3CB371" ) );
    netscapeColors.add( new Color( "mediumslateblue", "#7B68EE" ) );
    netscapeColors.add( new Color( "mediumspringgreen", "#00FA9A" ) );
    netscapeColors.add( new Color( "mediumturquoise", "#48D1CC" ) );
    netscapeColors.add( new Color( "mediumvioletred", "#C71585" ) );
    netscapeColors.add( new Color( "midnightblue", "#191970" ) );
    netscapeColors.add( new Color( "mintcream", "#F5FFFA" ) );
    netscapeColors.add( new Color( "moccasin", "#FFE4B5" ) );
    netscapeColors.add( new Color( "navajowhite", "#FFDEAD" ) );
    netscapeColors.add( new Color( "oldlace", "#FDF5E6" ) );
    netscapeColors.add( new Color( "olivedrab", "#6B8E23" ) );
    netscapeColors.add( new Color( "orange", "#FFA500" ) );
    netscapeColors.add( new Color( "orangered", "#FF4500" ) );
    netscapeColors.add( new Color( "orchid", "#DA70D6" ) );
    netscapeColors.add( new Color( "palegoldenrod", "#EEE8AA" ) );
    netscapeColors.add( new Color( "palegreen", "#98FB98" ) );
    netscapeColors.add( new Color( "paleturquoise", "#AFEEEE" ) );
    netscapeColors.add( new Color( "palevioletred", "#DB7093" ) );
    netscapeColors.add( new Color( "papayawhip", "#FFEFD5" ) );
    netscapeColors.add( new Color( "peachpuff", "#FFDAB9" ) );
    netscapeColors.add( new Color( "peru", "#CD853F" ) );
    netscapeColors.add( new Color( "pink", "#FFC0CB" ) );
    netscapeColors.add( new Color( "plum", "#DDA0DD" ) );
    netscapeColors.add( new Color( "powderblue", "#B0E0E6" ) );
    netscapeColors.add( new Color( "rosybrown", "#BC8F8F" ) );
    netscapeColors.add( new Color( "saddlebrown", "#8B4513" ) );
    netscapeColors.add( new Color( "salmon", "#FA8072" ) );
    netscapeColors.add( new Color( "sandybrown", "#F4A460" ) );
    netscapeColors.add( new Color( "seagreen", "#2E8B57" ) );
    netscapeColors.add( new Color( "seashell", "#FFF5EE" ) );
    netscapeColors.add( new Color( "sienna", "#A0522D" ) );
    netscapeColors.add( new Color( "skyblue", "#87CEEB" ) );
    netscapeColors.add( new Color( "slateblue", "#6A5ACD" ) );
    netscapeColors.add( new Color( "slategray", "#708090" ) );
    netscapeColors.add( new Color( "snow", "#FFFAFA" ) );
    netscapeColors.add( new Color( "springgreen", "#00FF7F" ) );
    netscapeColors.add( new Color( "tan", "#D2B48C" ) );
    netscapeColors.add( new Color( "thistle", "#D8BFD8" ) );
    netscapeColors.add( new Color( "tomato", "#FF6347" ) );
    netscapeColors.add( new Color( "turquoise", "#40E0D0" ) );
    netscapeColors.add( new Color( "violet", "#EE82EE" ) );
    netscapeColors.add( new Color( "wheat", "#F5DEB3" ) );
    netscapeColors.add( new Color( "whitesmoke", "#F5F5F5" ) );
    netscapeColors.add( new Color( "yellowgreen", "#9ACD32" ) );    
  }


  // inner classes
  ////////////////
  
  /** <p>a helping class which encapsulates a color name and it's hex 
    * string representation.</p> */
  private class Color {

    private String name;
    private String hex;
    
    Color( final String name, final String hex ) {
      this.name = name;
      this.hex = hex;
    }
   
    String getName() {
      return name;
    }
    
    String getHex() {
      return hex;
    }
  }
}
