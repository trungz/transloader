package com.googlecode.transloader.clone.reflect.internal;

import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.CloneInstantiater;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.Reference;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * @author jeremywales
 */
public final class MapClonesOperation {
    private final ClassLoader targetLoader;
    private final CloningDecisionStrategy decider;
    private final CloneInstantiater instantiater;

    private final Map clones = new IdentityMap();

    MapClonesOperation(Set originals, ClassLoader targetLoader, CloningDecisionStrategy decider, CloneInstantiater instantiater) throws Exception {
        Assert.areNotNull(originals, targetLoader, decider, instantiater);
        this.targetLoader = targetLoader;
        this.decider = decider;
        this.instantiater = instantiater;
        mapClonesFrom(originals);
    }

    private void mapClonesFrom(Set originals) throws Exception {
        for (Iterator iterator = originals.iterator(); iterator.hasNext();)
            mapCloneFrom(iterator.next());
    }

    private void mapCloneFrom(Object original) throws Exception {
        Object clone = original;
        if (shouldClone(original))
            clone = instantiater.instantiateShallowCloneOf(original, targetLoader);
        clones.put(original, clone);
    }

    private boolean shouldClone(Object original) throws ClassNotFoundException {
        boolean notNull = original != Reference.NULL;
        boolean deciderSaysSo = decider.shouldCloneObjectItself(original, targetLoader);
        return notNull && deciderSaysSo;
    }

    public Map getClones() {
        return clones;
    }
}
