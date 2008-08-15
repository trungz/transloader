package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.internal.InternalCloner;
import com.googlecode.transloader.except.Assert;

import java.util.Map;

/**
 * A <code>CloningStrategy</code> that uses Java Reflection as its mechanism. Can clone whole object graphs or just
 * necessary parts depending on how it is configured.
 *
 * @author Jeremy Wales
 */
public final class ReflectionCloningStrategy implements CloningStrategy {
    private final InternalCloner cloner;

    /**
     * Constructs a new <code>ReflectionCloningStrategy</code> with its dependencies injected.
     *
     * @param cloner encapsulates the overall reflective cloning algorithm
     */
    public ReflectionCloningStrategy(InternalCloner cloner) {
        Assert.isNotNull(cloner);
        this.cloner = cloner;
    }

    /**
     * {@inheritDoc}
     *
     * @return a completely or partially cloned object graph, depending on the <code>CloningDecisionStrategy</code>
     *         injected, with potentially the <code>original</code> itself being the top-level object in the graph
     *         returned if it was not cloned
     */
    public Object cloneObjectUsing(final ClassLoader targetLoader, final Object original) throws Exception {
        Assert.areNotNull(targetLoader, original);
        Map references = cloner.mapReferencesFrom(original);
        Map clones = cloner.mapClonesOf(references.keySet(), targetLoader);
        cloner.setClonesIn(references, clones);
        return clones.get(original);
    }
}
