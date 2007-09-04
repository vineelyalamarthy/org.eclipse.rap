/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.ui.internal.branding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.util.ParamCheck;
import org.eclipse.ui.internal.WorkbenchPlugin;

public class BrandingRegistry {

  private static final String EP_BRANDING = "org.eclipse.rap.ui.branding";
  private static final String TAG_ADITIONAL_HEADERS = "additionalHeaders";
  private static final String TAG_WHITELIST_ENTRYPOINTS = "entrypoints";
  private static final String TAG_META = "meta";
  private static final String TAG_LINK = "link";
  private static final String TAG_ATTRIBUTE = "attribute";
  private static final String ATT_NAME = "name";
  private static final String ATT_CONENT = "content";
  private static final String ATT_REL = "rel";
  private static final String ATT_VALUE = "value";
  private static final String ATT_EP_ID = "id";
  private static BrandingRegistry instance;
  private static Branding defaultBranding;
  private List brandings = new ArrayList();
  private Map entrypointAssociation = new HashMap();

  private BrandingRegistry() {
    // prevent instantiation from outside
  }

  public static BrandingRegistry getInstance() {
    if( instance == null ) {
      instance = new BrandingRegistry();
    }
    return instance;
  }

  public void bindEntrypoint( final String id, final String parameter ) {
    entrypointAssociation.put( parameter, id );
  }

  public String getEntrypointId( final String parameter ) {
    return ( String )entrypointAssociation.get( parameter );
  }

  public String getEntrypoint( final String id ) {
    Set entries = entrypointAssociation.entrySet();
    String entrypoint = null;
    Iterator iter = entries.iterator();
    while( entrypoint == null && iter.hasNext() ) {
      Map.Entry entry = ( Map.Entry )iter.next();
      if( entry.getValue() != null && entry.getValue().equals( id ) ) {
        entrypoint = ( String )entry.getKey();
      }
    }
    return entrypoint;
  }

  public static Branding getDefaultBranding() {
    if( defaultBranding == null ) {
      defaultBranding = new Branding( "org.eclipse.rap.branding.default" );
      defaultBranding.setTitle( "RAP Startup Page" );
      defaultBranding.setServletName( "rap" );
    }
    return defaultBranding;
  }

  public void registerBranding( final Branding branding ) {
    brandings.add( branding );
  }

  public Branding getBrandingFor( final String servletName,
                                  final String entrypoint )
  {
    for( Iterator b = brandings.iterator(); b.hasNext(); ) {
      Branding branding = ( Branding )b.next();
      if( servletName.equals( branding.getServletName() ) ) {
        String entrypointId;
        if( entrypoint == null ) {
          entrypointId = branding.getDefaultEntrypointId();
        } else {
          entrypointId = getEntrypointId( entrypoint );
        }
        if( branding.getEntrypoints().contains( entrypointId )
            || branding.getEntrypoints().size() == 1 )
        {
          return branding;
        }
        if( branding.getEntrypoints().size() > 1 ) {
          String msg = "Entrypoint ''{0}'' not allowed for branding ''{1}''";
          Object[] args = new Object[]{
            entrypoint, branding.getId()
          };
          String txt = MessageFormat.format( msg, args );
          throw new IllegalArgumentException( txt );
        }
      }
    }
    return getDefaultBranding();
  }

  public static void readBrandings() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint ep = registry.getExtensionPoint( EP_BRANDING );
    IConfigurationElement[] brandings = ep.getConfigurationElements();
    for( int i = 0; i < brandings.length; i++ ) {
      String contributorName = brandings[ i ].getContributor().getName();
      String id = brandings[ i ].getAttribute( "id" );
      String defaultEntrypointId = brandings[ i ].getAttribute( "defaultEntrypointId" );
      String body = brandings[ i ].getAttribute( "body" );
      String title = brandings[ i ].getAttribute( "title" );
      String servletname = brandings[ i ].getAttribute( "servletName" );
      String favicon = brandings[ i ].getAttribute( "favicon" );
      String theme = brandings[ i ].getAttribute( "themeId" );
      String exitConfirmation = brandings[ i ].getAttribute( "exitConfirmation" );
      Branding branding = new Branding( id );
      branding.setContributor( contributorName );
      branding.setBodyTemplate( body );
      branding.setTitle( title );
      branding.setThemeId( theme );
      branding.setFavIcon( favicon );
      branding.setServletName( servletname );
      branding.setExitConfirmation( exitConfirmation );
      branding.setDefaultEntrypointId( defaultEntrypointId );
      // add favicon
      HashMap att = new HashMap();
      att.put( "rel", "shortcut icon" );
      att.put( "type", "image/x-icon" );
      att.put( "href", favicon );
      branding.addHeader( "link", att );
      // loop through all additional headers
      IConfigurationElement[] ahl = brandings[ i ].getChildren( TAG_ADITIONAL_HEADERS );
      if( ahl.length > 0 ) {
        IConfigurationElement ah = ahl[ 0 ];
        IConfigurationElement[] headers = ah.getChildren();
        for( int j = 0; j < headers.length; j++ ) {
          IConfigurationElement h = headers[ j ];
          HashMap at = new HashMap();
          // add predefined attributes
          String element = h.getName();
          if( element == TAG_META ) {
            at.put( ATT_NAME, h.getAttribute( ATT_NAME ) );
            at.put( ATT_CONENT, h.getAttribute( ATT_CONENT ) );
          } else if( element == TAG_LINK ) {
            at.put( ATT_REL, h.getAttribute( ATT_REL ) );
          }
          // add additional attributes
          IConfigurationElement[] aAtt = h.getChildren( TAG_ATTRIBUTE );
          for( int k = 0; k < aAtt.length; k++ ) {
            at.put( aAtt[ k ].getAttribute( ATT_NAME ),
                    aAtt[ k ].getAttribute( ATT_VALUE ) );
          }
          branding.addHeader( element, at );
        }
      }
      // loop through all whitelisted entrypoints
      IConfigurationElement[] eps = brandings[ i ].getChildren( TAG_WHITELIST_ENTRYPOINTS );
      if( eps.length > 0 ) {
        IConfigurationElement epse = eps[ 0 ];
        IConfigurationElement[] epoints = epse.getChildren();
        for( int j = 0; j < epoints.length; j++ ) {
          IConfigurationElement point = epoints[ j ];
          String entrypointId = point.getAttribute( ATT_EP_ID );
          branding.addEntrypoint( entrypointId );
        }
      }
      WorkbenchPlugin.getDefault()
        .getHttpServiceTracker()
        .addServletAlias( servletname );
      getInstance().registerBranding( branding );
    }
  }

  public void registerImage( final String resource, final Branding branding ) {
    ParamCheck.notNull( resource, "resource" );
    ParamCheck.notNull( branding, "branding" );
    URL scriptUrl = Platform.getBundle( branding.getContributor() )
      .getEntry( resource );
    InputStream is;
    try {
      is = scriptUrl.openStream();
      if( is != null ) {
        RWT.getResourceManager().register( resource, is );
      }
    } catch( IOException e ) {
      e.printStackTrace();
    }
  }
}
