/*******************************************************************************
 * Copyright (c) 2002, 2010 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.lifecycle;

import junit.framework.TestCase;

import org.eclipse.rwt.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class WidgetUtil_Test extends TestCase {

  public void testFind() {
    Display display = new Display();
    Shell shell = new Shell( display );
    Composite composite = new Composite( shell, SWT.NONE );
    Button button = new Button( composite, SWT.PUSH );
    String shellId = WidgetUtil.getId( shell );
    String compositeId = WidgetUtil.getId( composite );
    String buttonId = WidgetUtil.getId( button );

    assertSame( composite, WidgetUtil.find( composite, compositeId ) );
    assertSame( button, WidgetUtil.find( composite, buttonId ) );
    assertSame( composite, WidgetUtil.find( composite, compositeId ) );
    assertNull( WidgetUtil.find( composite, shellId ) );
  }

  public void testGetVariant() {
    Display display = new Display();
    Shell shell = new Shell( display );
    String valid = "Foo_Bar_23_42";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, valid );
    assertEquals( valid, WidgetUtil.getVariant( shell ) );
    String withDash = "Foo-Bar-23-42";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withDash );
    assertEquals( withDash, WidgetUtil.getVariant( shell ) );
    String withLeadingDash = "-Foo-Bar-23-42";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withLeadingDash );
    assertEquals( withLeadingDash, WidgetUtil.getVariant( shell ) );
    String withNonAscii = "Foo-����-23-42";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withNonAscii );
    assertEquals( withNonAscii, WidgetUtil.getVariant( shell ) );
    String withSpaces = "Foo Bar 23 42 ";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withSpaces );
    try {
      WidgetUtil.getVariant( shell );
      fail( "IAE expected" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    String withColon = "Foo:Bar";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withColon );
    try {
      WidgetUtil.getVariant( shell );
      fail( "IAE expected" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
    String withLeadingNumber = "1-Foo-Bar";
    shell.setData( WidgetUtil.CUSTOM_VARIANT, withLeadingNumber );
    try {
      WidgetUtil.getVariant( shell );
      fail( "IAE expected" );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }
}
