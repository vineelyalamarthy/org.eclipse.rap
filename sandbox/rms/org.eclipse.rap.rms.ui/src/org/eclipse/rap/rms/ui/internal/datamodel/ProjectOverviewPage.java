// Created on 30.09.2007
package org.eclipse.rap.rms.ui.internal.datamodel;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rap.rms.ui.internal.Activator;
import org.eclipse.rap.rms.ui.internal.RMSMessages;
import org.eclipse.rap.rms.ui.internal.datamodel.PageUtil.Container;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;


public class ProjectOverviewPage extends FormPage {
  private static final String OVERVIEW = "Overview"; //$NON-NLS-1$
  private final ProjectCopy project;
  
  
  public ProjectOverviewPage( final FormEditor editor,
                              final ProjectCopy project )
  {
    super( editor, OVERVIEW, RMSMessages.get().ProjectOverviewPage_Title );
    this.project = project;
    this.project.setDirtyNotificator( new Runnable() {
      public void run() {
        ProjectOverviewPage.this.firePropertyChange( PROP_DIRTY );
      }
    } );
  }

  public void init( final IEditorSite site, final IEditorInput input ) {
    super.init( site, input );
    setTitleToolTip( RMSMessages.get().ProjectOverviewPage_ToolTip );
  }
  
  protected void createFormContent( final IManagedForm managedForm ) {
    ScrolledForm scrolledForm = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    
    Composite body
      = PageUtil.createBody( scrolledForm, Activator.IMG_FORM_HEAD_OVERVIEW );
    DataBindingContext ctx = PageUtil.createBindingContext();
    
    // general info section
    Composite info
      = PageUtil.createGeneralInfoSection( scrolledForm, 
                                           toolkit, 
                                           body, 
                                           project.getName() );
    Container cInfo = new Container( toolkit, info );
    PageUtil.createLabelText( cInfo, RMSMessages.get().ProjectOverviewPage_Name, project.getName(), true );
    Text txtDesc = PageUtil.createLabelMultiText( cInfo, 
                                                  RMSMessages.get().ProjectOverviewPage_Description, 
                                                  project.getDescription() );
    PageUtil.bindText( ctx,project, txtDesc, ProjectCopy.DESCRIPTION );
    IConverter dateToString = PageUtil.DATE_TO_STRING_CONVERTER;
    String startDate = ( String )dateToString.convert( project.getStartDate() );
    Text txtStart = PageUtil.createDatePicker( cInfo, 
                                               RMSMessages.get().ProjectOverviewPage_StartDate, 
                                               startDate );
    PageUtil.bindDate( ctx, project, txtStart, ProjectCopy.START_DATE );
    String endDate = ( String )dateToString.convert( project.getEndDate() );
    Text txtEnd = PageUtil.createDatePicker( cInfo, 
                                             RMSMessages.get().ProjectOverviewPage_EndDate, 
                                             endDate );
    PageUtil.bindDate( ctx, project, txtEnd, ProjectCopy.END_DATE );
  }
  
  @Override
  public void doSave( final IProgressMonitor monitor ) {
    project.save();
  }
  
  @Override
  public boolean isDirty() {
    return project.isDirty();
  }
}
