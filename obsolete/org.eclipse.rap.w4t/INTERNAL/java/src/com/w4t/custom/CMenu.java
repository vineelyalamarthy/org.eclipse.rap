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
package com.w4t.custom;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.rwt.internal.resources.ResourceManagerImpl;
import org.eclipse.rwt.internal.util.Assert;
import org.eclipse.rwt.resources.IResourceManager;

import com.w4t.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.menustyle.*;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.types.LocalPath;


/** <p>A convenience component that consists of a menu which can be configured
  * declaratively in an XML file.</p>
  * 
  * <p>The menu configuration (names of the menu items, their respective 
  * menus, and commands which are triggered by these items) is read 
  * from an XML file which can be set by {@link #setConfigFile(LocalPath) 
  * setConfigFile(LocalPath)} or {@link #setConfigResource(String) 
  * setConfigResource(String)}.</p>
  * 
  * <p>A configuration file for CMenu should look like this:
  * <pre>
  *   &lt;CMenu&gt;
  * 
  *     &lt;menuEntry&gt;
  *       &lt;!-- entry for a menu item in Menu 'File' --&gt;
  *       &lt;menu&gt;File&lt;/menu&gt;
  *       &lt;label&gt;File&lt;/label&gt;
  *       &lt;id&gt;FILE_QUIT&lt;/id&gt;
  *       &lt;command&gt;com.and.go.FileQuitCommand&lt;/command&gt;
  *     &lt;/menuEntry&gt;
  * 
  *     &lt;!-- more entries go here --&gt;
  *
  *   &lt;/CMenu&gt;
  * </pre>
  * 
  * <p>Specifying an <strong>&lt;id&gt;</strong> is obligatory. On click the 
  * CMenu invokes the <code>execute</code> method on a new instance of the 
  * class that was specified in &lt;command&gt;. This class must implement 
  * the <code>ICustomAction</code> interface. To notify the action about which
  * <strong>&lt;id&gt;</strong> is to be executed the 
  * <code>ICustomAction</code>s <code>init</code> method is invoked in advance.
  * </p> 
  * <p>The following example shows how an <code>ICustomAction</code> 
  * implementation could look like 
  * <pre>
  *   public class MyAction implements ICustomAction {
  *   
  *     private String commandToExecute;
  *     
  *     public void init( String commandId ) {
  *       commandToExecute = commandId;
  *     }
  *     
  *     public void execute() {
  *       if( "org.demo.openAction".equals( commandToExecute ) {
  *         // open something
  *       } else if( "org.demo.closeAction".equals( commandToExecute ) {
  *         // close something
  *       }
  *       // ...
  *     }
  *   }
  * </pre></p>
  * 
  * <p>Specifying a <strong>&lt;command&gt;</strong> is optional. If none is
  * specified, clicking the menu will do nothing.</p> 
  * @see org.eclipse.rwt.custom.ICustomAction
  */
public class CMenu extends WebPanel implements Concealer {

  /** <p>the encapsulated menu bar instance.</p>*/
  private MenuBar menuBar;
  /** <p>Container for currently loaded menus.</p>*/
  private Map menuList;
  /** <p>the file that contains the menu configuration.</p> */
  private LocalPath configFile;
  private String configResource;


  /** <p>Constructs a new CMenu.</p> */
  public CMenu() throws Exception {
    menuList = new Hashtable();    
    setWebComponents();
  }

  public Object clone() throws CloneNotSupportedException {
    CMenu result = ( CMenu )super.clone();
    result.menuList = new Hashtable();
    result.menuBar = ( MenuBar )this.menuBar.clone();
    try {
      if( configFile != null ) {
        result.setConfigFile( this.configFile );
      }
      if( configResource != null ) {
        result.setConfigResource( this.configResource );
      }
    } catch( Exception shouldNotHappen ) {
      // as shouldNotHappen should not happen, we do nothing when it not happens
    }  
    return result;
  }
  
