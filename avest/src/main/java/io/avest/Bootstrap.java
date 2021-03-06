package io.avest;

public interface Bootstrap {

    void registerObject(Object obj, boolean withView);

    /**
     * default inject with view's value: false
     * @param obj
     */
    void registerObject(Object obj);

    boolean resolveDependencies(Object obj);

    <T> void unregisterClass(Class<T> clazz);

    <T> T getVest(String name, Class<T> clazz);

    <T> T getVest(Class<T> clazz);

    void destroy();
}
