package com.googlecode.transloader.clone.reflect.instantiate;

import com.googlecode.transloader.ClassWrapper;
import com.googlecode.transloader.except.Assert;

import java.lang.reflect.Array;

/**
 * @author jeremywales
 */
public class DefaultInstantiater implements CloneInstantiater {
    private final InstantiationStrategy strategy;

    public DefaultInstantiater(InstantiationStrategy strategy) {
        Assert.isNotNull(strategy);
        this.strategy = strategy;
    }

    public Object instantiateShallowCloneOf(Object original, ClassLoader targetLoader) throws Exception {
        Assert.areNotNull(original, targetLoader);
        return original.getClass().isArray() ?
                instantiateArray(original, targetLoader) :
                instantiateObject(original, targetLoader);
    }

    private Object instantiateArray(Object original, ClassLoader targetLoader) {
        Class componentType = original.getClass().getComponentType();
        Class cloneComponentType = ClassWrapper.getClassFrom(targetLoader, componentType.getName());
        int length = Array.getLength(original);
        return Array.newInstance(cloneComponentType, length);
    }

    private Object instantiateObject(Object original, ClassLoader targetLoader) throws Exception {
        String className = original.getClass().getName();
        Class cloneClass = ClassWrapper.getClassFrom(targetLoader, className);
        return strategy.newInstanceOf(cloneClass);
    }
}
