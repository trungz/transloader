package com.googlecode.transloader;

import java.util.Arrays;
import java.util.List;

/**
 * Static utility for making assertions.
 * 
 * @author Jeremy Wales
 */
public final class Assert {

	private Assert() {
	}

	/**
	 * Asserts that the given parameter is not <code>null</code>.
	 * 
	 * @param parameter the parameter to check
	 * @throws IllegalArgumentException if <code>parameter</code> is <code>null</code>.
	 */
	public static void isNotNull(Object parameter) {
		areNotNull(new Object[] {parameter});
	}

	/**
	 * Asserts that the given parameters are not <code>null</code>.
	 * 
	 * @param parameter1 the first parameter to check
	 * @param parameter2 the second parameter to check
	 * @throws IllegalArgumentException if either <code>parameter1</code> or <code>parameter2</code> is
	 *             <code>null</code>.
	 */
	public static void areNotNull(Object parameter1, Object parameter2) {
		areNotNull(new Object[] {parameter1, parameter2});
	}

	/**
	 * Asserts that the given parameters are not <code>null</code>.
	 * 
	 * @param parameters the parameters to check
	 * @throws IllegalArgumentException if any elements of <code>parameters</code> are <code>null</code>.
	 */
	public static void areNotNull(Object[] parameters) {
		List parameterList = Arrays.asList(parameters);
		if (parameterList.contains(null))
			throw new IllegalArgumentException("Expecting no null parameters but received " + parameterList + ".");
	}
}
