/*******************************************************************************
 * Copyright (c) 2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.maildemo.custom.YoutubeShell;
import org.eclipse.ui.IWorkbenchWindow;


public class YoutubePopupAction extends Action {

  private YoutubeShell youtubeShell;
  private final IWorkbenchWindow window;

  public YoutubePopupAction( String text, IWorkbenchWindow window ) {
    super( text );
    this.window = window;
    // The id is used to refer to the action in a menu or toolbar
    setId( ICommandIds.CMD_OPEN_YOUTUBE );
  }

  public void run() {
    if( youtubeShell == null ) {
      youtubeShell = new YoutubeShell( window.getShell().getDisplay() );
    }
    youtubeShell.setId( "awZT13bPEBI" );
    youtubeShell.show();
  }
}