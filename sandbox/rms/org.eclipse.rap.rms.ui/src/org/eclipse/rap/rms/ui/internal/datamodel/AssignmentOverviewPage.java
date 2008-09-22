// Created on 30.09.2007
package org.eclipse.rap.rms.ui.internal.datamodel;

import org.eclipse.rap.rms.ui.internal.Activator;
import org.eclipse.rap.rms.ui.internal.RMSMessages;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;


public class AssignmentOverviewPage extends FormPage {
  
  private static final String OVERVIEW = "Overview"; //$NON-NLS-1$

  public AssignmentOverviewPage( final FormEditor editor ) {
    super( editor, OVERVIEW, RMSMessages.get().AssignmentOverviewPage_Title );
  }

  public void init( final IEditorSite site, final IEditorInput input ) {
    super.init( site, input );
    setTitleToolTip( RMSMessages.get().AssignmentOverviewPage_ToolTip );
  }
  
  protected void createFormContent( final IManagedForm managedForm ) {
    ScrolledForm form = managedForm.getForm();
//    FormToolkit toolkit = managedForm.getToolkit();
    
//    Composite body
//      = PageUtil.createBody( form, Activator.IMG_FORM_HEAD_OVERVIEW );
    PageUtil.createBody( form, Activator.IMG_FORM_HEAD_OVERVIEW );
  }
}
