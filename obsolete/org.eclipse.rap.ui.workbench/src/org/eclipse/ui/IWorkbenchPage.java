/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui;


public interface IWorkbenchPage extends IPartService, ISelectionService {
	
	/**
	 * Show view mode that indicates the view should be made visible and
	 * activated. Use of this mode has the same effect as calling
	 * {@link #showView(String)}.
	 * 
	 * @since 3.0
	 */
	public static final int VIEW_ACTIVATE = 1;

	/**
	 * Show view mode that indicates the view should be made visible. If the
	 * view is opened in the container that contains the active view then this
	 * has the same effect as <code>VIEW_CREATE</code>.
	 * 
	 * @since 3.0
	 */
	public static final int VIEW_VISIBLE = 2;

	/**
	 * Show view mode that indicates the view should be made created but not
	 * necessarily be made visible. It will only be made visible in the event
	 * that it is opened in its own container. In other words, only if it is not
	 * stacked with another view.
	 * 
	 * @since 3.0
	 */
	public static final int VIEW_CREATE = 3;

	/**
	 * Change event id when one or more views are shown in a perspective.
	 * 
	 * @see IPerspectiveListener
	 */
	public static final String CHANGE_VIEW_SHOW = "viewShow"; //$NON-NLS-1$

  IWorkbenchWindow getWorkbenchWindow();
  IViewReference[] getViewReferences();
  String getLabel();
  IViewPart findView( String viewId );
  IViewReference findViewReference( String viewId );
  IViewReference findViewReference( String viewId, String secondaryId );
  IWorkbenchPartReference getReference( IWorkbenchPart part );
  boolean isPartVisible( IWorkbenchPart part );
  void toggleZoom( IWorkbenchPartReference ref );
  boolean isZoomed();
  void activate( IWorkbenchPart part );

  /**
   * Shows the view identified by the given view id in this page and gives it
   * focus. If there is a view identified by the given view id (and with no
   * secondary id) already open in this page, it is given focus.
   * 
   * @param viewId
   *            the id of the view extension to use
   * @return the shown view
   * @exception PartInitException
   *                if the view could not be initialized
   */
  public IViewPart showView(String viewId) throws PartInitException;

  /**
   * Hides the given view. The view must belong to this page.
   * 
   * @param view
   *            the view to hide
   */
  public void hideView(IViewPart view);

  /**
   * Hides the given view that belongs to the reference, if any.
   * 
   * @param view
   *            the references whos view is to be hidden
   * @since 3.0
   */
  public void hideView(IViewReference view);

  /**
   * Returns the current perspective descriptor for this page, or
   * <code>null</code> if there is no current perspective.
   * 
   * @return the current perspective descriptor or <code>null</code>
   * @see #setPerspective
   * @see #savePerspective
   */
  public IPerspectiveDescriptor getPerspective();
  
  /**
   * Returns the show view shortcuts associated with the current perspective.
   * Returns an empty array if there is no current perspective.
   * 
   * @see IPageLayout#addShowViewShortcut(String)
   * @return an array of view identifiers
   * @since 3.1
   */
  public String[] getShowViewShortcuts();
  
	/**
	 * Shows a view in this page with the given id and secondary id. The
	 * behaviour of this method varies based on the supplied mode. If
	 * <code>VIEW_ACTIVATE</code> is supplied, the view is focus. If
	 * <code>VIEW_VISIBLE</code> is supplied, then it is made visible but not
	 * given focus. Finally, if <code>VIEW_CREATE</code> is supplied the view
	 * is created and will only be made visible if it is not created in a folder
	 * that already contains visible views.
	 * <p>
	 * This allows multiple instances of a particular view to be created. They
	 * are disambiguated using the secondary id. If a secondary id is given, the
	 * view must allow multiple instances by having specified
	 * allowMultiple="true" in its extension.
	 * </p>
	 * 
	 * @param viewId
	 *            the id of the view extension to use
	 * @param secondaryId
	 *            the secondary id to use, or <code>null</code> for no
	 *            secondary id
	 * @param mode
	 *            the activation mode. Must be {@link #VIEW_ACTIVATE},
	 *            {@link #VIEW_VISIBLE} or {@link #VIEW_CREATE}
	 * @return a view
	 * @exception PartInitException
	 *                if the view could not be initialized
	 * @exception IllegalArgumentException
	 *                if the supplied mode is not valid
	 * @since 3.0
	 */
	public IViewPart showView(String viewId, String secondaryId, int mode)
			throws PartInitException;
}
