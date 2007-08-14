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
package com.w4t.custom;

/** <p>The ICustomContainer is a marker interface for custom components
 *  that are based on WebContainer and should not be able to change the 
 *  {@link org.eclipse.rwt.WebLayout <code>WebLayout</code>} used for positioning 
 *  content components.</p>
 * 
 *  <p>The {@link org.eclipse.rwt.WebContainer#setWebLayout <code>setWebLayout()</code>} 
 *  method of the WebContainer class from which the implementing class is 
 *  extended, allows to set the layout for initialisation purposes once, 
 *  but after this the layout cannot be changed anymore.</p>
 */
public interface ICustomContainer {}
