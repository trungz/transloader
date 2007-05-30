package com.googlecode.transloader.test.fixture;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.transloader.test.Triangulator;

public class WithMapFields extends NonCommonJavaObject {
	private SortedMap map = new TreeMap();
	{
		map.put(Triangulator.anyInteger(), Triangulator.anyString());
		map.put(Triangulator.anyInteger(), new SerializableWithFinalFields(Triangulator.anyInteger()));
	}

	private Map unmodifiable = Collections.unmodifiableMap(map);
	private Map synchronizedMap = Collections.synchronizedSortedMap(map);
	private Map singleton = Collections.singletonMap(Triangulator.anyInteger(), Triangulator.anyString());
}
