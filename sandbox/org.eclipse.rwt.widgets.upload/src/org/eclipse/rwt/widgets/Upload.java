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
package org.eclipse.rwt.widgets;

import java.util.*;

import org.eclipse.swt.widgets.Composite;

/**
 * Widget representing an Upload box.
 * 
 * @author tjarodrigues
 */
public class Upload extends Composite {
    private final List uploadListeners;
    private final boolean showProgress;
    private String lastFileUploaded;
    private final String servlet;

    /**
     * Initializes the Upload.
     * 
     * @param parent Parent container.
     * @param style Widget style.
     * @param servlet The upload servlet name.
     * @param showProgress Indicates if the progress bar should be visible.
     */
    public Upload(final Composite parent, final int style, final String servlet, final boolean showProgress) {
        super(parent, style);
        this.servlet = ((servlet == null) ? "/upload" : servlet);
        this.showProgress = showProgress;
        this.lastFileUploaded = "";
        this.uploadListeners = new ArrayList();
    }

    /**
     * Gets the servlet.
     * 
     * @return Servlet name.
     */
    public final String getServlet() {
        return this.servlet;
    }

    /**
     * Indicates if the Progress Bar should be visible.
     * 
     * @return <code>True</code> if the Progress Bar is visible, <code>False</code> otherwise.
     */
    public final boolean isProgressVisible() {
        return this.showProgress;
    }

    /**
     * Gets the client path of the last uploaded file.
     * 
     * @return The client path of the last uploaded file.
     */
    public final String getLastFileUploaded() {
        return this.lastFileUploaded;
    }

    /**
     * Sets the client path of the last uploaded file.
     * 
     * @param lastFileUploaded The path to the last uploaded file.
     */
    public final void setLastFileUploaded(final String lastFileUploaded) {
        this.lastFileUploaded = lastFileUploaded;
    }

    /**
     * Adds a new Listener to the Upload.
     * 
     * @param uploadAdapter The new listener.
     */
    public final synchronized void addUploadListener(final UploadAdapter uploadAdapter) {
        this.uploadListeners.add(uploadAdapter);
    }

    /**
     * Removes a Listener from the Upload.
     * 
     * @param uploadAdapter The new listener.
     */
    public final synchronized void removeUploadListener(final UploadAdapter uploadAdapter) {
        this.uploadListeners.remove(uploadAdapter);
    }

    /**
     * Fires a new Upload Finished Event.
     * 
     * @param uploadEvent The Upload Event to be fired.
     */
    public final synchronized void fireUploadEvent(final UploadEvent uploadEvent) {
        final Iterator listeners = uploadListeners.iterator();
        while (listeners.hasNext()) {
            ((UploadAdapter) listeners.next()).uploadFinished(uploadEvent);
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void dispose() {
        final Iterator listeners = uploadListeners.iterator();
        while (listeners.hasNext()) {
            this.uploadListeners.remove(listeners.next());
        }
        super.dispose();
    }
}
