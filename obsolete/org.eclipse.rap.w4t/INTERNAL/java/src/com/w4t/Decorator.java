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

import org.eclipse.rwt.internal.util.ParamCheck;


/**
 * <p>A <code>Decorator</code> is a <code>WebComponent</code> that wraps other
 * <code>WebComponent</code>s to add additional functionality to the
 * wrapped component, e.g. scrollbars to a <code>TreeView</code>. 
 * <code>Decorator</code>s may be nested to combine their functionalities.</p>
 * 
 * <p><code>Decorator</code>s are closely coupled to the behavior of their
 * content component. For example if the content component is not visible
 * the decorator won't be visible too. Removing the content component from
 * the component tree via the component's <code>remove</code> method causes
 * a removal of the decorator too (Note that in this case the component
 * is also removed from the decorator - to remove the bundle from the parent
 * <code>WebContainer</code> without loosing the coupling, use the 
 * decorator's remove method). The content component is not aware of it's 
 * decoration and behaves as a directly added to a 
 * <code>WebContainer</code>.</p>
 */
abstract public class Decorator extends WebComponent {

  /** The wrapped WebComponent */ 
  private WebComponent content = null;
  
  
  /**
   * <p>Creates a new instance of <code>Decorator</code>.</p>
   */
  public Decorator() {
  }
  
  /** 
   * <p>Creates a new instance of <code>Decorator</code>.</p>
   * @param content the <code>WebComponent</code> that is decorated
   *        by this decorator.
   */
  public Decorator( final WebComponent content ) {
    setContent( content );
  }

  /** 
   * <p>Sets the WebComponent that should be decorated by this
   * <code>Decorator</code>.</p>
   * @param content the WebComponent to be decorated, must not be
   * <code>null</code>
   * @see #removeContent()
   */
  public void setContent( final WebComponent content ) {
    ParamCheck.notNull( content, "content" );
    if( content.parent != null ) {
      String msg =   "Parameter 'content' is a WebComponent that is already " 
                   + "added to a WebContainer.";
      throw new IllegalArgumentException( msg );
    }
    releaseContent();
    this.content = content;
    content.parent = this.getParent();
  }

  private void releaseContent() {
    if( this.content != null ) {
      this.content.parent = null;
    }
  }
  
  /** 
   * <p>Returns the WebComponent that is decorated by this
   * <code>Decorator</code>.</p>
   */
  public WebComponent getContent() {
    return content;
  }

  /** 
   * <p>Removes the component that is decorated by this <code>Decorator</code>.
   * </p>
   */
  public void removeContent() {
    releaseContent();
    content = null;
  }

  /**
   * <p>Sets whether the content component of this decorator is
   * enabled. Overrides <code>setEnabled</code> of 
   * {@link WebComponent WebComponent}.</p>
   */
  public void setEnabled( final boolean enabled ) {
    if ( content != null ) {
      content.setEnabled( enabled );
    }
  }

  /**
   * <p>Returns whether the content component of this decorator is
   * enabled. Overrides <code>isEnabled</code> of 
   * {@link WebComponent WebComponent}.</p>
   */
  public boolean isEnabled() {
    boolean result = false;
    if ( content != null ) {
      result = content.isEnabled();
    }
    return result;
  }

  /**
   * <p>Sets whether the content component of this decorator is
   * visible. Overrides <code>setVisible</code> of 
   * {@link WebComponent WebComponent}. This implies that no markup
   * for the decorator will be created if the decorator's content is not 
   * visible.</p>
   */
  public void setVisible( final boolean visible ) {
    if ( content != null ) {
      content.setVisible( visible );
    }
  }

  /**
   * <p>Returns whether the content component of this decorator is
   * visible. Overrides <code>isVisible</code> of 
   * {@link WebComponent WebComponent}. This implies that no markup
   * for the decorator will be created if the decorator's content is not 
   * visible.</p>
   */
  public boolean isVisible() {
    boolean result = false;
    if ( content != null ) {
      result = content.isVisible();
    }
    return result;
  }

