package com.googlecode.transloader.load;

/**
 * A <code>ClassLoader</code> which only loads <code>Class</code>es from the virtual machine's class loader.
 */
public final class BootClassLoader extends ClassLoader {
    /**
     * An instance of <code>BootClassLoader</code> which can be shared.
     */
    public static final BootClassLoader INSTANCE = new BootClassLoader();

    /**
     * Constructs a new <code>BootClassLoader</code> which has <code>null</code> as its parent <code>ClassLoader</code>.
     *
     * @see ClassLoader#ClassLoader(ClassLoader)
     * @see ClassLoader#loadClass(String)
     */
    private BootClassLoader() {
        super(null);
    }
}
