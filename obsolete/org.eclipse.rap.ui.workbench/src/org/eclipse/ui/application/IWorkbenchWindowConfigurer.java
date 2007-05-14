/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.application;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;

public interface IWorkbenchWindowConfigurer {

  IWorkbenchWindow getWindow();
  IActionBarConfigurer getActionBarConfigurer();
  
  String getTitle();
  void setTitle(String title);

  boolean getShowMenuBar();
  void setShowMenuBar( boolean show );
  
  boolean getShowCoolBar();
  void setShowCoolBar( boolean show );
  
  boolean getShowPerspectiveBar();
  void setShowPerspectiveBar( boolean show );

  Point getInitialSize();
  void setInitialSize( Point initialSize );
  
  int getShellStyle();
  void setShellStyle( int shellStyle );

}
