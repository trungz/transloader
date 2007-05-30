package com.googlecode.transloader.clone;

public interface InstantiationStrategy {
	Object newInstance(Class type) throws Exception;
}
