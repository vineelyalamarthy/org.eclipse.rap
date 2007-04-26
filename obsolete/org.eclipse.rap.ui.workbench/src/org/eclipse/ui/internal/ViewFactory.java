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

package org.eclipse.ui.internal;

import java.util.List;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;


public class ViewFactory {
  
  public final static String ID_SEP = ":"; 

  private final IViewRegistry viewRegistry;
  final WorkbenchPage page;
  private final ReferenceCounter counter;
  
  public ViewFactory( final WorkbenchPage page,
                      final IViewRegistry viewRegistry )
  {
    this.page = page;
    this.viewRegistry = viewRegistry;
    this.counter = new ReferenceCounter();
  }
  
  public IViewRegistry getViewRegistry() {
    return viewRegistry;
  }

  public IViewReference[] getViews() {
    List list = counter.values();
    IViewReference[] array = new IViewReference[ list.size() ];
    list.toArray( array );
    return array;
  }

  public WorkbenchPage getWorkbenchPage() {
    return page;
  }

  public IViewReference createView( String id, String secondaryId )
    throws PartInitException
  {
    IViewDescriptor desc = viewRegistry.find( id );
    // ensure that the view id is valid
    if( desc == null ) {
//      throw new PartInitException( NLS.bind( WorkbenchMessages.ViewFactory_couldNotCreate,
//                                             id ) );
      throw new PartInitException( "Could not create view: " + id );
    }
    // ensure that multiple instances are allowed if a secondary id is given
    if( secondaryId != null ) {
      if( !desc.getAllowMultiple() ) {
        //        throw new PartInitException( NLS.bind( WorkbenchMessages.ViewFactory_noMultiple,
//                                               id ) );
        String msg = "Multiple view instances are not allowed for " + id;
        throw new PartInitException( msg );
      }
    }
    String key = getKey( id, secondaryId );
    IViewReference ref = ( IViewReference )counter.get( key );
    if( ref == null ) {
//      IMemento memento = ( IMemento )mementoTable.get( key );
//      ref = new ViewReference( this, id, secondaryId, memento );
      ref = new ViewReference( this, id, secondaryId );
//      mementoTable.remove( key );
      counter.put( key, ref );
      getWorkbenchPage().partAdded( ( ViewReference )ref );
    } else {
      counter.addRef( key );
    }
    return ref;
  }
  
  static String extractPrimaryId( final String compoundId ) {
    String result = compoundId;
    int pos = compoundId.lastIndexOf( ID_SEP );
    if( pos != -1 ) {
      result = compoundId.substring( 0, pos );
    }
    return result;
  }

  static String extractSecondaryId( final String compoundId ) {
    String result = null;
    int pos = compoundId.lastIndexOf( ID_SEP );
    if( pos != -1 ) {
      result = compoundId.substring( pos + 1 );
    }
    return result;
  }
  
  static String getKey( final String id, final String secondaryId ) {
    return secondaryId == null ? id : id + ID_SEP + secondaryId;
  }

  static String getKey( final IViewReference viewRef ) {
    return getKey( viewRef.getId(), viewRef.getSecondaryId() );
  }
}
