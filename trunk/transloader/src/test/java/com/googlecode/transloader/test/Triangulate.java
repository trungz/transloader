package com.googlecode.transloader.test;

import com.googlecode.transloader.test.lang.Constructors;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.objenesis.ObjenesisHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * @author Jeremy Wales
 */
public final class Triangulate {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final byte[] ONE_BYTE_BUFFER = new byte[1];
    private static final Method[] MY_METHODS = Triangulate.class.getDeclaredMethods();
    public static final Field[] MY_FIELDS = Triangulate.class.getDeclaredFields();

    private Triangulate() {
    }

    public static String anyString() {
        return anyDouble() + "";
    }

    public static String anyAlphanumbericString() {
        return Long.toHexString(anyLong());
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
        return (Method) anyElementOf(MY_METHODS);
    }

    public static Field anyField() {
        return (Field) anyElementOf(MY_FIELDS);
    }

    public static Object anyElementOf(Object[] array) {
        return array[anyIntFromZeroTo(array.length - 1)];
    }

    public static Object[] anyInstancesOf(Class[] types) {
        Object[] params = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class paramType = types[i];
            params[i] = anyInstanceOf(paramType);
        }
        return params;
    }

    public static Object anyInstanceOf(Class type) {
        try {
            if (isNullOrVoid(type))
                return null;
            if (type.isArray())
                return anyArrayOf(type.getComponentType());
            Object instance = tryToTriangulateFromThisClass(type);
            if (instance != null)
                return instance;
            if (type.isInterface())
                return interfaceProxy(type);
            if (isAbstract(type))
                return classProxy(type);
            return ObjenesisHelper.newInstance(type);
        } catch (Exception e) {
            throw new NestableRuntimeException(e);
        }
    }

    private static boolean isNullOrVoid(Class type) {
        return type == null || type == void.class;
    }

    private static boolean isAbstract(Class type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    private static Object interfaceProxy(Class type) {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{type},
                new TriangulatingInvocationHandler()
        );
    }

    private static Object classProxy(Class type) {
        Constructor constructor = Constructors.getLowestShortestConstructor(type);
        Class[] paramTypes = constructor.getParameterTypes();
        Object[] params = anyInstancesOf(paramTypes);
        return proxy(type, paramTypes, params);
    }

    private static Object proxy(Class type, Class[] paramTypes, Object[] params) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallback(new TriangulatingInvocationHandler());
        return enhancer.create(paramTypes, params);
    }

    private static Object anyArrayOf(Class componentType) throws Exception {
        int length = anyIntFromZeroTo(3);
        Object array = Array.newInstance(componentType, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, anyInstanceOf(componentType));
        }
        return array;
    }

    private static Object tryToTriangulateFromThisClass(Class type) throws Exception {
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
}
