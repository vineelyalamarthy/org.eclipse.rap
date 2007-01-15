/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.jface.window;

import org.eclipse.rap.jface.action.*;
import org.eclipse.rap.jface.internal.provisional.action.ICoolBarManager2;
import org.eclipse.rap.rwt.widgets.*;

// TODO: [fappel] completion of this basic ApplicationWindow implementation...
public class ApplicationWindow extends Window {

  private MenuManager menuBarManager;
  private IToolBarManager toolBarManager = null;
  private ICoolBarManager coolBarManager = null;

  protected ApplicationWindow( final Shell parentShell ) {
    super( parentShell );
  }

  protected MenuManager createMenuManager() {
    return new MenuManager();
  }

  protected void addMenuBar() {
    if( getShell() == null && menuBarManager == null ) {
      menuBarManager = createMenuManager();
    }
  }

  public MenuManager getMenuBarManager() {
    return menuBarManager;
  }
  
  protected void addCoolBar( final int style ) {
    if(    getShell() == null 
        && toolBarManager == null 
        && coolBarManager == null )
    {
      coolBarManager = createCoolBarManager2( style );
    }
  }

  protected ICoolBarManager createCoolBarManager2( int style ) {
    return createCoolBarManager( style );
  }

  protected CoolBarManager createCoolBarManager( int style ) {
    return new CoolBarManager( style );
  }

  public ICoolBarManager getCoolBarManager2() {
    return coolBarManager;
  }

  protected Control createCoolBarControl( final Composite composite ) {
    if( coolBarManager != null ) {
      if( coolBarManager instanceof ICoolBarManager2 ) {
        ICoolBarManager2 coolBarManager2 = ( ICoolBarManager2 )coolBarManager;
        return coolBarManager2.createControl2( composite );
      }
      if( coolBarManager instanceof CoolBarManager ) {
        return ( ( CoolBarManager )coolBarManager ).createControl( composite );
      }
    }
    return null;
  }


}
