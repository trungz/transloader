package com.googlecode.transloader.test.function;

import com.googlecode.transloader.DefaultTransloader;
import com.googlecode.transloader.Transloader;
import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

public class MinimalCloningTest extends CloningTestCase {
    public static Test suite() throws Exception {
        return new ActiveTestSuite(MinimalCloningTest.class);
    }

    public void testDoesNotCloneStrings() throws Exception {
        Object string = Triangulate.anyString();
        assertSame(string, getTransloader().wrap(string).cloneWith(IndependentClassLoader.INSTANCE));
    }

    protected Transloader getTransloader() {
        return new DefaultTransloader(com.googlecode.transloader.configure.CloningStrategy.MINIMAL);
    }
}
