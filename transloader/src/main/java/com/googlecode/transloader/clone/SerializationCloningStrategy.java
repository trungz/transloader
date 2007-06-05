package com.googlecode.transloader.clone;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;
import org.apache.commons.lang.SerializationUtils;

public final class SerializationCloningStrategy implements CloningStrategy {
	public Object cloneObjectToClassLoader(Object original, ClassLoader cloneClassLoader) throws Exception {
		byte[] serializedOriginal = SerializationUtils.serialize((Serializable) original);
		return new ClassLoaderObjectInputStream(cloneClassLoader, new ByteArrayInputStream(serializedOriginal)).readObject();
	}
}
