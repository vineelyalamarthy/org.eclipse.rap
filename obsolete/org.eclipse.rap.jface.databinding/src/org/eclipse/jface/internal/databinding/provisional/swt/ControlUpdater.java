/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Samy Abou-Shama - initial adaptation for Eclipse RAP
 *******************************************************************************/
package org.eclipse.jface.internal.databinding.provisional.swt;

/**
 * NON-API - A ControlUpdater updates an SWT control in response to changes in the model.
 * By wrapping a block of code in a ControlUpdater, clients can rely on the fact
 * that the block of code will be re-executed whenever anything changes in the
 * model that might affect its behavior.
 *  
 * <p>
 * ControlUpdaters only execute when their controls are visible. If something changes
 * in the model while the control is invisible, the updator is flagged as dirty and
 * the updator stops listening to the model until the next time the control repaints.
 * This saves CPU cycles by deferring UI updates to widgets that are currently invisible.
 * </p>
 * 
 * <p>
 * Clients should subclass this when copying information from the model to
 * a control. Typical usage:
 * </p>
 * 
 * <ul>
 * <li>Override updateControl. It should do whatever is necessary to display
 *     the contents of the model in the control.</li>
 * <li>In the constructor, attach listeners to the model. The listeners should 
 *     call markDirty whenever anything changes in the model that affects 
 *     updateControl. Note: this step can be omitted when calling any method
 *     tagged with "@TrackedGetter" since ControlUpdater will automatically attach
 *     a listener to any object if a "@TrackedGetter" method is called in
 *     updateControl.</li>
 * <li>(optional)Extend dispose() to remove any listeners attached in the constructor</li>
 * </ul>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <code>
 * // Displays an observable value in a label and keeps the label in synch with changes
 * // in the value.
 * IReadableValue someValue = ...
 * final Label myLabel = new Label(parent, SWT.NONE);
 * new ControlUpdater(myLabel) {
 * 		protected void updateControl() {
 * 		   myLabel.setText(someValue.getValue().toString);
 *      }
 * }
 * // myLabel will display the value of someValue the next time it repaints, and will automatically
 * // be updated whenever someValue changes and the label is visible
 * </code>
 * 
 * <p>
 * Comments to RAP Implementation:
 * </p> 
 * Since RAP has no PaintEvent, the control gets updated in the PhaseListener, PhaseId.RENDER, beforePhase.
 * (this is still To be done)
 * @since 1.1
 */
public abstract class ControlUpdater {
		
	// TODO [sa] fix NullPointer Exception 
	
//	
//	
//	private class PrivateInterface implements PhaseListener, 
//		DisposeListener, Runnable, IChangeListener {
//		
//		private static final long serialVersionUID = 1L;
//		private Display display;
//
//
//		/**
//		 * @param display
//		 */
//		public PrivateInterface(Display display) {
//			super();
//			this.display = display;
//		}
//	
//		public void beforePhase( final PhaseEvent event ) {
//			if( display.equals(Display.getDefault()) ){
//				updateIfNecessary();		        	
//			}
//		}
//
//		public void afterPhase( final PhaseEvent event ) {
//			// not implemented
//		}
//  
//		public PhaseId getPhaseId() {
//			return PhaseId.RENDER;
//		}
//
//		// DisposeListener implementation
//		public void widgetDisposed(DisposeEvent e) {
//			ControlUpdater.this.dispose();
//		}
//		
//		// Runnable implementation. This method runs at most once per repaint whenever the
//		// value gets marked as dirty.
//		public void run() {
//			if (theControl != null && !theControl.isDisposed() && theControl.isVisible()) {
//				updateIfNecessary();
//			}
//		}
//		
//		// IChangeListener implementation (listening to the ComputedValue)
//		public void handleChange(ChangeEvent event) {
//			// Whenever this updator becomes dirty, schedule the run() method 
//			makeDirty();
//		}
//		
//	}
//	
//	private Runnable updateRunnable = new Runnable() {
//		public void run() {
//			updateControl();
//		}
//	};
//	
//	private PrivateInterface privateInterface;
//	private Control theControl;
//	private IObservable[] dependencies = new IObservable[0];
//	private boolean dirty = false;
//	
//	/**
//	 * Creates an updator for the given control.  
//	 * 
//	 * @param toUpdate control to update
//	 */
//	public ControlUpdater(Control toUpdate) {
//		theControl = toUpdate;
//		
//		privateInterface = new PrivateInterface(Display.getCurrent());
//		
//		theControl.addDisposeListener(privateInterface);
//		W4TContext.getLifeCycle().addPhaseListener(privateInterface);
//		
//		makeDirty();
//	}
//	
//	private void updateIfNecessary() {
//		if (dirty) {
//			dependencies = ObservableTracker.runAndMonitor(updateRunnable, privateInterface, null);
//			dirty = false;
//		}
//	}
//
//	/**
//	 * This is called automatically when the control is disposed. It may also
//	 * be called explicitly to remove this updator from the control. Subclasses
//	 * will normally extend this method to detach any listeners they attached
//	 * in their constructor.
//	 */
//	public void dispose() {
//		theControl.removeDisposeListener(privateInterface);
//		unboundPhaseListener();
//
//		stopListening();
//	}
//
//	private void unboundPhaseListener() {
//		W4TContext.getLifeCycle().removePhaseListener(privateInterface);
//		
//	}
//
//	private void stopListening() {
//		// Stop listening for dependency changes
//		for (int i = 0; i < dependencies.length; i++) {
//			IObservable observable = dependencies[i];
//				
//			observable.removeChangeListener(privateInterface);
//		}
//	}
//
//	/**
//	 * Updates the control. This method will be invoked once after the
//	 * updator is created, and once before any repaint during which the 
//	 * control is visible and dirty.
//	 *  
//	 * <p>
//	 * Subclasses should overload this method to provide any code that 
//	 * changes the appearance of the widget.
//	 * </p>
//	 */
//	protected abstract void updateControl();
//	
//	/**
//	 * Marks this updator as dirty. Causes the updateControl method to
//	 * be invoked before the next time the control is repainted.
//	 */
//	protected final void makeDirty() {
//		if (!dirty) {
//			dirty = true;
//			stopListening();
//			SWTUtil.runOnce(theControl.getDisplay(), privateInterface);
//		}
//	}
	
}
