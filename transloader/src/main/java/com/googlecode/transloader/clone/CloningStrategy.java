package com.googlecode.transloader.clone;


public interface CloningStrategy {
	Object cloneObjectToClassLoader(Object original, ClassLoader targetClassLoader) throws Exception;
}
