package com.googlecode.transloader;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.googlecode.transloader.clone.CloningStrategy;

public class ObjectWrapper implements TransloaderWrapper {
	private Object wrappedObject;
	private CloningStrategy cloner;

	public ObjectWrapper(Object objectToWrap, CloningStrategy cloningStrategy) {
		wrappedObject = objectToWrap;
		cloner = cloningStrategy;
	}

	public final boolean isNull() {
		return wrappedObject == null;
	}

	public final boolean isInstanceOf(String typeName) {
		return new ClassWrapper(wrappedObject.getClass(), cloner).isAssignableTo(typeName);
	}

	public final Object getUnwrappedSelf() {
		return wrappedObject;
	}

	public Object getEquivalentFrom(ClassLoader classLoader) {
		if (isNull()) return null;
		try {
			return cloner.cloneObjectToClassLoader(getUnwrappedSelf(), classLoader);
		} catch (Exception e) {
			throw new TransloaderException("Unable to clone '" + getUnwrappedSelf() + "'.", e);
		}
	}

	public final Object invoke(InvocationDescription description) {
		try {
			Class wrappedClass = getUnwrappedSelf().getClass();
			// TODO collect all ClassLoaders from the object graph into an abstraction like eg CollectedClassLoader?
			ClassLoader wrappedClassLoader = wrappedClass.getClassLoader();
			Class[] parameterTypes = ClassWrapper.getClasses(description.getParameterTypeNames(), wrappedClassLoader);
			Object[] clonedParameters =
					(Object[]) cloner.cloneObjectToClassLoader(description.getParameters(), wrappedClassLoader);
			Method method = wrappedClass.getMethod(description.getMethodName(), parameterTypes);
			return method.invoke(getUnwrappedSelf(), clonedParameters);
		} catch (Exception e) {
			// TODO test this bit
			throw new TransloaderException(
					"Unable to invoke '" + description.getMethodName() + Arrays.asList(description.getParameterTypeNames()) + "' on '" + getUnwrappedSelf() + "'.",
					e);
		}
	}
}
