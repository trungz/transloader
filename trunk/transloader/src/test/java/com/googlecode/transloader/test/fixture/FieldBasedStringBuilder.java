package com.googlecode.transloader.test.fixture;

import java.lang.reflect.Array;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.googlecode.transloader.clone.FieldDescription;
import com.googlecode.transloader.clone.FieldReflector;
import com.googlecode.transloader.clone.CyclicReferenceSafeTraverser;
import com.googlecode.transloader.clone.CyclicReferenceSafeTraverser.Traversal;

public class FieldBasedStringBuilder {
	private static final String FIELD_SEPERATOR = " ";
	private static final String OPEN_BRACKET = "[" + FIELD_SEPERATOR;
	private static final String CLOSE_BRACKET = "]";
	private static final CyclicReferenceSafeTraverser RECURSIVE_REFERENCE_TRAVERSER = new CyclicReferenceSafeTraverser();

	public static String toString(final Object object) {
		Traversal toStringTraversal = new Traversal() {
			public Object traverse(Object currentObject, Map referenceHistory) throws Exception {
				Class objectClass = object.getClass();
				String className = ClassUtils.getShortClassName(objectClass);
				referenceHistory.put(currentObject, className + "<circular reference>");
				StringBuffer toStringBuffer = new StringBuffer();
				appendClassAndClassLoader(toStringBuffer, objectClass);
				appendFields(object, toStringBuffer);
				return toStringBuffer.toString();
			}
		};
		try {
			return (String) RECURSIVE_REFERENCE_TRAVERSER.performWithoutFollowingCircles(toStringTraversal, object);
		} catch (Exception e) {
			throw new NestableRuntimeException(e);
		}
	}

	private static void appendFields(Object object, StringBuffer toStringBuffer) {
		toStringBuffer.append(OPEN_BRACKET);
		FieldReflector reflector = new FieldReflector(object);
		FieldDescription[] fieldDescriptions = reflector.getAllInstanceFieldDescriptions();
		for (int i = 0; i < fieldDescriptions.length; i++) {
			FieldDescription description = fieldDescriptions[i];
			try {
				toStringBuffer.append(description.getFieldName()).append("=");
				Object fieldValue = reflector.getValue(description);
				if (fieldValue != null && fieldValue.getClass().isArray()) {
					appendArray(toStringBuffer, fieldValue);
				} else {
					toStringBuffer.append(fieldValue);
				}
				toStringBuffer.append(FIELD_SEPERATOR);
			} catch (Exception e) {
				throw new NestableRuntimeException(e);
			}
		}
		toStringBuffer.append(CLOSE_BRACKET);
	}

	private static void appendArray(StringBuffer toStringBuffer, Object array) {
		appendClassAndClassLoader(toStringBuffer, array.getClass());
		toStringBuffer.append(OPEN_BRACKET);
		for (int i = 0; i < Array.getLength(array); i++) {
			toStringBuffer.append(Array.get(array, i)).append(FIELD_SEPERATOR);
		}
		toStringBuffer.append(CLOSE_BRACKET);
	}

	private static void appendClassAndClassLoader(StringBuffer toStringBuffer, Class clazz) {
		toStringBuffer.append(ClassUtils.getShortClassName(clazz)).append("(").append(clazz.getClassLoader()).append(
				")");
	}
}
