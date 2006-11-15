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
package com.w4t.webgridlayoutkit;

import com.w4t.*;
import com.w4t.util.browser.Default;

/**
 * <p>
 * Tests the rendering of com.w4t.WebGridLayout.
 * </p>
 */
public class WebGridLayoutRenderer_Test extends RenderingTestCase {

  public WebGridLayoutRenderer_Test( final String name ) {
    super( name );
    setGenerateResources( false );
  }

  public void setUp() throws Exception {
    super.setUp();
    // reset the component counter, so that we have always the same IDs
    resetWebComponentCounter();
    // needed for renderer loading
    Fixture.fakeBrowser( new Default( true ) );
  }
  
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testRender() throws Exception {
    WebPanel wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout();
        return result;
      }
    } );
    doRenderTest( wpl, 0 );
    
    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_1 
          = ( WebTableCell )result.getArea( new Position( 1, 1 ) );
        resultWebGridLayoutArea_1_1.setColspan( "4" );
        WebTableCell resultWebGridLayoutArea_2_1 
          = ( WebTableCell )result.getArea( new Position( 2, 1 ) );
        resultWebGridLayoutArea_2_1.setRowspan( "2" );
        WebTableCell resultWebGridLayoutArea_3_2 
          = ( WebTableCell )result.getArea( new Position( 3, 2 ) );
        resultWebGridLayoutArea_3_2.setColspan( "3" );
        resultWebGridLayoutArea_3_2.setRowspan( "3" );
        return result;
      }
    } );
    doRenderTest( wpl, 1 );
    
    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_2 
          = ( WebTableCell )result.getArea( new Position( 1, 2 ) );
        resultWebGridLayoutArea_1_2.setRowspan( "3" );
        WebTableCell resultWebGridLayoutArea_1_3 
          = ( WebTableCell )result.getArea( new Position( 1, 3 ) );
        resultWebGridLayoutArea_1_3.setRowspan( "4" );
        WebTableCell resultWebGridLayoutArea_1_4 
          = ( WebTableCell )result.getArea( new Position( 1, 4 ) );
        resultWebGridLayoutArea_1_4.setRowspan( "5" );
        return result;
      }
    } );
    doRenderTest( wpl, 2 );

    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_1 
          = ( WebTableCell )result.getArea( new Position( 1, 1 ) );
        resultWebGridLayoutArea_1_1.setColspan( "4" );
        WebTableCell resultWebGridLayoutArea_2_1 
          = ( WebTableCell )result.getArea( new Position( 2, 1 ) );
        resultWebGridLayoutArea_2_1.setColspan( "3" );
        WebTableCell resultWebGridLayoutArea_2_4 
          = ( WebTableCell )result.getArea( new Position( 2, 4 ) );
        resultWebGridLayoutArea_2_4.setRowspan( "4" );
        WebTableCell resultWebGridLayoutArea_3_1 
          = ( WebTableCell )result.getArea( new Position( 3, 1 ) );
        resultWebGridLayoutArea_3_1.setColspan( "2" );
        return result;
      }
    } );
    doRenderTest( wpl, 3 );

    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_1 
          = ( WebTableCell )result.getArea( new Position( 1, 1 ) );
        resultWebGridLayoutArea_1_1.setRowspan( "2" );
        WebTableCell resultWebGridLayoutArea_1_2 
          = ( WebTableCell )result.getArea( new Position( 1, 2 ) );
        resultWebGridLayoutArea_1_2.setRowspan( "3" );
        WebTableCell resultWebGridLayoutArea_1_3 
          = ( WebTableCell )result.getArea( new Position( 1, 3 ) );
        resultWebGridLayoutArea_1_3.setRowspan( "4" );
        WebTableCell resultWebGridLayoutArea_1_4 
          = ( WebTableCell )result.getArea( new Position( 1, 4 ) );
        resultWebGridLayoutArea_1_4.setRowspan( "5" );
        return result;
      }
    } );
    doRenderTest( wpl, 4 );
    
    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_1 
          = ( WebTableCell )result.getArea( new Position( 1, 1 ) );
        resultWebGridLayoutArea_1_1.setColspan( "4" );
        WebTableCell resultWebGridLayoutArea_2_1 
          = ( WebTableCell )result.getArea( new Position( 2, 1 ) );
        resultWebGridLayoutArea_2_1.setColspan( "3" );
        WebTableCell resultWebGridLayoutArea_2_4 
          = ( WebTableCell )result.getArea( new Position( 2, 4 ) );
        resultWebGridLayoutArea_2_4.setRowspan( "4" );
        WebTableCell resultWebGridLayoutArea_3_1 
          = ( WebTableCell )result.getArea( new Position( 3, 1 ) );
        resultWebGridLayoutArea_3_1.setColspan( "2" );
        WebTableCell resultWebGridLayoutArea_3_3 
          = ( WebTableCell )result.getArea( new Position( 3, 3 ) );
        resultWebGridLayoutArea_3_3.setRowspan( "3" );
        WebTableCell resultWebGridLayoutArea_4_2 
          = ( WebTableCell )result.getArea( new Position( 4, 2 ) );
        resultWebGridLayoutArea_4_2.setRowspan( "2" );
        return result;
      }
    } );
    doRenderTest( wpl, 5 );

    wpl = createControl( new GridLayoutBuilder() {
      public WebGridLayout build() {
        WebGridLayout result = new WebGridLayout( 5, 4 );
        WebTableCell resultWebGridLayoutArea_1_1 
          = ( WebTableCell )result.getArea( new Position( 1, 1 ) );
        resultWebGridLayoutArea_1_1.setColspan( "4" );
        WebTableCell resultWebGridLayoutArea_2_1 
          = ( WebTableCell )result.getArea( new Position( 2, 1 ) );
        resultWebGridLayoutArea_2_1.setRowspan( "3" );
        WebTableCell resultWebGridLayoutArea_3_1
          = ( WebTableCell )result.getArea( new Position( 3, 1 ) );
        resultWebGridLayoutArea_3_1.setRowspan( "" );
        WebTableCell resultWebGridLayoutArea_3_2
          = ( WebTableCell )result.getArea( new Position( 3, 2 ) );
        resultWebGridLayoutArea_3_2.setColspan( "3" );
        resultWebGridLayoutArea_3_2.setRowspan( "3" );
        return result;
      }
    } );
    doRenderTest( wpl, 6 );
    
  }

  private WebPanel createControl( final GridLayoutBuilder builder )
    throws Exception
  {
    WebForm testForm = new TestForm();
    WebGridLayout wgl = builder.build();
    WebPanel result = new WebPanel();
    result.setWebLayout( wgl );
    testForm.add( result, "CENTER" );
    return result;
  }
  
  ///////////////
  //inner classes
  
  
  private final class TestForm extends WebForm {
    protected void setWebComponents() throws Exception {
    }
  }
  
  private interface GridLayoutBuilder {
    WebGridLayout build();
  }
}
//$endOfPublicClass
class Render_6 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p14\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"3\" rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_5 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p12\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"2\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"2\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_4 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p10\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " rowspan=\"2\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"5\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_3 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p8\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"2\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_2 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p6\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    " rowspan=\"5\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_1 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p4\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"4\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " rowspan=\"2\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    " colspan=\"3\" rowspan=\"3\"",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_0 {

  private static String[] res = new String[] {
    "<table",
    " id=\"p2\"",
    "",
    " width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"",
    ">",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "<tr",
    "",
    ">",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "",
    "<td",
    "",
    "",
    ">",
    "&nbsp;",
    "</td>",
    "",
    "</tr>",
    "</table>"
  };

  static String[] getRes() {
    return res;
  }
}

class Render_6_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_5_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_4_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_3_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_2_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_1_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

class Render_0_css {

  private static String[] res = new String[] {
  };

  static String[] getRes() {
    return res;
  }
}

