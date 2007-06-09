package com.googlecode.transloader.clone;

import java.util.Map;

import org.apache.commons.collections.map.IdentityMap;

public final class CyclicReferenceSafeTraverser {
	private ThreadLocal referenceHistoryForThread = new ThreadLocal();

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

	public static interface Traversal {
		Object traverse(Object currentObjectInGraph, Map referenceHistory) throws Exception;
	}

	// TODO don't use Map interface externally for this but rather ReferenceHistory interface with just put(key, value)
	private static final class ReferenceHistory extends IdentityMap {
		private static final long serialVersionUID = 7800649333760581678L;

		public Object remove(Object key) {
			// TODO test this bit
			throw new UnsupportedOperationException(
					"Remove must not be called on a ReferenceHistory except by RecursiveReferenceTraverser. Other classes may only add to it.");
		}

		private Object privatelyRemove(Object key) {
			return super.remove(key);
		}
	}
}
