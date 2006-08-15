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

/** <p>This is the superclass of all property setting classes like
  * Style, WindowProperties etc.</p>
  */
public abstract class WebComponentProperties implements Cloneable {

  /** <p>Returns a clone of this WebComponentProperties Object.<br>
    * For all WebComponentPropertiess, cloning will result in a deep copy,
    * copying all fields, and recursively deep copying Fields containing
    * external references to WebComponentProperties objects in the cloned
    * object.</p>
    * <p>This also wraps the clone() method to make it public.</p> */
  public Object clone() throws CloneNotSupportedException {
    Object clone = super.clone();

    try {
      Field[] fields = this.getDeclaredFields();         // we use our own
                                                         // getDeclaredFields()
      Class fieldType = null;
      WebComponentProperties temp = null;
      WebComponentProperties tempClone = null;
      int length = fields.length;
      for ( int i = 0; i < length; i++ ) {
        if( !isStatic( fields[ i ] ) ) {
          fieldType = fields[ i ].getType();
          if( fieldType.isPrimitive() ) {
            // it should have been copied at super.clone()
          } else if ( fieldType.isAssignableFrom(
                                       Class.forName( "java.lang.String" ) ) ) {
            // it should have been copied at super.clone()
          } else if ( Class.forName( "com.w4t.WebComponentProperties" ).
                                               isAssignableFrom( fieldType ) ) {
            // we clone the attribute, if it is a WebComponentProperty
            // object itself
            try {
              fields[ i ].setAccessible( true );
              temp = ( WebComponentProperties )fields[ i ].get( this );
              tempClone = null;
              if( temp != null ) {
                tempClone = ( WebComponentProperties )temp.clone();
              }
              fields[ i ].set( ( WebComponentProperties )clone, tempClone );
            } catch ( IllegalAccessException iace ) {
              System.err.println(
                        "Exception occured in 'WebComponentProperties.clone()'."
                      + " Cloning an attribute of WebComponentProperties type. "
                      + iace.toString() );
            } catch ( IllegalArgumentException iarge ) {
              System.err.println(
                        "Exception occured in 'WebComponentProperties.clone()'."
                      + " Cloning an attribute of WebComponentProperties type. "
                      + iarge.toString()
                      + "\nType is " + this.getClass().getName() + "." );
            }
          } else {
            try {
              // we set all references to null, for reasons of safety
              fields[ i ].setAccessible( true );
              fields[ i ].set( ( WebComponentProperties )clone, null );
            } catch ( IllegalAccessException iace ) {
              System.err.println(
                        "Exception occured in 'WebComponentProperties.clone()'."
                      + " Nulling an attribute. "
                      + iace.toString() );
            } catch ( IllegalArgumentException iarge ) {
              System.err.println(
                        "Exception occured in 'WebComponentProperties.clone()'."
                      + " Nulling an attribute. "
                      + iarge.toString() );
            }
          }
        }        
      } // for fields
    } catch ( Exception ex ) {
      System.err.println(
                      "Exception occured in 'WebComponentProperties.clone()'. "
                     + ex.toString() );
      ex.printStackTrace();
    }
    return clone;
  }

  /** <p>Uses the Class.getDeclaredFields() in order to get a list of
    * declared fields in this WebObject, including inherited fields
    * from all classes above in the hierarchy.</p> */
  protected Field[] getDeclaredFields() {
    String thisClassName;
    String superClassName;
    Class thisClass;
    Class superClass;
    Field[] fields;

    try {
      thisClassName   = this.getClass().getName();
      thisClass  = Class.forName( thisClassName );
      superClassName  = thisClass.getSuperclass().getName();
      superClass = Class.forName( superClassName );
  
      fields = thisClass.getDeclaredFields();
      Field[] superFields = null;
      Field[] tempFields = null;
      boolean wcpReached = false;
      while ( !wcpReached ) {
        // get superclass
        Class objClass = Class.forName( "java.lang.Object" ); 
        if( !superClass.isAssignableFrom( objClass ) ) {
          // get declared fields from the next class above in the hierarchy
          superFields = superClass.getDeclaredFields();
          superClassName = superClass.getSuperclass().getName();
          superClass = Class.forName( superClassName );
  
          // merge the fields arrays
          tempFields = new Field[ fields.length + superFields.length ];
          System.arraycopy( fields, 0, tempFields, 0, fields.length );
          System.arraycopy( superFields, 0,
                            tempFields, fields.length, superFields.length );
          fields = tempFields;
  
        } else {
          wcpReached = true;
        }
      } // while
    } catch( final ClassNotFoundException shouldNotHappen ) {
      throw new RuntimeException( shouldNotHappen );
    }
    return fields;
  }

  // helping methods
  //////////////////
  
  private boolean isStatic( final Field field ) {
    int mod = field.getModifiers();
    return Modifier.isStatic( mod );
  }
}
