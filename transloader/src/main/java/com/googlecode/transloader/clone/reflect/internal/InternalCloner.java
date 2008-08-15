package com.googlecode.transloader.clone.reflect.internal;

import java.util.Map;
import java.util.Set;

/**
 * @author jeremywales
 */
public interface InternalCloner {
    Map mapReferencesFrom(Object original) throws IllegalAccessException;
    Map mapClonesOf(Set originals, ClassLoader targetLoader) throws Exception;
    void setClonesIn(Map references, Map clones) throws NoSuchFieldException;
}
