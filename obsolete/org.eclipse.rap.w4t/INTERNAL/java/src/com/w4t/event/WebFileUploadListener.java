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
package com.w4t.event;

import java.util.EventListener;

/** 
  * <p>A file-upload listener is notified of an upload issued by a
  * <code>WebFileUpload</code> component.</p>
  * @see org.eclipse.rwt.event.WebFileUploadEvent
  */
public interface WebFileUploadListener extends EventListener {

  /** 
   * <p>Invoked when a file has been uploaded.</p> 
   */
  void fileUploaded( WebFileUploadEvent e );

}

 