package com.googlecode.transloader.reference.field;

import com.googlecode.transloader.reference.AbstractReflecter;
import com.googlecode.transloader.reference.ReferenceDescription;
import com.googlecode.transloader.except.Assert;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author jeremywales
*/
public final class FieldReflecter extends AbstractReflecter {
    private final FieldSetter setter;

    public FieldReflecter(Object object, FieldSetter setter) {
        super(object);
        Assert.isNotNull(setter);
        this.setter = setter;
    }

    public ReferenceDescription[] getAllReferenceDescriptions() throws IllegalAccessException {
        return getAllInstanceFieldDescriptionsFor(object.getClass());
    }

    private FieldDescription[] getAllInstanceFieldDescriptionsFor(Class currentClass) {
        List descriptions = new ArrayList();
        while (currentClass != null) {
            List currentDescriptions = getDescriptionsDirectlyFrom(currentClass);
            descriptions.addAll(currentDescriptions);
            currentClass = currentClass.getSuperclass();
        }
        return (FieldDescription[]) descriptions.toArray(new FieldDescription[descriptions.size()]);
    }

    private List getDescriptionsDirectlyFrom(Class currentClass) {
        Field[] fields = currentClass.getDeclaredFields();
        List descriptions = new ArrayList(fields.length);
        for (int i = 0; i < fields.length; i++)
            if (isInstance(fields[i]))
                descriptions.add(new FieldDescription(fields[i], setter));
        return descriptions;
    }

    private boolean isInstance(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }
}
