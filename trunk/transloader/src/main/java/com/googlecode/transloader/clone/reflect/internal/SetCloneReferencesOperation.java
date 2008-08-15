package com.googlecode.transloader.clone.reflect.internal;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceDescription;

import java.util.Map;
import java.util.Iterator;

/**
 * @author jeremywales
 */
public class SetCloneReferencesOperation {
    private final Map references;
    private final Map clones;

    public SetCloneReferencesOperation(Map references, Map clones) throws NoSuchFieldException {
        Assert.areNotNull(references, clones);
        this.references = references;
        this.clones = clones;
        setClonesReferences();
    }

    public void setClonesReferences() throws NoSuchFieldException {
        Iterator originals = references.keySet().iterator();
        while (originals.hasNext())
            setReferencesForCloneOf(originals.next());
    }

    private void setReferencesForCloneOf(Object original) throws NoSuchFieldException {
        Object clone = getCloneOf(original);
        setCloneReferencesFrom(original, clone);
    }

    private void setCloneReferencesFrom(Object original, Object clone) throws NoSuchFieldException {
        Reference[] originalReferences = (Reference[]) references.get(original);
        for (int i = 0; i < originalReferences.length; i++)
            setCloneOf(originalReferences[i], clone);
    }

    private void setCloneOf(Reference originalReference, Object clone) throws NoSuchFieldException {
        Object cloneValue = getCloneOf(originalReference.getValue());
        ReferenceDescription description = originalReference.getDescription();
        if (shouldSet(cloneValue))
            description.setValueIn(clone, cloneValue);
    }

    private boolean shouldSet(Object transformation) throws NoSuchFieldException {
        return transformation != Reference.NULL;
    }

    private Object getCloneOf(Object original) {
        return clones.containsKey(original) ? clones.get(original) : original;
    }
}
