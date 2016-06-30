package io.avest.util;

import java.lang.reflect.Field;

public class FieldUtil {

    public void setFieldValue(Object o, Field field, Object value) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        field.setAccessible(true);
        field.set(o, value);
    }
}
