/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/

package org.eclipse.rap.demo.controls;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class TreeTab extends ExampleTab {

  private final static int INITIAL_COLUMNS = 5;
  private static final int INITIAL_ITEMS = 15;

  private boolean headerVisible;
  private boolean linesVisible;
  private Tree tree;
  private boolean showImages;
  private final Image treeImage;
  private boolean addMouseListener;

  public TreeTab( final CTabFolder topFolder ) {
    super( topFolder, "Tree" );
    treeImage = Graphics.getImage( "resources/tree_item.gif",
                                   getClass().getClassLoader() );
    showImages = true;
    headerVisible = true;
  }

  protected void createStyleControls( final Composite parent ) {
    createStyleButton( "BORDER", SWT.BORDER );
    createStyleButton( "CHECK", SWT.CHECK );
    createStyleButton( "MULTI", SWT.MULTI );
    createStyleButton( "VIRTUAL", SWT.VIRTUAL );
    createStyleButton( "FULL_SELECTION", SWT.FULL_SELECTION );
    createStyleButton( "NO_SCROLL", SWT.NO_SCROLL );
    createVisibilityButton();
    createEnablementButton();
    createHeaderVisibleButton();
    createLinesVisibleButton();
    createImagesButton( parent );
    createAddNodeButton( parent );
    createDisposeNodeButton( parent );
    createSelectAllButton( parent );
    createDeselectAllButton( parent );
    createSelectButton( parent );
    createDeselectButton( parent );
    createSetSelectionButton( parent );
    createShowColumnControl();
    createFgColorButton();
    createBgColorButton();
    createBgImageButton();
    createFontChooser();
    final Button itemFgButton
      = createPropertyButton( "Custom foreground on 1st item", SWT.CHECK );
    itemFgButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        TreeItem item = tree.getItem( 0 );
        Display display = parent.getDisplay();
        Color green = display.getSystemColor( SWT.COLOR_GREEN );
        item.setForeground( itemFgButton.getSelection() ? green  : null );
      }
    } );
    final Button itemBgButton
      = createPropertyButton( "Custom background on 1st item", SWT.CHECK );
    itemBgButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        TreeItem item = tree.getItem( 0 );
        Display display = parent.getDisplay();
        Color red = display.getSystemColor( SWT.COLOR_DARK_RED );
        item.setBackground( itemBgButton.getSelection() ? red  : null );
      }
    } );
    final Button itemGrayButton2 = createPropertyButton( "Gray out 2nd item",
        SWT.CHECK );
    itemGrayButton2.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        tree.getItem( 1 ).setGrayed( itemGrayButton2.getSelection() );
      }
    } );
    Button columnsAlignmentButton
      = createPropertyButton( "Change columns alignment", SWT.PUSH );
    columnsAlignmentButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        TreeColumn col1 = tree.getColumn( 1 );
        if( col1.getAlignment() == SWT.RIGHT ) {
          col1.setAlignment( SWT.LEFT );
        } else {
          col1.setAlignment( SWT.RIGHT );
        }
        TreeColumn col2 = tree.getColumn( 2 );
        if( col2.getAlignment() == SWT.CENTER ) {
          col2.setAlignment( SWT.LEFT );
        } else {
          col2.setAlignment( SWT.CENTER );
        }
      }
    } );
    Button cbAddMouseListener = new Button( parent, SWT.CHECK );
    cbAddMouseListener.setText( "Attach MouseListener" );
    cbAddMouseListener.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ) {
        addMouseListener = !addMouseListener;
        createNew();
      }
    } );
    cbAddMouseListener.setSelection( addMouseListener );
    Button getTopItemButton
      = createPropertyButton( "Query topItem", SWT.PUSH );
    getTopItemButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        TreeItem item = tree.getTopItem();
        String message = "Current topItem: " + item.toString();
        MessageDialog.openInformation( tree.getShell(),
                                       "Information",
                                       message );
      }
    } );
    Button setTopItemButton
      = createPropertyButton( "Set selection as topItem", SWT.PUSH );
    setTopItemButton.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        TreeItem[] item = tree.getSelection();
        if( item.length > 0 ) {
          tree.setTopItem( item[ 0 ] );
        }
      }
    } );
  }

  protected void createExampleControls( final Composite parent ) {
    parent.setLayout( new GridLayout( 1, false ) );
    int style = getStyle();
    tree = new Tree( parent, style );
    tree.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    for( int i = 0; i < INITIAL_COLUMNS; i++ ) {
      TreeColumn col1 = new TreeColumn( tree, SWT.NONE );
      col1.setText( "Col " + i );
      col1.setWidth( 150 );
    }
    for( int i = 0; i < INITIAL_ITEMS; i++ ) {
      TreeItem item = new TreeItem( tree, SWT.NONE );
      for( int j = 0; j < INITIAL_COLUMNS; j++ ) {
        item.setText( j, "Node_" + ( i + 1 ) + "." + j );
      }
      if( i % 2 == 0 ) {
        TreeItem subitem = new TreeItem( item, SWT.NONE );
        for( int j = 0; j < INITIAL_COLUMNS; j++ ) {
          subitem.setText( j, "Subnode_" + ( i + 1 ) + "." + j );
        }
      }
    }
    if( showImages ) {
      changeImage( tree, treeImage );
    }
    final Label lblTreeEvent = new Label( parent, SWT.NONE );
    lblTreeEvent.setLayoutData( new GridData( 300, 22 ) );
    Menu treeMenu = new Menu( tree );
    MenuItem treeMenuItem = new MenuItem( treeMenu, SWT.PUSH );
    treeMenuItem.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        TreeItem item = tree.getSelection()[ 0 ];
        String itemText = "null";
        if( item != null ) {
          item.getText();
        }
        String message = "You requested a context menu for: " + itemText;
        MessageDialog.openInformation( tree.getShell(),
                                       "Information",
                                       message );
      }
    } );
    treeMenuItem.setText( "TreeContextMenuItem" );
    tree.setMenu( treeMenu );
    tree.addTreeListener( new TreeListener() {
      public void treeCollapsed( final TreeEvent event ) {
        Item item = ( Item )event.item;
        lblTreeEvent.setText( "Collapsed: "  + item.getText() );
      }
      public void treeExpanded( final TreeEvent event ) {
        Item item = ( Item )event.item;
        lblTreeEvent.setText( "Expanded: "  + item.getText() );
      }
    } );
    tree.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        String msg = "Selected: ";
        TreeItem item = ( TreeItem )event.item;
        if( ( getStyle() & SWT.CHECK ) != 0 ) {
          msg += ( item.getChecked() ? "[x] " : "[ ] " );
        }
        msg += item.getText();
        switch( event.detail ) {
          case SWT.NONE:
            msg += ", detail: SWT.NONE";
            break;
          case SWT.CHECK:
            msg += ", detail: SWT.CHECK";
            break;
        }
        lblTreeEvent.setText( msg );
      }

      public void widgetDefaultSelected( final SelectionEvent event ) {
        String title = "Widget Default Selected";
        Item item = ( Item )event.item;
        String message = "Widget default selected on "
          + item.getText()
          + " received";
        MessageDialog.openInformation( getShell(), title, message );
      }
    } );
    tree.setSelection( tree.getItem( 0 ) );
    tree.setHeaderVisible( true );
    if( addMouseListener ) {
      MouseListener listener = new MouseListener(  ) {
        public void mouseDoubleClick( MouseEvent e ) {
          log( "mouseDoubleClick: " + e );
        }
        public void mouseDown( MouseEvent e ) {
          log( "mouseDown: " + e );
        }
        public void mouseUp( MouseEvent e ) {
          log( "mouseUp: " + e );
        }
      };
      tree.addMouseListener( listener );
    }

    registerControl( tree );
  }

  private void createHeaderVisibleButton() {
    final Button button = new Button( styleComp, SWT.CHECK );
    button.setText( "headerVisible" );
    button.setSelection( headerVisible );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        headerVisible = button.getSelection();
        tree.setHeaderVisible( headerVisible );
      }
    } );
  }

  private void createLinesVisibleButton() {
    final Button button = new Button( styleComp, SWT.CHECK );
    button.setText( "linesVisible" );
    button.setSelection( linesVisible );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        linesVisible = button.getSelection();
        tree.setLinesVisible( linesVisible );
      }
    } );
  }

  private void createImagesButton( final Composite parent ) {
    final Button button = new Button( parent, SWT.TOGGLE );
    button.setText( "Show Images" );
    button.setSelection( true );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        showImages = button.getSelection();
        changeImage( tree, showImages ? treeImage : null );
      }
    } );
  }

  private void createAddNodeButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Add child item" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        if( tree.getSelectionCount() > 0 ) {
          TreeItem selection = tree.getSelection()[ 0 ];
          TreeItem treeItem = new TreeItem( selection, SWT.NONE );
          Object[] args = new Object[] {
            new Integer( selection.getItemCount() ),
            selection.getText()
          };
          String text = MessageFormat.format( "SubItem {0} of {1}", args );
          treeItem.setText( text  );
          treeItem.setChecked( true );
          if( showImages ) {
            treeItem.setImage( treeImage );
          }
        }
      }
    } );
  }

  private void createDisposeNodeButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Dispose Selected Item" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        if( tree.getSelectionCount() > 0 ) {
          TreeItem selection = tree.getSelection()[ 0 ];
          selection.dispose();
        }
      }
    } );
  }

  private void createSelectAllButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Select All" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        tree.selectAll();
      }
    } );
  }

  private void createDeselectAllButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Deselect All" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        tree.deselectAll();
      }
    } );
  }

  private void createSelectButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Select second node" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        if( tree.getItemCount() > 1 ) {
          tree.select( tree.getItem( 1 ) );
        }
      }
    } );
  }

  private void createDeselectButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Deselect second node" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        if( tree.getItemCount() > 1 ) {
          tree.deselect( tree.getItem( 1 ) );
        }
      }
    } );
  }

  private void createSetSelectionButton( final Composite parent ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Set selection to first node" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        if( tree.getItemCount() > 0 ) {
          tree.setSelection( tree.getItem( 0 ) );
        }
      }
    } );
  }

  private void createShowColumnControl() {
    Composite composite = new Composite( styleComp, SWT.NONE );
    RowLayout layout = new RowLayout(  SWT.HORIZONTAL );
    layout.center = true;
    composite.setLayout( layout );
    Label label = new Label( composite, SWT.NONE );
    label.setText( "Column" );
    final Text text = new Text( composite, SWT.BORDER );
    Util.textSizeAdjustment( label, text );
    text.setText( String.valueOf( tree.getColumnCount() - 1 ) );
    Button button = new Button( composite, SWT.PUSH );
    button.setText( "Show" );
    button.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent event ) {
        try {
          int index = Integer.parseInt( text.getText() );
          TreeColumn column = tree.getColumn( index );
          tree.showColumn( column );
        } catch( Exception e ) {
          // ignore invalid column
        }
      }
    } );
  }

  private static void changeImage( final Tree tree, final Image image ) {
    TreeItem[] items = tree.getItems();
    for( int i = 0; i < items.length; i++ ) {
      changeImage( items[ i ], image );
    }
  }

  private static void changeImage( final TreeItem item, final Image image ) {
    item.setImage( image );
    TreeItem[] items = item.getItems();
    for( int i = 0; i < items.length; i++ ) {
      changeImage( items[ i ], image );
    }
  }
}
