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

import java.lang.reflect.Field;
import java.util.Vector;

import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.custom.ICustomContainer;
import com.w4t.dhtml.Node;
import com.w4t.event.*;
import com.w4t.internal.simplecomponent.UniversalAttributes;
import com.w4t.util.ComponentTreeVisitor;
import com.w4t.util.ComponentTreeVisitor.AllComponentVisitor;


/** 
 * <p>A WebContainer is a WebComponent, which contains other WebComponents.
 * WebContainers are used for grouping and positioning WebComponents.</p>
 *
 * <p>The components on WebContaines are arranged by layout managers, 
 * which must be subclasses of {@link com.w4t.WebLayout WebLayout}. 
 * The default WebLayout for a WebContainer is the 
 * {@link com.w4t.WebBorderLayout WebBorderLayout}.</p>
 *
 * <p>The WebContainer implements the WebActionListener interface
 * in order to receive WebActionEvents, which it passes to all its added
 * components, and to which WebActionListeners may be added that are then
 * notified about every WebActionEvent that occurs on a component added to
 * this WebContainer.</p>
 */
public abstract class WebContainer 
  extends WebComponent
  implements Validatable, SimpleComponent
{

  /** the default Layout Manager of the WebContainer is the WebBorderLayout
    * ({@link #getWebLayout getWebLayout},
    * {@link #setWebLayout setWeblayout}) */
  private WebLayout webLayout = new WebBorderLayout();
  /** dynamic datastructure for the webComponents, added to this WebContainer
    * the second vector field contains the information of the region
    * of the layout manager to which the component was added */
  private Vector[] webComponentList = new Vector[ 2 ];
  /** counter for the webComponents added to this WebContainer
    * {@link #getWebComponentCount getWebComponentCount} */
  private int webComponentCount = 0;
  /** <p>whether the validation on this WebContainer is active, i.e. 
    * whether validation is actually performed on validate() call.</p> */
  private boolean validationActive = true;  
  /** the universal html attributes encapsulation class */
  private UniversalAttributes universalAttributes; 
  /** <p>if this is an instance of ICustomContainer the layout
   *  could only be changed once.</p>*/
  private boolean hasCustomLayout = false;
  

  /** Creates a new instance of WebContainer. */
  protected WebContainer() {
    super();
    // management for added components
    webComponentList[ 0 ] = new Vector();
    webComponentList[ 1 ] = new Vector();
  }
  
  /** 
   * <p>Returns a clone of this WebComponent.</p>
   * 
   * <p>For all elementary WebComponents, cloning will result in a deep copy,
   * shallow copying all fields, cloning property objects (i.e. all
   * subclasses of WebComponentProperties, like Style, WindowProperties
   * etc.), and in case the WebObject to clone is a WebContainer, also
   * cloning all WebComponents, if they were added to it using
   * WebContainer.add(), and the WebLayout, if it was set using
   * WebContainer.setWebLayout(). Fields containing external references to
   * Objects which are not added with one of those methods are set to null
   * in the cloned object.</p>
   */
  public Object clone() throws CloneNotSupportedException {
    WebContainer result = ( WebContainer )super.clone();

    /* we must perform a separate init for the clone,
       since no constructors are called in Object.clone() */
    result.webComponentList = new Vector[ 2 ];
    result.webComponentList[ 0 ] = new Vector();
    result.webComponentList[ 1 ] = new Vector();
    result.webComponentCount = 0;
    result.universalAttributes = null;
    if( universalAttributes != null ) {
      result.universalAttributes
        = ( UniversalAttributes )universalAttributes.clone();
    }
    result.hasCustomLayout = false;

    /* clone the WebLayout set on this container */
    try {
      WebLayout layout = ( WebLayout )this.getWebLayout().clone();
      result.setWebLayout( layout );
    } catch ( Exception ex ) {
      System.err.println( "Exception occured in 'WebContainer.clone()' "
                          + " cloning and setting the WebLayout. "
                          + ex.toString() );
    }

    /* clone the WebComponents added to this container */
    int numAddedComponents = 0;
    Field[] fields = null;

    try {
      // preparations
      numAddedComponents = this.getWebComponentCount();
      fields = getDeclaredFields();
    } catch ( Exception ex ) {
      System.err.println( "Exception occured in 'WebContainer.clone()' "
                          + " reading the declared fields. "
                          + ex.toString() );
    }

    WebComponent wcI      = null;
    WebComponent wcIClone = null;
    Object       wcIConstraint = null;
    Class webComponentClass = null;
    try {
      webComponentClass =
                    Class.forName( "com.w4t.WebComponent" );
    } catch ( ClassNotFoundException cnfe ) {
      System.err.println( "Exception occured in 'WebContainer.clone()' "
                          + " creating the WebComponent Class object. "
                          + cnfe.toString() );
    }

    for ( int i = 0; i < numAddedComponents; i++ ) {
      // clone the i. added component and add it to the clone of this Container
      try {
        wcI = ( WebComponent )this.webComponentList[ 0 ].get( i );
        wcIClone = ( WebComponent )wcI.clone();
        wcIConstraint = this.webComponentList[ 1 ].get( i );
        result.add( wcIClone, wcIConstraint );
      } catch ( Exception ex ) {
        System.err.println( "Exception occured in 'WebContainer.clone()' "
                            + " cloning and setting the WebLayout. "
                            + ex.toString() );
      }

      // if one of the fields in this WebContainer or in one of its superclasses
      // contains a reference to the added component, this structure is
      // duplicated (the clone's fields is set to the corresponding WebComponent
      // added to the clone
      int length = fields.length;
      for ( int j = 0; j < length; j++ ) {
        try {
          if ( webComponentClass.isAssignableFrom( fields[ j ].getType() ) ) {
            fields[ j ].setAccessible( true );
            if ( ( WebComponent )fields[ j ].get( this ) == wcI ) {
             // set the corresponding field in clone
              fields[ j ].set( result, wcIClone );
            }
          }
        } catch ( Exception ex ) { // IllegalArgumentException
                                   // IllegalAccessException
          System.err.println( "Exception occured in 'WebContainer.clone()' "
                             + " duplicating the fields reference structure. "
                             + ex.toString() );
        }
      } // for fields

    } // for added components

    return result;
  }

  /** 
   * <p>Adds the specified WebComponent to the WebContainer.</p>
   * 
   * @param  webComponent the WebComponent to be added
   */
  public final void add( final WebComponent webComponent ) {
    add( webComponent, null );
  }


  /**
   *  <p>Adds the specified WebComponent to the WebContainer, using 
   * the specified constraint object.</p>
   *
   * @param  componentToAdd the WebComponent to be added
   * @param  constraints  where/how the component is added to the WebLayout.
   */
  public final void add( final WebComponent componentToAdd,
                         final Object constraints )
  {
    ParamCheck.notNull( componentToAdd, "componentToAdd" );
    checkConstraints( constraints );
    checkIfAlreadyAdded( componentToAdd );
    checkIfComponentIsForm( componentToAdd );
    checkSelfContainment( componentToAdd );
    
    webComponentList[ 0 ].add( componentToAdd );
    webComponentList[ 1 ].add( constraints );
    webComponentCount++;
    componentToAdd.parent = this;
    // check if the webComponent is a Decorator
    updateDecoratorOnAdd( componentToAdd );

    // notify the container listener about the adding
    int evtId = WebContainerEvent.COMPONENT_ADDED;
    WebContainerEvent wcEvt 
      = new WebContainerEvent( this, evtId, componentToAdd, constraints );
    wcEvt.processEvent();
  }

  private void checkSelfContainment( final WebComponent componentToAdd ) {
    if( componentToAdd instanceof WebContainer ) {
      for( WebContainer wc = this; wc != null; wc = wc.getParent() ) {
        if( wc == componentToAdd ) {
          String msg =   "Error in 'WebContainer.add()': "
                       + "adding container's parent to itself.";
          throw new IllegalArgumentException( msg );
        }
      }
    }
  }

  private void checkIfComponentIsForm( final WebComponent componentToAdd ) {
    if( componentToAdd instanceof WebForm ) {
      String msg = "A instance of WebForm cannot be added to a WebContainer!";
      String compName = componentToAdd.getName();
      if ( !"".equals( compName ) ) {
        msg += "\nName of the WebForm is '" + compName + "'.";
      }
      throw new IllegalArgumentException( msg );
    }
  }

  private void checkIfAlreadyAdded( final WebComponent componentToAdd ) {
    if( componentToAdd.getParent() != null ) {
      String msg =   "This WebComponent '"
                   + componentToAdd.getClass().getName()
                   + "' is already added to a WebContainer!";
      throw new IllegalArgumentException( msg );
    }
  }

  private void checkConstraints( final Object constraints ) {
    if( !webLayout.checkConstraint( constraints ) ) {
      String constraintsType = constraints == null
                                ? "null"
                                : constraints.getClass().getName();
      String msg =   "Wrong constraint type: '"
                   + constraintsType + "'.\n"
                   + "Use a constraint object of the '"
                   + webLayout.getClass().getName()+ "'";
      throw new IllegalArgumentException( msg );
    }
  }

  private void updateDecoratorOnAdd( final WebComponent webComponent ) {
    if(    webComponent instanceof Decorator
        && ( ( Decorator )webComponent ).getContent() != null ) 
    {
      Decorator decorator = ( Decorator )webComponent;
      decorator.getContent().parent = this;
      updateDecoratorOnAdd( decorator.getContent() );
    }
  }

  /** 
   * Removes the specified WebComponent from the WebContainer.
   * 
   * @param toRemove the WebComponent to removed
   */
  public void remove( final WebComponent toRemove ) {
    Object constraint = null;
    WebComponent child =   Decorator.isDecorated( toRemove ) 
                         ? Decorator.getOuterMostDecorator( toRemove )
                         : toRemove;

    if( webComponentList[ 0 ].contains( child ) ) {
      boolean removed = false;
      if( child instanceof Decorator ) {
        updateDecoratorOnRemove( ( Decorator )child, toRemove );
      }
      if( child instanceof Node ) {
        updateNodeOnRemove( ( Node )child );
      }
      for( int i = 0; !removed && i < webComponentCount; i++ ) {
        WebComponent wc = ( WebComponent )webComponentList[ 0 ].get( i );
        if( child == wc  ) {
          webComponentList[ 0 ].remove( i );
          constraint = webComponentList[ 1 ].remove( i );
          webComponentCount--;
          removed = true;
        }
      }
      if( removed ) {
        toRemove.parent = null;
  
        // notify the container listener about the removing
        int evtId = WebContainerEvent.COMPONENT_REMOVED;
        WebContainerEvent wcEvt 
          = new WebContainerEvent( this, evtId, child, constraint );
        wcEvt.processEvent();
      }
    }
  }

  private void updateNodeOnRemove( final Node node ) {
    ComponentTreeVisitor visitor = new AllComponentVisitor() {
      public boolean doVisit( final WebComponent component ) {
        component.parent = null;
        return true;
      }
      
    };
    ComponentTreeVisitor.accept( node, visitor );
  }

  private void updateDecoratorOnRemove( final Decorator decorator,
                                        final WebComponent toRemove )
  {
    if( decorator.getContent() instanceof Decorator ) {
      Decorator contentDecorator = ( Decorator )decorator.getContent();
      updateDecoratorOnRemove( contentDecorator, toRemove );
    }
    if( decorator != toRemove ) {
      decorator.removeContent();
    }
    decorator.parent = null;
    if( decorator.getContent() != null ) {
      decorator.getContent().parent = null;
    }
  }
  
  /** 
   * Removes the specified WebComponent from the WebContainer.
   * 
   * @param index the index of the WebComponent to remove
   */
  public void remove( final int index ) {
    remove( this.get( index ) );
  }


  /** 
   * Removes all WebComponents from the WebContainer.
   */
  public void removeAll() {
    for( int i = 0; i < webComponentCount; i++ ) {
      WebComponent wc = ( WebComponent )webComponentList[ 0 ].get( i );
      wc.parent = null;
    }
    webComponentList[ 0 ].removeAllElements();
    webComponentList[ 1 ].removeAllElements();
    webComponentCount = 0;
  }


  /** 
   * Returns the WebComponent at the specified index in the container
   * (index starts with 0).
   */
  public WebComponent get( final int index ) {
    return ( WebComponent )webComponentList[ 0 ].get( index );
  }

  /** 
   * Returns all WebComponents that are added to this WebContainer.
   */
  public WebComponent[] get() {
    WebComponent[] result = new WebComponent[ getWebComponentCount() ];
    webComponentList[ 0 ].toArray( result );
    return result;
  }
  
  /** 
   * Returns the constraints at the specified index in the container
   * (index starts with 0).
   */
  public Object getConstraint( final int index ) {
    return webComponentList[ 1 ].get( index );
  }

  /**
   * Returns the constraint object belonging to the specified component.
   * 
   * @return null, if the specified WebComponent is not added to the container
   *         of the component was added without a constraint, the depending
   *         constraint object otherwise.
   */
  public Object getConstraint( final WebComponent wc ) {
    int size = this.getWebComponentCount();
    int searchIndex = -1;
    Object constraint = null;
    boolean found = false;
    for( int i = 0; !found && i < size; i++ ) {
      if( this.get( i ) == wc ) {
        searchIndex = i;
        found = true;
      }
    }
    if( searchIndex != -1 ) {
      constraint = this.getConstraint( searchIndex );
    }
    return constraint;
  }

  /** 
   * Returns the current WebLayout of this WebContainer.
   */
  public final WebLayout getWebLayout() {
    return webLayout;
  }

  /** 
   * Sets a new WebLayout for this WebContainer.
   * @param  newLayout instance of the new WebLayout for this WebContainer
   */
  public final void setWebLayout( final WebLayout newLayout ) {
    if( webLayout != newLayout && !hasCustomLayout ) {
      this.webLayout = newLayout;

      // notify the WebContainerListener about the change of the layout
      int evtId = WebContainerEvent.LAYOUT_CHANGED;
      WebContainerEvent wcEvt
        = new WebContainerEvent( this, evtId, webLayout );
      wcEvt.processEvent();
    }
    if( this instanceof ICustomContainer ) {
      hasCustomLayout = true;
    }
  }

  /** 
   * Returns the counter of the WebComponents added to this WebContainer.
   */
  public int getWebComponentCount() {
    return webComponentCount;
  }

  /** 
   * Sets the "value" of the "designTime" property to the WebContainer and
   * it's added WebComponents.
   * 
   * @param designTime sets the current "value" of the "designTime" property.
   */
  public void setDesignTime( final boolean designTime ) {
    super.setDesignTime( designTime );
    for( int i = 0; i < webComponentCount; i++ ) {
      WebComponent wc = ( WebComponent )webComponentList[ 0 ].get( i );
      wc.setDesignTime( this.designTime );
    }
  }

  /** 
   * A value of true denotes that this WebContainer and it's added
   * WebComponents should behave in design time
   * mode, a value of false denotes runtime behavior.
   * 
   * @return the current "value" of the "designTime" property.
   */
  public boolean isDesignTime() {
    return designTime;
  }

  /** 
   * <p>Adds the specified WebContainerListener to receive container events
   * from this WebContainer.</p>
   * <p>Container events occur when a WebComponent is added to or removed
   * from this WebContainer, or when a new WebLayout is set to this
   * WebContainer.</p>
   *
   * @param listener the WebContainerListener
   */
  public void addWebContainerListener( final WebContainerListener listener ) {
    WebContainerEvent.addListener( this, listener );
  }


  /** 
   * <p>Removes the specified WebContainerListener which receives container
   * events from this WebContainer.</p>
   * <p>Container events occur when a WebComponent is added to or removed
   * from this WebContainer, or when a new WebLayout is set to this
   * WebContainer.</p>
   *
   * @param listener the WebContainerListener
   */
  public void removeWebContainerListener( final WebContainerListener listener ){
    WebContainerEvent.removeListener( this, listener );
  }

  
  // interface methods of com.w4t.Validatable
  ///////////////////////////////////////////////////////////////

  /** 
   * <p>Adds the specified listener for ValidationEvents on this 
   * WebContainer.</p>
   * 
   * <p>The {@link #validate() validate()} method is called on any 
   * {@link Validatable Validatable}s added to this WebContainer and the 
   * added ValidationListener is notified about the result of the 
   * validation.</p>
   */
  public void addValidationListener( final ValidationListener listener ) {
    ValidationEvent.addListener( this, listener );
  }

  /** 
   * <p>Removes the specified listener for ValidationEvents on this 
   * WebContainer.</p>
   */
  public void removeValidationListener( final ValidationListener listener ) {
    ValidationEvent.removeListener( this, listener );
  }

  /** 
   * Validates all Validatables added to this WebContainer and notifies the
   * ValidationListeners added to this WebContainer.
   *  
   * @return the result of the validation on all Validatables added to
   *         this WebContainer
   */
  public boolean validate() {
    boolean result = true;
    if( validationActive ) {    
      WebComponent[] comps = get();
      for( int i = 0; i < comps.length; i++ ) {
        if( comps[ i ] instanceof Validatable ) {
          result &= ( ( Validatable )comps[ i ] ).validate();
        }
      }
      int evtId = ValidationEvent.VALIDATED;
      ValidationEvent evt = new ValidationEvent( this, evtId, result );
      evt.processEvent();
    }
    return result;
  }
  
  public void setValidationActive( final boolean validationActive ) {
    this.validationActive = validationActive;
  }

  public boolean isValidationActive() {
    return validationActive;
  }
  
  
  // interface methods of com.w4t.SimpleComponent
  ///////////////////////////////////////////////////////////////
  
  public String getCssClass() {
    return getUniversalAttributes().getCssClass();
  }
  
  public String getDir() {
    return getUniversalAttributes().getDir();
  }
  
  public String getLang() {
    return getUniversalAttributes().getLang();
  }
  
  public Style getStyle() {
    return getUniversalAttributes().getStyle();
  }
  
  public String getTitle() {
    return getUniversalAttributes().getTitle();
  }
  
  public void setCssClass( final String cssClass ) {
    getUniversalAttributes().setCssClass( cssClass );
  }
  
  public void setDir( final String dir ) {
    getUniversalAttributes().setDir( dir );
  }
  
  public void setLang( final String lang ) {
    getUniversalAttributes().setLang( lang );
  }
  
  public void setStyle( final Style style ) {
    getUniversalAttributes().setStyle( style );
  }
  
  public void setTitle( final String title ) {
    getUniversalAttributes().setTitle( title );
  }
  
  public void setIgnoreLocalStyle( final boolean ignoreLocalStyle ) {
    getUniversalAttributes().setIgnoreLocalStyle( ignoreLocalStyle );
  }
  
  public boolean isIgnoreLocalStyle() {
    return getUniversalAttributes().isIgnoreLocalStyle();
  }
  
  UniversalAttributes getUniversalAttributes() {
    if( universalAttributes == null ) {
      universalAttributes = createUniversalAttributes();
    }
    return universalAttributes;
  }
  
  private static UniversalAttributes createUniversalAttributes() {
    UniversalAttributes result = new UniversalAttributes();
    result.getStyle().setFontFamily( "" );
    result.getStyle().setFontSize( Style.NOT_USED );
    return result;
  }
}