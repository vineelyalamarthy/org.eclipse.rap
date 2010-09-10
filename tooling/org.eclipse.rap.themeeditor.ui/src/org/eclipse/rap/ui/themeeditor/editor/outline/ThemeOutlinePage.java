/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor.editor.outline;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.ui.themeeditor.editor.outline.CommentFilter.CommentFilterAction;
import org.eclipse.rap.ui.themeeditor.scanner.Scanner;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class ThemeOutlinePage extends ContentOutlinePage {

  private Scanner scanner;

  public ThemeOutlinePage( Scanner scanner ) {
    this.scanner = scanner;
  }

  public void createControl( Composite parent ) {
    super.createControl( parent );
    TreeViewer viewer = getTreeViewer();
    viewer.setContentProvider( new RegionContentProvider() );
    viewer.setLabelProvider( new RegionLabelProvider() );
    viewer.setInput( scanner.getRegions() );
  }

  public void init( IPageSite pageSite ) {
    super.init( pageSite );
    CommentFilterAction commentAction = new CommentFilterAction( getTreeViewer() );
    pageSite.getActionBars().getToolBarManager().add( commentAction );
  }

  public void setActionBars( IActionBars actionBars ) {
    super.setActionBars( actionBars );
  }

  /* for testing */
  public TreeViewer getViewer() {
    return super.getTreeViewer();
  }
}
