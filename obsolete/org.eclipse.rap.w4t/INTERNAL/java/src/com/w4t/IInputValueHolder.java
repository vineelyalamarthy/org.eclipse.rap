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

import org.eclipse.rwt.Adaptable;

import com.w4t.event.WebDataListener;

/**
 * <p>
 * Classes implementing this interface provide a user-editable value like a
 * {@link org.eclipse.rwt.WebText <code>WebText</code>} in which a user can enter
 * text.
 * </p> 
 */
public interface IInputValueHolder extends IValueHolder, Adaptable {
  
  /** 
   * <p>Adds the specified <code>WebDataListener</code> to observe value
   * changes caused by user input. <code>WebDataEvent</code>s are fired at
   * the end of the read-data-phase of the request lifecycle. </p>
   * 
   * @param listener the WebDataListener implementation.
   */
  void addWebDataListener( WebDataListener listener );

  /** 
   * <p>Removes the specified <code>WebDataListener</code> that observes value
   * changes caused by user input. <code>WebDataEvent</code>s are fired at
   * the end of the read-data-phase of the request lifecycle. </p>
   * 
   * @param listener the WebDataListener implementation.
   */
  void removeWebDataListener( WebDataListener listener );
  
  // TODO: [fappel] comment
  void setUpdatable( boolean updatable );
  // TODO: [fappel] comment
  boolean isUpdatable();
  
}
