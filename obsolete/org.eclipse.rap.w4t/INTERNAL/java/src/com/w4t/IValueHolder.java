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

import java.text.Format;

/**
 * <p>
 * Classes implementing this interface represent a user-readable text, like
 * {@link org.eclipse.rwt.WebLabel <code>WebLabel</code>}. 
 * </p> 
 * @see org.eclipse.rwt.IInputValueHolder
 */
public interface IValueHolder {
  
  /**
   * <p>Sets the text that should be displayed.</p>
   * @param value the new value. A <code>null</code> argument will be considered
   * as an empty string ("").  
   */
  void setValue( final String value );
  
  /**
   * <p>
   * Returns the value.
   * </p>
   */
  String getValue();

  /**
   * @deprecated
   */
  // TODO [rh] JavaDoc tell why deprecated 
  void setFormatter( Format formatter );
  
  /**
   * @deprecated
   */
  // TODO [rh] JavaDoc tell why deprecated 
  Format getFormatter();
  
}
