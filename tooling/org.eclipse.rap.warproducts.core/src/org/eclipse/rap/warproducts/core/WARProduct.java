/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rap.warproducts.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.pde.internal.core.iproduct.IAboutInfo;
import org.eclipse.pde.internal.core.iproduct.IArgumentsInfo;
import org.eclipse.pde.internal.core.iproduct.IConfigurationFileInfo;
import org.eclipse.pde.internal.core.iproduct.IIntroInfo;
import org.eclipse.pde.internal.core.iproduct.IJREInfo;
import org.eclipse.pde.internal.core.iproduct.ILauncherInfo;
import org.eclipse.pde.internal.core.iproduct.ILicenseInfo;
import org.eclipse.pde.internal.core.iproduct.IPluginConfiguration;
import org.eclipse.pde.internal.core.iproduct.IProduct;
import org.eclipse.pde.internal.core.iproduct.IProductFeature;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.core.iproduct.IProductModelFactory;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.core.iproduct.ISplashInfo;
import org.eclipse.pde.internal.core.iproduct.IWindowImages;
import org.eclipse.pde.internal.core.util.PDEXMLHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WARProduct implements IWARProduct {

  private static final long serialVersionUID = -8956436490095281273L;
  private IProduct delegate;
  private List libraries;
  private IPath webXmlPath;
  private IPath launchIniPath;

  public WARProduct( final IProduct delegate ) {
    this.delegate = delegate;
    libraries = new ArrayList();
  }
  
  public void addLibrary( final IPath relativeWorkspacePath ) {
    libraries.add( relativeWorkspacePath );
  }

  public IPath[] getLibraries() {
    IPath[] result = new IPath[ libraries.size() ];
    libraries.toArray( result );
    return result;
  }
  
  public boolean contiansLibrary( final IPath relativeWorkspacePath ) {
    return libraries.contains( relativeWorkspacePath );
  }
  
  public void addWebXml( final IPath relativeWorkspacePath ) {
    webXmlPath = relativeWorkspacePath;
  }

  public IPath getWebXml() {
    return webXmlPath;
  }
  
  public void addLaunchIni( final IPath relativeWorkspacePath ) {
    launchIniPath = relativeWorkspacePath;
  }

  public IPath getLaunchIni() {
    return launchIniPath;
  }
  
  public void write( final String indent, final PrintWriter writer) {
    writeMetaInfos( indent, writer );
    writeInfo( indent, writer );
    writePlugins( indent, writer ); 
    writeFeatures( indent, writer );
    writePluginConfigurations( indent, writer );
    writeWARInfo( indent, writer );
    writeCloseProduct( writer ); 
  }

  private void writeMetaInfos( final String indent, final PrintWriter writer ) {
    writer.print( indent + "<product" );
    if( getName() != null && getName().length() > 0 ) {
      writer.print( " "
                    + P_NAME
                    + "=\""
                    + getWritableString( getName() )
                    + "\"" );
    }
    if( getId() != null && getId().length() > 0 ) {
      writer.print( " " + P_UID + "=\"" + getId() + "\"" );
    }
    if( getProductId() != null && getProductId().length() > 0 ) {
      writer.print( " " + P_ID + "=\"" + getProductId() + "\"" );
    }
    if( getApplication() != null && getApplication().length() > 0 ) {
      writer.print( " " + P_APPLICATION + "=\"" + getApplication() + "\"" );
    }
    if( getVersion() != null && getVersion().length() > 0 ) {
      writer.print( " " + P_VERSION + "=\"" + getVersion() + "\"" );
    }
    writer.print( " "
                  + P_USEFEATURES
                  + "=\""
                  + Boolean.toString( useFeatures() )
                  + "\"" );
    writer.print( " "
                  + P_INCLUDE_LAUNCHERS
                  + "=\""
                  + Boolean.toString( includeLaunchers() )
                  + "\"" );
    writer.println( ">" );
  }

  private void writeInfo( final String indent, final PrintWriter writer ) {
    if( getAboutInfo() != null ) {
      writer.println();
      getAboutInfo().write( indent + "   ", writer );
    }
    if( getConfigurationFileInfo() != null ) {
      writer.println();
      getConfigurationFileInfo().write( indent + "   ", writer );
    }
    if( getLauncherArguments() != null ) {
      writer.println();
      getLauncherArguments().write( indent + "   ", writer ); 
    }
    if( getWindowImages() != null ) {
      writer.println();
      getWindowImages().write( indent + "   ", writer ); 
    }
    if( getSplashInfo() != null ) {
      writer.println();
      getSplashInfo().write( indent + "   ", writer ); 
    }
    if( getLauncherInfo() != null ) {
      writer.println();
      getLauncherInfo().write( indent + "   ", writer ); 
    }
    if( getIntroInfo() != null ) {
      writer.println();
      getIntroInfo().write( indent + "   ", writer ); 
    }
    if( getJREInfo() != null ) {
      writer.println();
      getJREInfo().write( indent + "   ", writer ); 
    }
    if( getLicenseInfo() != null ) {
      writer.println();
      getLicenseInfo().write( indent + "   ", writer ); 
    }
    writer.println();
  }

  private void writePlugins( final String indent, final PrintWriter writer ) {
    writer.println( indent + "   <plugins>" ); 
    IProductPlugin[] plugins = getPlugins();
    for( int i = 0; i < plugins.length; i++ ) {
      IProductPlugin plugin = plugins[ i ];
      plugin.write( indent + "      ", writer ); 
    }
    writer.println( indent + "   </plugins>" );
  }

  private void writeFeatures( final String indent, final PrintWriter writer ) {
    IProductFeature[] features = getFeatures();
    if( features.length > 0 ) {
      writer.println();
      writer.println( indent + "   <features>" ); 
      for( int i = 0; i < features.length; i++ ) {
        IProductFeature feature = features[ i ];
        feature.write( indent + "      ", writer );
      }
      writer.println( indent + "   </features>" );
    }
    writer.println();
  }

  private void writePluginConfigurations( final String indent,
                                          final PrintWriter writer )
  {
    IPluginConfiguration[] pluginConfigurations = getPluginConfigurations();
    if( pluginConfigurations.length > 0 ) {
      writer.println( indent + "   <configurations>" ); 
      for( int i = 0; i < pluginConfigurations.length; i++ ) {
        IPluginConfiguration configuration = pluginConfigurations[ i ];
        configuration.write( indent + "      ", writer ); 
      }
      writer.println( indent + "   </configurations>" ); 
    }
    writer.println();
  }
  
  private void writeWARInfo( final String indent, final PrintWriter writer ) {
    writer.print( indent + "   <warConfiguration" );
    if( webXmlPath != null ) {
      writer.write(  " webXml=\"" + webXmlPath.toOSString() + "\"" );
    }
    if( launchIniPath != null ) {
      writer.write(  " launchIni=\"" + launchIniPath.toOSString() + "\"" );
    }
    writer.println( ">" );
    if( libraries.size() > 0 ) {
      writer.println( "   " + "<libraries>" );
      for( int i = 0; i < libraries.size(); i++ ) {
        String libraryPath = ( ( IPath )libraries.get( i ) ).toOSString();
        writer.println( "      " + "<library path=\"" + libraryPath + "\"/>" );
      }
      writer.println( "   " + "</libraries>" );
    }
    writer.println( indent + "   </warConfiguration>" );
  }

  private void writeCloseProduct( final PrintWriter writer ) {
    writer.println( "</product>" );
  }

  public String getWritableString(String source) {
      return PDEXMLHelper.getWritableString(source);
  }
  
  public void parse( final Node node ) {
    if( node.getNodeType() == Node.ELEMENT_NODE
        && node.getNodeName().equals( "product" ) ) { //$NON-NLS-1$
      Element element = ( Element )node;
      parseMetaInfo( element );
      parseInfo( node );
    }
  }
  
  private void parseMetaInfo( final Element element ) {
    setApplication( element.getAttribute( P_APPLICATION ) );
    setProductId( element.getAttribute( P_ID ) );
    setId( element.getAttribute( P_UID ) );
    setName( element.getAttribute( P_NAME ) );
    setVersion( element.getAttribute( P_VERSION ) );
    setUseFeatures( "true".equals( element.getAttribute( P_USEFEATURES ) ) );
    String launchers = element.getAttribute( P_INCLUDE_LAUNCHERS );
    setIncludeLaunchers( ( "true".equals( launchers ) 
                         || launchers.length() == 0 ) );
  }

  private void parseInfo( final Node node ) {
    NodeList children = node.getChildNodes();
    IProductModelFactory factory = getModel().getFactory();
    for( int i = 0; i < children.getLength(); i++ ) {
      Node child = children.item( i );
      if( child.getNodeType() == Node.ELEMENT_NODE ) {
        String name = child.getNodeName();
        if( name.equals( "aboutInfo" ) ) { //$NON-NLS-1$
          setAboutInfo( factory.createAboutInfo() );
          getAboutInfo().parse( child );
        } else if( name.equals( "plugins" ) ) { //$NON-NLS-1$
          parsePlugins( child.getChildNodes() );
        } else if( name.equals( "features" ) ) { //$NON-NLS-1$
          parseFeatures( child.getChildNodes() );
        } else if( name.equals( "configurations" ) ) { //$NON-NLS-1$
          parsePluginConfigurations( child.getChildNodes() );
        } else if( name.equals( "configIni" ) ) { //$NON-NLS-1$
          setConfigurationFileInfo( factory.createConfigFileInfo() );
          getConfigurationFileInfo().parse( child );
        } else if( name.equals( "windowImages" ) ) { //$NON-NLS-1$
          setWindowImages( factory.createWindowImages() );
          getWindowImages().parse( child );
        } else if( name.equals( "splash" ) ) { //$NON-NLS-1$
          setSplashInfo( factory.createSplashInfo() );
          getSplashInfo().parse( child );
        } else if( name.equals( "launcher" ) ) { //$NON-NLS-1$
          setLauncherInfo( factory.createLauncherInfo() );
          getLauncherInfo().parse( child );
        } else if( name.equals( "launcherArgs" ) ) { //$NON-NLS-1$
          setLauncherArguments( factory.createLauncherArguments() );
          getLauncherArguments().parse( child );
        } else if( name.equals( "intro" ) ) { //$NON-NLS-1$
          setIntroInfo( factory.createIntroInfo() );
          getIntroInfo().parse( child );
        } else if( name.equals( "vm" ) ) { //$NON-NLS-1$
          setJREInfo( factory.createJVMInfo() );
          getJREInfo().parse( child );
        } else if( name.equals( "license" ) ) { //$NON-NLS-1$
          setLicenseInfo( factory.createLicenseInfo() );
          getLicenseInfo().parse( child );
        } else if( name.equals( "warConfiguration" ) ) {
          parseWARConfiguration( child );
        }
      }
    }
  }

  private void parsePlugins( NodeList children ) {
    for( int i = 0; i < children.getLength(); i++ ) {
      Node child = children.item( i );
      if( child.getNodeType() == Node.ELEMENT_NODE ) {
        if( child.getNodeName().equals( "plugin" ) ) { //$NON-NLS-1$
          IProductPlugin plugin = getModel().getFactory().createPlugin();
          plugin.parse( child );
          addPlugins( new IProductPlugin[] { plugin } );
        }
      }
    }
  }

  private void parsePluginConfigurations( NodeList children ) {
    for( int i = 0; i < children.getLength(); i++ ) {
      Node child = children.item( i );
      if( child.getNodeType() == Node.ELEMENT_NODE ) {
        if( child.getNodeName().equals( "plugin" ) ) { //$NON-NLS-1$
          IPluginConfiguration configuration = getModel().getFactory()
            .createPluginConfiguration();
          configuration.parse( child );
          IPluginConfiguration[] configurationsArray 
            = new IPluginConfiguration[] { configuration };
          addPluginConfigurations( configurationsArray );
        }
      }
    }
  }

  private void parseFeatures( NodeList children ) {
    for( int i = 0; i < children.getLength(); i++ ) {
      Node child = children.item( i );
      if( child.getNodeType() == Node.ELEMENT_NODE ) {
        if( child.getNodeName().equals( "feature" ) ) { //$NON-NLS-1$
          IProductFeature feature = getModel().getFactory().createFeature();
          feature.parse( child );
          addFeatures( new IProductFeature[] { feature } );
        }
      }
    }
  }
  
  private void parseWARConfiguration( final Node child ) {
    processWARConfigurationAttributes( child );
    processLibrary( child );
  }

  private void processWARConfigurationAttributes( final Node child ) {
    NamedNodeMap attributes = child.getAttributes();
    Node webXmlNode = attributes.getNamedItem( "webXml" );
    if( webXmlNode != null ) {
      String webXml = webXmlNode.getNodeValue();
      webXmlPath = new Path( webXml );
    }
    Node launchIniNode = attributes.getNamedItem( "launchIni" );
    if( launchIniNode != null ) {
      launchIniPath = new Path( launchIniNode.getNodeValue() );
    }
  }

  private void processLibrary( final Node child ) {
    NodeList children = child.getChildNodes();
    for( int i = 0; i < children.getLength(); i++ ) {
      Node childNode = children.item( i );
      if( childNode.getNodeType() == Node.ELEMENT_NODE ) {
        if( childNode.getNodeName().equals( "libraries" ) ) { //$NON-NLS-1$
          processLibraries( childNode );
        }
      }
    }
  }

  private void processLibraries( final Node child ) {
    NodeList children = child.getChildNodes();
    for( int i = 0; i < children.getLength(); i++ ) {
      Node childNode = children.item( i );
      if( childNode.getNodeType() == Node.ELEMENT_NODE ) {
        if( childNode.getNodeName().equals( "library" ) ) {
          NamedNodeMap attributes = childNode.getAttributes();
          Node pathItem = attributes.getNamedItem( "path" );
          libraries.add( new Path( pathItem.getNodeValue() ) );
        }
      }
    }
  }
  
  public void reset() {
    delegate.reset();
    webXmlPath = null;
    launchIniPath = null;
    libraries.clear();
  }
  
  // simple delegate methods

  public String getId() {
    return delegate.getId();
  }

  public String getProductId() {
    return delegate.getProductId();
  }

  public String getName() {
    return delegate.getName();
  }

  public String getApplication() {
    return delegate.getApplication();
  }

  public String getVersion() {
    return delegate.getVersion();
  }

  public String getDefiningPluginId() {
    return delegate.getDefiningPluginId();
  }

  public boolean useFeatures() {
    return delegate.useFeatures();
  }

  public boolean includeLaunchers() {
    return delegate.includeLaunchers();
  }

  public IAboutInfo getAboutInfo() {
    return delegate.getAboutInfo();
  }

  public IConfigurationFileInfo getConfigurationFileInfo() {
    return delegate.getConfigurationFileInfo();
  }

  public IArgumentsInfo getLauncherArguments() {
    return delegate.getLauncherArguments();
  }

  public IJREInfo getJREInfo() {
    return delegate.getJREInfo();
  }

  public IWindowImages getWindowImages() {
    return delegate.getWindowImages();
  }

  public ISplashInfo getSplashInfo() {
    return delegate.getSplashInfo();
  }

  public IIntroInfo getIntroInfo() {
    return delegate.getIntroInfo();
  }

  public ILauncherInfo getLauncherInfo() {
    return delegate.getLauncherInfo();
  }

  public ILicenseInfo getLicenseInfo() {
    return delegate.getLicenseInfo();
  }

  public void addPlugins( final IProductPlugin[] plugin ) {
    delegate.addPlugins( plugin );
  }

  public void addFeatures( final IProductFeature[] feature ) {
    delegate.addFeatures( feature );
  }

  public void addPluginConfigurations( 
    final IPluginConfiguration[] configurations ) 
  {
    delegate.addPluginConfigurations( configurations );
  }

  public void removePlugins( final IProductPlugin[] plugins ) {
    delegate.removePlugins( plugins );
  }

  public void removeFeatures( final IProductFeature[] feature ) {
    delegate.removeFeatures( feature );
  }

  public void removePluginConfigurations( 
    final IPluginConfiguration[] configurations )
  {
    delegate.removePluginConfigurations( configurations );
  }

  public IPluginConfiguration findPluginConfiguration( final String id ) {
    return delegate.findPluginConfiguration( id );
  }

  public IProductPlugin[] getPlugins() {
    return delegate.getPlugins();
  }

  public IProductFeature[] getFeatures() {
    return delegate.getFeatures();
  }

  public IPluginConfiguration[] getPluginConfigurations() {
    return delegate.getPluginConfigurations();
  }

  public void setId( final String id ) {
    delegate.setId( id );
  }

  public void setProductId( final String id ) {
    delegate.setProductId( id );
  }

  public void setVersion( final String version ) {
    delegate.setVersion( version );
  }

  public void setName( final String name ) {
    delegate.setName( name );
  }

  public void setAboutInfo( final IAboutInfo info ) {
    delegate.setAboutInfo( info );
  }

  public void setApplication( final String application ) {
    delegate.setApplication( application );
  }

  public void setConfigurationFileInfo( final IConfigurationFileInfo info ) {
    delegate.setConfigurationFileInfo( info );
  }

  public void setLauncherArguments( final IArgumentsInfo info ) {
    delegate.setLauncherArguments( info );
  }

  public void setJREInfo( final IJREInfo info ) {
    delegate.setJREInfo( info );
  }

  public void setWindowImages( final IWindowImages images ) {
    delegate.setWindowImages( images );
  }

  public void setSplashInfo( final ISplashInfo info ) {
    delegate.setSplashInfo( info );
  }

  public void setIntroInfo( final IIntroInfo introInfo ) {
    delegate.setIntroInfo( introInfo );
  }

  public void setLauncherInfo( final ILauncherInfo info ) {
    delegate.setLauncherInfo( info );
  }

  public void setLicenseInfo( final ILicenseInfo info ) {
    delegate.setLicenseInfo( info );
  }

  public void setUseFeatures( final boolean use ) {
    delegate.setUseFeatures( use );
  }

  public void setIncludeLaunchers( final boolean exclude ) {
    delegate.setIncludeLaunchers( exclude );
  }

  public void swap( final IProductFeature feature1, 
                    final IProductFeature feature2 ) 
  {
    delegate.swap( feature1, feature2 );
  }

  public boolean containsPlugin( final String id ) {
    return delegate.containsPlugin( id );
  }

  public boolean containsFeature( final String id ) {
    return delegate.containsFeature( id );
  }

  public IProductModel getModel() {
    return delegate.getModel();
  }

  public void setModel( final IProductModel model ) {
    delegate.setModel( model );
  }

  public IProduct getProduct() {
    return delegate.getProduct();
  }
  
}
