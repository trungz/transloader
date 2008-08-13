package com.googlecode.transloader.reference.field;

import java.lang.reflect.Field;

/**
 * @author jeremywales
 */
public interface FieldSetter {
    void set(Object value, Field field, Object referer) throws Exception;
}
