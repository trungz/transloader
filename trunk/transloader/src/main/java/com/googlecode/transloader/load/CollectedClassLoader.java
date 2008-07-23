package com.googlecode.transloader.load;

import com.googlecode.transloader.ClassWrapper;
import com.googlecode.transloader.except.TransloaderException;
import com.googlecode.transloader.reference.Reference;
import com.googlecode.transloader.reference.ReferenceReflecter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO test-drive this spiked CollectedClassLoader
public class CollectedClassLoader extends ClassLoader {
    private final List classLoaders = new ArrayList();
    private final Set alreadyVisited = new HashSet();

    public CollectedClassLoader(Object objectGraph) {
        super(BootClassLoader.INSTANCE);
        try {
            collectClassLoadersFrom(objectGraph);
        } catch (IllegalAccessException e) {
            throw new TransloaderException("Cannot create CollectedClassLoader for '" + objectGraph + "'.", e);
        }
        alreadyVisited.clear();
    }

    protected Class findClass(String name) throws ClassNotFoundException {
        ClassNotFoundException finalException = null;
        for (int i = 0; i < classLoaders.size(); i++) {
            ClassLoader classLoader = (ClassLoader) classLoaders.get(i);
            try {
                return classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                finalException = e;
            }
        }
        throw finalException;
    }

    private void collectClassLoadersFrom(Object currentObjectInGraph) throws IllegalAccessException {
        if (shouldCollectFrom(currentObjectInGraph)) {
            collectFrom(currentObjectInGraph);
            Reference[] references = ReferenceReflecter.wrap(currentObjectInGraph).getAllReferences();
            for (int i = 0; i < references.length; i++)
                collectClassLoadersFrom(references[i].getValue());
        }
    }

    private boolean shouldCollectFrom(Object object) {
        boolean notNull = object != null;
        boolean notAlreadyVisited = !alreadyVisited.contains(object);
        return notNull && notAlreadyVisited;
    }

    private void collectFrom(Object object) {
        alreadyVisited.add(object);
        ClassLoader classLoader = ClassWrapper.getClassLoaderFrom(object);
        if (!classLoaders.contains(classLoader))
            classLoaders.add(classLoader);
    }
}
