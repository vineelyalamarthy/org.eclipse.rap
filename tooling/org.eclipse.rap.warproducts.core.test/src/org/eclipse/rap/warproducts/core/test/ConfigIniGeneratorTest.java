/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.rap.warproducts.core.ConfigIniGenerator;


public class ConfigIniGeneratorTest extends TestCase {
  
  public void testInitializeWithBundleList() {
    List bundleList = createBundleList( 1 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "osgi.bundles=" ) );    
  }
  
  public void testContainsBundles() {
    List bundleList = createBundleList( 10 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "org.eclipse.rap.test0,\\" ) );
    assertFalse( configIni.contains( "org.eclipse.rap.test10,\\" ) );
  }
  
  public void testContainsAddedBundles() {
    List bundleList = createBundleList( 1 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    generator.addBundle( "org.eclipse.rap.rwt" );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "org.eclipse.rap.rwt\\" ) );
  }
  
  public void testContainsBundlesWithAutoStartStart() {
    List bundleList = createBundleList( 10 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    generator.autoStartBundle( "org.eclipse.rap.test0" );
    generator.autoStartBundle( "org.eclipse.rap.test6" );
    generator.autoStartBundle( "org.eclipse.rap.test9" );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "org.eclipse.rap.test0@start,\\" ) );
    assertTrue( configIni.contains( "org.eclipse.rap.test6@start,\\" ) );
    assertTrue( configIni.contains( "org.eclipse.rap.test9@start\\" ) );
    assertTrue( configIni.contains( "org.eclipse.rap.test1,\\" ) );
  }
  
  public void testContainsDefaultStartLevel() {
    List bundleList = createBundleList( 1 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "osgi.bundles.defaultStartLevel=4" ) );    
  }
  
  public void testSetDefaultStartLevel() {
    List bundleList = createBundleList( 1 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    generator.setDefaultStartLevel( 3 );    
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "osgi.bundles.defaultStartLevel=3" ) );    
  }
  
  public void testChangeBundleStartLevel() {
    List bundleList = createBundleList( 10 );
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    generator.changeStartLevelForBundle( "org.eclipse.rap.test0", 2 );
    generator.changeStartLevelForBundle( "org.eclipse.rap.test4", 3 );
    generator.changeStartLevelForBundle( "org.eclipse.rap.test8", 4 );
    String configIni = generator.createConfigIni();
    assertTrue( configIni.contains( "org.eclipse.rap.test0@2:start,\\" ) );
    assertTrue( configIni.contains( "org.eclipse.rap.test4@3:start,\\" ) );
    assertTrue( configIni.contains( "org.eclipse.rap.test8@4:start,\\" ) );    
  }
    
  private List createBundleList( final int bundlesAmount ) {
    List bundleList = new ArrayList();
    for( int i = 0; i < bundlesAmount; i++ ) {
      bundleList.add( "org.eclipse.rap.test" + i );
    }
    return bundleList;
  }  
  
}
