/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.preferences;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.ISessionStore;
import org.eclipse.ui.internal.preferences.SessionScope;

/**
 * The ScopedPreferenceStore is an IPreferenceStore that uses the scopes
 * provided in org.eclipse.core.runtime.preferences.
 * <p>
 * A ScopedPreferenceStore does the lookup of a preference based on it's search
 * scopes and sets the value of the preference based on its store scope.
 * </p>
 * <p>
 * The default scope is always included in the search scopes when searching for
 * preference values.
 * </p>
 * 
 * @see org.eclipse.core.runtime.preferences
 * @since 1.1
 */
public class ScopedPreferenceStore 
  implements IPreferenceStore, IPersistentPreferenceStore
{
  private static final String KEY_SCOPED_PREF_CORE
    = ScopedPreferenceStoreCore.class.getName() + "#Store";
  private IScopeContext scopeContext;
  private ScopedPreferenceStoreCore defaultScopedPrefStore;
  private String nodeQualifier;
  private String defaultQualifier;


  /**
   * Create a new instance of the receiver. Store the values in context in the
   * node looked up by qualifier. <strong>NOTE:</strong> Any instance of
   * ScopedPreferenceStore should call
   * 
   * @param context the scope to store to
   * @param qualifier the qualifier used to look up the preference node
   * @param defaultQualifierPath the qualifier used when looking up the defaults
   */
  public ScopedPreferenceStore( final IScopeContext context,
                                final String qualifier,
                                final String defaultQualifierPath )
  {
    this( context, qualifier );
    this.defaultQualifier = defaultQualifierPath;
  }

  /**
   * Create a new instance of the receiver. Store the values in context in the
   * node looked up by qualifier.
   * 
   * @param context the scope to store to
   * @param qualifier the qualifer used to look up the preference node
   */
  public ScopedPreferenceStore( final IScopeContext context, 
                                final String qualifier )
  {
    createScopedPreferenceStoreCore( context, qualifier, qualifier );
    this.scopeContext = context;
    this.nodeQualifier = qualifier;
    this.defaultQualifier = qualifier;
  }

  private void createScopedPreferenceStoreCore( final IScopeContext context,
                                                final String qualifier,
                                                final String defaultQualifier )
  {
    // The defaultScopedPrefStore instance is used for non session scope 
    // contexts and the default values settings
    defaultScopedPrefStore
      = new ScopedPreferenceStoreCore( context, qualifier, defaultQualifier );
  }
  
  private ScopedPreferenceStoreCore getCore() {
    ScopedPreferenceStoreCore result = defaultScopedPrefStore;
    // In case the scopeContext is of type SessionScope we need to reference
    // an own ScopedPreferenceStore for each session
    if( scopeContext instanceof SessionScope ) {
      ISessionStore sessionStore = RWT.getSessionStore();
      String key = KEY_SCOPED_PREF_CORE;
      result = ( ScopedPreferenceStoreCore )sessionStore.getAttribute( key );
      if( result == null ) {
        result = new ScopedPreferenceStoreCore( scopeContext, 
                                                nodeQualifier, 
                                                defaultQualifier );
        sessionStore.setAttribute( key, result );
      }
    }
    return result;
  }

  /**
   * Does its best at determining the default value for the given key. Checks
   * the given object's type and then looks in the list of defaults to see if a
   * value exists. If not or if there is a problem converting the value, the
   * default default value for that type is returned.
   * 
   * @param key the key to search
   * @param obj the object who default we are looking for
   * @return Object or <code>null</code>
   */
  Object getDefault( final String key, final Object obj ) {
    return defaultScopedPrefStore.getDefault( key, obj );
  }

  /**
   * Return the IEclipsePreferences node associated with this store.
   * 
   * @return the preference node for this store
   */
  IEclipsePreferences getStorePreferences() {
    return getCore().getStorePreferences();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
   */
  public void addPropertyChangeListener( final IPropertyChangeListener lsnr ) {
    getCore().addPropertyChangeListener( lsnr );
  }

  /**
   * Set the search contexts to scopes. When searching for a value the search
   * will be done in the order of scope contexts and will not search the
   * storeContext unless it is in this list.
   * <p>
   * If the given list is <code>null</code>, then clear this store's search
   * contexts. This means that only this store's scope context and default scope
   * will be used during preference value searching.
   * </p>
   * <p>
   * The defaultContext will be added to the end of this list automatically and
   * <em>MUST NOT</em> be included by the user.
   * </p>
   * 
   * @param scopes a list of scope contexts to use when searching, or
   *            <code>null</code>
   */
  public void setSearchContexts( final IScopeContext[] scopes ) {
    getCore().setSearchContexts( scopes );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
   */
  public boolean contains( final String name ) {
    return getCore().contains( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent(java.lang.String,
   *      java.lang.Object, java.lang.Object)
   */
  public void firePropertyChangeEvent( final String name,
                                       final Object oldValue,
                                       final Object newValue )
  {
    getCore().firePropertyChangeEvent( name, oldValue, newValue );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String)
   */
  public boolean getBoolean( final String name ) {
    return getCore().getBoolean( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.lang.String)
   */
  public boolean getDefaultBoolean( final String name ) {
    return defaultScopedPrefStore.getDefaultBoolean( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang.String)
   */
  public double getDefaultDouble( final String name ) {
    return defaultScopedPrefStore.getDefaultDouble( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang.String)
   */
  public float getDefaultFloat( final String name ) {
    return defaultScopedPrefStore.getDefaultFloat( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang.String)
   */
  public int getDefaultInt( final String name ) {
    return defaultScopedPrefStore.getDefaultInt( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang.String)
   */
  public long getDefaultLong( final String name ) {
    return defaultScopedPrefStore.getDefaultLong( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang.String)
   */
  public String getDefaultString( final String name ) {
    return defaultScopedPrefStore.getDefaultString( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
   */
  public double getDouble( final String name ) {
    return getCore().getDouble( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
   */
  public float getFloat( final String name ) {
    return getCore().getFloat( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
   */
  public int getInt( final String name ) {
    return getCore().getInt( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
   */
  public long getLong( final String name ) {
    return getCore().getLong( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
   */
  public String getString( final String name ) {
    return getCore().getString( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
   */
  public boolean isDefault( final String name ) {
    return getCore().isDefault( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
   */
  public boolean needsSaving() {
    return getCore().needsSaving();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String,
   *      java.lang.String)
   */
  public void putValue( final String name, final String value ) {
    getCore().putValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
   */
  public void removePropertyChangeListener(
    final IPropertyChangeListener lsnr )
  {
    getCore().removePropertyChangeListener( lsnr );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      double)
   */
  public void setDefault( final String name, final double value ) {
    defaultScopedPrefStore.setDefault( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      float)
   */
  public void setDefault( final String name, final float value ) {
    defaultScopedPrefStore.setDefault( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      int)
   */
  public void setDefault( final String name, final int value ) {
    defaultScopedPrefStore.setDefault( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      long)
   */
  public void setDefault( final String name, final long value ) {
    defaultScopedPrefStore.setDefault( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      java.lang.String)
   */
  public void setDefault( final String name, final String defaultObject ) {
    defaultScopedPrefStore.setDefault( name, defaultObject );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
   *      boolean)
   */
  public void setDefault( final String name, final boolean value ) {
    defaultScopedPrefStore.setDefault( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.String)
   */
  public void setToDefault( final String name ) {
    defaultScopedPrefStore.setToDefault( name );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      double)
   */
  public void setValue( final String name, final double value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      float)
   */
  public void setValue( final String name, final float value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      int)
   */
  public void setValue( final String name, final int value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      long)
   */
  public void setValue( final String name, final long value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      java.lang.String)
   */
  public void setValue( final String name, final String value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
   *      boolean)
   */
  public void setValue( final String name, final boolean value ) {
    getCore().setValue( name, value );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.preference.IPersistentPreferenceStore#save()
   */
  public void save() throws IOException {
    getCore().save();
  }
}
