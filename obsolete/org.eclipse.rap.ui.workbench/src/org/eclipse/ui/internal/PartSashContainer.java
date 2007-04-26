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

package org.eclipse.ui.internal;

import java.util.*;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPageLayout;


public abstract class PartSashContainer
  extends LayoutPart
  implements ILayoutContainer
{
  
  protected final WorkbenchPage page;
  protected LayoutTree root;
  protected Composite parent;
  protected List children = new ArrayList();
  private final Composite parentWidget;
  private final ControlAdapter resizeListener;
  private boolean layoutDirty;
  private boolean active;
  private LayoutPart zoomedPart;  
  
  protected static class RelationshipInfo {

    protected LayoutPart part;
    protected LayoutPart relative;
    protected int relationship;
    /**
     * Preferred size for the left child (this would be the size, in pixels of
     * the child at the time the sash was last moved)
     */
    protected int left;
    /**
     * Preferred size for the right child (this would be the size, in pixels of
     * the child at the time the sash was last moved)
     */
    protected int right;

    /**
     * Computes the "ratio" for this container. That is, the ratio of the left
     * side over the sum of left + right. This is only used for serializing
     * PartSashContainers in a form that can be read by old versions of Eclipse.
     * This can be removed if this is no longer required.
     * 
     * @return the pre-Eclipse 3.0 sash ratio
     */
    public float getRatio() {
      int total = left + right;
      if( total > 0 ) {
        return ( float )left / ( float )total;
      }
      return 0.5f;
    }
  }

  public PartSashContainer( final String id,
                            final WorkbenchPage page, 
                            final Composite parentWidget )
  {
    super( id );
    this.page = page;
    this.parentWidget = parentWidget;
    resizeListener = new ControlAdapter() {
      public void controlResized( ControlEvent e ) {
        resizeSashes();
      }
    };
  }
  
  public void setActive( boolean isActive ) {
    if( isActive == active ) {
      return;
    }
    active = isActive;
//    ArrayList children = ( ArrayList )this.children.clone();
    List children = Arrays.asList( new Object[ this.children.size() ] );
    Collections.copy( children, this.children );
    for( int i = 0, length = children.size(); i < length; i++ ) {
      LayoutPart child = ( LayoutPart )children.get( i );
      if( child instanceof PartStack ) {
        PartStack stack = ( PartStack )child;
        stack.setActive( isActive );
      }
    }
    if( isActive ) {
      createControl( parentWidget );
      parent.addControlListener( resizeListener );
//      DragUtil.addDragTarget( parent, this );
//      DragUtil.addDragTarget( parent.getShell(), this );
      // ArrayList children = (ArrayList) this.children.clone();
      for( int i = 0, length = children.size(); i < length; i++ ) {
        LayoutPart child = ( LayoutPart )children.get( i );
        child.setContainer( this );
//        child.setVisible( zoomedPart == null || child == zoomedPart );
        child.setVisible( true );
        if( !( child instanceof PartStack ) ) {
          if( root != null ) {
            LayoutTree node = root.find( child );
            if( node != null ) {
              node.flushCache();
            }
          }
        }
      }
      if( root != null ) {
        // root.flushChildren();
//        if( !isZoomed() ) {
          root.createControl( parent );
//        }
      }
      resizeSashes();
    } else {
//      DragUtil.removeDragTarget( parent, this );
//      DragUtil.removeDragTarget( parent.getShell(), this );
      // remove all Listeners
      if( resizeListener != null && parent != null ) {
        parent.removeControlListener( resizeListener );
      }
      if( children != null ) {
        for( int i = 0, length = children.size(); i < length; i++ ) {
          LayoutPart child = ( LayoutPart )children.get( i );
          child.setContainer( null );
          if( child instanceof PartStack ) {
            child.setVisible( false );
          }
        }
      }
//      disposeSashes();
      // dispose();
    }
  }

  
  public LayoutPart findBottomRight() {
    if( root == null ) {
      return null;
    }
    return root.findBottomRight();
  }
  
  void addEnhanced( final LayoutPart child,
                    final int swtDirectionConstant,
                    final float ratioForNewPart,
                    final LayoutPart relative )
  {
    int relativePosition
      = PageLayout.swtConstantToLayoutPosition( swtDirectionConstant );
    float ratioForUpperLeftPart;
    if(    relativePosition == IPageLayout.RIGHT
        || relativePosition == IPageLayout.BOTTOM )
    {
      ratioForUpperLeftPart = 1.0f - ratioForNewPart;
    } else {
      ratioForUpperLeftPart = ratioForNewPart;
    }
    add( child, relativePosition, ratioForUpperLeftPart, relative );
  }
  
  public void add( final LayoutPart child,
                   final int relationship,
                   final float ratio,
                   final LayoutPart relative )
  {
    boolean isHorizontal = ( relationship == IPageLayout.LEFT || relationship == IPageLayout.RIGHT );
    LayoutTree node = null;
    if( root != null && relative != null ) {
      node = root.find( relative );
    }
    Rectangle bounds;
    if( getParent() == null ) {
      Control control = getPage().getClientComposite();
      if( control != null && !control.isDisposed() ) {
        bounds = control.getBounds();
      } else {
        bounds = new Rectangle( 0, 0, 800, 600 );
      }
      bounds.x = 0;
      bounds.y = 0;
    } else {
      bounds = getBounds();
    }
    int totalSize = measureTree( bounds, node, isHorizontal );
    int left = ( int )( totalSize * ratio );
    int right = totalSize - left;
    add( child, relationship, left, right, relative );
  }

  void add( LayoutPart child,
            int relationship,
            int left,
            int right,
            LayoutPart relative )
  {
    if( child == null ) {
      return;
    }
    if( relative != null && !isChild( relative ) ) {
      return;
    }
    if( relationship < IPageLayout.LEFT || relationship > IPageLayout.BOTTOM ) {
      relationship = IPageLayout.LEFT;
    }
    // store info about relative positions
    RelationshipInfo info = new RelationshipInfo();
    info.part = child;
    info.relationship = relationship;
    info.left = left;
    info.right = right;
    info.relative = relative;
    addChild( info );
  }

  protected void addChild( RelationshipInfo info ) {
    LayoutPart child = info.part;
    children.add( child );
    if( root == null ) {
      root = new LayoutTree( child );
    } else {
      // Add the part to the tree.
      int vertical = ( info.relationship == IPageLayout.LEFT || info.relationship == IPageLayout.RIGHT )
                                                                                                        ? SWT.VERTICAL
                                                                                                        : SWT.HORIZONTAL;
      boolean left = info.relationship == IPageLayout.LEFT
                     || info.relationship == IPageLayout.TOP;
      LayoutPartSash sash = new LayoutPartSash( this, vertical );
      sash.setSizes( info.left, info.right );
      if( ( parent != null ) && !( child instanceof PartPlaceholder ) ) {
        sash.createControl( parent );
      }
      root = root.insert( child, left, sash, info.relative );
    }
//    childAdded( child );
    if( active ) {
      child.createControl( parent );
      child.setVisible( true );
      child.setContainer( this );
      resizeChild( child );
    }
  }

  static int measureTree( Rectangle outerBounds,
                          LayoutTree toMeasure,
                          boolean horizontal )
  {
    if( toMeasure == null ) {
      return Geometry.getDimension( outerBounds, horizontal );
    }
    LayoutTreeNode parent = toMeasure.getParent();
    if( parent == null ) {
      return Geometry.getDimension( outerBounds, horizontal );
    }
    if( parent.getSash().isHorizontal() == horizontal ) {
      return measureTree( outerBounds, parent, horizontal );
    }
    boolean isLeft = parent.isLeftChild( toMeasure );
    LayoutTree otherChild = parent.getChild( !isLeft );
    if( otherChild.isVisible() ) {
      int left = parent.getSash().getLeft();
      int right = parent.getSash().getRight();
      int childSize = isLeft
                            ? left
                            : right;
      int bias = parent.getCompressionBias();
      // Normalize bias: 1 = we're fixed, -1 = other child is fixed
      if( isLeft ) {
        bias = -bias;
      }
      if( bias == 1 ) {
        // If we're fixed, return the fixed size
        return childSize;
      } else if( bias == -1 ) {
        // If the other child is fixed, return the size of the parent minus the fixed size of the
        // other child
        return measureTree( outerBounds, parent, horizontal )
               - ( left + right - childSize );
      }
      // Else return the size of the parent, scaled appropriately
      return measureTree( outerBounds, parent, horizontal )
             * childSize
             / ( left + right );
    }
    return measureTree( outerBounds, parent, horizontal );
  }
  
  protected boolean isChild( LayoutPart part ) {
    return children.indexOf( part ) >= 0;
  }

  public Composite getParent() {
    return parent;
  }
  
  public WorkbenchPage getPage() {
    return page;
  }

  public Rectangle getBounds() {
    return this.parent.getBounds();
  }

  public LayoutTree getLayoutTree() {
    return root;
  }
  
  protected abstract Composite createParent( Composite parentWidget );

  public void resizeChild( LayoutPart childThatChanged ) {
    if( root != null ) {
      LayoutTree tree = root.find( childThatChanged );
      if( tree != null ) {
        tree.flushCache();
      }
    }
    flushLayout();
  }

  public int computePreferredSize( boolean width,
                                   int availableParallel,
                                   int availablePerpendicular,
                                   int preferredParallel )
  {
//    if( isZoomed() ) {
//      return getZoomedPart().computePreferredSize( width,
//                                                   availableParallel,
//                                                   availablePerpendicular,
//                                                   preferredParallel );
//    }
    if( root != null ) {
      return root.computePreferredSize( width,
                                        availableParallel,
                                        availablePerpendicular,
                                        preferredParallel );
    }
    return preferredParallel;
  }

  public void flushLayout() {
    layoutDirty = true;
    super.flushLayout();
    if( layoutDirty ) {
      resizeSashes();
    }
  }

  public LayoutPart getZoomedPart() {
    return zoomedPart;
  }

  public boolean isZoomed() {
    return ( zoomedPart != null );
  }

  public LayoutPart pickPartToZoom() {
    return findBottomRight();
  }

  public void setVisible( boolean makeVisible ) {
    if( makeVisible == getVisible() ) {
      return;
    }
    if( !( this.parent == null || this.parent.isDisposed() ) ) {
      this.parent.setEnabled( makeVisible );
    }
    super.setVisible( makeVisible );
    List children = Arrays.asList( new Object[ this.children.size() ] );
    Collections.copy( children, this.children );
    for( int i = 0, length = children.size(); i < length; i++ ) {
      LayoutPart child = ( LayoutPart )children.get( i );
      child.setVisible(    makeVisible
                        && ( zoomedPart == null || child == zoomedPart ) );
    }
  }
  
  
  /////////////////////////////
  // interface ILayoutContainer
  
  public void setZoomed( boolean isZoomed ) {
    if( !isZoomed ) {
      zoomOut();
    } else {
      if( !isZoomed() ) {
        LayoutPart toZoom = pickPartToZoom();
        if( toZoom != null ) {
          zoomIn( toZoom );
        }
      }
    }
    super.setZoomed( isZoomed );
  }
  
  public void add( final LayoutPart child ) {
    if( child != null ) {
      addEnhanced( child, SWT.RIGHT, 0.5f, findBottomRight() );
    }
  }
  
  public void createControl( final Composite parentWidget ) {
    if( this.parent != null ) {
      return;
    }
    parent = createParent( parentWidget );
    List children = Arrays.asList( new Object[ this.children.size() ] );
    Collections.copy( children, this.children );
    for( int i = 0, length = children.size(); i < length; i++ ) {
      LayoutPart child = ( LayoutPart )children.get( i );
      child.createControl( parent );
    }
  }

  public Control getControl() {
    return parent;
  }

  public boolean allowsAdd( LayoutPart toAdd ) {
    throw new UnsupportedOperationException();
  }

  public boolean allowsAutoFocus() {
    throw new UnsupportedOperationException();
  }

  public boolean childIsZoomed( LayoutPart toTest ) {
    return toTest == getZoomedPart();
  }

  public boolean childObscuredByZoom( LayoutPart toTest ) {
    LayoutPart zoomPart = getZoomedPart();
    
    if (zoomPart != null && toTest != zoomPart) {
        return true;
    }
    return isObscuredByZoom();
  }

  public void childRequestZoomIn( LayoutPart toZoom ) {
//    if( !SwtUtil.isDisposed( this.parent ) ) {
//      this.parent.setRedraw( false );
//    }
    try {
      zoomIn( toZoom );
      requestZoomIn();
      if( layoutDirty ) {
        resizeSashes();
      }
    } finally {
//      if( !SwtUtil.isDisposed( this.parent ) ) {
//        this.parent.setRedraw( true );
//      }
    }
  }

  public void childRequestZoomOut() {
//    if( !SwtUtil.isDisposed( this.parent ) ) {
//      this.parent.setRedraw( false );
//    }
    try {
      zoomOut();
      requestZoomOut();
      if( layoutDirty ) {
        resizeSashes();
      }
    } finally {
//      if( !SwtUtil.isDisposed( this.parent ) ) {
//        this.parent.setRedraw( true );
//      }
    }
  }

  public LayoutPart[] getChildren() {
    LayoutPart[] result = new LayoutPart[children.size()];
    children.toArray(result);
    return result;
  }

  public void remove( LayoutPart part ) {
    throw new UnsupportedOperationException();
  }

  public void replace( LayoutPart oldChild, LayoutPart newChild ) {
    if( !isChild( oldChild ) ) {
      return;
    }
    if( oldChild == getZoomedPart() ) {
      if( newChild instanceof PartPlaceholder ) {
        childRequestZoomOut();
      } else {
        zoomedPart.setZoomed( false );
        zoomedPart = newChild;
        zoomedPart.setZoomed( true );
      }
    }
    children.remove( oldChild );
    children.add( newChild );
    childAdded( newChild );
    if( root != null ) {
      LayoutTree leaf = null;
      leaf = root.find( oldChild );
      if( leaf != null ) {
        leaf.setPart( newChild );
      }
    }
    childRemoved( oldChild );
    if( active ) {
      oldChild.setVisible( false );
      oldChild.setContainer( null );
      newChild.createControl( parent );
      newChild.setContainer( this );
      newChild.setVisible( zoomedPart == null || zoomedPart == newChild );
      resizeChild( newChild );
    }
  }

  protected void childAdded( LayoutPart child ) {
//    if( isDeferred() ) {
//      child.deferUpdates( true );
//    }
  }

  protected void childRemoved( LayoutPart child ) {
//    if( isDeferred() ) {
//      child.deferUpdates( false );
//    }
  }
  
  // ////////////////
  // helping methods
  
  private void resizeSashes() {
    layoutDirty = false;
    if( !active ) {
      return;
    }
    if( isZoomed() ) {
      getZoomedPart().setBounds( parent.getClientArea() );
    } else {
      if( root != null ) {
        root.setBounds( parent.getClientArea() );
      }
    }
  }

  private void zoomOut() {
    // Sanity check.
    if( !isZoomed() ) {
      return;
    }
    LayoutPart zoomedPart = this.zoomedPart;
    this.zoomedPart = null;
    // Inform the part that it is no longer zoomed
    zoomedPart.setZoomed( false );
    // Make all children visible
    LayoutPart[] children = getChildren();
    for( int i = 0; i < children.length; i++ ) {
      LayoutPart child = children[ i ];
      child.setVisible( true );
    }
    // Recreate the sashes
    root.createControl( getParent() );
    // Ensure that the part being un-zoomed will have its size refreshed.
    LayoutTree node = root.find( zoomedPart );
    node.flushCache();
    layoutDirty = true;
  }

  private void zoomIn( LayoutPart part ) {
    // Sanity check.
    if( isZoomed() ) {
      return;
    }
    // Hide the sashes
    root.disposeSashes();
    // Make all parts invisible except for the zoomed part
    LayoutPart[] children = getChildren();
    for( int i = 0; i < children.length; i++ ) {
      LayoutPart child = children[ i ];
      child.setVisible( child == part );
    }
    zoomedPart = part;
    // Notify the part that it has been zoomed
    part.setZoomed( true );
    // Remember that we need to trigger a layout
    layoutDirty = true;
  }
}
