/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.activities.ws;

import org.eclipse.osgi.util.NLS;
import org.eclipse.rwt.RWT;


/**
 * The ActivtyMessages are the messages used by the activities
 * support.
 *
 */
public class ActivityMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.activities.ws.messages";//$NON-NLS-1$

//	public  String ActivityEnabler_description;
//	public  String ActivityEnabler_activities;
//	public  String ActivityEnabler_categories;
//	public  String ActivityEnabler_selectAll;
//	public  String ActivityEnabler_deselectAll;
//	public  String ActivitiesPreferencePage_advancedDialogTitle;
//	public  String ActivitiesPreferencePage_advancedButton;
//	public  String ActivitiesPreferencePage_lockedMessage;
//	public  String ActivitiesPreferencePage_captionMessage;
//	public  String ActivitiesPreferencePage_requirements;
	public  String ManagerTask;
	public  String ManagerWindowSubTask;
	public  String ManagerViewsSubTask;
	public  String Perspective_showAll;
//	public  String activityPromptButton;
//	public  String activityPromptToolTip;

	// load message values from bundle file
	public static ActivityMessages get() {
		Class clazz = ActivityMessages.class;
		Object result = RWT.NLS.getISO8859_1Encoded( BUNDLE_NAME, clazz );
		return ( ActivityMessages )result;
	}

}
