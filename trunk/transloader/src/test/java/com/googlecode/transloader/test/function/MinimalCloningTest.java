package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.TransloaderObject;
import com.googlecode.transloader.test.Triangulator;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;

public class MinimalCloningTest extends CloningTest {

	public static Test suite() throws Exception {
		return new ActiveTestSuite(MinimalCloningTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulator.anyString();
		assertSame(string, new TransloaderObject(string).cloneMinimallyTo(IndependentClassLoader.getInstance()));
	}

	protected void assertDeeplyClonedIntoOtherClassLoader(NonCommonJavaType original) throws Exception {
		String originalString = original.toString();
		print(original);
		Object clone = new TransloaderObject(original).cloneMinimallyTo(IndependentClassLoader.getInstance());
		print(clone);
		assertNotSame(original, clone);
		assertEqualExceptForClassLoader(originalString, clone);
	}
}
