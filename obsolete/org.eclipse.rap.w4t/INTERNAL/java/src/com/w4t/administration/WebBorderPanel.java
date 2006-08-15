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
package com.w4t.administration;

import com.w4t.*;
import com.w4t.event.WebRenderEvent;
import com.w4t.event.WebRenderListener;
import com.w4t.types.WebColor;

/**
 * a helping panel which produces small borders around the center area of a
 * border layout project.
 * <p>
 */
class WebBorderPanel extends WebPanel {

  private static final String IMAGE 
    = "<div><img src=\"resources/images/transparent.gif\" border=\"0\""
    + " width=\"1\" height=\"1\" align=\"top\" /></div>";
  
  public static final int INNERLINE = 1;
  public static final int OUTERLINE = 2;
  MarkupEmbedder wlbNorth;
  MarkupEmbedder wlbEast;
  MarkupEmbedder wlbWest;
  MarkupEmbedder wlbSouth;
  WebBorderLayout wblHeaderBorder;
  /** determines the type of border of the panel */
  private int borderType = OUTERLINE;

  /** Constructor */
  WebBorderPanel() {
    try {
      wblHeaderBorder = new WebBorderLayout();
      wblHeaderBorder.setWidth( "100%" );
      setWebLayout( wblHeaderBorder );
      wlbNorth = new MarkupEmbedder();
      String content = IMAGE;
      wlbNorth.setContent( content );
      wlbEast = new MarkupEmbedder();
      wlbEast.setContent( content );
      wlbWest = new MarkupEmbedder();
      wlbWest.setContent( content );
      wlbSouth = new MarkupEmbedder();
      wlbSouth.setContent( content );
    } catch( Exception e ) {
      System.out.println( e.getMessage() );
    }
    addWebRenderListener( new WebRenderListener() {
      public void beforeRender( final WebRenderEvent evt ) {
        doBeforeRender();
      }

      public void afterRender( final WebRenderEvent evt ) {
        doAfterRender();
      }
    } );
  }

  private void doAfterRender() {
    remove( wlbNorth );
    remove( wlbEast );
    remove( wlbWest );
    remove( wlbSouth );
  }

  private void doBeforeRender() {
    removeBorderComponents();
    if( getWebLayout() instanceof WebBorderLayout ) {
      wblHeaderBorder = ( WebBorderLayout )getWebLayout();
    } else {
      setWebLayout( wblHeaderBorder );
    }
    // wblHeaderBorder.setWidth( "100%" );
    switch( borderType ) {
      case INNERLINE:
        wblHeaderBorder.getArea( "NORTH" ).setBgColor( new WebColor( "gray" ) );
        wblHeaderBorder.getArea( "WEST" ).setBgColor( new WebColor( "gray" ) );
        wblHeaderBorder.getArea( "SOUTH" ).setBgColor( new WebColor( "white" ) );
        wblHeaderBorder.getArea( "EAST" ).setBgColor( new WebColor( "white" ) );
      break;
      case OUTERLINE:
        wblHeaderBorder.getArea( "NORTH" ).setBgColor( new WebColor( "white" ) );
        wblHeaderBorder.getArea( "WEST" ).setBgColor( new WebColor( "white" ) );
        wblHeaderBorder.getArea( "SOUTH" ).setBgColor( new WebColor( "gray" ) );
        wblHeaderBorder.getArea( "EAST" ).setBgColor( new WebColor( "gray" ) );
      break;
    }
    wblHeaderBorder.getArea( "WEST" ).setWidth( "1" );
    wblHeaderBorder.getArea( "EAST" ).setWidth( "1" );
    add( wlbNorth, "NORTH" );
    add( wlbEast, "EAST" );
    add( wlbWest, "WEST" );
    add( wlbSouth, "SOUTH" );
  }

  private void removeBorderComponents() {
    int componentCount = this.getWebComponentCount();
    for( int i = 0; i < componentCount; i++ ) {
      Object constraint = this.getConstraint( i );
      if( constraint instanceof java.lang.String ) {
        String constraintString = ( String )constraint;
        if( !constraintString.equalsIgnoreCase( "CENTER" ) ) {
          this.remove( i );
        }
      } else {
        this.remove( i );
      }
    }
  }

  /** returns a deep copy of this WebBorderPanel. */
  public Object clone() throws CloneNotSupportedException {
    Object clone = super.clone();
    // all inits in the constructor must be applied to the clone
    try {
      WebBorderLayout wbl = new WebBorderLayout();
      wbl.setWidth( "100%" );
      ( ( WebBorderPanel )clone ).wblHeaderBorder = wbl;
      ( ( WebBorderPanel )clone ).setWebLayout( wbl );
      MarkupEmbedder wlbTransGif = new MarkupEmbedder();
      wlbTransGif.setContent( IMAGE );
      ( ( WebBorderPanel )clone ).wlbNorth = wlbTransGif;
      ( ( WebBorderPanel )clone ).wlbEast = wlbTransGif;
      ( ( WebBorderPanel )clone ).wlbWest = wlbTransGif;
      ( ( WebBorderPanel )clone ).wlbSouth = wlbTransGif;
    } catch( Exception ex ) {
      System.err.println( "Exception cloning WebBorderLayout:\n"
                          + ex.toString() );
    }
    return clone;
  }

  /** gets the type of border of this panel */
  public int getBorderType() {
    return borderType;
  }

  /** sets the type of border of this panel */
  public void setBorderType( final int borderType ) {
    this.borderType = borderType;
  }

  protected void finalize() throws Throwable {
    super.finalize();
  }

  /**
   * <p>
   * returns a path to an image that represents this WebComponent (widget icon).
   * </p>
   */
  public static String retrieveIconName() {
    return "resources/images/icons/borderpanel.gif";
  }
}