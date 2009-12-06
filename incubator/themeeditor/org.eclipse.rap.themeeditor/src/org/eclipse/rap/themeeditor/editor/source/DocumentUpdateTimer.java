/*******************************************************************************
 * Copyright (c) 2008 Mathias Schaeffner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mathias Schaeffner - initial API and implementation
 *******************************************************************************/
package org.eclipse.rap.themeeditor.editor.source;

/**
 * Timer that is used to buffer all occurring document change events. A document
 * change event is only forwarded to the editor after a certain delay after the
 * last occurring event. This will save the editor from updating Outline on
 * every received key event.
 */
public class DocumentUpdateTimer implements Runnable {

  private Thread thread;
  private Object mutex = new Object();
  private boolean isReset = false;
  private boolean isStopped = false;
  private IDocumentUpdateListener listener;
  private int updateDelay = 1000;

  public DocumentUpdateTimer( IDocumentUpdateListener listener ) {
    this.listener = listener;
  }

  protected void start() {
    thread = new Thread( this, "DocumentUpdateTimer.update_delay" );
    thread.start();
  }

  public void run() {
    isStopped = false;
    try {
      while( true ) {
        synchronized( mutex ) {
          if( updateDelay != 0 ) {
            mutex.wait( updateDelay );
          }
          if( isReset ) {
            isReset = false;
            continue;
          }
        }
        if( listener != null && !isStopped ) {
          listener.updateDocument();
        }
        break;
      }
    } catch( InterruptedException exception ) {
      exception.printStackTrace();
    }
    thread = null;
  }

  protected void reset() {
    synchronized( mutex ) {
      isReset = true;
      mutex.notifyAll();
    }
  }

  public void stop() {
    synchronized( mutex ) {
      // Thread threadToStop = thread;
      // if( threadToStop != null && threadToStop.isAlive() ) {
      // threadToStop.interrupt();
      // }
      isStopped = true;
    }
  }

  public void documentChanged() {
    if( thread != null && thread.isAlive() ) {
      reset();
    } else {
      start();
    }
  }
}
