/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * Contributors: Innoopract Informationssysteme GmbH - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.rap.ui.part;

import org.eclipse.core.runtime.*;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.IWorkbenchPart;
import org.eclipse.rap.ui.IWorkbenchPartSite;
import org.eclipse.rap.ui.internal.util.Util;
import org.eclipse.rap.ui.plugin.AbstractUIPlugin;

public abstract class WorkbenchPart 
  implements IWorkbenchPart, IExecutableExtension
{

  private IWorkbenchPartSite partSite;
  private String title = "";
  private String partName;
  private IConfigurationElement configElement;
  
  public abstract void createPartControl( Composite parent );

  public IWorkbenchPartSite getSite() {
    return partSite;
  }

  public Object getAdapter( Class adapter ) {
    return Platform.getAdapterManager().getAdapter( this, adapter );
  }

  protected void setSite( final IWorkbenchPartSite site ) {
    this.partSite = site;
  }

  public String getTitle() {
    return title;
  }

  public String getPartName() {
    return partName;
  }

  public void setInitializationData( IConfigurationElement cfig,
                                     String propertyName,
                                     Object data )
  {
    // Save config element.
    configElement = cfig;
    // Part name and title.
    partName = Util.safeString( cfig.getAttribute( "name" ) );//$NON-NLS-1$;
    title = partName;
    // Icon.
    String strIcon = cfig.getAttribute( "icon" );//$NON-NLS-1$
    if( strIcon == null ) {
      return;
    }
//    imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin( configElement.getNamespace(),
//                                                                  strIcon );
//    if( imageDescriptor == null ) {
//      return;
//    }
//    titleImage = JFaceResources.getResources()
//      .createImageWithDefault( imageDescriptor );
  }

  protected void setPartName( String partName ) {
    internalSetPartName( partName );
    setDefaultTitle();
  }

  void internalSetPartName( String partName ) {
    partName = Util.safeString( partName );
    Assert.isNotNull( partName );
    // Do not send changes if they are the same
    if( Util.equals( this.partName, partName ) ) {
      return;
    }
    this.partName = partName;
//    firePropertyChange( IWorkbenchPartConstants.PROP_PART_NAME );
  }

  void setDefaultTitle() {
//    String description = getContentDescription();
    String name = getPartName();
    String newTitle = name;
//    if( !Util.equals( description, "" ) ) { //$NON-NLS-1$
//      newTitle = MessageFormat.format( WorkbenchMessages.WorkbenchPart_AutoTitleFormat,
//                                       new String[]{
//                                         name, description
//                                       } );
//    }
    setTitle( newTitle );
  }

  protected void setTitle( String title ) {
    title = Util.safeString( title );
    // Do not send changes if they are the same
    if( Util.equals( this.title, title ) ) {
      return;
    }
    this.title = title;
    // firePropertyChange( IWorkbenchPart.PROP_TITLE );
  }
}
