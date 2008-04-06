package com.googlecode.transloader.test.fixture.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.googlecode.transloader.test.Triangulate;
import com.googlecode.transloader.test.fixture.NotCommonJavaObject;
import com.googlecode.transloader.test.fixture.NotCommonJavaType;

public class WithListFields extends NotCommonJavaObject {
	List listFromArray =
			Arrays.asList(new NotCommonJavaType[] {new WithPrimitiveFields(),
					new WithStringField(Triangulate.anyString())});
	private List list = new ArrayList();
	{
		list.add(Triangulate.anyString());
		list.add(Triangulate.anyInteger());
		list.add(Triangulate.anyString());
	}
	private List empty = Collections.EMPTY_LIST;
	private List unmodifiable = Collections.unmodifiableList(list);
	private List synchronizedList = Collections.synchronizedList(list);
	private List singelton = Collections.singletonList(Triangulate.anyInteger());
}
