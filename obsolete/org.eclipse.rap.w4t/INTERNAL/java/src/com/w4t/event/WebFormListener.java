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

import java.util.EventListener;

/** 
 * <p>Classes which implement this interface are provide methods that deal with 
 * the initialization or closing of WebForms.</p>
 * @see org.eclipse.rwt.event.WebFormEvent
 * @see org.eclipse.rwt.event.WebFormAdapter      
 */
public interface WebFormListener extends EventListener {

  /** 
   * <p>Invoked when the <code>WebForm</code> is requested to close.</p> 
   */
  void webFormClosing( WebFormEvent e );
  
  /** 
   * <p>Invoked after the <code>WebForm</code>s initialisations take place 
   * ({@link org.eclipse.rwt.WebForm#setWebComponents() WebForm.setWebComponents()}).
   * </p>
   */
  void afterInit( WebFormEvent e );  

}

 