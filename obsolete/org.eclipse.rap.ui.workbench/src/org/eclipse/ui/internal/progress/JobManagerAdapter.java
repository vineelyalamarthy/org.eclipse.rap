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

package org.eclipse.ui.internal.progress;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.swt.lifecycle.UICallBackUtil;
import org.eclipse.swt.widgets.Display;

import com.w4t.engine.service.ContextProvider;

class JobManagerAdapter extends ProgressProvider implements IJobChangeListener {
  
  private static JobManagerAdapter _instance;
  private final Map providers;
  private final Map jobs;
  private final ProgressManager defaultProgressManager;
  final Object lock;
  private int count;

  static synchronized JobManagerAdapter getInstance() {
    if( _instance == null ) {
      _instance = new JobManagerAdapter();
    }
    return _instance;
  }
  
  
  private JobManagerAdapter() {
    lock = new Object();
    providers = new HashMap();
    jobs = new HashMap();
    defaultProgressManager = new ProgressManager( true );
    Job.getJobManager().setProgressProvider( this );
    Job.getJobManager().addJobChangeListener( this );
  }
  
  // TODO [fappel]: unset? 
  public void setProgressProvider( final ProgressManager progressManager ) {
    synchronized( lock ) {
      if( ContextProvider.hasContext() ) {
        Display display = Display.getCurrent();
        providers.put( display, progressManager );
        bindToSession( display );
      }
    }
  }

  
  ///////////////////////////////
  // ProgressProvider
  
  public IProgressMonitor createMonitor( final Job job ) {
    ProgressManager manager = findProgressManager( job );
    return manager.createMonitor( job );
  }
  
  public IProgressMonitor createMonitor( final Job job,
                                         final IProgressMonitor group,
                                         final int ticks )
  {
    ProgressManager manager = findProgressManager( job );
    return manager.createMonitor( job, group, ticks );
  }
  
  public IProgressMonitor createProgressGroup() {
    return defaultProgressManager.createProgressGroup();
  }
  
  
  ///////////////////////////////
  // interface IJobChangeListener
  
  public void aboutToRun( final IJobChangeEvent event ) {
    ProgressManager manager = findProgressManager( event.getJob() );
    manager.changeListener.aboutToRun( event );
  }

  public void awake( final IJobChangeEvent event ) {
    ProgressManager manager = findProgressManager( event.getJob() );
    manager.changeListener.awake( event );
  }

  public void done( final IJobChangeEvent event ) {
    ProgressManager manager;
    synchronized( lock ) {
      try {
        manager = findProgressManager( event.getJob() );
        Display display = ( Display )jobs.get( event.getJob() );
        if( display != null ) {
          display.asyncExec( new Runnable() {
            public void run() {
              String id = String.valueOf( event.getJob().hashCode() );
              UICallBackUtil.deactivateUICallBack( id );
            }
          } );
        }
      } finally {
        jobs.remove( event.getJob() );
      }
    }
    manager.changeListener.done( event );
  }

  public void running( final IJobChangeEvent event ) {
    ProgressManager manager = findProgressManager( event.getJob() );
    manager.changeListener.running( event );
  }

  public void scheduled( final IJobChangeEvent event ) {
    ProgressManager manager;
    synchronized( lock ) {
      if( ContextProvider.hasContext() ) {
        jobs.put( event.getJob(), Display.getCurrent() );
        bindToSession( event.getJob() );
        String id = String.valueOf( event.getJob().hashCode() );
        UICallBackUtil.activateUICallBack( id );
      }
      manager = findProgressManager( event.getJob() );
    }
    manager.changeListener.scheduled( event );
  }

  public void sleeping( final IJobChangeEvent event ) {
    ProgressManager manager = findProgressManager( event.getJob() );
    manager.changeListener.sleeping( event );
  }

  
  //////////////////
  // helping methods
  
  private ProgressManager findProgressManager( final Job job ) {
    synchronized( lock ) {
      ProgressManager result;
      Display display = ( Display )jobs.get( job );
      if( display != null ) {
        result = ( ProgressManager )providers.get( display );
        if( result == null ) {
          String msg = "ProgressManager must not be null.";
          throw new IllegalStateException( msg );
        }
      } else {
        result = defaultProgressManager;
      }
      return result;
    }
  }
  
  private void bindToSession( final Object keyToRemove ) {
    String id = JobManagerAdapter.class.getName() + count++;
    HttpSessionBindingListener listener = new HttpSessionBindingListener() {
      public void valueBound( final HttpSessionBindingEvent event ) {
      }
      public void valueUnbound( final HttpSessionBindingEvent event ) {
        synchronized( lock ) {
          providers.remove( keyToRemove );
          jobs.remove( keyToRemove );
        }
      }
    };
    ContextProvider.getSession().setAttribute( id, listener );
  }
}
