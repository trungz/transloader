package com.googlecode.transloader.clone;

interface ChildCloner {
	Object instantiateClone(Object original, ClassLoader targetClassLoader) throws Exception;

	void cloneContent(Object original, Object clone, ClassLoader targetClassLoader)
			throws Exception;
}
