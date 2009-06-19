package org.eclipse.nebula.widgets.gallery;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class Gallery extends ScrolledComposite {

	private List listeners = new ArrayList();
	private AbstractGalleryItemRenderer ir;
	List items = new ArrayList();
	Composite contentArea;
	String lastSelection;

	public Gallery(Composite parent, int style) {
		super(parent, style | SWT.V_SCROLL);
		setLayout(new FillLayout());
		
		contentArea = new Composite(this, SWT.NONE);
		Layout layout = new GridLayout(4, true);
		contentArea.setLayout(layout );
		setContent(contentArea);
		setExpandHorizontal(true);
		setExpandVertical(true);
	    contentArea.addControlListener(new org.eclipse.swt.events.ControlAdapter() {
	      public void controlResized(ControlEvent e) {
	        Rectangle r = getClientArea();
	        setMinSize(contentArea.computeSize(r.width, SWT.DEFAULT));
	      }
	    });

	}

	public void setGroupRenderer(AbstractGalleryGroupRenderer renderer) {
	}

	public void addSelectionListener(SelectionAdapter selectionAdapter) {
		listeners.add(selectionAdapter);
	}
	
	public void notifyListeners(GalleryItem item) {
		lastSelection = item.getText();
		for (int i = 0; i < items.size(); i++) {
			GalleryItem currentItem = (GalleryItem) items.get(i);
			currentItem.setSelected(false);
			
		}
		item.setSelected(true);
		for (int i = 0; i < listeners.size(); i++) {
			SelectionAdapter listener = (SelectionAdapter) listeners.get(i);
			SelectionEvent event = new SelectionEvent(getParent(), item, SWT.Selection);
			listener.widgetSelected(event);
		}
	}

	public void removeAll() {
		for (int i = 0; i < items.size(); i++) {
			GalleryItem currentItem = (GalleryItem) items.get(i);
			currentItem.dispose();
			items.remove(i);
		}
	}
	
	public void setItemRenderer(AbstractGalleryItemRenderer ir) {
		this.ir = ir;
	}

	public GalleryItem getItem(int index) {
		return null;
	}

	public int getItemCount() {
		return getChildren().length;
	}

	public AbstractGalleryItemRenderer getItemRenderer() {
		return ir;
	}

	public void addChild(GalleryItem galleryItem) {
		items.add(galleryItem);
	}

}
