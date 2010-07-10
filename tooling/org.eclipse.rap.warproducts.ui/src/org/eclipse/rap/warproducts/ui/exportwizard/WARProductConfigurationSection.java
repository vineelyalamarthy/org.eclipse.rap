/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.warproducts.ui.exportwizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.util.FileExtensionFilter;
import org.eclipse.pde.internal.ui.util.FileValidator;
import org.eclipse.pde.internal.ui.util.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class WARProductConfigurationSection {

  private static final String DOTTED_WARPRODUCT_FILE_EXTENSION = ".warproduct";
  private static final String WARPRODUCT_FILE_EXTENSION = "warproduct";
  private static final String S_PRODUCT_CONFIG = "WARProductConfig";
  private Combo productCombo;
  private WARProductExportWizardPage page;

  public WARProductConfigurationSection( final WARProductExportWizardPage page ) 
  {
    this.page = page;
  }

  public Control createControl( final Composite parent ) {
    Composite group = new Composite( parent, SWT.NONE );
    GridLayout layout = new GridLayout( 3, false );
    layout.marginHeight = 0;
    group.setLayout( layout );
    group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Label label = new Label( group, SWT.NONE );
    label.setText( PDEUIMessages.ProductExportWizardPage_config );
    productCombo = new Combo( group, SWT.BORDER );
    productCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Button browse = new Button( group, SWT.PUSH );
    browse.setText( PDEUIMessages.ProductExportWizardPage_browse );
    browse.setLayoutData( new GridData() );
    browse.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        handleBrowse();
      }
    } );
    SWTUtil.setButtonDimensionHint( browse );
    return group;
  }

  private void handleBrowse() {
    ElementTreeSelectionDialog dialog 
      = new ElementTreeSelectionDialog( page.getShell(),
                                        new WorkbenchLabelProvider(),
                                        new WorkbenchContentProvider() );
    dialog.setValidator( new FileValidator() );
    dialog.setAllowMultiple( false );
    dialog.setTitle( PDEUIMessages.ProductExportWizardPage_fileSelection );
    dialog.setMessage( "Select a WAR product configuration" );
    dialog.addFilter( new FileExtensionFilter( WARPRODUCT_FILE_EXTENSION ) ); 
    dialog.setInput( PDEPlugin.getWorkspace().getRoot() );
    IFile product = getProductFile();
    if( product != null ) {
      dialog.setInitialSelection( product );
    }
    dialog.create();
    IWorkbench workbench = PlatformUI.getWorkbench();
    IWorkbenchHelpSystem helpSystem = workbench.getHelpSystem();
    helpSystem.setHelp( dialog.getShell(), 
                        IHelpContextIds.PRODUCT_CONFIGURATION_SELECTION );
    if( dialog.open() == Window.OK ) {
      IFile file = ( IFile )dialog.getFirstResult();
      String value = file.getFullPath().toString();
      if( productCombo.indexOf( value ) == -1 ) {
        productCombo.add( value, 0 );
      }
      productCombo.setText( value );
    }
  }

  protected void hookListeners() {
    productCombo.addModifyListener( new ModifyListener() {
      public void modifyText( final ModifyEvent e ) {
        updateProductFields();
        page.pageChanged();
      }
    } );
  }

  protected void initialize( final IStructuredSelection selection,
                             final IDialogSettings settings )
  {
    fillProductComboFromSettings( settings );
    if( selection.size() > 0 ) {
      fillProductComboFromSelection( selection );
    } else if( productCombo.getItemCount() > 0 ) {
      productCombo.setText( productCombo.getItem( 0 ) );
    }
    hookListeners();
  }

  private void fillProductComboFromSelection( 
    final IStructuredSelection selection )
  {
    Object object = selection.getFirstElement();
    if( object instanceof IFile ) {
      IFile file = ( IFile )object;
      if( WARPRODUCT_FILE_EXTENSION.equals( file.getFileExtension() ) ) { 
        String entry = file.getFullPath().toString();
        if( productCombo.indexOf( entry ) == -1 ) {
          productCombo.add( entry, 0 );
        }
        productCombo.setText( entry );
      }
    } else if( object instanceof IContainer ) {
      IContainer container = ( IContainer )object;
      try {
        if( container.isAccessible() ) {
          IResource[] resources = container.members();
          for( int i = 0; i < resources.length; i++ ) {
            IResource resource = resources[ i ];
            String resourceName = resource.getName();
            if( resource instanceof IFile
                && resourceName.endsWith( DOTTED_WARPRODUCT_FILE_EXTENSION ) )
            { 
              String path = resource.getFullPath().toString();
              if( productCombo.indexOf( path ) == -1 ) {
                productCombo.add( path, 0 );
              }
            }
          }
        }
        if( productCombo.getItemCount() > 0 ) {
          productCombo.setText( productCombo.getItem( 0 ) );
        }
      } catch( final CoreException e ) {
      }
    }
  }

  private void fillProductComboFromSettings( final IDialogSettings settings ) {
    for( int i = 0; i < 6; i++ ) {
      String curr = settings.get( S_PRODUCT_CONFIG + String.valueOf( i ) );
      if( curr != null && productCombo.indexOf( curr ) == -1 ) {
        IFile file = getProductFile( curr );
        if( file.exists() ) {
          productCombo.add( curr );
        }
      }
    }
  }

  protected IFile getProductFile() {
    return getProductFile( productCombo.getText().trim() );
  }

  protected IFile getProductFile( final String path ) {
    IFile result = null;
    if( path != null && path.length() != 0 ) {
      IPath thePath = new Path( path );
      if( thePath.segmentCount() >= 2 ) {
        result = PDEPlugin.getWorkspace().getRoot().getFile( new Path( path ) );
      }
    }
    return result;
  }

  protected void updateProductFields() {
    page.updateProductFields();
  }

  protected String getRootDirectory() {
    return "WEB-INF";
  }

  protected void saveSettings( final IDialogSettings settings ) {
    saveCombo( settings );
  }

  protected void saveCombo( final IDialogSettings settings ) {
    if( productCombo.getText().trim().length() > 0 ) {
      settings.put( S_PRODUCT_CONFIG + String.valueOf( 0 ),
                    productCombo.getText().trim() );
      String[] items = productCombo.getItems();
      int entries = Math.min( items.length, 5 );
      for( int i = 0; i < entries; i++ ) {
        settings.put( S_PRODUCT_CONFIG + String.valueOf( i + 1 ),
                      items[ i ].trim() );
      }
    }
  }

  protected String validate() {
    String result = null;
    String configLocation = productCombo.getText().trim();
    if( configLocation.length() == 0 ) {
      result = "WAR product configuration is not specified.";
    }
    IPath path = new Path( configLocation );
    IResource resource = PDEPlugin.getWorkspace().getRoot().findMember( path );
    if( resource == null || !( resource instanceof IFile ) ) {
      result = "Specified WAR product configuration does not exist.";
    }
    if( !resource.getName().endsWith( DOTTED_WARPRODUCT_FILE_EXTENSION ) ) {
      result = "A WAR product configuration file name must " +
      		   "have a '.warproduct' extension.";
    }
    return result;
  }
  
}
