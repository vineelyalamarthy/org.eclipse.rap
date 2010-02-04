/******************************************************************************
 * Copyright (c) 2009-2010 Texas Center for Applied Technology
 * Texas Engineering Experiment Station
 * The Texas A&M University System
 * All Rights Reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *                   initial demo implementation
 * 
 *****************************************************************************/
package org.eclipse.rap.rwt.visualization.google.demo;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory
{

   public void createInitialLayout(IPageLayout layout)
   {
      layout.setEditorAreaVisible(false);
   }

}
