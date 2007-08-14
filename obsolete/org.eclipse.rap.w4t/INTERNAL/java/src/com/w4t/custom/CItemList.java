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

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.ajax.AjaxStatusUtil;
import com.w4t.developer.AreaSelector;
import com.w4t.event.*;

/** <p>The CItemList creates unordered or numbered lists. It complies with the 
 *  HTML tags &lt;ul&gt;,&lt;ol&gt; and &lt;li&gt;.</p> 
 *  <p>Use the addItem method for simply adding Strings to the list. You can
 *  add any WebComponent with the add(WebComponent) method. Constraints are
 *  not allowed.</p>
 *  <p>Note: It is not possible to mix up String items and WebComponents within
 *  the same CItemList instance.</p>
 */
public class CItemList extends WebContainer implements ICustomContainer {

  ///////////////////////
  // constant definitions
  
  /** <p>list kind definition for order lists (&lt;ol&gt;).</p> */
  public final static String ORDERED_LIST = "ordered";
  /** <p>list kind definition for unorder lists (&lt;ul&gt;).</p> */
  public final static String UNORDERED_LIST = "unordered";
  /** <p>type value for displaying a round bullet before each list entry.</p> */
  public final static String BULLET_TYPE_CIRCLE = "circle";
  /** <p>type value for displaying a square bullet before each list entry.</p>*/
  public final static String BULLET_TYPE_SQUARE ="square";
  /** <p>type value for displaying a file symbol before each list entry.</p> */
  public final static String BULLET_TYPE_DISC = "disc";
  /** <p>type value for numbering the list entries with I., II., III.</p> */
  public final static String NUMBERING_UPPERCASE_I = "I";
  /** <p>type value for numbering the list entries with i., ii., iii.</p> */
  public final static String NUMBERING_LOWERCASE_I = "i";
  /** <p>type value for numbering the list entries with A., B., C.</p> */
  public final static String NUMBERING_UPPERCASE_A = "A";
  /** <p>type value for numbering the list entries with a., b., c.</p> */
  public final static String NUMBERING_LOWERCASE_A = "a";
   
  
  /////////
  // fields
  
  private String listKind = UNORDERED_LIST;
  private String type = "";  
  private int start = 1;
  private boolean firstItemHead;
  
  /** <p>internal datastructure used if working with String items.</p> */
  private ArrayList items;
  /** <p>indicator that render phase of a request lifecycle was entered.</p> */
  private boolean renderFlag;
  

  /** <p>creates a new instance of CItemList.</p> */
  public CItemList() {
    items = new ArrayList();
    try {
      this.setWebLayout( new CItemListLayout() );
    } catch( Exception e ) {
      /* Ignoring the Exception is safe here: the only reason for
       * throwing it may be a multiple adding of WebLayouts to containers;
       * since we have just created the WebLayout here,
       * this can't be the case. */
    }
    addEventHandler();
  }
  
  public Object clone() throws CloneNotSupportedException {
    CItemList result = ( CItemList )super.clone();
    result.items = new ArrayList();
    for( int i = 0; i < items.size(); i++ ) {
      result.items.add( items.get( i ) );
    }
    return result;
  }
  
  /** <p>sets the items which will be displayed as list entries. This will be 
   *  ignored, if WebComponents are added to the CItemList.</p> */
  public void setItem( final String[] items ) {
    if( !isComponentList() ) {
      this.items.clear();
      for( int i = 0; i < items.length; i++ ) {
        this.items.add( items[ i ] );
      }
    }
  }

  /** <p>returns the items which will be displayed as list entries.
   *  Note: modifying the result will not change the displayed entries, use
   *  addItem(String) or setItem(String[]). If WebComponents are added to 
   *  this CItemList, getItems returns an empty String array.</p> */  
  public String[] getItem() {
    String[] result = new String[ items.size() ];
    items.toArray( result );
    return result;
  }

  /** <p>sets the items which will be displayed as list entries at the
   *  given index. This will be ignored, if WebComponents are added to the 
   *  CItemList.</p> */  
  public void setItem( final int index, final String item ) {
    if( !isComponentList() ) {
      this.items.add( index, item );
    }
  }

  /** <p>returns the item at the given index.</p> */    
  public String getItem( final int index ) {
    return ( String )items.get( index );
  }
  
  /** <p>add a item at the end of the list.This will be ignored, if 
   *  WebComponents are added to the CItemList.</p> */  
  public void addItem( final String item ) {
    if( !isComponentList() ) {
      items.add( item ); 
    }
  }
  
