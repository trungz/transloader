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
    private final CloningStrategy fallback;

    /**
     * Constructs a new <code>ReflectionCloningStrategy</code> with its dependencies injected.
     *
     * @param decider      the strategy by which the decision to clone or not to clone a particular given
     *                     object is made
     * @param instantiater the strategy by which to instantiate normal objects (as opposed to arrays, for which
     *                     standard reflection is always adequate)
     * @param fallback     the <code>CloningStrategy</code> to fall back to when <code>this</code>
     *                     strategy fails
     */
    public ReflectionCloningStrategy(CloningDecisionStrategy decider, InstantiationStrategy instantiater, CloningStrategy fallback) {
        Assert.areNotNull(decider, instantiater, fallback);
        this.decider = decider;
        this.instantiater = instantiater;
        this.fallback = fallback;
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
        CloneMapper mapper = new StatefulMapper(original, targetClassLoader, decider, instantiater);
        CloneWeaver weaver = new StatefulWeaver(mapper);
        weaver.weaveTransformedReferences();
        return mapper.getTransformationOf(original);
    }
}
