package com.googlecode.transloader.test.fixture;

import java.net.URL;
import java.net.URLClassLoader;

public final class IndependentClassLoader extends URLClassLoader {
	public static final ClassLoader INSTANCE = createNewInstance();

    public static ClassLoader createNewInstance() {
		return new IndependentClassLoader();
	}

    private IndependentClassLoader() {
		super(getAppClassLoaderUrls(), null);
	}

	private static URL[] getAppClassLoaderUrls() {
		URLClassLoader appClassLoader = (URLClassLoader) IndependentClassLoader.class.getClassLoader();
		return appClassLoader.getURLs();
	}
}
