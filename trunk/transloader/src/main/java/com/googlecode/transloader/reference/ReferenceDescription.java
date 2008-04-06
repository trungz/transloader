package com.googlecode.transloader.reference;

public interface ReferenceDescription {
    Object getValueFrom(Object referer) throws NoSuchFieldException, IllegalAccessException;

    void setValueIn(Object referer, Object value) throws NoSuchFieldException, IllegalAccessException;

    /**
     * Indicates whether or not the reference is to a primitive rather than an {@link Object}.
     *
     * @return <code>true</code> if the reference is of primitive type
     */
    boolean isOfPrimitiveType();

    String getName();
}
