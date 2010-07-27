/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.editor.product.PluginSection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class ConfigurationPage extends PDEFormPage {

  public static final String PLUGIN_ID = "plugin-dependencies"; //$NON-NLS-1$
  public static final String FEATURE_ID = "feature-dependencies"; //$NON-NLS-1$
  private PluginSection fPluginSection = null;

  public ConfigurationPage( FormEditor editor, boolean useFeatures ) {
    super( editor, useFeatures
                              ? FEATURE_ID
                              : PLUGIN_ID, "Configuration" );
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.pde.internal.ui.editor.PDEFormPage#getHelpResource()
   */
  protected String getHelpResource() {
    return IHelpContextIds.CONFIGURATION_PAGE;
  }

  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.pde.internal.ui.editor.PDEFormPage#createFormContent(org.eclipse
   * .ui.forms.IManagedForm)
   */
  protected void createFormContent( IManagedForm managedForm ) {
    super.createFormContent( managedForm );
    ScrolledForm form = managedForm.getForm();
    FormToolkit toolkit = managedForm.getToolkit();
    form.setImage( PDEPlugin.getDefault()
      .getLabelProvider()
      .get( PDEPluginImages.DESC_REQ_PLUGINS_OBJ ) );
    form.setText( PDEUIMessages.Product_DependenciesPage_title );
    fillBody( managedForm, toolkit );
    PlatformUI.getWorkbench()
      .getHelpSystem()
      .setHelp( form.getBody(), IHelpContextIds.CONFIGURATION_PAGE );
  }

  private void fillBody( IManagedForm managedForm, FormToolkit toolkit ) {
    Composite body = managedForm.getForm().getBody();
    body.setLayout( FormLayoutFactory.createFormGridLayout( false, 1 ) );
    // sections
    managedForm.addPart( fPluginSection = new PluginSection( this, body ) );
    managedForm.addPart( new LibrarySection( this, body ) );
  }

  public boolean includeOptionalDependencies() {
    return ( fPluginSection != null )
                                     ? fPluginSection.includeOptionalDependencies()
                                     : false;
  }
}
