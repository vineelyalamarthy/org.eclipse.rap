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

package org.eclipse.ui.internal.registry;

import java.util.StringTokenizer;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.IViewDescriptor;


public class ViewDescriptor implements IViewDescriptor, IPluginContribution {

  private final IConfigurationElement element;
  private String id;
  private ImageDescriptor imageDescriptor;
  private String[] categoryPath;
  
  public ViewDescriptor( final IConfigurationElement element )
    throws CoreException
  {
    this.element = element;
    loadFromExtension();
  }

  public boolean getAllowMultiple() {
    return false;
  }

  public String[] getCategoryPath() {
	  return categoryPath;
  }

  public String getDescription() {
    throw new UnsupportedOperationException();
  }

  public float getFastViewWidthRatio() {
    throw new UnsupportedOperationException();
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return element.getAttribute( IWorkbenchRegistryConstants.ATT_NAME );
  }

  public Object getAdapter( Class adapter ) {
    throw new UnsupportedOperationException();
  }
  
  public IViewPart createView() throws CoreException {
    Object extension 
      = Activator.createExtension( element, 
                                   IWorkbenchRegistryConstants.ATT_CLASS );
    return ( IViewPart )extension;
  }
  
  public ImageDescriptor getImageDescriptor() {
    if( imageDescriptor != null ) {
      return imageDescriptor;
    }
    String iconName = element.getAttribute( IWorkbenchRegistryConstants.ATT_ICON );
    // If the icon attribute was omitted, use the default one
//    if( iconName == null ) {
//      return PlatformUI.getWorkbench()
//        .getSharedImages()
//        .getImageDescriptor( ISharedImages.IMG_DEF_VIEW );
//    }
    IExtension extension = element.getDeclaringExtension();
    String extendingPluginId = extension.getNamespace();
    imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin( extendingPluginId,
                                                                  iconName );
    // If the icon attribute was invalid, use the error icon
//    if( imageDescriptor == null ) {
//      imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
//    }
    return imageDescriptor;
  }

  ////////////////////////////////
  // interface IPluginContribution
  
  public String getPluginId() {
    String pluginId = element.getNamespace();
    return pluginId == null ? "" : pluginId;
  }

  public String getLocalId() {
    return getId() == null ? "" : getId();
  }
  
  // ////////////////
  // helping methods
 
  private void loadFromExtension() throws CoreException {
    id = element.getAttribute( IWorkbenchRegistryConstants.ATT_ID );
    String category = element.getAttribute(IWorkbenchRegistryConstants.TAG_CATEGORY);
    
    if (category != null) {
        StringTokenizer stok = new StringTokenizer(category, "/"); //$NON-NLS-1$
        categoryPath = new String[stok.countTokens()];
        // Parse the path tokens and store them
        for (int i = 0; stok.hasMoreTokens(); i++) {
            categoryPath[i] = stok.nextToken();
        }
    }
  }
}
