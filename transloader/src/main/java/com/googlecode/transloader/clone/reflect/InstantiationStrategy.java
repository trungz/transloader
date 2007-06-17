package com.googlecode.transloader.clone.reflect;

public interface InstantiationStrategy {
	Object newInstance(Class type) throws Exception;
}
