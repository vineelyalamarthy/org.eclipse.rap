/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.editor.product.PluginSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class ConfigurationPage extends PDEFormPage {

  public static final String PLUGIN_ID = "plugin-dependencies"; //$NON-NLS-1$
  public static final String FEATURE_ID = "feature-dependencies"; //$NON-NLS-1$
  private PluginSection pluginSection = null;

  public ConfigurationPage( final FormEditor editor, final boolean useFeatures )
  {
    super( editor, PLUGIN_ID, "Configuration" );
  }

  protected String getHelpResource() {
    return IHelpContextIds.CONFIGURATION_PAGE;
  }

  protected void createFormContent( final IManagedForm managedForm ) {
    super.createFormContent( managedForm );
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    PDELabelProvider labelProvider = PDEPlugin.getDefault().getLabelProvider();
    form.setImage( labelProvider.get( PDEPluginImages.DESC_REQ_PLUGINS_OBJ ) );
    form.setText( "Configuration" );
    fillBody( managedForm, toolkit );
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    helpSystem.setHelp( form.getBody(), IHelpContextIds.CONFIGURATION_PAGE );
  }

  private void fillBody( final IManagedForm managedForm, 
                         final FormToolkit toolkit ) 
  {
    Composite body = managedForm.getForm().getBody();
    body.setLayout( FormLayoutFactory.createFormGridLayout( false, 1 ) );
    managedForm.addPart( pluginSection = new PluginSection( this, body ) );
    managedForm.addPart( new LibrarySection( this, body ) );
  }

  public boolean includeOptionalDependencies() {
    boolean result = false;
    if( pluginSection != null ) {
      result = pluginSection.includeOptionalDependencies();
    }
    return result;
  }
  
}
