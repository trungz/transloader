package com.googlecode.transloader.clone.reflect;

import com.googlecode.transloader.reference.Reference;

import java.util.Iterator;

/**
 * @author Jeremy Wales
 */
interface CloneMapper {
    Iterator iterateOriginals();

    Object getTransformationOf(Object original);

    Reference[] getReferencesFrom(Object original);
}
