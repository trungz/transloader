package com.googlecode.transloader.reference.element;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.AbstractReflecter;
import com.googlecode.transloader.reference.ReferenceDescription;

import java.lang.reflect.Array;

/**
 * @author jeremywales
*/
public final class ElementReflecter extends AbstractReflecter {
    public ElementReflecter(Object array) {
        super(Assert.isArray(array));
    }

    public ReferenceDescription[] getAllReferenceDescriptions() {
        ElementDescription[] descriptions = new ElementDescription[Array.getLength(object)];
        for (int i = 0; i < descriptions.length; i++)
            descriptions[i] = new ElementDescription(i, isPrimitive());
        return descriptions;
    }

    private boolean isPrimitive() {
        return object.getClass().getComponentType().isPrimitive();
    }
}
