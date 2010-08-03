/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.ILauncherFormPageHelper;
import org.eclipse.pde.internal.ui.editor.LaunchShortcutOverviewPage;
import org.eclipse.pde.internal.ui.editor.PDELauncherFormEditor;
import org.eclipse.pde.internal.ui.editor.product.ProductLauncherFormPageHelper;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.rap.warproducts.ui.WARProductConstants;
import org.eclipse.rap.warproducts.ui.validation.IValidationListener;
import org.eclipse.rap.warproducts.ui.validation.WARProductValidateAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class OverviewPage extends LaunchShortcutOverviewPage {

  public static final String PAGE_ID = "overview"; //$NON-NLS-1$
  private ProductLauncherFormPageHelper launcherHelper;

  public OverviewPage( final PDELauncherFormEditor editor ) {
    super( editor, PAGE_ID, PDEUIMessages.OverviewPage_title );
  }

  protected String getHelpResource() {
    return WARProductConstants.HELP_CONTEXT_OVERVIEW_PAGE;
  }

  protected void createFormContent( final IManagedForm managedForm ) {
    super.createFormContent( managedForm );
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    form.setText( PDEUIMessages.OverviewPage_title );
    PDELabelProvider labelProvider = PDEPlugin.getDefault().getLabelProvider();
    ImageDescriptor description = PDEPluginImages.DESC_PRODUCT_DEFINITION;
    form.setImage( labelProvider.get( description ) );
    fillBody( managedForm, toolkit );
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    String contextId = WARProductConstants.HELP_CONTEXT_OVERVIEW_PAGE;
    helpSystem.setHelp( form.getBody(), contextId );
  }

  private void fillBody( final IManagedForm managedForm, 
                         final FormToolkit toolkit ) 
  {
    Composite body = managedForm.getForm().getBody();
    body.setLayout( FormLayoutFactory.createFormTableWrapLayout( true, 2 ) );
    GeneralInfoSection generalSection = new GeneralInfoSection( this, body );
    managedForm.addPart( generalSection );
    if( getModel().isEditable() ) {
      createExportingSection( body, toolkit );
    }
  }

  private void createExportingSection( final Composite parent, 
                                       final FormToolkit toolkit ) 
  {
    String title = PDEUIMessages.OverviewPage_exportingTitle;
    Section section = createStaticSection( toolkit, parent, title );
    String text = "<form>" +
                  "<li style=\"text\" value=\"1.\" bindent=\"5\">Configure " +
                  "the WAR product on the <a href=\"action.configuration\">" +
                  "Configuration</a> page.</li>" +
                  "<li style=\"text\" value=\"2.\" bindent=\"5\">" +
                  "<a href=\"action.validate\">Validate</a> the WAR product " +
                  "and fix possible errors.</li>" +
    		      "<li style=\"text\" value=\"3.\" bindent=\"5\">Use the " +
    		      "<a href=\"action.export\">Eclipse " +
    		      "WAR Product export wizard</a> to package and export the " +
    		      "WAR product defined in this configuration.</li></form>";
    section.setClient( createClient( section, text, toolkit ) );
  }

  public void linkActivated( final HyperlinkEvent linkEvent ) {
    String href = ( String )linkEvent.getHref();
    if( href.equals( "action.export" ) ) { //$NON-NLS-1$
      if( getPDEEditor().isDirty() ) {
        getPDEEditor().doSave( null );
      }
      new WARProductExportAction( getPDEEditor() ).run();
    } else if( href.equals( "action.configuration" ) ) { //$NON-NLS-1$
      String pageId = ConfigurationPage.PLUGIN_ID;
      getEditor().setActivePage( pageId );
    } else if( href.equals( "action.validate" ) ) { //$NON-NLS-1$
      IProductModel model= ( IProductModel )getPDEEditor().getAggregateModel();
      WARProductValidateAction validationAction 
        = new WARProductValidateAction( ( IWARProduct )model.getProduct() );
      IValidationListener listener = ( IValidationListener )getPDEEditor();
      validationAction.addValidationListener( listener );
      validationAction.run();
    } else {
      super.linkActivated( linkEvent );
    }
  }

  protected ILauncherFormPageHelper getLauncherHelper() {
    if( launcherHelper == null ) {
      launcherHelper 
        = new ProductLauncherFormPageHelper( getPDELauncherEditor() );
    }
    return launcherHelper;
  }

  protected short getIndent() {
    return 35;
  }
  
}
