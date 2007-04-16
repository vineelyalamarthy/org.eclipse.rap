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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.w4t.dhtml.event.DoubleClickEvent;
import com.w4t.dhtml.event.DragDropEvent;
import com.w4t.engine.requests.RequestParams;
import com.w4t.engine.requests.URLHelper;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.ResourceManager;
import com.w4t.event.WebActionEvent;
import com.w4t.event.WebItemEvent;
import com.w4t.internal.adaptable.IFormAdapter;
import com.w4t.types.WebColor;
import com.w4t.util.*;
import com.w4t.util.browser.Mozilla1_6up;

/**
 * <p>Contains helping methods commonly needed in rendering components and
 * layouting containers.</p>
 */
public final class RenderUtil {

  private static final Pattern DOUBLE_HYPHEN_PATTERN = Pattern.compile( "--" );
  
  public final static String SUBMITTER_IMAGE
    = com.w4t.util.image.ImageCache.STANDARD_SUBMITTER_IMAGE;
  public final static String DRAG_DROP_IMAGE = "resources/images/dragDrop.gif";
  public final static String DOUBLE_CLICK_IMAGE 
    = "resources/images/doubleclick.gif";
  public final static String PREFIX_STATE_INFO = "stateInfo_";
  
  private static final String INVISIBLE_STYLE 
    = "display:none;visibility:hidden";


  /**
   * <p>Creates the html code for an additional image button that triggers the
   * itemStateChanged on components when rendered for noscript.</p>
   * @param componentId the unique id of the component for which the 
   * itemStateChange-button should be created.
   * @throws IOException if an I/O error occurs
   * @see WebItemEvent
   */
  public static void writeItemSubmitter( final String componentId )
    throws IOException
  {
    writeSubmitter( SUBMITTER_IMAGE, appendItemPrefix( componentId ), "", "" );
  }

  /**
   * <p>Creates the html code for an additional image button that triggers the
   * actionEvent on components when rendered for noscript.</p>
   * @param componentId the unique id of the component for which the 
   * actionEvent-button should be created.
   * @throws IOException if an I/O error occurs
   * @see WebActionEvent
   */
  public static void writeActionSubmitter( final String componentId )
    throws IOException
  {
    writeSubmitter( SUBMITTER_IMAGE, 
                    appendActionPrefix( componentId ), 
                    "", 
                    "" );
  }

  /**
   * <p>Creates the html code for an additional image button that triggers the
   * actionEvent on components when rendered for noscript.</p>
   * @param imageName the name of the image to be used 
   * @param componentId the unique id of the component for which the
   * actionEvent-button should be created.
   * @param alt the alternate text used for the image button
   * @param styleClass the css class name for the image button
   * @throws IOException if an I/O error occurs
   * @see com.w4t.event.WebActionEvent
   */
  public static void writeActionSubmitter( final String imageName,
                                           final String componentId,
                                           final String alt, 
                                           final String styleClass )
    throws IOException
  {
    writeSubmitter( imageName, 
                    appendActionPrefix( componentId ), 
                    alt, 
                    styleClass );
  }

  public static void writeDoubleClickSubmitter( final String componentId ) 
    throws IOException 
  {
    StringBuffer prefixedId = new StringBuffer();
    prefixedId.append( DoubleClickEvent.PREFIX );
    prefixedId.append( componentId );
    // TODO [rh] i18n
    String alt = "Click here to simulate double-click.";
    writeSubmitter( DOUBLE_CLICK_IMAGE, prefixedId.toString(), alt, "" );
  }
  
