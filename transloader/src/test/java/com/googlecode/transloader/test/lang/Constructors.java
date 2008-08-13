package com.googlecode.transloader.test.lang;

import java.lang.reflect.Constructor;

/**
 * @author jeremywales
 */
public class Constructors {
    public static Constructor getLowestShortestConstructor(Class type) {
        Constructor[] constructors = lowestConstructors(type);
        return shortest(constructors);
    }

    private static Constructor[] lowestConstructors(Class type) {
        Constructor[] constructors = type.getDeclaredConstructors();
        return constructors.length > 0 ? constructors : lowestConstructors(type.getSuperclass());
    }

    private static Constructor shortest(Constructor[] constructors) {
        Constructor shortest = constructors[0];
        for (int i = 1; i < constructors.length; i++) {
            Constructor current = constructors[i];
            if (params(current) < params(shortest))
                shortest = current;
        }
        return shortest;
    }

    private static int params(Constructor constructor) {
        return constructor.getParameterTypes().length;
    }
}
