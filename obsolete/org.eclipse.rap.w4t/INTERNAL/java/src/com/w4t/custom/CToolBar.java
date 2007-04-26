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
import com.w4t.*;
import com.w4t.engine.util.ResourceManager;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebActionListener;
import com.w4t.types.LocalPath;
import com.w4t.types.WebColor;
import com.w4t.util.Assert;
import com.w4t.util.DefaultColorScheme;


/** <p>A convenience component that consists of a row of buttons that 
  * can be configured declaratively in an XML file.</p>
  * 
  * <p>The configuration (images for the buttons and commands which are 
  * triggered by these buttons) is read from an XML file which can be 
  * set by {@link #setConfigFile(LocalPath) setConfigFile(LocalPath)}
  * or {@link #setConfigResource(String) setConfigResource(String)}.</p>
  * 
  * <p>A configuration file for CToolBar should look like this:</p>
  * <p><pre><code>
  *   &lt;CToolBar&gt;
  * 
  *     &lt;toolBarButton&gt;
  *       &lt;!-- entry for a button --&gt;
  *       &lt;image&gt;resources/images/myImage.gif&lt;/image&gt;
  *       &lt;toolTip&gt;Text that appears on mouse over.&lt;/toolTip&gt;
  *       &lt;id&gt;NEW_FILE&lt;/id&gt;
  *       &lt;command&gt;com.and.go.NewFileCommand&lt;/command&gt;
  *     &lt;/toolBarButton&gt;
  * 
  *     &lt;!-- more entries go here --&gt;
  * 
  *   &lt;/CToolBar&gt;
  * </pre></code></p>
  * 
  * <p>Specifying an <strong>&lt;id&gt;</strong> is obligatory. On click the 
  * CToolBar invokes the <code>execute</code> method on a new instance of the 
  * class that was specified in &lt;command&gt;. This class must implement 
  * the <code>ICustomAction</code> interface. To notify the action about 
  * which <strong>&lt;id&gt;</strong> is to be executed the 
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
  * @see com.w4t.custom.ICustomAction
  */
public class CToolBar extends WebPanel implements Concealer {

  /** <p>the file that contains the toolbar configuration.</p> */
  private LocalPath configFile;

  /** <p>the background color of this CToolBar.</p>*/
  private WebColor bgColor;
  /** <p>the color of the border that appears around a button on this 
   * CToolBar when the  user moves the mouse over that button.</p> */
  private WebColor hoverBorderColor;

  private String configResource;

  /** <p>Constructs a new CToolBar.</p> */
  public CToolBar()  {
    String bg = DefaultColorScheme.get( DefaultColorScheme.WEB_OBJECT_BG );
    bgColor = new WebColor( bg );
    String hover = DefaultColorScheme.get( DefaultColorScheme.MENU_BORDER );
    hoverBorderColor = new WebColor( hover );
  }

  public Object clone() throws CloneNotSupportedException {
    CToolBar result = ( CToolBar )super.clone();
    result.setBgColor( this.getBgColor() );
    result.setHoverBorderColor( this.getHoverBorderColor() );
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
    return "resources/images/icons/ctoolbar.gif";
  } 
  
  
  // attribute getters and setters
  ////////////////////////////////

  /** <p>Sets the file from which the configuration of this CToolBar is 
   * read.</p>
   * <p>Note: Using <code>setConfigFile</code> to initialize this 
   * <code>CToolBar</code> instance, discards the <code>setConfigResource</code>
   * initializations.</p>
   */
  public void setConfigFile( final LocalPath configFile ) {
    Assert.isNotNull( configFile );
    Assert.isTrue( configFile.toFile().exists() );
    this.configFile = configFile;
    this.configResource = null;
    try {
      initialiseButtons();
    } catch( final Exception ex ) {
      String txt =   "The file \"{0}\" does not exist or is not a "
                   + "valid configuration file.";
      Object[] params = new Object[]{ configFile.toFile().toString() };
      String msg = MessageFormat.format( txt, params );
      throw new IllegalArgumentException( msg );
    }

  }

  /** <p>Returns the file from which the configuration of this CToolBar is 
   * read.</p>
   */
  public LocalPath getConfigFile() {
    return configFile;
  }
  
  /**
   * <p>Loads the given configuration resource from the application's 
   * classpath.</p>
   * <p>Note: Using <code>setConfigResource</code> to initialize this 
   * <code>CToolBar</code> instance, discards the <code>setConfigFile</code>
   * initializations.</p>
   * @param configResource the path to an toolbar configuration file that is
   *                       available on the application's classpath.
   */
  public void setConfigResource( final String configResource ) {
    Assert.isNotNull( configResource );
    this.configResource = configResource;
    this.configFile = null;
    try {
      initialiseButtons();
    } catch( final Exception ex ) {
      String txt =   "The file \"{0}\" does not exist or is not a "
                   + "valid configuration file.";
      String msg = MessageFormat.format( txt, new Object[]{ configResource } );
      throw new IllegalArgumentException( msg );
    }
  }
  
