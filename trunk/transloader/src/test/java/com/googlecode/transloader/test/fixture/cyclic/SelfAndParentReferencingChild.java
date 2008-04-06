package com.googlecode.transloader.test.fixture.cyclic;

import com.googlecode.transloader.test.fixture.fields.WithStringField;

public class SelfAndParentReferencingChild extends WithStringField {
    private Object self = this;
    private SelfAndChildReferencingParent parent;

    public SelfAndParentReferencingChild(String stringFieldValue, SelfAndChildReferencingParent parent) {
        super(stringFieldValue);
        this.parent = parent;
        parent.setChild(this);
    }
}
