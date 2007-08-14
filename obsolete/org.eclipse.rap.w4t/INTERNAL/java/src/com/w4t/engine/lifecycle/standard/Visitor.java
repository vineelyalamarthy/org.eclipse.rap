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
package com.w4t.engine.lifecycle.standard;

import com.w4t.*;
import com.w4t.event.WebRenderEvent;
import com.w4t.util.ComponentTreeVisitor;
import com.w4t.util.RendererCache;
import com.w4t.util.ComponentTreeVisitor.AllComponentVisitor;

final class Visitor extends AllComponentVisitor {
  
  final static Visitor BEFORE_RENDER_VISITOR 
    = new Visitor( new BeforeRenderRunnable() );
  final static Visitor AFTER_RENDER_VISITOR 
    = new Visitor( new AfterRenderRunnable() );
  final static Visitor READ_DATA_VISITOR 
    = new Visitor( new ReadDataRunnable() );
  final static Visitor PROCESS_ACTION_VISITOR
    = new Visitor( new ProcessActionRunnable() );
 
  private final IVisitorRunnable runnable;
  
  private static interface IVisitorRunnable {
    public boolean run( WebComponent component );
  }
  
  private static final class ProcessActionRunnable implements IVisitorRunnable {
    public boolean run( final WebComponent component ) {
      LifeCycleHelper.processAction( component );
      return true;
    }
  }

  private static final class AfterRenderRunnable implements IVisitorRunnable {
    public boolean run( final WebComponent component ) {
      if( getRenderingSchedule().isScheduled( component ) ) {
        int evtId = WebRenderEvent.AFTER_RENDER;
        WebRenderEvent evt = new WebRenderEvent( component, evtId );
        evt.processEvent();
      }
      return true;
    }
  }

  private static final class BeforeRenderRunnable implements IVisitorRunnable {
    public boolean run( final WebComponent component ) {
      if( component instanceof WebForm ) {
        getRenderingSchedule().schedule( component );
      }
      // Note: a simple component is scheduled by its parent component 
      if( getRenderingSchedule().isScheduled( component ) ) {
        int evtId = WebRenderEvent.BEFORE_RENDER;
        WebRenderEvent evt = new WebRenderEvent( component, evtId );
        evt.processEvent();
        
        // let the component renderer decide which children should be scheduled
        Class clazz = component.getClass();
        RendererCache rendererCache = RendererCache.getInstance();
        Renderer renderer = rendererCache.retrieveRenderer( clazz );
        renderer.scheduleRendering( component );
      }
      return true;
    }
  }
  
  private static final class ReadDataRunnable implements IVisitorRunnable {
    public boolean run( final WebComponent component ) {
      LifeCycleHelper.readData( component );
      return true;
    }
  }

  
  private Visitor( final IVisitorRunnable runnable ) {
    this.runnable = runnable;
  }
  
  public boolean doVisit( final WebComponent component ) {
    return runnable.run( component );
  }
  
  static void accept( final WebForm form, final Visitor visitor ) {
    ComponentTreeVisitor.accept( form, visitor );
  }
  
  private static IRenderingSchedule getRenderingSchedule() {
    return LifeCycleHelper.getSchedule();
  }
}