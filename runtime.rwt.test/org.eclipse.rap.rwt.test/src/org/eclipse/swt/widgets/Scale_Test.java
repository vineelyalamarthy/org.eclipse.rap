/*******************************************************************************
 * Copyright (c) 2008, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.swt.widgets;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;


public class Scale_Test extends TestCase {

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testInitialValues() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Scale scale = new Scale( shell, SWT.NONE );
    assertEquals( 0, scale.getMinimum() );
    assertEquals( 100, scale.getMaximum() );
    assertEquals( 0, scale.getSelection() );
    assertEquals( 1, scale.getIncrement() );
    assertEquals( 10, scale.getPageIncrement() );
  }

  public void testValues() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    Scale scale = new Scale( shell, SWT.NONE );

    scale.setSelection( 34 );
    assertEquals( 34, scale.getSelection() );
    scale.setMinimum( 10 );
    assertEquals( 10, scale.getMinimum() );
    scale.setMaximum( 56 );
    assertEquals( 56, scale.getMaximum() );
    scale.setIncrement( 5 );
    assertEquals( 5, scale.getIncrement() );
    scale.setPageIncrement( 15 );
    assertEquals( 15, scale.getPageIncrement() );

    scale.setMinimum( 40 );
    assertEquals( 40, scale.getMinimum() );
    assertEquals( 40, scale.getSelection() );

    scale.setSelection( 52 );
    scale.setMaximum( 50 );
    assertEquals( 50, scale.getMaximum() );
    assertEquals( 50, scale.getSelection() );

    scale.setMaximum( 30 );
    assertEquals( 50, scale.getMaximum() );

    scale.setSelection( 52 );
    assertEquals( 50, scale.getSelection() );

    scale.setSelection( 10 );
    assertEquals( 50, scale.getSelection() );

    scale.setSelection( -10 );
    assertEquals( 50, scale.getSelection() );

    scale.setPageIncrement( -15 );
    assertEquals( 15, scale.getPageIncrement() );

    scale.setIncrement( -5 );
    assertEquals( 5, scale.getIncrement() );
  }

  public void testStyle() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.NONE );
    // Test SWT.NONE
    Scale scale = new Scale( shell, SWT.NONE );
    assertTrue( ( scale.getStyle() & SWT.HORIZONTAL ) != 0 );
    // Test SWT.BORDER
    scale = new Scale( shell, SWT.BORDER );
    assertTrue( ( scale.getStyle() & SWT.HORIZONTAL ) != 0 );
    assertTrue( ( scale.getStyle() & SWT.BORDER ) != 0 );
    // Test SWT.VERTICAL
    scale = new Scale( shell, SWT.VERTICAL );
    assertTrue( ( scale.getStyle() & SWT.VERTICAL ) != 0 );
    // Test combination of SWT.HORIZONTAL | SWT.VERTICAL
    scale = new Scale( shell, SWT.HORIZONTAL | SWT.VERTICAL );
    assertTrue( ( scale.getStyle() & SWT.HORIZONTAL ) != 0 );
    assertTrue( ( scale.getStyle() & SWT.VERTICAL ) == 0 );
  }

  public void testDispose() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Scale scale = new Scale( shell, SWT.NONE );
    scale.dispose();
    assertTrue( scale.isDisposed() );
  }

  public void testComputeSize() {
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    Display display = new Display();
    Shell shell = new Shell( display );
    Scale scale = new Scale( shell, SWT.HORIZONTAL );
    Point expected = new Point( 160, 41 );
    assertEquals( expected, scale.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    scale = new Scale( shell, SWT.HORIZONTAL | SWT.BORDER );
    expected = new Point( 164, 45 );
    assertEquals( expected, scale.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    scale = new Scale( shell, SWT.VERTICAL );
    expected = new Point( 41, 160 );
    assertEquals( expected, scale.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    scale = new Scale( shell, SWT.VERTICAL | SWT.BORDER );
    expected = new Point( 45, 164 );
    assertEquals( expected, scale.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );

    expected = new Point( 104, 104 );
    assertEquals( expected, scale.computeSize( 100, 100 ) );
  }
}
