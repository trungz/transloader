package com.googlecode.transloader.reference;

/**
 * @author jeremywales
 */
public interface ReferenceReflecter {
    Reference[] reflectReferencesFrom(Object referer) throws IllegalAccessException;
}
