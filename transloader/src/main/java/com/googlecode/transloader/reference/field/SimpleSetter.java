package com.googlecode.transloader.reference.field;

import com.googlecode.transloader.except.Assert;

import java.lang.reflect.Field;

/**
 * @author jeremywales
 */
public final class SimpleSetter implements FieldSetter {
    public void set(Object value, Field field, Object referer) throws IllegalAccessException {
        Assert.areNotNull(value, field, referer);
        field.set(referer, value);
    }
}
