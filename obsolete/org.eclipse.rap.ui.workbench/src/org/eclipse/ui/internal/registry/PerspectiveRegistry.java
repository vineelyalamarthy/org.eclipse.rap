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

import java.util.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.internal.Workbench;
import com.w4t.SessionSingletonBase;


public class PerspectiveRegistry 
  extends SessionSingletonBase
  implements IPerspectiveRegistry
{
  private String defaultPerspId;
  private List perspectives = new ArrayList( 10 );
  
  
  private PerspectiveRegistry() {
    loadPredefined();
    verifyDefaultPerspective();
  
  }
  
  public static IPerspectiveRegistry getInstance() {
    return ( IPerspectiveRegistry )getInstance( PerspectiveRegistry.class );
  }

  public void addPerspective( final PerspectiveDescriptor descriptor ) {
    if( descriptor != null ) {
      add( descriptor );
    }
  }
  
  
  /////////////////////////////////
  // interface IPerspectiveRegistry
  
  public String getDefaultPerspective() {
    return defaultPerspId;
  }
  
  public IPerspectiveDescriptor findPerspectiveWithId( final String id ) {
    IPerspectiveDescriptor result = null;
    Iterator iterator = perspectives.iterator();
    while( result == null && iterator.hasNext() ) {
      PerspectiveDescriptor desc = ( PerspectiveDescriptor )iterator.next();
      if( desc.getId().equals( id ) ) {
        result = desc;
      }
    }
    return result;
  }
  
  
  //////////////////
  // helping methods
  
  private void add( final PerspectiveDescriptor description ) {
    perspectives.add( description );
  }
  
  private void loadPredefined() {
    PerspectiveRegistryReader reader = new PerspectiveRegistryReader( this );
    reader.readPerspectives( Platform.getExtensionRegistry() );
  }
  
  private void verifyDefaultPerspective() {
    defaultPerspId = Workbench.getInstance().getDefaultPerspectiveId();
  }
}
