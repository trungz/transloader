package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.DefaultTransloader;
import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.fields.WithMapFields;
import com.googlecode.transloader.test.fixture.fields.WithSetFields;

public class MinimalCloningTest extends CloningTestCase {
	public static Test suite() throws Exception {
		return new ActiveTestSuite(MinimalCloningTest.class);
	}

	public void testDoesNotCloneStrings() throws Exception {
		Object string = Triangulate.anyString();
        assertSame(string, getTransloader().wrap(string).cloneWith(IndependentClassLoader.INSTANCE));
	}

	protected Transloader getTransloader() {
		return new DefaultTransloader(CloningStrategy.MINIMAL);
	}
}
