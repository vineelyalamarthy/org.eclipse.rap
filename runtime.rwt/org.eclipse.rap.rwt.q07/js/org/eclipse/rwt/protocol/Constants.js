/*******************************************************************************
 * Copyright (c) 2010 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/

org.eclipse.rwt.protocol = { };

//org.eclipse.rwt.protocol.handler = {};

org.eclipse.rwt.protocol.widgetManager 
  = org.eclipse.swt.WidgetManager.getInstance();

org.eclipse.rwt.protocol.constants = {
  PAYLOAD_SYNCHRONIZE : 'synchronize',
  PAYLOAD_MULTI_SYNCHRONIZE : 'multiSynchronize',
  PAYLOAD_LISTEN : 'listen',
  PAYLOAD_FIRE_EVENT : 'fireEvent',
  PAYLOAD_CONSTRUCT : 'construct',
  PAYLOAD_DESTROY : 'destroy',
  PAYLOAD_EXECUTE : 'execute',
  PAYLOAD_EXECUTE_SCRIPT : 'executeScript',
  MESSAGE_META : 'meta',
  MESSAGE_DEVICE : 'device',
  MESSAGE_WIDGETS : 'widgets',
  WIDGETS_ID : 'widgetId',
  WIDGETS_TYPE : 'type',
  WIDGETS_PAYLOAD : 'payload',
  KEY_PARAMETER_LIST : 'parameter',
  KEY_METHODNAME : 'method',
  KEY_PARENT_ID : 'parent',
  KEY_WIDGET_TYPE : 'widgetType',
  KEY_WIDGET_STYLE : 'style',
  KEY_EVENT : 'event',
  KEY_SCRIPT_TYPE : 'scriptType',
  KEY_SCRIPT : 'script',
  KEY_WIDGETS : 'widgets',
  KEY_JAVA_SCRIPT : 'text/javascript',
  
  
  TYPE_SHELL : 'org.eclipse.swt.widgets.Shell',
  MESSAGE_PART_INDENT : '  '
};