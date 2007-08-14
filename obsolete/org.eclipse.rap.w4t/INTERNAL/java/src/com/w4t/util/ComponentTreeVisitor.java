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
package com.w4t.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rwt.internal.util.ParamCheck;

import com.w4t.*;
import com.w4t.dhtml.Item;
import com.w4t.dhtml.Node;


/**
 * <p>Simple utility class that provides a traversal through a component-tree
 * using the visitor pattern. The component types of interest are
 * <code>WebComponent</code>, <code>WebContainer</code>, <code>Node</code> 
 * and <code>Decorator</code>, since they represent the different nesting 
 * types used by W4 Toolkit.</p>
 * 
 * <p>The traversal through the children will be ignored if the visit call
 * on the parent node returns <code>false</code>.</p> 
 * 
 */
public class ComponentTreeVisitor {
  
  /**
   * <p>The depth-first traversal first visits the root node and then
   *  the first child node of the search tree that appears and 
   *  thus going deeper and deeper until it hits a node that has no children. 
   *  Then the traversal backtracks and starts off on the next node.</p>
   *  
   * <p>For example, the following component tree</p>
   * <pre>
   *   +- root
   *      +- a
   *         +- a1
   *         +- a2
   *      +- b
   *      +- c
   *         +- c1
   * </pre>
   * <p>... will be visited in the following order: root, a, a1, a2,
   * b, c, c1.</p>   
   */
  public static final int STRATEGY_DEPTH_FIRST = 0;
  
  /**
   * <p>The breadth-first traversal visits the nodes in the order of their depth 
   * in the component tree. Breadth-first traversal first visits all the nodes 
   * at depth zero (i.e., the root), then all the nodes at depth one, and 
   * so on.</p>
   * 
   * <p>For example, the following component tree</p>
   * <pre>
   *   +- root
   *      +- a
   *         +- a1
   *         +- a2
   *      +- b
   *      +- c
   *         +- c1
   * </pre>
   * <p>... will be visited in the following order: root, a, b, c, a1, 
   * a2, c1.</p> 
   */
  public static final int STRATEGY_BREADTH_FIRST = 1;
  
  /**
   * <p>The <code>AllComponentTreeVisitor</code> is an convenience 
   * <code>ComponentTreeVisitor</code> implementation which visits all
   * different types that made up the component tree structure. This
   * is useful, if the same operation of the <code>doVisit</code> method
   * should be performed on each component in the component tree and
   * only the common super type API is needed.</p>
   */
  public static abstract class AllComponentVisitor
    extends ComponentTreeVisitor
  {
    
    public boolean visit( final WebComponent leaf ) {
      return doVisit( leaf );
    }
    public boolean visit( final WebContainer container ) {
      return doVisit( container );
    }
    public boolean visit( final Decorator decorator ) {
      return doVisit( decorator );
    }
    public boolean visit( final Node node ) {
      return doVisit( node );
    }
   
    /** 
     * <p>Subclassses of <code>AllComponentVisitor</code> must implement
     * this method to perform a common operation on each component
     * of the component tree.</p>
     * 
     * @param component The actual element of the component tree to process.
     * @return Whether the traversal should continue with the component's 
     *         children (if there are any) or not. If <code>true</code> 
     *         children of the specified component will be visited.
     */
    public abstract boolean doVisit( final WebComponent component );
  }

  /** 
   * <p>Starts the tree traversal with the specified component as root element
   * of the component tree to visit using the 
   * {@link ComponentTreeVisitor#STRATEGY_DEPTH_FIRST} traversal
   * algorithm.</p>
   * 
   * @param root The root component of the tree traversal. Must not be null.
   * @param visitor The actual <code>ComponentTreeVisitor</code> implementation
   *                that performs the operations on the visited component
   *                tree elements. Must not be null.
   */
  public static void accept( final WebComponent root, 
                             final ComponentTreeVisitor visitor )
  {
    accept( root, STRATEGY_DEPTH_FIRST, visitor );
  }
  

  /** 
   * <p>Starts the tree traversal with the specified component as root element
   * of the component tree to visit.</p>
   * 
   * @param root The root component of the tree traversal. Must not be null.
   * @param strategy The traversal strategy that is used for the tree traversal.
   *                 Must be one of 
   *                 {@link ComponentTreeVisitor#STRATEGY_DEPTH_FIRST} or
   *                 {@link ComponentTreeVisitor#STRATEGY_BREADTH_FIRST}.
   * @param visitor The actual <code>ComponentTreeVisitor</code> implementation
   *                that performs the operations on the visited component
   *                tree elements. Must not be null.
   */  
  public static void accept( final WebComponent root, 
                             final int strategy, 
                             final ComponentTreeVisitor visitor )
  {
    ParamCheck.notNull( root, "root" );
    ParamCheck.notNull( visitor, "visitor" );
    switch( strategy ) {
      case STRATEGY_DEPTH_FIRST:
        acceptDepthFirst( root , visitor );
      break;
      case STRATEGY_BREADTH_FIRST:
        doAcceptBreadthFirst( root , visitor );        
      break;
      default:
       throw new IllegalArgumentException( "Unknown traversal strategy." );
    }
  }
  
