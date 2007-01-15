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

package org.eclipse.rap.ui.internal.registry;

import java.util.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.rap.ui.PlatformUI;
import org.eclipse.rap.ui.views.*;
import com.w4t.SessionSingletonBase;


public class ViewRegistry
  extends SessionSingletonBase
  implements IViewRegistry
{
  
  private ViewRegistryReader reader = new ViewRegistryReader();
  
  
  private final SortedSet views = new TreeSet( new Comparator() {
    public int compare( Object o1, Object o2 ) {
      String id1 = ( ( ViewDescriptor )o1 ).getId();
      String id2 = ( ( ViewDescriptor )o2 ).getId();
      return id1.compareTo( id2 );
    }
  } );
  
  private ViewRegistry() {
    reader.readViews( Platform.getExtensionRegistry(), this );
  }
  
  
  public static IViewRegistry getInstance() {
    return ( IViewRegistry )getInstance( ViewRegistry.class );
  }

  public void add( final ViewDescriptor desc ) {
    if( views.add( desc ) ) {
//      dirtyViewCategoryMappings = true;
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

  public IViewCategory[] getCategories() {
    throw new UnsupportedOperationException();
  }

  public IStickyViewDescriptor[] getStickyViews() {
    throw new UnsupportedOperationException();
  }

  public IViewDescriptor[] getViews() {
    throw new UnsupportedOperationException();
  }
}
