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

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import com.w4t.WebComponent;
import com.w4t.util.ComponentTreeVisitor;
import com.w4t.util.ComponentTreeVisitor.AllComponentVisitor;

final class AjaxStatusImpl implements AjaxStatus {
  
  private final WeakReference componentRef;
  private boolean mustRender;
  private Integer componentHashCode;
  private boolean wasVisible;
  private boolean wasEnabled;
  
  private final class Visitor extends AllComponentVisitor {

    private final boolean render;

    private Visitor( final boolean render ) {
      this.render = render;
    }

    public boolean doVisit( final WebComponent component ) {
      Object adapter = component.getAdapter( AjaxStatus.class );
      AjaxStatus ajaxStatus = ( AjaxStatus )adapter;
      ajaxStatus.setMustRender( render );
      return true;
    }
  }

  /** <p>Sole constructor.</p>*/
  public AjaxStatusImpl( final WebComponent component ) {
    this.componentRef = new WeakReference( component );
  }

  public WebComponent getComponent() {
    return ( WebComponent )componentRef.get();
  }

  public boolean hasComponentHashCode() {
    return componentHashCode != null;
  }
  
  public int getComponentHashCode() {
    if ( componentHashCode == null ) {
      String text = "No hash code available for component ''{0}''";
      String msg = MessageFormat.format( text, new Object[] { componentRef } );
      throw new IllegalStateException( msg );
    }
    return componentHashCode.intValue();
  }

  public void setComponentHashCode( final int hashCode ) {
    this.componentHashCode = new Integer( hashCode );
  }

  public boolean mustRender() {
    return mustRender;
  }

  public void setMustRender( final boolean mustRender ) {
    this.mustRender = mustRender;
  }
  
  public void updateStatus( final boolean mustRender ) {
    setMustRender( mustRender );
    ComponentTreeVisitor visitor = new Visitor( mustRender );
    int strategy = ComponentTreeVisitor.STRATEGY_BREADTH_FIRST;
    ComponentTreeVisitor.accept( getComponent(), strategy, visitor );
  }

  public boolean isWasVisible() {
    return wasVisible;
  }

  public void setWasVisible( final boolean wasVisible ) {
    this.wasVisible = wasVisible;
  }

  public void setWasEnabled( final boolean wasEnabled ) {
    this.wasEnabled = wasEnabled;
  }

  public boolean isWasEnabled() {
    return wasEnabled;
  }
}