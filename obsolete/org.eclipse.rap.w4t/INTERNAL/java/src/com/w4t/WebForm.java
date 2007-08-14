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
package com.w4t;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.lifecycle.*;

import com.w4t.IWindowManager.IWindow;
import com.w4t.engine.util.FormManager;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebFormEvent;
import com.w4t.event.WebFormListener;
import com.w4t.internal.adaptable.IFileUploadAdapter;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.types.WebColor;
import com.w4t.types.WebTriState;
import com.w4t.util.DefaultColorScheme;

/** <p>WebForm is the root of a component-tree that represents the 
  * html document as shown in the browser on the server. A W4Toolkit application
  * consists of one or more WebForms with user interface components (widgets) 
  * that display information to the user and react to user action (like 
  * mouse clicks etc.).</p>
  *
  * <p>A WebForm may open another WebForm in the
  * same window (see {@link W4TContext#dispatchTo(WebForm) 
  * W4TContext#dispatchTo(WebForm)}), or open another WebForm in a new browser 
  * window (see {@link W4TContext#showInNewWindow(WebForm) 
  * W4TContext#showInNewWindow(WebForm)}). It usually contains widgets like 
  * WebLabels, WebButtons etc.</p>
  *
  * <p>To design a WebForm, extend this class and implement the 
  * abstract method {@link #setWebComponents() setWebComponents()}. 
  * When the WebForm is created, setWebComponents() is called automatically.
  * </p>
  * <p>For example:<pre>
  * public class MyWebForm extends WebForm {
  * 
  *   private WebLabel label;
  *
  *   protected void setWebComponents() throws Exception {
  *     label = new WebLabel();
  *     label.setValue( "Hello World" );
  *   }
  * }
  * </pre></p>
  *
  * <p>For more information on how to place components on a WebForm 
  * and how to arrange them using layout managers, see {@link 
  * org.eclipse.rwt.WebContainer WebContainer}.</p>
  *
  * <p>WebForm instances are not created with the new-Operator, 
  * but must be loaded with the factory method {@link 
  * W4TContext#loadForm(String) W4TContext.loadForm(String)} 
  * instead. Thus obtaining a WebForm instance works like this:</p>
  *
  * <p><pre>
  * // create an instance of MyForm and register it with the W4T System
  * WebForm form = W4TContext.loadForm( "com.mycorp.w4tapp.MyForm" );
  * // cast into a MyForm, if needed
  * MyForm myForm = ( MyForm )form;
  * </pre></p>
  */
public abstract class WebForm extends WebContainer {

  /** <p>A reference to this WebForm (for use in anonymous event 
   * listener classes).</p> */
  public final WebForm self = this;  
  private boolean multipartFormEncoding = false;
  /** a counter which enshures, that only the latest rendering
    * of this WebForm can send data to the WebComponents of the form */
  private int requestCounter = 0;
  /** id of the WebComponent of this form which gets the focus */
  private String focusID = "";
  /** flag, which tells if the WebForm is currently shown in a browser */
  private boolean active = false;
  /** the special timeout for closing this WebForm, overrides the standard
    * closing timeout for forms as set in the configuration file */
  private long closingTimeout = -1;
  /** contains all css files set to this WebForm, which appear in the
    * render output as "link rel" tag.*/
  private List cssFiles;
  
  /** tells whether the browsers navigation buttons are 
   *  enabled for this WebForm */
  private boolean enableBrowserNavigation = false;
  
  private String windowOpenerBuffer = "";
  private String windowRefresherBuffer = "";
  private String windowCloserBuffer = "";
  
  /** milliseconds since the WebForm's last rendering */
  private long timeStamp;


  
  // window handling attributes
  /////////////////////////////
  
