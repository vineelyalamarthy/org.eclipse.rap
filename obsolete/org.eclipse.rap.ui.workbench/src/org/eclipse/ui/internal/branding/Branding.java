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
import org.eclipse.rwt.internal.util.ParamCheck;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.osgi.framework.Bundle;

public class Branding {

  private StringBuffer buffer;
  private String id;
  private String title = "";
  private String themeId;
  private String favIcon;
  private String contributor;
  private String servletName;
  private String defaultEntrypointId;
  private List headers;
  private List entrypointIdWhiteList = new ArrayList();

  public List getEntrypoints() {
    return entrypointIdWhiteList;
  }

  public void addEntrypoint( final String entrypointId ) {
    entrypointIdWhiteList.add( entrypointId );
  }

  public String getTitle() {
    return title;
  }

  public void setTitle( final String title ) {
    this.title = title == null ? "" : title;
  }

  public String getId() {
    return id;
  }

  public Branding( final String id ) {
    this.id = id;
  }

  public void addHeader( final String tagname, final HashMap attributes ) {
    if( headers == null ) {
      headers = new ArrayList();
    }
    Header h = new Header( tagname );
    h.setAttributes( attributes );
    headers.add( h );
  }

  public String renderHeaders() {
    registerResource( favIcon );
    if( headers == null ) {
      return "";
    }
    StringBuffer buffer = new StringBuffer();
    for( Iterator iter = headers.iterator(); iter.hasNext(); ) {
      Header header = ( Header )iter.next();
      try {
        buffer.append( header.render() + "\n" );
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
    return buffer.toString();
  }

  public void registerResource( final String resource ) {
    if( resource == null ) {
      return;
    }
    BrandingRegistry.getInstance().registerImage( resource, this );
  }

  public String getBody() {
    if( buffer == null ) {
      return "";
    }
    return buffer.toString();
  }

  public void setBodyTemplate( final String bodyTemplate ) {
    if( contributor == null || bodyTemplate == null ) {
      return;
    }
    final Bundle bundle = Platform.getBundle( contributor );
    URL url = bundle.getResource( bodyTemplate );
    InputStream inputStream;
    try {
      inputStream = url.openStream();
      if( inputStream != null ) {
        buffer = new StringBuffer();
        try {
          byte[] bytes = new byte[ 512 ];
          int bytesRead = inputStream.read( bytes );
          while( bytesRead != -1 ) {
            buffer.append( new String( bytes, 0, bytesRead ) );
            bytesRead = inputStream.read( bytes );
          }
        } finally {
          inputStream.close();
        }
      }
    } catch( Exception e ) {
      String text = "Could not register body template ''{0}'' contributed by bundle ''{1}''.";
      Object[] param = new Object[]{
        bodyTemplate, contributor
      };
      String msg = MessageFormat.format( text, param );
      Status status = new Status( IStatus.ERROR,
                                  contributor,
                                  IStatus.OK,
                                  msg,
                                  e );
      WorkbenchPlugin.getDefault().getLog().log( status );
    }
  }

  public String getThemeId() {
    return themeId;
  }

  public void setThemeId( final String themeId ) {
    this.themeId = themeId;
  }

  public String getFavIcon() {
    return favIcon;
  }

  public void setFavIcon( final String favIcon ) {
    this.favIcon = favIcon;
  }

  public String getServletName() {
    return servletName;
  }

  public void setServletName( final String servletName ) {
    this.servletName = servletName;
  }

  public String getContributor() {
    return contributor;
  }

  public void setContributor( final String contributor ) {
    ParamCheck.notNull( contributor, "contributor" );
    this.contributor = contributor;
  }

  public String getDefaultEntrypointId() {
    return defaultEntrypointId;
  }

  public void setDefaultEntrypointId( final String defaultEntrypointId ) {
    this.defaultEntrypointId = defaultEntrypointId;
    this.entrypointIdWhiteList.add( defaultEntrypointId );
  }
}
