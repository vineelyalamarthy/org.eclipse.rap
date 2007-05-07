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

package org.eclipse.ui.actions;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.ShowViewMenu;

/**
 * Access to standard contribution items provided by the workbench.
 * <p>
 * Most of the functionality of this class is provided by
 * static methods and fields.
 * Example usage:
 * <pre>
 * MenuManager menu = ...;
 * IContributionItem reEdit
 * 	  = ContributionItemFactory.REOPEN_EDITORS.create(window);
 * menu.add(reEdit);
 * </pre>
 * </p>
 * <p>
 * Clients may declare subclasses that provide additional application-specific
 * contribution item factories.
 * </p>
 * 
 * @since 3.0
 */
public abstract class ContributionItemFactory {

    /**
     * Id of contribution items created by this factory.
     */
    private final String contributionItemId;
    
    /**
     * Creates a new workbench contribution item factory with the given id.
     * 
     * @param contributionItemId the id of contribution items created by this factory
     */
    protected ContributionItemFactory(String contributionItemId) {
        this.contributionItemId = contributionItemId;
    }
    
    /**
     * Workbench contribution item (id "viewsShortlist"): A list of views
     * available to be opened in the window, arranged as a shortlist of 
     * promising views and an "Other" subitem. Selecting
     * one of the items opens the corresponding view in the active window.
     * This action dynamically maintains the view shortlist.
     */
    public static final ContributionItemFactory VIEWS_SHORTLIST = new ContributionItemFactory(
            "viewsShortlist") { //$NON-NLS-1$
        /* (non-javadoc) method declared on ContributionItemFactory */
        public IContributionItem create(IWorkbenchWindow window) {
            if (window == null) {
                throw new IllegalArgumentException();
            }
            return new ShowViewMenu(window, getId());
        }
    };
    
    /**
     * Creates a new standard contribution item for the given workbench window.
     * <p>
     * A typical contribution item automatically registers listeners against the
     * workbench window so that it can keep its enablement state up to date.
     * Ordinarily, the window's references to these listeners will be dropped
     * automatically when the window closes. However, if the client needs to get
     * rid of a contribution item while the window is still open, the client must
     * call IContributionItem#dispose to give the item an
     * opportunity to deregister its listeners and to perform any other cleanup.
     * </p>
     * 
     * @param window the workbench window
     * @return the workbench contribution item
     */
    public abstract IContributionItem create(IWorkbenchWindow window);
    
    /**
     * Returns the id of this contribution item factory.
     * 
     * @return the id of contribution items created by this factory
     */
    public String getId() {
        return contributionItemId;
    }
    
}
