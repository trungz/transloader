package com.googlecode.transloader.test.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.transloader.AcceptsNullMethodParams;
import com.googlecode.transloader.Assert;
import com.googlecode.transloader.test.BaseTestCase;
import com.googlecode.transloader.test.ProductionClassFinder;
import com.googlecode.transloader.test.Triangulator;

public class DesignPoliciesTest extends BaseTestCase {
	private static final Class PRODUCTION_ASSERT_CLASS = Assert.class;
	private static final String PRODUCTION_PACKAGE_NAME = PRODUCTION_ASSERT_CLASS.getPackage().getName();
	private static final String TEST_PACKAGE_NAME = "com.googlecode.transloader.test";

	public void testAllPublicMethodsRejectNullParameters() throws Exception {
		Class[] allProductionClasses =
				ProductionClassFinder.getAllProductionClasses(PRODUCTION_ASSERT_CLASS, TEST_PACKAGE_NAME);
		for (int i = 0; i < allProductionClasses.length; i++) {
			Class productionClass = allProductionClasses[i];
			if (shouldBeTestedForNulls(productionClass)) assertPublicMethodsRejectNullParameters(productionClass);
		}
	}

	private boolean shouldBeTestedForNulls(Class productionClass) {
		return !productionClass.isInterface() && Modifier.isPublic(productionClass.getModifiers()) && !AcceptsNullMethodParams.class.isAssignableFrom(productionClass) && productionClass != PRODUCTION_ASSERT_CLASS;
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
		List nonNullParameters = new ArrayList();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class parameterType = parameterTypes[i];
			nonNullParameters.add(Triangulator.dummyInstanceOf(parameterType));
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			final List parameters = new ArrayList(nonNullParameters);
			parameters.set(i, null);
			assertExceptionThrownFromInvoking(instance, method, parameters);
		}
	}

	private void assertExceptionThrownFromInvoking(final Object instance, final Method method, final List parameters) {
		System.out.println(method.toString() + parameters);
		Thrower thrower = new Thrower() {
			public void executeUntilThrow() throws Throwable {
				method.invoke(instance, parameters.toArray());
			}
		};
		assertThrows(thrower, new InvocationTargetException(new IllegalArgumentException(
				"Expecting no null parameters but received " + parameters + ".")));
	}
}
