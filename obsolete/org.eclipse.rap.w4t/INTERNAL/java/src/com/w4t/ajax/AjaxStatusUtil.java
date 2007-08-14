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
package com.w4t.ajax;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.lifecycle.HtmlResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.internal.util.HTML;

import com.w4t.*;
import com.w4t.dhtml.Item;
import com.w4t.engine.lifecycle.standard.IRenderingSchedule;
import com.w4t.util.ComponentTreeVisitor;
import com.w4t.util.RendererCache;
import com.w4t.util.ComponentTreeVisitor.AllComponentVisitor;


/**
 * <p>Utility class with helper methods for AJaX rendering.</p>
 */
public final class AjaxStatusUtil {
  
  private static final String ENVELOPE = "envelope";

  
  private AjaxStatusUtil() {
    // prevent instantiation
  }

  // TODO [rh] Verify that only the component is updated. In case it is a
  //      container, none of its children must be changed
  // TODO [rh] JavaDoc
  public static void updateHashCode( final WebComponent component ) {
    HashCodeBuilder builder;
    builder = HashCodeBuilderFactory.getBuilder( component.getClass() );
    HashCodeBuilderSupportImpl support = new HashCodeBuilderSupportImpl();
    int hashCode = builder.compute( support, component );
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class ); 
    ajaxStatus.setComponentHashCode( hashCode );
  }

  /**
   * <p>Collects information that is needed for the actual rendering run
   * of the given component-tree to render.</p>
   * 
   * @param root the <code>WebContainer</code> that represents the root of
   *             the component-tree.
   */
  public static void preRender( final WebContainer root ) {
    HashCodeBuilderSupport support = new HashCodeBuilderSupportImpl();
    ComponentTreeVisitor visitor = new Visitor( root, support );
    int strategy = ComponentTreeVisitor.STRATEGY_BREADTH_FIRST;
    ComponentTreeVisitor.accept( root, strategy, visitor );
  }
  
  /**
   * <p>Collects information that is needed for the rendering run of the next
   * request of the given component-tree.</p>
   * @param rootContainer the <code>WebContainer</code> that represents the 
   * root of the component-tree.
   */
  public static void postRender( final WebContainer rootContainer ) {
    ComponentTreeVisitor visitor = new AllComponentVisitor() {
      public boolean doVisit( final WebComponent component ) {
        AjaxStatus ajaxStatus;
        ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
        ajaxStatus.setMustRender( false );
        ajaxStatus.setWasVisible( component.isVisible() );
        ajaxStatus.setWasEnabled( component.isEnabled() );
        return true;
      }
    };
    ComponentTreeVisitor.accept( rootContainer, visitor );
  }
  
  /**
   * <p>Calls <code>mustRender</code> for the <code>AjaxStatus</code> which is
   * associated with the given <code>component</code>.</p> 
   */
  public static boolean mustRender( final WebComponent component ) {
    AjaxStatus ajaxStatus;
    ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class );
    return ajaxStatus.mustRender();
  }
  
  static boolean isRootOfSubTreeToRender( final WebComponent comp ) {
    return    mustRender( comp )
           && (    comp instanceof WebForm 
                || (    getPredecessor( comp ) != null
                     && !mustRender( getPredecessor( comp ) ) ) );
  }

  private static WebComponent getPredecessor( final WebComponent comp ) {
    WebComponent result = null;
    if( comp.getParent() != null && Decorator.isDecorated( comp ) ) {
      result = Decorator.getParentDecorator( comp );
    } else if( comp instanceof Item ) {
      result = ( ( Item )comp ).getParentNode();
      if( result == null ) {
        result = comp.getParent();
      }
    } else {
      result = comp.getParent();
    }
    return result;
  }
  
  public static void startEnvelope( final Adaptable adaptable )
    throws IOException
  {
    if( adaptable instanceof WebComponent ) {
      startEnvelope( ( WebComponent )adaptable );
    }
  }
  
  public static void endEnvelope( final Adaptable adaptable )
    throws IOException
  {
    if( adaptable instanceof WebComponent ) {
      endEnvelope( ( WebComponent )adaptable );
    }
  }
  
  public static void startEnvelope( final WebComponent component )
    throws IOException
  {
    if(    W4TContext.getBrowser().isAjaxEnabled() 
        && isRootOfSubTreeToRender( component ) )
    {
      HtmlResponseWriter writer = getResponseWriter();
      writer.startElement( ENVELOPE, null );
      writer.writeAttribute( HTML.ID, component.getUniqueID(), null );
      writer.closeElementIfStarted();
      writer.append( "<!--" );
    }
  }

  public static void endEnvelope( final WebComponent component )
    throws IOException
  {
    if(    W4TContext.getBrowser().isAjaxEnabled() 
        && isRootOfSubTreeToRender( component ) )
    {
      HtmlResponseWriter writer = getResponseWriter();
      writer.append( "-->" );
      writer.endElement( ENVELOPE );
    }
  }

  private static HtmlResponseWriter getResponseWriter() {
    IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
    return stateInfo.getResponseWriter();
  }
  
  public static HashCodeBuilderSupport newHashCodeBuilderSupport() {
    return new HashCodeBuilderSupportImpl();
  }
  
  ///////////////////
  // Private classes
  
  private static class Visitor extends AllComponentVisitor {

    private final HashCodeBuilderSupport support;

    public Visitor( final WebContainer rootContainer, 
                    final HashCodeBuilderSupport support )
    {
      this.support = support;
    }

    public boolean doVisit( final WebComponent component ) {
      boolean result = false;
      if( isScheduled( component ) ) {
        // Compute new hashCode
        HashCodeBuilder builder;
        builder = HashCodeBuilderFactory.getBuilder( component.getClass() );
        int newHashCode = builder.compute( support, component );
        // Decide whether component must be rendered
        determineMustRender( component, newHashCode );
        // Store new hashCode 
        storeHashCode( component, newHashCode );
        result = true;
      } else {
        // ensure that next time the component is scheduled for rendering
        // will be marked as changed.
        storeHashCode( component, 0 );
      }
      return result;
    }

    private boolean isScheduled( final WebComponent component ) {
      IRenderingSchedule renderingSchedule = LifeCycleHelper.getSchedule();
      return renderingSchedule.isScheduled( component );
    }
    
    private void determineMustRender( final WebComponent component, 
                                      final int newHashCode )
    {
      AjaxStatus ajaxStatus;
      ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class ); 
      boolean mustRender = ajaxStatus.mustRender();

      /////////////////////////////////////
      // TODO: [fappel] debug only
//      if( !hasAjaxRenderer( component ) ) {
//        System.out.println(  "No ajax renderer found for ["
//                            + component.getClass().getName() 
//                            + "]. Schedule parent for rendering." );
//      }
      ////////////////////////////////////
      
      // TODO [rh] Bug? if no ajax-renderer was found, the parent must be
      //      rendererd in any case - not only when the component itself
      //      is not to be rendered
      //      see AjaxStatus_Test#testComponentWithNonAjaxParent
      if( !mustRender && !hasAjaxRenderer( component ) ) {
        mustRender = true;
        markParentAsMustRender( component );
      }
      if ( !mustRender ) {
        if (    !ajaxStatus.hasComponentHashCode()
             || ajaxStatus.getComponentHashCode() != newHashCode )
        {
          mustRender = true;
          ajaxStatus.updateStatus( mustRender );
        }
      }
    }

    private static void markParentAsMustRender( final WebComponent component ) {
      WebContainer parent = component.getParent();
      if( parent == null ) {
        AjaxStatus ajaxStatus;
        ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class ); 
        ajaxStatus.updateStatus( true );
      } else {
        AjaxStatus parentAjaxStatus;
        parentAjaxStatus = ( AjaxStatus )parent.getAdapter( AjaxStatus.class );
        parentAjaxStatus.updateStatus( true );
      }
    }

    private static void storeHashCode( final WebComponent component, 
                                       final int hashCode ) 
    {
      AjaxStatus ajaxStatus;
      ajaxStatus = ( AjaxStatus )component.getAdapter( AjaxStatus.class ); 
      ajaxStatus.setComponentHashCode( hashCode );
    }
    
  }

  private static class HashCodeBuilderSupportImpl 
      implements HashCodeBuilderSupport 
  {
    private final Map recursionList = new IdentityHashMap();
    
    public void addToRecursionList( final Object component ) {
      recursionList.put( component, null );
    }

    public boolean isInRecursionList( final Object component ) {
      return recursionList.containsKey( component );
    }
  }
  
  private static boolean hasAjaxRenderer( final WebComponent component ) {
    RendererCache rendererCache = RendererCache.getInstance();
    String rendererClassName;
    rendererClassName = rendererCache.getRendererClass( component.getClass() );
    return    rendererClassName != null 
           && rendererClassName.endsWith( "_Ajax" );
  }
}