  /**
   * <p>Returns the configuration resource available on the application's 
   * classpath that was used to initialize this <code>CToolBar</code> or
   * <code>null</code>, if not available.</p>
   */

  public String getConfigResource() {
    return configResource;
  }

  /** <p>Returns the background color of this CToolBar.</p>*/
  public WebColor getBgColor() {
    return bgColor;
  }

  /** <p>Sets the background color of this CToolBar.</p>*/
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /** <p>Returns the color of the border that appears around a button on this 
   * CToolBar when the  user moves the mouse over that button.</p> */
  public WebColor getHoverBorderColor() {
    return hoverBorderColor;
  }

  /** <p>Sets the color of the border that appears around a button on this 
   * CToolBar when the  user moves the mouse over that button.</p> */
  public void setHoverBorderColor( final WebColor hoverBorderColor ) {
    this.hoverBorderColor = hoverBorderColor;
  }


  // helping methods
  //////////////////
  
  private void initialiseButtons() throws Exception {
    removeAll();
    ToolBarButton[] toolBarButtons = getToolBarButtons();
    initGrid( toolBarButtons.length );
    for( int i = 0; i < toolBarButtons.length; i++ ) {
      WebButton wbt = createButton( toolBarButtons[ i ] );
      WebBorderComponent wbc = new WebBorderComponent();
      wbc.setBorderType( WebBorderComponent.HOVER );
      wbc.setDarkColor( hoverBorderColor );
      wbc.setLightColor( hoverBorderColor );
      wbc.setBgColor( getBgColor() );
      wbc.setPadding( 3 );
      wbc.setContent( wbt );
      add( wbc, new Position( 1, i + 1 ) );
    }      
  }
  
  private void initGrid( final int count ) {
    WebGridLayout gridLayout = new WebGridLayout( 1, count );
    setWebLayout( gridLayout );
    gridLayout.setWidth( "" );
    gridLayout.setCellpadding( "2" );
    gridLayout.setBgColor( getBgColor() );
  }
  
  private WebButton createButton( final ToolBarButton desc ) {
    WebButton result = new WebButton();
    result.setImage( desc.getImage() );
    result.setTitle( desc.getToolTip() );
    if( !desc.getId().equals( "" ) ) {
      final String className = desc.getCommand().trim();
      final ClassLoader contextLoader
        = ResourceManager.getInstance().getContextLoader();
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
              = "ICustomAction not found: {0} [{1}]\nfor toolbar button."; 
            Object[] params = new Object[] { 
              desc.getId(), 
              className
            };
            String txt = MessageFormat.format( msg, params );
            throw new RuntimeException( txt, ex );
          }
          command.init( desc.getId() );
          command.execute();
        }
      } );
    }    
    return result;
  }
  
  private ToolBarButton[] getToolBarButtons() throws Exception {
    String fileName;
    if( getConfigFile() != null ) {
      fileName = getConfigFile().toFile().toString();
    } else {
      fileName = configResource;
    }
    InitialisationReader reader = new InitialisationReader( fileName );
    
    int count = reader.selectContent( "toolBarButton" );
    ToolBarButton[] result = new ToolBarButton[ count ];
    for( int i = 0; i < count; i++ ) {
      String image = reader.getAttribute( "image" );
      String toolTip = reader.getAttribute( "toolTip" );
      String id = reader.getAttribute( "id" );
      String command = reader.getAttribute( "command" );
      result[ i ] = new ToolBarButton( image, toolTip, id, command );
      reader.nextContent();      
    }
    return result;    
  }
  
  
  ////////////////
  // inner classes
  
  /** <p>Encapsulates information about toolbar buttons, as specified in the 
    * custom toolbar configuration file (which is an xml file that contains 
    * information about the buttons, their images, and which command is 
    * triggered by them).</p>
    */
  private static class ToolBarButton {

    private String image;
    private String toolTip;
    private String id;
    private String command;

    private ToolBarButton( final String image,
                           final String toolTip,
                           final String id, 
                           final String command ) 
    {
      this.image = image;
      this.toolTip = toolTip;
      this.id = id;
      this.command = command;
    }

    // attribute getters and setters
    ////////////////////////////////

    private String getImage() {
      return image;
    }

    private String getToolTip() {
      return toolTip;
    }

    private String getId() {
      return id;
    }
  
    private String getCommand() {
      return command;
    }
  }  
}