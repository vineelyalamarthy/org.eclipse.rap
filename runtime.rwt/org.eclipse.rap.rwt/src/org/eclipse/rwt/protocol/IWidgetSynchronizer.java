/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.rwt.protocol;


/**
 * A {@link IWidgetSynchronizer} acts as a broker between server and client widgets.
 * It provides helper methods to transfer changes from the server to the client 
 * side widgets. A Broker is unique per widget and should always be instantiated 
 * via the {@link WidgetSynchronizerFactory}.
 *
 * @see WidgetSynchronizerFactory
 * 
 * @since 1.4
 */
public interface IWidgetSynchronizer {

  /**
   * Creates a new widget on the client-side by creating an instance of the
   * corresponding client class defined by the widget's class name. This
   * is normally done in the <code>renderInitialization</code> method of the
   * widgets life-cycle adapter (LCA).
   * @param styles TODO
   * 
   * @since 1.4
   */
  public void newWidget( final String[] styles);

  /**
   * Creates a new widget on the client-side by creating an instance of the
   * corresponding client class defined by the widget's class name. This
   * is normally done in the <code>renderInitialization</code> method of the
   * widgets life-cycle adapter (LCA).
   * @param styles TODO
   * @param arguments the arguments which are passed to the client 
   * widget's constructor.
   * 
   * @since 1.4
   */
  public void newWidget( final String[] styles, final Object[] arguments );

  /**
   * DisposeWidget is used to dispose of the widget of this 
   * {@link IWidgetSynchronizer} on the client side.
   * 
   * @since 1.4
   * 
   */
  public void disposeWidget();

  /**
   * Sets the specified property of the client-side widget to a new value.
   *
   * @param name the attributes name on the client widget.
   * @param value the new value of the property.
   *
   * @since 1.4
   */
  public void setWidgetProperty( final String name, final int value );

  /**
   * Sets the specified property of the client-side widget to a new value.
   *
   * @param name the attributes name on the client widget.
   * @param value the new value of the property.
   *
   * @since 1.4
   */
  public void setWidgetProperty( final String name, final double value );

  /**
   * Sets the specified property of the client-side widget to a new value.
   *
   * @param name the attributes name on the client widget.
   * @param value the new value of the property.
   *
   * @since 1.4
   */
  public void setWidgetProperty( final String name, final boolean value );

  /**
   * Sets the specified property of the client-side widget to a new value.
   *
   * @param name the attributes name on the client widget.
   * @param value the new value of the property.
   *
   * @since 1.4
   */
  public void setWidgetProperty( final String name, final String value );

  /**
   * Sets the specified property of the client-side widget to a new value.
   *
   * @param name the attributes name on the client widget.
   * @param value the new value of the property.
   *
   * @since 1.4
   */
  public void setWidgetProperty( final String name, final Object value );

  /**
   * This will add a listener to the client side widget of 
   * this {@link IWidgetSynchronizer}. 
   *
   * @param listenerName the name of the event the client widget should 
   * listen to.
   *
   * @since 1.4
   */
  public void addListener( final String listenerName );

  /**
   * This will remove a listener from the client side widget of 
   * this {@link IWidgetSynchronizer}. 
   *
   * @param listenerName the name of the event the client widget should no 
   * longer listen to.
   *
   * @since 1.4
   */
  public void removeListener( final String listenerName );

  /**
   * Calls a specific method of the widget on the client-side.
   *
   * @param methodName the method name.
   *
   * @since 1.4
   */
  public void call( final String methodName );

  /**
   * Calls a specific method of the widget on the client-side.
   *
   * @param methodName the method name.
   * @param arguments the arguments which are passed to the specified method.
   *
   * @since 1.4
   */
  public void call( final String methodName, final Object[] arguments );

  /**
   * Executes a script on the client side.
   *
   * @param scriptType the type of the script, value should be something 
   * like "text/javascript".
   * @param script the content of the script which will be executed.
   *
   * @since 1.4
   */
  public void executeScript( final String scriptType, final String script );
  
}