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

import org.eclipse.swt.widgets.Event;

/**
 * Represents an Upload Event.
 * 
 * @author tjarodrigues
 * @version $Revision: 1.1 $
 */
public class UploadEvent extends Event {

  private static final long serialVersionUID = 487892668094315935L;
  private final boolean finished;
  private final int uploadedParcial;
  private final int uploadedTotal;

  /**
   * Checks if the Upload has finished.
   * 
   * @return <code>True</code> if the Upload has finished, <code>False</code>
   *         otherwise.
   */
  public final boolean isFinished() {
    return finished;
  }

  /**
   * Gets the parcial amount of data uploaded.
   * 
   * @return The parcial amount of data uploaded.
   */
  public final int getUploadedParcial() {
    return this.uploadedParcial;
  }

  /**
   * Gets the total file size.
   * 
   * @return The total file size.
   */
  public final int getUploadedTotal() {
    return this.uploadedTotal;
  }

  /**
   * Creates a new instance of the Upload Event.
   * 
   * @param finished Indicates if the upload is finished.
   * @param uploadedParcial The parcial amount of data uploaded.
   * @param uploadedTotal The total file size.
   */
  public UploadEvent( final boolean finished,
                      final int uploadedParcial,
                      final int uploadedTotal )
  {
    this.finished = finished;
    this.uploadedParcial = uploadedParcial;
    this.uploadedTotal = uploadedTotal;
  }
}
