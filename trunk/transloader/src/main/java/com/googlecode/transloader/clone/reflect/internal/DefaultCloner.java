package com.googlecode.transloader.clone.reflect.internal;

import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.CloneInstantiater;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.ReferenceReflecter;

import java.util.Map;
import java.util.Set;

/**
 * @author jeremywales
 */
public class DefaultCloner implements InternalCloner {
    private final CloningDecisionStrategy decider;
    private final CloneInstantiater instantiater;
    private final ReferenceReflecter reflecter;

    /**
     * Constructs a new <code>InternalCloner</code> with its dependencies injected.
     *
     * @param decider      the strategy by which the decision to clone or not to clone a particular given object is made
     * @param instantiater the strategy by which to instantiate shallow clones
     * @param reflecter    the stategy by which {@link com.googlecode.transloader.reference.Reference}s are are created
     */
    public DefaultCloner(CloningDecisionStrategy decider, CloneInstantiater instantiater, ReferenceReflecter reflecter) {
        Assert.areNotNull(decider, instantiater, reflecter);
        this.decider = decider;
        this.instantiater = instantiater;
        this.reflecter = reflecter;
    }

    public Map mapReferencesFrom(Object original) throws IllegalAccessException {
        Assert.isNotNull(original);
        MapReferencesOperation operation = new MapReferencesOperation(original, reflecter);
        return operation.getReferences();
    }

    public Map mapClonesOf(Set originals, ClassLoader targetLoader) throws Exception {
        Assert.areNotNull(originals, targetLoader);
        MapClonesOperation operation = new MapClonesOperation(originals, targetLoader, decider, instantiater);
        return operation.getClones();
    }

    public void setClonesIn(Map references, Map clones) throws NoSuchFieldException {
        Assert.areNotNull(references, clones);
        new SetCloneReferencesOperation(references, clones);
    }
}
