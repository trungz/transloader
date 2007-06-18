package com.googlecode.transloader;

import org.apache.commons.lang.Validate;

/**
 * Describes a method invocation by method name, parameter type names and parameters.
 * 
 * @author Jeremy Wales
 */
public final class InvocationDescription {
	private final String methodName;
	private final String[] parameterTypeNames;
	private final Object[] parameters;

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name.
	 * 
	 * @param methodName the name of a zero-parameter method
	 */
	public InvocationDescription(String methodName) {
		this(methodName, new String[] {}, new Object[] {});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameter a parameter whose type name declared in the method is the same as the name of its concrete
	 *            implementation <code>Class</code>
	 */
	public InvocationDescription(String methodName, Object parameter) {
		this(methodName, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name and parameters.
	 * 
	 * @param methodName the name of a multi-parameter method
	 * @param parameters some parameters whose type names declared in the method are the same as the names of their
	 *            concrete implementation <code>Class</code>es
	 */
	public InvocationDescription(String methodName, Object[] parameters) {
		this(methodName, getClasses(parameters), parameters);
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterType a <code>Class</code> whose name is the same as the parameter type declared in the method
	 * @param parameter the parameter to the method invocation
	 */
	public InvocationDescription(String methodName, Class parameterType, Object parameter) {
		this(methodName, new Class[] {parameterType}, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterTypeName the name of the parameter type declared in the method
	 * @param parameter the parameter to the method invocation
	 */
	public InvocationDescription(String methodName, String parameterTypeName, Object parameter) {
		this(methodName, new String[] {parameterTypeName}, new Object[] {parameter});
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter types and parameters.
	 * 
	 * @param methodName the name of a multi-parameter method
	 * @param parameterTypes some <code>Class</code>es whose names are the same as the parameter types declared in
	 *            the method
	 * @param parameters the parameters to the method invocation
	 */
	public InvocationDescription(String methodName, Class[] parameterTypes, Object[] parameters) {
		this(methodName, getNames(parameterTypes), parameters);
	}

	/**
	 * Constructs an <code>InvocationDescription</code> with the given method name, parameter type name and parameter.
	 * 
	 * @param methodName the name of a single-parameter method
	 * @param parameterTypeNames the names of the parameter types declared in the method
	 * @param parameters the parameters to the method invocation
	 */
	public InvocationDescription(String methodName, String[] parameterTypeNames, Object[] parameters) {
		// TODO test this bit
		Validate.isTrue(parameterTypeNames.length == parameters.length);
		this.methodName = methodName;
		this.parameterTypeNames = parameterTypeNames;
		this.parameters = parameters;
	}

	private static Class[] getClasses(Object[] objects) {
		Class[] classes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}

	private static String[] getNames(Class[] classes) {
		String[] names = new String[classes.length];
		for (int i = 0; i < classes.length; i++) {
			names[i] = classes[i].getName();
		}
		return names;
	}

	/**
	 * Gets the name of the method to be invoked.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the names of the parameter types of the method to be invoked.
	 * 
	 * @return the parameter type names
	 */
	public String[] getParameterTypeNames() {
		return parameterTypeNames;
	}

	/**
	 * Gets the parameters to be passed to the method to be invoked.
	 * 
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}
}
