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

import java.text.*;
import java.util.Date;

import org.eclipse.rwt.internal.util.ParamCheck;

/**
 * <p>This exists only for compatibility reasons and may be removed in the
 * future.</ü>
 * @deprecated
 */
public class ConverterUtil {
  
  private ConverterUtil() {
  }

  /**
   * This exists only for compatibility reasons and may be removed in the
   * future.
   * 
   * @deprecated
   */
  public static String parse( final Format formatter, final String value ) {
    ParamCheck.notNull( formatter, "formatter" );
    ParamCheck.notNull( value, "value" );
    String result = "";
    if( !value.equals( "" ) ) {
      ParsePosition pos = new ParsePosition( 0 );
      if( formatter instanceof SimpleDateFormat ) {
        result = formatDate( formatter, value );
      } else {
        Object o = formatter.parseObject( value, pos );
        result = o.toString();
      }
    }
    return result;
  }

  /**
   * This exists only for compatibility reasons and may be removed in the
   * future.
   * 
   * @deprecated
   */
  public static String format( final Format formatter, final String value ) {
    String result = "";
    if( !value.equals( "" ) ) {
      if( formatter instanceof NumberFormat ) {
        result = formatNumber( formatter, value );
      } else if( formatter instanceof SimpleDateFormat ) {
        result = formatDate( formatter, value );
      } else {
        result = formatter.format( value );
      }
    }
    return result;
  }


  //////////////////
  // helping methods
  
  private static String formatNumber( final Format formatter, 
                                      final String value )
  {
    String result = "";
    try {
     int newVal = Integer.parseInt( value );
     result = formatter.format( new Integer( newVal ) );
   } catch( Exception formatInt ) {
     try {
       long newVal = Long.parseLong( value );
       result = formatter.format( new Long( newVal ) );
     } catch( Exception formatLong ) {
       try {
         float newVal = Float.parseFloat( value );
         result = formatter.format( new Float( newVal ) );
       } catch( Exception formatFloat ) {
         double newVal = Double.parseDouble( value );
         result = formatter.format( new Double( newVal ) );
       }
     }
   }
   return result;
  }
  
  private static String formatDate( final Format formatter, 
                                    final String value )
  {
    String result = "";
    SimpleDateFormat dateFormatter = ( SimpleDateFormat )formatter;
    try {
      result = formatShortYearDate( dateFormatter, value );
    } catch( final Exception e ) {
      Date newDate = null;
      try {
        newDate = dateFormatter.parse( value );
      } catch( final ParseException pe ) {
        String msg = "'" + value + "' is not a valid date.";
        throw new IllegalArgumentException( msg );
      }
      result = dateFormatter.format( newDate );
    }
    return result;
  }

  private static String formatShortYearDate( final SimpleDateFormat formatter, 
                                             final String value )
  {
    String result = "";
    String pattern = formatter.toPattern();
    int yearPosition = pattern.indexOf( "yyyy" );
    if( yearPosition != -1 ) {
      pattern =   pattern.substring( 0, yearPosition )
                + pattern.substring( yearPosition + 2 );
      SimpleDateFormat shortDateFormatter = new SimpleDateFormat( pattern );
      Date newDate = null;
      try {
        newDate = shortDateFormatter.parse( value );
      } catch( final ParseException pe ) {
        String msg = "'" + value + "' is not a valid date.";
        throw new IllegalArgumentException( msg );
      }
      result = formatter.format( newDate );
    } else {
      String msg =   "Parameter 'formatter' does not have correct pattern - " 
                   + "must contain 'yyyy'.";
      throw new IllegalArgumentException( msg );
    }
    return result;
  }
}
