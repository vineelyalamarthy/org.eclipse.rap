///*******************************************************************************
// * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     Innoopract Informationssysteme GmbH - initial API and implementation
// ******************************************************************************/
//package com.w4t.javascript;
//
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.ContextFactory;
//
//
//public class TestContextFactory extends ContextFactory {
//  
//  protected boolean hasFeature( final Context context, final int feature ) {
//    boolean result;
//    switch( feature ) {
//      case Context.FEATURE_STRICT_VARS:
//        result = false;
//        break;
//      case Context.FEATURE_STRICT_EVAL:
//        result = true;
//        break;
//      case Context.FEATURE_DYNAMIC_SCOPE:
//        result = true;
//        break;
//      default:
//        result = super.hasFeature( context, feature );
//        break;
//    }
//    return result;
//  }
//}
