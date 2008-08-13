package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.CloneInstantiater;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceReflecter;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Iterator;
import java.util.Map;

final class StatefulMapper implements CloneMapper {
    private final ClassLoader targetLoader;
    private final CloningDecisionStrategy decider;
    private final CloneInstantiater instantiater;

    private final Map transformationMap = new IdentityMap();
    private final Map referenceMap = new IdentityMap();
    private final ReferenceReflecter reflecter;

    StatefulMapper(
            Object original,
            ClassLoader targetLoader,
            CloningDecisionStrategy decider,
            CloneInstantiater instantiater,
            ReferenceReflecter reflecter
    ) throws Exception {
        Assert.areNotNull(original, targetLoader, decider, instantiater, reflecter);
        this.targetLoader = targetLoader;
        this.decider = decider;
        this.instantiater = instantiater;
        this.reflecter = reflecter;
        mapTransformationAndReferencesFrom(original);
    }

    private void mapTransformationAndReferencesFrom(Object original) throws Exception {
        mapTransformationFrom(original);
        mapReferencesFrom(original);
        Reference[] references = getReferencesFrom(original);
        for (int i = 0; i < references.length; i++)
            if (shouldMap(references[i]))
                mapTransformationAndReferencesFrom(references[i].getValue());
    }

    private void mapTransformationFrom(Object original) throws Exception {
        Object clone = original;
        if (shouldClone(original))
            clone = instantiater.instantiateShallowCloneOf(original, targetLoader);
        transformationMap.put(original, clone);
    }

    private void mapReferencesFrom(Object original) throws IllegalAccessException {
        Reference[] references = reflecter.reflectReferencesFrom(original);
        referenceMap.put(original, references);
    }

    private boolean shouldMap(Reference reference) {
        boolean notPrimitive = !reference.getDescription().isOfPrimitiveType();
        boolean notAlreadyMapped = !transformationMap.containsKey(reference.getValue());
        return notPrimitive && notAlreadyMapped;
    }

    private boolean shouldClone(Object original) throws ClassNotFoundException {
        boolean notNull = original != Reference.NULL;
        boolean deciderSaysSo = decider.shouldCloneObjectItself(original, targetLoader);
        return notNull && deciderSaysSo;
    }

    public Iterator iterateOriginals() {
        return transformationMap.keySet().iterator();
    }

    public Object getTransformationOf(Object original) {
        Object mapped = transformationMap.get(original);
        return mapped == null ? original : mapped;
    }

    public Reference[] getReferencesFrom(Object original) {
        return (Reference[]) referenceMap.get(original);
    }
}