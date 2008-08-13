package com.googlecode.transloader.test.unit;

import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.serializable.WithFinalFields;
import com.googlecode.transloader.reference.field.FieldSetter;
import com.googlecode.transloader.reference.field.SerializationSetter;

import java.lang.reflect.Field;

public class SerializationSetterTest extends BaseTestCase {
    WithFinalFields fixture = new WithFinalFields(Triangulate.anyInteger());
    FieldSetter subject;

    public void setUp() throws NoSuchMethodException, NoSuchFieldException {
        subject = new SerializationSetter();
    }

    public void testSetsFinalObjectField() throws Exception {
        checkChangeTo("string");
    }

    public void testSetsFinalPrimitiveField() throws Exception {
        checkChangeTo("intField");
    }

    private void checkChangeTo(String name) throws Exception {
        Field field = reflectField(name);
        Object pre = reflectValue(field);
        Object newValue = getValueDifferentFrom(pre, field.getType());
        subject.set(newValue, field, fixture);
        Object post = reflectValue(field);
        assertEquals(newValue, post);
    }

    private Object getValueDifferentFrom(Object original, Class type) {
        Object newValue = original;
        while (newValue.equals(original))
            newValue = Triangulate.anyInstanceOf(type);
        return newValue;
    }


    private Object reflectValue(Field field) throws IllegalAccessException {
        return field.get(fixture);
    }

    private Field reflectField(String name) throws NoSuchFieldException {
        Field field = fixture.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}