package com.googlecode.transloader;

public final class InvocationDescription {
	private String methodName;
	private String[] parameterTypeNames;
	private Object[] parameters;

	public InvocationDescription(String methodName) {
		this(methodName, new String[] {}, new Object[] {});
	}

	public InvocationDescription(String methodName, Object parameter) {
		this(methodName, new Object[] {parameter});
	}

	public InvocationDescription(String methodName, Object[] parameters) {
		this(methodName, getClasses(parameters), parameters);
	}

	public InvocationDescription(String methodName, Class parameterType, Object parameter) {
		this(methodName, new Class[] {parameterType}, new Object[] {parameter});
	}

	public InvocationDescription(String methodName, String parameterTypeName, Object parameter) {
		this(methodName, new String[] {parameterTypeName}, new Object[] {parameter});
	}

	public InvocationDescription(String methodName, Class[] classes, Object[] parameters) {
		this(methodName, getNames(classes), parameters);
	}

	public InvocationDescription(String methodName, String[] parameterTypeNames, Object[] parameters) {
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

	public String getMethodName() {
		return methodName;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public String[] getParameterTypeNames() {
		return parameterTypeNames;
	}
}
