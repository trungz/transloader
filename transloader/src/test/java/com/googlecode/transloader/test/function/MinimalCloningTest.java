package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.TransloaderObject;
import com.googlecode.transloader.test.Triangulator;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithSetFields;

public class MinimalCloningTest extends CloningTestCase {
	public static Test suite() throws Exception {
		return new ActiveTestSuite(MinimalCloningTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulator.anyString();
		assertSame(string, new TransloaderObject(string).cloneTo(IndependentClassLoader.getInstance()));
	}

	public void testClonesObjectsWithSetFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithSetFields());
	}

	public void testClonesObjectsWithMapFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithMapFields());
	}

	protected void assertDeeplyClonedToOtherClassLoader(NonCommonJavaType original) throws Exception {
		String originalString = original.toString();
		Object clone = new TransloaderObject(original).cloneTo(IndependentClassLoader.getInstance());
		assertNotSame(original, clone);
		assertEqualExceptForClassLoader(originalString, clone);
	}
}
