package com.googlecode.transloader.test.function;

import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.hierarchy.Bottom;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;
import com.googlecode.transloader.test.fixture.cyclic.SelfAndChildReferencingParent;
import com.googlecode.transloader.test.fixture.cyclic.SelfAndParentReferencingChild;
import com.googlecode.transloader.test.fixture.serializable.WithAnonymousClassFields;
import com.googlecode.transloader.test.fixture.fields.WithPrimitiveFields;
import com.googlecode.transloader.test.fixture.fields.*;
import com.googlecode.transloader.test.fixture.fields.WithStringField;

public abstract class CloningTestCase extends BaseTestCase {

	protected Object assertDeeplyClonedToOtherClassLoader(NotCommonJavaType original) throws Exception {
        String originalString = original.toString();
        Object clone = getTransloader().wrap(original).cloneWith(IndependentClassLoader.INSTANCE);
        assertNotSame(original, clone);
		assertEqualExceptForClassLoader(originalString, clone);
		return clone;
	}

	protected abstract Transloader getTransloader();

	public void testClonesObjectsWithPrimitiveFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithPrimitiveFields());
	}

	public void testClonesObjectsNotOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new NotCommonJavaObject());
	}

	public void testClonesObjectsWithFieldsOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithStringField(Triangulate.anyString()));
	}

	public void testClonesObjectsWithFieldsNotOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithNotCommonJavaFields(new WithStringField(Triangulate.anyString())));
	}

	public void testClonesObjectsWithArrayFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithArrayFields());
	}

	public void testClonesFieldsThroughoutHierarchies() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new Bottom(new NotCommonJavaObject(),
				Triangulate.anyInt(), Triangulate.anyString(), Triangulate.eitherBoolean()));
	}

	public void testClonesObjectsOfSerializableAnonymousClasses() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithAnonymousClassFields(Triangulate.anyInteger()));
	}

	public void testClonesObjectsWithListFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithListFields());
	}

    public void testClonesObjectsWithSetFields() throws Exception {
        assertDeeplyClonedToOtherClassLoader(new WithSetFields());
    }

    public void testClonesObjectsWithMapFields() throws Exception {
        assertDeeplyClonedToOtherClassLoader(new WithMapFields());
    }

	public void testClonesAllFieldsWithCircularReferences() throws Exception {
		cloneWithCircularReferences();
	}

	public void testClonesAllFieldsWithCircularReferencesConcurrently() throws Exception {
		cloneWithCircularReferences();
	}

	public void testClonesAllFieldsWithCircularReferencesYetMoreConcurrently() throws Exception {
		cloneWithCircularReferences();
	}

	private void cloneWithCircularReferences() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new SelfAndParentReferencingChild(Triangulate.anyString(),
				new SelfAndChildReferencingParent(Triangulate.anyString())));
	}
}