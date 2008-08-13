package com.googlecode.transloader.primitive;

import com.googlecode.transloader.except.Assert;
import org.apache.commons.lang.ClassUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectStreamField;

/**
 * @author jeremywales
 */
public final class WrapperConverter {
    private static final String IRRELEVANT = "irrelevant";

    public byte[] asBytes(Object wrapper) throws IOException {
        Assert.isNotNull(wrapper);
        Assert.contains(wrapper.getClass(), Wrapper.LIST);
        return convert(wrapper);
    }

    private static byte[] convert(Object wrapper) throws IOException {
        ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(arrayStream);
        write(wrapper, dataStream);
        return arrayStream.toByteArray();
    }

    private static void write(Object wrapper, DataOutputStream dataStream) throws IOException {
        char typeCode = getTypeCode(wrapper);
        switch (typeCode) {
            case 'B':
                dataStream.writeByte(intValue(wrapper));
                break;
            case 'C':
                dataStream.writeChar(charValue(wrapper));
                break;
            case 'S':
                dataStream.writeShort(intValue(wrapper));
                break;
            case 'I':
                dataStream.writeInt(intValue(wrapper));
                break;
            case 'J':
                dataStream.writeLong(number(wrapper).longValue());
                break;
            case 'F':
                dataStream.writeFloat(number(wrapper).floatValue());
                break;
            case 'D':
                dataStream.writeDouble(number(wrapper).doubleValue());
                break;
            case 'Z':
                dataStream.writeBoolean(booleanValue(wrapper));
        }
    }

    private static char charValue(Object wrapper) {
        return ((Character) wrapper).charValue();
    }

    private static char getTypeCode(Object wrapper) {
        Class primitiveType = ClassUtils.wrapperToPrimitive(wrapper.getClass());
        ObjectStreamField typeCodeProvider = new ObjectStreamField(IRRELEVANT, primitiveType);
        return typeCodeProvider.getTypeCode();
    }

    private static Number number(Object wrapper) {
        return ((Number) wrapper);
    }

    private static int intValue(Object wrapper) {
        return number(wrapper).intValue();
    }

    private static boolean booleanValue(Object wrapper) {
        return Boolean.valueOf(wrapper.toString()).booleanValue();
    }
}
