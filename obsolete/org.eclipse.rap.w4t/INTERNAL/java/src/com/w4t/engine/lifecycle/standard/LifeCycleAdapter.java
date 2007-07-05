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

import java.io.IOException;

import com.w4t.*;
import com.w4t.engine.service.ContextProvider;
import com.w4t.engine.service.IServiceStateInfo;
import com.w4t.engine.util.ExitForm;
import com.w4t.engine.util.WindowManager;
import com.w4t.event.WebRenderAdapter;
import com.w4t.event.WebRenderEvent;
import com.w4t.util.RendererCache;

class LifeCycleAdapter implements ILifeCycleAdapter {
  
  private interface ILifecycleRunnable {
    void run();
  }

  private final class RenderRunnable implements ILifecycleRunnable {
    public void run() {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      stateInfo.setRendereringSchedule( new RenderingSchedule() );
      // dispatch to 'static' invalidated-form if needed 
      dispatchToInvalidated();
      // Obtain form to be rendered
      WebForm formToRender = WindowManager.doDispatch();
      IFormRenderer formRenderer 
        = ( IFormRenderer )retrieveRenderer( formToRender );
      formRenderer.prepare();
      boolean isExceptionOccured = true;
      try {
        if( formRenderer.fireRenderEvents() ) {          
          Visitor.accept( formToRender, Visitor.BEFORE_RENDER_VISITOR );
        }
        try {
          formRenderer.render( formToRender );
        } catch( IOException e ) {
          throw new WrappedIOException( e );
        }
        if( formRenderer.fireRenderEvents() ) {          
          Visitor.accept( formToRender, Visitor.AFTER_RENDER_VISITOR );
        }
        isExceptionOccured = false;
      } finally {
        if( isExceptionOccured ) {
          stateInfo.setExceptionOccured( true );
        }
        stateInfo.setRendereringSchedule( null );
        WindowManager.afterRender();
      }
    }

    private void dispatchToInvalidated() {
      IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
      if( stateInfo.isInvalidated() ) {
        String className = ExitForm.class.getName();
        ExitForm invalidationForm 
          = ( ExitForm )W4TContext.loadForm( className );
        invalidationForm.addWebRenderListener( new WebRenderAdapter() {
          public void afterRender( final WebRenderEvent e ) {
            ContextProvider.getRequest().getSession().invalidate();
          }
        } );
        W4TContext.dispatchTo( invalidationForm );
      }
    }
  }

  private final class ProcessActionRunnable implements ILifecycleRunnable {
    public void run() {
      Visitor.accept( form, Visitor.PROCESS_ACTION_VISITOR );
    }
  }

  private final class ReadDataRunnable implements ILifecycleRunnable {
    public void run() {
      Visitor.accept( form, Visitor.READ_DATA_VISITOR );
    }
  }


  private final WebForm form;
  private final ReadDataRunnable readDataRunnable;
  private final ProcessActionRunnable processActionRunnable;
  private final RenderRunnable renderRunnable;
  
  
  LifeCycleAdapter( final WebForm form ) {
    this.form = form;
    readDataRunnable = new ReadDataRunnable();
    processActionRunnable = new ProcessActionRunnable();
    renderRunnable = new RenderRunnable();
  }

  public void readData() {
    run( readDataRunnable );
  }

  public void processAction() {
    run( processActionRunnable );
  }

  public void render() throws IOException {
    try {
      run( renderRunnable );
    } catch( WrappedIOException e ) {
      throw e.getWrappedException();
    }
  }

  private void run( final ILifecycleRunnable runnable ) {
    runnable.run();
  }
  
  private Renderer retrieveRenderer( final WebForm form ) {
    return RendererCache.getInstance().retrieveRenderer( form.getClass() );
  }  
}