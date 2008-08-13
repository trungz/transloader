package com.googlecode.transloader.reference.field;

import java.lang.reflect.Field;

/**
 * @author jeremywales
 */
public class NoSetter implements FieldSetter {
    public static final FieldSetter INSTANCE = new NoSetter();

    private NoSetter() {
    }

    public void set(Object value, Field field, Object referer) throws Exception {
        throw new UnsupportedOperationException();
    }
}
