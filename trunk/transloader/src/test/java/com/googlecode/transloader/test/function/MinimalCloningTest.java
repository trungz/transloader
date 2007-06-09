package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.DefaultTransloaderFactory;
import com.googlecode.transloader.TransloaderFactory;
import com.googlecode.transloader.test.Triangulator;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithSetFields;

public class MinimalCloningTest extends CloningTestCase {
	public static Test suite() throws Exception {
		return new ActiveTestSuite(MinimalCloningTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulator.anyString();
		assertSame(string, new DefaultTransloaderFactory().wrap(string).getEquivalentFrom(
				IndependentClassLoader.getInstance()));
	}

	public void testClonesObjectsWithSetFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithSetFields());
	}

	public void testClonesObjectsWithMapFields() throws Exception {
		assertDeeplyClonedToOtherClassLoader(new WithMapFields());
	}

	protected TransloaderFactory getTransloaderFactory() {
		return new DefaultTransloaderFactory();
	}
}
