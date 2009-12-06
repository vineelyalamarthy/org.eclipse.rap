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

import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.TypedRegion;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

public class CSSDamagerRepairer extends DefaultDamagerRepairer {

  public CSSDamagerRepairer( final ITokenScanner scanner ) {
    super( scanner );
  }

  public void createPresentation( final TextPresentation presentation,
                                  final ITypedRegion region )
  {
    ITypedRegion newRegion = new TypedRegion( 0, fDocument.getLength(), null );
    super.createPresentation( presentation, newRegion );
  }
}
