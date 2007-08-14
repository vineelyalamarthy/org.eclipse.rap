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
package com.w4t;

import java.beans.DesignMode;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.event.EventAdapter;
import org.eclipse.rwt.internal.event.IEventAdapter;

import com.w4t.developer.PanelCreator;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;
import com.w4t.util.WebComponentCounter;
import com.w4t.util.WebComponentRegistry;

/** 
 * <p>The abstract superclass of all WebComponents.</p>
 * 
 * <p>A WebComponent is an object having a html representation that can be
 * used in an html document and that can interact with the user.</p>
 * 
 * <p>Examples of WebComponent are the WebButtons, WebCheckBoxes, and
 * WebTexts of a typical graphical user interface.</p>
 */
public abstract class WebComponent
  extends WebObject
  implements DesignMode, Adaptable
{  
  
  /** <p>every WebComponent has an internal unique idendentifier, which is
    * set at construction time, see {@link #getUniqueID() getUniqueID()}.</p> */
  private String uniqueId = "";
  /** reference to the WebContainer, which contains this WebComponent
   *  {@link #getParent getParent} */
  protected WebContainer parent = null;
  /** used in design time for code generation as 
   *  variable name for this component */
  protected String name = "";
  /** <p>whether this WebComponent is enabled (reacts on user input)
   * or not. A WebComponent is also not enabled if it is added to a
   * WebContainer and the WebContainer to which it is added is
   * disabled.</p> */
  private boolean enabled = true;
  /** <p>flag: tells, whether this component is rendered on the
   * WebForm.</p> */
  private boolean visible = true;
  /** <p>the event semantics that was associated with this WebComponent
   * in a w4t application.</p>
   * <p>An event semantics identifies a special purpose of an event firing
   * that may occur on various Components in the application. A component
   * may follow various event semantics at once (ids must be bitwise
   * or-combined in order to achieve this.</p>
   * <p>Event semantics ids must be defined by the application. Value 0
   * signals a null semantics.</p> */
  private int eventSemanticsID = 0;

  private IEventAdapter eventAdapter;
  

  // design time specific fields
  //////////////////////////////
  
  /** A value of true denotes that this WebComponent should behave in design
   *  time mode, a value of false denotes runtime behavior. */
  protected boolean designTime = false;

  
  /** Constructor. */
  protected WebComponent() {
    uniqueId = WebComponentCounter.getInstance().getNewID();
    WebComponentRegistry.getInstance().add( this );
    if( this instanceof Concealer ) {
      this.designTime =  false;
    }
  }
  
  /** returns a clone of this WebComponent.<br>
   * For all elementary WebComponents, cloning will result in a deep copy,
   * shallow copying all fields, cloning property objects (i.e. all
   * subclasses of WebComponentProperties, like Style, WindowProperties
   * etc.), and in case the WebObject to clone is a WebContainer, also
   * cloning all WebComponents, if they were added to it using
   * WebContainer.add(), and the WebLayout, if it was set using
   * WebContainer.setWebLayout(). Fields containing external references to
   * Objects which are not added with one of those methods are set to null
   * in the cloned object.
   */
  public Object clone() throws CloneNotSupportedException {
    WebComponent clone = ( WebComponent )super.clone();
    
    /* we must perform a separate init (get an ID, set unused) for the clone,
       since no constructors are called in Object.clone() */
    clone.uniqueId = WebComponentCounter.getInstance().getNewID();
    WebComponentRegistry.getInstance().add( clone );
    return clone;
  }
  
  /**
   * <p>Adds the specified WebRenderListener to receive render events from
   * this WebComponent. Render events occur before and/or after this
   * WebComponent is rendered (note that WebComponents are not rendered if
   * the visible attribute is set to false; therefore no render event occurs).
   * </p>
   * 
   * @param listener the WebRenderListener to add
   */
  public void addWebRenderListener( final WebRenderListener listener ) {
    WebRenderEvent.addListener( this, listener );
  }
  
  /**
   * <p>Removes the specified WebRenderListener so that it no longer
   * receives render events from this WebComponent. Render events occur before
   * and/or after this WebComponent is rendered (note that WebComponents are
   * not rendered if the visible attribute is set to false; therefore no
   * render event occurs).</p>
   * 
   * @param listener the WebRenderListener to remove
   */
  public void removeWebRenderListener( final WebRenderListener listener ) {
    WebRenderEvent.removeListener( this, listener );
  }
  
  /** <p>returns a reference to the {@link org.eclipse.rwt.WebContainer WebContainer}
    * that contains this WebComponent.</p>
    * 
    * @return the parent {@link org.eclipse.rwt.WebContainer WebContainer} 
    *         of this component
    */
  public WebContainer getParent() {
    return parent;
  }
    
  /**
   *  <p>Returns the <code>WebForm</code> that defines the root of the
   *  component-tree to which this component belongs to. Might be null, 
   *  if the component or one of its predecessors has no parent.</p>
   */
  public WebForm getWebForm() {
    WebForm result = null;
    if( this instanceof WebForm ) {
      result = ( WebForm )this;
    } else if ( parent != null ) {
      result = parent.getWebForm();
    }
    return result;
  }
  
  /** Sets the "value" of the "designTime" property.
    * @param designTime sets the current "value" of the "designTime" property.
    */
  public void setDesignTime( final boolean designTime ){
    if(    !( this instanceof Concealer ) 
        || (    ( getParent() != null ) 
             && ( getParent() instanceof PanelCreator ) ) ) 
    {           
      this.designTime = designTime;
    }
  }
  
  /** <p>A value of true denotes that this WebComponent should behave in 
    * design time mode, a value of false denotes runtime behavior.</p>
    * 
    * @return the current value of the designTime property.
    */
  public boolean isDesignTime() {
    return designTime;
  }
  
  /** <p>sets the name of this WebComponent.</p> 
    * 
    * <p>The passed String is ignored, if it is not a valid Java identifier
    * </p>
    * 
    * <p>Note that this name is not necessarily a unique identifier for
    * this WebComponent. It can be set freely and might change at runtime.
    * It is primarily used by development tools that inspect component 
    * trees.</p>
    * 
    * <p>If you need a unique id for this WebComponent, use 
    * {@link #getUniqueID() getUniqueId()}, which returns an identifier that 
    * is assigned to this WebComponent by the library and is really unique
    * in the application (not just in the session).</p>
    */ 
  public void setName( final String name ) {
    if( !name.equals( "" ) && isCorrectIdentifier( name ) ) {
      this.name = name;
    }
  }

  /** <p>returns the name of this WebComponent.</p> 
    * 
    * <p>Note that this name is not necessarily a unique identifier for
    * this WebComponent. It can be set freely and might change at runtime.
    * It is primarily used by development tools that inspect component 
    * trees.</p>
    * 
    * <p>If you need a unique id for this WebComponent, use 
    * {@link #getUniqueID() getUniqueId()}, which returns an identifier that 
    * is assigned to this WebComponent by the library and is really unique
    * in the application (not just in the session).</p>
    */ 
 public String getName() {
    return name;
  }
  
  /** removes this WebComponent from its parent container */
  public void remove() {
    if( parent != null ) {
      parent.remove( this );
    }
  }
    
  /** <p>sets, whether this WebComponent is enabled (reacts on user input)
   * or not. A webComponent is also not enabled if it is added to a
   * WebContainer and the WebContainer to which it is added is
   * disabled.</p> */
  public void setEnabled( final boolean enabled ) {
    this.enabled = enabled;
  }
  
  /** <p>returns, whether this WebComponent is enabled (reacts on user input)
   * or not. A webComponent is also not enabled if it is added to a
   * WebContainer and the WebContainer to which it is added is
   * disabled.</p> */
  public boolean isEnabled() {
    boolean result = true;
    if( this instanceof WebForm ) {
      result = enabled;
    } else {
      if( enabled ) {
        WebContainer par = getParent();
        result = ( par == null ) ? enabled : par.isEnabled();
      } else {
        result = false;
      }
    }
    return result;
  }
  
  /** <p>sets, if this WebComponent is rendered to the WebForm.</p> */
  public void setVisible( final boolean visible ) {
    this.visible = visible;
  }
  
  /** <p>returns, whether this WebComponent is rendered to the
   * WebForm.</p> */
  public boolean isVisible() {
    boolean result = true;
    if( this instanceof WebForm ) {
      result = visible;
    } else {
      if( visible ) {
        WebContainer parent = getParent();
        result = ( parent == null ) ? visible : parent.isVisible();
      } else {
        result = false;
      }
    }
    return result;
  }
  
  /** <p>sets the event semantics that was associated with this WebComponent
   * in a w4t application.</p>
   * <p>An event semantics identifies a special purpose of an event firing
   * that may occur on various Components in the application. A component
   * may follow various event semantics at once (ids must be bitwise
   * or-combined in order to achieve this.</p>
   * <p>Event semantics ids must be defined by the application. Value 0
   * signals a null semantics.</p> */
  public void setEventSemanticsID( final int eventSemanticsID ) {
    this.eventSemanticsID = eventSemanticsID;
  }
  
  /** <p>returns the event semantics that was associated with this WebComponent
   * in a w4t application.</p>
   * <p>An event semantics identifies a special purpose of an event firing
   * that may occur on various Components in the application. A component
   * may follow various event semantics at once (ids must be bitwise
   * or-combined in order to achieve this.</p>
   * <p>Event semantics ids must be defined by the application. Value 0
   * signals a null semantics.</p> */
  public int getEventSemanticsID() {
    return eventSemanticsID;
  }
  
  /** <p>returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "";
  }

  /** <p>returns a unique identifier for this WebComponent.</p> */
  public String getUniqueID() {
    return uniqueId;
  }
  
  public Object getAdapter( final Class adapter ) {
    Object result = null;
    if( adapter == IEventAdapter.class ) {
      ////////////////////////////////////////////////////////
      // Note: This is not implemented via the AdapterManager,
      //       since the manager's mapping mechanism prevents
      //       the component being released unless the session 
      //       is invalidated.
      if( eventAdapter == null ) {
        eventAdapter = new EventAdapter();
      }
      result = eventAdapter;
    } else {
      result = W4TContext.getAdapterManager().getAdapter( this, adapter );
    }
    return result;
  }
  
  
  // helping methods
  //////////////////
  
  private static boolean isCorrectIdentifier( final String name ) {
    boolean result = Character.isJavaIdentifierStart( name.charAt ( 0 ) );
    if( name.length() > 1 ) {
      for( int i = 0; i < name.length(); i++ ) {
        result &= Character.isJavaIdentifierPart( name.charAt( i ) );
      }
    }
    return result;
  }
}