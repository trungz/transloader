package com.googlecode.transloader.test.function;

import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceReflecter;
import com.googlecode.transloader.test.CyclicReferenceSafeTraverser;
import com.googlecode.transloader.test.CyclicReferenceSafeTraverser.Operation;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;
import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import java.util.Map;

public class MaximalCloningTest extends CloningTestCase {
    private static CyclicReferenceSafeTraverser CYCLIC_REFERENCE_TRAVERSER = new CyclicReferenceSafeTraverser();

    public static Test suite() throws Exception {
        return new ActiveTestSuite(MaximalCloningTest.class);
    }

    protected Object assertDeeplyClonedToOtherClassLoader(NotCommonJavaType original) throws Exception {
        Object clone = super.assertDeeplyClonedToOtherClassLoader(original);
        assertDeeplyNotTheSame(original, clone);
        return clone;
    }

    private void assertDeeplyNotTheSame(final Object original, final Object clone) throws Exception {
        Operation notSameOperation = new Operation() {
            public Object performOn(Object currentObjectInGraph, Map referenceHistory) throws Exception {
                assertNotSame(original, clone);
                Reference[] references = ReferenceReflecter.wrap(original).getAllReferences();
                for (int i = 0; i < references.length; i++) {
                    if (!references[i].getDescription().isOfPrimitiveType()) {
                        Object originalValue = references[i].getValue();
                        if (originalValue != null)
                            assertDeeplyNotTheSame(originalValue, references[i].getDescription().getValueFrom(clone));
                    }
                }
                return null;
            }
        };
        CYCLIC_REFERENCE_TRAVERSER.performOperationWithoutLoopingOn(original, notSameOperation);
    }

    protected Transloader getTransloader() {
        return Transloader.DEFAULT;
    }
}
