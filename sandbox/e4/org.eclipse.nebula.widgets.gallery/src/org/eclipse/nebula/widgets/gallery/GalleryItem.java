package org.eclipse.nebula.widgets.gallery;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.graphics.ResourceFactory;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

public class GalleryItem extends Item {

	public static final String REAL_ITEM = "realItem";
	private CLabel realItem;
	private Gallery parent;
	private List childs = new ArrayList();

	public GalleryItem(Gallery parent, int style) {
		super(parent, style);
		this.parent = parent;
		realItem = new CLabel(parent.contentArea, style | SWT.BORDER);
		realItem.addMouseListener(new MouseListener() {
		
			public void mouseUp(MouseEvent e) {
				Gallery gallery = (Gallery) getParent();
				gallery.notifyListeners(GalleryItem.this);
			}
		
			public void mouseDown(MouseEvent e) {
			}
		
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		this.parent.addChild(this);
		parent.contentArea.layout();
	}

	public GalleryItem(GalleryItem group, int style) {
		this(group.getParent(), style);
		group.childs.add(this);
	}
	
	public void dispose() {
		super.dispose();
		realItem.dispose();
		if(childs.size() > 0) {
			for (int i = 0; i < childs.size(); i++) {
				GalleryItem item = (GalleryItem) childs.get(i);
				item.dispose();
			}
		}
	}
	

	public void setExpanded(boolean b) {
	}
	
	public void setImage(Image image) {
		ImageData imageData = ResourceFactory.getImageData(image);
		ImageData imageData2 = imageData.scaledTo(32, 32);
		Image newImage = ResourceFactory.findImage(imageData2);
		imageData = null;
		realItem.setImage(newImage);
	}

	public void setText(String text) {
		realItem.setText(text);
		if(text.equals(parent.lastSelection)) {
			setSelected(true);
		}
	}
	
	public String getText() {
		return realItem.getText();	
	}
	
	public GalleryItem getItem(int index) {
		return parent.getItem(index);
	}

	public int getItemCount() {
		return getParent().getChildren().length;
	}
	
	public Gallery getParent() {
		return parent;
	}

	public Widget getParentItem() {
		return getParent();
	}

	public void setBackground(Color newColor) {
		realItem.setBackground(newColor);
	}

	public Color getBackground() {
		return null;
	}

	public void setForeground(Color newColor) {
	}

	public Color getForeground() {
		return null;
	}

	public void setSelected(boolean selected) {
		if(selected) {
			setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
		} else {
			setBackground(null);
		}
	}

}
