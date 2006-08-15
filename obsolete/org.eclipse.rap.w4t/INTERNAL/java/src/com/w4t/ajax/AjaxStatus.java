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
package com.w4t.ajax;

import com.w4t.WebComponent;


/**
 * <p>This class holds information about a component that is rendered for an
 * ajax-enabled browser.</p>
 * <p>This interface is not intended to be implemented by clients.</p>
 */
public interface AjaxStatus {
  
  /** <p>Returns the component for which this status applies.</p> */
  WebComponent getComponent();
  
  /** <p>Returns whether already a hash code has been stored - this is
   * <code>true</code> as soon as <code>setComponentHashCode</code> has been
   * called.</p> */
  boolean hasComponentHashCode();
  
  /** <p>Returns the hash code for the component.</p>
   * <p>May throw an exception if there was no hash code assigned yet.</p> */
  int getComponentHashCode();
  
  /** <p>Sets the hash code for the component.</p> */
  void setComponentHashCode( int hashCode );

  /** <p>Returns whether the component needs to be rendered.</p>
   * @see #updateStatus(boolean) */
  boolean mustRender();
  
  /**
   * <p>Sets the <code>mustRender</code> flag. This method is intended to be 
   * used only by implementors, clients should use
   * {@link #updateStatus(boolean) <code>changeMustRender</code>} instead.
   * </p> 
   */
  void setMustRender( boolean mustRender );
  
  /**
   * <p>Updates this <code>AjaxStatus</code> with information reflecting
   * the current state of the adapted component. In case that the
   * component is an instance of <code>WebContainer</code> 
   * the update is triggered for all components located at the 
   * component-tree-branch of the container.</p>
   * 
   * @param mustRender whether the component (or the branch in case
   *                   of a container) has changed and needs to be rendered.
   */
  void updateStatus( boolean mustRender );
  
  /**
   * <p>Returns whether the visible attribute of the component that is adapted
   * by this <code>AjaxsStatus</code> was <code>true</code> or 
   * <code>false</code> during the last render phase.</p>
   */
  boolean isWasVisible();

  /**
   * <p>Buffers the current value of the visible attribute of the component 
   * that is adapted by this <code>AjaxsStatus</code>. This information can 
   * be recalled in the next render phase via the 
   * <code>{@link #isWasVisible()}</code> method.</p>
   */
  void setWasVisible( boolean wasVisible );
  
  /**
   * <p>Buffers the current value of the enable attribute of the component 
   * that is adapted by this <code>AjaxsStatus</code>. This information can 
   * be recalled in the next render phase via the 
   * <code>{@link #isWasEnabled()}</code> method.</p>
   */
  void setWasEnabled( boolean wasEnabled );

  /**
   * <p>Returns whether the visible attribute of the component that is adapted
   * by this <code>AjaxsStatus</code> was <code>true</code> or 
   * <code>false</code> during the last render phase.</p>
   */
  boolean isWasEnabled();
}
