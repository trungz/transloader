package com.googlecode.transloader.reference;

import com.googlecode.transloader.except.Assert;

public final class Reference {
    public static final Object NULL = new Object() {
        public String toString() {
            return "null";
        }
    };

    private final ReferenceDescription description;
    private final Object value;

    public Reference(ReferenceDescription description, Object value) {
        Assert.areNotNull(description, value);
        this.description = description;
        this.value = value;
    }

    public ReferenceDescription getDescription() {
        return description;
    }

    public Object getValue() {
        return value;
    }
}
