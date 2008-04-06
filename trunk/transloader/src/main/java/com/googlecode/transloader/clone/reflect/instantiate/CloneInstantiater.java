package com.googlecode.transloader.clone.reflect.instantiate;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.ClassWrapper;

public abstract class CloneInstantiater {
    public static CloneInstantiater wrap(Object object, InstantiationStrategy instantiationStrategy) {
        return Assert.isNotNull(object).getClass().isArray() ? new Array(object) : (CloneInstantiater) new NormalObject(object, instantiationStrategy);
    }

    protected final Object original;

    public CloneInstantiater(Object original) {
         this.original = Assert.isNotNull(original);
    }

    public abstract Object instantiateShallowCloneUsing(ClassLoader targetClassLoader) throws Exception;

    private static class Array extends CloneInstantiater {
        Array(Object object) {
            super(Assert.isArray(object));
        }

        public Object instantiateShallowCloneUsing(ClassLoader targetClassLoader) throws Exception {
            Class originalComponentType = original.getClass().getComponentType();
            Class cloneComponentType = ClassWrapper.getClassFrom(targetClassLoader, originalComponentType.getName());
            return java.lang.reflect.Array.newInstance(cloneComponentType, java.lang.reflect.Array.getLength(original));
        }
    }

    private static class NormalObject extends CloneInstantiater {
        private final InstantiationStrategy instantiater;

        NormalObject(Object original, InstantiationStrategy instantiationStrategy) {
            super(original);
            Assert.isNotNull(instantiationStrategy);
            instantiater = instantiationStrategy;
        }

        public Object instantiateShallowCloneUsing(ClassLoader targetClassLoader) throws Exception {
            Class cloneClass = ClassWrapper.getClassFrom(targetClassLoader, original.getClass().getName());
            return instantiater.newInstanceOf(cloneClass);
        }
    }
}
