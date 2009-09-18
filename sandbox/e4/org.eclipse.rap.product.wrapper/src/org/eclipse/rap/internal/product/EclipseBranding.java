package org.eclipse.rap.internal.product;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.rap.internal.application.Activator;
import org.osgi.framework.Bundle;

// [bm] same intention as ProductExtensionBranding
public class EclipseBranding {
	private static final String ATTR_DESCRIPTION = "description"; //$NON-NLS-1$
	private static final String ATTR_NAME = "name"; //$NON-NLS-1$
	private static final String ATTR_APPLICATION = "application"; //$NON-NLS-1$
	private static final String ATTR_VALUE = "value"; //$NON-NLS-1$

	String application = null;
	String name = null;
	String id = null;
	String description = null;
	HashMap properties = new HashMap();
	Bundle definingBundle = null;

	public EclipseBranding(String id, IConfigurationElement element) {
		this.id = id;
		if (element == null)
			return;
		application = element.getAttribute(ATTR_APPLICATION);
		name = element.getAttribute(ATTR_NAME);
		description = element.getAttribute(ATTR_DESCRIPTION);
		loadProperties(element);
	}

	private void loadProperties(IConfigurationElement element) {
		IConfigurationElement[] children = element.getChildren();
		for (int i = 0; i < children.length; i++) {
			IConfigurationElement child = children[i];
			String key = child.getAttribute(ATTR_NAME);
			String value = child.getAttribute(ATTR_VALUE);
			if (key != null && value != null)
				properties.put(key, value);
		}
		definingBundle = Activator.getBundle(element.getContributor());
	}

	public Bundle getDefiningBundle() {
		return definingBundle;
	}

	public String getApplication() {
		return application;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	public Object getProduct() {
		return null;
	}
}