package com.googlecode.transloader.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author jeremywales
*/
final class TriangulatingInvocationHandler implements InvocationHandler, net.sf.cglib.proxy.InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class type = method.getReturnType();
        if (type == void.class)
            return null;
        if (hasNameAndParams("equals", 1, method) && method.getParameterTypes()[0] == Object.class)
            return equals(args[0], proxy);
        if (hasNameAndParams("hashCode", 0, method))
            return hashCode(proxy);
        if (hasNameAndParams("toString", 0, method))
            return toString(proxy);
        return Triangulate.anyInstanceOf(type);
    }

    private static Boolean equals(Object arg, Object proxy) {
        return proxy == arg ? Boolean.TRUE : Boolean.FALSE;
    }

    private static Integer hashCode(Object proxy) {
        return new Integer(System.identityHashCode(proxy));
    }

    private static String toString(Object proxy) {
        return TriangulatingInvocationHandler.class.getName() + '#' + hashCode(proxy);
    }

    private static boolean hasNameAndParams(String name, int params, Method method) {
        boolean nameEqual = method.getName().equals(name);
        boolean paramEqual = method.getParameterTypes().length == params;
        return nameEqual && paramEqual;
    }
}
