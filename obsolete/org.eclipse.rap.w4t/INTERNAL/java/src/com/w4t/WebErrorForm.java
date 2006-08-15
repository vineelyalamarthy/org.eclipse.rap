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


/** <p>This Interface has to be implemented by a WebForm which is used
  * as an error form in a W4 Toolkit application.</p>
  *
  * <p>Applications may implement WebErrorForm in order to display error
  * messages to the user in a custom way. A WebForm that implements 
  * WebErrorForm can be named in the w4t configuration file W4T.xml in
  * the WEB-INF/conf/ directory of the web application. When an exception 
  * occurs within the w4t system, it will be passed to the {@link 
  * #setException(Exception) setException(Exception)} method of this 
  * WebErrorForm and it is displayed to the the user.</p>
  */
public interface WebErrorForm {

  /** <p>Called by the system, if a runtime exception occurs.</p>
    * 
    * @param e  the Exception that occured within the w4t system. 
    *           Implementations of WebErrorForm are supposed to use it
    *           to display an error message etc. */
  void setException( Exception e );

}

