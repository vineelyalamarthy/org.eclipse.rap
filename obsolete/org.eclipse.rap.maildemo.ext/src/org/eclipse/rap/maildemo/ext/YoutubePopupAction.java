package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.maildemo.custom.YoutubeShell;
import org.eclipse.ui.IWorkbenchWindow;


public class YoutubePopupAction extends Action {

    private YoutubeShell youtube;
    private final IWorkbenchWindow window; 

    public YoutubePopupAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_YOUTUBE);
    }

    public void run() {
      if( youtube == null ) {
        youtube = new YoutubeShell( window.getShell().getDisplay() );
      }
      youtube.setId( "awZT13bPEBI" );
      youtube.show();
    }
}