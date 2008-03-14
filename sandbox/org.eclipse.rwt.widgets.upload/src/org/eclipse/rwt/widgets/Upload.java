/*******************************************************************************
 * Copyright (c) 2002-2007 Critical Software S.A. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors: Tiago
 * Rodrigues (Critical Software S.A.) - initial implementation Joel Oliveira
 * (Critical Software S.A.) - initial commit
 ******************************************************************************/
package org.eclipse.rwt.widgets;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.widgets.internal.uploadkit.IUploadAdapter;
import org.eclipse.rwt.widgets.upload.servlet.FileUploadServlet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.swt.widgets.Composite;

/**
 * Widget representing an Upload box.
 * 
 * @author tjarodrigues
 */
// TODO [fappel] checkWidget() calls in methods
// TODO [fappel] set text methods for browse and upload buttons
public class Upload extends Composite {
  /**
   * TODO [fappel]: comment
   */
  public final static int SHOW_PROGRESS = 1;
  /**
   * TODO [fappel]: comment
   */
  public final static int SHOW_UPLOAD_BUTTON = 2;
  
  private final List uploadListeners;
  private String lastFileUploaded;
  private final String servlet;
  private String path;
  private boolean performUpload;
  private boolean[] uploadInProgresses = { false };
  private int flags;
  private LCAAdapter lcaAdapter;
  private String browseButtonText = "Browse";
  private String uploadButtonText = "Upload";

  // avoid exposure of upload internal stuff
  // TODO [fappel]: think of a better name...
  private final class LCAAdapter implements IUploadAdapter {
    public boolean performUpload() {
      boolean result = Upload.this.performUpload;
      Upload.this.performUpload = false;
      return result;
    }
    public int getFlags() {
      return flags;
    }
    public void setPath( final String path ) {
      if( path != null ) {
        if( !Upload.this.path.equals( path ) ) {
          Upload.this.path = path;
          ModifyEvent modifyEvent = new ModifyEvent( Upload.this );
          modifyEvent.processEvent();
        }
      }
    }
  }
  

  /**
   * Initializes the Upload.
   * 
   * @param parent Parent container.
   * @param style Widget style.
   * @param servlet The upload servlet name.
   * @param showProgress Indicates if the progress bar should be visible.
   */
  public Upload( final Composite parent,
                 final int style,
                 final String servlet,
                 final boolean showProgress )
  {
    this( parent, 
          style, 
          servlet, 
          ( showProgress ? SHOW_PROGRESS : 0 ) | SHOW_UPLOAD_BUTTON );
  }
  
  /**
   * TODO [fappel]: comment
   */
  public Upload( final Composite parent,
                 final int style,
                 final String servlet )
  {
    this( parent, style, servlet, 0 );
  }
  
  /**
   * TODO [fappel]: comment
   */
  public Upload( final Composite parent,
                 final int style,
                 final String servlet,
                 final int flags )
  {
    super( parent, style );
    this.servlet = ( ( servlet == null ) ? "/upload" : servlet );
    this.flags = flags;
    this.lastFileUploaded = "";
    this.uploadListeners = new ArrayList();
    this.path = "";
  }

  /**
   * Gets the servlet.
   * 
   * @return Servlet name.
   */
  public final String getServlet() {
    return servlet;
  }
  
  /**
   * TODO [fappel] comment
   */
  public String getPath() {
    checkWidget();
    return path;
  }
  
  /**
   * TODO [fappel] comment
   */
  public void performUpload() {
    checkWidget();
    if( isEnabled() && !uploadInProgresses[ 0 ] ) {
      performUpload = true;
      uploadInProgresses[ 0 ] = true;
      UploadAdapter listener =  new UploadAdapter() {
        public void uploadFinished( final UploadEvent uploadEvent ) {
          uploadInProgresses[ 0 ] = false;
        }
      };
      addUploadListener( listener );
      try {
        while( uploadInProgresses[ 0 ] ) {
          if( !getDisplay().readAndDispatch() ) {
            getDisplay().sleep();
          }
        }
      } finally {
        uploadInProgresses[ 0 ] = false;
        performUpload = false;
        removeUploadListener( listener );
      }
    }
  }
  
