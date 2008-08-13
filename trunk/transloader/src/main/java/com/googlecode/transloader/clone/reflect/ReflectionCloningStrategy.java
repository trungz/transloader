package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.CloneInstantiater;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.ReferenceReflecter;

/**
 * A <code>CloningStrategy</code> that uses Java Reflection as its mechanism. Can clone whole object graphs or just
 * necessary parts depending on how it is configured.
 *
 * @author Jeremy Wales
 */
public final class ReflectionCloningStrategy implements CloningStrategy {
    private final CloningDecisionStrategy decider;
    private final CloneInstantiater instantiater;
    private final ReferenceReflecter reflecter;

    /**
     * Constructs a new <code>ReflectionCloningStrategy</code> with its dependencies injected.
     *
     * @param decider      the strategy by which the decision to clone or not to clone a particular given
     *                     object is made
     * @param instantiater the strategy by which to instantiate shallow clones
     * @param reflecter    the stategy by which {@link com.googlecode.transloader.reference.AbstractReflecter}s are
     *                     ae created
     */
    public ReflectionCloningStrategy(CloningDecisionStrategy decider, CloneInstantiater instantiater, ReferenceReflecter reflecter) {
        Assert.areNotNull(decider, instantiater, reflecter);
        this.decider = decider;
        this.instantiater = instantiater;
        this.reflecter = reflecter;
    }

    /**
     * {@inheritDoc}
     *
     * @return a completely or partially cloned object graph, depending on the <code>CloningDecisionStrategy</code>
     *         injected in
     *         {@link #ReflectionCloningStrategy(CloningDecisionStrategy, CloneInstantiater, com.googlecode.transloader.reference.ReferenceReflecter)},
     *         with potentially the <code>original</code> itself being the top-level object in the graph returned if
     *         it was not cloned
     */
    public Object cloneObjectUsing(final ClassLoader targetClassLoader, final Object original) throws Exception {
        Assert.areNotNull(targetClassLoader, original);
        CloneMapper mapper = new StatefulMapper(original, targetClassLoader, decider, instantiater, reflecter);
        CloneWeaver weaver = new StatefulWeaver(mapper);
        weaver.weaveTransformedReferences();
        return mapper.getTransformationOf(original);
    }
}
