package com.w4t.engine.lifecycle;

import junit.framework.TestCase;

import org.eclipse.rwt.internal.lifecycle.LifeCycleFactory;
import org.eclipse.rwt.lifecycle.ILifeCycle;

import com.w4t.W4TFixture;
import com.w4t.engine.lifecycle.standard.LifeCycle_Standard;


public class LifeCycleFactory_Test extends TestCase {
  
  public void testStandardLifeCycleLoading() {
    ILifeCycle lifeCycle1 = LifeCycleFactory.loadLifeCycle();
    assertTrue( lifeCycle1 instanceof LifeCycle_Standard );
    ILifeCycle lifeCycle2 = LifeCycleFactory.loadLifeCycle();
    assertNotSame( lifeCycle1, lifeCycle2 );
  }
  
  protected void setUp() throws Exception {
    W4TFixture.setUp();
    W4TFixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    W4TFixture.tearDown();
    W4TFixture.removeContext();
  }
}
