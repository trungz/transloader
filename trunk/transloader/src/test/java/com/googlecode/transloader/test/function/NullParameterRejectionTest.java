package com.googlecode.transloader.test.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;

import org.apache.commons.lang.StringUtils;

import com.googlecode.transloader.Assert;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.ProductionClassFinder;
import com.googlecode.transloader.test.Triangulator;

public class NullParameterRejectionTest extends BaseTestCase {
	private static final Class PRODUCTION_ASSERT_CLASS = Assert.class;
	private static final String PRODUCTION_PACKAGE_NAME = PRODUCTION_ASSERT_CLASS.getPackage().getName();
	private static final String TEST_PACKAGE_NAME = "com.googlecode.transloader.test";
	private static final Class[] ALL_PRODUCTION_CLASSES =
			ProductionClassFinder.getAllProductionClasses(PRODUCTION_ASSERT_CLASS, TEST_PACKAGE_NAME);

	private static class ExcemptParam {
		String methodDescription;
		int paramNumber;

		ExcemptParam(String methodDescription, int paramNumber) {
			this.methodDescription = methodDescription;
			this.paramNumber = paramNumber;
		}

		public boolean equals(Object obj) {
			ExcemptParam other = (ExcemptParam) obj;
			return StringUtils.contains(methodDescription, other.methodDescription) && paramNumber == other.paramNumber;
		}
	}

	private static final List EXCEMPT_PARAMS =
			Arrays.asList(new ExcemptParam[] {
					new ExcemptParam("DefaultTransloader.wrap(java.lang.Class)", 0),
					new ExcemptParam("ClassWrapper(java.lang.Class)", 0),
					new ExcemptParam("InvocationDescription(java.lang.String,java.lang.Class,java.lang.Object)", 2),
					new ExcemptParam("InvocationDescription(java.lang.String,java.lang.String,java.lang.Object)", 2),
					new ExcemptParam("InvocationDescription(java.lang.reflect.Method,java.lang.Object[])", 1),
					new ExcemptParam(
							"ObjectWrapper(java.lang.Object,com.googlecode.transloader.clone.CloningStrategy)", 0),});

	public static Test suite() throws Exception {
		return new ActiveTestSuite(NullParameterRejectionTest.class);
	}

	public void testAllPublicMethodsRejectNullParameters() throws Exception {
		for (int i = 0; i < ALL_PRODUCTION_CLASSES.length; i++) {
			Class productionClass = ALL_PRODUCTION_CLASSES[i];
			if (isPublicConcreteClassOtherThanAssert(productionClass))
				assertPublicMethodsRejectNullParameters(productionClass);
		}
	}

	public void testAllPublicConstuctorsRejectNullParameters() throws Exception {
		for (int i = 0; i < ALL_PRODUCTION_CLASSES.length; i++) {
			Class productionClass = ALL_PRODUCTION_CLASSES[i];
			if (isPublicConcreteClassOtherThanAssert(productionClass))
				assertPublicConstuctorsRejectNullParameters(productionClass);
		}
	}

	private boolean isPublicConcreteClassOtherThanAssert(Class productionClass) {
		return !productionClass.isInterface() && Modifier.isPublic(productionClass.getModifiers()) && productionClass != PRODUCTION_ASSERT_CLASS;
	}

	private void assertPublicMethodsRejectNullParameters(Class productionClass) throws Exception {
		Object instance = Triangulator.dummyInstanceOf(productionClass);
		Method[] methods = productionClass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getDeclaringClass().getPackage().getName().startsWith(PRODUCTION_PACKAGE_NAME))
				assertRejectsNullParameters(instance, methods[i]);
		}
	}

	private void assertRejectsNullParameters(final Object instance, final Method method) throws Exception {
		Class[] parameterTypes = method.getParameterTypes();
		List nonNullParameters = getNonNullParameters(parameterTypes);
		for (int i = 0; i < parameterTypes.length; i++) {
			if (shouldTestNullRejectionForParameter(method, parameterTypes, i)) {
				List parameters = new ArrayList(nonNullParameters);
				parameters.set(i, null);
				assertExceptionThrownFromInvoking(instance, method, parameters);
			}
		}
	}

	private void assertExceptionThrownFromInvoking(final Object instance, final Method method, final List parameters) {
		dump(method.toString() + parameters);
		Thrower thrower = new Thrower() {
			public void executeUntilThrow() throws Throwable {
				method.invoke(instance, parameters.toArray());
			}
		};
		assertThrows(thrower, new InvocationTargetException(new IllegalArgumentException(
				"Expecting no null parameters but received " + parameters + ".")));
	}

	private void assertPublicConstuctorsRejectNullParameters(Class productionClass) {
		Constructor[] constructors = productionClass.getConstructors();
		for (int i = 0; i < constructors.length; i++) {
			assertRejectsNullParameters(constructors[i]);
		}
	}

	private void assertRejectsNullParameters(Constructor constructor) {
		Class[] parameterTypes = constructor.getParameterTypes();
		List nonNullParameters = getNonNullParameters(parameterTypes);
		for (int i = 0; i < parameterTypes.length; i++) {
			if (shouldTestNullRejectionForParameter(constructor, parameterTypes, i)) {
				List parameters = new ArrayList(nonNullParameters);
				parameters.set(i, null);
				assertExceptionThrownFromInvoking(constructor, parameters);
			}
		}
	}

	private void assertExceptionThrownFromInvoking(final Constructor constructor, final List parameters) {
		dump(constructor.toString() + parameters);
		Thrower thrower = new Thrower() {
			public void executeUntilThrow() throws Throwable {
				constructor.newInstance(parameters.toArray());
			}
		};
		assertThrows(thrower, new InvocationTargetException(new IllegalArgumentException(
				"Expecting no null parameters but received ")));
	}

	private List getNonNullParameters(Class[] parameterTypes) {
		List nonNullParameters = new ArrayList();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class parameterType = parameterTypes[i];
			nonNullParameters.add(Triangulator.dummyInstanceOf(parameterType));
		}
		return nonNullParameters;
	}

	private boolean shouldTestNullRejectionForParameter(Object methodOrConstructor, Class[] parameterTypes, int i) {
		return !(parameterTypes[i].isPrimitive() || EXCEMPT_PARAMS.contains(new ExcemptParam(
				methodOrConstructor.toString(), i)));
	}
}
