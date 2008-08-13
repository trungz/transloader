package com.googlecode.transloader.reference;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.element.ElementReflecter;
import com.googlecode.transloader.reference.field.FieldSetter;
import com.googlecode.transloader.reference.field.NoSetter;
import com.googlecode.transloader.reference.field.FieldReflecter;

/**
 * @author jeremywales
 */
public class DefaultReflecter implements ReferenceReflecter {
    private final FieldSetter setter;

    public DefaultReflecter() {
        this(NoSetter.INSTANCE);
    }

    public DefaultReflecter(FieldSetter setter) {
        Assert.isNotNull(setter);
        this.setter = setter;
    }

    private AbstractReflecter reflecterFor(Object subject) {
        return subject.getClass().isArray() ?
                new ElementReflecter(subject) :
                (AbstractReflecter) new FieldReflecter(subject, setter);
    }

    public Reference[] reflectReferencesFrom(Object referer) throws IllegalAccessException {
        Assert.isNotNull(referer);
        return reflecterFor(referer).getAllReferences();
    }
}
