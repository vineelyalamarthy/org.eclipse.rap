/*******************************************************************************
 * Copyright (c) 2002-2007 Critical Software S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tiago Rodrigues (Critical Software S.A.) - initial implementation
 *     Joel Oliveira (Critical Software S.A.) - initial commit
 ******************************************************************************/
package org.eclipse.rwt.widgets;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Class the implementes and Upload Event Adapter.
 * 
 * @author tjarodrigues
 * @version $Revision: 1.1 $
 */
public abstract class UploadAdapter implements SWTEventListener {

  /**
   * Fires a new Upload Finished Event.
   * 
   * @param uploadEvent The Upload Event to be fired.
   */
  public void uploadFinished( final UploadEvent uploadEvent ) {
  }
}
