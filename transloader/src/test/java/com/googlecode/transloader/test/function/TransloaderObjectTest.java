package com.googlecode.transloader.test.function;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import com.googlecode.transloader.InvocationDescription;
import com.googlecode.transloader.TransloaderObject;
import com.googlecode.transloader.TransloadingException;
import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.Triangulator;
import com.googlecode.transloader.test.fixture.IndependentClassLoader;
import com.googlecode.transloader.test.fixture.NonCommonJavaObject;
import com.googlecode.transloader.test.fixture.NonCommonJavaType;
import com.googlecode.transloader.test.fixture.WithMapFields;
import com.googlecode.transloader.test.fixture.WithMethods;
import com.googlecode.transloader.test.fixture.WithPrimitiveFields;
import com.googlecode.transloader.test.fixture.WithStringField;

public class TransloaderObjectTest extends BaseTestCase {
	private Object foreignObject;
	private Object foreignObjectWithMethods;

	public static Test suite() throws Exception {
		return new ActiveTestSuite(TransloaderObjectTest.class);
	}

	protected void setUp() throws Exception {
		foreignObject = getNewInstanceFromOtherClassLoader(WithMapFields.class);
		foreignObjectWithMethods = getNewInstanceFromOtherClassLoader(WithMethods.class);
	}

	private static Object getNewInstanceFromOtherClassLoader(Class clazz) throws Exception {
		return IndependentClassLoader.getInstance().loadClass(clazz.getName()).newInstance();
	}

	public void testReportsIsNullWhenGivenNull() throws Exception {
		assertTrue(new TransloaderObject(null).isNull());
	}

	public void testReportsIsNotNullWhenGivenNonNullObject() throws Exception {
		assertFalse(new TransloaderObject(new Object()).isNull());
	}

	public void testReportsIsNotInstanceOfUnrelatedType() throws Exception {
		assertFalse(new TransloaderObject(new Object()).isInstanceOf(NonCommonJavaType.class.getName()));
	}

	public void testReportsIsInstanceOfSameClass() throws Exception {
		assertTrue(new TransloaderObject(foreignObject).isInstanceOf(foreignObject.getClass().getName()));
	}

	public void testReportsIsInstanceOfSuperClass() throws Exception {
		assertTrue(new TransloaderObject(foreignObject).isInstanceOf(NonCommonJavaObject.class.getName()));
	}

	public void testReportsIsInstanceOfImplementedInterface() throws Exception {
		assertTrue(new TransloaderObject(foreignObject).isInstanceOf(NonCommonJavaType.class.getName()));
	}

	public void testReturnsNullWhenAskedToCloneNull() throws Exception {
		assertNull(new TransloaderObject(null).cloneTo(null));
	}

	public void testReturnsCloneReturnedFromGivenCloningStrategy() throws Exception {
		final Object expectedOriginal = new Object();
		final ClassLoader expectedClassloader = new ClassLoader() {
		};
		final Object expectedClone = new Object();
		CloningStrategy cloningStrategy = new CloningStrategy() {
			public Object cloneObjectToClassLoader(Object original, ClassLoader cloneClassLoader) throws Exception {
				assertSame(expectedOriginal, original);
				assertSame(expectedClassloader, cloneClassLoader);
				return expectedClone;
			}
		};
		assertSame(expectedClone, new TransloaderObject(expectedOriginal, cloningStrategy).cloneTo(expectedClassloader));
	}

	public void testWrapsExceptionThrownByGivenCloningStrategy() throws Exception {
		final Object expectedOriginal = new Object();
		final Exception expectedException = new Exception(Triangulator.anyString());
		final CloningStrategy cloningStrategy = new CloningStrategy() {
			public Object cloneObjectToClassLoader(Object original, ClassLoader cloneClassLoader) throws Exception {
				throw expectedException;
			}
		};
		Thrower thrower = new Thrower() {
			public void executeUntilThrow() throws Throwable {
				new TransloaderObject(expectedOriginal, cloningStrategy).cloneTo(null);
			}
		};
		assertThrows(thrower, new TransloadingException("Unable to clone '" + expectedOriginal + "'.",
				expectedException));
	}

	public void testProvidesWrappedObjectOnRequest() throws Exception {
		final Object expected = new Object();
		assertSame(expected, new TransloaderObject(expected).getUntransloadedObject());
	}

	public void testPassesAndReturnsStringsToAndFromInvocations() throws Exception {
		TransloaderObject transloaderObject = new TransloaderObject(foreignObjectWithMethods);
		String expectedStringFieldValue = Triangulator.anyString();
		transloaderObject.invoke(new InvocationDescription("setStringField", expectedStringFieldValue));
		assertSame(expectedStringFieldValue, transloaderObject.invoke(new InvocationDescription("getStringField")));
	}

	public void testClonesParametersOfNonCommonJavaTypesInInvocations() throws Exception {
		NonCommonJavaType first = new WithStringField(Triangulator.anyString());
		NonCommonJavaType second = new WithPrimitiveFields();
		String expected = new WithMethods().concatenate(first, second);
		Class[] paramTypes = {NonCommonJavaType.class, NonCommonJavaType.class};
		Object[] params = {first, second};
		String actual =
				(String) new TransloaderObject(foreignObjectWithMethods).invoke(new InvocationDescription(
						"concatenate", paramTypes, params));
		assertEqualExceptForClassLoader(expected, actual);
	}
}
