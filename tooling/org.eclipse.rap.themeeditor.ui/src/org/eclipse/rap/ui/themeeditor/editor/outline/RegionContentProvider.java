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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.rap.ui.themeeditor.scanner.TokenStyleProvider;
import org.eclipse.rap.ui.themeeditor.scanner.region.IRegionExt;

public class RegionContentProvider extends ArrayContentProvider
  implements ITreeContentProvider
{

  public Object[] getChildren( Object parentElement ) {
    return null;
  }

  public Object getParent( Object element ) {
    return null;
  }

  public boolean hasChildren( Object element ) {
    IRegionExt region = (IRegionExt) element;
    return region.getTokenType() == TokenStyleProvider.SELECTOR_TOKEN;
  }

  public Object[] getElements( Object inputElement ) {
    List result = new ArrayList();
    IRegionExt[] elements = ( IRegionExt[] )inputElement;
    for( int i = 0; i < elements.length; i++ ) {
      IRegionExt region = elements[ i ];
      if( region.getTokenType() == TokenStyleProvider.SELECTOR_TOKEN
          && !region.getContent().equals( "" ) )
      {
        result.add( region );
      }
    }
    return result.toArray( new Object[ 0 ] );
  }
}