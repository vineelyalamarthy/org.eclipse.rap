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
package com.w4t.engine;

import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.service.ISessionStore;

import junit.framework.TestCase;

import com.w4t.W4TFixture;
import com.w4t.engine.util.W4TModelList;

public class W4TModelUtil_Test extends TestCase {
  
  
  public void testW4TModelCreation() throws Exception {
    try {
      W4TModelUtil.initModel();
      fail();
    } catch( IllegalStateException iae ) {
      // no request context available
    }
    
    W4TFixture.setUp();
    W4TModel model1;
    try {
      W4TModelUtil.initModel();
      model1 = W4TModel.getInstance();
      assertNotNull( model1 );
      
      ISessionStore session = ContextProvider.getSession();
      Object storedModel = W4TModel.getInstance();
      assertSame( model1, storedModel );
      storedModel = W4TModelList.getInstance().get( session.getId() );
      assertSame( model1, storedModel );
    } finally {
      W4TFixture.tearDown();
    }
    
    W4TFixture.setUp();
    W4TModel model2;
    try {
      W4TModelUtil.initModel();
      model2 = W4TModel.getInstance();
      assertNotNull( model2 );
    } finally {
      W4TFixture.tearDown();
    }
    assertNotSame( model1, model2 );
    assertEquals( 0, W4TModelList.getInstance().getList().length );
  }
}
