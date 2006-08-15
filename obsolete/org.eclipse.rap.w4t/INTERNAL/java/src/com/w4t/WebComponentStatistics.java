/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/
package com.w4t;

import java.util.*;

/** <p>A WebComponentStatistics encapsulates statistical information about 
  * the web application that is currently running: how many WebComponents
  * of which types have been created, how many sessions currently are on 
  * the web application, memory usage, application uptime etc.</p>
  */
public class WebComponentStatistics {

  /** contains the fully qualified component names as keys and their number
    * as values. */
  private Hashtable htComponentCounts;
  /** the number of components altogether in the web application. */
  private int componentCountAltogether = -1;
  /** the number of sessions that are currently open on the web 
    * application. */
  private int sessionCount = 1;
  /* the amount of memory currently used by the web application. */
  private long occupiedMemoryKB = -1;
  /** the time at which this WebComponentStatistics was created. */
  private Date creationTime = null;
  /** the time the web application runs by now. */
  private long applicationUptime = -1;
  /** the time doing the statistics contained in this WebComponentStatistics
    * took. */
  private long statisticsTime = -1;


  /** <p>constructs a new webComponentStatistics, thereby determining the time
    * of the creation.</p> */
  public WebComponentStatistics() {
    this.creationTime = new Date();
    htComponentCounts = new Hashtable();
  }

  public void applyComponentCounts( final Hashtable componentCounts ) {
    this.htComponentCounts = componentCounts;
  }

  public Hashtable getComponentCounts() {
    return htComponentCounts;
  }
  
  
  // attribute getters and setters
  ////////////////////////////////
  
  /** sets the number of components altogether in the web application. */
  public void setComponentCountAltogether( final int count ) {
    this.componentCountAltogether = count;
  }

  /** returns the number of components altogether in the web application. */
  public int getComponentCountAltogether() {
    return componentCountAltogether;
  }
  
  /** sets the number of sessions that are currently open on the web 
    * application. */
  public void setSessionCount( final int sessionCount ) {
    this.sessionCount = sessionCount;
  }

  /** returns the number of sessions that are currently open on the web 
    * application. */
  public int getSessionCount() {
    return sessionCount;
  }

  /* sets the amount of memory currently used by the web application. */
  public void setOccupiedMemoryKB( final long occupiedMemoryKB ) {
    this.occupiedMemoryKB = occupiedMemoryKB;
  }
 
  /* returns the amount of memory currently used by the web application. */
  public long getOccupiedMemoryKB() {
    return occupiedMemoryKB;
  }

  /** returns the time at which this WebComponentStatistics was created. */
  public Date getCreationTime() {
    return creationTime;
  }
  
  /** sets the time the web application runs by now. */
  public void setApplicationUptime( final long applicationUptime ) {
    this.applicationUptime = applicationUptime;
  }

  /** returns the time the web application runs by now. */
  public long getApplicationUptime() {
    return applicationUptime;
  }
  
  /** sets the time doing the statistics contained in this
    * WebComponentStatistics took. */
  public void setStatisticsTime( final long statisticsTime ) {
    this.statisticsTime = statisticsTime;
  }
  
  /** sets the time doing the statistics contained in this
    * WebComponentStatistics took. */
  public long getStatisticsTime() {
    return statisticsTime;
  }

  
  // standard functionality
  /////////////////////////
  
  /** <p>returns a string representation of this WebComponentStatistics.</p> */
  public String toString() {
    StringBuffer result = new StringBuffer();

    String nl = "\n";
    String[] componentCounts = getComponentCountsText();
    result.append( "WebComponentRegistry statistics at " );
    result.append( creationTime );
    result.append( nl );
    for( int i = 0; i < componentCounts.length; i++ ) {
      result.append( componentCounts[ i ] );
      result.append( nl );
    }
    result.append( getComponentCountAltogetherText() );
    result.append( nl );
    result.append( getApplicationUptimeText() );
    result.append( nl );
    result.append( getSessionCountText() );
    result.append( nl );
    result.append( getOccupiedMemoryKBText() );
    result.append( nl );
    result.append( getStatisticsTimeText() );
    result.append( nl );

    return result.toString();
  }
  
  /** integrates all statistics found in statsToIntegrate to the corresponding 
    * statistics elements in this WebComponentStatistics. */
  public void integrate( final WebComponentStatistics statsToIntegrate ) {
    int compAdded =   statsToIntegrate.getComponentCountAltogether()
                    + getComponentCountAltogether(); 
    setComponentCountAltogether( compAdded );
    
    int sessionsAdded =   statsToIntegrate.getSessionCount()
                        + getSessionCount();
    setSessionCount( sessionsAdded );

    long appUptimeLater = Math.max( statsToIntegrate.getApplicationUptime(),
                                    getApplicationUptime() );
    setApplicationUptime( appUptimeLater );

    long statsTimeAdded =   statsToIntegrate.getStatisticsTime()
                          + getStatisticsTime(); 
    setStatisticsTime( statsTimeAdded );
  
    integrateComponentCounts( statsToIntegrate );
  }

  
  // helping methods
  //////////////////

  private void integrateComponentCounts( 
                               final WebComponentStatistics statsToIntegrate ) {
    Hashtable stats = statsToIntegrate.getComponentCounts();
    Enumeration keys = stats.keys();
    while( keys.hasMoreElements() ) {
      String componentName = ( String )keys.nextElement();
      int count = ( ( Integer )stats.get( componentName ) ).intValue();
      apply( componentName, count );
    }    
  }
  
  private void apply( final String componentName, final int count ) {
    if( htComponentCounts.containsKey( componentName ) ) {
      int oldCount 
        = ( ( Integer )htComponentCounts.get( componentName ) ).intValue();
      Integer newCount = new Integer( oldCount + count );
      htComponentCounts.put( componentName, newCount );
    } else {
      htComponentCounts.put( componentName, new Integer( count ) );
    }
  }
  
  // public for the moment, TODO: move texting to admin?
  public String[] getComponentCountsText() {
    String[] names = new String[ htComponentCounts.keySet().size() ];
    htComponentCounts.keySet().toArray( names );
    Arrays.sort( names );
    String[] componentCounts = new String[ names.length ];
    for( int i = 0; i < names.length; i++ ) {
      String componentName = names[ i ];
      Integer wrapper = ( Integer )htComponentCounts.get( componentName );
      componentCounts[ i ] = wrapper.intValue() + " : " + componentName;
    }
    return componentCounts;
  }

  public String getComponentCountAltogetherText() {
    return componentCountAltogether + ": Components in the registry.";
  }

  public String getSessionCountText() {
    return   "Currently there are "
           + sessionCount
           + " sessions on this application.";
  }

  public String getOccupiedMemoryKBText() {
    return occupiedMemoryKB + " kB of memory occupied.";
  }

  public String getApplicationUptimeText() {
    return   "Application runs "
           + ( applicationUptime / 1000 )
           + " s by now.";
  }

  /** <p>returns the time doing the statistics contained in this
    * WebComponentStatistics took.</p> */
  public String getStatisticsTimeText() {
    return  "Statistics took " + statisticsTime + " ms.\n";
  }
}