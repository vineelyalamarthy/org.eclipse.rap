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

package org.eclipse.rap.ui.internal;

import org.eclipse.rap.jface.resource.ImageDescriptor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.layout.FillLayout;
import org.eclipse.rap.rwt.widgets.Composite;
import org.eclipse.rap.ui.*;
import org.eclipse.rap.ui.views.IViewDescriptor;

class ViewReference 
  extends WorkbenchPartReference
  implements IViewReference
{
  
    private final ViewFactory factory;
    String secondaryId;

  public ViewReference( final ViewFactory factory, 
                        final String id, 
                        final String secondaryId )
  {
    this.factory = factory;
    this.secondaryId = secondaryId;
    IViewDescriptor desc = this.factory.getViewRegistry().find( id );
    ImageDescriptor iDesc = null;
    String title = null;
    if (desc != null) {
        iDesc = desc.getImageDescriptor();
        title = desc.getLabel();
    }

    String name = title;
    init( id, title, "", iDesc, name, "" );
  }

  
  /////////////////////////////////////////
  // implementations WorkbenchPartReference
  
  protected PartPane createPane() {
    return new ViewPane( this, this.factory.page );
  }

  protected IWorkbenchPart createPart() {
    // Check the status of this part
    IWorkbenchPart result = null;
    PartInitException exception = null;
    // Try to restore the view -- this does the real work of restoring the
    // view
    //
    try {
      result = createPartHelper();
    } catch( PartInitException e ) {
			// TODO [rh] preliminary: until ErrorViewPart is in place, at least
      //      show the programmer what happened in the console
      e.printStackTrace();
      // end of preliminary    
      exception = e;
    }
    // If unable to create the part, create an error part instead
//    if( exception != null ) {
//      IStatus partStatus = exception.getStatus();
//      IStatus displayStatus = StatusUtil.newStatus( partStatus,
//                                                    NLS.bind( WorkbenchMessages.ViewFactory_initException,
//                                                              partStatus.getMessage() ) );
//      IStatus logStatus = StatusUtil.newStatus( partStatus,
//                                                NLS.bind( "Unable to create view ID {0}: {1}", getId(), partStatus.getMessage() ) ); //$NON-NLS-1$
//      WorkbenchPlugin.log( logStatus );
//      IViewDescriptor desc = factory.viewReg.find( getId() );
//      String label = getId();
//      if( desc != null ) {
//        label = desc.getLabel();
//      }
//      ErrorViewPart part = new ErrorViewPart( displayStatus );
//      PartPane pane = getPane();
//      ViewSite site = new ViewSite( this,
//                                    part,
//                                    factory.page,
//                                    getId(),
//                                    PlatformUI.PLUGIN_ID,
//                                    label );
//      site.setActionBars( new ViewActionBars( factory.page.getActionBars(),
//                                              site,
//                                              ( ViewPane )pane ) );
//      try {
//        part.init( site );
//      } catch( PartInitException e ) {
//        WorkbenchPlugin.log( e );
//        return null;
//      }
//      part.setPartName( label );
//      Composite parent = ( Composite )pane.getControl();
//      Composite content = new Composite( parent, SWT.NONE );
//      content.setLayout( new FillLayout() );
//      try {
//        part.createPartControl( content );
//      } catch( Exception e ) {
//        content.dispose();
//        WorkbenchPlugin.log( e );
//        return null;
//      }
//      result = part;
//    }
    return result;
  }

  private IWorkbenchPart createPartHelper() throws PartInitException {
    IWorkbenchPart result = null;
//    IMemento stateMem = null;
//    if( memento != null ) {
//      stateMem = memento.getChild( IWorkbenchConstants.TAG_VIEW_STATE );
//    }
//    IViewDescriptor desc = factory.viewReg.find( getId() );
    IViewDescriptor desc = factory.getViewRegistry().find( getId() );
    if( desc == null ) {
//      throw new PartInitException( WorkbenchMessages.ViewFactory_couldNotCreate );
      throw new PartInitException( "Could not create view " + getId() );
    }
    // Create the part pane
    PartPane pane = getPane();
    // Create the pane's top-level control
    pane.createControl( factory.page.getClientComposite() );
    String label = desc.getLabel(); // debugging only
    // Things that will need to be disposed if an exception occurs (they are
    // listed here
    // in the order they should be disposed)
    Composite content = null;
    IViewPart initializedView = null;
    ViewSite site = null;
    ViewActionBars actionBars = null;
    // End of things that need to be explicitly disposed from the try block
    try {
      IViewPart view = null;
      try {
//        UIStats.start( UIStats.CREATE_PART, label );
        view = desc.createView();
      } finally {
//        UIStats.end( UIStats.CREATE_PART, view, label );
      }
      // Create site
      site = new ViewSite( this, view, factory.page, desc );
//      actionBars = new ViewActionBars( factory.page.getActionBars(),
//                                       site,
//                                       ( ViewPane )pane );
      // TODO [rh] work around missing IServiceLocator
      actionBars = new ViewActionBars( factory.page.getActionBars(),
                                       ( ViewPane )pane );
      site.setActionBars( actionBars );
      try {
//        UIStats.start( UIStats.INIT_PART, label );
//        view.init( site, stateMem );
        view.init( site );
        // Once we've called init, we MUST dispose the view. Remember
        // the fact that
        // we've initialized the view in case an exception is thrown.
        initializedView = view;
      } finally {
//        UIStats.end( UIStats.INIT_PART, view, label );
      }
      if( view.getSite() != site ) {
        throw new PartInitException( "Wrong site." );
//        throw new PartInitException( WorkbenchMessages.ViewFactory_siteException,
//                                     null );
      }
      int style = RWT.NONE;
//      if( view instanceof IWorkbenchPartOrientation ) {
//        style = ( ( IWorkbenchPartOrientation )view ).getOrientation();
//      }
      // Create the top-level composite
      {
        Composite parent = ( Composite )pane.getControl();
        content = new Composite( parent, style );
        content.setLayout( new FillLayout() );
        try {
//          UIStats.start( UIStats.CREATE_PART_CONTROL, label );
          view.createPartControl( content );
//          parent.layout( true );
          parent.layout();
        } finally {
//          UIStats.end( UIStats.CREATE_PART_CONTROL, view, label );
        }
      }
      // Install the part's tools and menu
      {
        ViewActionBuilder builder = new ViewActionBuilder();
        builder.readActionExtensions( view );
        ActionDescriptor[] actionDescriptors = builder.getExtendedActions();
//        IKeyBindingService keyBindingService = view.getSite()
//          .getKeyBindingService();
//        if( actionDescriptors != null ) {
//          for( int i = 0; i < actionDescriptors.length; i++ ) {
//            ActionDescriptor actionDescriptor = actionDescriptors[ i ];
//            if( actionDescriptor != null ) {
//              IAction action = actionDescriptors[ i ].getAction();
//              if( action != null && action.getActionDefinitionId() != null ) {
//                keyBindingService.registerAction( action );
//              }
//            }
//          }
//        }
        site.getActionBars().updateActionBars();
      }
      // The editor should now be fully created. Exercise its public
      // interface, and sanity-check
      // it wherever possible. If it's going to throw exceptions or behave
      // badly, it's much better
      // that it does so now while we can still cancel creation of the
      // part.
//      PartTester.testView( view );
      result = view;
//      IConfigurationElement element = ( IConfigurationElement )desc.getAdapter( IConfigurationElement.class );
//      if( element != null ) {
//        factory.page.getExtensionTracker()
//          .registerObject( element.getDeclaringExtension(),
//                           view,
//                           IExtensionTracker.REF_WEAK );
//      }
    } catch( Throwable e ) {
      if( ( e instanceof Error ) && !( e instanceof LinkageError ) ) {
        throw ( Error )e;
      }
      // An exception occurred. First deallocate anything we've allocated
      // in the try block (see the top
      // of the try block for a list of objects that need to be explicitly
      // disposed)
      if( content != null ) {
        try {
          content.dispose();
        } catch( RuntimeException re ) {
          Activator.log( re );
        }
      }
      if( initializedView != null ) {
        try {
//          initializedView.dispose();
        } catch( RuntimeException re ) {
          Activator.log( re );
        }
      }
      if( site != null ) {
        try {
//          site.dispose();
        } catch( RuntimeException re ) {
          Activator.log( re );
        }
      }
      if( actionBars != null ) {
        try {
          actionBars.dispose();
        } catch( RuntimeException re ) {
          WorkbenchPlugin.log( re );
        }
      }
      throw new PartInitException( Activator.getStatus( e ) );
    }
    return result;
  }

  
  ///////////////////////////
  // interface IViewReference
  
  public String getSecondaryId() {
    return secondaryId;
  }

  public IViewPart getView( boolean restore ) {
    return ( IViewPart )getPart( restore );
  }
}
