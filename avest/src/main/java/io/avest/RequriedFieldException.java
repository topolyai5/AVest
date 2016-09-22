package io.avest;

import java.lang.reflect.Field;

public class RequriedFieldException extends RuntimeException {
    public RequriedFieldException(Class clss, Field field) {
        super("Field is required: " + clss.getName() + "." + field.getName());
    }
}
