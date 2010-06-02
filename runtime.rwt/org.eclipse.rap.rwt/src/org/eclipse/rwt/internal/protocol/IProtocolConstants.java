/******************************************************************************* 
* Copyright (c) 2010 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*******************************************************************************/ 
package org.eclipse.rwt.internal.protocol;


public interface IProtocolConstants {

  public static final String MESSAGE_META = "meta";
  public static final String MESSAGE_DEVICE = "device";
  public static final String MESSAGE_WIDGETS = "widgets";
  
  public static final String META_SETTING_STORE = "settingStore";
  public static final String META_REQUEST_COUNTER = "requestCounter";

  public static final String DEVICE_ID = "id"; /*Formally known as UIRoot*/
  public static final String DEVICE_CURSOR_LOCATION = "cursorLocation";
  public static final String DEVICE_FOCUS_CONTROL = "focusControl";
  
  public static final String WIDGETS_ID = "widgetId";
  public static final String WIDGETS_TYPE = "type";
  public static final String WIDGETS_PAYLOAD = "payload";
  
  public static final String PAYLOAD_SYNCHRONIZE = "synchronize";
  public static final String PAYLOAD_MULTI_SYNCHRONIZE = "multiSynchronize";
  public static final String PAYLOAD_LISTEN = "listen";
  public static final String PAYLOAD_FIRE_EVENT = "fireEvent";
  public static final String PAYLOAD_CONSTRUCT = "construct";
  public static final String PAYLOAD_DESTROY = "destroy";    
  public static final String PAYLOAD_EXECUTE = "execute";
  public static final String PAYLOAD_EXECUTE_SCRIPT = "executeScript";
  
  public static final String KEY_PARAMETER_LIST = "parameter";
  public static final String KEY_METHODNAME = "method";
  public static final String KEY_PARENT_ID = "parent";
  public static final String KEY_WIDGET_TYPE = "widgetType";
  public static final String KEY_WIDGET_STYLE = "style";
  public static final String KEY_EVENT = "event";
  public static final String KEY_SCRIPT_TYPE = "scriptType";
  public static final String KEY_SCRIPT = "script";
  public static final String KEY_WIDGETS = "widgets";
  public static final String KEY_REQUEST_COUNTER = "requestCounter";  
}
