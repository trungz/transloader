package com.googlecode.transloader.test;

import com.googlecode.transloader.except.Assert;
import org.apache.commons.collections.map.IdentityMap;

import java.util.Map;

/**
 * <p>
 * A utility employed as part of traversing through object graphs which may contain cyclic references.
 * By making each operation pass through an instance of this class, a client will ensure that cyclic references in the
 * object graph they are traversing, which would otherwise be operated on over and over, are tracked so that they do not
 * result in infinite loops.
 * </p>
 * The implementation is thread-safe.
 *
 * @author Jeremy Wales
 */
public final class CyclicReferenceSafeTraverser {
    private final ThreadLocal referenceHistoryForThread = new ThreadLocal();

    /**
     * Executes the given operation on the current location in the object graph if it has not already been
     * traversed in the current journey through the graph.
     *
     * @param currentObjectInGraph the location in the object graph over which to perform the operation
     * @param operation            the operation to execute
     * @return the result of performing the operation
     * @throws Exception can throw any <code>Exception</code> from the operation itself
     */
    public Object performOperationWithoutLoopingOn(Object currentObjectInGraph, Operation operation) throws Exception {
        Assert.areNotNull(currentObjectInGraph, operation);
        Map referenceHistory = getReferenceHistory();
        if (referenceHistory.containsKey(currentObjectInGraph)) return referenceHistory.get(currentObjectInGraph);
        referenceHistory.put(currentObjectInGraph, null);
        Object result = operation.performOn(currentObjectInGraph, referenceHistory);
        // TODO make it so removals can only happen here
        // TODO put the removal in a finally block to prevent memory leaks following Exceptions
        referenceHistory.remove(currentObjectInGraph);
        return result;
    }

    private Map getReferenceHistory() {
        Map referenceHistory = (Map) referenceHistoryForThread.get();
        if (referenceHistory == null) {
            referenceHistoryForThread.set(referenceHistory = new IdentityMap());
        }
        return referenceHistory;
    }

    /**
     * The callback interface by which is implemented the operation to be performed on the object given to
     * {@link CyclicReferenceSafeTraverser#performOperationWithoutLoopingOn(Object, Operation)}.
     *
     * @author Jeremy Wales
     */
    public static interface Operation {
        /**
         * Performs an operation on the given current location in the object graph. May update the given reference
         * history to associate the given current object with the result of operating on it so that the result can be
         * reused next time the same object is encountered in the same journey over the graph.
         *
         * @param currentObjectInGraph the location in the object graph over which to perform the traversal
         * @param referenceHistory     the history of objects already traversed and the results of traversing them
         * @return the result of traversing <code>currentObjectInGraph</code>
         * @throws Exception can throw any <code>Exception</code> depending on the implementation
         */
        Object performOn(Object currentObjectInGraph, Map referenceHistory) throws Exception;
    }
}
