package com.googlecode.transloader;

/**
 * A marker interface which indicates that the implementing <code>Class</code> accepts <code>null</code> for method
 * parameters. The convention within Transloader for all <code>Class</code>es which do <b>not</b> implement this
 * <code>interface</code> is to use {@link Assert} which will throw an {@link IllegalArgumentException} for any
 * parameter which is <code>null</code>.
 * 
 * @author Jeremy Wales
 */
public interface AcceptsNullMethodParams {
}
