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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.ContextProvider;

import com.w4t.dhtml.event.WebTreeNodeCollapsedEvent;
import com.w4t.dhtml.event.WebTreeNodeExpandedEvent;
import com.w4t.event.WebItemEvent;


/** <p>A utility class that contains static helping methods for working 
  * with component names and ids.</p>
  */
public class NoscriptUtil {
  
  private NoscriptUtil() {
  }
  
  /** <p>removes image coordinate suffixes from component names.</p>
    *
    * <p>Form element names may contain additional mouse coordinates as 
    * suffixes (on input type 'image'), we  must remove them to avoid 
    * errors (incorrect component id).</p> */
  public static String stripFromSuffix( final String name ) {
    String result = name;
    int pos = name.indexOf( "." );
    if( pos != -1 ) {
      result = name.substring( 0, pos );
    }
    return result;
  }
  
  /** <p>adds the prefix for the WebItemEvent to a component name.</p> */
  public static String addItemPrefix( final String name ) {
    return WebItemEvent.PREFIX + name;
  }

  /** <p>adds the prefix for the WebTreeNodeExpandedEvent to a 
    * component name.</p> */
  public static String addExpandedPrefix( final String name ) {
    return WebTreeNodeExpandedEvent.PREFIX + name;
  }
  
  /** <p>adds the prefix for the WebTreeNodeCollapsedEvent to a 
    * component name.</p> */
  public static String addCollapsedPrefix( final String name ) {
    return WebTreeNodeCollapsedEvent.PREFIX + name;
  }
  
  /** <p>adds a suffix for the WebItemEvent to a component name (suffixes 
    * appear on posted names when they were posted from an input type 
    * 'image' form element and contain the mouse position relative to the 
    * image.</p> */
  public static String addSuffix( final String name ) {
    return name + ".x";
  }

  public static boolean wasSubmitted( final WebComponent component ) {
    String prefixedName = addItemPrefix( component.getUniqueID() );
    String suffixedName = addSuffix( prefixedName );
    HttpServletRequest request = ContextProvider.getRequest();
    return (    request.getParameter( prefixedName ) != null 
             || request.getParameter( suffixedName ) != null );
  }

  public static boolean isActionSource( final StringBuffer key ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String parameter = request.getParameter( key.toString() );
    if( parameter == null ) {
      parameter = request.getParameter( key.append( ".x" ).toString() );
    }
    return parameter != null;
  }
}