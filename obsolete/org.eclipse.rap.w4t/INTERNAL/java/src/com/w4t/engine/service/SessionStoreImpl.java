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

package com.w4t.engine.service;

import java.util.*;

import javax.servlet.http.*;

import com.w4t.ParamCheck;


class SessionStoreImpl implements ISessionStore, HttpSessionBindingListener {

  static final String ID_SESSION_STORE = SessionStoreImpl.class.getName();
  
  private final Map attributes = new HashMap();
  private final Set listeners = new HashSet();
  private final HttpSession session;
  private boolean bound;
  
  SessionStoreImpl( final HttpSession session ) {
    ParamCheck.notNull( session, "session" );
    this.session = session;
    this.session.setAttribute( ID_SESSION_STORE, this );
    bound = true;
  }
  
  public Object getAttribute( final String name ) {
    checkBound();
    return attributes.get( name );
  }

  public void setAttribute( final String name, final Object value ) {
    checkBound();
    if( value == null ) {
      removeAttribute( name );
    } else {
      if( attributes.containsKey( name ) ) {
        removeAttribute( name );
      }
      attributes.put( name, value );
      fireValueBound( name, value );
    }
  }
  
  public void removeAttribute( final String name ) {
    checkBound();
    Object removed = attributes.remove( name );
    fireValueUnbound( name, removed );
  }

  public Enumeration getAttributeNames() {
    checkBound();
    final Iterator iterator = attributes.keySet().iterator();
    return new Enumeration() {
      public boolean hasMoreElements() {
        return iterator.hasNext();
      }
      public Object nextElement() {
        return iterator.next();
      }
    };
  }
  
  public String getId() {
    return session.getId();
  }
  
  public HttpSession getHttpSession() {
    return session;
  }
  
  boolean isBound() {
    return bound;
  }

  public void addSessionStoreListener( final SessionStoreListener lsnr ) {
    checkBound();
    listeners.add( lsnr );
  }

  public void removeSessionStoreListener( final SessionStoreListener lsnr ) {
    checkBound();
    listeners.remove( lsnr );
  }

  
  ///////////////////////////////////////
  // interface HttpSessionBindingListener
  
  public void valueBound( final HttpSessionBindingEvent event ) {
    bound = true;
  }
  
  public void valueUnbound( final HttpSessionBindingEvent event ) {
    Object[] lsnrs = listeners.toArray();
    SessionStoreEvent evt = new SessionStoreEvent( this );
    for( int i = 0; i < lsnrs.length; i++ ) {
      ( ( SessionStoreListener )lsnrs[ i ] ).beforeDestroy( evt );
    }
    Object[] names = attributes.keySet().toArray();
    for( int i = 0; i < names.length; i++ ) {
      removeAttribute( ( String )names[ i ] );
    }
    bound = false;
  }
  
  
  //////////////////
  // helping methods
  
  private void checkBound() {
    if( !bound ) {
      throw new IllegalStateException( "The session store has been unbound." );
    }
  }
    
  private void fireValueBound( final String name, final Object value ) {
    if( value instanceof HttpSessionBindingListener ) {
      HttpSessionBindingListener listener
      = ( HttpSessionBindingListener )value;
      HttpSessionBindingEvent evt 
      = new HttpSessionBindingEvent( session, name, value );
      listener.valueBound( evt );
    }
  }
  
  private void fireValueUnbound( final String name, Object removed ) {
    if( removed instanceof HttpSessionBindingListener ) {
      HttpSessionBindingListener listener
      = ( HttpSessionBindingListener )removed;
      HttpSessionBindingEvent evt 
        = new HttpSessionBindingEvent( session, name, removed );
      listener.valueUnbound( evt );
    }
  }
}
