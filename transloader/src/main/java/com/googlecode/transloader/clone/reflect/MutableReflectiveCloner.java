package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.clone.reflect.instantiate.CloneInstantiater;
import com.googlecode.transloader.clone.reflect.instantiate.InstantiationStrategy;
import com.googlecode.transloader.clone.reflect.decide.CloningDecisionStrategy;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceReflecter;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Iterator;
import java.util.Map;

final class MutableReflectiveCloner {
    private final Object original;
    private final ClassLoader targetClassLoader;
    private final CloningDecisionStrategy decider;
    private final InstantiationStrategy instantiater;
    private final CloningStrategy fallbackCloner;

    private final Map transformationMap = new IdentityMap();
    private final Map referenceMap = new IdentityMap();

    MutableReflectiveCloner(Object originalToClone, ClassLoader targetClassLoaderForClone,
                            CloningDecisionStrategy cloningDecisionStrategy,
                            InstantiationStrategy instantiationStrategy,
                            CloningStrategy fallbackCloningStrategy) {
        Assert.areNotNull(originalToClone, targetClassLoaderForClone, cloningDecisionStrategy, instantiationStrategy, fallbackCloningStrategy);
        original = originalToClone;
        targetClassLoader = targetClassLoaderForClone;
        decider = cloningDecisionStrategy;
        instantiater = instantiationStrategy;
        fallbackCloner = fallbackCloningStrategy;
    }

    Object performCloning() throws Exception {
        mapTransformationAndReferencesFrom(original);
        setReferencesBetweenTransformations();
        return transformationMap.get(original);
    }

    private void mapTransformationAndReferencesFrom(Object original) throws Exception {
        mapTransformationFrom(original);
        Reference[] references = mapReferencesFrom(original);
        for (int i = 0; i < references.length; i++)
            if (!(references[i].getValue() == null ||
                    references[i].getDescription().isOfPrimitiveType() ||
                    transformationMap.containsKey(references[i].getValue())))
                mapTransformationAndReferencesFrom(references[i].getValue());
    }

    private void mapTransformationFrom(Object original) throws Exception {
        Object clone = original;
        if (decider.shouldCloneObjectItself(original, targetClassLoader))
            clone = CloneInstantiater.wrap(original, instantiater).instantiateShallowCloneUsing(targetClassLoader);
        transformationMap.put(original, clone);
    }

    private Reference[] mapReferencesFrom(Object original) throws IllegalAccessException {
        Reference[] references = ReferenceReflecter.wrap(original).getAllReferences();
        referenceMap.put(original, references);
        return references;
    }

    private void setReferencesBetweenTransformations() throws NoSuchFieldException, IllegalAccessException {
        Iterator iterator = transformationMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object original = iterator.next();
            Object clone = transformationMap.get(original);
            Reference[] references = (Reference[]) referenceMap.get(original);
            for (int i = 0; i < references.length; i++) {
                Object originalReferenceValue = references[i].getValue();
                Object cloneReferenceValue = transformationMap.get(originalReferenceValue);
                references[i].getDescription().setValueIn(clone, cloneReferenceValue == null ? originalReferenceValue : cloneReferenceValue);
            }
        }
    }
}