package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceDescription;
import com.googlecode.transloader.except.Assert;

import java.util.Iterator;

/**
 * @author Jeremy Wales
 */
class StatefulWeaver implements CloneWeaver {
    private final CloneMapper mapper;

    public StatefulWeaver(CloneMapper mapper) throws NoSuchFieldException {
        Assert.isNotNull(mapper);
        this.mapper = mapper;
    }

    public void weaveTransformedReferences() throws NoSuchFieldException {
        Iterator originals = mapper.iterateOriginals();
        while (originals.hasNext()) {
            Object original = originals.next();
            Object clone = mapper.getTransformationOf(original);
            transferTransformedReferencesFrom(original, clone);
        }
    }

    private void transferTransformedReferencesFrom(Object original, Object clone) throws NoSuchFieldException {
        Reference[] originalReferences = mapper.getReferencesFrom(original);
        for (int i = 0; i < originalReferences.length; i++)
            setTransformedReferenceIn(clone, originalReferences[i]);
    }

    private void setTransformedReferenceIn(Object clone, Reference originalReference) throws NoSuchFieldException {
        Object transformation = mapper.getTransformationOf(originalReference.getValue());
        ReferenceDescription description = originalReference.getDescription();
        if (shouldSetTransformedReferenceIn(clone, description, transformation))
            description.setValueIn(clone, transformation);
    }

    private boolean shouldSetTransformedReferenceIn(Object clone, ReferenceDescription description, Object transformation) throws NoSuchFieldException {
        boolean isNotTransient = !description.isTransient();
        boolean isNotAlreadyEqual = !transformation.equals(description.getValueFrom(clone));
        return isNotTransient && transformation != Reference.NULL && isNotAlreadyEqual;
    }
}
