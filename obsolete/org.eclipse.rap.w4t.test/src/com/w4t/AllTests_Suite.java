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

import junit.framework.Test;
import junit.framework.TestSuite;

import com.w4t.ajax.*;
import com.w4t.custom.*;
import com.w4t.dhtml.*;
import com.w4t.dhtml.menubarkit.*;
import com.w4t.dhtml.treeviewkit.*;
import com.w4t.dhtml.webscrollpanekit.WebScrollPaneRenderer_Test;
import com.w4t.engine.FirstAccess_Test;
import com.w4t.engine.W4TModelUtil_Test;
import com.w4t.engine.lifecycle.*;
import com.w4t.engine.lifecycle.standard.LifeCycleAdapter_Test;
import com.w4t.engine.lifecycle.standard.LifeCycle_Standard_Test;
import com.w4t.engine.service.*;
import com.w4t.engine.util.*;
import com.w4t.internal.tablecell.BorderedSpacingHelper_Test;
import com.w4t.internal.tablecell.DefaultSpacingHelper_Test;
import com.w4t.javascript.JSSyntax_Test;
import com.w4t.markupembedderkit.MarkupEmbedderRenderer_Test;
import com.w4t.types.WebColor_Test;
import com.w4t.util.*;
import com.w4t.webanchorkit.WebAnchorRenderer_Test;
import com.w4t.webbordercomponentkit.WebBorderComponentRenderer_Test;
import com.w4t.webborderlayoutkit.WebBorderLayout_Test;
import com.w4t.webbuttonkit.WebButtonRenderer_Test;
import com.w4t.webcardlayoutkit.*;
import com.w4t.webcheckboxkit.WebCheckBoxRenderer_Test;
import com.w4t.webfileuploadkit.WebFileUploadRenderer_Test;
import com.w4t.webflowlayoutkit.WebFlowLayout_Test;
import com.w4t.webformkit.*;
import com.w4t.webgridlayoutkit.WebGridLayoutRenderer_Test;
import com.w4t.webimagekit.WebImageRenderer_Test;
import com.w4t.weblabelkit.WebLabelRenderer_Test;
import com.w4t.webpanelkit.WebPanelRenderer_Test;
import com.w4t.webradiobuttonkit.WebRadioButtonBehaviour_Test;
import com.w4t.webradiobuttonkit.WebRadioButtonRenderer_Test;
import com.w4t.webselectkit.WebSelectRenderer_Test;
import com.w4t.webtablecellkit.WebTableCellRenderer_Test;
import com.w4t.webtablerowkit.WebTableRowRenderer_Test;
import com.w4t.webtextareakit.WebTextAreaRenderer_Test;
import com.w4t.webtextkit.WebTextRenderer_Test;

/** <p>contains all test cases below org.eclipse.rap.</p>
  * 
  * <p>Note: this can be used from an IDE, but is not run from the
  * integration process, which runs every test ending with '_Test' separately.
  * </p>
  */
public class AllTests_Suite {