  /** <p>Returns a path to an image that represents this WebComponent
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/menubar.gif";
  }  

  private void setWebComponents() throws Exception {
    instanceWebComponents();
    initialiseMenuBar1();
  }

  private void instanceWebComponents() {
    menuBar = new MenuBar();
  }

  private void initialiseMenuBar1() {
    this.add( menuBar );
    menuBar.setName( "menuBar" );
  }


  // helping methods
  //////////////////
  
  private void initialiseMenus() throws Exception {
    menuBar.removeAllItems();
    menuList.clear();
    MenuEntry[] menuEntries = getMenuEntries();
    for( int i = 0; i < menuEntries.length; i++ ) {
      Menu menu = initialiseMenu( menuEntries[ i ].getMenu() );
      addMenuItem( menu, menuEntries[ i ] );
    }      
  }
  
  private Menu initialiseMenu( final String menuName ) {
    Menu result = ( Menu )menuList.get( menuName );
    if( result == null ) {
      result = new Menu( menuName );
      menuBar.addItem( result );
      menuList.put( menuName, result );
    }      
    return result;
  }
    
  private void addMenuItem( final Menu menu, final MenuEntry entry ) {
    Item item;
    if( entry.getLabel().equals( "separator" ) ) {
      item = new MenuItemSeparator();
    } else {
      item = createMenuItem( entry );
    } 
    menu.addItem( item );   
  }
  
  private MenuItem createMenuItem( final MenuEntry entry ) {
    final MenuItem result = new MenuItem( entry.getLabel() );    
    if( !entry.getId().equals( "" ) ) {
      final String className = entry.getCommand().trim();
      IResourceManager manager = ResourceManagerImpl.getInstance();
      final ClassLoader contextLoader = manager.getContextLoader();
      result.addWebActionListener( new WebActionListener() {
        public void webActionPerformed( final WebActionEvent evt ) {
          ICustomAction command;
          try {
            Class clazz;
            if( contextLoader == null ) {
              clazz = Class.forName( className );
            } else {
              clazz = contextLoader.loadClass( className );
            }
            command = ( ICustomAction )clazz.newInstance();
          } catch( final Exception ex ) {
            String msg 
              = "ICustomAction not found: {0} [{1}]\nfor menu item {2}."; 
            Object[] params = new Object[] { 
              entry.getId(), 
              className, 
              W4TContext.resolve( result.getLabel() )
            };
            String txt = MessageFormat.format( msg, params );
            
            throw new RuntimeException( txt, ex );
          }
          command.init( entry.getId() );
          command.execute();
        }
      } );
    }
    return result;
  }
  
  private MenuEntry[] getMenuEntries() throws Exception {
    String fileName;
    if( getConfigFile() != null ) {
      fileName = getConfigFile().toFile().toString();
    } else {
      fileName = configResource;
    }
    InitialisationReader reader = new InitialisationReader( fileName );
    
    int count = reader.selectContent( "menuItem" );
    MenuEntry[] result = new MenuEntry[ count ];
    for( int i = 0; i < count; i++ ) {
      String menu = reader.getAttribute( "menu" );
      String label = reader.getAttribute( "label" );
      String id = reader.getAttribute( "id" );
      String command = reader.getAttribute( "command" );
      result[ i ] = new MenuEntry( menu, label, id, command );
      reader.nextContent();      
    }
    return result;    
  }
  
  // attribute getters and setters
  ////////////////////////////////

  /** <p>Sets the file from which the configuration of this CMenu is 
   * read.</p>
   * 
   * <p>Note: Using <code>setConfigFile</code> to initialize this 
   * <code>CMenu</code> instance, discards the <code>setConfigResource</code>
   * initializations.</p>
   */
  public void setConfigFile( final LocalPath configFile ) {
    Assert.isNotNull( configFile );
    Assert.isTrue( configFile.toFile().exists() );
    this.configFile = configFile;
    this.configResource = null;
    try {
      initialiseMenus();
    } catch( final Exception ex ) {
      String txt =   "The file \"{0}\" does not exist or is not a "
                   + "valid configuration file.";
      Object[] param = new Object[]{ configFile.toFile().toString() };
      String msg = MessageFormat.format( txt, param );
      throw new IllegalArgumentException( msg );
    }
  }