  /** flag: whether the browser window containing this WebForm
    * will be refreshed */
  private boolean refreshWindow = false;
  /** flag: whether this WebForm is opened in a new browser window */
  private boolean showInNewWindow = false;
  /** a properties class which encapsulates information for the
    * browser instance, if this WebForm is shown in a new window */
  private WindowProperties windowProperties = null;
  /** flag: whether the position of the horizontal and vertical scrollbars in
    * the browser window which is displaying this WebForm is preserved
    * between renderings. If true, the scrollbars are set at the newly rendered
    * html page in the browser window to the same position as before the
    * rendering. */
  private boolean autoScroll = true;
  /** the position (in pixels) of the horizontal scrollbar in the browser
    * window displaying this WebForm. */
  private int scrollX = 0;
  /** the position (in pixels) of the horizontal scrollbar in the browser
    * window displaying this WebForm. */
  private int scrollY = 0;
  /** the title of the window, in which this WebForm is to be displayed. */
  private String windowTitle = "";
  
  
  // body attributes
  //////////////////

  /** <p>the background color of this WebForm.</p> */
  private WebColor bgColor;
  /** <p>the global text color on this WebForm.</p> */
  private WebColor textColor;
  
  /** left position of the forms layout manager */
  private String leftmargin = "0";
  /** top position of the forms layout manager */
  private String topmargin = "0";
  /** left border width to the forms layout managers table */
  private String marginwidth = "0";
  /** top border width of the forms to the layout managers table */
  private String marginheight = "0";
  
  
  // design time specific fields
  //////////////////////////////
  
  private IFormAdapter formAdapter;

  
  /** <p>Used by the W4 Toolkit library to create a new instance of 
    * this WebForm - in order to obtain an instance that is properly 
    * initialised and registered with the system, use the factory method 
    * {@link org.eclipse.rwt.W4TContext#loadForm(String) loadForm(String)} 
    * instead.</p> */
  public WebForm() {
    refreshTimeStamp();
    cssFiles = new ArrayList( 4 );
    String sBgColor 
      = DefaultColorScheme.get( DefaultColorScheme.WEB_OBJECT_BG );
    bgColor = new WebColor( sBgColor );
    String sTextColor 
      = DefaultColorScheme.get( DefaultColorScheme.WEB_FORM_TEXT );
    textColor = new WebColor( sTextColor );
    initWindowProperties();
  }

  /**
    * inits the properties for the window in which this WebForm is
    * displayed, if it opens a new browser instance.
    */
  private void initWindowProperties() {
    windowProperties = new WindowProperties();
    windowProperties.setLocation( new WebTriState( "no" ) );
    windowProperties.setMenubar( new WebTriState( "no" ) );
    windowProperties.setToolbar( new WebTriState( "no" ) );
    windowProperties.setStatus( new WebTriState( "no" ) );
    windowProperties.setDirectories( "no" );
    windowProperties.setDependent( "no" );
    windowProperties.setResizable( new WebTriState( "yes" ) );
    windowProperties.setScrollbars( new WebTriState( "yes" ) );
    windowProperties.setStatus( new WebTriState( "yes" ) );
  }

  /** returns a clone of this WebForm.<br>
    * For all elementary WebComponents, cloning will result in a deep copy,
    * shallow copying all fields, cloning property objects (i.e. all
    * subclasses of WebComponentProperties, like Style, WindowProperties
    * etc.), and in case the WebObject to clone is a WebContainer, also
    * cloning all WebComponents, if they were added to it using
    * WebContainer.add(), and the WebLayout, if it was set using
    * WebContainer.setWebLayout(). Fields containing external references to
    * Objects which are not added with one of those methods are set to null
    * in the cloned object.
    */
  public Object clone() throws CloneNotSupportedException {
    WebForm result = ( WebForm )super.clone();
    // inits
    result.requestCounter = 0;
    result.windowProperties = ( WindowProperties )this.windowProperties.clone();
    result.bgColor = ( WebColor )this.bgColor.clone();
    return result;
  }

  private void postRender() {
    increaseRequestCounter();
    refreshTimeStamp();
  }

  /** {@link #requestCounter requestCounter} */
  private void increaseRequestCounter() {
    requestCounter++;
  }

