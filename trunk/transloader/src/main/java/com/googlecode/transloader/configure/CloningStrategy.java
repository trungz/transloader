package com.googlecode.transloader.configure;

import com.googlecode.transloader.clone.reflect.ReflectionCloningStrategy;
import com.googlecode.transloader.clone.reflect.decide.MaximalCloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.decide.MinimalCloningDecisionStrategy;

/**
 * @author jeremywales
 */
public final class CloningStrategy {
    /**
     * The implementation which clones as little as possible to make the given object graph use <code>Class</code>es
     * that are the same as those that would be loaded through the given <code>ClassLoader</code>. This is the most
     * efficient implementation to use simply for the purpose of making sure the returned object graph is perfectly
     * compatible with all other objects referencing <code>Class</code>es loaded through the given
     * <code>ClassLoader</code>.
     * <p>
     * However, the fact that it only clones what is necessary to make the object graph compatible with the given
     * <code>ClassLoader</code> means that usually <b>not all</b> of the objects in the graph will be cloned. This
     * means that, depending on the <code>Class</code>es in the object graph and which of these is the same if loaded
     * through the given <code>ClassLoader</code>, it is possible that a top level object is not cloned but objects
     * it references through fields are cloned. This effectively means that the existing object graph can be altered
     * rather than a new, purely seperate object graph being created. This <b>may not be what you want</b> if you want
     * to continue using the given object in its original context. In which case, use {@link #MAXIMAL}.
     * </p>
     *
     * @see com.googlecode.transloader.clone.reflect.ReflectionCloningStrategy
     * @see com.googlecode.transloader.clone.reflect.decide.MinimalCloningDecisionStrategy
     */
    public static final com.googlecode.transloader.clone.CloningStrategy MINIMAL = new ReflectionCloningStrategy(
            new MinimalCloningDecisionStrategy(),
            CloneInstantiater.DEFAULT,
            ReflecterFactory.DEFAULT
    );

    /**
     * The implementation which clones every <code>Object</code> in the given object graph. The given object graph is
     * not altered in any way and a completely new copy of the object graph referencing only <code>Class</code>es
     * loaded through the given <code>ClassLoader</code> is returned. Only primitives, which are not
     * <code>Object</code>s, are not cloned because they cannot be, as there is no concept of different references to
     * the same primitive value in Java. Similiar in behaviour to {@link com.googlecode.transloader.clone.SerializationCloningStrategy} except that it
     * can clone more object graphs because it does not rely on all referenced objects being
     * {@link java.io.Serializable} and also performs much faster than serialization.
     *
     * @see com.googlecode.transloader.clone.reflect.ReflectionCloningStrategy
     * @see com.googlecode.transloader.clone.reflect.decide.MaximalCloningDecisionStrategy
     */
    public static final com.googlecode.transloader.clone.CloningStrategy MAXIMAL = new ReflectionCloningStrategy(
            new MaximalCloningDecisionStrategy(),
            CloneInstantiater.DEFAULT, 
            ReflecterFactory.DEFAULT
    );

    private CloningStrategy() {}
}
