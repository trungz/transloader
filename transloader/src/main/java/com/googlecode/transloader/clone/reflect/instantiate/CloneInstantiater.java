package com.googlecode.transloader.clone.reflect.instantiate;

public interface CloneInstantiater {
    Object instantiateShallowCloneOf(Object original, ClassLoader targetLoader) throws Exception;
}