  /** <p>the main entry point for initialisation code. This method is executed
    * when an instance of this WebForm is created. It will usually be
    * used to add and initialise widgets (see {@link org.eclipse.rwt.WebContainer
    * WebContainer} for more information about how to add WebComponents and
    * arrange them using layout managers.</p>
    *
    * @throws Exception  whenever an Exception occurs during the 
    *                    initialisation code.
    */
  protected abstract void setWebComponents() throws Exception;

  /**
   * Unloads a this WebForm from session scope.
   */
  public void unload() {
    // before unloading the form fire the closing event
    try {
      if( active ) {
        active = false;
        int webEventType = WebFormEvent.WEBFORM_CLOSING;
        WebFormEvent evt = new WebFormEvent( this, webEventType );
        evt.processEvent();
      }
    } finally {
      FormManager.remove( this );
      WindowManager.removeAssociatedWindow( this );
    }
  }

  /**
    * Adds the specified WebFormListener to receive webFormClosing
    * events of this WebForm. WebFormClosing Events occur when a
    * a browser window is closed of a external url is loaded
    * @param listener the WebFormListener
    */
  public void addWebFormListener( final WebFormListener listener ) {
    WebFormEvent.addListener( this, listener );
  }

  /**
    * removes the specified WebFormListener which receives
    * webFormClosing events of this WebForm.
    * WebFormClosing Events occur when a
    * a browser window is closed of a external url is loaded
    * @param listener the WebFormListener
    */
  public void removeWebFormListener( final WebFormListener listener ) {
    WebFormEvent.removeListener( this, listener );
  }

  /**
    * <p>
    * closes the browser window displaying this WebForm.
    * </p>
    * <p>
    * <strong>Note:</strong> The WebForm is <em>not</em> unloaded when closing 
    * the window.
    * </p>
    */
  public void closeWindow() {
    final IWindow window = W4TContext.getWindowManager().findWindow( this );
    if( window != null ) {
      window.close();
      W4TContext.getLifeCycle().addPhaseListener( new PhaseListener() {
        private static final long serialVersionUID = 1L;
        public void afterPhase( final PhaseEvent event ) {
          if( WindowManager.isClosed( window ) ) {
            WindowManager.getInstance().remove( window );
            W4TContext.getLifeCycle().removePhaseListener( this );
          }
        }
        public void beforePhase( final PhaseEvent event ) {
        }
        public PhaseId getPhaseId() {
          return PhaseId.RENDER;
        }
      } );
    }
  }

  /**
    * <p>
    * Refreshes the browser window displaying this WebForm.
    * </p>
    * <p>
    * Usually it is not necessary to refresh the WebForm currently being 
    * processed. But it might be necessary to refresh another form 
    * that is shown in a popup window. 
    * <p>
    * <strong>Note:</strong> The effect of any previous call to 
    * {@link #closeWindow <code>closeWindow</strong>} will be overridden by 
    * this method.
    * </p> 
    */
  public void refreshWindow() {
    IWindow window = WindowManager.getInstance().findWindow( this );
    if( window == null ) {
      // TODO [rh] throw IllegalStateException
      window = WindowManager.getInstance().create( this );
    } else if( WindowManager.isClosing( window ) ) {
      WindowManager.setClosing( window, false );
    }
    this.refreshWindow = true;
  }

  /**
    * returns whether the browser window displaying this WebForm will
    * be refreshed
    */
  public boolean isRefreshing() {
    return refreshWindow;
  }
  
  /**
    * returns whether this WebForm will be displayed in a new
    * browser window
    */
  public boolean isOpeningNewWindow() {
    return showInNewWindow;
  }

  /** sets the window properties for this WebForm */
  public void setWindowProperties( final WindowProperties windowProperties ) {
    this.windowProperties = windowProperties;
  }

  /** <p>returns the window properties for this WebForm.</p> */
  public WindowProperties getWindowProperties() {
    return windowProperties;
  }

