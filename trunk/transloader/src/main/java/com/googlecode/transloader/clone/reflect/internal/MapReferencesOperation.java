package com.googlecode.transloader.clone.reflect.internal;

import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceReflecter;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Map;

/**
 * @author jeremywales
 */
public final class MapReferencesOperation {
    private final ReferenceReflecter reflecter;
    private final Map references = new IdentityMap();

    public MapReferencesOperation(Object original, ReferenceReflecter reflecter) throws IllegalAccessException {
        Assert.areNotNull(original, reflecter);
        this.reflecter = reflecter;
        recursivelyMapAllReferencesFrom(original);
    }

    private void recursivelyMapAllReferencesFrom(Object original) throws IllegalAccessException {
        Reference[] references = mapReferencesFrom(original);
        for (int i = 0; i < references.length; i++)
            if (shouldMap(references[i]))
                recursivelyMapAllReferencesFrom(references[i].getValue());
    }

    private Reference[] mapReferencesFrom(Object original) throws IllegalAccessException {
        Reference[] refs = reflecter.reflectReferencesFrom(original);
        references.put(original, refs);
        return refs;
    }

    private boolean shouldMap(Reference reference) {
        boolean notPrimitive = !reference.getDescription().isOfPrimitiveType();
        boolean notAlreadyMapped = !references.containsKey(reference.getValue());
        return notPrimitive && notAlreadyMapped;
    }

    public Map getReferences() {
        return references;
    }
}