  /**
   * <p>Creates the html code for an additional image button that triggers the
   * event denoted by <code>prefixedId</code> on components when rendered for 
   * noscript.</p>
   * @param imageName the name of the image to be used
   * @param prefixedId the unique id - prepended by an event prefix - of the 
   * component for which the event-button should be created.
   * @param alt the alternate text used for the image button
   * @param styleClass the css class name for the image button
   * @throws IOException if an I/O error occurs
   * @see com.w4t.event.WebEvent
   */
  public static void writeSubmitter( final String imageName,
                                     final String prefixedId,
                                     final String alt,
                                     final String styleClass ) 
    throws IOException
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.INPUT, null );
    if( styleClass != null && !"".equals( styleClass ) ) {
      out.writeAttribute( HTML.CLASS, styleClass, null );
    }
    out.writeAttribute( HTML.TYPE, HTML.IMAGE, null );
    out.writeAttribute( HTML.SRC, imageName, null );
    out.writeAttribute( HTML.NAME, prefixedId, null );
    out.writeAttribute( HTML.BORDER, "0", null );
    if( !"".equals( alt ) ) {
      out.writeAttribute( HTML.ALT, alt, null );
    }
    out.endElement( HTML.INPUT );
  }

  /**
   * <p>Wraps the given <code>javaScriptCode</code> with an opening and closing
   * &lt;script&gt; tag. It is ensured that the content of the 
   * <code>javaScriptCode</code> will be encoded or otherwise wrapped to not 
   * corrupt the surrounding markup.</p>
   * <p>The resulting markup looks like
   * <code>&lt;script type="text/javascript"&gt;function myFunc() { ... }
   * &lt;/script&gt;</code>
   * </p>
   */
  public static String createJavaScriptInline( final String javaScriptCode ) {
    StringBuffer result = new StringBuffer();
    if( javaScriptCode != null && !"".equals( javaScriptCode ) ) {
      result.append( "<script" );
      HTMLUtil.attribute( result, HTML.TYPE, HTML.CONTENT_TEXT_JAVASCRIPT );
      result.append( ">" );
      result.append( encodeHTMLEntities( javaScriptCode ) );
      result.append( "</script>" );
    }
    return result.toString();
  }

  /**
   * <p>Uses the given <code>writer</code> to write the given 
   * <code>javaScriptCode</code>. The JavaScript will be surrounded by an 
   * opening and closing &lt;script&gt; tag. The <code>javaScriptCode</code>
   * is not modified (e.g. encoded) in any way. 
   * </p>
   * <p>
   * The resulting markup looks like
   * <code>&lt;script type="text/javascript"&gt;function myFunc() { ... }
   * &lt;/script&gt;</code>
   * </p>
   * @param writer the response writer to be written to
   * @param javaScriptCode the JavaScript code to be embedded inside the tags
   * @throws IOException if an I/O error occurs
   */
  public static void writeJavaScriptInline( final HtmlResponseWriter writer, 
                                            final String javaScriptCode ) 
    throws IOException 
  {
    if( javaScriptCode != null && !"".equals( javaScriptCode ) ) {
      writer.startElement( HTML.SCRIPT, null );
      writer.writeAttribute( HTML.TYPE, HTML.CONTENT_TEXT_JAVASCRIPT, null );
      writer.writeText( javaScriptCode, null );
      writer.endElement( HTML.SCRIPT );
    }
  }
  
  /**
   * <p>
   * Creates a complete &lt;script&gt; tag for the given
   * <code>srcAttribute</code>
   * </p>
   * <p>
   * The resulting markup looks like
   * <code>&lt;script scr="<em>scrAttribute</em>" 
   * type="text/javascript"&gt;&lt;/script&gt;</code>
   * </p>
   */
  public static String createJavaScriptLink( final String srcValue ) {
    StringBuffer result = new StringBuffer();
    result.append( "<script" );
    HTMLUtil.attribute( result, HTML.CHARSET, HTML.CHARSET_NAME_UTF_8 );
    HTMLUtil.attribute( result, HTML.SRC, srcValue );
    HTMLUtil.attribute( result, HTML.TYPE, HTML.CONTENT_TEXT_JAVASCRIPT );
    result.append( "></script>" );
    return result.toString();
  }

  /**
   * <p>Creates the html code for an additional image button that triggers the
   * DragDropEvent on dragdrop enabled components when rendered for noscript.
   * </p>
   * @param componentId the id of the component for which the image button 
   * shoud be rendered. Must not be <code>null</code>
   * @throws IOException if an I/O error occurs
   */
  public static void writeDragDropSubmitter( final String componentId )
    throws IOException
  {
    // TODO [rh] i18n
    writeSubmitter( DRAG_DROP_IMAGE, 
                    appendDragDropPrefix( componentId ),
                    "Click here to simulate drag-drop.", 
                    "" );
  }
  
  /**
   * <p>Writes the following <em>universal attributes</em> for the given 
   * <code>component</code>:
   * <ul><li>class</li>
   * <li>dir</li>
   * <li>lang</li>
   * <li>title</li>
   * </ul>As the method name indicates the &lt;style&gt; attribute is not 
   * written.</p>
   * @param component the component whose universal attributes should be
   * written
   * @throws IOException if an I/O error occurs
   * @see #hasUniversalAttributes(SimpleComponent)
   * @see #writeUniversalAttributes(SimpleComponent)
   */
  public static void writeUniversalAttributesWithoutStyle( 
    final SimpleComponent component )
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    String cssClass = component.getCssClass();
    if( !isEmpty( cssClass ) ) {
      out.writeAttribute( HTML.CLASS, cssClass, null );
    }
    String dir = component.getDir();
    if( !isEmpty( dir ) ) {
      out.writeAttribute( HTML.DIR, dir, null );
    }
    String lang = component.getLang();
    if( !isEmpty( lang ) ) {
      out.writeAttribute( HTML.LANG, lang, null );
    }
    String title = resolve( component.getTitle() );
    if( !isEmpty( title ) ) {
      out.writeAttribute( HTML.TITLE, title, null );
    }
  }

  /**
   * <p>Returns whether the given <code>cmp</code> has any non-empty 
   * <em>universal attribute</em>.</p> 
   * @param cmp the component whose universal attributes should be
   * inspected
   */
  public static boolean hasUniversalAttributes( final SimpleComponent cmp ) {
    return    !isEmpty( cmp.getStyle().toString() )
           || hasUniversalAttributesWithoutStyle( cmp );
  }

  /**
   * <p>Writes the following <em>universal attributes</em> for the given 
   * <code>component</code>:
   * <ul><li>class</li>
   * <li>dir</li>
   * <li>lang</li>
   * <li>title</li>
   * <li>style</li>
   * </ul></p>
   * @param component the component whose universal attributes should be
   * written
   * @throws IOException if an I/O error occurs
   * @see #hasUniversalAttributes(SimpleComponent)
   * @see #writeUniversalAttributes(SimpleComponent)
   */
  public static void writeUniversalAttributes( final SimpleComponent component )
    throws IOException
  {
    // if empty css class, we pack the style settings into a class and
    // render the class name instead of the literal style attributes
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    String styleString = component.getStyle().toString();
    if( !isEmpty( styleString ) ) {
      if( isEmpty( component.getCssClass() ) ) {
        String cssName = out.registerCssClass( styleString );
        component.setCssClass( cssName );
        writeUniversalAttributesWithoutStyle( component );
        component.setCssClass( "" );
      } else {
        if( !component.isIgnoreLocalStyle() ) {
          out.writeAttribute( HTML.STYLE, styleString, null );
        }
        writeUniversalAttributesWithoutStyle( component );
      }
    } else {
      writeUniversalAttributesWithoutStyle( component );
    }
  }

  /**
   * <p>Writes an <em>AJaX-placeholder</em> for the given
   * <code>component</code>. An AJaX-placeholder currently consists of a 
   * &lt;span&gt; element whose <em>id</em> attribute holds the unique id of
   * the given <code>component</code>.</p>
   * <p>When in AJaX mode, an AJaX-placeholder should be rendered for each 
   * component that is currently not visible be may later become visible.</p>
   * @param writer the response writer to be written to 
   * @param component the component for which the placeholder should be created
   * @param insertNBSP whether a non-breaking space should be place inside
   * the element
   * @throws IOException if an I/O error occurs
   */
  public static void appendAjaxPlaceholder( final HtmlResponseWriter writer,
                                            final WebComponent component,
                                            final boolean insertNBSP ) 
    throws IOException
  {
    if( insertNBSP ) {
      writer.startElement( HTML.SPAN, null );
      writer.writeAttribute( HTML.ID, component.getUniqueID(), null );
      writer.writeAttribute( HTML.STYLE, INVISIBLE_STYLE, null );
      writer.writeNBSP();
      writer.endElement( HTML.SPAN );
    } else {
      // TODO [rh] think about having a PlaceholderRenderer to allow different
      //      placeholder markup for each browser
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      if( stateInfo.getDetectedBrowser() instanceof Mozilla1_6up ) {
        writer.startElement( HTML.SPAN, null );
        writer.writeAttribute( HTML.ID, component.getUniqueID(), null );
        writer.endElement( HTML.SPAN );
      } else {
        // a simple <span id="xx" /> does not work here since (for whatever
        // reasons) IE can not replace this...
        writer.startElement( HTML.SPAN, null );
        writer.writeAttribute( HTML.ID, component.getUniqueID(), null );
        writer.startElement( HTML.IMG, null );
        writer.writeAttribute( HTML.WIDTH, "0", null );
        writer.writeAttribute( HTML.HEIGHT, "0", null );
        String location = "resources/images/transparent.gif";
        writer.writeAttribute( HTML.SRC, location, null );
        writer.endElement( HTML.SPAN );
      }
    }
  }

  /**
   * <p>Encodes the characters in the given <code>text</code> using HTML 
   * entities and returns it.</p>
   * @param text the text to be encoded, must not be <code>null</code>
   */
  public static String encodeHTMLEntities( final String text ) {
    String result = Entities.HTML40.escape( text );
    // Encode double-hyphens because they are not allowed inside comments. This
    // is necessary since in AJaX mode most markup is placed inside comment
    // tags. (see [WFT-36]) 
    Matcher matcher = DOUBLE_HYPHEN_PATTERN.matcher( result );
    result = matcher.replaceAll( "&#045;&#045;" );
    return result;
  }

  /**
   * <p>Encodes all of the following character (-sequences) in the given 
   * <code>text</code> with their respective Unicode charater code: &amp;amp;, 
   * &quot;, &lt;, &gt;</p>
   * @param text the text to be encoded
   */
  // TODO [rh] revise this: how to generally approach encoding of ajax-response
  public static String encodeXMLEntities( final String text ) {
    String result = text.replaceAll( HTML.NBSP, "&#160;" );
    result = result.replaceAll( "\"", "&#034;" );
    result = result.replaceAll( ">", "&#062;" );
    result = result.replaceAll( "<", "&#060;" );
    result = replaceAmpersand( result );
    // Encode double-hyphens because they are not allowed inside comments. This
    // is necessary since in AJaX mode most markup is placed inside comment
    // tags. (see [WFT-36]) 
    Matcher matcher = DOUBLE_HYPHEN_PATTERN.matcher( result );
    result = matcher.replaceAll( "&#045;&#045;" );
    return result;
  }

  /**
   * <p>Returns the given <code>text</code> with all ampersand chars (&amp;)
   * replaced by <code>&amp;amp;</code>.</p>
   * @param text the text whose ampersand chars should be replaced, must
   * not be <code>null</code>
   */
  public static String replaceAmpersand( final String text ) {
    StringBuffer result = new StringBuffer();
    char character;
    for( int i = 0; i < text.length(); i++ ) {
      character = text.charAt( i );
      if( character == '&' ) {
        if(    getChar( text, i + 1 ) == '#'
            && isDigit( getChar( text, i + 2 ) )
            && isDigit( getChar( text, i + 3 ) )
            && isDigit( getChar( text, i + 4 ) )
            && getChar( text, i + 5 ) == ';' )
        {
          result.append( character );
        } else {
          result.append( "&amp;" );
        }
      } else {
        result.append( character );
      }
    }
    return result.toString();
  }

  private static char getChar( final String text, final int currentPos ) {
    char result;
    if( currentPos < text.length() ) {
      result = text.charAt( currentPos );
    } else {
      result = 'X';
    }
    return result;
  }

  private static boolean isDigit( final char character ) {
    return character >= '0' && character <= '9';
  }

  /**
   * <p>Writes an opening &lt;font&gt tag with the attributes as given by 
   * the arguments to the response writer of the current request.</p>
   * @see #writeFontCloser()
   */
  public static void writeFontOpener( final String fontFamily,
                                      final WebColor color,
                                      final int fontSize ) 
    throws IOException
  {
    HtmlResponseWriter out = ContextProvider.getStateInfo().getResponseWriter();
    out.startElement( HTML.FONT, null );
    if( fontFamily != null && !"".equals( fontFamily ) ) {
      out.writeAttribute( HTML.FACE, fontFamily, null );
    }
    if( !"".equals( color.toString() ) ) {
      out.writeAttribute( HTML.COLOR, color.toString(), null );
    }
    if( fontSize != Style.NOT_USED ) {
      String size = Integer.toString( calculateFontSize( fontSize ) );
      out.writeAttribute( HTML.SIZE, size, null );
    }
    out.closeElementIfStarted();
  }

  /**
   * <p>Writes a closing &lt;font&gt; tag to the response writer of the current
   * request.</p>
   * @see #writeFontOpener(String, WebColor, int)
   */
  public static void writeFontCloser() throws IOException {
    getResponseWriter().endElement( HTML.FONT );
  }

  /**
   * <p>Creates the replacement for the font-weight style attribute bold 
   * (opening tag)</p>
   */
  public static void writeBoldOpener( final SimpleComponent component )
    throws IOException
  {
    if( isBold( component ) ) {
      getResponseWriter().startElement( HTML.BOLD, null );
    }
  }

  /**
   * <p>Creates the replacement for the font-Weight style attribute bold
   * (closing tag)</p>
   */
  public static void writeBoldCloser( final SimpleComponent component )
    throws IOException
  {
    if( isBold( component ) ) {
      getResponseWriter().endElement( HTML.BOLD );
    }
  }

  /**
   * <p>Writes a <em>readonly</em> attribute to the response writer of the 
   * current request if the given <code>valueHolders</code> is not {@link 
   * IInputValueHolder#isUpdatable() updatable}.</p> 
   * @param valueHolder the valueHolder for which the attribute should be
   * written 
   * @throws IOException if an I/O error occurs
   */
  public static void writeReadOnly( final IInputValueHolder valueHolder )
    throws IOException
  {
    if( !valueHolder.isUpdatable() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.writeAttribute( HTML.READONLY, null, null );
    }
  }
  
  /**
   * <p>Writes a <em>disabled</em> attribute to the response writer of the 
   * current request if the given <code>component</code> is not {@link 
   * WebComponent#isEnabled() enabled}.</p> 
   * @param component the component for which the attribute should be
   * written 
   * @throws IOException if an I/O error occurs
   */
  public static void writeDisabled( final WebComponent component )
    throws IOException
  {
    if( !component.isEnabled() ) {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.writeAttribute( HTML.DISABLED, null, null );
    }
  }
  
  /** <p>Helping method for the bold tag opener and closer creator.</p> */
  private static boolean isBold( final SimpleComponent component ) {
    return component.getStyle().getFontWeight().equalsIgnoreCase( "bold" );
  }

  private static HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }

  /**
   * <p>Calculates the font size used by the font tag based on the font size in
   * points.</p>
   */
  private static int calculateFontSize( final int fontSizeInPoints ) {
    int result = 3;
    if( fontSizeInPoints <= 8 ) {
      result = 1;
    } else if( fontSizeInPoints <= 9 ) {
      result = 2;
    } else if( fontSizeInPoints <= 12 ) {
      result = 3;
    } else if( fontSizeInPoints <= 15 ) {
      result = 4;
    } else if( fontSizeInPoints <= 22 ) {
      result = 5;
    } else if( fontSizeInPoints <= 30 ) {
      result = 6;
    } else {
      result = 7;
    }
    return result;
  }

  /**
   * <p>Writes the followin attribute for the given <code>table</code> to
   * the response writer for the current request:
   * <ul><li>width</li>
   * <li>height</li>
   * <li>cellspacing</li>
   * <li>cellpadding</li>
   * <li>border</li>
   * <li>bgcolor</li>
   * <li>align</li></ul>
   * An eventually empty attribute is not written.</p>
   * @param table the table whose attribute should be written
   * @throws IOException if an I/O error occurs
   */
  public static void writeTableAttributes( final WebTable table )
    throws IOException
  {
    HtmlResponseWriter out = getResponseWriter();
    if( !"".equals( table.getWidth() ) ) {
      out.writeAttribute( HTML.WIDTH, table.getWidth(), null );
    }
    if( !"".equals( table.getHeight() ) ) {
      out.writeAttribute( HTML.HEIGHT, table.getHeight(), null );
    }
    if( !"".equals( table.getCellspacing() ) ) {
      out.writeAttribute( HTML.CELLSPACING, table.getCellspacing(), null );
    }
    if( !"".equals( table.getCellpadding() ) ) {
      out.writeAttribute( HTML.CELLPADDING, table.getCellpadding(), null );
    }
    if( !"".equals( table.getBorder().toString() ) ) {
      out.writeAttribute( HTML.BORDER, table.getBorder().toString(), null );
    }
    if( !"".equals( table.bgColor.toString() ) ) {
      out.writeAttribute( HTML.BGCOLOR, table.bgColor.toString(), null );
    }
    if( !"".equals( table.getAlign() ) ) {
      out.writeAttribute( HTML.ALIGN, table.getAlign(), null );
    }
  }

  /** 
   * <p>Returns an <em>onclick</em> attribute that triggers an 'action-
   * performed' event for the component identified by the given 
   * <code>componentId</code>.
   * @param componentId the id of the component for which the <em>onclick</em> 
   * attribute should be returned. Must not be <code>null</code>
   */
  public static String useEventHandler( final String componentId ) {
    return MessageFormat.format( WebActionEvent.WEB_ACTION_PERFORMED_HANDLER, 
                                 new Object[] { componentId } );
  }
  
  /**
   * <p>Returns the JavaScript function call (prepended by javascript:) for 
   * 'action-performed' for the given <code>componentId</code>.</p> 
   * @param componentId the id of the component for which the JavaScript
   * function call shoud be returned. Must not be <code>null</code>. 
   */
  public static String jsWebActionPerformed( final String componentId ) {
    String javaScript = "javascript:eventHandler.webActionPerformed(''{0}'')";
    return MessageFormat.format( javaScript, new Object[] { componentId } );
  }
  
  /**
   * <p>Returns the JavaScript function call for 'action-performed' for
   * the given <code>componentId</code>.</p> 
   * @param componentId the id of the component for which the JavaScript
   * function call shoud be returned. Must not be <code>null</code>. 
   */
  public static String webActionPerformed( final String componentId ) {
    return MessageFormat.format( "eventHandler.webActionPerformed(''{0}'')", 
                                 new Object[] { componentId } );
  }
  
  /**
   * <p>Returns the JavaScript function call (prepended by javascript:) to
   * drag drop for the given <code>component</code>.</p>
   * @param component the component to return the JavaScript function call for,
   * must not be <code>null</code> 
   */
  public static String jsDoDragDrop( final WebComponent component ) {
    String javaScript = "javascript:dragDropHandler.doDragDrop( ''{0}'' );";
    return MessageFormat.format( javaScript, 
                                 new Object[] { component.getUniqueID() } );
  }
  
  /** <p>Returns the xml processing instruction as needed for an AJaX 
   * response.</p> */
  public static String createXmlProcessingInstruction() {
    StringBuffer result = new StringBuffer();
    result.append( "<?xml" );
    HTMLUtil.attribute( result, "version", "1.0" );
    HTMLUtil.attribute( result, "encoding", HTML.CHARSET_NAME_UTF_8 );
    result.append( " ?>" );
    return result.toString();
  }
  
  /**
   * <p>Returns the URL which requests per GET method the given WebForm from
   * the w4 toolkit servlet. If nessecary the URL is rewritten with the current
   * sessionid.</p>
   */
  public static String createEncodedFormGetURL( final WebForm form ) {
    String formURL = createFormGetURL( form );
    return ContextProvider.getResponse().encodeURL( formURL );
  }

  /**
   * <p>Returns the URL which requests per GET method the given WebForm from
   * the W4Toolkit servlet.</p>
   */
  public static String createFormGetURL( final WebForm form ) {
    IFormAdapter adapter;
    adapter = ( IFormAdapter )form.getAdapter( IFormAdapter.class );
    String counter = String.valueOf( adapter.getRequestCounter() - 1 );    
    StringBuffer result = new StringBuffer();
    result.append( URLHelper.getURLString( false ) );
    String uiRootId = LifeCycleHelper.createUIRootId( form );
    URLHelper.appendFirstParam( result, RequestParams.UIROOT, uiRootId );
    URLHelper.appendParam( result, RequestParams.REQUEST_COUNTER, counter );
    URLHelper.appendParam( result, RequestParams.PARAMLESS_GET, "true" );
    
    URLHelper.appendParam( result,
                           "nocache", 
                           String.valueOf( form.hashCode() ) );
    
    return result.toString();
  }

  /**
   * <p>Returns the locale-specific translation of the given <code>key</code>.
   * The action being taken in case that there is no localized resource for
   * <code>key</code> depends on the settings in <code>w4t.conf</code>.
   * </p> 
   * @param key the key to be resolved
   * @see IInitialization#getHandleMissingI18NResource()
   */
  public static String resolve( final String key ) {
    String result = key;
    if( PropertyURI.isValid( key ) ) {
      String howToHandle = determineHowToHandle();
      if( howToHandle.equalsIgnoreCase( "Empty" ) ) {
        result = "";
      }
      try {
        PropertyURI puri = new PropertyURI( key );
        String bundleBaseName = puri.getBundleBaseName();
        result = loadBundle( bundleBaseName ).getString( puri.getName() );
      } catch( InvalidPropertyURIException ipuex ) {
        ipuex.printStackTrace();
      } catch( MissingResourceException mrex ) {
        if( howToHandle.equalsIgnoreCase( "Fail" ) ) {
          Assert.isTrue( false, mrex.toString() );
        }
      }
    }
    return result;
  }

  public static String resolveLocation( final String resource ) {
    String result = resource;
    IResourceManager manager = ResourceManager.getInstance();
    if( manager != null && manager.isRegistered( resource ) ) {
      result = manager.getLocation( resource );
    }
    return result;
  }

  private static ResourceBundle loadBundle( final String bundleBaseName )
    throws MissingResourceException
  {
    Locale locale = SessionLocale.isSet()
                  ? SessionLocale.get()
                  : W4TContext.getBrowser().getLocale();
    ClassLoader loader = RenderUtil.class.getClassLoader();
    IResourceManager manager = ResourceManager.getInstance();
    ClassLoader ctxLoader = manager.getContextLoader();
    ResourceBundle result;
    if( ctxLoader == null ) {
      result = ResourceBundle.getBundle( bundleBaseName, locale, loader );
    } else {
      result = ResourceBundle.getBundle( bundleBaseName, locale, ctxLoader );
    }
    return result;
  }

  public static String[] splitLineBreak( final String text ) {
    StringTokenizer tokenizer = new StringTokenizer( text, "\n", true );
    String[] result = new String[ tokenizer.countTokens() ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = tokenizer.nextToken();
    }
    return result;
  }

  private static boolean hasUniversalAttributesWithoutStyle( 
    final SimpleComponent cmp ) 
  {
    return    !isEmpty( cmp.getCssClass() ) 
           || !isEmpty( cmp.getTitle() ) 
           || !isEmpty( cmp.getLang() )
           || !isEmpty( cmp.getDir() );
  }
                                                          
  private static boolean isEmpty( final String str ) {
    return "".equals( str );
  }

  private static String determineHowToHandle() {
    IConfiguration configuration = ConfigurationReader.getConfiguration();
    return configuration.getInitialization().getHandleMissingI18NResource();
  }

  private static String appendItemPrefix( final String componentID ) {
    return WebItemEvent.PREFIX + componentID;
  }

  private static String appendActionPrefix( final String componentID ) {
    return WebActionEvent.PREFIX + componentID;
  }

  private static String appendDragDropPrefix( final String componentID ) {
    return DragDropEvent.PREFIX + componentID;
  }
}