  /** <p>sets which kind of list kind should be rendered, can be one of those:
   *  ORDERED_LIST (&lt;ol&gt;) or UNORDERED_LIST (&lt;ul&gt;).</p>*/
  public void setListKind( final String listKind ) {
    if(    listKind.equalsIgnoreCase( ORDERED_LIST ) 
        || listKind.equalsIgnoreCase( UNORDERED_LIST ) ) {
      if( !this.listKind.equalsIgnoreCase( listKind ) ) {
        start = 1;
        type = "";
      }
      this.listKind = listKind;
    }
  }
  
  /** <p>returns which kind of list should be rendered, can be one of 
   *  those: ORDERED_LIST (&lt;ol&gt;) or UNORDERED_LIST (&lt;ul&gt;).</p> */
  public String getListKind() {
    return listKind;
  }
  
  /** <p>sets the prefix symbol or number of each list entry.</p> 
   *  <p>For UNORDERED_LIST kinds this could be one of those:
   *  <ul>
   *    <li>BULLET_TYPE_CIRCLE displays a round Bullet</li>
   *    <li>BULLET_TYPE_SQUARE displays a sqare Bullet</li>
   *    <li>BULLET_TYPE_DISC displays a file symbol</li>
   *  </ul>
   *  </p>
   *  <p>For ORDERED_LIST kinds this could be one of those:
   *  <ul>
   *    <li>NUMBERING_UPPERCASE_I numbers the list entries with I., II., III.,
   *        etc.</li>
   *    <li>NUMBERING_LOWERCASE_I numbers the list entries with i., ii., iii.,
   *        etc.</li>
   *    <li>NUMBERING_UPPERCASE_A numbers the list entries with A., B., C.,
   *        etc.</li>
   *    <li>NUMBERING_LOWERCASE_A numbers the list entries with a., b., c.,
   *        etc.</li>
   *  </ul>
   *  <p>Leaving this attribute empty uses the Browsers default settings.</p>
   *  </p>*/
  public void setType( final String type ) {
    if( listKind.equalsIgnoreCase( UNORDERED_LIST ) ) {
      if(    type.equalsIgnoreCase( BULLET_TYPE_CIRCLE ) 
          || type.equalsIgnoreCase( BULLET_TYPE_DISC ) 
          || type.equalsIgnoreCase( BULLET_TYPE_SQUARE ) 
          || type.equals( "" ) ) {
        this.type = type;
      }
    } else {
      if(    type.equals( NUMBERING_LOWERCASE_A ) 
          || type.equals( NUMBERING_LOWERCASE_I ) 
          || type.equals( NUMBERING_UPPERCASE_A )
          || type.equals( NUMBERING_UPPERCASE_I ) 
          || type.equals( "" ) ) {
        this.type = type;
      }
    }
  }

  /** <p>returns the prefix symbol or number of each list entry.</p> 
   *  <p>For UNORDERED_LIST kinds this could be one of those:
   *  <ul>
   *    <li>BULLET_TYPE_CIRCLE displays a round bullet</li>
   *    <li>BULLET_TYPE_SQUARE displays a square bullet</li>
   *    <li>BULLET_TYPE_DISC displays a file symbol</li>
   *  </ul>
   *  </p>
   *  <p>For ORDERED_LIST kinds this could be one of those:
   *  <ul>
   *    <li>NUMBERING_UPPERCASE_I numbers the list entries with I., II., III.,
   *        etc.</li>
   *    <li>NUMBERING_LOWERCASE_I numbers the list entries with i., ii., iii.,
   *        etc.</li>
   *    <li>NUMBERING_UPPERCASE_A numbers the list entries with A., B., C.,
   *        etc.</li>
   *    <li>NUMBERING_LOWERCASE_A numbers the list entries with a., b., c.,
   *        etc.</li>
   *  </ul>
   *  <p>Leaving this attribute empty uses the Browsers default settings.</p>
   *  </p>*/
  
  public String getType() {
    return type;
  }

  /** <p>sets with which count value to start the ordered list.</p> */
  public void setStart( final int start ) {
    if( start > 0 ) {
      this.start = start;
    }
  }
  
  /** <p>returns with which count value to start the ordered list.</p> */
  public int getStart() {
    return start;
  }
  
  /** <p>sets whether the first list entry should be used as headline
   *  of the list. Useful in nested CItemLists because the headline is
   *  then displayed as item of the surrounding CItemList.</p> */
  public boolean isFirstItemHead() {
    return firstItemHead;
  }
  
  /** <p>returns whether the first list entry should be used as headline
     *  of the list. Useful in nested CItemLists because the headline is
     *  then displayed as item of the surrounding CItemList.</p> */  
  public void setFirstItemHead( final boolean firstItemHead ) {
    this.firstItemHead = firstItemHead;
  }
  