  public Object getAdapter( final Class adapter ) {
    Object result;
    if( adapter == IUploadAdapter.class ) {
      if( lcaAdapter == null ) {
        lcaAdapter = new LCAAdapter();
      }
      result = lcaAdapter;
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }
  
  // TODO [fappel]: improve this preliminary compute size implementation
  public Point computeSize( final int wHint,
                            final int hHint,
                            final boolean changed )
  {
    // need to add this for getting a reasonable height, but don't know exactly
    // how comes, so I hard coded this for the moment...
    int paddingAndBorderOrSo = 14;
    int progressHeight = 16;
    
    int height = 0, width = 0;
    if( wHint == SWT.DEFAULT || hHint == SWT.DEFAULT ) {
      if( ( flags & SHOW_PROGRESS & SHOW_UPLOAD_BUTTON ) > 0 ) {
        // progress bar and upload button visible
        width = computeBaseWidth();
        Point textExtent
          = TextSizeDetermination.textExtent( getFont(), "Upload", 0 );
        width += textExtent.x;
        
        height = computeBaseHeight();
        height = height > textExtent.y ? height : textExtent.y;
        height += paddingAndBorderOrSo + progressHeight;
      } else if( ( flags & SHOW_PROGRESS ) > 0 ) {
        // progress bar visible
        width = computeBaseWidth();
        height = computeBaseHeight() + paddingAndBorderOrSo + progressHeight;        
      } else if( ( flags & SHOW_UPLOAD_BUTTON ) > 0 ) {
        // upload button visible
        width = computeBaseWidth();
        Point textExtent
          = TextSizeDetermination.textExtent( getFont(), "Upload", 0 );
        width += textExtent.x;
        
        height = computeBaseHeight();
        height = height > textExtent.y ? height : textExtent.y;
        height += paddingAndBorderOrSo;
      } else {
        // no progress bar and no upload button visible
        width = computeBaseWidth();
        height =   computeBaseHeight() 
                 + paddingAndBorderOrSo;
      }
    }
    
    if( wHint != SWT.DEFAULT ) {
      width = wHint;
    }
    
    if( hHint != SWT.DEFAULT ) {
      height = hHint;
    }

    return new Point( width, height );
  }

  private int computeBaseHeight() {
    return TextSizeDetermination.getCharHeight( getFont() );
  }

  private int computeBaseWidth() {
    float avgCharWidth = TextSizeDetermination.getAvgCharWidth( getFont() );
    return ( int )( avgCharWidth * 50 );
  }
  
  /**
   * TODO [fappel]: comment
   */
  public void setBrowseButtonText( final String browseButtonText ) {
    checkWidget();
    if( browseButtonText == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    this.browseButtonText = browseButtonText;
  }
  
  /**
   * TODO [fappel]: comment
   */
  public String getBrowseButtonText() {
    checkWidget();
    return browseButtonText;
  }

  /**
   * TODO [fappel]: comment
   */
  public void setUploadButtonText( final String uploadButtonText ) {
    checkWidget();
    if( uploadButtonText == null ) {
      SWT.error( SWT.ERROR_NULL_ARGUMENT );
    }
    this.uploadButtonText = uploadButtonText;
  }
  
  /**
   * TODO [fappel]: comment
   */
  public String getUploadButtonText() {
    checkWidget();
    return uploadButtonText;
  }
  
  /**
   * Adds the listener to the collection of listeners who will
   * be notified when the receiver's path is modified, by sending
   * it one of the messages defined in the <code>ModifyListener</code>
   * interface.
   *
   * @param listener the listener which should be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see ModifyListener
   * @see #removeModifyListener
   */
  public void addModifyListener( final ModifyListener listener ) {
    checkWidget();
    ModifyEvent.addListener( this, listener );
  }

  /**
   * Removes the listener from the collection of listeners who will
   * be notified when the receiver's path is modified.
   *
   * @param listener the listener which should no longer be notified
   *
   * @exception IllegalArgumentException <ul>
   *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   * </ul>
   * @exception SWTException <ul>
   *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   * </ul>
   *
   * @see ModifyListener
   * @see #addModifyListener
   */
  public void removeModifyListener( final ModifyListener listener ) {
    checkWidget();
    ModifyEvent.removeListener( this, listener );
  }



  /**
   * Gets the name of the last uploaded file.
   * 
   * @return The name of the last uploaded file.
   */
  public final String getLastFileUploaded() {
    return lastFileUploaded;
  }
  
  /**
   * Returns the <code>java.io.File<code> that represents the absolute 
   * path to the last uploaded file disk.
   * 
   * @return The <code>java.io.File<code> that represents the absolute 
   * path to the last uploaded file disk or null if no file was uploaded. The
   * latter may be the case if the path entered by the user doesn't exist.
   */
  public File getLastUploadedFile() {
    HttpSession session = RWT.getSessionStore().getHttpSession();
    File tmpDir = FileUploadServlet.getUploadTempDir( session );
    File result = new File( tmpDir, lastFileUploaded );
    if( !result.exists() ) {
      result = null;
    }
    return result;
  }

  /**
   * Sets the name of the last uploaded file.
   * 
   * @param lastFileUploaded The name of the last uploaded file.
   */
  public final void setLastFileUploaded( final String lastFileUploaded ) {
    this.lastFileUploaded = lastFileUploaded;
  }

  /**
   * Adds a new Listener to the Upload.
   * 
   * @param uploadAdapter The new listener.
   */
  public final synchronized void addUploadListener(
    final UploadAdapter uploadAdapter )
  {
    uploadListeners.add( uploadAdapter );
  }

  /**
   * Removes a Listener from the Upload.
   * 
   * @param uploadAdapter The new listener.
   */
  public final synchronized void removeUploadListener(
    final UploadAdapter uploadAdapter )
  {
    uploadListeners.remove( uploadAdapter );
  }

  /**
   * Fires a new Upload Finished Event.
   * 
   * @param uploadEvent The Upload Event to be fired.
   */
  public final synchronized void fireUploadEvent(
    final UploadEvent uploadEvent )
  {
    final Iterator listeners = uploadListeners.iterator();
    while( listeners.hasNext() ) {
      ( ( UploadAdapter )listeners.next() ).uploadFinished( uploadEvent );
    }
  }

  /**
   * {@inheritDoc}
   */
  public final void dispose() {
    uploadListeners.clear();
    super.dispose();
  }
}
