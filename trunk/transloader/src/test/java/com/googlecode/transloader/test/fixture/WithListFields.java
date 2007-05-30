package com.googlecode.transloader.test.fixture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.googlecode.transloader.test.Triangulator;

public class WithListFields extends NonCommonJavaObject {
	List listFromArray =
			Arrays.asList(new NonCommonJavaType[] {new WithPrimitiveFields(),
					new WithStringField(Triangulator.anyString())});
	private List list = new ArrayList();
	{
		list.add(Triangulator.anyString());
		list.add(Triangulator.anyInteger());
		list.add(Triangulator.anyString());
	}
	private List empty = Collections.EMPTY_LIST;
	private List unmodifiable = Collections.unmodifiableList(list);
	private List synchronizedList = Collections.synchronizedList(list);
	private List singelton = Collections.singletonList(Triangulator.anyInteger());
}
