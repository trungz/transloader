package com.googlecode.transloader;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.googlecode.transloader.clone.CloningStrategy;

/**
 * The TransloaderWrapper appropriate for wrapping around all <code>Object</code>s.
 * 
 * @author Jeremy Wales
 */
public class ObjectWrapper implements TransloaderWrapper {
	private final Object wrappedObject;
	private final CloningStrategy cloner;

	/**
	 * Constructs a new <code>ObjectWrapper</code> around the given object and which will use the given
	 * <code>CloningStrategy</code> when required.
	 * 
	 * @param objectToWrap the object to wrap
	 * @param cloningStrategy the strategy for cloning
	 */
	public ObjectWrapper(Object objectToWrap, CloningStrategy cloningStrategy) {
		wrappedObject = objectToWrap;
		cloner = cloningStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isNull() {
		return wrappedObject == null;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isInstanceOf(String typeName) {
		return new ClassWrapper(wrappedObject.getClass(), cloner).isAssignableTo(typeName);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object getUnwrappedSelf() {
		return wrappedObject;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation clones the wrapped object using <code>Class</code>es from the given
	 * <code>ClassLoader</code>. It makes use of the <code>CloningStrategy</code> injected at construction. Note
	 * that using {@link CloningStrategy#MINIMAL}, the default strategy in {@link DefaultTransloaderFactory}, will
	 * often effect some changes to the wrapped object itself, as opposed to producing a purely seperate clone. Using
	 * {@link CloningStrategy#MAXIMAL} instead prevents this, producing a purely seperate clone without any changes to
	 * the wrapped object, at the cost of potentially far greater cloning effort.
	 * </p>
	 */
	public Object getEquivalentFrom(ClassLoader classLoader) {
		if (isNull()) return null;
		try {
			return cloner.cloneObjectToClassLoader(getUnwrappedSelf(), classLoader);
		} catch (Exception e) {
			throw new TransloaderException("Unable to clone '" + getUnwrappedSelf() + "'.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Invokes on the wrapped object the method described by the given invocation description, with the parameters given
	 * by the same. Finds the method reflectively using parameter types loaded from the wrapped object's
	 * <code>ClassLoader</code>(s). Any parameters which refer to <code>Class</code>es that are foreign to the
	 * wrapped object's <code>ClassLoader</code>(s) are cloned using the <code>CloningStrategy</code> injected at
	 * construction.
	 * </p>
	 */
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
