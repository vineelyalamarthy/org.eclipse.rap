/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.warproducts.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.core.IModelChangedEvent;
import org.eclipse.pde.internal.core.IPluginModelListener;
import org.eclipse.pde.internal.core.PluginModelDelta;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.PDEFormPage;
import org.eclipse.pde.internal.ui.editor.TableSection;
import org.eclipse.pde.internal.ui.editor.build.JARFileFilter;
import org.eclipse.pde.internal.ui.elements.DefaultTableProvider;
import org.eclipse.pde.internal.ui.parts.TablePart;
import org.eclipse.rap.warproducts.core.IWARProduct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

public class LibrarySection extends TableSection
  implements IPluginModelListener
{

  class LibraryContentProvider extends DefaultTableProvider {

    public Object[] getElements( final Object parent ) {
      return getProduct().getLibraries();
    }
    
  }
  
  class LibraryLabelProvider extends LabelProvider {
    
    public String getText( final Object element ) {
      String result = "";
      if( element instanceof IPath ) {
        IPath path = ( IPath )element;
        result = path.segment( path.segmentCount() - 1 );
      } else {
        result = super.getText( element );
      }
      return result;
    }
    
    public Image getImage( final Object element ) {
      Image result = null;
      if( element instanceof IPath ) {
        PDELabelProvider pdeLabelProvider 
          = PDEPlugin.getDefault().getLabelProvider();
        result = pdeLabelProvider.get( PDEPluginImages.DESC_JAR_LIB_OBJ );
      } else {
        result = super.getImage( element );
      }
      return result;
    }
    
  }
  
  private TableViewer libraryTable;

  public LibrarySection( final PDEFormPage formPage, final Composite parent ) {
    super( formPage, parent, Section.DESCRIPTION, getButtonLabels() );
  }

  private static String[] getButtonLabels() {
    String[] labels = new String[ 2 ];
    labels[ 0 ] = PDEUIMessages.Product_PluginSection_add;
    labels[ 1 ] = PDEUIMessages.PluginSection_remove;
    return labels;
  }

  protected void createClient( final Section section, 
                               final FormToolkit toolkit )
  {
    section.setLayout( FormLayoutFactory.createClearGridLayout( false, 1 ) );
    GridData sectionData = new GridData( GridData.FILL_BOTH );
    sectionData.verticalSpan = 2;
    section.setLayoutData( sectionData );
    Composite container = createClientContainer( section, 2, toolkit );
    createViewerPartControl( container, SWT.MULTI, 2, toolkit );
    container.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    TablePart tablePart = getTablePart();
    libraryTable = tablePart.getTableViewer();
    libraryTable.setContentProvider( new LibraryContentProvider() );
    libraryTable.setLabelProvider( new LibraryLabelProvider() );
    addComparator();
    GridData data = ( GridData )tablePart.getControl().getLayoutData();
    data.minimumWidth = 200;
    libraryTable.setInput( getProduct() );
    tablePart.setButtonEnabled( 0, isEditable() );
    tablePart.setButtonEnabled( 1, isEditable() );
    toolkit.paintBordersFor( container );
    section.setClient( container );
    section.setText( "Libraries" );
    section.setDescription( "List all the libraries that goes into the "
                            + "lib folder of the WAR file." );
    getModel().addModelChangedListener( this );
  }

  private void addComparator() {
    libraryTable.setComparator( new ViewerComparator() {

      public int compare( final Viewer viewer, 
                          final Object e1, 
                          final Object e2 ) 
      {
        IPath p1 = ( IPath )e1;
        IPath p2 = ( IPath )e2;
        return super.compare( viewer, p1.toOSString(), p2.toOSString() );
      }
      
    } );
  }

  private IWARProduct getProduct() {
    return ( IWARProduct )getModel().getProduct();
  }

  private IProductModel getModel() {
    return ( IProductModel )getPage().getPDEEditor().getAggregateModel();
  }

  public void modelsChanged( final PluginModelDelta delta ) {
    final Control control = libraryTable.getControl();
    if( !control.isDisposed() ) {
      control.getDisplay().asyncExec( new Runnable() {

        public void run() {
          if( !control.isDisposed() ) {
            libraryTable.refresh();
            updateRemoveButtons();
          }
        }
      } );
    }
  }

  public void modelChanged( final IModelChangedEvent e ) {
    // No need to call super, handling world changed event here
    if( e.getChangeType() == IModelChangedEvent.WORLD_CHANGED ) {
      handleModelEventWorldChanged( e );
    } else {
      Object[] objects = e.getChangedObjects();
      if( e.getChangeType() == IModelChangedEvent.INSERT ) {
        handleInsert( objects );
      } else if( e.getChangeType() == IModelChangedEvent.REMOVE ) {
        handleRemove( objects );
      } else if( e.getChangeType() == IModelChangedEvent.CHANGE ) {
        libraryTable.refresh();
      }
      updateRemoveButtons();
    }
  }

  private void handleInsert( final Object[] objects ) {
    for( int i = 0; i < objects.length; i++ ) {
      if( objects[ i ] instanceof IPath ) {
        libraryTable.add( objects[ i ] );
      }
    }
  }

  private void handleRemove( final Object[] objects ) {
    Table table = libraryTable.getTable();
    int index = table.getSelectionIndex();
    for( int i = 0; i < objects.length; i++ ) {
      if( objects[ i ] instanceof IPath ) {
        libraryTable.remove( objects[ i ] );
      }
    }
    // Update Selection
    int count = table.getItemCount();
    if( count == 0 ) {
      // Nothing to select
    } else if( index < count ) {
      table.setSelection( index );
    } else {
      table.setSelection( count - 1 );
    }
  }

  private void handleModelEventWorldChanged( final IModelChangedEvent event ) {
    if( !libraryTable.getTable().isDisposed() ) {
      libraryTable.setInput( getProduct() );
      refresh();
    }
  }

  protected void selectionChanged( final IStructuredSelection selection ) {
    getPage().getPDEEditor().setSelection( selection );
    updateRemoveButtons();
  }

  private void updateRemoveButtons() {
    TablePart tablePart = getTablePart();
    ISelection selection = getViewerSelection();
    boolean enabled = isEditable()
                      && !selection.isEmpty()
                      && selection instanceof IStructuredSelection;
    tablePart.setButtonEnabled( 1, enabled );
  }

  public void refresh() {
    libraryTable.refresh();
    updateRemoveButtons();
    super.refresh();
  }

  protected void buttonSelected( final int index ) {
    if( index == 0 ) {
      handleAddLibrary();
    } else if( index == 1 ) {
      handleRemoveLibrary();
    }
  }

  private void handleRemoveLibrary() {
    IStructuredSelection sel 
      = ( IStructuredSelection )libraryTable.getSelection();
    if( sel.size() > 0 ) {
      Object[] objects = sel.toArray();
      IPath[] pathes = new IPath[ objects.length ];
      System.arraycopy( objects, 0, pathes, 0, objects.length );
      getProduct().removeLibraries( pathes );
    }
  }
  
  private void handleAddLibrary() {
    Shell shell = getPage().getSite().getShell();
    WorkbenchLabelProvider labelProvider = new WorkbenchLabelProvider();
    WorkbenchContentProvider contentProvider = new WorkbenchContentProvider();
    ElementTreeSelectionDialog dialog 
      = new ElementTreeSelectionDialog( shell, labelProvider, contentProvider );
    dialog.setTitle( PDEUIMessages.BuildEditor_ClasspathSection_jarsTitle );
    dialog.setMessage( "Select JAR archives to be added to the " +
    		           "WAR file's lib directory." );
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    dialog.addFilter( new JARFileFilter() );
    dialog.setInput( workspace );
    dialog.setComparator( new ResourceComparator( ResourceComparator.NAME ) );
    dialog.create();
    IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
    helpSystem.setHelp( dialog.getShell(), IHelpContextIds.ADD_LIBRARY );
    List newLibs = new ArrayList();
    if( dialog.open() == Window.OK ) {
      updateProduct( dialog, newLibs );
    }
    libraryTable.setSelection( new StructuredSelection( newLibs.toArray() ) );
    libraryTable.getTable().setFocus();
  }

  private void updateProduct( final ElementTreeSelectionDialog dialog, 
                              final List newLibs )
  {
    Object[] elements = dialog.getResult();
    for( int i = 0; i < elements.length; i++ ) {
      Object element = elements[ i ];
      if( element instanceof IFile ) {
        IFile file = ( IFile )element;
        IPath fullPath = file.getLocation();
        newLibs.add( fullPath );
        getProduct().addLibrary( fullPath );
      }
    }
  }
  
  public void dispose() {
    IProductModel model = getModel();
    if( model != null ) {
      model.removeModelChangedListener( this );
    }
    super.dispose();
  }
  
}
