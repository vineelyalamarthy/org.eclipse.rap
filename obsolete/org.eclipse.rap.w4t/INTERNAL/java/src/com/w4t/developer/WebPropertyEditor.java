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
package com.w4t.developer;

import java.beans.PropertyEditor;
import com.w4t.WebComponent;
import com.w4t.event.WebItemListener;

/**
 * The interface for the PropertyEditors of WebComponent
 * <P>
 * changes:<br>
 * 05.10.2001<br>
 * structure design
 * 25.10.2001<br>
 * rename WebComponentEditor
 */
public interface WebPropertyEditor extends PropertyEditor {

  /**
   * returns a WebComponent representing the editors view
   * in WebComponent-Technologie
   */
  WebComponent getWebComponentView();

  /**
   * Adds the specified WebItemListener to receive WebItemEvents from
   * this WebPropertyEditors WebComponent.
   */
  void addWebItemListener( WebItemListener listener );

  /**
   * Removes the specified WebItemListener to receive WebItemEvents from
   * this WebPropertyEditors WebComponent.
   */
  void removeWebItemListener( WebItemListener listener );

  /**
   * sets the PropertyHandle of the property
   * component to the WebPropertyEditor
   */
  void setPropertyHandle( PropertyHandle propertyHandle );

  /**
   * sets the component to which this property belongs
   */
  void setComponent( Object component );
}

 