  /** <p>sets the background color for this WebForm.</p>
    * <p>Corresponds to the HTML attribute bgcolor of the tag 
    * &lsaquo;body&rsaquo;.</p>
    *
    * @param bgColor  specifies the chosen color. Can either be a
    *                 hexadecimal RGB-value (red/green/blue-value 
    *                 of the color) or one of 16 color names (like 
    *                 "black", "white", "red" etc.).
    */
  public void setBgColor( final WebColor bgColor ) {
    this.bgColor = bgColor;
  }

  /** <p>gets the background color for this WebForm.</p>
    * <p>Corresponds to the HTML attribute bgcolor of the tag 
    * &lsaquo;body&rsaquo;.</p>
    */
  public WebColor getBgColor() {
    return bgColor;
  }

  /** <p>sets the global text color on this WebForm.</p> */
  public void setTextColor( final WebColor textColor ) {
    this.textColor = textColor;
  }

  /** <p>returns the global text color on this WebForm.</p> */  
  public WebColor getTextColor() {
    return textColor;
  }
  
  private void refreshTimeStamp() {
    timeStamp = System.currentTimeMillis();
  }
  
  /** sets, whether the position of the horizontal and vertical scrollbars in
    * the browser window which is displaying this WebForm is preserved
    * between renderings. If true, the scrollbars are set at the newly rendered
    * html page in the browser window to the same position as before the
    * rendering. */
  public void setAutoScroll( final boolean autoScroll ) {
    this.autoScroll = autoScroll;
  }

  /** returns, whether the position of the horizontal and vertical scrollbars in
    * the browser window which is displaying this WebForm is preserved
    * between renderings. If true, the scrollbars are set at the newly rendered
    * html page in the browser window to the same position as before the
    * rendering. */
  public boolean isAutoScroll() {
    return autoScroll;
  }

  /** 
   * <p>Sets the position of the horizontal scrollbar of the browser window,
   * in which this WebForm is displayed (in pixels).</p>
   */
  public void setScrollX( final int scrollX ) {
    this.scrollX = scrollX;
  }

  /** 
   * <p>Returns the position of the horizontal scrollbars of the browser 
   * window, in which this WebForm is displayed (in pixels).</p>
   */
  public int getScrollX() {
    return scrollX;
  }

  /** 
   * <p>Sets the position of the vertical scrollbar of the browser window,
   * in which this WebForm is displayed (in pixels).</p>
   */ 
  public void setScrollY( final int scrollY ) {
    this.scrollY = scrollY;
  }

  /** 
   * <p>Returns the position of the vertical scrollbar of the browser window,
   * in which this WebForm is displayed (in pixels).</p>
   */
  public int getScrollY() {
    return scrollY;
  }

  /** <p>Sets the title of the window, in which this WebForm is
    * to be displayed.</p> */
  public void setWindowTitle( final String windowTitle ) {
    this.windowTitle = windowTitle;
  }

  /** returns the title of the window, in which this WebForm is
    * to be displayed. */
  public String getWindowTitle() {
    return windowTitle;
  }

  /** sets the id of the WebComponent of this form which gets the focus */
  public void setFocusID( final String focusID ){
    this.focusID = focusID;
  }

  /** gets the id of the WebComponent which gets the focus,
  * if not set returns id of this form  */
  public String retrieveFocusID() {
    return focusID;
  }

  /** left position of the form layout manager */
  public void setLeftmargin( final String leftmargin ) {
    this.leftmargin = leftmargin;
  }

  /** left position of the form layout manager */
  public String getLeftmargin() {
    return leftmargin;
  }

  /** top position of the form layout manager */
  public void setTopmargin( final String topmargin ) {
    this.topmargin = topmargin;
  }

  /** top position of the form layout manager */
  public String getTopmargin() {
    return topmargin;
  }

  /** left border width to the forms layout managers table */
  public void setMarginwidth( final String marginwidth ){
    this.marginwidth = marginwidth;
  }

  /** left border width to the forms layout managers table */
  public String getMarginwidth(){
    return marginwidth;
  }

  /** top border width of the forms to the layout managers table */
  public void setMarginheight( final String marginheight ) {
    this.marginheight = marginheight;
  }

