package com.googlecode.transloader.test;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Random;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.objenesis.ObjenesisHelper;

public final class Triangulator {
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final byte[] ONE_BYTE_BUFFER = new byte[1];
	private static final Method[] MY_METHODS = Triangulator.class.getDeclaredMethods();

	private Triangulator() {
	}

	public static String anyString() {
		return anyDouble() + "";
	}

	public static boolean eitherBoolean() {
		return RANDOM.nextBoolean();
	}

	public static byte anyByte() {
		RANDOM.nextBytes(ONE_BYTE_BUFFER);
		return ONE_BYTE_BUFFER[0];
	}

	public static char anyChar() {
		return (char) anyByte();
	}

	public static short anyShort() {
		return (short) anyByte();
	}

	public static int anyInt() {
		return RANDOM.nextInt();
	}

	public static int anyIntFromZeroTo(int upperBound) {
		return RANDOM.nextInt(upperBound);
	}

	public static Integer anyInteger() {
		return new Integer(anyInt());
	}

	public static long anyLong() {
		return RANDOM.nextLong();
	}

	public static float anyFloat() {
		return RANDOM.nextFloat();
	}

	public static double anyDouble() {
		return RANDOM.nextDouble();
	}

	public static Class anyClass() {
		return anyMethod().getReturnType();
	}

	public static Method anyMethod() {
		return MY_METHODS[anyIntFromZeroTo(MY_METHODS.length - 1)];
	}

	public static Object dummyInstanceOf(Class type) {
		try {
			if (type.isArray()) return anyArrayOf(type.getComponentType());
			Object triangulatedInstance = locallyTriangulate(type);
			if (triangulatedInstance != null) return triangulatedInstance;
			if (type.isInterface())
				return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {type},
						new NoOpInvocationHandler());
			if (Modifier.isAbstract(type.getModifiers())) return Enhancer.create(type, new NoOp() {
			});
			return ObjenesisHelper.newInstance(type);
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		}
	}

	private static Object anyArrayOf(Class componentType) throws Exception {
		int length = anyIntFromZeroTo(3);
		Object array = Array.newInstance(componentType, length);
		for (int i = 0; i < length; i++) {
			Array.set(array, i, dummyInstanceOf(componentType));
		}
		return array;
	}

	private static Object locallyTriangulate(Class type) throws Exception {
		for (int i = 0; i < MY_METHODS.length; i++) {
			Method method = MY_METHODS[i];
			Class returnType = method.getReturnType();
			boolean hasNoParameters = method.getParameterTypes() == null || method.getParameterTypes().length == 0;
			if (returnType == type && hasNoParameters) {
				return method.invoke(null, new Object[0]);
			}
		}
		return null;
	}

	private static class NoOpInvocationHandler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return null;
		}
	}
}