  public static Test suite() {
    TestSuite suite = new TestSuite( "Test for org.eclipse.rap.w4t" );
    //$JUnit-BEGIN$
    suite.addTestSuite( AjaxStatusUpdate_Test.class );
    suite.addTestSuite( TreeViewRenderer_Default_Script_Test.class );
    suite.addTestSuite( TreeViewRenderer_Ie5up_Ajax_Test.class );
    suite.addTestSuite( TreeViewRenderer_Ie5up_Script_Test.class );
    suite.addTestSuite( TreeViewRenderer_Noscript_Test.class );
    suite.addTestSuite( TreeViewRenderer_Ie5up_NoScript_Test.class );
    suite.addTestSuite( TreeViewRenderer_Konqueror3_2up_Script_Test.class );
    suite.addTestSuite( TreeViewRenderer_Mozilla1_6up_Script_Test.class );
    suite.addTestSuite( TreeViewRenderer_Mozilla1_6up_Ajax_Test.class );
    suite.addTestSuite( WebRadioButton_Test.class );
    suite.addTestSuite( WebCheckBox_Test.class );
    suite.addTestSuite( AbsoluteCell_Test.class );
    suite.addTestSuite( AbsoluteLayout_Test.class );
    suite.addTestSuite( AbsolutePositioner_Test.class );
    suite.addTestSuite( Menu_Test.class );
    suite.addTestSuite( SessionUnbound_Test.class );
    suite.addTestSuite( WebColor_Test.class );
    suite.addTestSuite( PropertyURI_Test.class );
    suite.addTestSuite( RenderUtil_Test.class );
    suite.addTestSuite( Style_Test.class );
    suite.addTestSuite( StyleManager_Test.class );
    suite.addTestSuite( WebComponent_Test.class );
    suite.addTestSuite( WebContainer_Test.class );
    suite.addTestSuite( WebFileUpload_Test.class );
    suite.addTestSuite( WebGridLayoutRenderer_Test.class );
    suite.addTestSuite( ComponentTreeVisitor_Test.class );
    suite.addTestSuite( LifeCycleAdapter_Test.class );
    suite.addTestSuite( RenderingTestCase_Test.class );
    suite.addTestSuite( HashCodeCalculator_Test.class );
    suite.addTestSuite( HashCodeBuilderFactory_Test.class );
    suite.addTestSuite( AjaxStatusAdapter_Test.class );
    suite.addTestSuite( DefaultHashCodeBuilder_Test.class );
    suite.addTestSuite( AjaxStatus_Test.class );
    suite.addTestSuite( WebFormUtil_Test.class );
    suite.addTestSuite( LifeCycleFactory_Test.class );
    suite.addTestSuite( LifeCycleStartupRequest_Test.class );
    suite.addTestSuite( LifeCycleStandardRequest_Test.class );
    suite.addTestSuite( LifeCycleFormDispatchRequest_Test.class );
    suite.addTestSuite( LifeCyclePopUpFormRequest_Test.class );
    suite.addTestSuite( LifeCycleRequestWithException_Test.class );
    suite.addTestSuite( LifeCycleRequestWithMessage_Test.class );
    suite.addTestSuite( FormCloseAndUnload_Test.class );
    suite.addTestSuite( W4TModelUtil_Test.class );
    suite.addTestSuite( FormManager_Test.class );
    suite.addTestSuite( WindowManager_Test.class );
    
    suite.addTestSuite( JSLibrary_Test.class );
    suite.addTestSuite( JSSyntax_Test.class );
    suite.addTestSuite( WebForm_Test.class );
    suite.addTestSuite( WebFormRenderer_Test.class );
    suite.addTestSuite( TriggerTimeStamp_Test.class );
    suite.addTestSuite( WebPanelRenderer_Test.class );
    suite.addTestSuite( WebLabelRenderer_Test.class );
    suite.addTestSuite( WebCheckBoxRenderer_Test.class );
    suite.addTestSuite( WebButtonRenderer_Test.class );
    suite.addTestSuite( WebSelectRenderer_Test.class );
    suite.addTestSuite( WebTextRenderer_Test.class );
    suite.addTestSuite( WebTextAreaRenderer_Test.class );
    suite.addTestSuite( WebImageRenderer_Test.class );
    suite.addTestSuite( WebRadioButtonRenderer_Test.class );
    suite.addTestSuite( WebRadioButtonBehaviour_Test.class );
    suite.addTestSuite( WebScrollPaneRenderer_Test.class );
    suite.addTestSuite( MarkupEmbedderRenderer_Test.class );
    suite.addTestSuite( ReadData_Test.class );
    suite.addTestSuite( ProcessAction_Test.class );
    suite.addTestSuite( WebDataEvent_Test.class );
    suite.addTestSuite( WebRenderListener_Test.class );
    suite.addTestSuite( ValidationListener_Test.class );
    suite.addTestSuite( WebFormListener_Test.class );
    suite.addTestSuite( Focus_Test.class );
    suite.addTestSuite( Decorator_Test.class );
    suite.addTestSuite( WebComponentRegistry_Test.class );
    suite.addTestSuite( LifeCycle_Standard_Test.class );
    suite.addTestSuite( WebTableCellRenderer_Test.class );
    suite.addTestSuite( DefaultSpacingHelper_Test.class );
    suite.addTestSuite( BorderedSpacingHelper_Test.class );
    suite.addTestSuite( WebTableRowRenderer_Test.class );
    suite.addTestSuite( WebAnchorRenderer_Test.class );
    suite.addTestSuite( WebFileUploadRenderer_Test.class );
    suite.addTestSuite( WebBorderLayout_Test.class );
    suite.addTestSuite( WebFlowLayout_Test.class );
    suite.addTestSuite( CItemList_Test.class );
    suite.addTestSuite( CMenu_Test.class );
    suite.addTestSuite( CToolbar_Test.class );
    suite.addTestSuite( LifeCycleServiceHandler_Test.class );
    suite.addTestSuite( FirstAccess_Test.class );
    suite.addTestSuite( ResourceServiceHandler_Test.class );
    suite.addTestSuite( TimestampServiceHandler_Test.class );
    
    suite.addTestSuite( WebBorderComponentRenderer_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Default_Noscript_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Default_Script_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Ie5up_Script_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Ie5up_Ajax_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Mozilla1_6up_Script_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Mozilla1_6up_Ajax_Test.class );
    suite.addTestSuite( WebCardLayoutRenderer_Opera8_Script_Test.class );
    
    suite.addTestSuite( MenuBarRenderer_Ie5up_Ajax_Test.class );
    suite.addTestSuite( MenuBarRenderer_Mozilla1_6up_Ajax_Test.class );
    suite.addTestSuite( MenuBarRenderer_Mozilla1_6up_Noscript_Test.class );
    suite.addTestSuite( MenuBarRenderer_Default_Noscript_Test.class );
    suite.addTestSuite( MenuBarRenderer_Default_Script_Test.class );
    
    //RendererCache for all browsers
    suite.addTestSuite( RendererCache_Test.class );
    suite.addTestSuite( RendererCache_Default_Test.class );
    suite.addTestSuite( RendererCache_Ie5up_Test.class );
    suite.addTestSuite( RendererCache_Ie5_5up_Test.class );
    suite.addTestSuite( RendererCache_Ie6_Test.class );
    suite.addTestSuite( RendererCache_Ie6up_Test.class );
    suite.addTestSuite( RendererCache_Konqueror3_1up_Test.class );
    suite.addTestSuite( RendererCache_Konqueror3_2up_Test.class );
    suite.addTestSuite( RendererCache_Mozilla1_6up_Test.class );
    suite.addTestSuite( RendererCache_Mozilla1_7up_Test.class );
    suite.addTestSuite( RendererCache_Opera8_Test.class );
    suite.addTestSuite( RendererCache_Safari2_Test.class );
   
    //$JUnit-END$
    return suite;
  }
}
