/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.rms.ui.internal.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.rap.rms.ui.internal.RMSMessages;
import org.eclipse.rap.rms.ui.internal.startup.DatePickerDialogHelper;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Just a Q&D implementation of a datepicker. Should be replaced once there is
 * a component available in standard RWT.
 */
public class DatePickerDialog extends Dialog implements SelectionListener {
  //TODO: [yao] format
  private final SimpleDateFormat FORMATTER
    = new SimpleDateFormat( "MMM yyyy", RWT.getLocale() ); //$NON-NLS-1$
  private Display display = null;
  private Date dateSelection = null;
  private String selectedDate = null;
  private Shell shell = null;
  private CLabel sunday = null;
  private CLabel monday = null;
  private CLabel tuesday = null;
  private CLabel wednesday = null;
  private CLabel thursday = null;
  private CLabel friday = null;
  private CLabel saturday = null;
  private Button yearUp = null;
  private Button yearNext = null;
  private Button monthUp = null;
  private Button monthNext = null;
  private CLabel selectionLabel = null;
  private Button[] days = new Button[ 42 ];
  private Point location;

  
  public DatePickerDialog( final Shell parent, final int style ) {
    super( parent, style );
  }

  public DatePickerDialog( final Shell parent ) {
    this( parent, 0 );
  }
  
  public void setSelectedDate( final Date date ) {
    this.dateSelection = date;
  }
  
  public void setLocation( final Point location ) {
    this.location = location;
  }

  public Object open() {
    Shell parent = getParent();
    display = Display.getDefault();
    shell = new Shell( parent, SWT.NO_TRIM );
    
    GridLayout gridLayout = new GridLayout();
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.horizontalSpacing = 1;
    gridLayout.verticalSpacing = 1;
    gridLayout.numColumns = 7;
    gridLayout.makeColumnsEqualWidth = true;
    shell.setLayout( gridLayout );

    Calendar now = Calendar.getInstance();
    if( dateSelection == null ) {
      dateSelection = new Date( now.getTimeInMillis() );
    }
    now.setTime( dateSelection );
    
    createNavigation();
    createWeekDays();
    
    for( int i = 0; i < 42; i++ ) {
      days[ i ] = new Button( shell, SWT.FLAT | SWT.CENTER );
      int fillBoth = GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL;
      GridData gridData = new GridData(fillBoth );
      gridData.heightHint = 25;
      gridData.widthHint = 25;
      days[ i ].setLayoutData( gridData );
      days[ i ].setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      days[ i ].addSelectionListener( this );
    }
    setDayForDisplay( now );
    
    
    if( location != null ) {
      shell.setLocation( location );
    }
    shell.pack();
    shell.addShellListener( new ShellAdapter() {
      @Override
      public void shellDeactivated( ShellEvent e ) {
        doClose();
      }
    } );
    shell.open();
    shell.setActive();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return selectedDate;
  }

  
  //////////////////////////////
  // interface SelectionListener
  
  public void widgetDefaultSelected( final SelectionEvent evt ) {
  }

  public void widgetSelected( final SelectionEvent evt ) {
    Button day = ( Button )evt.getSource();
    String[] split = selectionLabel.getText().split( " " ); //$NON-NLS-1$
    this.selectedDate = split[ 0 ] + " " + day.getText() + " " + split[ 1 ]; //$NON-NLS-1$ //$NON-NLS-2$
    doClose();
  }
  
  
  //////////////////
  // helping methods

  private int getLastDayOfMonth( final int year, final int month ) {
    int result = 31;
    if( month == 4 || month == 6 || month == 9 || month == 11 ) {
      result = 30;
    } else if( month == 2 ) {
      if( isLeapYear( year ) ) {
        result = 29;
      } else {
        result = 28;
      }
    }
    return result;
  }

  private boolean isLeapYear( final int year ) {
    return ( year % 4 == 0 && year % 100 != 0 ) || ( year % 400 == 0 );
  }

  private void moveTo( final int type, final int value ) {
    Calendar now = Calendar.getInstance();
    now.setTime( dateSelection );
    now.add( type, value );
    dateSelection = new Date( now.getTimeInMillis() );
    selectionLabel.setText( FORMATTER.format( dateSelection ) );
    setDayForDisplay( now );
    shell.layout( true );
  }

  private void setDayForDisplay( final Calendar now ) {
    int currentDay = now.get( Calendar.DATE );
    now.add( Calendar.DAY_OF_MONTH, -( now.get( Calendar.DATE ) - 1 ) );
    int startIndex = now.get( Calendar.DAY_OF_WEEK ) - 1;
    int year = now.get( Calendar.YEAR );
    int month = now.get( Calendar.MONTH ) + 1;
    int lastDay = this.getLastDayOfMonth( year, month );
    int endIndex = startIndex + lastDay - 1;
    int startday = 1;
    for( int i = 0; i < 42; i++ ) {
      Color temp = days[ i ].getBackground();
      if( temp.equals( display.getSystemColor( SWT.COLOR_BLUE ) ) ) {
        days[ i ].setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
      }
    }
    for( int i = 0; i < 42; i++ ) {
      if( i >= startIndex && i <= endIndex ) {
        days[ i ].setVisible( true );
        days[ i ].setText( String.valueOf( startday ) );
        days[ i ].setForeground( display.getSystemColor( SWT.COLOR_BLACK ) );
        days[ i ].setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
        if( startday == currentDay ) {
          Color selectionBg = DatePickerDialogHelper.getNavigationBgColor();
          days[ i ].setBackground( selectionBg );
          Color selectionFg = DatePickerDialogHelper.getNavigationFgColor();
          days[ i ].setForeground( selectionFg );
          days[ i ].setFocus();
          shell.setDefaultButton( days[ i ] );
        }
        startday++;
      } else {
        days[ i ].setVisible( false );
        days[ i ].setForeground( display.getSystemColor( SWT.COLOR_WHITE ) );
      }
    }
  }

