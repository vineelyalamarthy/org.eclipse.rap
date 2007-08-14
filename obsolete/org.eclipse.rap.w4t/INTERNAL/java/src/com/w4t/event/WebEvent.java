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
package com.w4t.event;

import org.eclipse.rwt.internal.event.Event;

import com.w4t.WebComponent;


/** <p>The base class for all WebEvents.</p>
  * <p>The WebEvent has a source Object, which 'fires' the event.
  * Every WebEvent class has its own listener interface. A listener 
  * interface has one or more method declarations - the types of
  * the event. For each of these types an integer constants is defined. 
  * These ids are set as a parameter of the WebEvents constructor.</p>
  * <p>Example implementation of a WebEvent and its listener:</p>
  * <pre>
  * public class MyEvent extends WebEvent {
  * 
  *   public static void addListener( Adaptable adaptable, MyListener listener ) {
  *     addListener( adaptable, MyListener.class, listener );
  *   }
  *   
  *   public static void removeListener( Adaptable adaptable, MyListener listener ) {
  *     removeListener( adaptable, MyListener.class, listener );
  *   }
  *   
  *   public static Object[] getListeners( Adaptable adaptable ) {
  *     return getListener( adaptable, MyListener.class );
  *   }
  *    
  *   public MyEvent( Object source, int id ) {
  *     super( source, id );
  *   }
  *   
  *   protected void dispatchToObserver( Object listener ) {
  *     ( ( MyListener )listener ).eventOccured( this );
  *   }
  *   
  *   protected Class getListenerType() {
  *     return MyListener.class;
  *   }
  * }
  * 
  * public interface MyListener {
  *   void eventOccured( MyEvent event ); 
  * }
  * </pre>
  * <p>The static <code>add/removeListener</code> methods should be implemented
  * in order be called from the component's <code>add/removeXXXListener</code>
  * methods.</p>
  */
public abstract class WebEvent extends Event {

  /**
    * Constructor
    * @param source the source WebComponent at which the event occured
    * @param id the type of the event
    */
  public WebEvent( final Object source, final int id ) {
    super( source, id );
  }
  
  /** <p>returns the source (as WebComponent) on which this event occured,
    * if it is of type WebComponent, or null else.</p> */
  public WebComponent getSourceComponent() {
    WebComponent result = null;
    if( getSource() instanceof WebComponent ) {
      result = ( WebComponent )getSource();
    }
    return result;
  }

  /** <p>returns the ID for the EventSemantics that was associated in an
    * application with the source of this WebEvent.</p> */
  public int getEventSemanticsID() {
    int result = 0;

    WebComponent wcSource = getSourceComponent();
    if( wcSource != null ) {
      result = wcSource.getEventSemanticsID();
    }

    return result;
  }

  /** <p>returns whether the passed ID for an EventSemantics was associated
    * in an application with the source of this <code>WebEvent</code>.</p> */
  public boolean hasSemantics( final int eventSemanticsId ) {
    boolean result = false;
    WebComponent wcSource = getSourceComponent();
    if( wcSource != null ) {
      int sourceSemanticsId = ( wcSource.getEventSemanticsID() );
      result = ( ( sourceSemanticsId & eventSemanticsId ) == eventSemanticsId );
    }
    return result;
  }
}