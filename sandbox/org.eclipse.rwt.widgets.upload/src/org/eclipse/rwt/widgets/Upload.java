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

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.internal.theme.ThemeManager;
import org.eclipse.rwt.theme.IControlThemeAdapter;
import org.eclipse.rwt.widgets.internal.uploadkit.IUploadAdapter;
import org.eclipse.rwt.widgets.upload.servlet.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.graphics.TextSizeDetermination;
import org.eclipse.swt.widgets.*;

/**
 * Widget representing an Upload box.
 * 
 * @author tjarodrigues
 * @author stefan.roeck 
 */
// TODO [fappel] checkWidget() calls in methods
// TODO [fappel] set text methods for browse and upload buttons
public class Upload extends Control {
  /**
   * Displays a progress bar inside the widget.
   */
  public final static int SHOW_PROGRESS = 1;
  
  /**
   * Fires progress events to registered UploadListeners. 
   * If this flag is not set, only the {@link UploadListener#uploadFinished()}
   * event is fired. 
   * @see UploadListener#uploadInProgress(UploadEvent)
   */
  public final static int FIRE_PROGRESS_EVENTS = 4;
  /**
   * Displays a upload button next to the browse button.
   */
  public final static int SHOW_UPLOAD_BUTTON = 2;
  
  static {
    // TODO: [sr] move to extension point if existant
    // Register FileUploadServiceHandler
    FileUploadServiceHandler.register();
  }
  
