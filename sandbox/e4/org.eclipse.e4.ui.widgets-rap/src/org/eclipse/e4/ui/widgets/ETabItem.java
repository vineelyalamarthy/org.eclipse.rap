package org.eclipse.e4.ui.widgets;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class ETabItem extends CTabItem {

	public ETabItem(CTabFolder parent, int style) {
		super(parent, style);
	}

	public ETabItem(CTabFolder parent, int style, int index) {
		super(parent, style, index);
	}

	public ETabFolder getETabParent() {
		return (ETabFolder) getParent();
	}

}
