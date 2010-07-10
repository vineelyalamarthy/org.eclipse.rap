/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.warproducts.ui.newwizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.wizards.PDEWizardNewFileCreationPage;
import org.eclipse.pde.launching.IPDELauncherConstants;
import org.eclipse.pde.ui.launcher.EclipseLaunchShortcut;
import org.eclipse.rap.warproducts.ui.WARProductConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class WARProductFileWizardPage extends PDEWizardNewFileCreationPage {

  public final static int USE_DEFAULT = 0;
  public final static int USE_LAUNCH_CONFIG = 1;
  private static final String FILE_EXTENSION = "warproduct"; //$NON-NLS-1$   
  private Button basicButton;
  private Button launchConfigButton;
  private Combo launchConfigCombo;
  private Group group;

  public WARProductFileWizardPage( final String pageName,
                                   final IStructuredSelection selection )
  {
    super( pageName, selection );
    setDescription( "Create a new WAR product configuration and " +
    		        "initialize it's content." );
    setTitle( "WAR Product Configuration" );
    // Force the file extension to be 'warproduct'
    setFileExtension( FILE_EXTENSION );
  }


  /*
   * (non-Javadoc)
   * @see
   * org.eclipse.ui.dialogs.WizardNewFileCreationPage#createAdvancedControls
   * (org.eclipse.swt.widgets.Composite)
   */
  protected void createAdvancedControls( final Composite parent ) {
    group = new Group( parent, SWT.NONE );
    group.setText( PDEUIMessages.ProductFileWizadPage_groupTitle );
    group.setLayout( new GridLayout( 2, false ) );
    group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    basicButton = new Button( group, SWT.RADIO );
    GridData gd = new GridData();
    gd.horizontalSpan = 2;
    basicButton.setLayoutData( gd );
    basicButton.setText( PDEUIMessages.ProductFileWizadPage_basic );
    launchConfigButton = new Button( group, SWT.RADIO );
    launchConfigButton.setText( PDEUIMessages.ProductFileWizadPage_existingLaunchConfig );
    launchConfigButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        launchConfigCombo.setEnabled( launchConfigButton.getSelection() );
      }
    } );
    launchConfigCombo = new Combo( group, SWT.SINGLE | SWT.READ_ONLY );
    launchConfigCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    launchConfigCombo.setItems( getLaunchConfigurations() );
    initializeState();
  }

  private void initializeState() {
    launchConfigCombo.setEnabled( false );
    if( launchConfigCombo.getItemCount() > 0 ) {
      launchConfigCombo.setText( launchConfigCombo.getItem( 0 ) );
    }
    basicButton.setSelection( true );
  }

  private String[] getLaunchConfigurations() {
    ArrayList list = new ArrayList();
    try {
      addLaunchConfigToListFromType( list, 
                                     EclipseLaunchShortcut.CONFIGURATION_TYPE  );
      // add osgi launch configs to the list
      addLaunchConfigToListFromType( list, 
                                     IPDELauncherConstants.OSGI_CONFIGURATION_TYPE  );
      // add RAP launch configs to the list
      addLaunchConfigToListFromType( list, 
                                     WARProductConstants.RAP_LAUNCH_CONFIG_TYPE );
    } catch( final CoreException e ) {
      PDEPlugin.logException( e );
    }
    String[] launchConfigArray = new String[ list.size() ];
    return ( String[] )list.toArray( launchConfigArray );
  }
  
  private void addLaunchConfigToListFromType( final List list, 
                                              final String type ) 
    throws CoreException 
  {
    ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType configType 
      = manager.getLaunchConfigurationType( type );
    ILaunchConfiguration[] configs 
      = manager.getLaunchConfigurations( configType );
    for( int i = 0; i < configs.length; i++ ) {
      if( !DebugUITools.isPrivate( configs[ i ] ) ) {
        list.add( configs[ i ].getName() );
      }
    }
  }

  public ILaunchConfiguration getSelectedLaunchConfiguration() {
    ILaunchConfiguration result = null;
    if( launchConfigButton.getSelection() ) {
      String configName = launchConfigCombo.getText();
      try {
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = manager.getLaunchConfigurationType( EclipseLaunchShortcut.CONFIGURATION_TYPE );
        ILaunchConfigurationType type2 = manager.getLaunchConfigurationType( IPDELauncherConstants.OSGI_CONFIGURATION_TYPE );
        ILaunchConfigurationType type3 = manager.getLaunchConfigurationType( WARProductConstants.RAP_LAUNCH_CONFIG_TYPE );
        ILaunchConfiguration[] configs = manager.getLaunchConfigurations( type );
        ILaunchConfiguration[] configs2 = manager.getLaunchConfigurations( type2 );
        ILaunchConfiguration[] configs3 = manager.getLaunchConfigurations( type3 );
        ILaunchConfiguration[] configurations = new ILaunchConfiguration[ configs.length
                                                                          + configs2.length 
                                                                          + configs3.length ];
        System.arraycopy( configs, 0, configurations, 0, configs.length );
        System.arraycopy( configs2,
                          0,
                          configurations,
                          configs.length,
                          configs2.length );
        System.arraycopy( configs3,
                          0,
                          configurations,
                          configs2.length,
                          configs3.length );
        for( int i = 0; i < configurations.length && result == null; i++ ) {
          if( configurations[ i ].getName().equals( configName )
              && !DebugUITools.isPrivate( configurations[ i ] ) ) 
          {
            result = configurations[ i ];
          }
        }
      } catch( final CoreException e ) {
        PDEPlugin.logException( e );
      }
    }
    return result;
  }

  public int getInitializationOption() {
    int result = USE_LAUNCH_CONFIG;
    if( basicButton.getSelection() ) {
      result = USE_DEFAULT;
    }
    return result;
  }

  public void createControl( final Composite parent ) {
    super.createControl( parent );
    Dialog.applyDialogFont( group );
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    helpSystem.setHelp( getControl(), IHelpContextIds.PRODUCT_FILE_PAGE );
  }
}
