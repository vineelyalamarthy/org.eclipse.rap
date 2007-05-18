/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.part;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.util.Util;

public abstract class WorkbenchPart extends EventManager
  implements IWorkbenchPart2, IExecutableExtension
{

  private IWorkbenchPartSite partSite;
  private String title = "";
  private String partName;
  private IConfigurationElement configElement;
  private ImageDescriptor imageDescriptor;
  private Image titleImage;
  private String toolTip = ""; //$NON-NLS-1$
  private String contentDescription = ""; //$NON-NLS-1$
  
  public abstract void createPartControl( Composite parent );

  public IWorkbenchPartSite getSite() {
    return partSite;
  }

  public Object getAdapter( Class adapter ) {
    return Platform.getAdapterManager().getAdapter( this, adapter );
  }

  protected void setSite( final IWorkbenchPartSite site ) {
    this.partSite = site;
  }

  public String getTitle() {
    return title;
  }

  public String getPartName() {
    return partName;
  }

  public void setInitializationData( IConfigurationElement cfig,
                                     String propertyName,
                                     Object data )
  {
    // Save config element.
    configElement = cfig;
    // Part name and title.
    partName = Util.safeString( cfig.getAttribute( "name" ) );//$NON-NLS-1$;
    title = partName;
    // Icon.
    String strIcon = cfig.getAttribute( "icon" );//$NON-NLS-1$
    if( strIcon == null ) {
      return;
    }
//    imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin( configElement.getNamespace(),
//                                                                  strIcon );
//    if( imageDescriptor == null ) {
//      return;
//    }
//    titleImage = JFaceResources.getResources()
//      .createImageWithDefault( imageDescriptor );
  }

  protected void setPartName( String partName ) {
    internalSetPartName( partName );
    setDefaultTitle();
  }

  void internalSetPartName( String partName ) {
    partName = Util.safeString( partName );
    Assert.isNotNull( partName );
    // Do not send changes if they are the same
    if( Util.equals( this.partName, partName ) ) {
      return;
    }
    this.partName = partName;
    firePropertyChange( IWorkbenchPartConstants.PROP_PART_NAME );
  }

  void setDefaultTitle() {
//    String description = getContentDescription();
    String name = getPartName();
    String newTitle = name;
//    if( !Util.equals( description, "" ) ) { //$NON-NLS-1$
//      newTitle = MessageFormat.format( WorkbenchMessages.WorkbenchPart_AutoTitleFormat,
//                                       new String[]{
//                                         name, description
//                                       } );
//    }
    setTitle( newTitle );
  }

  protected void setTitle( String title ) {
    title = Util.safeString( title );
    // Do not send changes if they are the same
    if( Util.equals( this.title, title ) ) {
      return;
    }
    this.title = title;
     firePropertyChange( IWorkbenchPart.PROP_TITLE );
  }
  
  void internalSetContentDescription(String description) {
    Assert.isNotNull(description);

    // Do not send changes if they are the same
    if (Util.equals(contentDescription, description)) {
      return;
    }
    this.contentDescription = description;

    firePropertyChange(IWorkbenchPartConstants.PROP_CONTENT_DESCRIPTION);
  }
  
  /**
   * Sets the content description for this part. The content description is typically
   * a short string describing the current contents of the part. Setting this to the
   * empty string will cause a default content description to be used. Clients should
   * call this method instead of overriding getContentDescription(). For views, the
   * content description is shown (by default) in a line near the top of the view. For
   * editors, the content description is shown beside the part name when showing a
   * list of editors. If the editor is open on a file, this typically contains the path 
   * to the input file, without the filename or trailing slash.
   *
   * <p>
   * This may overwrite a value that was previously set in setTitle
   * </p>
   * 
   * @param description the content description
   * 
   * @since 3.0
   */
//  protected void setContentDescription(String description) {
//      internalSetContentDescription(description);
//
//      setDefaultTitle();
//  }
  
  /**
   * {@inheritDoc}
   * <p>
   * It is considered bad practise to overload or extend this method.
   * Parts should call setContentDescription to change their content description. 
   * </p>
   */
  public String getContentDescription() {
      return contentDescription;
  }
  
  /* (non-Javadoc)
   * Gets the title tool tip text of this part.
   *
   * @return the tool tip text
   */
  public String getTitleToolTip() {
      return toolTip;
  }
  
  /* (non-Javadoc)
   * Method declared on IWorkbenchPart.
   */
  public Image getTitleImage() {
      if (titleImage != null) {
          return titleImage;
      }
      return getDefaultImage();
  }
  
  /**
   * Returns the default title image.
   *
   * @return the default image
   */
  protected Image getDefaultImage() {
//      return PlatformUI.getWorkbench().getSharedImages().getImage(
//              ISharedImages.IMG_DEF_VIEW);
    throw new UnsupportedOperationException();
  }
  
  /**
   * Sets or clears the title tool tip text of this part. Clients should
   * call this method instead of overriding <code>getTitleToolTip</code>
   *
   * @param toolTip the new tool tip text, or <code>null</code> to clear
   */
  protected void setTitleToolTip(String toolTip) {
    toolTip = Util.safeString(toolTip);
    // Do not send changes if they are the same
    if (Util.equals(this.toolTip, toolTip)) {
      return;
    }
    this.toolTip = toolTip;
    firePropertyChange(IWorkbenchPart.PROP_TITLE);
  }
  
  /**
   * Sets or clears the title image of this part.
   *
   * @param titleImage the title image, or <code>null</code> to clear
   */
  // TODO: [bm] setTitleImage for parts see WorkbenchPartReference#computeImageDescriptor
//  protected void setTitleImage(Image titleImage) {
//    Assert.isTrue(titleImage == null || true /* || !titleImage.isDisposed() */);
//    // Do not send changes if they are the same
//    if (this.titleImage == titleImage) {
//      return;
//    }
//    this.titleImage = titleImage;
//    firePropertyChange(IWorkbenchPart.PROP_TITLE);
//    if (imageDescriptor != null) {
//// JFaceResources.getResources().destroyImage(imageDescriptor);
//      imageDescriptor = null;
//    }
//    throw new UnsupportedOperationException();
//  }
  
  /**
   * Fires a property changed event.
   * 
   * @param propertyId
   *          the id of the property that changed
   */
  protected void firePropertyChange(final int propertyId) {
      Object[] array = getListeners();
      for (int nX = 0; nX < array.length; nX++) {
          final IPropertyListener l = (IPropertyListener) array[nX];
          try {
              l.propertyChanged(WorkbenchPart.this, propertyId);
          } catch (RuntimeException e) {
//              WorkbenchPlugin.log(e);
            Activator.log(e);
          }
      }
  }
  
  /* (non-Javadoc)
   * Method declared on IWorkbenchPart.
   */
  public void removePropertyListener(IPropertyListener l) {
      removeListenerObject(l);
  }
  
  /* (non-Javadoc)
   * Method declared on IWorkbenchPart.
   */
  public void addPropertyListener(IPropertyListener l) {
      addListenerObject(l);
  }
}