  /** <p>returns a path to an image that represents this CItemList
   * (widget icon).</p> */
  public static String retrieveIconName() {
    return "resources/images/icons/itemlist.gif";
  }

  
  //////////////////
  // helping methods
  
  private void addEventHandler() {
    this.addWebContainerListener( new WebContainerListener() {
      public void webComponentAdded( final WebContainerEvent evt ) {
        // if runnung as list with String based items avoid adding WebComponents
        if( !renderFlag && isItemList() ) {
          evt.getChild().remove();
        }
      }

      public void webComponentRemoved( final WebContainerEvent evt ) {
      }

      public void webLayoutChanged( final WebContainerEvent evt ) {
      }
    } );
    
    // to avoid confusion of string or component mode String items are added
    // before rendering wrapped into a WebLabel to the container and are removed
    // afterwards. 
    this.addWebRenderListener( new WebRenderListener() {
      
      public void beforeRender( final WebRenderEvent evt ) {
        renderFlag = true;
        if( isItemList() ) {
          for( int i = 0; i < items.size(); i++ ) {
            add( new WebLabel( ( String )items.get( i ) ) );
          }
        }
      }

      public void afterRender( final WebRenderEvent evt ) {
        if( isItemList() ) {
          removeAll();
        }
        renderFlag = false;
      }
    } );
  }
    
  private boolean isItemList() {
    return items.size() > 0;
  }
  
  private boolean isComponentList() {
    return !isItemList() && super.get() != null && super.get().length > 0;
  }
  
  
  ////////////////
  // inner classes
  
  /** the layout manager */
  private class CItemListLayout implements WebLayout, Cloneable {

    private AreaSelector selector = new AreaSelector();

    public Object clone() throws CloneNotSupportedException {
      CItemListLayout layout = ( CItemListLayout )super.clone();
      layout.selector = new AreaSelector();
      return layout;
    }

    public void layoutWebContainer( final WebContainer container ) 
      throws IOException
    {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      WebComponent[] components = container.get();
      if(    !W4TContext.getBrowser().isAjaxEnabled()
          || AjaxStatusUtil.mustRender( container ) )
      {
        writeHeadLine( components );      
        writeListOpen( container, out );
        writeAreaSelector( container, out );
        for( int i = firstItemHead ? 1: 0; i < components.length; i++ ) {
          writeItemOpen();
          LifeCycleHelper.render( components[ i ] );
          writeItemClose();
        }
        writeListClose( out );
      } else {
        for( int i = 0; i < components.length; i++ ) {
          LifeCycleHelper.render( components[ i ] );
        }
      }
    }

    private void writeHeadLine( final WebComponent[] components )
      throws IOException
    {
      if( firstItemHead && components.length > 0 ) {
        LifeCycleHelper.render( components[ 0 ] );
      }
    }
    
    private void writeAreaSelector( final WebContainer container,
                                     final HtmlResponseWriter out )
      throws IOException
    {
      if( !isItemList() && isDesignTime() ) {        
        writeItemOpen();
        selector.setContainer( container );
        container.add( selector );
        LifeCycleHelper.render( selector );
        container.remove( selector );
        writeItemClose();        
      }
    }

    private void writeItemOpen() throws IOException {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.startElement( HTML.LI, null );
    }
    
    private void writeItemClose() throws IOException {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      HtmlResponseWriter out = stateInfo.getResponseWriter();
      out.endElement( HTML.LI );
    }

    private void writeListOpen( final WebContainer container, 
                                 final HtmlResponseWriter out ) 
      throws IOException 
    {
      if( listKind.equalsIgnoreCase( ORDERED_LIST ) ) {
        out.startElement( HTML.OL, null );
      } else {
        out.startElement( HTML.UL, null );
      }
      out.writeAttribute( HTML.ID, container.getUniqueID(), null );
      RenderUtil.writeUniversalAttributes( container );
      if( !type.equals( "" ) ) {
        out.writeAttribute( HTML.TYPE, type, null );
      }
      if( listKind.equalsIgnoreCase( ORDERED_LIST ) && start > 1 ) {
        out.writeAttribute( HTML.START, Integer.toString( start ), null );
      }
      out.closeElementIfStarted();
    }
    
    private void writeListClose( final HtmlResponseWriter out ) 
      throws IOException 
    {
      if( listKind.equalsIgnoreCase( ORDERED_LIST ) ) {
        out.endElement( HTML.OL );
      } else {
        out.endElement( HTML.UL );
      }
    }

    public boolean checkConstraint( final Object constraint ) {
      return constraint == null;
    }

    public WebTableCell getRegion( final Object constraint ) {
      return null;
    }
    
    public Area getArea( final Object constraint ) {
      return null;
    }
  }
}