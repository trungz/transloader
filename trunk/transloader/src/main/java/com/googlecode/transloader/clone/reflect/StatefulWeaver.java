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
            transferTransformed(originalReferences[i], clone);
    }

    private void transferTransformed(Reference originalReference, Object clone) throws NoSuchFieldException {
        Object transformation = mapper.getTransformationOf(originalReference.getValue());
        ReferenceDescription description = originalReference.getDescription();
        if (shouldTransfer(transformation, description, clone))
            description.setValueIn(clone, transformation);
    }

    private boolean shouldTransfer(Object transformation, ReferenceDescription description, Object clone) throws NoSuchFieldException {
        boolean notTransient = !description.isTransient();
        boolean notAlreadyEqual = !transformation.equals(description.getValueFrom(clone));
        boolean notNull = transformation != Reference.NULL;
        return notTransient && notAlreadyEqual && notNull;
    }
}
