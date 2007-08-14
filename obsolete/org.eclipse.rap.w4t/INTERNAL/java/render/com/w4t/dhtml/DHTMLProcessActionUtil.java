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
package com.w4t.dhtml;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.service.ISessionStore;

import com.w4t.NoscriptUtil;
import com.w4t.dhtml.event.*;
import com.w4t.event.WebActionEvent;


public class DHTMLProcessActionUtil {
  
  private static final String DRAG_SOURCE_BUFFER = "w4t_drag_source_buffer";
  
  private DHTMLProcessActionUtil() {
  }
  
  
  public static void processTreeNodeCollapsedScript( final TreeNode node ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String id = request.getParameter( WebTreeNodeCollapsedEvent.FIELD_NAME );
    if( node.getUniqueID().equals( id ) ) {
      doTreeNodeCollapsed( node );
    }    
  }

  public static void processTreeNodeCollapsedNoScript( final TreeNode node ) {
    StringBuffer key = new StringBuffer( WebTreeNodeCollapsedEvent.PREFIX );
    key.append( node.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      doTreeNodeCollapsed( node );
    }    
  }
  
  public static void processTreeNodeExpandedScript( final TreeNode node ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String id = request.getParameter( WebTreeNodeExpandedEvent.FIELD_NAME );
    if( node.getUniqueID().equals( id ) ) {
      doTreeNodeExpanded( node );
    }    
  }
  
  public static void processTreeNodeExpandedNoScript( final TreeNode node ) {
    StringBuffer key = new StringBuffer( WebTreeNodeExpandedEvent.PREFIX );
    key.append( node.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      doTreeNodeExpanded( node );
    }    
  }
  
  public static void processDragDropScript( final Item item ) {
    String destKey = DragDropEvent.FIELD_NAME_DESTINATION;
    String sourceKey = DragDropEvent.FIELD_NAME_SOURCE;
    
    HttpServletRequest request = ContextProvider.getRequest();
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    String sourceId = request.getParameter( sourceKey );
    if( item.getUniqueID().equals( sourceId ) ) {
      Item destination = ( Item )stateInfo.getAttribute( destKey );
      if( destination != null ) {
        handleDragDrop( item, destination );
      } else {
        stateInfo.setAttribute( sourceKey, item );
      }
    }
    
    String destinationId = request.getParameter( destKey );
    if( item.getUniqueID().equals( destinationId ) ) {
      Item source = ( Item )stateInfo.getAttribute( sourceKey );
      if( source != null ) {
        handleDragDrop( source, item );
      } else {
        stateInfo.setAttribute( destKey, item );
      }
    }
  }
  
  public static void processDragDropNoScript( final Item item ) {
    StringBuffer key = new StringBuffer( DragDropEvent.PREFIX );
    key.append( item.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      if( !hasDragSourceBuffer() ) {
        bufferDragSource( item );
      } else {
        if( getDragSourceBuffer() != item ) {
          handleDragDrop( getDragSourceBuffer(), item );
        }
        clearDragSource();
      }
    }
  }
  
  public static void processDoubleClickScript( final Item item ) {
    HttpServletRequest request = ContextProvider.getRequest();
    String paramValue = request.getParameter( DoubleClickEvent.FIELD_NAME );
    if( item.getUniqueID().equals( paramValue ) ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      if(    !stateInfo.getDetectedBrowser().isAjaxEnabled() 
          && WebActionEvent.hasListener( item  ) ) 
      {
        doActionPerformed( item );
      }
      doDoubleClickPerformed( item );
    }    
  }
  
  public static void processDoubleClickNoScript( final Item item ) {
    StringBuffer key = new StringBuffer( DoubleClickEvent.PREFIX );
    key.append( item.getUniqueID() );
    if( NoscriptUtil.isActionSource( key ) ) {
      if( WebActionEvent.hasListener( item  ) ) {
        doActionPerformed( item );
      }
      doDoubleClickPerformed( item );
    }    
  }

  //////////////////
  // helping methods
  
  // TODO: [fappel] move event processing to event class
  private static void handleDragDrop( final Item source, 
                                      final Item destination )
  {
    if( destination instanceof Node ) {
      Node node = ( Node )destination;
      int evtId = DragDropEvent.DRAGDROP;
      DragDropEvent evt = new DragDropEvent( source, node, evtId );
      evt.processEvent();
    }
  }
  
  private static void clearDragSource() {
    ContextProvider.getSession().removeAttribute( DRAG_SOURCE_BUFFER );
  }

  private static Item getDragSourceBuffer() {
    ISessionStore session = ContextProvider.getSession();
    return ( Item )session.getAttribute( DRAG_SOURCE_BUFFER );
  }

  private static void bufferDragSource( final Item source ) {
    ISessionStore session = ContextProvider.getSession();
    session.setAttribute( DRAG_SOURCE_BUFFER, source );
  }

  private static boolean hasDragSourceBuffer() {
    ISessionStore session = ContextProvider.getSession();
    return session.getAttribute( DRAG_SOURCE_BUFFER ) != null;
  }

  private static void doTreeNodeCollapsed( final TreeNode node ) {
    int id = WebTreeNodeCollapsedEvent.TREENODE_COLLAPSED;
    WebTreeNodeCollapsedEvent evt = new WebTreeNodeCollapsedEvent( node, id );
    evt.processEvent();
  }
  
  private static void doTreeNodeExpanded( final TreeNode node )
  {
    int id = WebTreeNodeExpandedEvent.TREENODE_EXPANDED;
    WebTreeNodeExpandedEvent evt = new WebTreeNodeExpandedEvent( node, id );
    evt.processEvent();
  }
  
  private static void doDoubleClickPerformed( final Item item ) {
    DoubleClickEvent event 
      = new DoubleClickEvent( item, DoubleClickEvent.DOUBLE_CLICK_PERFORMED );
    event.processEvent();
  }
  
  private static void doActionPerformed( final Item item ) {
    int evtId = WebActionEvent.ACTION_PERFORMED;
    WebActionEvent evt = new WebActionEvent( item, evtId );
    evt.processEvent();
  }
}