/*******************************************************************************
 * Copyright (c) 2010 EclipseSource.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.ui.themeeditor.editor.outline;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.rap.ui.themeeditor.Images;
import org.eclipse.rap.ui.themeeditor.scanner.region.AbstractRegion;
import org.eclipse.rap.ui.themeeditor.scanner.region.SelectorRegion;
import org.eclipse.swt.graphics.Image;

public class RegionLabelProvider extends LabelProvider {

  private Image ruleImage = Images.RULE.createImage();

  public String getText( Object element ) {
    String result = "";
    if( element instanceof SelectorRegion ) {
      AbstractRegion region = ( AbstractRegion )element;
      result = region.getContent();
    }
    return result;
  }
  
  public Image getImage( Object element ) {
    return ruleImage;
  }
  
  public void dispose() {
    ruleImage.dispose();
    super.dispose();
  }
}
