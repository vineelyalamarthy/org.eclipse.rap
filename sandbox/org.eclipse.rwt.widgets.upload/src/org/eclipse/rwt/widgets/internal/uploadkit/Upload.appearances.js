/*******************************************************************************
 * Copyright (c) 2002-2007 Critical Software S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tiago Rodrigues (Critical Software S.A.) - initial implementation
 *     Joel Oliveira (Critical Software S.A.) - initial commit
 ******************************************************************************/
    /*
    ---------------------------------------------------------------------------
      UPLOAD
    ---------------------------------------------------------------------------
    */

    "upload-field" : {
      style : function( states ) {
        return {
          border : states.rwt_BORDER ? "text.BORDER.border" : "text.border",
          font : "widget.font",
          padding : [ 2, 3, 2, 3 ],
          textColor : states.disabled ? "widget.graytext" : "undefined",
          backgroundColor : "list.background"
        };
      }
    }
