/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     R�diger Herrmann - bug 316961: Exception handling may fail in AdapterFactoryRegistry
 ******************************************************************************/
package org.eclipse.rwt.internal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.AdapterFactory;
import org.eclipse.rwt.internal.service.ServletLog;
import org.eclipse.rwt.internal.util.ParamCheck;



public final class AdapterFactoryRegistry {
  
  private final static List factories = new ArrayList();
  
  private final static class FactoryEntry {
    private Class factoryClass;
    private Class adaptableClass;
  }

  public static void add( final Class factoryClass, 
                          final Class adaptableClass )
  {
    ParamCheck.notNull( factoryClass, "factoryClass" );
    ParamCheck.notNull( adaptableClass, "adaptableClass" );
    if( !AdapterFactory.class.isAssignableFrom( factoryClass ) ) {
      Object[] params = new Object[] {
        factoryClass.getName(),
        AdapterFactory.class.getName()
      };
      String text = "''{0}'' is not an instance of ''{1}''.";
      String msg = MessageFormat.format( text, params );
      throw new IllegalArgumentException( msg );
    }
    if( !Adaptable.class.isAssignableFrom( adaptableClass ) ) {
      Object[] params = new Object[] {
        adaptableClass.getName(),
        Adaptable.class.getName()
      };
      String text = "''{0}'' is not an instance of ''{1}''.";
      String msg = MessageFormat.format( text, params );
      throw new IllegalArgumentException( msg );
    }
    FactoryEntry[] entries = getEntries();
    for( int i = 0; i < entries.length; i++ ) {
      if(    entries[ i ].factoryClass == factoryClass
          && entries[ i ].adaptableClass == adaptableClass )
      {
        Object[] params = new Object[]{
          factoryClass.getName(),
          adaptableClass.getName()
        };
        String text
          = "The factory ''{0}'' was already added for the adaptable ''{1}''.";
        String msg = MessageFormat.format( text, params );
        throw new IllegalArgumentException( msg );
      }
    }
    
    FactoryEntry factoryEntry = new FactoryEntry();
    factoryEntry.factoryClass = factoryClass;
    factoryEntry.adaptableClass = adaptableClass;
    factories.add( factoryEntry );
  }

  public static void register() {
    FactoryEntry[] entries = getEntries();
    for( int i = 0; i < entries.length; i++ ) {
      Class clazz = entries[ i ].factoryClass;
      try {
        AdapterFactory factory = ( AdapterFactory )clazz.newInstance();
        AdapterManager manager = AdapterManagerImpl.getInstance();
        manager.registerAdapters( factory, entries[ i ].adaptableClass );
      } catch( Throwable thr ) {
        String text = "Could not create an instance of ''{0}''.";
        String msg = MessageFormat.format( text, new Object[] { clazz } );
        ServletLog.log( msg, thr );
      }
    }
  }
  
  public static void clear() {
    factories.clear();
  }
  
  //////////////////
  // helping methods

  private static FactoryEntry[] getEntries() {
    FactoryEntry[] entries = new FactoryEntry[ factories.size() ];
    factories.toArray( entries );
    return entries;
  }
}