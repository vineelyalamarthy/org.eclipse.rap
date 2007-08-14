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

import org.eclipse.rwt.internal.browser.*;


/**
 * <p>Helping structure used by browser specific Renderer loading. This class
 * enables easily to determine the fully qualified name of a renderer class,
 * depending on the browser type and component class for which the renderer is
 * used.</p>
 */
class RendererDescriptor {

  private String className;
  private Browser browser;
  private Browser originalBrowser;

  /**
   * <p>
   * creates a new RendererDescriptor for the specified Class and Browser
   * instance.
   * </p>
   */
  RendererDescriptor( final Class clazz, final Browser browser ) {
    this.className = clazz.getName();
    this.browser = browser;
    this.originalBrowser = browser;
  }

  /**
   * <p>
   * returns whether the browser that is used by this RendererDescriptor is the
   * default browser object.
   * </p>
   */
  boolean isDefaultBrowser() {
    return browser.toString().equals( Browser.DEFAULT );
  }

  /**
   * <p>
   * causes this RendererDescriptor to use the predecessor of the currently set
   * browser instead of the latter.
   * </p>
   * <p>
   * There is an exception to the 'normal' browser hierary for AJaX-enabled
   * browsers: the predecessor of the default AJaX browser is the script-
   * enabled original browser.
   * </p>
   * 
   * @return <code>true</code> if a predecessor could be determined or
   *         <code>false</code> if the currently set browser does not have a
   *         predecessor.
   */
  boolean useBrowserPredecessor() {
    Browser currentBrowser = browser;
    if( !isDefaultBrowser() ) {
      String newName = "";
      if( browser.toString().indexOf( "up" ) != -1 ) {
        newName = browser.getClass().getSuperclass().getName();
        // only leafes in the class hierarchy of browsers have no "up"
        if( !newName.endsWith( "up" ) ) {
          newName = Default.class.getName();
        }
      } else {
        newName = browser.getClass().getName() + "up";
      }
      browser = BrowserLoader.loadClassForName( newName, browser );
    } else if( browser.isAjaxEnabled() ) {
      String browserClassName = originalBrowser.getClass().getName();
      browser 
        = BrowserLoader.loadClassForName( browserClassName, true, false );
      originalBrowser = browser;
    }
    return currentBrowser != browser;
  }

  /**
   * returns the fully qualified className of the Renderer class used with the
   * specified Browser type
   */
  public String toString() {
    return getRendererPackage()
           + getRendererName()
           + getBrowserSuffix()
           + getScriptSuffix();
  }

  // ////////////////
  // helping methods
  private String getRendererPackage() {
    String packageName = getPackageName();
    String shortClassName = getShortClassName();
    return packageName + shortClassName.toLowerCase() + "kit.";
  }

  private String getPackageName() {
    int index = className.lastIndexOf( '.' ) + 1;
    return className.substring( 0, index );
  }

  private String getShortClassName() {
    int index = className.lastIndexOf( '.' ) + 1;
    return className.substring( index );
  }

  private String getRendererName() {
    return getShortClassName() + "Renderer";
  }

  private String getBrowserSuffix() {
    return "_" + browser.toString();
  }

  private String getScriptSuffix() {
    String result;
    if( browser.isAjaxEnabled() ) {
      result = "_Ajax";
    } else {
      result = browser.isScriptEnabled()
             ? "_Script"
             : "_Noscript";
    }
    return result;
  }
}