/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.forms.examples.views;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.events.*;
import org.eclipse.ui.forms.examples.internal.ExamplesPlugin;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class FormView extends ViewPart {
	
  private FormToolkit toolkit;
	private ScrolledForm form;

	/**
	 * This is a callback that will allow us to create the viewer and
	 * initialize it.
	 */
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Hello, Eclipse Forms");
		TableWrapLayout layout = new TableWrapLayout();
		form.getBody().setLayout(layout);
		
		Hyperlink link = toolkit.createHyperlink(form.getBody(), "Click here.",
				SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				System.out.println("Link activated!");
			}
		});
		link.setText("This is an example of a form that is much longer and will need to wrap.");
		layout.numColumns = 2;
		TableWrapData td = new TableWrapData();
		td.colspan = 2;
		link.setLayoutData(td);
		toolkit.createLabel(form.getBody(), "Text field label:");
		Text text = toolkit.createText(form.getBody(), "");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		text.setLayoutData(td);
		Button button = toolkit.createButton(form.getBody(),
				"An example of a checkbox in a form", SWT.CHECK);
		td = new TableWrapData();
		td.colspan = 2;
		button.setLayoutData(td);
		
		ImageHyperlink ih = toolkit.createImageHyperlink(form.getBody(), SWT.NULL);
		ih.setText("Image link with no image");
		ih = toolkit.createImageHyperlink(form.getBody(), SWT.NULL);
		ih.setImage(ExamplesPlugin.getDefault().getImageRegistry().get(ExamplesPlugin.IMG_SAMPLE));
		ih.setText("Link with image and text");
		
		ExpandableComposite ec = toolkit.createExpandableComposite(form.getBody(), ExpandableComposite.TREE_NODE|ExpandableComposite.CLIENT_INDENT);
		ImageHyperlink eci = toolkit.createImageHyperlink(ec, SWT.NULL);
		eci.setImage(ExamplesPlugin.getDefault().getImageRegistry().get(ExamplesPlugin.IMG_SAMPLE));
		ec.setTextClient(eci);
		ec.setText("Expandable Composite title");
		String ctext = "We will now create a somewhat long text so that "+
		"we can use it as content for the expandable composite. "+
		"Expandable composite is used to hide or show the text using the "+
		"toggle control";
		Label client = toolkit.createLabel(ec, ctext, SWT.WRAP);
		ec.setClient(client);
		td = new TableWrapData();
		td.colspan = 2;
		ec.setLayoutData(td);
		ec.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION|Section.TWISTIE|Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL);
		td.colspan = 2;
		section.setLayoutData(td);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Section title");
		toolkit.createCompositeSeparator(section);
		section.setDescription("This is the description that goes below the title");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		button = toolkit.createButton(sectionClient, "Radio 1", SWT.RADIO);
		button = toolkit.createButton(sectionClient, "Radio 2", SWT.RADIO);
		section.setClient(sectionClient);
	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		Control focusControl = form.getDisplay().getFocusControl();
		if (focusControl!=null) {
			Composite parent = focusControl.getParent();
			while (parent!=null) {
				if (parent==form) {
					// already have focus
					return;
				}
				parent = parent.getParent();
			}
		}
		form.setFocus();
	}
	
	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
}
