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

package org.eclipse.ui.part;

import org.eclipse.ui.*;
import org.eclipse.ui.internal.util.Util;


public abstract class ViewPart extends WorkbenchPart implements IViewPart {

    /**
     * Listens to PROP_TITLE property changes in this object until the first
     * call to setContentDescription. Used for compatibility with old parts that
     * call setTitle or overload getTitle instead of using
     * setContentDescription.
     */
  private IPropertyListener compatibilityTitleListener = new IPropertyListener() {
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object,
     *      int)
     */
    public void propertyChanged(Object source, int propId) {
      if (propId == IWorkbenchPartConstants.PROP_TITLE) {
        setDefaultContentDescription();
      }
    }
  };
  
  /**
   * Creates a new view.
   */
  protected ViewPart() {
      super();

      addPropertyListener(compatibilityTitleListener);
  }

  
  public IViewSite getViewSite() {
    return ( IViewSite )getSite();
  }

  public void init( final IViewSite site ) throws PartInitException {
    setSite( site );
    setDefaultContentDescription();
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.ui.part.WorkbenchPart#setPartName(java.lang.String)
   */
  protected void setPartName(String partName) {
      if (compatibilityTitleListener != null) {
          removePropertyListener(compatibilityTitleListener);
          compatibilityTitleListener = null;
      }

      super.setPartName(partName);
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.part.WorkbenchPart#setContentDescription(java.lang.String)
   */
//  protected void setContentDescription(String description) {
//      if (compatibilityTitleListener != null) {
//          removePropertyListener(compatibilityTitleListener);
//          compatibilityTitleListener = null;
//      }
//
//      super.setContentDescription(description);
//  }
  
  private void setDefaultContentDescription() {
    if (compatibilityTitleListener == null) {
        return;
    }

    String partName = getPartName();
    String title = getTitle();

    if (Util.equals(partName, title)) {
        internalSetContentDescription(""); //$NON-NLS-1$
    } else {
        internalSetContentDescription(title);
    }
}
}
