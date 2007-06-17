package com.googlecode.transloader;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.googlecode.transloader.clone.CloningStrategy;

/**
 * The wrapper appropriate for wrapping around all <code>Object</code>s referencing <code>Class</code>es from
 * potentially foreign <code>ClassLoader</code>s.
 * 
 * @author Jeremy Wales
 */
public final class ObjectWrapper {
	private final Object wrappedObject;
	private final CloningStrategy cloner;

	/**
	 * Constructs a new <code>ObjectWrapper</code> around the given object, which will use the given
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
	 * Indicates whether or not <code>null</code> is what is wrapped.
	 * 
	 * @return true if the wrapped "object" is actually <code>null</code>
	 */
	public boolean isNull() {
		return wrappedObject == null;
	}

	/**
	 * Provides direct access to the wrapped object.
	 * 
	 * @return the actual wrapped object without any wrapping
	 */
	public Object getUnwrappedSelf() {
		return wrappedObject;
	}

	/**
	 * Indicates whether or not the wrapped object is an instance of the type with the given name in the wrapped
	 * object's <code>ClassLoader</code>(s).
	 * 
	 * @param typeName the name of the type against which the wrapped object will be checked
	 * @return true if the wrapped object is an instance of the type with the given name in the wrapped object's
	 *         <code>ClassLoader</code>(s)
	 */
	public boolean isInstanceOf(String typeName) {
		return TransloaderFactory.DEFAULT.wrap(wrappedObject.getClass()).isAssignableTo(typeName);
	}

	/**
	 * Gets an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the given
	 * <code>ClassLoader</code>. Every object referenced in the object graph starting with the object returned will
	 * be able to be cast to its respective types in the given <code>ClassLoader</code>.
	 * <p>
	 * This implementation employs the <code>CloningStrategy</code> configured at construction. Note that using
	 * {@link CloningStrategy#MINIMAL}, the default strategy in {@link TransloaderFactory#DEFAULT}, will often effect
	 * some changes within the object graph that starts with the wrapped object itself, as opposed to producing a
	 * completely new, seperate graph. Using {@link CloningStrategy#MAXIMAL} instead prevents this, producing a purely
	 * seperate clone without any changes within the wrapped object graph, at the cost of potentially far greater
	 * cloning effort. An object graph altered by cloning with {@link CloningStrategy#MINIMAL} can of course be restored
	 * entirely for use with other objects of <code>Class</code>es from its original <code>ClassLoader</code>(s)
	 * by cloning it back with those original <code>ClassLoader</code>(s), but this is an extra coding step and
	 * somewhat reduces the effort saved by not using {@link CloningStrategy#MAXIMAL} in the first place.
	 * </p>
	 * 
	 * @param classLoader the <code>ClassLoader</code> to use in creating an equivalent of the wrapped object
	 * @return an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the
	 *         given <code>ClassLoader</code>
	 */
	public Object cloneWith(ClassLoader classLoader) {
		if (isNull()) return null;
		try {
			return cloner.cloneObjectUsingClassLoader(getUnwrappedSelf(), classLoader);
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
	public Object invoke(InvocationDescription description) {
		try {
			Class wrappedClass = getUnwrappedSelf().getClass();
			// TODO collect all ClassLoaders from the object graph into an abstraction like eg CollectedClassLoader
			ClassLoader wrappedClassLoader = wrappedClass.getClassLoader();
			Class[] parameterTypes = ClassWrapper.getClasses(description.getParameterTypeNames(), wrappedClassLoader);
			Object[] clonedParameters =
					(Object[]) cloner.cloneObjectUsingClassLoader(description.getParameters(), wrappedClassLoader);
			Method method = wrappedClass.getMethod(description.getMethodName(), parameterTypes);
			return method.invoke(getUnwrappedSelf(), clonedParameters);
		} catch (Exception e) {
			// TODO test this bit
			throw new TransloaderException(
					"Unable to invoke '" + description.getMethodName() + Arrays.asList(description.getParameterTypeNames()) + "' on '" + getUnwrappedSelf() + "'.",
					e);
		}
	}

	// TODO add makeCastableTo(Class interface)
}
