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

import org.eclipse.rwt.Adaptable;

import com.w4t.Validator;

/** 
 * <p>An instance of this class describes a validation that occured.</p>
 * <p>This class is not intended to be instantiated or extended by clients.</p>
 * @see org.eclipse.rwt.event.ValidationListener
 */
public class ValidationEvent extends WebEvent {

  private static final Class LISTENER = ValidationListener.class;
  /** Marks the first integer id for the range of validation event ids. */
  public static final int VALIDATION_FIRST = 11001;
  /** Marks the last integer id for the range of validation event ids. */
  public static final int VALIDATION_LAST = 11001;
  /** event type for validation events. */
  public static final int VALIDATED = VALIDATION_FIRST;

  protected Validator validator;
  protected boolean result;


  /** 
   * <p>Creates a new instance of this class.</p>
   * @param source the source WebComponent at which the event occured, must 
   * not be <code>null</code>
   * @param id the type of the event
   * @param validator the Validator that was used for the validation
   * @param result contains the result of the validation
   */
  public ValidationEvent( final Object source,
                          final int id,
                          final Validator validator,
                          final boolean result ) {
    this( source, id, result );
    this.validator = validator;
  }

  /** 
   * <p>Creates a new instance of this class.</p>
   * @param source the source WebComponent at which the event occured, must 
   * not be <code>null</code>
   * @param id the type of the event
   * @param result contains the result of the validation
   */
  public ValidationEvent( final Object source,
                          final int id,
                          final boolean result ) {
    super( source, id );
    this.result = result;
  }
  
  
  /** 
   * <p>Returns the <code>Validator</code> instance that was used for the 
   * validation.</p>
   */
  public Validator getValidator() {
    return validator;
  }

  /**
   * <p>Returns whether a <code>Validator</code> exists for this event.</p>
   */
  // TODO [rh] remove this?!?!
  public boolean hasValidator() {
    return validator != null;
  }

  /**
   * <p>Returns the result of the validation that was performed when this
   * ValidationeEvent occured.</p>
   */
  public boolean getResult() {
    return result;
  }

  protected void dispatchToObserver( final Object listener ) {
    ( ( ValidationListener )listener ).validated( this );
  }

  protected Class getListenerType() {
    return LISTENER;
  }
  
  /**
   * <p>Returns whether the given <code>adaptable</code> has registered any 
   * listeners of type <code>ValidationListener</code>.</p>
   * @see WebEvent#hasListener(Adaptable, Class)
   */
  public static boolean hasListener( final Adaptable adaptable ) {
    return hasListener( adaptable, LISTENER );
  }

  /**
   * <p>Adds the given <code>listener</code> to the <code>adatable</code> that
   * will be notified when events of the class that defines this method occur.
   * </p>
   * <p>This method is only intended to be called by the components' event 
   * registration code.</p>
   * @see WebEvent#addListener(Adaptable, Class, Object)
   */
  public static void addListener( final Adaptable adaptable, 
                                  final ValidationListener listener )
  {
    addListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Removes the given <code>listener</code> from the given
   * <code>adaptable</code>.</p>
   * <p>This method is only intended to be called by the components' event 
   * deregistration code.</p>
   * @param adaptable the adaptable on which the listener is registered, must 
   * not be <code>null</code>.
   * @param listener the listener to be removed, must not be <code>null</code>.
   * @throws NullPointerException when <code>listener</code> or 
   * <code>adaptable</code> is <code>null</code> 
   * @see WebEvent#removeListener(Adaptable, Class, Object)
   */
  public static void removeListener( final Adaptable adaptable,
                                     final ValidationListener listener )
  {
    removeListener( adaptable, LISTENER, listener );
  }

  /**
   * <p>Returns all <code>ValidationListener</code>s for the given 
   * <code>adaptable</code>. An empty array is returned if no listeners 
   * are registered.</p> 
   * @param adaptable the adaptable for which the listneners should be returned.
   * Must not be <code>null</code>.
   */
  public static Object[] getListeners( final Adaptable adaptable ) {
    return getListener( adaptable, LISTENER );
  }
}