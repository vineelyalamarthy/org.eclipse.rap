package org.eclipse.rap.rms.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

public class DataStorage_Test extends TestCase {

  @Override
  protected void setUp() throws Exception {
    DataModelRegistry.register( DataModelRegistry.DEFAULT_MODEL_TYPE );
  }

  @Override
  protected void tearDown() throws Exception {
    DataModelRegistry.deregisterFactory();
  }

  public void testSave() throws IOException {
    IDataModel dataModel = DataModelRegistry.getFactory();
    IEmployee employee1 = dataModel.newEmployee( "last1", "first1" );
    IEmployee employee2 = dataModel.newEmployee( "last2", "first2" );
    IEmployee employee3 = dataModel.newEmployee( "last3", "first3" );
    IPrincipal principal1 = dataModel.newPrincipal( "custom1" );
    principal1.setCity( "Karlsruhe" );
    principal1.setCountry( "Deutschland" );
    principal1.setEMail( "ycheng@innoopract.com" );
    principal1.setFaxNumber( "453432" );
    principal1.setFirstName( "Firma" );
    principal1.setLastName( "innoopract" );
    principal1.setMobileNumber( "0176-23930394" );
    principal1.setPhoneNumber( "0721-1827239" );
    principal1.setPostCode( "76131" );
    principal1.setStreet( "stephanien" );    
    
    IProject project1 = principal1.newProject( "project1" );
    project1.setDescription( "project1description lalala" );
    project1.setStartDate( new Date( 7856 ) );
    project1.setEndDate( new Date( 7897 ) );
    IProject project2 = principal1.newProject( "project2" );
    project2.setDescription( "project2description lalala" );
    project2.setStartDate( new Date( 7858 ) );
    project2.setEndDate( new Date( 7997 ) );
    
    IAssignment assigment1 = project1.newAssignment( employee1 );
    IAssignment assigment2 = project2.newAssignment( employee2 );
    IAssignment assigment3 = project2.newAssignment( employee3 );
    ITask task1 = project1.newTask( "task1" );
    task1.setDescription( "task1description" );
    task1.setStartDate( new Date( 785645452323423l ) );
    task1.setEndDate( new Date( 78974564 ) );
    ITask task2 = project2.newTask( "task2" );
    task2.setDescription( "task2description" );
    task2.setStartDate( new Date( 785845645 ) );
    task2.setEndDate( new Date( 79974564 ) );
    
    // save
    IStorageManager storageManager = dataModel.getStorageManager();
    ByteArrayOutputStream out = new ByteArrayOutputStream();      
    storageManager.save( out );   
    
    
  
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    storageManager.load( in );
    
    // test employees
    List<IEmployee> employees = dataModel.getEmployees();
    assertEquals( 3, employees.size() );
    IEmployee employee1A = employees.get( 0 );
    assertEquals( employee1.getFirstName(), employee1A.getFirstName() );
    assertEquals( employee1.getLastName(), employee1A.getLastName() );
    IEmployee employee2A = employees.get( 1 );
    assertEquals( employee2.getFirstName(), employee2A.getFirstName() );
    assertEquals( employee2.getLastName(), employee2A.getLastName() );
    IEmployee employee3A = employees.get( 2 );
    assertEquals( employee3.getFirstName(), employee3A.getFirstName() );
    assertEquals( employee3.getLastName(), employee3A.getLastName() );
    
    // test principals
    List<IPrincipal> principalsA = dataModel.getPrincipals();
    assertEquals( 1, principalsA.size() );
    IPrincipal principal1A = principalsA.get( 0 );
    assertEquals( principal1.getName(), principal1A.getName() );
    assertEquals( principal1.getStreet(), principal1A.getStreet() );
    assertEquals( principal1.getCity(), principal1A.getCity());
    assertEquals( principal1.getPostCode(), principal1A.getPostCode());
    assertEquals( principal1.getCountry(), principal1A.getCountry());
    assertEquals( principal1.getLastName(), principal1A.getLastName() );
    assertEquals( principal1.getFirstName(), principal1A.getFirstName() );
    assertEquals( principal1.getEMail(), principal1A.getEMail());
    assertEquals( principal1.getPhoneNumber(), principal1A.getPhoneNumber());
    assertEquals( principal1.getFaxNumber(), principal1A.getFaxNumber() );
    assertEquals( principal1.getMobileNumber(), principal1A.getMobileNumber());    
    
    // test projects
    assertEquals( 2, principal1A.getProjects().size() );
    
    IProject project1A = principal1A.getProjects().get( 0 );
    assertEquals( project1.getName(), project1A.getName() );
    assertEquals( project1.getDescription(), project1A.getDescription() );
    assertEquals( project1.getStartDate(), project1A.getStartDate() );
    assertEquals( project1.getEndDate(), project1A.getEndDate() );
    
    assertEquals( 1, project1A.getAssignments().size() );
    
    IAssignment assignment1A = project1A.getAssignments().get( 0 );
    assertEquals( employee1.getFirstName(),
                  assignment1A.getEmployee().getFirstName() );
    assertEquals( assigment1.getEmployee().getLastName(),
                  assignment1A.getEmployee().getLastName() );
    assertEquals( assigment1.getProject().getName(), 
                  assignment1A.getProject().getName() );
    
    assertEquals( 1, project1A.getTasks().size() );
    ITask task1A = project1A.getTasks().get( 0 );
    assertEquals( task1.getName(), task1A.getName() );
    assertEquals( task1.getDescription(), task1A.getDescription() );
    assertEquals( task1.getStartDate(), task1A.getStartDate() );
    assertEquals( task1.getEndDate(), task1A.getEndDate() );
    
    IProject project2A = principal1A.getProjects().get( 1 );
    assertEquals( project2.getName(), project2A.getName() );
    assertEquals( project2.getDescription(), project2A.getDescription() );
    assertEquals( project2.getStartDate(), project2A.getStartDate() );
    assertEquals( project2.getEndDate(), project2A.getEndDate() );
    
    assertEquals( 2, project2A.getAssignments().size() );    
    IAssignment assignment2A = project2A.getAssignments().get( 0 );
    assertEquals( employee2.getFirstName(),
                  assignment2A.getEmployee().getFirstName() );
    assertEquals( assigment2.getEmployee().getLastName(),
                  assignment2A.getEmployee().getLastName() );
    assertEquals( assigment2.getProject().getName(),
                  assignment2A.getProject().getName() );
    
    IAssignment assignment3A = project2A.getAssignments().get( 1 );
    assertEquals( employee3.getFirstName(),
                  assignment3A.getEmployee().getFirstName() );
    assertEquals( assigment3.getEmployee().getLastName(),
                  assignment3A.getEmployee().getLastName() );
    assertEquals( assigment3.getProject().getName(),
                  assignment3A.getProject().getName() );
    assertEquals( 1, project2A.getTasks().size() );
    
    ITask task2A = project2A.getTasks().get( 0 );
    assertEquals( task2.getName(), task2A.getName() );
    assertEquals( task2.getDescription(), task2A.getDescription() );
    assertEquals( task2.getStartDate(), task2A.getStartDate() );
    assertEquals( task2.getEndDate(), task2A.getEndDate() );
  }
}