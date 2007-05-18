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

package org.eclipse.ui.internal.registry;

import java.text.MessageFormat;
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.views.*;
import com.w4t.SessionSingletonBase;


public class ViewRegistry
  extends SessionSingletonBase
  implements IViewRegistry
{
  
  private ViewRegistryReader reader = new ViewRegistryReader();
  
  private List categories;
  
  private Category miscCategory;
  
  private boolean dirtyViewCategoryMappings = true;
  
  /**
   * Proxies a Category implementation.
   * 
   * @since 3.1
   */
  private static class ViewCategoryProxy implements IViewCategory /*, IPluginContribution */ {

      private Category rawCategory;

      /**
       * Create a new instance of this class
       * 
       * @param rawCategory the category
       */
      public ViewCategoryProxy(Category rawCategory) {
          this.rawCategory = rawCategory;
      }
      
      /* (non-Javadoc)
       * @see org.eclipse.ui.views.IViewCategory#getViews()
       */
      public IViewDescriptor[] getViews() {
          ArrayList elements = rawCategory.getElements();
          if (elements == null) {
				return new IViewDescriptor[0];
			}
          return (IViewDescriptor[]) elements
                  .toArray(
                          new IViewDescriptor[elements.size()]);
      }

      /* (non-Javadoc)
       * @see org.eclipse.ui.views.IViewCategory#getId()
       */
      public String getId() {
          return rawCategory.getId();
      }

      /* (non-Javadoc)
       * @see org.eclipse.ui.views.IViewCategory#getPath()
       */
      public IPath getPath() {
          String rawParentPath = rawCategory.getRawParentPath();
          if (rawParentPath == null) {
				return new Path(""); //$NON-NLS-1$
			}
          return new Path(rawParentPath);
      }

      /* (non-Javadoc)
       * @see org.eclipse.ui.views.IViewCategory#getLabel()
       */
      public String getLabel() {
          return rawCategory.getLabel();
      }

      /* (non-Javadoc)
       * @see org.eclipse.ui.IPluginContribution#getLocalId()
       */
      public String getLocalId() {
          return getId();
      }

      /* (non-Javadoc)
       * @see org.eclipse.ui.IPluginContribution#getPluginId()
       */
      public String getPluginId() {
          return rawCategory.getPluginId();
      }
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			if (o instanceof IViewCategory) {
				return getId().equals(((IViewCategory)o).getId());
			}
			return false;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return getId().hashCode();
		}
  }
  
  private final SortedSet views = new TreeSet( new Comparator() {
    public int compare( Object o1, Object o2 ) {
      String id1 = ( ( ViewDescriptor )o1 ).getId();
      String id2 = ( ( ViewDescriptor )o2 ).getId();
      return id1.compareTo( id2 );
    }
  } );
  
  private ViewRegistry() {
	categories = new ArrayList();
    reader.readViews( Platform.getExtensionRegistry(), this );
  }
  
  
  public static IViewRegistry getInstance() {
    return ( IViewRegistry )getInstance( ViewRegistry.class );
  }

  public void add( final ViewDescriptor desc ) {
    if( views.add( desc ) ) {
      dirtyViewCategoryMappings = true;
//      PlatformUI.getWorkbench()
//        .getExtensionTracker()
//        .registerObject( desc.getConfigurationElement().getDeclaringExtension(),
//                         desc,
//                         IExtensionTracker.REF_WEAK );
//      desc.activateHandler();
    }
  }

  public IViewDescriptor find( final String id ) {
    Iterator itr = views.iterator();
    IViewDescriptor result = null;
    while( result == null && itr.hasNext() ) {
      IViewDescriptor desc = ( IViewDescriptor )itr.next();
      if( id.equals( desc.getId() ) ) {
        result = desc;
      }
    }
    return result;
  }

  /**
   * Get the list of view categories.
   */
  public IViewCategory[] getCategories() {
  	mapViewsToCategories();
    int nSize = categories.size();
    IViewCategory[] retArray = new IViewCategory[nSize];
    int i = 0;
    for (Iterator itr = categories.iterator(); itr.hasNext();) {
        retArray[i++] = new ViewCategoryProxy((Category) itr.next());
    }
    return retArray;
  }

  public IStickyViewDescriptor[] getStickyViews() {
    throw new UnsupportedOperationException();
  }

  public IViewDescriptor[] getViews() {
	  return (IViewDescriptor []) views.toArray(new IViewDescriptor [views.size()]);
  }
  
  /**
   * Find a category with a given name.
   * 
   * @param id the id to search for
   * @return the category or <code>null</code>
   */
  public IViewCategory findCategory(String id) {
  	mapViewsToCategories();
      Category category = internalFindCategory(id);
      if (category == null) {
			return null;
		}
      return new ViewCategoryProxy(category);
  }
  
  /**
   * Adds each view in the registry to a particular category.
   * The view category may be defined in xml.  If not, the view is
   * added to the "misc" category.
   */
  public void mapViewsToCategories() {
  	if (dirtyViewCategoryMappings) {
  		dirtyViewCategoryMappings = false;
	    	// clear all category mappings
	    	for (Iterator i = categories.iterator(); i.hasNext(); ) {
	    		Category category = (Category) i.next();
	    		category.clear(); // this is bad    		
	    	}
	    	
	    	if (miscCategory != null) {
	    		miscCategory.clear();
	    	}
	    	
	    	for (Iterator i = views.iterator(); i.hasNext(); ) {
	            IViewDescriptor desc = (IViewDescriptor) i.next();
	            Category cat = null;
	            String[] catPath = desc.getCategoryPath();
	            if (catPath != null) {
	                String rootCat = catPath[0];
	                cat = internalFindCategory(rootCat);
	            }
	            if (cat != null) {
	                if (!cat.hasElement(desc)) {
	                    cat.addElement(desc);
	                }
	            } else {
	                if (miscCategory == null) {
	                    miscCategory = new Category();
	                    add(miscCategory);                    
	                }
	                if (catPath != null) {
	                    // If we get here, this view specified a category which
	                    // does not exist. Add this view to the 'Other' category
	                    // but give out a message (to the log only) indicating 
	                    // this has been done.
	                    String fmt = "Category {0} not found for view {1}.  This view added to ''{2}'' category."; //$NON-NLS-1$
	                    WorkbenchPlugin.log(MessageFormat
	                            .format(fmt, new Object[] { catPath[0],
	                                    desc.getId(), miscCategory.getLabel() }));
	                }
	                miscCategory.addElement(desc);
	            }
	        }	        
  	}
  }
  
  /**
   * Returns the category with no updating of the view/category mappings.
   *
	 * @param id the category id
	 * @return the Category
   * @since 3.1
	 */
	private Category internalFindCategory(String id) {
		Iterator itr = categories.iterator();
      while (itr.hasNext()) {
          Category cat = (Category) itr.next();
          if (id.equals(cat.getRootPath())) {
              return cat;
          }
      }
      return null;
  }
	
	/**
	 * Add a category to the registry.
	 * 
	 * @param desc the descriptor to add
	 */
	public void add(Category desc) {
		/* fix for 1877 */
		if (internalFindCategory(desc.getId()) == null) {
			dirtyViewCategoryMappings = true;
			// Mark categories list as dirty
			categories.add(desc);
			IConfigurationElement element = (IConfigurationElement) desc.getAdapter(IConfigurationElement.class);
			if (element == null) {
				return;
			}
//			PlatformUI.getWorkbench().getExtensionTracker()
//			.registerObject(
//					element.getDeclaringExtension(),
//					desc,
//					IExtensionTracker.REF_WEAK);
		}
	}
	
    /**
     * Returns the Misc category. This may be <code>null</code> if there are
     * no miscellaneous views.
     * 
     * @return the misc category or <code>null</code>
     */
    public Category getMiscCategory() {
        return miscCategory;
    }
}
