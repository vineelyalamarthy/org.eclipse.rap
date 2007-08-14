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


/**
 * <p>The <code>IWindowManager</code> keeps track of all <code>IWindow</code>s
 * for a given session and allows to create and remove them.</p>
 * <p>An instance of the <code>IWindowManager</code> for the current session
 * can be obtained by {@link org.eclipse.rwt.W4TContext#getWindowManager() 
 * <code>W4TContext#getWindowManager()</code>}</p>
 * <p>This interface is not intended to be implemented by clients.</p> 
 */
public interface IWindowManager {

  /** 
   * <p>The instances of this interface represent the library's abstraction 
   * of a browser window.</p>
   * <p>This interface is not intended to be implemented by clients.</p> 
   */
  public interface IWindow {
  
    /** 
     * <p>Returns the unique identifier of this <code>IWindow</code> instance.
     * </p> 
     */
    String getId();
  
    /** 
     * <p>Displays the given <code>form</code> in this <code>IWindow</code>
     * and replaces the one currently being displayed.</p>
     * @param form the new form to be displayed. Must not be <code>null</code>.
     * @throws NullPointerException when <code>form</code> is <code>null</code>.
     * @throws IllegalStateException when the given <code>form</code> is already
     * associated with an <code>IWindow</code>.
     */
    void setFormToDispatch( WebForm form );
    
    /** 
     * <p>Returns the {@link org.eclipse.rwt.WebForm WebForm} to which will be 
     * dispatched. This is the last <code>WebForm</code> which was successfully 
     * set by {@link #setFormToDispatch(WebForm) setFormToDispatch(WebForm)}.
     * </p>
     */
    WebForm getFormToDispatch();

    /**
     * <p>Closes this window.</p>
     * <p>This method is not intended to be called by clients.</p>
     */
    // TODO [rh] calling close directly won't clean up
    void close();

  }

  /**
   * <p>Creates and returns an {@link IWindow IWindow} for the
   * given <code>form</code>.</p>
   * @param form the {@link WebForm <code>WebForm</code>} for which the window
   * should be created. Must not be <code>null</code>.
   * @throws NullPointerException when <code>form</code> is <code>null</code>.
   */
  IWindow create( WebForm form );

  /**
   * <p>Returns the {@link IWindow <code>IWindow</code>} with the given 
   * <code>id</code> or <code>null</code> if no window with that id exists.</p>
   * @param id the id of the window to be found. Must not be <code>null</code>.
   * @throws NullPointerException when <code>id</code> is <code>null</code>.
   */
  IWindow findById( String id );

  /**
   * <p>Returns the {@link IWindow <code>IWindow</code>} which is associated to 
   * the given <code>form</code> or <code>null</code> if no window is 
   * associated.</p>
   * @param form the form to find the associated window for. Must not be 
   * <code>null</code>.
   * @throws NullPointerException when <code>form</code> is <code>null</code>.
   */
  IWindow findWindow( WebForm form );

  /**
   * <p>Returns the {@link WebForm <code>WebForm</code>} which is associated to 
   * the given <code>window</code> or <code>null</code> if no form is 
   * associated.</p>
   * @param window the window to find the associated form for. Must not be 
   * <code>null</code>. 
   * @throws NullPointerException when <code>window</code> is <code>null</code>.
   */
  WebForm findForm( IWindow window );
  
  /**
   * <p>Removes the given <code>window</code> from the list of windows 
   * maintained by this <code>IWindowManager</code>.</p>
   * @param window the <code>IWindow</code> instance to be removed. Must not 
   * be <code>null</code>.
   * @throws NullPointerException when <code>window</code> is <code>null</code>.
   */
  void remove( IWindow window );
}