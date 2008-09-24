package org.eclipse.rap.maildemo.ext;

import org.eclipse.jface.action.IAction;
import org.eclipse.rap.maildemo.MessagePopupAction;
import org.eclipse.rap.maildemo.OpenViewAction;
import org.eclipse.rap.maildemo.View;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {    

    private IAction openViewAction;
    private IAction messagePopupAction;
    private IAction youtubePopupAction;


    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    public IAction[] getActions(){
      return new IAction[]{ openViewAction, messagePopupAction, youtubePopupAction };
    }
    
    
    protected void makeActions(final IWorkbenchWindow window) {
     
      openViewAction = new OpenViewAction(window, "Open Another Message View", View.ID);
     
      messagePopupAction = new MessagePopupAction("Open Message", window);
      
      youtubePopupAction = new YoutubePopupAction("Open Youtube video", window);

  }

}
