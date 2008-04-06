package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.InstantiationStrategy;
import com.googlecode.transloader.except.Assert;

/**
 * A <code>CloningStrategy</code> that uses Java Reflection as its mechanism. Can clone whole object graphs or just
 * necessary parts depending on how it is configured.
 *
 * @author Jeremy Wales
 */
public final class ReflectionCloningStrategy implements CloningStrategy {
    private final CloningDecisionStrategy decider;
    private final InstantiationStrategy instantiater;
    private final CloningStrategy fallbackCloner;

    /**
     * Constructs a new <code>ReflectionCloningStrategy</code> with its dependencies injected.
     *
     * @param cloningDecisionStrategy the strategy by which the decision to clone or not to clone a particular given
     *                                object is made
     * @param instantiationStrategy   the strategy by which to instantiate normal objects (as opposed to arrays, for which
     *                                standard reflection is always adequate)
     * @param fallbackCloningStrategy the <code>CloningStrategy</code> to fall back to when <code>this</code>
     *                                strategy fails
     */
    public ReflectionCloningStrategy(CloningDecisionStrategy cloningDecisionStrategy,
                                     InstantiationStrategy instantiationStrategy, CloningStrategy fallbackCloningStrategy) {
        Assert.areNotNull(cloningDecisionStrategy, instantiationStrategy, fallbackCloningStrategy);
        decider = cloningDecisionStrategy;
        instantiater = instantiationStrategy;
        fallbackCloner = fallbackCloningStrategy;
    }

    /**
     * {@inheritDoc}
     *
     * @return a completely or partially cloned object graph, depending on the <code>CloningDecisionStrategy</code>
     *         injected in
     *         {@link #ReflectionCloningStrategy(CloningDecisionStrategy, InstantiationStrategy, CloningStrategy)},
     *         with potentially the <code>original</code> itself being the top-level object in the graph returned if
     *         it was not cloned
     */
    public Object cloneObjectUsing(final ClassLoader targetClassLoader, final Object original) throws Exception {
        Assert.areNotNull(targetClassLoader, original);
        return new MutableReflectiveCloner(original, targetClassLoader, decider, instantiater, fallbackCloner).performCloning();
    }
}
