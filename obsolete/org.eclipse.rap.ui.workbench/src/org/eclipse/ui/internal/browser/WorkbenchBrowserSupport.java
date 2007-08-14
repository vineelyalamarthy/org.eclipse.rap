/*******************************************************************************
 * Copyright (c) 2007 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.browser;

import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


public class WorkbenchBrowserSupport extends SessionSingletonBase {

	/*
	 * (intentionally non-JavaDoc'ed) Returns the default implementation since
	 * the browserSupport extension point is not implemented (yet).
	 */
	public static IWorkbenchBrowserSupport getInstance() {
		Object result = getInstance(DefaultWorkbenchBrowserSupport.class);
		return (IWorkbenchBrowserSupport) result;
	}

	private WorkbenchBrowserSupport() {
		// prevent instantiation
	}
}