  /** top border width of the forms to the layout managers table */
  public String getMarginheight() {
    return marginheight;
  }
  
  /** sets whether the browsers navigation buttons are 
   *  enabled for this WebForm */
  public void setEnableBrowserNavigation( 
    final boolean enableBrowserNavigation )
  {
    this.enableBrowserNavigation = enableBrowserNavigation;
  }
  
  /** returns whether the browsers navigation buttons are 
   *  enabled for this WebForm */
  public boolean isEnableBrowserNavigation() {
    return enableBrowserNavigation;
  }

  /** <p>Sets the special timeout, in ms, for closing this WebForm,
    * overrides the standard closing timeout for forms as set in the
    * configuration file.</p>
    * 
    * <p>As long as a WebForm is opened in a browser (JavaScript enabled
    * only), the form will be triggered from time to time even if there is no 
    * user action. This means neither the WebForm closing timeout is triggered,
    * nor the user session expires. After the user closes the window or 
    * navigates to another form, the closing timout countdown starts. This
    * allows very short session timeouts, but having the user running long
    * sessions without bothering running into such a session timeout by e.g. 
    * editing the content of a page to slowly.</p>
    *  
    * <p>Note: the timeout is only an approxymately value, since it depends on a  
    * cleanup thread running from time to time. So the shorter the timeout the  
    * more inexact is the real trigger of the closing timeout relatively to  
    * the timeout settings.</p> 
    * 
    * @param closingTimeout The value for closingTimeout must be at least 60.*/
  public void setClosingTimeout( final long closingTimeout ) {
    if ( closingTimeout >= 60 ) {
      this.closingTimeout = closingTimeout;
    }
  }

  /** <p>returns the special timeout for closing this WebForm, overrides
    * the standard closing timeout for forms as set in the configuration
    * file.</p> */
  public long getClosingTimeout() {
    return closingTimeout;
  }
  
  /** <p>sets the passed css files to this WebForm.</p>
    * <p>Note that the given <code>cssFiles</code> must not contain duplicate 
    * entries.</p>
    * <p>A css file that is set to a WebForm is referenced in the HTML 
    * output from a &lt;link rel=stylesheet type="text/css"&gt; tag. The
    * href attribute of this tag is set to what is set here.</p> 
    */
  public void setCssFile( final String[] cssFiles ) {
    List buffer = new ArrayList();
    for( int i = 0; i < cssFiles.length; i++ ) {
      if( buffer.contains( cssFiles[ i ] ) ) {
        String text = "The parameter 'cssFiles' contains duplicate entries.";
        throw new IllegalArgumentException( text );
      }
      buffer.add( cssFiles[ i ] );
    }
    this.cssFiles.clear();
    for( int i = 0; i < cssFiles.length; i++ ) {
      this.cssFiles.add( cssFiles[ i ] );
    }
  }

  /** <p>Sets the passed css file to this WebForm at the specified index.</p>
    * <p>Note that duplicate entries are not allowed.</p>
    * <p>A css file that is set to a WebForm is referenced in the HTML 
    * output from a &lt;link rel=stylesheet type="text/css"&gt; tag. The
    * href attribute of this tag is set to what is set here.</p> 
    */
  public void setCssFile( final int index, final String cssFile ) {
    int existingIndex = cssFiles.indexOf( cssFile );
    if( existingIndex != -1 && existingIndex != index ) {
      String text 
        = "The parameter 'cssFile' already exists in the list of cssFiles. " 
        + "Duplicate entries are not allowed.";
      throw new IllegalArgumentException( text );
    }
    cssFiles.set( index, cssFile );
  }

  /** <p>returns the css files which were set to this WebForm.</p>
    *
    * <p>A css file that is set to a WebForm is referenced in the HTML 
    * output from a &lt;link rel=stylesheet type="text/css"&gt; tag. The
    * href attribute of this tag is set to what is set here.</p> 
    */
  public String[] getCssFile() {
    String[] result = new String[ cssFiles.size() ];
    cssFiles.toArray( result );
    return result;
  }

