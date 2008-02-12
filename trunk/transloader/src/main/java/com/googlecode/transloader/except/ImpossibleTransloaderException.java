package com.googlecode.transloader.except;

/**
 * The <code>TransloaderException</code> thrown only in circumstances that should never occur e.g. wrapping checked
 * <code>Exception</code>s, the handling of which is enforced by the compiler, but which can never actually be thrown in
 * the relevant context.
 *
 * @author Jeremy Wales
 */
public class ImpossibleTransloaderException extends TransloaderException {
    /**
     * Constructs a new <code>ImpossibleTransloaderException</code> with the given nested <code>Throwable</code>.
     *
     * @param cause the <code>Exception</code> that caused this one to be thrown
     */
    public ImpossibleTransloaderException(Exception cause) {
        super("This should NEVER happen!", cause);
    }
}
