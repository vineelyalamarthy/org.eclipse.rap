package com.w4t.engine.lifecycle;

import javax.servlet.ServletException;
import junit.framework.TestCase;
import com.w4t.Fixture;
import com.w4t.engine.lifecycle.standard.LifeCycle_Standard;


public class LifeCycleFactory_Test extends TestCase {
  
  public final static class TestLifeCycle extends LifeCycle {
    public void addPhaseListener( final PhaseListener listener ) {}
    public void execute() throws ServletException {}
    public void removePhaseListener( final PhaseListener listener ) {}
  }
  
  public void testStandardLifeCycleLoading() {
    ILifeCycle lifeCycle = LifeCycleFactory.loadLifeCycle();
    assertTrue( lifeCycle instanceof LifeCycle_Standard );
  }
  
  public void testUserdefinedLifeCycleLoading() {
    System.setProperty( "lifecycle", TestLifeCycle.class.getName() );
    ILifeCycle lifeCycle = LifeCycleFactory.loadLifeCycle();
    assertTrue( lifeCycle instanceof TestLifeCycle );
    System.getProperties().remove( "lifecycle" );
  }
  
  protected void setUp() throws Exception {
    Fixture.setUp();
    Fixture.createContext();
  }
  
  protected void tearDown() throws Exception {
    Fixture.tearDown();
    Fixture.removeContext();
  }
}
