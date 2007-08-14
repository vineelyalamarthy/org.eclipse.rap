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
package com.w4t.engine.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.rwt.SessionSingletonBase;

import com.w4t.WebForm;


/** <p>encapsulates a special stack for WebForms that is used in 
  * noscript rendering for keeping track of multiple forms opened 
  * in the same window (the substitute for the openInNewWindow in the 
  * javascript version).</p>
  */
public class NoscriptFormStack extends SessionSingletonBase {

  /** <p>the internal data structure for this NoscriptFormStack.</p> */
  private Stack stack;
  
    
  /** <p>private constructor in order to ensure the singleton pattern.</p> */
  private NoscriptFormStack() {
    stack = new Stack();
  }
  
  /** <p>returns a reference to the singleton instance of 
    * NoscriptFormStack. </p> */
  public static synchronized NoscriptFormStack getInstance() {
    return ( NoscriptFormStack )getInstance( NoscriptFormStack.class );
  }
  
  
  // stack operations
  ///////////////////
  
  /** <p>returns the WebForm which is on top of this 
    * NoscriptFormStack, also removing it from the stack.</p> */
  public WebForm pop() {
    WeakReference wr = ( WeakReference )stack.pop();
    Object obj = wr.get();
    return ( WebForm )obj;    
  }
  
  /** <p>adds the specified WebForm on top of this 
    * NoscriptFormStack.</p> */
  public void push( final WebForm wf ) {
    WeakReference wr = new WeakReference( wf );
    stack.push( wr );
  }
 
  /** <p>return whether no more WebForms are contained in this 
    * NoscriptFormStack.</p> */
  public boolean isEmpty() {
    return stack.empty();
  }
  
  /** <p>removes the specified WebForm from whereever it is within 
    * this NoscriptFormStack.</p> */
  public synchronized void remove( final WebForm wf ) {
    ArrayList removeList = new ArrayList();
    
    // buffer all empty weak references and all weak references that contain 
    // a reference to the passed wf
    for( int i = 0; i < stack.size(); i++ ) {
      WeakReference wr = ( WeakReference )stack.get( i );
      Object obj = wr.get();
      if( obj != null ) {
        if ( obj == wf ) {
          removeList.add( wr );
        }
      } else {
        removeList.add( wr );
      }
    }
    
    // do the actual remove
    for( int i = 0; i < removeList.size(); i++ ) {
      stack.remove( removeList.get( i ) );
    }
  }
}