  /** <p>Returns the file from which the configuration of this CMenu is 
   * read.</p>
   */
  public LocalPath getConfigFile() {
    return configFile;
  }
  
  /**
   * <p>Loads the given configuration resource from the application's
   * classpath.</p>
   * <p>Note: Using <code>setConfigResource</code> to initialize this 
   * <code>CMenu</code> instance, discards the <code>setConfigFile</code>
   * initializations.</p>
   * @param configResource the path to an menu configuration file that is
   * available on the application's classpath.
   */
  public void setConfigResource( final String configResource ) {
    Assert.isNotNull( configResource );
    this.configResource = configResource;
    this.configFile = null;
    try {
      initialiseMenus();
    } catch( final Exception ex ) {
      String txt =   "The file \"{0}\" does not exist or is not a "
                   + "valid configuration file.";
      String msg = MessageFormat.format( txt, new Object[]{ configResource } );
      throw new IllegalArgumentException( msg );
    }
  }
  
  /**
   * <p>Returns the configuration resource available on the application's 
   * classpath that was used to initialize this <code>CMenu</code> or 
   * <code>null</code>, if not available.</p>
   */
  public String getConfigResource() {
    return configResource;
  }


  // attribute delegations to the menuBar
  ///////////////////////////////////////

  /** <p>Sets the passed MenuBarStyle to the MenuBar in this CMenu.</p>
   * 
   * @see MenuBar#setMenuBarStyle( MenuBarStyle )
   */
  public void setMenuBarStyle( final MenuBarStyle menuBarStyle ) {
    menuBar.setMenuBarStyle( menuBarStyle );
  }
  
  /** <p>Returns the MenuBarStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getMenuBarStyle()
    */  
  public MenuBarStyle getMenuBarStyle() {
    return menuBar.getMenuBarStyle();
  }

  /** <p>Sets the passed MenuButtonActiveStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setButtonActiveStyle( MenuButtonActiveStyle )
   */
  public void setButtonActiveStyle( 
                               final MenuButtonActiveStyle buttonActiveStyle ) {
    menuBar.setButtonActiveStyle( buttonActiveStyle );
  }

  /** <p>Returns the MenuButtonActiveStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getButtonActiveStyle()
    */  
  public MenuButtonActiveStyle getButtonActiveStyle() {
    return menuBar.getButtonActiveStyle();
  }

  /** <p>Sets the passed MenuButtonDisabledStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setButtonDisabledStyle( MenuButtonDisabledStyle )
   */
  public void setButtonDisabledStyle( 
                          final MenuButtonDisabledStyle buttonDisabledStyle ) {
    menuBar.setButtonDisabledStyle( buttonDisabledStyle );
  }
  
  /** <p>Returns the MenuButtonDisabledStyle which is set on the MenuBar in 
    * this CMenu.</p>
    * 
    * @see MenuBar#getButtonDisabledStyle()
    */  
  public MenuButtonDisabledStyle getButtonDisabledStyle() {
    return menuBar.getButtonDisabledStyle();
  }
  
  /** <p>sets the passed MenuButtonEnabledStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setButtonEnabledStyle( MenuButtonEnabledStyle )
   */
  public void setButtonEnabledStyle( 
                             final MenuButtonEnabledStyle buttonEnabledStyle ) {
    menuBar.setButtonEnabledStyle( buttonEnabledStyle );
  }
  
