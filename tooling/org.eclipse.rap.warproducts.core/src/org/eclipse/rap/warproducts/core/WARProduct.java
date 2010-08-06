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

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.pde.core.IModelChangeProvider;
import org.eclipse.pde.core.IModelChangedEvent;
import org.eclipse.pde.core.ModelChangedEvent;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.ModelEntry;
import org.eclipse.pde.core.plugin.PluginRegistry;
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
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.core.iproduct.ISplashInfo;
import org.eclipse.pde.internal.core.iproduct.IWindowImages;
import org.eclipse.pde.internal.core.util.PDEXMLHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class WARProduct implements IWARProduct {

  private static final long serialVersionUID = -8956436490095281273L;
  private IProduct delegate;
  private Map libraries;
  private IPath webXmlPath;
  private IPath launchIniPath;

  public WARProduct( final IProduct delegate ) {
    this.delegate = delegate;
    libraries = new HashMap();
  }
  
  public void addLibrary( final IPath libraryPath, final boolean fromTarget ) {
    Object containedPath = libraries.get( libraryPath );
    boolean modified = containedPath == null;
    if( modified ) {
      libraries.put( libraryPath, Boolean.valueOf( fromTarget ) );
    }
    if( getModel().isEditable() && modified ) {
      fireStructureChanged( new IPath[] { libraryPath }, 
                            IModelChangedEvent.INSERT );
    }
  }
  
  public void removeLibrary( final IPath libraryPath ) {
    Object containedPath = libraries.get( libraryPath );
    boolean modified = containedPath != null;
    libraries.remove( libraryPath );
    if( getModel().isEditable() && modified ) {
      fireStructureChanged( new IPath[] { libraryPath }, 
                            IModelChangedEvent.REMOVE );
    }
  }
  
  public void removeLibraries( final IPath[] pathes ) {
    for( int i = 0; i < pathes.length; i++ ) {
      removeLibrary( pathes[ i ] );
    }
  }

  public IPath[] getLibraries() {
    IPath[] result = new IPath[ libraries.size() ];
    Set keySet = libraries.keySet();
    keySet.toArray( result );
    return result;
  }
  
  public boolean isLibraryFromTarget( final IPath libraryPath ) {
    Boolean result = ( Boolean )libraries.get( libraryPath );
    return result.booleanValue();
  }
  
  public boolean contiansLibrary( final IPath relativeWorkspacePath ) {
    Object containedPath = libraries.get( relativeWorkspacePath );
    return containedPath != null;
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
    if( pluginConfigurations != null && pluginConfigurations.length > 0 ) {
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
      IPath[] pathes = new IPath[ libraries.size() ];
      libraries.keySet().toArray( pathes );
      for( int i = 0; i < pathes.length; i++ ) {
        String libraryPath = pathes[ i ].toOSString();
        Boolean fromTarget = ( Boolean )libraries.get( pathes[ i ] );
        writer.println( "      " + "<library path=\"" + libraryPath + 
                        "\" fromTarget=\"" + fromTarget + "\"/>" );
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
    delegate.parse( node );
    if( node.getNodeType() == Node.ELEMENT_NODE
        && node.getNodeName().equals( "product" ) ) { //$NON-NLS-1$
      parseInfo( node );
    }
  }

  private void parseInfo( final Node node ) {
    NodeList children = node.getChildNodes();
    for( int i = 0; i < children.getLength(); i++ ) {
      Node child = children.item( i );
      if( child.getNodeType() == Node.ELEMENT_NODE ) {
        String name = child.getNodeName();
        if( name.equals( "warConfiguration" ) ) {
          parseWARConfiguration( child );
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
      String webXml = getValidPath( webXmlNode.getNodeValue() );
      webXmlPath = new Path( webXml );
    }
    Node launchIniNode = attributes.getNamedItem( "launchIni" );
    if( launchIniNode != null ) {
      String launchIni = getValidPath( launchIniNode.getNodeValue() );
      launchIniPath = new Path( launchIni );
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
          Node fromTargetString = attributes.getNamedItem( "fromTarget" );
          Boolean fromTarget 
            = Boolean.valueOf( fromTargetString.getNodeValue() );
          String libPath = getValidPath( pathItem.getNodeValue() );
          libraries.put( new Path( libPath ), fromTarget );
        }
      }
    }
  }
  
  private String getValidPath( final String path ) {
    StringBuffer buffer = new StringBuffer();
    for( int i = 0; i < path.length(); i++ ) {
      char charAt = path.charAt( i );
      if( charAt == '/' || charAt == '\\' ) {
        charAt = File.separatorChar;
      }
      buffer.append( charAt );
    }
    return buffer.toString();
  }
  
  public void reset() {
    delegate.reset();
    webXmlPath = null;
    launchIniPath = null;
    libraries.clear();
  }
  
  public IPluginConfiguration[] getPluginConfigurations() {
    List containedBundles = new ArrayList();
    IProductPlugin[] plugins = getPlugins();
    if( plugins != null && plugins.length > 0 ) {
      WARProductModelFactory factory = new WARProductModelFactory( getModel() );
      for( int i = 0; i < plugins.length; i++ ) {
        String pluginId = plugins[ i ].getId();
        if( !isBundleFragment( pluginId ) ) {
          IPluginConfiguration conf = factory.createPluginConfiguration();
          conf.setId( pluginId ); 
          conf.setAutoStart( true );
          containedBundles.add( conf );
        }
      }
    }
    IPluginConfiguration[] result 
      = new IPluginConfiguration[ containedBundles.size() ];
    containedBundles.toArray( result );
    return result;
  }
  
  private boolean isBundleFragment( final String pluginId ) {
    boolean result = false;
    ModelEntry entry = PluginRegistry.findEntry( pluginId );
    if( entry != null ) {
      IPluginModelBase[] models = entry.getActiveModels();
      for( int i = 0; i < models.length && !result; i++ ) {
        result = models[ i ].isFragmentModel();
      }
    }
    return result;
  }

  protected void fireStructureChanged( final Object child, 
                                       final int changeType ) 
  {
    fireStructureChanged( new Object[]{ child }, changeType );
  }

  protected void fireStructureChanged( final Object[] children, 
                                       final int changeType )
  {
    if( getModel().isEditable() ) {
      IModelChangeProvider provider = getModel();
      provider.fireModelChanged( new ModelChangedEvent( provider,
                                                        changeType,
                                                        children,
                                                        null ) );
    }
  }
  
  public boolean includeLaunchers() {
    return false;
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
