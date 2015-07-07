package com.topolyai.avest;

public interface Bootstrap {

    void registerObject(Object obj, boolean withView);

    <T> T getVest(String name, Class<T> clazz);

    <T> T getVest(Class<T> clazz);
}
