package com.googlecode.transloader.configure;

import com.googlecode.transloader.reference.DefaultReflecter;
import com.googlecode.transloader.reference.ReferenceReflecter;
import com.googlecode.transloader.reference.field.FieldSetter;
import com.googlecode.transloader.reference.field.SerializationSetter;
import com.googlecode.transloader.reference.field.SimpleSetter;

/**
 * @author jeremywales
 */
public final class ReflecterFactory {
    public static final ReferenceReflecter DEFAULT;

    // TODO revisit FieldSetter decision
    static {
        FieldSetter setter;
        try {
            setter = new SerializationSetter();
        } catch (Exception e) {
            setter = new SimpleSetter();
        }
        DEFAULT = new DefaultReflecter(setter);
    }

    private ReflecterFactory() {
    }
}
