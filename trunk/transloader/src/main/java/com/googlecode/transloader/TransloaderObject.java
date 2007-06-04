package com.googlecode.transloader;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.MaximalCloningDecisionStrategy;
import com.googlecode.transloader.clone.InstantiationPlusFieldsCloningStrategy;
import com.googlecode.transloader.clone.MinimalCloningDecisionStrategy;
import com.googlecode.transloader.clone.ObjenesisInstantiationStrategy;

public class TransloaderObject {
	public static final CloningStrategy MINIMAL_CLONER =
			new InstantiationPlusFieldsCloningStrategy(new MinimalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy());
	public static final CloningStrategy MAXIMAL_CLONER =
			new InstantiationPlusFieldsCloningStrategy(new MaximalCloningDecisionStrategy(),
					new ObjenesisInstantiationStrategy());

	private final CloningStrategy cloner;
	private final Object wrappedObject;

	public TransloaderObject(Object objectToWrap) {
		this(objectToWrap, MINIMAL_CLONER);
	}

	public TransloaderObject(Object objectToWrap, CloningStrategy cloningStrategy) {
		wrappedObject = objectToWrap;
		cloner = cloningStrategy;
	}

	public boolean isNull() {
		return wrappedObject == null;
	}

	public boolean isInstanceOf(String typeName) {
		return new TransloaderClass(wrappedObject.getClass()).isAssignableTo(typeName);
	}

	public Object cloneTo(ClassLoader classLoader) {
		if (isNull()) return null;
		try {
			return cloner.cloneObjectToClassLoader(wrappedObject, classLoader);
		} catch (Exception e) {
			throw new TransloadingException("Unable to clone '" + wrappedObject + "'.", e);
		}
	}

	public Object invoke(InvocationDescription description) {
		try {
			Class wrappedClass = wrappedObject.getClass();
			// TODO collect all the ClassLoaders from the object graph into an abstraction called CollectedClassLoader?
			ClassLoader wrappedClassLoader = wrappedClass.getClassLoader();
			Class[] parameterTypes =
					TransloaderClass.getClasses(description.getParameterTypeNames(), wrappedClassLoader);
			Object[] clonedParameters =
					(Object[]) cloner.cloneObjectToClassLoader(description.getParameters(), wrappedClassLoader);
			Method method = wrappedClass.getMethod(description.getMethodName(), parameterTypes);
			return method.invoke(wrappedObject, clonedParameters);
		} catch (Exception e) {
			// TODO test this bit
			throw new TransloadingException(
					"Unable to invoke '" + description.getMethodName() + Arrays.asList(description.getParameterTypeNames()) + "' on '" + wrappedObject + "'.",
					e);
		}
	}

	public Object getUntransloadedObject() {
		return wrappedObject;
	}
}
