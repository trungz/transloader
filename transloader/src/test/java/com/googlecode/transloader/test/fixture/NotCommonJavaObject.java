package com.googlecode.transloader.test.fixture;

import com.googlecode.transloader.test.ClassLoaderAndReferencesStringBuilder;

public class NotCommonJavaObject implements NotCommonJavaType {
    public String toString() {
        return ClassLoaderAndReferencesStringBuilder.toString(this);
    }
}