  private String lastFileUploaded;
  private final String servlet;
  private String path;
  private boolean performUpload = false;
  private boolean resetUpload = false;
  private int flags;
  private LCAAdapter lcaAdapter;
  private String browseButtonText = "Browse";
  private String uploadButtonText = "Upload";
  private boolean[] uploadInProgresses = { false };

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
      // TODO: [sr] Frank, why not throw this event within readData of the LCA?
      // Its quite hidden here :-)
      if( path != null ) {
        if( !path.equals( Upload.this.path ) ) {
          Upload.this.path = path;
          ModifyEvent modifyEvent = new ModifyEvent( Upload.this );
          modifyEvent.processEvent();
        }
      }
    }
    
    public void setLastFileUploaded( final String lastFileUploaded ) {
      Upload.this.lastFileUploaded = lastFileUploaded;
    }
    
    public String getServletPath() {
      return Upload.this.servlet;
    }
    
    public boolean isResetUpload() {
      return Upload.this.resetUpload;
    }
    
    public void setResetUpload( boolean resetUpload ) {
      Upload.this.resetUpload = resetUpload;
    }
  }
  

  /**
   * Initializes the Upload.
   * 
   * @param parent Parent container.
   * @param style Widget style.
   * @param servlet The upload servlet name.
   * @param showProgress Indicates if the progress bar should be visible.
   * @deprecated use Upload(Composite, int, int) instead
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
   * @deprecated use Upload(Composite, int, int) instead
   */
  public Upload( final Composite parent,
                 final int style,
                 final String servlet )
  {
    this( parent, style, servlet, 0 );
  }
  
  /**
   * @deprecated use Upload(Composite, int, int) instead
   */
  public Upload( final Composite parent,
                 final int style,
                 final String servlet,
                 final int flags )
  {
    super( parent, style );
    this.servlet = ( ( servlet == null ) ? FileUploadServiceHandler.getUrl(getWidgetId()) : servlet );
    this.flags = flags;
    
    if ((this.flags & SHOW_PROGRESS) > 0) {
      this.flags |= FIRE_PROGRESS_EVENTS;
    }
    
    this.lastFileUploaded = "";
    this.path = "";
    
    // Add a fileStorage item which is used for transfering the uploaded file
    FileUploadStorage.getInstance().setUploadStorageItem( getWidgetId(), new FileUploadStorageItem() );
  }
  
  /**
   * Constructs a upload widget.
   * @param style Supported styles:
   * {@link SWT#BORDER}
   * @param flags supported flags:
   * {@link Upload#SHOW_PROGRESS}
   * {@link Upload#SHOW_UPLOAD_BUTTON}
   * {@link Upload#FIRE_PROGRESS_EVENTS}
   * The SHOW_PROGRESS flag implies the flag FIRE_PROGRESS_EVENTS.
   */
  public Upload( final Composite parent,
                 final int style,
                 final int flags )
  {
    // TODO: [sr] implement SWT.BORDER support
    this (parent, style, null, flags);
  }
  

  /**
   * Gets the servlet.
   * 
   * @return Servlet name.
   * @deprecated This method will be removed in a future version as the servlet
   * is only used internally and cannot be set from outside anymore.
   */
  public String getServlet() {
    checkWidget();
    return servlet;
  }
  
  /**
   * Returns the full file name of the last
   * uploaded file including the file path as
   * selected by the user on his local machine.
   * @see Upload#getLastFileUploaded()
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
      UploadListener listener =  new UploadAdapter() {
        public void uploadFinished(UploadEvent event) {
          uploadInProgresses[ 0 ] = false;
        }
      };
      addUploadListener( listener );
      uploadInProgresses[ 0 ] = true;
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
    Point browseButtonSize = computeBrowseButtonSize();
    
    int browseButtonHeight = browseButtonSize.y;
    int progressHeight = 16;
    
    int height = 0, width = 0;
    if( wHint == SWT.DEFAULT || hHint == SWT.DEFAULT ) {
      if( ( ( flags & SHOW_PROGRESS ) > 0 )
          && ( ( flags & SHOW_UPLOAD_BUTTON ) > 0 ) )
      {
        // progress bar and upload button visible
        width = computeBaseWidth();
        final Point textExtent = TextSizeDetermination.stringExtent( getFont(), getUploadButtonText());
        width += textExtent.x;
        
        height = Math.max( computeBaseHeight(), 
                           Math.max( textExtent.y, browseButtonHeight ) );
        
        height += progressHeight;
        
      } else if( ( flags & SHOW_PROGRESS ) > 0 ) {
        // progress bar visible
        width = computeBaseWidth();
        height = Math.max( computeBaseHeight(), browseButtonHeight );
        height += progressHeight;
        
      } else if( ( flags & SHOW_UPLOAD_BUTTON ) > 0 ) {
        // upload button visible
        width = computeBaseWidth();
        
        Point textExtent = TextSizeDetermination.stringExtent( getFont(), getUploadButtonText());
        width += textExtent.x;
        
        height = Math.max( computeBaseHeight(), 
                           Math.max( textExtent.y, browseButtonHeight ) );

      } else {
        // no progress bar and no upload button visible
        width = computeBaseWidth();
        height = Math.max( computeBaseHeight(), browseButtonHeight );
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
    return Graphics.getCharHeight( getFont() );
  }
  
  /**
   * These lines are copied from {@link Button#computeSize(int, int)}.
   * TODO: [sr] Find a better solution to avoid this code duplication...
   */
  private Point computeBrowseButtonSize() {
    int border = getButtonBorder();
    int width = 0, height = 0;
    
    Point extent = TextSizeDetermination.stringExtent( getFont(), getBrowseButtonText() );
    height = Math.max( height, extent.y );
    width += extent.x;
  
    width += 12;
    height += 10;
    
    width += border * 2;
    height += border * 2;
    return new Point( width, height );
  }

  private int getButtonBorder() {
    ThemeManager themeMgr = ThemeManager.getInstance();
    IControlThemeAdapter buttonThemeAdapter = ( IControlThemeAdapter )themeMgr.getThemeAdapter( Button.class );
    int border = buttonThemeAdapter.getBorderWidth( this );
    return border;
  }

  private int computeBaseWidth() {
    float avgCharWidth = Graphics.getAvgCharWidth( getFont() );
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
   * Gets the name of the last uploaded file. This method
   * can be called even if the upload has not finished yet.
   * @see Upload#getPath()
   * 
   * @return The name of the last uploaded file.
   */
  public String getLastFileUploaded() {
    checkWidget();
    return lastFileUploaded;
  }
  
  /**
   * Returns the <code>java.io.File<code> that represents the absolute 
   * path to the last uploaded file disk.
   * 
   * @return The <code>java.io.File<code> that represents the absolute 
   * path to the last uploaded file disk or null if no file was uploaded. The
   * latter may be the case if the path entered by the user doesn't exist.
   * @deprecated This method is no longer supported and always returns null.
   * Please use {@link Upload#getUploadItem()} instead.
   */
  public File getLastUploadedFile() {
    checkWidget();
    return null;
//    HttpSession session = RWT.getSessionStore().getHttpSession();
//    File tmpDir = FileUploadServlet.getUploadTempDir( session );
//    File result = new File( tmpDir, lastFileUploaded );
//    if( !result.exists() ) {
//      result = null;
//    }
//    return result;
  }
  
  
  /**
   * After uploading has finished this method returns the uploaded file
   * and all available meta data, as file name, content type, etc.
   */
  public UploadItem getUploadItem() {
    checkWidget();
    
    final FileUploadStorage storage = FileUploadStorage.getInstance();
    final FileUploadStorageItem uploadedFile = storage.getUploadStorageItem( getWidgetId() );
    final UploadItem uploadItem = new UploadItem( uploadedFile.getFileInputStream(),
                                                  uploadedFile.getContentType(),
                                                  getLastFileUploaded(),
                                                  getPath() );
    return uploadItem;
  }

  private String getWidgetId() {
    return String.valueOf(this.hashCode());
  }

  /**
   * Sets the name of the last uploaded file.
   * 
   * @param lastFileUploaded The name of the last uploaded file.
   * @deprecated This method should not be used and will be removed
   * in a future version because the semantics don't make sense.
   */
  public void setLastFileUploaded( final String lastFileUploaded ) {
    checkWidget();
    this.lastFileUploaded = lastFileUploaded;
  }

  /**
   * Adds a new Listener to the Upload.
   * 
   * @param listener The new listener.
   */
  public void addUploadListener( final UploadListener listener ) {
    checkWidget();
    UploadEvent.addListener( this, listener );

  }

  /**
   * Removes a Listener from the Upload.
   * 
   * @param listener The new listener.
   */
  public void removeUploadListener( final UploadListener listener ) {
    checkWidget();
    UploadEvent.removeListener( this, listener );
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    FileUploadStorage.getInstance().setUploadStorageItem( getWidgetId(), null );
    super.dispose();
  }
  
  /**
   * Resets the internal state of the widget so that all information about the last
   * uploaded file are lost. Additionally the text and the progressbar (if visible)
   * are reset to the defaults.
   */
  public void reset() {
    this.lastFileUploaded = null;
    this.path = null;
    
    FileUploadStorageItem storageItem = FileUploadStorage.getInstance().getUploadStorageItem( getWidgetId() );
    storageItem.setContentType( null );
    storageItem.setFileInputStream( null );
    
    resetUpload = true;
  }
}
