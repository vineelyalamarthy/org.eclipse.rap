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

import java.io.IOException;

/** <p>the superclass of all Renderers that render components which extend
 *  org.eclipse.rap.Decorator.</p>
  */
public abstract class DecoratorRenderer extends Renderer {
  
  public void scheduleRendering( final WebComponent component ) {
    Decorator decorator = ( Decorator )component;
    WebComponent content = decorator.getContent();
    if( content != null && content.isVisible() )
    {
      getRenderingSchedule().schedule( content );
    }
  }
  
  public void render( final WebComponent component ) throws IOException {
    Decorator dec = ( Decorator )component;
    if( dec.getContent() != null ) {
      dec.getContent().parent = dec.getParent();
      dec.getContent().setDesignTime( dec.getContent().isDesignTime() );
      createDecoratorHead( dec );
      LifeCycleHelper.render( dec.getContent() );
      createDecoratorFoot( dec );
    }      
  }
  
  /** this method will be executed before rendering the decorators content */
  abstract protected void createDecoratorHead(Decorator dec) throws IOException;
  /** this method will be executed after rendering the decorators content */
  abstract protected void createDecoratorFoot(Decorator dec) throws IOException;  
    
}
