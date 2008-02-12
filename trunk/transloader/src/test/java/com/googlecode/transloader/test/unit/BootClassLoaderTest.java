package com.googlecode.transloader.test.unit;

import com.googlecode.transloader.load.BootClassLoader;
import com.googlecode.transloader.test.BaseTestCase;
import org.apache.commons.lang.ClassUtils;

public class BootClassLoaderTest extends BaseTestCase {
    public void testLoadsPrimitiveClasses() throws ClassNotFoundException {
        assertSame(int.class, tryToLoadFromBootClassLoader(int.class));
    }

    public void testDoesNotLoadClassPathClasses() throws ClassNotFoundException {
        assertThrows(new ClassNotFoundException(), new Thrower() {
            public void executeUntilThrow() throws Throwable {
                tryToLoadFromBootClassLoader(this.getClass());
            }
        });
    }

    private Class tryToLoadFromBootClassLoader(Class theClass) throws ClassNotFoundException {
        return ClassUtils.getClass(BootClassLoader.INSTANCE, theClass.getName());
    }
}
