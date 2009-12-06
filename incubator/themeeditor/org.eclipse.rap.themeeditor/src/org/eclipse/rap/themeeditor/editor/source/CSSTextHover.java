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

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.rap.themeeditor.editor.source.region.IRegionExt;
import org.eclipse.swt.graphics.Point;

public class CSSTextHover implements ITextHover {

  private CSSTokenScanner tokenScanner;

  public CSSTextHover( final CSSTokenScanner tokenScanner ) {
    this.tokenScanner = tokenScanner;
  }

  public String getHoverInfo( final ITextViewer textViewer,
                              final IRegion hoverRegion )
  {
    String result = "";
    if( hoverRegion != null ) {
      int offset = hoverRegion.getOffset();
      IRegionExt regionExt = tokenScanner.getRegionExt( offset );
      // TODO [rst] Implement pluggable descriptions to avoid tight coupling
//      result = ThemeDefinitionProvider.getDescription( regionExt,
//                                                       regionExt.getContent()
//                                                         .trim() );
    }
    return result;
  }

  public IRegion getHoverRegion( final ITextViewer textViewer, final int offset )
  {
    IRegion result;
    Point selection = textViewer.getSelectedRange();
    if( selection.x <= offset && offset < selection.x + selection.y ) {
      result = new Region( selection.x, selection.y );
    } else {
      result = new Region( offset, 0 );
    }
    return result;
  }
}
