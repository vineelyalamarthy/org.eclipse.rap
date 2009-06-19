/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.osgi.commands;

import java.lang.reflect.Field;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.rwt.branding.AbstractBranding;
import org.eclipse.rwt.internal.branding.BrandingManager;
import org.eclipse.rwt.internal.lifecycle.EntryPointManager;

public class RAPCommandProvider implements CommandProvider {

  private static final String BRANDING_HELP = "brandings [-v] [-d] - displays brandings;"
                                              + "add -v to display config elements;"
                                              + "add -d to show default branding\n";
  private static final String EP_HELP = "entrypoints - shows all registered entrypoints\n";
  private static final String SERVICEHANDLER_HELP = "servicehandlers - displays all service handlers";
  private static final String[] HELP_TOPICS = new String[]{
    BRANDING_HELP, EP_HELP, SERVICEHANDLER_HELP
  };

  public String getHelp() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "---RAP Runtime Commands---\n" );
    for( int i = 0; i < HELP_TOPICS.length; i++ ) {
      String topic = HELP_TOPICS[ i ];
      buffer.append( "\t" );
      buffer.append( topic );
    }
    return buffer.toString();
  }

  public void _entrypoints( final CommandInterpreter ci ) throws Exception {
    String[] entryPoints = EntryPointManager.getEntryPoints();
    for( int i = 0; i < entryPoints.length; i++ ) {
      String entrypoint = entryPoints[ i ];
      System.out.println( " * " + entrypoint );
    }
  }

  public void _brandings( final CommandInterpreter ci ) throws Exception {
    AbstractBranding[] brandings = BrandingManager.getAll();
    String nextArgument = ci.nextArgument();
    boolean showDetails = false;
    boolean showDefaultBranding = false;
    while( ( nextArgument != null ) ) {
      if( nextArgument.equals( "-v" ) ) {
        showDetails = true;
      } else if( nextArgument.equals( "-d" ) ) {
        showDefaultBranding = true;
      }
      nextArgument = ci.nextArgument();
    }
    for( int i = 0; i < brandings.length; i++ ) {
      AbstractBranding branding = brandings[ i ];
      printBranding( branding, showDetails );
    }
    if( showDefaultBranding ) {
      Field field = BrandingManager.class.getDeclaredField( "DEFAULT_BRANDING" );
      field.setAccessible( true );
      AbstractBranding defaultBranding = ( AbstractBranding )field.get( null );
      printBranding( defaultBranding, showDetails );
    }
  }

  private void printBranding( AbstractBranding branding, boolean showDetails ) {
    System.out.println( " * " + branding.getTitle() );
    if( showDetails ) {
      System.out.println( "\tservletName: " + branding.getServletName() );
      System.out.println( "\tdefaultEntryPoint: "
                          + branding.getDefaultEntryPoint() );
      System.out.println( "\tthemeId: " + branding.getThemeId() );
      System.out.println( "\tid: " + branding.getId() );
    }
  }
}
