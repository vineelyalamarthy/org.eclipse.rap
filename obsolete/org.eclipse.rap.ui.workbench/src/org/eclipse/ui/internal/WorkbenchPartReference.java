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

package org.eclipse.ui.internal;

import java.util.BitSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.util.Util;


public abstract class WorkbenchPartReference
  implements IWorkbenchPartReference
{
  public static int STATE_LAZY = 0;
  public static int STATE_CREATION_IN_PROGRESS = 1;
  public static int STATE_CREATED = 2;
  public static int STATE_DISPOSED = 3;

  private int state = STATE_LAZY;
  private String id;
  private String partName;
  private String title;
  private String tooltip;
  protected PartPane pane;
  protected IWorkbenchPart part;
  private String contentDescription;
  private Image image;
  private ImageDescriptor imageDescriptor;
  private ImageDescriptor defaultImageDescriptor;

  private ListenerList propChangeListeners = new ListenerList();
  private ListenerList internalPropChangeListeners = new ListenerList();
  private BitSet queuedEvents = new BitSet();
  private boolean queueEvents = false;
  
  private IPropertyListener propertyChangeListener = new IPropertyListener() {
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
     */
    public void propertyChanged(Object source, int propId) {
      partPropertyChanged(source, propId);
    }
  };

  public final PartPane getPane() {
    if( pane == null ) {
      pane = createPane();
    }
    return pane;
  }

  public void init( String id,
                    String title,
                    String tooltip,
                    ImageDescriptor desc,
                    String paneName,
                    String contentDescription )
  {
    Assert.isNotNull( id );
    Assert.isNotNull( title );
    Assert.isNotNull( tooltip );
    Assert.isNotNull( desc );
    Assert.isNotNull( paneName );
    Assert.isNotNull( contentDescription );
    this.id = id;
    this.title = title;
    this.tooltip = tooltip;
    this.partName = paneName;
    this.contentDescription = contentDescription;
    this.defaultImageDescriptor = desc;
    this.imageDescriptor = computeImageDescriptor();
  }

  protected ImageDescriptor computeImageDescriptor() {
    if( part != null ) {
      // TODO [bm] image desc of image
//      return ImageDescriptor.createFromImage( part.getTitleImage(),
//                                              Display.getCurrent() );
//        try {
//          URL id = new URL("bundleentry://" + part.getTitleImage().getPath(image));
//          return ImageDescriptor.createFromURL(id);
//        } catch (MalformedURLException e) {
//          e.printStackTrace();
//        }
    }
    return defaultImageDescriptor;
  }

  public boolean getVisible() {
// if( isDisposed() ) {
// return false;
//    }
    return getPane().getVisible();
  }

  public void setVisible( boolean isVisible ) {
//    if( isDisposed() ) {
//      return;
//    }
    getPane().setVisible( isVisible );
  }

  protected void setTitle( String newTitle ) {
    if( Util.equals( title, newTitle ) ) {
      return;
    }
    title = newTitle;
    firePropertyChange( IWorkbenchPartConstants.PROP_TITLE );
  }

  protected void setToolTip( String newToolTip ) {
    if( Util.equals( tooltip, newToolTip ) ) {
      return;
    }
    tooltip = newToolTip;
    firePropertyChange( IWorkbenchPartConstants.PROP_TITLE );
  }

  protected void partPropertyChanged(Object source, int propId) {

    // We handle these properties directly (some of them may be transformed
    // before firing events to workbench listeners)
    if (propId == IWorkbenchPartConstants.PROP_CONTENT_DESCRIPTION
        || propId == IWorkbenchPartConstants.PROP_PART_NAME
        || propId == IWorkbenchPartConstants.PROP_TITLE) {

      refreshFromPart();
    } else {
      // Any other properties are just reported to listeners verbatim
      firePropertyChange(propId);
    }

    // Let the model manager know as well
//    if (propId == IWorkbenchPartConstants.PROP_DIRTY) {
//      IWorkbenchPart actualPart = getPart(false);
//      if (actualPart != null) {
//        SaveablesList modelManager = (SaveablesList) actualPart.getSite()
//            .getService(ISaveablesLifecycleListener.class);
//        modelManager.dirtyChanged(actualPart);
//      }
//    }
  }
  
  /**
   * Refreshes all cached values with the values from the real part 
   */
  protected void refreshFromPart() {
      deferEvents(true);

      setPartName(computePartName());
      setTitle(computeTitle());
      setContentDescription(computeContentDescription());
      setToolTip(getRawToolTip());
      setImageDescriptor(computeImageDescriptor());

      deferEvents(false);
  }
  
  protected final String getRawToolTip() {
    return Util.safeString(part.getTitleToolTip());
  }
  
  /**
   * Computes a new content description for the part. Subclasses may override to change the
   * default behavior
   * 
   * @return the new content description for the part
   */
  protected String computeContentDescription() {
      return getRawContentDescription();
  }

  /**
   * Returns the content description as set directly by the part, or the empty string if none
   * 
   * @return the unmodified content description from the part (or the empty string if none)
   */
  protected final String getRawContentDescription() {
      if (part instanceof IWorkbenchPart2) {
          IWorkbenchPart2 part2 = (IWorkbenchPart2) part;

          return part2.getContentDescription();
      }

      return ""; //$NON-NLS-1$        
  }
  
  /**
   * Computes a new title for the part. Subclasses may override to change the default behavior.
   * 
   * @return the title for the part
   */
  protected String computeTitle() {
      return getRawTitle();
  }

  /**
   * Returns the unmodified title for the part, or the empty string if none
   * 
   * @return the unmodified title, as set by the IWorkbenchPart. Returns the empty string if none.
   */
  protected final String getRawTitle() {
      return Util.safeString(part.getTitle());
  }
  
  /**
   * Gets the part name directly from the associated workbench part,
   * or the empty string if none.
   * 
   * @return
   */
  protected final String getRawPartName() {
      String result = ""; //$NON-NLS-1$

      if (part instanceof IWorkbenchPart2) {
          IWorkbenchPart2 part2 = (IWorkbenchPart2) part;

          result = Util.safeString(part2.getPartName());
      }

      return result;
  }

  protected String computePartName() {
      return getRawPartName();
  }
  
  protected void setPartName(String newPartName) {
    if (Util.equals(partName, newPartName)) {
        return;
    }

    partName = newPartName;
    firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
}
  /**
   * Calling this with deferEvents(true) will queue all property change events until a subsequent
   * call to deferEvents(false). This should be used at the beginning of a batch of related changes
   * to prevent duplicate property change events from being sent.
   * 
   * @param shouldQueue
   */
  private void deferEvents(boolean shouldQueue) {
      queueEvents = shouldQueue;

      if (queueEvents == false) {
        // do not use nextSetBit, to allow compilation against JCL Foundation (bug 80053)
        for (int i = 0, n = queuedEvents.size(); i < n; ++i) {
          if (queuedEvents.get(i)) {
            firePropertyChange(i);
            queuedEvents.clear(i);
          }
          }
      }
  }
  
  protected void setContentDescription( String newContentDescription ) {
    if( Util.equals( contentDescription, newContentDescription ) ) {
      return;
    }
    contentDescription = newContentDescription;
    firePropertyChange( IWorkbenchPartConstants.PROP_CONTENT_DESCRIPTION );
  }

  protected void setImageDescriptor( final ImageDescriptor descriptor ) {
    if( Util.equals( imageDescriptor, descriptor ) ) {
      return;
    }
//    Image oldImage = image;
//    ImageDescriptor oldDescriptor = imageDescriptor;
    image = null;
    imageDescriptor = descriptor;
    // Don't queue events triggered by image changes. We'll dispose the image
    // immediately after firing the event, so we need to fire it right away.
    immediateFirePropertyChange( IWorkbenchPartConstants.PROP_TITLE );
    if( queueEvents ) {
      // If there's a PROP_TITLE event queued, remove it from the queue because
      // we've just fired it.
      queuedEvents.clear( IWorkbenchPartConstants.PROP_TITLE );
    }
//     If we had allocated the old image, deallocate it now (AFTER we fire the
//     property change
//     -- listeners may need to clean up references to the old image)
//    if( oldImage != null ) {
//      JFaceResources.getResources().destroy( oldDescriptor );
//    }
  }


  
  protected abstract PartPane createPane();
  protected abstract IWorkbenchPart createPart();
  
  ////////////////////////////////////
  // Interface IWorkbenchPartReference
  
  public String getId() {
    return id;
  }

  public IWorkbenchPart getPart( boolean restore ) {
//    if( isDisposed() ) {
//      return null;
//    }
    if( part == null && restore ) {
      if( state == STATE_CREATION_IN_PROGRESS ) {
        IStatus result = Activator.getStatus( new PartInitException( NLS.bind( "Warning: Detected recursive attempt by part {0} to create itself (this is probably, but not necessarily, a bug)", //$NON-NLS-1$
                                                                                     getId() ) ) );
//        Activator.log( result );
        return null;
      }
      try {
        state = STATE_CREATION_IN_PROGRESS;
        IWorkbenchPart newPart = createPart();
        if( newPart != null ) {
          part = newPart;
          // Add a dispose listener to the part. This dispose listener does
          // nothing but log an exception
          // if the part's widgets get disposed unexpectedly. The workbench part
          // reference is the only
          // object that should dispose this control, and it will remove the
          // listener before it does so.
//          getPane().getControl().addDisposeListener( prematureDisposeListener );
          part.addPropertyListener( propertyChangeListener );
          refreshFromPart();
//          releaseReferences();
//          fireInternalPropertyChange( INTERNAL_PROPERTY_OPENED );
        }
      } finally {
        state = STATE_CREATED;
      }
    }
    return part;
  }

  public String getPartName() {
    return partName;
  }

  public String getTitle() {
    return Util.safeString( title );
  }

  public String getTitleToolTip() {
    return Util.safeString( tooltip );
  }

  public String getContentDescription() {
    return Util.safeString( contentDescription );
  }

  public final Image getTitleImage() {
//    if( isDisposed() ) {
//      return PlatformUI.getWorkbench()
//        .getSharedImages()
//        .getImage( ISharedImages.IMG_DEF_VIEW );
//    }
//    if( image == null ) {
//      image = JFaceResources.getResources()
//        .createImageWithDefault( imageDescriptor );
//    }
    if( image == null ) {
      image = imageDescriptor.createImage();
    }
    return image;
  }

  public boolean isDirty() {
//    if( !( part instanceof ISaveablePart ) ) {
//      return false;
//    }
//    return ( ( ISaveablePart )part ).isDirty();
    return false;
  }

  public void addPropertyListener( IPropertyListener listener ) {
    // The properties of a disposed reference will never change, so don't
    // add listeners
//    if( isDisposed() ) {
//      return;
//    }
    propChangeListeners.add( listener );
  }

  /**
   * @see IWorkbenchPart
   */
  public void removePropertyListener( IPropertyListener listener ) {
    // Currently I'm not calling checkReference here for fear of breaking things
    // late in 3.1, but it may
    // make sense to do so later. For now we just turn it into a NOP if the
    // reference is disposed.
//    if( isDisposed() ) {
//      return;
//    }
    propChangeListeners.remove( listener );
  }

  /////////////////////////
  // Property change events
  
  protected void firePropertyChange( int id ) {
    if( queueEvents ) {
      queuedEvents.set( id );
      return;
    }
    immediateFirePropertyChange( id );
  }

  private void immediateFirePropertyChange( int id ) {
//    UIListenerLogging.logPartReferencePropertyChange( this, id );
    Object listeners[] = propChangeListeners.getListeners();
    for( int i = 0; i < listeners.length; i++ ) {
      ( ( IPropertyListener )listeners[ i ] ).propertyChanged( part, id );
    }
    fireInternalPropertyChange( id );
  }

  private void fireInternalPropertyChange(int id) {
    Object listeners[] = internalPropChangeListeners.getListeners();
    for( int i = 0; i < listeners.length; i++ ) {
      ( ( IPropertyListener )listeners[ i ] ).propertyChanged( this, id );
    }
  }
}
