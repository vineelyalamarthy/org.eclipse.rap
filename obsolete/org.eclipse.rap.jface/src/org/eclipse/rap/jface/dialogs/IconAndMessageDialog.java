/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.jface.dialogs;

import org.eclipse.rap.jface.resource.JFaceResources;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.graphics.Image;
import org.eclipse.rap.rwt.layout.GridData;
import org.eclipse.rap.rwt.layout.GridLayout;
import org.eclipse.rap.rwt.widgets.*;

/**
 * The IconAndMessageDialog is the abstract superclass of dialogs that have an
 * icon and a message as the first two widgets. In this dialog the icon and
 * message are direct children of the shell in order that they can be read by
 * accessibility tools more easily.
 */
public abstract class IconAndMessageDialog extends Dialog {

  /**
   * Message (a localized string).
   */
  protected String message;
  /**
   * Message label is the label the message is shown on.
   */
  protected Label messageLabel;
  /**
   * Return the label for the image.
   */
  protected Label imageLabel;

  /**
   * Constructor for IconAndMessageDialog.
   * 
   * @param parentShell
   *            the parent shell, or <code>null</code> to create a top-level
   *            shell
   */
  public IconAndMessageDialog( Shell parentShell ) {
    super( parentShell );
  }

  /**
   * Create the area the message will be shown in.
   * @param composite The composite to parent from.
   * @return Control
   */
  protected Control createMessageArea( Composite composite ) {
    // create composite
    // create image
    Image image = getImage();
    if( image != null ) {
      imageLabel = new Label( composite, RWT.NULL );
//      TODO [rst] we don't need this, as we have transparent images, haven't we?
//      image.setBackground( imageLabel.getBackground() );
      imageLabel.setImage( image );
      addAccessibleListeners( imageLabel, image );
      imageLabel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER
                                              | GridData.VERTICAL_ALIGN_BEGINNING ) );
    }
    // create message
    if( message != null ) {
      messageLabel = new Label( composite, getMessageLabelStyle() );
      messageLabel.setText( message );
      GridData data = new GridData( GridData.GRAB_HORIZONTAL
                                    | GridData.HORIZONTAL_ALIGN_FILL
                                    | GridData.VERTICAL_ALIGN_BEGINNING );
      data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
      messageLabel.setLayoutData( data );
    }
    return composite;
  }

  private String getAccessibleMessageFor( Image image ) {
    if( image.equals( getErrorImage() ) ) {
      return JFaceResources.getString( "error" );//$NON-NLS-1$
    }
    if( image.equals( getWarningImage() ) ) {
      return JFaceResources.getString( "warning" );//$NON-NLS-1$
    }
    if( image.equals( getInfoImage() ) ) {
      return JFaceResources.getString( "info" );//$NON-NLS-1$
    }
    if( image.equals( getQuestionImage() ) ) {
      return JFaceResources.getString( "question" ); //$NON-NLS-1$
    }
    return null;
  }

  /**
   * Add an accessible listener to the label if it can be 
   * inferred from the image.
   * @param label
   * @param image
   */
  private void addAccessibleListeners( Label label, final Image image ) {
//    TODO
//    label.getAccessible().addAccessibleListener( new AccessibleAdapter() {
//      public void getName( AccessibleEvent event ) {
//        final String accessibleMessage = getAccessibleMessageFor( image );
//        if( accessibleMessage == null ) {
//          return;
//        }
//        event.result = accessibleMessage;
//      }
//    } );
  }

  /**
   * Returns the style for the message label.
   * 
   * @return the style for the message label
   * 
   * @since 3.0
   */
  protected int getMessageLabelStyle() {
    return RWT.WRAP;
  }

  /*
   * @see Dialog.createButtonBar()
   */
  protected Control createButtonBar( Composite parent ) {
    Composite composite = new Composite( parent, RWT.NONE );
    // create a layout with spacing and margins appropriate for the font
    // size.
    GridLayout layout = new GridLayout();
    layout.numColumns = 0; // this is incremented by createButton
    layout.makeColumnsEqualWidth = true;
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
    layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
    composite.setLayout( layout );
    GridData data = new GridData( GridData.HORIZONTAL_ALIGN_END
                                  | GridData.VERTICAL_ALIGN_CENTER );
    data.horizontalSpan = 2;
    composite.setLayoutData( data );
    composite.setFont( parent.getFont() );
    // Add the buttons to the button bar.
    createButtonsForButtonBar( composite );
    return composite;
  }

  /**
   * Returns the image to display beside the message in this dialog.
   * <p>
   * Subclasses may override.
   * </p>
   * 
   * @return the image to display beside the message
   * @since 2.0
   */
  protected abstract Image getImage();

  /*
   * @see Dialog.createContents(Composite)
   */
  protected Control createContents( Composite parent ) {
    // initialize the dialog units
    initializeDialogUnits( parent );
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.marginHeight = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN ) * 3 / 2;
    layout.marginWidth = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_MARGIN );
    layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
    layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING ) * 2;
    layout.makeColumnsEqualWidth = false;
    parent.setLayout( layout );
    parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    createDialogAndButtonArea( parent );
    return parent;
  }

  /**
   * Create the dialog area and the button bar for the receiver.
   * 
   * @param parent
   */
  protected void createDialogAndButtonArea( Composite parent ) {
    // create the dialog area and button bar
    dialogArea = createDialogArea( parent );
    buttonBar = createButtonBar( parent );
    //Apply to the parent so that the message gets it too.
    applyDialogFont( parent );
  }

  /**
   * Return the <code>Image</code> to be used when 
   * displaying an error. 
   * 
   * @return image  the error image
   */
  public Image getErrorImage() {
    return getSWTImage( RWT.ICON_ERROR );
  }

  /**
   * Return the <code>Image</code> to be used when 
   * displaying a warning. 
   * 
   * @return image  the warning image
   */
  public Image getWarningImage() {
    return getSWTImage( RWT.ICON_WARNING );
  }

  /**
   * Return the <code>Image</code> to be used when 
   * displaying information. 
   * 
   * @return image  the information image
   */
  public Image getInfoImage() {
    return getSWTImage( RWT.ICON_INFORMATION );
  }

  /**
   * Return the <code>Image</code> to be used when 
   * displaying a question. 
   * 
   * @return image  the question image
   */
  public Image getQuestionImage() {
    return getSWTImage( RWT.ICON_QUESTION );
  }

  /**
   * Get an <code>Image</code> from the provided SWT image
   * constant.
   * 
   * @param imageID the SWT image constant
   * @return image  the image
   */
  private Image getSWTImage( final int imageID ) {
    Shell shell = getShell();
    final Display display;
    if( shell == null ) {
      shell = getParentShell();
    }
    if( shell == null ) {
      display = Display.getCurrent();
    } else {
      display = shell.getDisplay();
    }
    Image image = display.getSystemImage( imageID );
    return image;
  }

}