  /** <p>returns the css file which was set to this WebForm at the 
    * specified index.</p>
    *
    * <p>A css file that is set to a WebForm is referenced in the HTML 
    * output from a &lt;link rel=stylesheet type="text/css"&gt; tag. The
    * href attribute of this tag is set to what is set here.</p> 
    */
  public String getCssFile( final int index ) {
    return ( String )cssFiles.get( index );
  }
  
  /** <p>adds the passed css files to the css files already set to 
    * this WebForm.</p>
    * <p>Note that duplicate entries will be ignored.</p>
    * <p>A css file that is set to a WebForm is referenced in the HTML 
    * output from a &lt;link rel=stylesheet type="text/css"&gt; tag. The
    * href attribute of this tag is set to what is set here.</p> 
    */
  public void addCssFile( final String cssFile ) {
    if( !cssFiles.contains( cssFile ) ) {
      cssFiles.add( cssFile );
    }
  }
  
  public Object getAdapter( final Class adapter ) {
    Object result = null;
    if( adapter == IFileUploadAdapter.class ) {
      result = createFileUploadAdapter();
    } else if( adapter == IFormAdapter.class ) {
      result = getFormAdapter();
    } else {
      result = super.getAdapter( adapter );
    }
    return result;
  }

  private Object createFileUploadAdapter() {
    return new IFileUploadAdapter() {
      public void setFileItem( final FileItem newFileItem ) {
      }
      public boolean isMultipartFormEncoding() {
        return multipartFormEncoding;
      }
      public void setMultipartFormEncoding( final boolean encoding ) {
        multipartFormEncoding = encoding;
      }
    };
  }
  
  private Object getFormAdapter() {
    if( formAdapter == null ) {
      formAdapter = new IFormAdapter() {
        private HtmlResponseWriter renderBuffer;
        private void init() {
          if( renderBuffer == null ) {
            renderBuffer = new HtmlResponseWriter();
            renderBuffer.append( "      The WebForm '" ); 
            renderBuffer.append( getClass().getName() );
            renderBuffer.append( "' could not be rendered." );
          }
        }
        public void postRender() {
          IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
          this.renderBuffer = stateInfo.getResponseWriter();
          init();
          WebForm.this.postRender();
        }
        public HtmlResponseWriter getRenderBuffer() {
          init();
          refreshTimeStamp();
          return renderBuffer;
        }
        public void increase() {
          increaseRequestCounter();
        }
        public long getTimeStamp() {
          return timeStamp;
        }
        public void refreshTimeStamp() {
          WebForm.this.refreshTimeStamp();
        }
        public int getRequestCounter() {
          return requestCounter;
        }
        public void updateRequestCounter( final int requestCounter ) {
          WebForm.this.requestCounter = requestCounter;
        }
        public void refreshWindow( final boolean refreshWindow ) {
          WebForm.this.refreshWindow = refreshWindow;
        }
        public void showInNewWindow( final boolean showInNewWindow ) {
          WebForm.this.showInNewWindow = showInNewWindow;
        }
        public void addWindowOpenerBuffer( final String windowOpenerBuffer ) {
          WebForm.this.windowOpenerBuffer = windowOpenerBuffer;
        }
        public String getWindowOpenerBuffer() {
          return windowOpenerBuffer;
        }
        public void addWindowRefresherBuffer( final String refresherBuffer ) {
          WebForm.this.windowRefresherBuffer = refresherBuffer;
        }
        public String getWindowRefresherBuffer() {
          return windowRefresherBuffer;
        }
        public void addWindowCloserBuffer( final String closerBuffer ) {
          WebForm.this.windowCloserBuffer = closerBuffer;
        }
        public String getWindowCloserBuffer() {
          return windowCloserBuffer;
        }
        public void setActive( final boolean active ) {
          WebForm.this.active = active;
        }
        public boolean isActive() {
          return active;
        }
      };
    }
    return formAdapter;
  }
}