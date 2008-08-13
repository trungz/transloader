package com.googlecode.transloader.reference;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.except.ImpossibleException;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractReflecter {
    protected final Object object;

    protected AbstractReflecter(Object object) {
        this.object = Assert.isNotNull(object);
    }

    protected abstract ReferenceDescription[] getAllReferenceDescriptions() throws IllegalAccessException;

    public final Reference[] getAllReferences() throws IllegalAccessException {
        ReferenceDescription[] descriptions = getAllReferenceDescriptions();
        List references = new ArrayList(descriptions.length);
        for (int i = 0; i < descriptions.length; i++)
            add(descriptions[i], references);
        return (Reference[]) references.toArray(new Reference[references.size()]);
    }

    private void add(ReferenceDescription description, List references) {
        Object value = getValueOf(description);
        Reference reference = new Reference(description, value);
        references.add(reference);
    }

    private Object getValueOf(ReferenceDescription description) {
        try {
            Object value = description.getValueFrom(object);
            return value == null ? Reference.NULL : value;
        } catch (NoSuchFieldException e) {
            throw new ImpossibleException(e);
        }
    }
}
