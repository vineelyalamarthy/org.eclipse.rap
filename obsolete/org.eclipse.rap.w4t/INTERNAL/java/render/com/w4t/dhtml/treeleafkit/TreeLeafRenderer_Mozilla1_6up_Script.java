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
package com.w4t.dhtml.treeleafkit;

/**
 * <p>
 * The renderer for org.eclipse.rap.dhtml.TreeLeaf on Mozilla 1.6 and later.
 * </p>
 */
public class TreeLeafRenderer_Mozilla1_6up_Script
  extends TreeLeafRenderer_DOM_Script
{

  String getVerticalAlign() {
    return "top";
  }
}