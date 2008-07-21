package com.googlecode.transloader.reference;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.except.ImpossibleException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public abstract class ReferenceReflecter {
    public static ReferenceReflecter wrap(Object object) {
        Class objectType = Assert.isNotNull(object).getClass();
        return objectType.isArray() ? new Array(object) : (ReferenceReflecter) new NormalObject(object);
    }

    protected Object object;

    protected ReferenceReflecter(Object object) {
        this.object = Assert.isNotNull(object);
    }

    protected abstract ReferenceDescription[] getAllReferenceDescriptions() throws IllegalAccessException;

    public final Reference[] getAllReferences() throws IllegalAccessException {
        ReferenceDescription[] descriptions = getAllReferenceDescriptions();
        List references = new ArrayList(descriptions.length);
        for (int i = 0; i < descriptions.length; i++)
            try {
                Object value = descriptions[i].getValueFrom(object);
                references.add(new Reference(descriptions[i], value == null ? Reference.NULL : value));
            } catch (NoSuchFieldException e) {
                throw new ImpossibleException(e);
            }
        return (Reference[]) references.toArray(new Reference[references.size()]);
    }

    private static final class Array extends ReferenceReflecter {
        Array(Object array) {
            super(Assert.isArray(array));
        }

        public ReferenceDescription[] getAllReferenceDescriptions() {
            ArrayElementDescription[] descriptions = new ArrayElementDescription[java.lang.reflect.Array.getLength(object)];
            for (int i = 0; i < descriptions.length; i++)
                descriptions[i] = new ArrayElementDescription(i, object.getClass().getComponentType().isPrimitive());
            return descriptions;
        }
    }

    private static final class NormalObject extends ReferenceReflecter {
        NormalObject(Object object) {
            super(object);
        }

        public ReferenceDescription[] getAllReferenceDescriptions() throws IllegalAccessException {
            return getAllInstanceFieldDescriptionsFor(object.getClass());
        }

        static FieldDescription[] getAllInstanceFieldDescriptionsFor(Class currentClass) {
            List descriptions = new ArrayList();
            while (currentClass != null) {
                descriptions.addAll(getInstanceFieldDescriptionsDirectlyFrom(currentClass));
                currentClass = currentClass.getSuperclass();
            }
            return (FieldDescription[]) descriptions.toArray(new FieldDescription[descriptions.size()]);
        }

        static List getInstanceFieldDescriptionsDirectlyFrom(Class currentClass) {
            Field[] fields = currentClass.getDeclaredFields();
            List descriptions = new ArrayList(fields.length);
            for (int i = 0; i < fields.length; i++)
                if (!Modifier.isStatic(fields[i].getModifiers())) descriptions.add(new FieldDescription(fields[i]));
            return descriptions;
        }
    }
}
