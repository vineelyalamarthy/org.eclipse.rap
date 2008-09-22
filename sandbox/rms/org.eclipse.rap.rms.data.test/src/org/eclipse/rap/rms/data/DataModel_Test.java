// Created on 08.09.2007
package org.eclipse.rap.rms.data;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.rap.rms.internal.data.DataModel;


public class DataModel_Test extends TestCase {
  
  private static final String PROJECT_1 = "project1";
  private static final String PROJECT_2 = "project2";
  private static final String LAST_NAME_1 = "last1";
  private static final String FIRST_NAME_1 = "first1";
  private static final String PRINCIPAL_1 = "principal1";
  private static final String TASK_1 = "task1";
  private StringBuffer log = new StringBuffer();
  

  private final class TestDataModel implements IDataModel {
    public IEmployee newEmployee( final String lastName, final String firstName ) {
      return null;
    }
    public IPrincipal newPrincipal( final String name ) {
      return null;
    }
    public List<IPrincipal> getPrincipals() {
      return null;
    }
    public List<IEmployee> getEmployees() {
      return null;
    }
    public void addModelListener( final ModelListener modelListener ) {
    }
    public void removeModelListener( final ModelListener modelListener ) {
    }
    public IStorageManager getStorageManager() {
      // TODO Auto-generated method stub
      return null;
    }
    @SuppressWarnings("unchecked")
    public Object getAdapter( Class adapter ) {
      return null;
    }
    public String getId() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  public void testDataFactoryRegistry() {
    assertNull( DataModelRegistry.getFactory() );
    
    TestDataModel testDataFactory = new TestDataModel();
    DataModelRegistry.register( testDataFactory );
    assertSame( DataModelRegistry.getFactory(), testDataFactory );
    
    try {
      DataModelRegistry.register( testDataFactory );
      fail( "Only one data factory at a time is allowed" );
    } catch( final IllegalArgumentException ise ) {
    }
    
    DataModelRegistry.deregisterFactory();
    assertNull( DataModelRegistry.getFactory() );
  }
  
  public void testDataModelImplementation() {
    IDataModel model = new DataModel();
    ModelListener modelListener = new ModelListener() {
      public void entityCreated( final ModelEvent evt ) {
        log.append( evt.getEntity().getClass().getName() );
        System.out.println(log);
      }
      public void entityChanged( final ModelEvent evt ) {
        log.append( evt.getEntity().getClass().getName() );
      }
    };
    model.addModelListener( modelListener );

    // test employee creation
    assertNotNull( model.getEmployees() );
    assertEquals( 0, model.getEmployees().size() );
    assertNotSame( model.getEmployees(), model.getEmployees() );
    
    IEmployee employee1 = model.newEmployee( LAST_NAME_1, FIRST_NAME_1 );
    assertNotNull( employee1 );
    assertSame( LAST_NAME_1, employee1.getLastName() );
    assertSame( FIRST_NAME_1, employee1.getFirstName() );
    assertTrue( log.toString().endsWith( "Employee" ) );
    
    List<IEmployee> employees = model.getEmployees();
    assertEquals( 1, employees.size() );
    assertSame( employee1, employees.get( 0 ) );
    try {
      employees.add( new IEmployee() {
        public String getFirstName() {
          return null;
        }
        public String getLastName() {
          return null;
        }
        public List<IAssignment> getAssignments() {
          return null;
        }
        public String getId() {
          return null;
        }
        @SuppressWarnings("unused")
        public Object getAdapter( Class<?> adapter ) {
          return null;
        }
      } );
      fail( "Employees list must be read-only." );
    } catch( final UnsupportedOperationException uoe ) {
    }
    
    // test principal creation
    assertNotNull( model.getPrincipals() );
    assertEquals( 0, model.getPrincipals().size() );
    assertNotSame( model.getPrincipals(), model.getPrincipals() );
    
    IPrincipal principal1 = model.newPrincipal( PRINCIPAL_1 );
    assertNotNull( principal1 );
    assertSame( PRINCIPAL_1, principal1.getName() );
    assertTrue( log.toString().endsWith( "Principal" ) );
    
    List<IPrincipal> principals = model.getPrincipals(); 
    assertEquals( 1, principals.size() );
    assertSame( principal1, principals.get(  0  ) );
    
    // test project creation
    assertNotNull( principal1.getProjects() );
    assertEquals( 0, principal1.getProjects().size() );
    assertNotSame( principal1.getProjects(), principal1.getProjects() );

    IProject project1 = principal1.newProject( PROJECT_1 );
    assertNotNull( project1 );
    assertSame( PROJECT_1, project1.getName() );
    assertTrue( log.toString().endsWith( "Project" ) );
    
    List<IProject> projects = principal1.getProjects();
    assertEquals( 1, projects.size() );
    assertSame( project1, projects.get( 0 ) );
    assertSame( principal1, project1.getPrincipal() );
    
    // assign employee to project
    assertNotNull( project1.getAssignments() );
    assertEquals( 0, project1.getAssignments().size() );
    assertNotSame( project1.getAssignments(), project1.getAssignments() );
    assertNotNull( employee1.getAssignments() );
    assertEquals( 0, employee1.getAssignments().size() );
    assertNotSame( employee1.getAssignments(), employee1.getAssignments() );
    
    IAssignment assignment1 = project1.newAssignment( employee1 );
    assertSame( project1, assignment1.getProject() );
    assertSame( employee1, assignment1.getEmployee() );
    assertTrue( log.toString().endsWith( "Assignment" ) );
   
    assertSame( assignment1, project1.getAssignments().get( 0 ) );
    assertSame( assignment1, employee1.getAssignments().get( 0 ) );

    // add tasks to project
    assertNotNull( project1.getTasks() );
    assertEquals( 0, project1.getTasks().size() );
    assertNotSame( project1.getTasks(), project1.getTasks() );
    
    ITask task1 = project1.newTask( TASK_1 );
    assertSame( project1, task1.getProject() );
    assertTrue( log.toString().endsWith( "Task" ) );
   
    assertSame( task1, project1.getTasks().get( 0 ) );
    
    principal1.setCity( "Bonn" );
    assertTrue( log.toString().endsWith( "Principal" ) );
    
    log = new StringBuffer();
    principal1.setCity( "Bonn" );
    assertTrue( log.toString().equals( "" ) );
    
    principal1.setCity( "Berlin" );
    assertTrue( log.toString().endsWith( "Principal" ) );
    
    log = new StringBuffer();
    principal1.setCity( null );
    assertTrue( log.toString().endsWith( "Principal" ) );
    

    log = new StringBuffer();
    model.removeModelListener( modelListener );
    principal1.newProject( PROJECT_2 );
    assertEquals( "", log.toString() );
  }
}

