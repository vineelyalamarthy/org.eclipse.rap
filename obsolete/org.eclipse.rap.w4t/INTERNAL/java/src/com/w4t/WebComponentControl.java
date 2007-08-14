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
package com.w4t;

import java.lang.reflect.Field;

import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.internal.adaptable.IFormAdapter;

/** <p>A helping class to get access to hidden methods and properties from
  * outside of the org.eclipse.rap package.</p>
  * <p>It provides some static methods to get access to hidden properties
  * and methods of WebComponents from outside the package, e.g. for the
  * WebComponentRegistry.</p>
  * <p>
  * This class is for internal use only.
  * </p>
  */
public final class WebComponentControl {
  
  // TODO: [fappel] move this to the respective component
  static {
    IResourceManager manager = W4TContext.getResourceManager();
    manager.register( "resources/images/icons/anchor.gif" );
    manager.register( "resources/images/icons/button.gif" );
    manager.register( "resources/images/icons/cdatabaselist.gif" );
    manager.register( "resources/images/icons/checkbox.gif" );
    manager.register( "resources/images/icons/ctoolbar.gif" );
    manager.register( "resources/images/icons/disabledanchor.gif" );
    manager.register( "resources/images/icons/disabledbutton.gif" );
    manager.register( "resources/images/icons/disabledcdatabaselist.gif" );
    manager.register( "resources/images/icons/disabledcheckbox.gif" );
    manager.register( "resources/images/icons/disabledctoolbar.gif" );
    manager.register( "resources/images/icons/disableddropdown.gif" );
    manager.register( "resources/images/icons/disabledfileupload.gif" );
    manager.register( "resources/images/icons/disabledimage.gif" );
    manager.register( "resources/images/icons/disableditemlist.gif" );
    manager.register( "resources/images/icons/disabledlabel.gif" );
    manager.register( "resources/images/icons/disabledmenubar.gif" );
    manager.register( "resources/images/icons/disabledpanel.gif" );
    manager.register( "resources/images/icons/disabledradiobutton.gif" );
    manager.register( "resources/images/icons/disabledscrollpane.gif" );
    manager.register( "resources/images/icons/disabledselect.gif" );
    manager.register( "resources/images/icons/disabledtabbedpane.gif" );
    manager.register( "resources/images/icons/disabledtable.gif" );
    manager.register( "resources/images/icons/disabledtext.gif" );
    manager.register( "resources/images/icons/disabledtextarea.gif" );
    manager.register( "resources/images/icons/disabledtreeview.gif" );
    manager.register( "resources/images/icons/disabledwebdataobject.gif" );
    manager.register( "resources/images/icons/disabledwebdatatable.gif" );
    manager.register( "resources/images/icons/disabledwebdata_db.gif" );
    manager.register( "resources/images/icons/disabledwebdata_rs.gif" );
    manager.register( "resources/images/icons/fileupload.gif" );
    manager.register( "resources/images/icons/image.gif" );
    manager.register( "resources/images/icons/itemlist.gif" );
    manager.register( "resources/images/icons/label.gif" );
    manager.register( "resources/images/icons/menubar.gif" );
    manager.register( "resources/images/icons/panel.gif" );
    manager.register( "resources/images/icons/radiobutton.gif" );
    manager.register( "resources/images/icons/scrollpane.gif" );
    manager.register( "resources/images/icons/select.gif" );
    manager.register( "resources/images/icons/tabbedpane.gif" );
    manager.register( "resources/images/icons/table.gif" );
    manager.register( "resources/images/icons/text.gif" );
    manager.register( "resources/images/icons/textarea.gif" );
    manager.register( "resources/images/icons/treeview.gif" );
    manager.register( "resources/images/icons/webdataobject.gif" );
    manager.register( "resources/images/icons/webdatatable.gif" );
    manager.register( "resources/images/icons/webdata_db.gif" );
    manager.register( "resources/images/icons/webdata_rs.gif" );
    manager.register( "resources/images/icons/disabledradiobuttongroup.gif" );
    manager.register( "resources/images/icons/radiobuttongroup.gif" );      
    manager.register( "resources/images/icons/disabledcmenu.gif" );
    manager.register( "resources/images/icons/cmenu.gif" );      
  }

  public static void setWebComponents( final WebForm form ) {
    // TODO: [fappel] improve this kind of exception handling ...
    try {
      form.setWebComponents();
    } catch( final Exception ex ) {
      String msg = "Unable to create the component tree.";
      throw new RuntimeException( msg, ex );
    }
  }

  public static String getUniversalAttributes( final WebForm form ) {
    return form.getUniversalAttributes().getUniversalAttributes();
  }

  /** reads out the protected flag in wf to find out, whether the window
    * displaying wf is to be refreshed, and resets the flag */
  public static boolean refreshWindow( final WebForm form ) {
    boolean isRefreshing = form.isRefreshing();
    getFormAdapter( form ).refreshWindow( false );
    return isRefreshing;
  }

  /** reads out the protected flag in wf to find out, whether the window
    * displaying wf is to be refreshed, and resets the flag */
  public static boolean refreshWindow( final WebForm wf, 
                                       final boolean refresh ) 
  {
    boolean isRefreshing = wf.isRefreshing();
    getFormAdapter( wf ).refreshWindow( refresh );
    return isRefreshing;
  }

  /** reads out the protected flag in wf to find out, whether wf is to
    * be displaed in a new browser window, and resets the flag */
  public static boolean openInNewWindow( final WebForm wf ) {
    boolean isOpeningInNewWindow = wf.isOpeningNewWindow();
    getFormAdapter( wf ).showInNewWindow( false );
    return isOpeningInNewWindow;
  }

  private static IFormAdapter getFormAdapter( final WebForm form ) {
    return ( IFormAdapter )form.getAdapter( IFormAdapter.class );
  }

  /** <p>sets whether the WebForm is currently shown in a browser.</p> */
  public static void setActive( final WebForm form, final boolean active ) {
    getFormAdapter( form ).setActive( active );
  }

  /** <p>returns whether the WebForm is currently shown in a 
    * browser.</p> */
  public static boolean isActive( final WebForm form ) {
    return getFormAdapter( form ).isActive();
  }

  public static Field[] getDeclaredFields( final WebComponent component ) {
    return component.getDeclaredFields();
  }
  
  public static void setParent( final WebComponent target, 
                                final WebContainer parent ) 
  {
    target.parent = parent;
  }
}