package com.googlecode.transloader.clone;


/**
 * The API by which an object can be cloned using a different <code>ClassLoader</code> than that/those which loaded
 * the <code>Class</code>es it references. It can be used directly or serves as the strategy interface for
 * customising the behaviour of {@link com.googlecode.transloader.ObjectWrapper}s.
 *
 * @author Jeremy Wales
 */
public interface CloningStrategy {

    /**
     * Clones the given object using the given <code>ClassLoader</code>.
     *
     * @param targetClassLoader the <code>ClassLoader</code> by which to load <code>Class</code>es for clones
     * @param original          the original object to be cloned (can be <code>null</code>).
     * @return the result of cloning the object graph
     * @throws Exception can throw any <code>Exception</code> depending on the implementation
     */
	Object cloneObjectUsing(ClassLoader targetClassLoader, Object original) throws Exception;
}
