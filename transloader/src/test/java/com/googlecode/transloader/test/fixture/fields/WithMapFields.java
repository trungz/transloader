package com.googlecode.transloader.test.fixture.fields;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.serializable.WithFinalFields;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WithMapFields extends NotCommonJavaObject {
    private SortedMap map = new TreeMap();

    {
        map.put(Triangulate.anyInteger(), Triangulate.anyString());
        map.put(Triangulate.anyInteger(), Triangulate.anyString());
    }

    private Map unmodifiable = Collections.unmodifiableMap(map);
    private Map synchronizedMap = Collections.synchronizedSortedMap(map);
    private Map singleton = Collections.singletonMap(Triangulate.anyString(), Triangulate.anyInteger());
}
