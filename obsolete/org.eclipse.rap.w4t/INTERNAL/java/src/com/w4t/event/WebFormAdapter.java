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
package com.w4t.event;

/**
 * <p>This adapter class provides default implementations for the methods 
 * described by the <code>WebFormListener</code> interface.</p>
 * <p>Classes that wish to deal with <code>WebFormEvent</code>s can extend 
 * this class and override only the methods which they are interested in.</p> 
 * @see org.eclipse.rwt.event.WebFormListener
 * @see org.eclipse.rwt.event.WebFormEvent
 */
public class WebFormAdapter implements WebFormListener {
    
  /**
   * <p>Invoked when the browser window will be closed or a external link
   * is opened in the browser. The default behavior is to do nothing.</p> 
   */
  public void webFormClosing( final WebFormEvent e ) {
  }

  /**
   * <p>Invoked after the <code>WebForm</code>s initialisations took place.
   * The default behavior is to do nothing.</p>
   */
  public void afterInit( final WebFormEvent e ) {
  }
}