  private void previousYear() {
    moveTo( Calendar.YEAR, -1 );
  }

  private void nextYear() {
    moveTo( Calendar.YEAR, 1 );
  }

  private void nextMonth() {
    moveTo( Calendar.MONTH, 1 );
  }

  private void previousMonth() {
    moveTo( Calendar.MONTH, -1 );
  }

  private void createNavigation() {
    Composite composite = new Composite( this.shell, SWT.NONE );
    int fillBoth = GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL;
    GridData gdComposite = new GridData( fillBoth );
    gdComposite.horizontalSpan = 7;
    gdComposite.heightHint = 22;
    composite.setLayoutData( gdComposite );
    Color bgColor = DatePickerDialogHelper.getNavigationBgColor();
    Color fgColor = DatePickerDialogHelper.getNavigationFgColor();
    composite.setBackground( bgColor );

    GridLayout gridLayout = new GridLayout( 7, true );
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 1;
    gridLayout.horizontalSpacing = 1;
    composite.setLayout( gridLayout );

    int buttonStyle = SWT.PUSH;
    yearUp = new Button( composite, buttonStyle );
    setNavigationColors( yearUp, bgColor, fgColor );
    yearUp.setText( "<" ); //$NON-NLS-1$
    yearUp.setLayoutData( createNavigationLayoutData() );
    yearUp.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        previousYear();
      }
    } );
    
    monthUp = new Button( composite, buttonStyle );
    monthUp.setText( "<<" ); //$NON-NLS-1$
    setNavigationColors( monthUp, bgColor, fgColor );
    monthUp.setLayoutData( createNavigationLayoutData() );
    monthUp.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        previousMonth();
      }
    } );
    
    selectionLabel = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    GridData gdNowLabel = new GridData( GridData.FILL_HORIZONTAL );
    gdNowLabel.horizontalSpan = 3;
    selectionLabel.setLayoutData( gdNowLabel );
    selectionLabel.setText( FORMATTER.format( dateSelection ) );
    setNavigationColors( selectionLabel, bgColor, fgColor );
    
    monthNext = new Button( composite, buttonStyle );
    monthNext.setText( ">>" ); //$NON-NLS-1$
    setNavigationColors( monthNext, bgColor, fgColor );
    monthNext.setLayoutData( createNavigationLayoutData() );
    monthNext.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        nextMonth();
      }
    } );
    
    yearNext = new Button( composite, buttonStyle );
    yearNext.setText( ">" ); //$NON-NLS-1$
    setNavigationColors( yearNext, bgColor, fgColor );
    yearNext.setLayoutData( createNavigationLayoutData() );
    yearNext.addSelectionListener( new SelectionAdapter() {
      public void widgetSelected( final SelectionEvent e ) {
        nextYear();
      }
    } );
  }

  private void setNavigationColors( final Control control,
                                    final Color bgColor, 
                                    final Color fgColor )
  {
    control.setBackground( bgColor );
    control.setForeground( fgColor );
  }

  private Object createNavigationLayoutData() {
    GridData result = new GridData( GridData.FILL_HORIZONTAL );
    result.heightHint = 20;
    return result;
  }

  private void createWeekDays() {
    Composite composite = new Composite( this.shell, SWT.NONE );
    int fillBoth = GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL;
    GridData gridData = new GridData( fillBoth );
    gridData.horizontalSpan = 7;
    composite.setLayoutData( gridData );
    GridLayout gridLayout = new GridLayout( 7, true );
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 1;
    gridLayout.horizontalSpacing = 1;
    composite.setLayout( gridLayout );


    Color bgColor = DatePickerDialogHelper.getWeekDaysBgColor();
    Color weekEndFgColor = DatePickerDialogHelper.getWeekEndFgColor();
    sunday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    sunday.setText( RMSMessages.get().DatePickerDialog_Sun );
    sunday.setLayoutData( createWeekDaysLayoutData() );
    sunday.setForeground( weekEndFgColor );
    sunday.setBackground( bgColor );
    monday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    monday.setText( RMSMessages.get().DatePickerDialog_Mon );
    monday.setBackground( bgColor );
    monday.setLayoutData( createWeekDaysLayoutData() );
    tuesday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    tuesday.setText( RMSMessages.get().DatePickerDialog_Tue );
    tuesday.setBackground( bgColor );
    tuesday.setLayoutData( createWeekDaysLayoutData() );
    wednesday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    wednesday.setText( RMSMessages.get().DatePickerDialog_Wed );
    wednesday.setBackground( bgColor );
    wednesday.setLayoutData( createWeekDaysLayoutData() );
    thursday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    thursday.setText( RMSMessages.get().DatePickerDialog_Thu );
    thursday.setBackground( bgColor );
    thursday.setLayoutData( createWeekDaysLayoutData() );
    friday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    friday.setText( RMSMessages.get().DatePickerDialog_Fri );
    friday.setBackground( bgColor );
    friday.setLayoutData( createWeekDaysLayoutData() );
    saturday = new CLabel( composite, SWT.CENTER | SWT.SHADOW_OUT );
    saturday.setText( RMSMessages.get().DatePickerDialog_Sat );
    saturday.setForeground( weekEndFgColor );
    saturday.setBackground( bgColor );
    saturday.setLayoutData( createWeekDaysLayoutData() );
  }

  private Object createWeekDaysLayoutData() {
    GridData result = new GridData( GridData.FILL_HORIZONTAL );
    result.heightHint = 20;
    return result;
  }


  private void doClose() {
    this.shell.close();
  }
}
