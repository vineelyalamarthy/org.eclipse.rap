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
    List bundleList = new ArrayList();
    for( int i = 0; i < 10; i++ ) {
      bundleList.add( "org.eclipse.rap.test" + i );
    }
    ConfigIniGenerator generator = new ConfigIniGenerator( bundleList );
    String configIni = generator.createConfigIni();
    
    
  }
  
  
  
}
