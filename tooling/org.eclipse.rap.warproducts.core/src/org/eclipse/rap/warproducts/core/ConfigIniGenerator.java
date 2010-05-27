/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigIniGenerator {

  private List bundleList;
  private List autoStartBundleList;
  private int bundleDefaultStartLevel;
  private Map bundleStartLevels;

  public ConfigIniGenerator( final List bundleList ) {
    this();
    this.bundleList = bundleList;    
  }
  
  public ConfigIniGenerator() {
    bundleDefaultStartLevel = 4;
  }
  
  public void autoStartBundle( final String bundleId ) {
    if( autoStartBundleList == null ) {
      autoStartBundleList = new ArrayList();
    }
    autoStartBundleList.add( bundleId );
  }
  
  public void setDefaultStartLevel( final int newDefaultStartLevel ) {
    bundleDefaultStartLevel = newDefaultStartLevel;
  }
  
  public void changeStartLevelForBundle( final String bundleId, 
                                         final int startLevel ) 
  {
    if(bundleStartLevels == null ) {
      bundleStartLevels = new HashMap();
    }
    Integer indexOfBundle = new Integer( bundleList.indexOf( bundleId ) );
    Integer newStartLevel = new Integer( startLevel );
    bundleStartLevels.put( indexOfBundle, newStartLevel );    
  }
  
  public void addBundle( final String bundleId ) {
    if( bundleList == null ) {
      bundleList = new ArrayList();
    }
    if( !bundleList.contains( bundleId ) ) {
      bundleList.add( bundleId );
    }
  }

  public String createConfigIni() {
    String result = null;
    if( bundleList != null && bundleList.size() > 0 ) {
      StringBuffer configIni = new StringBuffer();
      configIni.append( "osgi.bundles=" );
      addBundlesToConfigIni( configIni );
      configIni.append( createStartLevelString() );
      result = configIni.toString();
    } else {
      throw new IllegalStateException( "No bundles added to config.ini" );
    }
    return result;
  }

  private void addBundlesToConfigIni( final StringBuffer configIni ) {
    for( int i = 0; i < bundleList.size(); i++ ) {
      String bundleId = ( String )bundleList.get( i );
      configIni.append( getConfigIniStringForBundleId( bundleId, i ) );
    }
  }

  private String getConfigIniStringForBundleId( final String bundleId, 
                                                final int bundlePosition ) 
  {
    String separator = getBundleSeparator( bundlePosition );
    separator = handleBundleStart( bundleId ) + separator;    
    return bundleId + separator + "\\" + "\n";
  }

  private String handleBundleStart( final String bundleId ) {
    String result = "";
    result = handleBundleAutostart( bundleId );
    int bundleStartLevel = bundleHasStartLevel( bundleId );
    if( bundleStartLevel != -1 ) {
      result = handleBundleStartLevel( bundleStartLevel );
    }
    return result;
  }

  private String handleBundleStartLevel( int bundleStartLevel ) {
      return "@" + bundleStartLevel + ":start";
  }

  private String handleBundleAutostart( final String bundleId ) {
    String result = "";
    if(    autoStartBundleList != null 
        && autoStartBundleList.contains( bundleId ) ) 
    {
      result = "@start";
    }
    return result;
  }

  private int bundleHasStartLevel( final String bundleId ) {
    int result = -1;
    if( bundleStartLevels != null ) {
      Integer indexOf = new Integer( bundleList.indexOf( bundleId ) );
      Object object = bundleStartLevels.get( indexOf );
      if( object != null && object instanceof Integer ) {
        int startLevel = ( ( Integer )object ).intValue();
        result = startLevel;
      }
    }
    return result;
  }

  private String getBundleSeparator( final int bundlePosition ) {
    String separator = ",";
    if( bundlePosition >= ( bundleList.size() - 1 ) ) {
      separator = "";
    }
    return separator;
  }

  private String createStartLevelString() {
    return "osgi.bundles.defaultStartLevel=" + bundleDefaultStartLevel;
  }

}
