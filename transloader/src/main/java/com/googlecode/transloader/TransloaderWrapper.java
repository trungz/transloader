package com.googlecode.transloader;

/**
 * A wrapper around an object from a potentially foreign <code>ClassLoader</code>. Effectively adds methods to the
 * object to enable its use in the current <code>ClassLoader</code>.
 * 
 * @author Jeremy Wales
 */
public interface TransloaderWrapper {

	/**
	 * Indicates whether or not <code>null</code> is what is wrapped.
	 * 
	 * @return true if the wrapped "object" is actually <code>null</code>
	 */
	boolean isNull();

	/**
	 * Indicates whether or not the wrapped object is an instance of the type with the given name in the wrapped
	 * object's <code>ClassLoader</code>(s).
	 * 
	 * @param typeName the name of the type against which the wrapped object will be checked
	 * @return true if the wrapped object is an instance of the type with the given name in the wrapped object's
	 *         <code>ClassLoader</code>(s)
	 */
	boolean isInstanceOf(String typeName);

	/**
	 * Provides direct access to the wrapped object.
	 * 
	 * @return the actual wrapped object without any wrapping
	 */
	Object getUnwrappedSelf();

	/**
	 * Gets an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the given
	 * <code>ClassLoader</code>. Every object referenced in the object graph starting with the object returned will
	 * be able to be cast to its respective types in the given <code>ClassLoader</code>. Precisely how the equivalent
	 * is constructed or retrieved (and indeed whether it is newly constructed or an existing instance retrieved) is
	 * entirely dependent on the particular implementation of this interface in use.
	 * 
	 * @param classLoader the <code>ClassLoader</code> to use in creating an equivalent of the wrapped object
	 * @return an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the
	 *         given <code>ClassLoader</code>
	 */
	Object getEquivalentFrom(ClassLoader classLoader);

	/**
	 * Invokes on the wrapped object the method matching the given description. If the parameter types need to be loaded
	 * in - or the parameters themselves need to be cloned to - the wrapped object's <code>ClassLoader</code>, then
	 * this will be done.
	 * 
	 * @param invocationDescription the description of the method to invoke and the parameters to invoke it with
	 * @return the unwrapped result of the invocation (use of this result in the current <code>ClassLoader</code> is
	 *         likely to require further wrapping)
	 */
	Object invoke(InvocationDescription invocationDescription);
}
