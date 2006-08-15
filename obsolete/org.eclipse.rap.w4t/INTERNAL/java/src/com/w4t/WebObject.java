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
import java.lang.reflect.Modifier;

/** <p>Implements the basic functionality which is common to every 
  * WebObject based component. </p>
  */
public abstract class WebObject implements Cloneable {

  /** whether the event listener fiels in this WebObject should be cloned. */
  private boolean cloneListeners = true;
  
  /** Constructor */
  protected WebObject() {
  }

  /** uses the Class.getDeclaredFields() in order to get a list of
    * declared fields in this WebObject, including inherited fields
    * from all classes above in the hierarchy. */
  protected Field[] getDeclaredFields() {
    Class thisClass;
    Class superClass;
    Field[] fields;

    try {      
      thisClass = this.getClass();      
      superClass = thisClass.getSuperclass();

      fields = thisClass.getDeclaredFields();
      Field[] superFields = null;
      Field[] tempFields = null;
      boolean woReached = false;
      while ( !woReached ) {
        // get superclass
        if( !superClass.isAssignableFrom(
                                     Class.forName( "java.lang.Object" ) ) ) {
          // get declared fields from the next class above in the hierarchy
          superFields = superClass.getDeclaredFields();
          superClass = superClass.getSuperclass();
          // merge the fields arrays
          tempFields = new Field[ fields.length + superFields.length ];
          System.arraycopy( fields, 0, tempFields, 0, fields.length );
          System.arraycopy( superFields, 0,
                            tempFields, fields.length, superFields.length );
          fields = tempFields;
        } else {
          woReached = true;
        }
      } // while
    } catch ( final ClassNotFoundException shouldNotHappen ) {
      throw new RuntimeException( shouldNotHappen );
    }
    return fields;
  }

  /** returns a clone of this WebObject.<br>
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
    Object clone = super.clone();
    try {
      Field[] fields = this.getDeclaredFields();         // we use our own
                                                         // getDeclaredFields()
      Class fieldType = null;
      int length = fields.length;
      for ( int i = 0; i < length; i++ ) {
        if( !isStatic( fields[ i ] ) && !isFinal( fields[ i ] ) ) {
          fieldType = fields[ i ].getType();
          if( fieldType.isPrimitive() ) {
            // it should have been copied at super.clone()
          } else if ( fieldType.isAssignableFrom(
                                      Class.forName( "java.lang.String" ) ) ) {
            // it should have been copied at super.clone()
          } else {
            try {
              if(    !isListener( fields[ i ] )
                  || !isCloneListeners() ) { 
                fields[ i ].setAccessible( true );
                fields[ i ].set( ( WebObject )clone, null );
              }
            } catch ( IllegalAccessException iace ) {
              System.err.println(  "Exception occured in 'WebObject.clone()' "
                                 + "cloning field '" 
                                 + fields[ i ].getName() + "'\n"
                                 + iace.toString() );
            } catch ( IllegalArgumentException iarge ) {
              System.err.println(  "Exception occured in 'WebObject.clone()'. "
                                 + "cloning field '" 
                                 + fields[ i ].getName() + "'\n"
                                 + iarge.toString() );
            }
          }
        }
      } // for fields
    } catch ( Exception ex ) {
      System.err.println(  "Exception occured in 'WebObject.clone()'. "
                          + ex.toString() );
    }
    return clone;
  }

  // helping methods
  //////////////////

  private boolean isFinal( final Field field ) {
    int mod = field.getModifiers();
    return Modifier.isFinal( mod );
  }

  private boolean isStatic( final Field field ) {
    int mod = field.getModifiers();
    return Modifier.isStatic( mod );
  }

  private boolean isListener( final Field field )  {
    return    checkListenerExtension( field.getType().getName() )
           && checkEventClass( field );
  }

  private boolean checkListenerExtension( final String fieldName ) {
    return terminatesAfterListenerEnding( fieldName );
  }
  
  private boolean checkEventClass( final Field field ) {
    boolean result = false;
    String eventName = createEventName( field );
    try {
      Class.forName( eventName );
      result = true;
    } catch( ClassNotFoundException canBeIgnored ) {
      // because we use it only to see whether the class is accessible,
      // if not, we act accordingly
    }
    return result;
  }
  
  private int getListenerIndex( final String name ) {
    return name.lastIndexOf( "Listener" );
  }
  
  private boolean hasListenerEnding( final String name ) {
    return getListenerIndex( name ) != -1;
  }
  
  private boolean terminatesAfterListenerEnding( final String name ) {
    return    hasListenerEnding( name )
           && name.length() == getListenerIndex( name ) + 8;
  }
  
  private String createEventName( final Field field ) {
    String result = field.getType().getName();
    result = result.substring( 0, getListenerIndex( result ) ) + "Event";
    return result;
  }
  
  
  // attribute getters and setters
  ////////////////////////////////
  
  /** sets whether the event listener fields in this WebObject should be 
    * cloned. */
  public void setCloneListeners( final boolean cloneListeners ) {
    this.cloneListeners = cloneListeners;
  }
  
  /** returns whether the event listener fields in this WebObject should be 
    * cloned. */
  public boolean isCloneListeners() {
    return cloneListeners;
  }
}