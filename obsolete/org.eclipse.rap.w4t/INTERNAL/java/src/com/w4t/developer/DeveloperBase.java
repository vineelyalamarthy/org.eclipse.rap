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
package com.w4t.developer;

import java.beans.BeanInfo;
import java.util.*;

import com.w4t.*;

/** <p>contains the base data structures for the introspection- and
 *  codegeneration mechanism for developer classes. </p>
 * 
 * <p>Implemented as Singleton to make it accesssible from all 
 * over the application.</p>
 */
public abstract class DeveloperBase {
  
  /** <p>the singleton instance of DeveloperBase.</p> */
  private static DeveloperBase _instance;

  /** <p>returns a reference to the singleton instance of 
    * DeveloperBaseImpl.</p> */
  public static synchronized DeveloperBase getInstance() {
    if( _instance == null ) {
      try {
        String name = "org.eclipse.rap.developer.core.DeveloperBaseImpl";
        _instance = ( DeveloperBase )Class.forName( name ).newInstance();
      } catch( Exception ex ) {
        System.out.println( "PANIC: DeveloperBase imlementation not found." );
        ex.printStackTrace();
      }
    }
    return _instance;
  }


  // interface methods
  ////////////////////

  /**
   * returns a String array with the names of all components in datastructure
   */
  public abstract String[] getBeanHandleNames();

  /** reference to the BeanHandle of the WebForm to edit */
  public abstract BeanHandle getRoot();

  /**
   * adds an editor WebForm to the usedEditorList
   * @param editorForm the editor WebForm to add
   * @param beanHandleName the name of the Component, which is edited by the
   * added WebForm
   */
  public abstract void addEditorForm( String beanHandleName,
                                      WebForm editorForm );

  /** resets name of the panel if normal WebForm is loaded */
  public abstract void resetClassName();
  
  /**
   * returns the editor WebForm, which belongs to a specific beanHandle.
   * if not used yet, null is returned;
   * @param beanHandleName the name of the Component, which is edited by the
   * requested WebForm
   */
  public abstract WebForm getEditorForm( String beanHandleName );

  /** removes every WebForm from the editor list */
  public abstract void clearEditFormList();

  /** returns a Enumeration with all WebForms used by this developerBase */
  public abstract Enumeration getEditorFormList();
  
  /** clear the internal data structures */
  public abstract void clearBeanList();

  /** returns the BeanHandle with the specified name */
  public abstract BeanHandle getBeanHandle( String beanHandleName );

  public abstract BeanInfo retrieveBeanInfo( Class beanClass ) throws Exception;

  /** adds a WebComponent and its subcomponents to the beanlist */
  public abstract void addComponent( WebComponent wc ) throws Exception;
  
  public abstract void clearPackageNameList();

  public abstract void clearCodeBuffers();

  /** returns a list of WebComponents ( not from sub bean handles ) 
    * of given instance */
  public abstract Vector getMainWebComponentList( Class classIdentity );
  
  /** returns a list of WebComponents of given instance */
  public abstract Vector getWebComponentList( Class classIdentity );
  
  /**
   * generate java class code for a WebForm, which looks like the
   * currently designed WebView
   */
  public abstract String getCode() throws Exception;

  public abstract String getClickedMethodName();

  public abstract String getUserDefinedEventCodeToEdit();

  public abstract void setUserDefinedEventCodeToEdit( String userDefCode );
 
  public abstract void setClickedMethodName( String clickedMethodName );

  /**
   * reference list of WebComponent instances used in the beanList
   */  
  public abstract Hashtable getUsedComponentsList();

  /** writes the code into the safety code file */
  public abstract void writeCodeSafetyFile();

  /** reads the code from safety code file
  * and writes it into the code file */
  public abstract void writeCodeFile();
  
  /** retrieves a initial component of the same type as the given component */
  public abstract Object getReferenceComponent( Object component ) 
                                                               throws Exception;
  
  /** retrieves a initial component of the same type as 
    * the given component fully qualified class name */  
  public abstract Object getReferenceComponent( String componentName ) 
                                                               throws Exception;

  public abstract String getPropertySetter( BeanHandle beanHandle,
                                   PropertyHandle propHandle );
  
  public abstract void addToDecoratorContentList( Object bean );

  public abstract String createBeanDeclarations( BeanHandle beanHandle );
  
  public abstract void setRootPanel( WebPanel rootPanel );

  public abstract WebPanel getRootPanel();
  
  public abstract String getEventListenersCode();
  
  public abstract String getUserDefinedCode();
  
  public abstract String getUserDefinedEventListenerCode();
  
  public abstract String getRootsPackageName();
  
  public abstract String getRootsClassName();
  
  public abstract void addLayout( WebContainer wc, 
                                  Class layoutClass,
                                  boolean newBean ) throws Exception;
  
  public abstract void addLayout( WebContainer wc, 
                                  boolean newBean ) throws Exception;  
  
  public abstract void changeName( String newName, BeanHandle toChange );
  
  public abstract void deleteEventListenerAddition( String beanName );  
  
  /** gets the code of the opened WebForm */
  public abstract String getWebFormCode();

  /** sets the full name of the panel to create if PanelCreator is loaded */
  public abstract void setClassName( String className );

  /** sets the String containing the userdefined code */
  public abstract void setUserDefinedCode( String userDefinedCode );

  public abstract void setUserDefinedEventListenerCode( String code );

  /** code of the method initialiseEventListeners */
  public abstract void setEventListenersCode( String eventListenersCode );
  
  /** sets the code file path with chosen name and
   * writes the code there into a file */
  public abstract void writeCodeFile( String saveAsCodeFilePath );
  
  /** gets the  initialisation methods calls (used for the FunctionBrowser) */
  public abstract String[] getInitialisationMethods();
  
  public abstract void addBeanHandle( BeanHandle beanHandle ) throws Exception;
  
  public abstract boolean containsBeanHandle( String handleName );
  
  public abstract boolean isDecoratorContent( WebComponent wc );
    
  public abstract void addToDeclarationList( BeanHandle beanHandle );
  
  public abstract void initialiseCodeFileWriter();
  
  public abstract void parseUserDefinedCode( String className );
  
  public abstract void parseEventListenersCode( String className );  
  
  /** returns a unique name for the specified component in the namespace of
   *  the actually developed container */
  public abstract String getComponentName( WebComponent wc );
}