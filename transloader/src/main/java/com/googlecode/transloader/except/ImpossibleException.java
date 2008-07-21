package com.googlecode.transloader.except;

/**
 * The <code>RuntimeException</code> thrown only in circumstances that should never occur e.g. wrapping checked
 * <code>Exception</code>s, the handling of which is enforced by the compiler, but which can never actually be thrown in
 * the relevant context.
 *
 * @author Jeremy Wales
 */
public final class ImpossibleException extends TransloaderException {
    /**
     * Constructs a new <code>ImpossibleException</code> with the given nested <code>Exception</code>.
     *
     * @param cause the throwable which caused this one to be thrown
     */
    public ImpossibleException(Exception cause) {
        super("This should NEVER happen!", cause);
    }
}
