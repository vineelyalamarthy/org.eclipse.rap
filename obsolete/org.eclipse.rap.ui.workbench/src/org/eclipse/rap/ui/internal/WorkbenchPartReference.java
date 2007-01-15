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

package org.eclipse.rap.ui.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.util.NLS;
import org.eclipse.rap.jface.util.Assert;
import org.eclipse.rap.ui.*;


public abstract class WorkbenchPartReference
  implements IWorkbenchPartReference
{
  public static int STATE_LAZY = 0;
  public static int STATE_CREATION_IN_PROGRESS = 1;
  public static int STATE_CREATED = 2;
  public static int STATE_DISPOSED = 3;

  private int state = STATE_LAZY;
  private String id;
  private String partName;
  protected PartPane pane;
  protected IWorkbenchPart part;

  public final PartPane getPane() {
    if( pane == null ) {
      pane = createPane();
    }
    return pane;
  }
  
  // TODO: [fappel] missing parameters
  public void init( final String id, final String partName ) {
    Assert.isNotNull( id );
    Assert.isNotNull( partName );
    
    this.id = id;
    this.partName = partName;
  }

  public boolean getVisible() {
//    if( isDisposed() ) {
//      return false;
//    }
    return getPane().getVisible();
  }

  public void setVisible( boolean isVisible ) {
//    if( isDisposed() ) {
//      return;
//    }
    getPane().setVisible( isVisible );
  }

  
  protected abstract PartPane createPane();
  protected abstract IWorkbenchPart createPart();
  
  ////////////////////////////////////
  // Interface IWorkbenchPartReference
  
  public String getId() {
    return id;
  }

  public IWorkbenchPart getPart( boolean restore ) {
//    if( isDisposed() ) {
//      return null;
//    }
    if( part == null && restore ) {
      if( state == STATE_CREATION_IN_PROGRESS ) {
        IStatus result = Activator.getStatus( new PartInitException( NLS.bind( "Warning: Detected recursive attempt by part {0} to create itself (this is probably, but not necessarily, a bug)", //$NON-NLS-1$
                                                                                     getId() ) ) );
//        Activator.log( result );
        return null;
      }
      try {
        state = STATE_CREATION_IN_PROGRESS;
        IWorkbenchPart newPart = createPart();
        if( newPart != null ) {
          part = newPart;
          // Add a dispose listener to the part. This dispose listener does
          // nothing but log an exception
          // if the part's widgets get disposed unexpectedly. The workbench part
          // reference is the only
          // object that should dispose this control, and it will remove the
          // listener before it does so.
//          getPane().getControl().addDisposeListener( prematureDisposeListener );
//          part.addPropertyListener( propertyChangeListener );
//          refreshFromPart();
//          releaseReferences();
//          fireInternalPropertyChange( INTERNAL_PROPERTY_OPENED );
        }
      } finally {
        state = STATE_CREATED;
      }
    }
    return part;
  }

  public String getPartName() {
    return partName;
  }
}
