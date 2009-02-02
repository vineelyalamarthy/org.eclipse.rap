/*******************************************************************************
 * Copyright (c) 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

package org.eclipse.swt.custom;

import junit.framework.TestCase;

import org.eclipse.swt.RWTFixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.custom.IStyledTextAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class StyledText_Test extends TestCase {

  private static final String TXT =
    "Eclipse is an open source community.";

  public void testInitialValues() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    assertEquals( "", styledText.getText() );
    assertEquals( 0, styledText.getCaretOffset() );
    assertEquals( new Point( 0, 0 ), styledText.getSelection() );
    assertEquals( 0, styledText.getStyleRanges().length );
  }

  public void testText() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    assertEquals( TXT, styledText.getText() );
    assertEquals( TXT.length(), styledText.getCharCount() );
    try {
      styledText.setText( null );
      fail( "Must not allow to set null-text." );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testSelectionText() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    styledText.setSelection( 14, 25 );
    assertEquals( "open source", styledText.getSelectionText() );
  }

  public void testSelection() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    styledText.setSelection( 0, 7 );
    assertEquals( new Point( 0, 7 ), styledText.getSelection() );
    styledText.setSelection( 10, 300 );
    assertEquals( new Point( 10, 36 ), styledText.getSelection() );
  }

  public void testCaretOffset() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    styledText.setCaretOffset( 10 );
    assertEquals( 10, styledText.getCaretOffset() );
    styledText.setCaretOffset( 300 );
    assertEquals( 36, styledText.getCaretOffset() );
  }

  public void testStyleRange() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    assertEquals( 0, styledText.getStyleRanges().length );
    StyleRange sr = new StyleRange();
    sr.start = 10;
    sr.length = 10;
    sr.fontStyle = SWT.BOLD;
    styledText.setStyleRange( sr );
    assertEquals( 1, styledText.getStyleRanges().length );
    StyleRange sr1 = new StyleRange();
    sr1.start = 12;
    sr1.length = 6;
    sr1.fontStyle = SWT.ITALIC;
    styledText.setStyleRange( sr1 );
    assertEquals( 3, styledText.getStyleRanges().length );
    StyleRange sr2 = new StyleRange();
    sr2.start = 12;
    sr2.length = 100;
    sr2.fontStyle = SWT.ITALIC;
    try {
      styledText.setStyleRange( sr2 );
      fail( "Must not allow to set range out of bounds." );
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  public void testAdapter() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    IStyledTextAdapter adapter
      = ( IStyledTextAdapter )styledText.getAdapter( IStyledTextAdapter.class );
    String html = adapter.getHtml();
    String expected = "<span id=sr0></span><span id=sel></span>"
     + "<span id=sr0>Eclipse&nbsp;is&nbsp;an&nbsp;open&nbsp;source&nbsp;community.</span>";
    assertEquals( expected, html );
  }

  public void testGenerateHtml() {
    Display display = new Display();
    Shell shell = new Shell( display, SWT.SHELL_TRIM );
    StyledText styledText = new StyledText( shell, SWT.NONE );
    styledText.setText( TXT );
    IStyledTextAdapter adapter
      = ( IStyledTextAdapter )styledText.getAdapter( IStyledTextAdapter.class );
    StyleRange sr = new StyleRange();
    sr.start = 10;
    sr.length = 10;
    sr.fontStyle = SWT.BOLD;
    styledText.setStyleRange( sr );
    String html = adapter.getHtml();
    String expected = "<span id=sr0></span><span id=sel></span>"
      + "<span id=sr0>Eclipse&nbsp;is</span>"
      + "<span id=sr10 style='font-weight:bold;'>&nbsp;an&nbsp;open&nbsp;s</span>"
      + "<span id=sr20>ource&nbsp;community.</span>";
    assertEquals( expected, html );
    styledText.setSelection( 14, 25 );
    html = adapter.getHtml();
    expected = "<span id=sr0>Eclipse&nbsp;is</span>"
      + "<span id=sr10 style='font-weight:bold;'>&nbsp;an&nbsp;</span>"
      + "<span id=sel><span id=sr14 style='font-weight:bold;'>open&nbsp;s</span>"
      + "<span id=sr20>ource</span></span><span id=sr25>&nbsp;community.</span>";
    assertEquals( expected, html );
  }

  protected void setUp() throws Exception {
    RWTFixture.setUp();
  }

  protected void tearDown() throws Exception {
    RWTFixture.tearDown();
  }
}