  /**
   *  <p>For the designer if cut a component</p> 
   */
  public void hide() {
    if ( parent != null ) {
      parent.remove( this );
    }
  }

  /** 
   * <p>Returns a clone of this <code>Decorator</code>.</p> 
   */
  public Object clone() throws CloneNotSupportedException {
    Decorator result = ( Decorator )super.clone();
    if( content != null ) {
      try {
        result.setContent( ( WebComponent )content.clone() );
      } catch ( Exception ex ) {
        System.out.println(   "Exception in Decorator.clone(): " 
                            + ex.toString() );
      }  
    }      
    return result;
  }
  
  public void remove() {
    super.remove();
    if( content != null ) {
      content.parent = null;
    }
  }

  /** 
   * <p>Sets the 'value' of the 'designTime' property to the Decorator and
   * it's added WebComponent.</p>
   * @param designTime sets the current 'value' of the 'designTime' property.
   */
  public void setDesignTime( final boolean designTime ) {
    super.setDesignTime( designTime );
    content.setDesignTime( this.designTime );
  }

  /**
   * <p>Returns whether the specified component is a content component
   * of a <code>Decorator</code>.</p>
   * @param component the component which might be decorated, must not be
   * <code>null</code>
   * @throws IllegalArgumentException if the <code>component</code> is not part
   * of the component tree.
   */
  public static boolean isDecorated( final WebComponent component ) {
    ParamCheck.notNull( component, "component" );
    if( component.getParent() == null ) {
      String msg = "Parameter 'component' has no parent.";
      throw new IllegalArgumentException( msg );
    }
    boolean result = false;
    int count = component.getParent().getWebComponentCount();
    for( int i = 0; !result && i < count; i++ ) {
      WebComponent toCompare = component.getParent().get( i );
      result = isDecorated( component, toCompare );
    }
    return result;
  }
  
  private static boolean isDecorated( final WebComponent searched,
                                      final WebComponent toCompare )
  {
    return    toCompare != null 
           && toCompare instanceof Decorator
           && ( ( Decorator )toCompare ).getContent() == searched
           // check also nested decorators
           || toCompare instanceof Decorator
           && isDecorated( searched, 
                          ( ( Decorator )toCompare ).getContent() );
  }

  /**
   * <p>Returns the parent <code>Decorator</code> of the specified component
   * if exists, null otherwise.</p>
   * @param component the component to obtain the parent decorator for, must
   * not be <code>null</code>
   * @throws  IllegalArgumentException if <code>component</code> is not part
   * of the component tree.
   */
  public static Decorator getParentDecorator( final WebComponent component ) {
    ParamCheck.notNull( component, "component" );
    if( component.getParent() == null ) {
      String msg = "Parameter 'component' has no parent.";
      throw new IllegalArgumentException( msg );
    }
    Decorator result = null;
    int count = component.getParent().getWebComponentCount();
    for( int i = 0; result == null && i < count; i++ ) {
      WebComponent toCompare = component.getParent().get( i );
      if( toCompare instanceof Decorator ) {
        Decorator decorator = ( Decorator )toCompare;
        while( decorator != null ) {
          if( decorator.getContent() == component ) {
            result = decorator;
            decorator = null;
          } else if( decorator.getContent() instanceof Decorator ) {
            decorator = ( Decorator )decorator.getContent();
          } else {
            decorator = null;
          }
        }
      }
    }
    return result;
  }

  /**
   * <p>Returns the outermost <code>Decorator</code> of the specified component
   * if exists, null otherwise.</p>
   * @param comp the component to obtain the outermost decorator for, must
   * not be <code>null</code> 
   * @throws  IllegalArgumentException if <code>component</code> is not part
   * of the component tree.
   */
  public static Decorator getOuterMostDecorator( final WebComponent comp ) {
    Decorator result = getParentDecorator( comp );
    while( isDecorated( result ) ) {
      result = getParentDecorator( result );
    }
    return result;
  }
}