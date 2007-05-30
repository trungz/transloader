package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.TransloaderObject;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.Triangulator;
import com.googlecode.transloader.test.fixture.HiearchyWithFieldsBottom;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NonCommonJavaObject;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;
import com.googlecode.transloader.test.fixture.SelfAndChildReferencingParent;
import com.googlecode.transloader.test.fixture.SelfAndParentReferencingChild;
import com.googlecode.transloader.test.fixture.SerializableWithAnonymousClassFields;
import com.googlecode.transloader.test.fixture.WithArrayFields;
import com.googlecode.transloader.test.fixture.WithListFields;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithNonCommonJavaFields;
import com.googlecode.transloader.test.fixture.WithPrimitiveFields;
import com.googlecode.transloader.test.fixture.WithSetFields;
import com.googlecode.transloader.test.fixture.WithStringField;

public class DefaultCloningStrategyTest extends BaseTestCase {
	private static final CloningStrategy CLONER = TransloaderObject.DEFAULT_CLONER;

	public static Test suite() throws Exception {
		return new ActiveTestSuite(DefaultCloningStrategyTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulator.anyString();
		assertSame(string, CLONER.cloneObjectToClassLoader(string, IndependentClassLoader.getInstance()));
	}

	public void testClonesObjectsWithPrimitiveFields() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithPrimitiveFields());
	}

	public void testClonesObjectsNotOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new NonCommonJavaObject());
	}

	public void testClonesObjectsWithFieldsOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithStringField(Triangulator.anyString()));
	}

	public void testClonesObjectsWithFieldsNotOfCommonJavaTypes() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithNonCommonJavaFields(
				new WithStringField(Triangulator.anyString())));
	}

	public void testClonesObjectsWithArrayFields() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithArrayFields());
	}

	public void testClonesFieldsThroughoutHierarchies() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new HiearchyWithFieldsBottom(new NonCommonJavaObject(),
				Triangulator.anyInt(), Triangulator.anyString(), Triangulator.eitherBoolean()));
	}

	public void testClonesObjectsOfSerializableAnonymousClasses() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new SerializableWithAnonymousClassFields(Triangulator.anyInteger()));
	}

	public void testClonesObjectsWithListFields() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithListFields());
	}

	public void testClonesObjectsWithSetFields() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithSetFields());
	}

	public void testClonesObjectsWithMapFields() throws Exception {
		assertDeeplyClonedIntoOtherClassLoader(new WithMapFields());
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
		assertDeeplyClonedIntoOtherClassLoader(new SelfAndParentReferencingChild(Triangulator.anyString(),
				new SelfAndChildReferencingParent(Triangulator.anyString())));
	}

	private void assertDeeplyClonedIntoOtherClassLoader(NonCommonJavaType original) throws Exception {
		String originalString = original.toString();
		print(original);
		Object clone = CLONER.cloneObjectToClassLoader(original, IndependentClassLoader.getInstance());
		print(clone);
		assertNotSame(original, clone);
		assertEqualExceptForClassLoader(originalString, clone);
	}
}