  private static void doAcceptBreadthFirst( final WebComponent root, 
                                            final ComponentTreeVisitor visitor )
  {
    List queue = new ArrayList();
    queue.add( root );
    while( !queue.isEmpty() ) {
      WebComponent component = ( WebComponent )queue.remove( 0 );
      if( performBFVisit( component, visitor ) ) {
        if( component instanceof WebContainer ) {
          WebContainer container = ( WebContainer )component;
          WebComponent[] webComponents = container.get();
          for( int i = 0; i < webComponents.length; i++ ) {
            queue.add( webComponents[ i ] );
          }
        } else if( component instanceof Node ) {          
          Node node = ( Node )component;
          Item[] items = node.getItem();
          for( int i = 0; i < items.length; i++ ) {
            queue.add( items[ i ] );
          }
        } else if( component instanceof Decorator ) {
          Decorator decorator = ( Decorator )component;
          queue.add( decorator.getContent() );
        }
      }
    }
  }
  
  private static boolean performBFVisit( final WebComponent component,
                                         final ComponentTreeVisitor visitor )
  {
    boolean result = false;
    if( component instanceof WebContainer ) {
      result = visitor.visit( ( WebContainer )component );
    } else if( component instanceof Decorator ) {
      result = visitor.visit( ( Decorator )component );
    } else if( component instanceof Node ) {
      result = visitor.visit( ( Node )component );
    } else {
      result = visitor.visit( component );      
    }
    return result;
  }


  private static void acceptDepthFirst( final WebComponent root, 
                                        final ComponentTreeVisitor visitor )
  {
    if( root instanceof Decorator ) {
      Decorator decorator = ( Decorator )root;
      if( visitor.visit( decorator ) ) {
        acceptDepthFirst( decorator.getContent(), visitor );
      }
    } else if( root instanceof WebContainer ) {
      WebContainer container = ( WebContainer )root;
      if( visitor.visit( container ) ) {
        WebComponent[] webComponents = container.get();
        for( int i = 0; i < webComponents.length; i++ ) {
          acceptDepthFirst( webComponents[ i ], visitor );
        }
      }
    } else if( root instanceof Node ) {
      Node node = ( Node )root;
      if( visitor.visit( node ) ) {
        Item[] items = node.getItem();
        for( int i = 0; i < items.length; i++ ) {
          acceptDepthFirst( items[ i ], visitor );
        }
      }
    } else {
      visitor.visit( root );
    }
  }

  /** 
   * <p>Subclassses of <code>ComponentTreeVisitor</code> may override
   * this method to perform a <code>WebComponent</code> type specific
   * operation.</p>
   * 
   * <p>Note that only 'leaf' components, this means components that
   * do not have potentially child components, will be visited by
   * this method.</p>
   * 
   * @param leaf A 'leaf' element of the component tree to process.
   * @return As 'leaf' components do not have any child components the return
   *         value will not be evaluated. This exists only for method signature
   *         reasons.
   */
  public boolean visit( final WebComponent leaf ) {
    return true;
  }
  
  /** 
   * <p>Subclassses of <code>ComponentTreeVisitor</code> may override
   * this method to perform a <code>WebContainer</code> type specific
   * operation.</p>
   * 
   * @param container A <code>WebContainer</code> instance of the component 
   *                  tree to process.
   * @return Whether the traversal should continue with the component's 
   *         children (if there are any) or not. If <code>true</code> 
   *         children of the specified component will be visited.
   */
  public boolean visit( final WebContainer container ) {
    return true;
  }
  
  /**
   * <p>Subclassses of <code>ComponentTreeVisitor</code> may override
   * this method to perform a <code>Decorator</code> type specific
   * operation.</p>
   * 
   * @param decorator A <code>Decorator</code> instance of the component tree 
   *                  to process.
   * @return Whether the traversal should continue with the component's 
   *         children (if there are any) or not. If <code>true</code> 
   *         children of the specified component will be visited.
   */
  public boolean visit( final Decorator decorator ) {
    return true;
  }
  
  /**
   * <p>
   * Subclassses of <code>ComponentTreeVisitor</code> may override this method
   * to perform a <code>Node</code> type specific operation.
   * </p>
   * 
   * @param node A <code>Node</code> instance of the component tree to
   *          process.
   * @return Whether the traversal should continue with the component's children
   *         (if there are any) or not. If <code>true</code> children of the
   *         specified component will be visited.
   */
  public boolean visit( final Node node ) {
    return true;
  }
}
