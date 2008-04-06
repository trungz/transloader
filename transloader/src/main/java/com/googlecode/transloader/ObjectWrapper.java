package com.googlecode.transloader;

import com.googlecode.transloader.clone.CloningStrategy;
import com.googlecode.transloader.except.Assert;
import com.googlecode.transloader.except.TransloaderException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * The wrapper appropriate for wrapping around all <code>Object</code>s referencing <code>Class</code>es from
 * potentially foreign <code>ClassLoader</code>s.
 *
 * @author Jeremy Wales
 */
public final class ObjectWrapper {
    private final Object wrappedObject;
    private final CloningStrategy cloner;
    private final ClassLoader parameterClassLoader;

    /**
     * Constructs a new <code>ObjectWrapper</code> around the given object, which will use the given
     * <code>CloningStrategy</code> when required. Note that using an implementation of {@link Transloader} is the
     * recommended way to produce these.
     *
     * @param objectToWrap         the object to wrap (can be <code>null</code>)
     * @param cloningStrategy      the strategy for cloning
     * @param parameterClassLoader the <code>ClassLoader</code> with which to clone parameters passed into methods of
     *                             the <code>objectToWrap</code>
     */
    public ObjectWrapper(Object objectToWrap, CloningStrategy cloningStrategy, ClassLoader parameterClassLoader) {
        Assert.areNotNull(cloningStrategy, parameterClassLoader);
        wrappedObject = objectToWrap;
        cloner = cloningStrategy;
        this.parameterClassLoader = parameterClassLoader;
    }

    /**
     * Indicates whether or not <code>null</code> is what is wrapped.
     *
     * @return true if the wrapped "object" is actually <code>null</code>
     */
    public boolean isNull() {
        return wrappedObject == null;
    }

    /**
     * Provides direct access to the wrapped object.
     *
     * @return the actual wrapped object without any wrapping
     */
    public Object getUnwrappedSelf() {
        return wrappedObject;
    }

    /**
     * Indicates whether or not the wrapped object is an instance of the type with the given name in the wrapped
     * object's <code>ClassLoader</code>(s). It takes a parameter of type <code>String</code> instead of
     * <code>Class</code> so that the test can be performed for <code>Class</code>es that do not have an equivalent
     * in the caller's <code>ClassLoader</code>.
     *
     * @param typeName the name of the type against which the wrapped object will be checked
     * @return true if the wrapped object is an instance of the type with the given name in the wrapped object's
     *         <code>ClassLoader</code>(s)
     */
    public boolean isInstanceOf(String typeName) {
        Assert.isNotNull(typeName);
        return Transloader.DEFAULT.wrap(wrappedObject.getClass()).isAssignableTo(typeName);
    }

    /**
     * Gets an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the given
     * <code>ClassLoader</code>. Every object referenced in the object graph starting with the object returned will
     * be able to be cast to its respective types in the given <code>ClassLoader</code>.
     * <p>
     * This implementation employs the <code>CloningStrategy</code> configured at construction. Note that using
     * {@link CloningStrategy#MINIMAL} (which is not the default strategy in {@link Transloader#DEFAULT}) will often
     * effect some changes within the object graph that starts with the wrapped object itself, as opposed to producing a
     * completely new, seperate graph. Using {@link CloningStrategy#MAXIMAL} instead prevents this, producing a purely
     * seperate clone without any changes within the wrapped object graph, at the cost of potentially far greater
     * cloning effort. An object graph altered by cloning with {@link CloningStrategy#MINIMAL} can of course be restored
     * entirely for use with other objects of <code>Class</code>es from its original <code>ClassLoader</code>(s)
     * by cloning it back with those original <code>ClassLoader</code>(s), but this is an extra coding step and
     * somewhat reduces the effort saved by not using {@link CloningStrategy#MAXIMAL} in the first place.
     * </p>
     *
     * @param classLoader the <code>ClassLoader</code> to use in creating an equivalent of the wrapped object
     * @return an equivalent of the wrapped object with all <code>Class</code>es referenced being loaded from the
     *         given <code>ClassLoader</code>
     */
    public Object cloneWith(ClassLoader classLoader) {
        Assert.isNotNull(classLoader);
        if (isNull()) return null;
        try {
            return cloner.cloneObjectUsing(classLoader, getUnwrappedSelf());
        } catch (Exception e) {
            throw new TransloaderException("Unable to clone '" + getUnwrappedSelf() + "'.", e);
        }
    }

    /**
     * Invokes on the wrapped object the method described by the given invocation description, with the parameters given
     * by the same. Finds the method reflectively using parameter types loaded from the wrapped object's
     * <code>ClassLoader</code>(s). Any parameters which refer to <code>Class</code>es that are foreign to the
     * wrapped object's <code>ClassLoader</code>(s) are cloned using the <code>CloningStrategy</code> injected at
     * construction.
     *
     * @param description the description of the invocation to be performed
     * @return the result of performing the invocation described by <code>description</code>
     */
    public Object invoke(InvocationDescription description) {
        Assert.isNotNull(description);
        try {
            Class wrappedClass = getUnwrappedSelf().getClass();
            Class[] parameterTypes = ClassWrapper.getClassesFrom(parameterClassLoader, description.getParameterTypeNames());
            Object[] clonedParameters =
                    (Object[]) cloner.cloneObjectUsing(parameterClassLoader, description.getParameters());
            Method method = wrappedClass.getMethod(description.getMethodName(), parameterTypes);
            return method.invoke(getUnwrappedSelf(), clonedParameters);
        } catch (Exception e) {
            // TODO test Exception from invoke
            throw new TransloaderException(
                    "Unable to invoke '" + description.getMethodName() + Arrays.asList(description.getParameterTypeNames()) + "' on '" + getUnwrappedSelf() + "'.",
                    e);
        }
    }

    /**
     * Makes an implementation of the given <code>interface</code> that calls through to the wrapped object. This is
     * particularly useful if you have access in the current <code>ClassLoader</code> to an interface that the wrapped
     * object is expected to implement, except that it actually implements the equivalent from a different
     * <code>ClassLoader</code>. It is therefore usefully employed in conjunction with {@link #isInstanceOf(String)}.
     * <p>
     * This method will <i>not</i> fail fast if the wrapped object does not implement its own <code>ClassLoader</code>'s
     * equivalent of the given <code>interface</code>. Therefore it can be used for "duck"-typing i.e. it will
     * execute the methods on the given <code>interface</code>, expecting them to be there on the wrapped object even
     * if it does not actually <code>implement</code> the interface, and if the methods happen to be there it will work.
     * It can therefore work as a more syntactically elegant alternative to using
     * {@link #invoke(InvocationDescription)}, if desired.
     * </p>
     *
     * @param targetInterface the <code>interface</code> that the returned object can be cast to
     * @return a {@link Proxy} to the wrapped object that implements <code>desiredInterface</code>
     */
    public Object makeCastableTo(Class targetInterface) {
        Assert.isNotNull(targetInterface);
        return Proxy.newProxyInstance(targetInterface.getClassLoader(), new Class[]{targetInterface}, new Invoker());
    }

    private class Invoker implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
			return ObjectWrapper.this.invoke(new InvocationDescription(method, parameters));
		}
	}
}
