package com.googlecode.transloader;

/**
 * A wrapper around an object from a potentially foreign <code>ClassLoader</code>. Effectively adds methods to the
 * object to enable its use in the current <code>ClassLoader</code>.
 * 
 * @author Jeremy Wales
 */
public interface TransloaderWrapper {

	/**
	 * @return true if the wrapped "object" is actually <code>null</code>.
	 */
	boolean isNull();

	/**
	 * @param typeName the name of the type against which the wrapped object will be checked.
	 * @return true if the wrapped object is an instance of the type with the given name in the wrapped object's
	 *         <code>ClassLoader</code>.
	 */
	boolean isInstanceOf(String typeName);

	/**
	 * @return the actual wrapped object without any wrapping.
	 */
	Object getUnwrappedSelf();

	/**
	 * Gets an equivalent of the wrapped object appropriate for the given <code>ClassLoader</code>. Every object
	 * referenced in the object graph that starts with the object returned will be able to be cast to its respective
	 * types in the given <code>ClassLoader</code>. Precisely how the equivalent is constructed or retrieved (and
	 * indeed whether it is constructed or retrieved) is entirely dependent on the implementation of the interface.
	 * 
	 * @param classLoader the <code>ClassLoader</code> to use in creating an equivalent of the wrapped object.
	 * @return an equivalent of the wrapped object for the given <code>ClassLoader</code>
	 */
	Object getEquivalentFrom(ClassLoader classLoader);

	/**
	 * Invokes on the wrapped object the method matching the given description. If the parameter types and/or parameters
	 * themselves need to be transferred to the wrapped object's <code>ClassLoader</code> then this will be taken care
	 * of.
	 * 
	 * @param description the description of the method to invoke and the parameters to invoke with.
	 * @return the unwrapped result of the invocation. Use of the result is likely to require further wrapping.
	 */
	Object invoke(InvocationDescription description);
}
