/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.core.databinding.observable;

import com.w4t.SessionSingletonBase;

/**
 * Session Singleton used to link a Realm to one session.
 *
 * The default Realm can not be handled as a trhreadLocal in the WebServer context,
 * since the WebServer is handling requests on multiple threads 
 * The the ui in an RCP/Eclipse environment on the other hand, has a main UI thread. 
 * Thus we link the default thread as variable to the session.
 * This is done using this SessionRealm Session Singleton object 
 */
public class SessionRealm 
	extends SessionSingletonBase {
		   
		private SessionRealm() {}
		
		private Realm realm;
		  
		/**
		 * @return the singleton instance 
		 */
		public static SessionRealm getInstance() {
			return ( SessionRealm )getInstance( SessionRealm.class );
		}

		/**
		 * @return Returns the realm.
		 */
		public Realm getRealm() {
			return realm;
		}

		/**
		 * @param realm The realm to set.
		 */
		public void setRealm(Realm realm) {
			this.realm = realm;
		}
	
}
