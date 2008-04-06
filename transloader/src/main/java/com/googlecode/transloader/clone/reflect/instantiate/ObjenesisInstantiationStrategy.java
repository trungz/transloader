package com.googlecode.transloader.clone.reflect.instantiate;

import com.googlecode.transloader.except.Assert;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * Uses {@link ObjenesisStd} to create new instances of <code>Class</code>es without invoking their constructors.
 *
 * @author Jeremy Wales
 */
public final class ObjenesisInstantiationStrategy implements InstantiationStrategy {
    private final Objenesis objenesis = new ObjenesisStd();

    /**
     * {@inheritDoc}
     */
    public Object newInstanceOf(Class type) throws Exception {
        Assert.isNotNull(type);
        return objenesis.newInstance(type);
    }
}