  /** <p>returns the MenuButtonEnabledStyle which is set on the MenuBar 
    * in this CMenu.</p>
    * 
    * @see MenuBar#getButtonEnabledStyle()
    */  
  public MenuButtonEnabledStyle getButtonEnabledStyle() {  
    return menuBar.getButtonEnabledStyle();
  }
  
  /** <p>sets the passed MenuButtonHoverStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setButtonHoverStyle( MenuButtonHoverStyle )
   */
  public void setButtonHoverStyle( 
                                 final MenuButtonHoverStyle buttonHoverStyle ) {
    menuBar.setButtonHoverStyle( buttonHoverStyle );
  }
  
  /** <p>returns the MenuButtonHoverStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getButtonHoverStyle()
    */  
  public MenuButtonHoverStyle getButtonHoverStyle() {
    return menuBar.getButtonHoverStyle();
  }
  
  /** <p>sets the passed MenuItemHoverStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#osetItemHoverStyle( MenuItemHoverStyle )
   */
  public void setItemHoverStyle( final MenuItemHoverStyle itemHoverStyle ) {
    menuBar.setItemHoverStyle( itemHoverStyle );
  }
  
  /** <p>returns the MenuItemHoverStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getItemHoverStyle()
    */  
  public MenuItemHoverStyle getItemHoverStyle() {
    return menuBar.getItemHoverStyle();
  }
  
  /** <p>sets the passed MenuItemEnabledStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setItemEnabledStyle( MenuItemEnabledStyle )
   */
  public void setItemEnabledStyle( 
                                 final MenuItemEnabledStyle itemEnabledStyle ) {
    menuBar.setItemEnabledStyle( itemEnabledStyle );
  }
  
  /** <p>returns the MenuItemEnabledStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getItemEnabledStyle()
    */  
  public MenuItemEnabledStyle getItemEnabledStyle() {
    return menuBar.getItemEnabledStyle();
  }

  /** <p>sets the passed MenuItemDisabledStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setItemDisabledStyle( MenuItemDisabledStyle )
   */
  public void setItemDisabledStyle( 
                               final MenuItemDisabledStyle itemDisabledStyle ) {
    menuBar.setItemDisabledStyle( itemDisabledStyle );
  }
  
  /** <p>returns the MenuItemDisabledStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getItemDisabledStyle()
    */  
  public MenuItemDisabledStyle getItemDisabledStyle() {
    return menuBar.getItemDisabledStyle();
  }
  
  /** <p>sets the passed MenuPopupStyle to the MenuBar in this 
   * CMenu.</p>
   * 
   * @see MenuBar#setMenuPopupStyle( MenuPopupStyle )
   */
  public void setMenuPopupStyle( final MenuPopupStyle menuPopupStyle ) {
    menuBar.setMenuPopupStyle( menuPopupStyle );
  }
 
  /** <p>returns the MenuPopupStyle which is set on the MenuBar in this 
    * CMenu.</p>
    * 
    * @see MenuBar#getMenuPopupStyle()
    */  
  public MenuPopupStyle getMenuPopupStyle() {
    return menuBar.getMenuPopupStyle();
  }
  
  ////////////////
  // inner classes
  
  /** <p>encapsulates information about menu items, as specified in the 
    * custom menu configuration file (which is an xml file that contains 
    * information about the menu items, their name, to which CMenu they belong, 
    * and which command is triggered by them).</p>
    */
  private static class MenuEntry {

    private String menu;
    private String label;
    private String id;
    private String command;
  
    /** Creates a new instance of MenuEntry. */
    private MenuEntry( final String menu, 
                       final String label, 
                       final String id, 
                       final String command ) 
    {
      this.menu = menu;
      this.label = label;
      this.id = id;
      this.command = command;
    }


    // attribute getters and setters
    ////////////////////////////////

    private String getCommand() {
      return command;
    }

    private String getLabel() {
      return label;
    }

    private String getMenu() {
      return menu;
    }
  
    private String getId() {
      return id;
    }
  }  

}