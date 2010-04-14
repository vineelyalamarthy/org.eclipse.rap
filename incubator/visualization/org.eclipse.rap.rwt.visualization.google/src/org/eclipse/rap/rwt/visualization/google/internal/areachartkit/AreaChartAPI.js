/*******************************************************************************
 * Copyright (c) 2009-2010 David Donahue and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     David Donahue - initial API, implementation and documentation
 *     Austin Riddle (Texas Center for Applied Technology) - 
 *        added fault tolerance for offline situations
 ******************************************************************************/
try {
	google.load('visualization', '1', {'packages':['areachart']});
}
catch (e) {
	var mesg = "Error loading Google Area Chart API: "+e;
	if (console) {
		console.log(mesg);
	}
	else {
		alert(mesg);
	}
}