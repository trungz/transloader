package com.googlecode.transloader.clone.reflect;

import java.util.Map;

import org.apache.commons.collections.map.IdentityMap;

/**
 * A utility for traversing through entire object graphs which may contain cyclic references, without thereby entering
 * an endless loop. It is thread safe also.
 * 
 * @author Jeremy Wales
 */
public final class CyclicReferenceSafeTraverser {
	private ThreadLocal referenceHistoryForThread = new ThreadLocal();

	/**
	 * Executes the given the traversal over the current location in the object graph if it has not already been
	 * traversed in the current journey through the graph.
	 * 
	 * @param traversal the traversal to execute
	 * @param currentObjectInGraph the location in the object graph over which to perform the traversal
	 * @return the result of performing the traversal
	 * @throws Exception can throw any <code>Exception</code> from the traversal itself
	 */
	public Object performWithoutFollowingCircles(Traversal traversal, Object currentObjectInGraph) throws Exception {
		ReferenceHistory referenceHistory = getReferenceHistory();
		if (referenceHistory.containsKey(currentObjectInGraph)) return referenceHistory.get(currentObjectInGraph);
		referenceHistory.put(currentObjectInGraph, null);
		Object result = traversal.traverse(currentObjectInGraph, referenceHistory);
		referenceHistory.privatelyRemove(currentObjectInGraph);
		return result;
	}

	private ReferenceHistory getReferenceHistory() {
		ReferenceHistory referenceHistory = (ReferenceHistory) referenceHistoryForThread.get();
		if (referenceHistory == null) {
			referenceHistoryForThread.set(referenceHistory = new ReferenceHistory());
		}
		return referenceHistory;
	}

	/**
	 * The callback interface for implementing a traversal over an object graph.
	 * 
	 * @author Jeremy Wales
	 */
	public static interface Traversal {
		/**
		 * Performs some logic on the given current location in the object graph. May update the given reference history
		 * to associate the given current object with the result of traversing it so that can be used next time the same
		 * object is encountered in the same journey over the graph.
		 * 
		 * @param currentObjectInGraph the location in the object graph over which to perform the traversal
		 * @param referenceHistory the history of objects already traversed and the results of traversing them
		 * @return the result of traversing <code>currentObjectInGraph</code>
		 * @throws Exception can throw any <code>Exception</code> depending on the implementation
		 */
		Object traverse(Object currentObjectInGraph, Map referenceHistory) throws Exception;
	}

	// TODO don't use Map interface externally for this but rather ReferenceHistory interface with just put(key, value)
	private static final class ReferenceHistory extends IdentityMap {
		private static final long serialVersionUID = 7800649333760581678L;

		public Object remove(Object key) {
			// TODO test this bit
			throw new UnsupportedOperationException(
					"Remove must not be called on a ReferenceHistory except by CyclicReferenceSafeTraverser. Other classes may only add to it.");
		}

		private Object privatelyRemove(Object key) {
			return super.remove(key);
		}
	}
}
