/******************************************************************************
 * Copyright © 2010-2011 Austin Riddle
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle - initial API and implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.jit;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

public class SpaceTree extends JITGraphWidget
{
    
    public SpaceTree( final Composite parent, final int style )
    {
      super( parent, style );
      nodeColor = new RGB(255,221,136);
      edgeColor = new RGB(0,0,200);
    }
    
}
