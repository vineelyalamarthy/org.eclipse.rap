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
package org.eclipse.ui.internal.progress;

import org.eclipse.rwt.RWT;

public class ProgressMessages {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.progress.messages";//$NON-NLS-1$

	public String PendingUpdateAdapter_PendingLabel;
	public String JobInfo_DoneMessage;
	public String JobInfo_DoneNoProgressMessage;
	public String JobInfo_NoTaskNameDoneMessage;
	public String JobsViewPreferenceDialog_Note;
	public String JobErrorDialog_CustomJobText;
	public String JobInfo_UnknownProgress;
	public String JobInfo_Waiting;
	public String JobInfo_Sleeping;
	public String JobInfo_System;
	public String JobInfo_Cancelled;
	public String JobInfo_Error;
	public String JobInfo_Blocked;
	public String JobInfo_Finished;
	public String JobInfo_FinishedAt;
	public String JobErrorDialog_CloseDialogMessage;
	public String Error;
	public String DeferredTreeContentManager_NotDeferred;
	public String DeferredTreeContentManager_AddingChildren;
	public String DeferredTreeContentManager_FetchingName;
	public String ProgressView_CancelAction;
	public String ProgressView_ClearAllAction;
	public String ProgressView_NoOperations;
	
	public String NewProgressView_RemoveAllJobsToolTip;
	public String NewProgressView_CancelJobToolTip;
	public String NewProgressView_ClearJobToolTip;
	public String NewProgressView_errorDialogTitle;
	public String NewProgressView_errorDialogMessage;
	public String ProgressAnimationItem_tasks;
	public String ProgressAnimationItem_ok;
	public String ProgressAnimationItem_error;
	public String SubTaskInfo_UndefinedTaskName;
	public String DeferredTreeContentManager_ClearJob;
	public String ProgressContentProvider_UpdateProgressJob;
	public String JobErrorDialog_MultipleErrorsTitle;
	public String ProgressManager_openJobName;
	public String ProgressManager_showInDialogName;
	public String ProgressMonitorJobsDialog_DetailsTitle;
	public String ProgressMonitorJobsDialog_HideTitle;
	public String ErrorNotificationManager_OpenErrorDialogJob;
	public String AnimationManager_AnimationStart;
	public String ProgressFloatingWindow_EllipsisValue;
	public String BlockedJobsDialog_UserInterfaceTreeElement;
	public String BlockedJobsDialog_BlockedTitle;
	public String WorkbenchSiteProgressService_CursorJob;
	public String ProgressMonitorFocusJobDialog_UserDialogJob;
	public String ProgressMonitorFocusJobDialog_CLoseDialogJob;
	public String ProgressMonitorFocusJobDialog_RunInBackgroundButton;

	public String JobErrorDialog_MultipleErrorsMessage;
	public String JobErrorDialog_CloseDialogTitle;
	public String JobsViewPreferenceDialog_Title;
	public String JobErrorDialog_DoNotShowAgainMessage;

    public static ProgressMessages get() {
      Class clazz = ProgressMessages.class;
      Object result = RWT.NLS.getISO8859_1Encoded( BUNDLE_NAME, clazz );
      return ( ProgressMessages )result;
    }
}
