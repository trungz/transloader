package com.googlecode.transloader.clone.reflect;

/**
 * @author jeremywales
 */
interface CloneWeaver {
    void weaveTransformedReferences() throws NoSuchFieldException;
}
