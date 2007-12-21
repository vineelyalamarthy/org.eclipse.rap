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
package org.eclipse.rwt.widgets.internal.uploadkit;

import java.io.IOException;

import org.eclipse.rwt.lifecycle.*;
import org.eclipse.rwt.widgets.Upload;
import org.eclipse.rwt.widgets.UploadEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Class that interfaces between the Java and the JavaScript.
 *
 * @author tjarodrigues
 */
public class UploadLCA extends AbstractWidgetLCA {
    private static final String JS_PROP_LASTFILEUPLOADED = "lastFileUploaded";
    private static final String PROP_LASTFILEUPLOADED = "lastFileUploaded";

    /**
     * Preserves the property values between the Java and the JavaScript.
     *
     * @param widget The <code>Widget</code>.
     */
    public final void preserveValues(final Widget widget) {
        ControlLCAUtil.preserveValues((Control) widget);
        final IWidgetAdapter adapter = WidgetUtil.getAdapter(widget);
        adapter.preserve(PROP_LASTFILEUPLOADED, ((Upload) widget).getLastFileUploaded());
    }

    /**
     * Reads data from the <code>Widget</code>.
     *
     * @param widget The <code>Widget</code>.
     */
    public final void readData(final Widget widget) {
        final Upload u = (Upload) widget;
        final String lastFileUploaded = WidgetLCAUtil.readPropertyValue(u, "lastFileUploaded");
        u.setLastFileUploaded(lastFileUploaded);
        final String finished = WidgetLCAUtil.readPropertyValue(u, "finished");
        final String uploadParcial = WidgetLCAUtil.readPropertyValue(u, "uploadParcial");
        final String uploadTotal = WidgetLCAUtil.readPropertyValue(u, "uploadTotal");
        if ((finished != null) || (uploadParcial != null) || (uploadTotal != null)) {
            // allows the changes to be visible on the client side
            ProcessActionRunner.add( new Runnable() {
                public void run() {
                    u.fireUploadEvent(new UploadEvent(Boolean.valueOf(finished).booleanValue(), Integer.parseInt(uploadParcial), Integer
                            .parseInt(uploadTotal)));
                }
            } );

        }
    }

    /**
     * Creates the initial <code>Widget</code> rendering.
     *
     * @param widget The <code>Widget</code>.
     * @throws IOException If the <code>Widget</code> JavaScript is not found.
     */
    public final void renderInitialization(final Widget widget) throws IOException {
        final JSWriter writer = JSWriter.getWriterFor(widget);
        final Upload u = ((Upload) widget);
        writer.newWidget("org.eclipse.rwt.widgets.Upload", new Object[]{ u.getServlet(),
                Boolean.valueOf(u.isProgressVisible()) });
        writer.set("appearance", "composite");
        writer.set("overflow", "hidden");
        ControlLCAUtil.writeStyleFlags((Upload) widget);
    }

    /**
     * Renders the <code>Widget</code> changes in the JavaScript.
     *
     * @param widget The <code>Widget</code>.
     * @throws IOException If the <code>Widget</code> JavaScript is not found.
     */
    public final void renderChanges(final Widget widget) throws IOException {
        final Upload u = (Upload) widget;
        ControlLCAUtil.writeChanges(u);
        final JSWriter writer = JSWriter.getWriterFor(widget);
        writer.set(PROP_LASTFILEUPLOADED, JS_PROP_LASTFILEUPLOADED, u.getLastFileUploaded());
    }

    /**
     * Renders the <code>Widget</code> dispose in the JavaScript.
     *
     * @param widget The <code>Widget</code>.
     * @throws IOException If the <code>Widget</code> JavaScript is not found.
     */
    public final void renderDispose(final Widget widget) throws IOException {
        final JSWriter writer = JSWriter.getWriterFor(widget);
        writer.dispose();
    }

    /**
     * Resets the handler calls.
     *
     * @param typePoolId The Pool ID.
     * @throws IOException If can't reset the style flags.
     */
    public final void createResetHandlerCalls(final String typePoolId) throws IOException {
        ControlLCAUtil.resetChanges();
        ControlLCAUtil.resetStyleFlags();
    }

    /**
     * Returns the <code>Widget</code> Pool ID.
     *
     * @param widget The <code>Widget</code>.
     * @return <code>Widget</code> class name.
     */
    public final String getTypePoolId(final Widget widget) {
        return UploadLCA.class.getName();
    }
}
