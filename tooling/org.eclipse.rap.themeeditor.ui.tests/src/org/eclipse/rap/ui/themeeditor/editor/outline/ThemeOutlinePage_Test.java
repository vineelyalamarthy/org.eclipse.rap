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

import junit.framework.TestCase;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.ui.themeeditor.scanner.Scanner;
import org.eclipse.rap.ui.themeeditor.scanner.region.IRegionExt;
import org.eclipse.swt.widgets.Shell;

public class ThemeOutlinePage_Test extends TestCase {

  public void testInstallProviders() {
    Scanner scanner = new Scanner();
    ThemeOutlinePage outlinePage = new ThemeOutlinePage( scanner );
    outlinePage.createControl( new Shell() );
    TreeViewer viewer = outlinePage.getViewer();
    assertNotNull( viewer.getContentProvider() );
    assertNotNull( viewer.getLabelProvider() );
    assertNotNull( viewer.getInput() );
  }

  public void testContentProvider() {
    RegionContentProvider contentProvider = new RegionContentProvider();
    Scanner scanner = new Scanner();
    scanner.scanSheet( "*{font:abc;}Button{color:blue;}" );
    IRegionExt[] regions = scanner.getRegions();
    Object[] elements = contentProvider.getElements( regions );
    assertEquals( 2, elements.length );
    assertTrue( contentProvider.hasChildren( regions[ 0 ] ) );
    assertFalse( contentProvider.hasChildren( regions[ 1 ] ) );
  }

  public void testLabelProvider() {
    RegionLabelProvider labelProvider = new RegionLabelProvider();
    Scanner scanner = new Scanner();
    scanner.scanSheet( "*{font:abc;}Button{color:blue;}" );
    IRegionExt[] regions = scanner.getRegions();
    IRegionExt rootRule = regions[ 0 ];
    assertEquals( "*", labelProvider.getText( rootRule ) );
    assertNotNull( labelProvider.getImage( rootRule ) );
    System.out.println( scanner );
    IRegionExt buttonRule = regions[ 4 ];
    assertEquals( "Button", labelProvider.getText( buttonRule ) );
    assertNotNull( labelProvider.getImage( buttonRule ) );
    labelProvider.dispose();
  }
}
