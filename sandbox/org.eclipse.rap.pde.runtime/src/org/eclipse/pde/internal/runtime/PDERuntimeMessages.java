/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.internal.runtime;

import org.eclipse.osgi.util.NLS;
import org.eclipse.rwt.RWT;

public class PDERuntimeMessages {
	private static final String BUNDLE_NAME = "org.eclipse.pde.internal.runtime.pderuntimeresources";//$NON-NLS-1$

	public String LogView_column_message;
	public String LogView_column_plugin;
	public String LogView_column_date;
	public String LogView_clear;
	public String LogView_clear_tooltip;
	public String LogView_copy;
	public String LogView_delete;
	public String LogView_delete_tooltip;
	public String LogView_export;
	public String LogView_exportLog;
	public String LogView_export_tooltip;
	public String LogView_import;
	public String LogView_import_tooltip;
	public String LogView_filter;
	public String LogView_readLog_reload;
	public String LogView_readLog_restore;
	public String LogView_readLog_restore_tooltip;
	public String LogView_severity_error;
	public String LogView_severity_warning;
	public String LogView_severity_info;
	public String LogView_severity_ok;
	public String LogView_confirmDelete_title;
	public String LogView_confirmDelete_message;
	public String LogView_confirmOverwrite_message;
	public String LogView_operation_importing;
	public String LogView_operation_reloading;
	public String LogView_activate;
	public String LogView_view_currentLog;
	public String LogView_view_currentLog_tooltip;
	public String LogView_properties_tooltip;

	public String LogView_FilterDialog_title;
	public String LogView_FilterDialog_eventTypes;
	public String LogView_FilterDialog_information;
	public String LogView_FilterDialog_warning;
	public String LogView_FilterDialog_error;
	public String LogView_FilterDialog_limitTo;
	public String LogView_FilterDialog_eventsLogged;
	public String LogView_FilterDialog_allSessions;
	public String LogView_FilterDialog_recentSession;

	public String LogViewLabelProvider_truncatedMessage;
	
	public String RegistryView_refresh_label;
	public String RegistryView_refresh_tooltip;
	public String RegistryView_collapseAll_label;
	public String RegistryView_collapseAll_tooltip;

	public String RegistryView_folders_imports;
	public String RegistryView_folders_libraries;
	public String RegistryView_folders_extensionPoints;
	public String RegistryView_folders_extensions;
	public String EventDetailsDialog_title;
	public String EventDetailsDialog_date;
	public String EventDetailsDialog_severity;
	public String EventDetailsDialog_message;
	public String EventDetailsDialog_exception;
	public String EventDetailsDialog_session;
	public String EventDetailsDialog_noStack;
	public String EventDetailsDialog_previous;
	public String EventDetailsDialog_next;
	public String EventDetailsDialog_copy;

	public String RegistryView_showRunning_label;

	public String RegistryView_titleSummary;
	public String OpenLogDialog_title;
	public String OpenLogDialog_message;
	public String OpenLogDialog_cannotDisplay;

	public static PDERuntimeMessages get(){
	  Class clazz = PDERuntimeMessages.class;
	  Object result = RWT.NLS.getISO8859_1Encoded( BUNDLE_NAME, clazz );
	    return ( PDERuntimeMessages )result;
	}
	public String RegistryBrowserLabelProvider_nameIdBind;
}
