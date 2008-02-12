package com.googlecode.transloader.clone.reflect;

interface InnerCloner {
	Object instantiateCloneOf(Object original, ClassLoader targetClassLoader) throws Exception;

	void cloneObjectsReferencedBy(Object original, Object clone, ClassLoader targetClassLoader) throws Exception;
}
