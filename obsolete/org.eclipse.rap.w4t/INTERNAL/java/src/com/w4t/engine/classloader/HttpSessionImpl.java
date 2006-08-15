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
package com.w4t.engine.classloader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.servlet.http.*;


/** 
 * A simple session wrapper used in the preloading mode of the W4 Toolkit
 * library for compatibility reasons.
 */
public class HttpSessionImpl implements HttpSession, 
                                        HttpSessionBindingListener {

  /** The number of random bytes to include when generating 
   *  a session identifier. */
  private final static int SESSION_ID_SIZE = 16;
  /** The message digest algorithm to use. */
  private final static String DIGEST_ALGORITHM = "MD5";  
  
  /** Random instance used by the creation of the session id. */
  private static Random random;
  /** the MessageDigest implementation to be used when creating 
   *  session identifiers.*/
  private static MessageDigest digest;  
  /** the session id of this session. */
  private String id;
  /** the internal datastructure used for buffering the Objects added to
   *  this session implementation. */  
  private Hashtable attributes;
  
  private long creationTime;
  private long lastAccessTime;
  private int maxInactiveInterval;
  
  private HttpSession serverSession;
  
  /** static initialisations for session id generation. */
  static {
    initRandom();
    initMessageDigest();
  }
 

  /** constructor */
  public HttpSessionImpl() {
    creationTime = System.currentTimeMillis();
    maxInactiveInterval = -1;
    attributes = new Hashtable();
    id = generateSessionId();
  }

  public Object getAttribute( final String key ) {
    return attributes.get( key );
  }

  public Enumeration getAttributeNames() {
    return attributes.keys();
  }

  public long getCreationTime() {
    return creationTime;
  }

  public String getId() {
    return id;
  }

  public long getLastAccessedTime() {
    return lastAccessTime;
  }

  public int getMaxInactiveInterval() {
    return maxInactiveInterval;
  }

  public HttpSessionContext getSessionContext() {
    return serverSession == null ? null : serverSession.getSessionContext();
  }

  public Object getValue( final String key ) {
    return attributes.get( key );
  }

  public String[] getValueNames() {
    String[] result = new String[ attributes.size() ];
    int i = 0;
    for( Enumeration keys = attributes.keys(); keys.hasMoreElements(); i++ ) {
      result[ i ] = ( String )keys.nextElement();
    }
    Arrays.sort( result );
    return result;
  }

  public void invalidate() {
    String[] names = getValueNames();
    for( int i = 0; i < names.length; i++ ) {
      removeAttribute( names[ i ] );
    }
    attributes.clear();
    lastAccessTime = creationTime;
    
// [fappel]: I Think this causes problems if the server removes each
// attribute of the server session before calling invalidate,
// because if this session as attribute of the serversession will
// be removed itself, this will cause a call of this invalidate
// methods. Afterwards removal of attributes of the server session
// may fail with an illegal state exception.
// 
//    if( serverSession != null ) {
//      try {
//        serverSession.invalidate();
//      } catch( RuntimeException ignore ) {
//        // ensure that the wrapped session was unbound
//        // therefore we need no exception handling here
//      }
//    }
  }
  
  public boolean isNew() {
    return lastAccessTime == creationTime;
  }

  public void putValue( final String key, final Object value ) {
    setAttribute( key, value );
  }

  public void removeAttribute( final String key ) {
    Object value = attributes.remove( key );
    if( value instanceof HttpSessionBindingListener ) {
      HttpSessionBindingEvent evt = new HttpSessionBindingEvent( this, key );
      ( ( HttpSessionBindingListener )value ).valueUnbound( evt );
    }
  }

  public void removeValue( final String key ) {
    removeAttribute( key );
  }

  public void setAttribute( final String key, final Object value ) {
    if( value instanceof HttpSessionBindingListener ) {
      HttpSessionBindingEvent evt = new HttpSessionBindingEvent( this, key );
      ( ( HttpSessionBindingListener )value ).valueBound( evt );
    }
    attributes.put( key, value );
  }

  public void setMaxInactiveInterval( int parm1 ) {
    maxInactiveInterval = parm1;
  }

  protected void setLastAccessTime( long lastAccessTime ) {
    this.lastAccessTime = lastAccessTime;
  }

  public javax.servlet.ServletContext getServletContext() {
    // FIXME [rh] Revise this: There was a StackOverflowError when run from
    //       EngineAdapter_Test. Is this the right solution?
    // return serverSession == null ? null : serverSession.getServletContext();    
    return serverSession == null || serverSession == this 
           ? null 
           : serverSession.getServletContext();    
  }
  
  //////////////////////////////////////
  // HttpBindingListener implementations
  
  public void valueBound( final HttpSessionBindingEvent evt ) {
    serverSession = evt.getSession();
  }
  
  public void valueUnbound( final HttpSessionBindingEvent evt ) {
    this.invalidate();
  }
  
  
  /////////////////////////////
  // session ID generation
  
  /** Generate and return a new session identifier. */
  private static String generateSessionId() {
    // Generate a byte array containing a session identifier
    byte bytes[] = new byte[ SESSION_ID_SIZE ];
    random.nextBytes( bytes );
    bytes = digest.digest( bytes );

    // Render the result as a String of hexadecimal digits
    StringBuffer result = new StringBuffer();
    for( int i = 0; i < bytes.length; i++ ) {
      byte tetrade1 = ( byte )( ( bytes[ i ] & 0xf0 ) >> 4 );
      byte tetrade2 = ( byte )( bytes[ i ] & 0x0f );
      appendHexChar( tetrade1, result );
      appendHexChar( tetrade2, result );
    }
    return result.toString();
  }
  
  private static void appendHexChar( final byte tetrade, 
                                     final StringBuffer result ) 
  {
    if( tetrade < 10 ) {
      result.append( ( char )( '0' + tetrade ) );
    } else {
      result.append( ( char )( 'A' + ( tetrade - 10 ) ) );
    }    
  }
  
  /** initialise the random number generator instance we should use for
   *  generating session identifiers.  If there is no such generator
   *  currently defined, construct and seed a new one. */
  private synchronized static void initRandom() {
    if( random == null ) {
      random = new Random();
      random.setSeed( System.currentTimeMillis() );
    }
  }
  
  /** initialise the MessageDigest implementation to be used when creating 
   *  session identifiers.*/
  private synchronized static void initMessageDigest() {
    if( digest == null ) {
      try {
        digest = MessageDigest.getInstance( DIGEST_ALGORITHM );
      } catch( NoSuchAlgorithmException shouldNotHappen ) {
        System.out.println( "Exception creating MessegDigest:\n"
                          + shouldNotHappen );
      }
    }
  }
}