package com.bilgeadam.resources;

import java.util.ResourceBundle;

public class Resources {
	private static final String MYBUNDLE = Resources.class.getPackageName() + ".config";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(MYBUNDLE);
	
	public static String getConfig(String key) {
		return RESOURCE_BUNDLE.getString(key);
	}
	
}
