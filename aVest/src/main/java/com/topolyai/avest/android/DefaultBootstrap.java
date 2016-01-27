package com.topolyai.avest.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.topolyai.avest.Bootstrap;
import com.topolyai.avest.BootstrapNotInitializedException;
import com.topolyai.avest.FailedToInitializationException;
import com.topolyai.avest.annotations.Component;
import com.topolyai.avest.annotations.Configuration;
import com.topolyai.avest.annotations.EmbeddedLayout;
import com.topolyai.avest.annotations.Inject;
import com.topolyai.avest.annotations.InjectView;
import com.topolyai.avest.annotations.Layout;
import com.topolyai.avest.annotations.PostConstruct;
import com.topolyai.avest.annotations.Prototype;
import com.topolyai.avest.annotations.ScreenElement;
import com.topolyai.avest.annotations.Vest;
import com.topolyai.vlogger.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dalvik.system.DexFile;

class DefaultBootstrap implements Bootstrap {

    private static Logger LOGGER = Logger.get(DefaultBootstrap.class);

    Map<String, Object> objs = new HashMap<>();
    Set<Class> prototypes = new HashSet<>();
    List<Object> configs = new ArrayList<>();
    List<Object> screenElements = new ArrayList<>();

    private Context context;

    private static DefaultBootstrap inst;

    public static Bootstrap create(Context context) {
        if (inst == null) {
            inst = new DefaultBootstrap();
            inst.onCreate(context);
        } else if (inst.context.equals(context)) {
            //ignore
        } else {
            LOGGER.d("New context was registered. {}", context.toString());
            inst.registerObject(context, true);
        }
        return inst;
    }

    public static Bootstrap get() {
        if (inst == null) {
            throw new BootstrapNotInitializedException();
        }
        return inst;
    }

    public void onCreate(Context context) {
        LOGGER.d("New context was started.");
        long start = System.currentTimeMillis();
        try {
            this.context = context;
            objs.put(Context.class.getName(), context);
            createObjects();
            fetchObjFromConfig();
            fetchSystemServices();
            resolveAwares();
            injectDependecies();
            activity(context);
            screenElements();
            postConstructs();
        } catch (Exception e) {
            LOGGER.e(e.getMessage(), e);
            throw new FailedToInitializationException(e);
        }
        long end = System.currentTimeMillis();
        LOGGER.i("inited: {} ms.", (end - start));
    }

    private void resolveAwares() {
        for (Object o : objs.values()) {
            if (o instanceof BootsrapAware) {
                ((BootsrapAware) o).setBootstrap(this);
            }
        }
    }

    private void screenElements() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException,
            NoSuchMethodException, InvocationTargetException {
        for (Object element : screenElements) {
            injectViewOnScreenElement(element);
        }
        screenElements.clear();
    }

    private void injectViewOnScreenElement(Object element) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        LOGGER.d("Inject views on screen element: {}", element.getClass().getName());
        Field[] fields = element.getClass().getDeclaredFields();

        List<Field> injectView = new ArrayList<>();
        View layout = null;

        for (Field field : fields) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation.annotationType().equals(Layout.class)) {
                    if (layout != null) {
                        throw new IllegalArgumentException("Cannot define two layouts!");
                    }
                    Activity activity = (Activity) context;
                    layout = activity.findViewById(field.getAnnotation(Layout.class).value());
                    setFieldValue(element, field, layout);
                } else if (annotation.annotationType().equals(InjectView.class)) {
                    if (layout != null) {
                        findViewOnLayoutAndInject(element, layout, field);
                    } else {
                        injectView.add(field);
                    }
                } else if (annotation.annotationType().equals(EmbeddedLayout.class)) {
                    View embeddedLayout = LayoutInflater.from(context)
                            .inflate(field.getAnnotation(EmbeddedLayout.class).value(), null);
                    setFieldValue(element, field, embeddedLayout);
                }
            }
        }

        if (layout == null && (element instanceof Activity)) {
            layout = ((Activity) element).findViewById(android.R.id.content);
        }
        if (layout == null) {
            Layout annotation = element.getClass().getAnnotation(Layout.class);
            if (annotation != null) {
                Method method = element.getClass().getMethod("getLayout");
                layout = (View) method.invoke(element);

            }
        }
        if (layout == null && !injectView.isEmpty()) {
            LOGGER.w("Layout is not defined in " + element.getClass().getName());
        }
        for (Field field : injectView) {
            findViewOnLayoutAndInject(element, layout, field);
        }

    }

    private void findViewOnLayoutAndInject(Object element, View layout, Field field) throws NoSuchFieldException,
            IllegalAccessException {
        LOGGER.d("Resolve views on element: {}", element.getClass().getName());
        InjectView annotation = field.getAnnotation(InjectView.class);
        View embeddedLayout = layout;
        if (!TextUtils.isEmpty(annotation.layout())) {
            Field f = element.getClass().getDeclaredField(annotation.layout());
            f.setAccessible(true);
            embeddedLayout = (View) f.get(element);
        }
        View view = embeddedLayout.findViewById(annotation.value());
        setFieldValue(element, field, view);
    }

    private void fetchObjFromConfig() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ClassNotFoundException {
        LOGGER.d("Fetch obj from configuration...");
        for (Object config : configs) {
            internalResolveDependencies(config);
            Class<?> clazz = config.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Vest vest = field.getAnnotation(Vest.class);
                if (vest != null) {
                    Class<?> type = field.getType();
                    objs.put(type.getName(), type.newInstance());
                }
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                Vest vest = method.getAnnotation(Vest.class);
                if (vest != null) {
                    Class<?> type = method.getReturnType();
                    Object invoke = method.invoke(config);
                    objs.put(type.getName(), invoke);
                }
            }
        }

    }

    private void fetchSystemServices() {
        LOGGER.d("Fetch system services...");
        objs.put(LocationManager.class.getName(), context.getSystemService(Context.LOCATION_SERVICE));
        objs.put(LayoutInflater.class.getName(), context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        objs.put(WifiManager.class.getName(), context.getSystemService(Context.WIFI_SERVICE));
        objs.put(WindowManager.class.getName(), context.getSystemService(Context.WINDOW_SERVICE));
        objs.put(ActivityManager.class.getName(), context.getSystemService(Context.ACTIVITY_SERVICE));
        objs.put(PowerManager.class.getName(), context.getSystemService(Context.POWER_SERVICE));
        objs.put(AlarmManager.class.getName(), context.getSystemService(Context.ALARM_SERVICE));
        objs.put(NotificationManager.class.getName(), context.getSystemService(Context.NOTIFICATION_SERVICE));
        objs.put(KeyguardManager.class.getName(), context.getSystemService(Context.KEYGUARD_SERVICE));
        objs.put(SearchManager.class.getName(), context.getSystemService(Context.SEARCH_SERVICE));
        objs.put(Vibrator.class.getName(), context.getSystemService(Context.VIBRATOR_SERVICE));
        objs.put(ConnectivityManager.class.getName(), context.getSystemService(Context.CONNECTIVITY_SERVICE));
        objs.put(InputMethodManager.class.getName(), context.getSystemService(Context.INPUT_METHOD_SERVICE));
        objs.put(UiModeManager.class.getName(), context.getSystemService(Context.UI_MODE_SERVICE));
        objs.put(BluetoothManager.class.getName(), context.getSystemService(Context.BLUETOOTH_SERVICE));
        // objs.put(DownloadManager.class.getName(),
        // context.getSystemService(DOWNLOAD_SERVICE));

    }

    private void postConstructs() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException, InstantiationException {
        LOGGER.d("Postconstructs...");
        //clone for temp. if new objects are created in post construct, need resolve the dependencies in these objects
        Map<String, Object> l = new HashMap<>();
        for (Entry<String, Object> object : objs.entrySet()) {
            l.put(object.getKey(), object.getValue());
        }
        objs.clear();
        for (Object obj : l.values()) {
            invokePostConstruct(obj);
        }

        //if found a new object clone these as well
        Map<String, Object> l2 = new HashMap<>();
        if (!objs.isEmpty()) {
            for (Entry<String, Object> object : objs.entrySet()) {
                l2.put(object.getKey(), object.getValue());
            }
        }

        //copy the original object back to the map
        for (Entry<String, Object> entry : l.entrySet()) {
            objs.put(entry.getKey(), entry.getValue());
        }

        if (!l2.isEmpty()) {
            for (Object object : l2.values()) {
                internalResolveDependencies(object);
                invokePostConstruct(object);
            }
        }
    }

    public void invokePostConstruct(Object obj) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PostConstruct annotation = method.getAnnotation(PostConstruct.class);
            if (annotation != null) {
                method.setAccessible(true);
                method.invoke(obj);
            }
        }
    }

    private void injectDependecies() throws IllegalAccessException, IllegalArgumentException, ClassNotFoundException, InstantiationException {
        for (Object obj : objs.values()) {
            if (obj.getClass().getAnnotation(Component.class) != null) {
                resolveDependenciesInSuperClass(obj);
            } else {
                internalResolveDependencies(obj);
            }
        }
    }

    private void resolveDependenciesInSuperClass(Object o) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        walkOnFields(o, getAllFields(o.getClass()));
    }

    private Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        if (!clazz.getSuperclass().isInstance(Object.class)) {
            fields = concat(fields, getAllFields(clazz.getSuperclass()));
        }

        return fields;

    }

    public <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private void internalResolveDependencies(Object obj) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        LOGGER.d("Resolve dependencies in obj: {}", obj.getClass().getName());
        Field[] fields = obj.getClass().getDeclaredFields();
        walkOnFields(obj, fields);
    }

    private void walkOnFields(Object obj, Field[] fields) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Field field : fields) {
            Inject annotation = field.getAnnotation(Inject.class);
            if (annotation != null) {
                Class<?> type = field.getType();
                if (type.equals(List.class)) {
                    field.setAccessible(true);
                    if (annotation.value() == null) {
                        throw new IllegalArgumentException("Super class should be set.");
                    }
                    field.set(obj, findAllAsSuperClass(annotation.value()));
                } else if (type.equals(Map.class)) {
                    field.setAccessible(true);
                    if (annotation.value() == null) {
                        throw new IllegalArgumentException("Super class should be set.");
                    }
                    field.set(obj, findAllAsSuperClassInMap(annotation.value()));
                } else if (prototypes.contains(type)) {
                    field.set(obj, resolveDependencies(type.newInstance()));
                } else {
                    Object object = null;
                    if (!annotation.value().equals(Object.class)) {
                        object = objs.get(annotation.value().getName());
                    }
                    if (object == null) {
                        if (type.isInstance(context)) {
                            type = Class.forName("android.content.Context");
                            object = objs.get(type.getName());
                        } else if (FragmentManager.class.equals(type) && context instanceof VestFragmentActivity) {
                            object = ((VestFragmentActivity) context).getSupportFragmentManager();
                        } else {
                            object = objs.get(type.getName());
                        }
                    }

                    if (object == null) {
                        object = findAsSuperClass(type);
                    }

                    field.setAccessible(true);
                    field.set(obj, object);
                }
            }
        }
    }

    private Object findAsSuperClass(Class<?> type) {
        for (Object o : objs.values()) {
            if (type.isInstance(o)) {
                return o;
            }
        }
        return null;
    }

    private List<Object> findAllAsSuperClass(Class<?> type) {
        List<Object> ret = new ArrayList<>();
        for (Object o : objs.values()) {
            if (type.isInstance(o)) {
                ret.add(o);
            }
        }
        return ret;
    }

    private Map<Class<?>, Object> findAllAsSuperClassInMap(Class<?> type) {
        Map<Class<?>, Object> ret = new HashMap<>();
        for (Object o : objs.values()) {
            if (type.isInstance(o)) {
                ret.put(o.getClass(), o);
            }
        }
        return ret;
    }

    private void createObjects() {
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String s = iter.nextElement();
                if (s.startsWith(getAppPackageName())) {
                    Class<?> class1 = null;
                    try {
                        class1 = Class.forName(s);
                    } catch (ClassNotFoundException e) {
                        LOGGER.w("class is not loaded: " + s);
                        class1 = df.loadClass(s, Thread.currentThread().getContextClassLoader());
                    }
                    for (Annotation ann : class1.getAnnotations()) {
                        if (ann.annotationType().equals(Component.class)) {
                            Object o = class1.newInstance();
                            objs.put(class1.getName(), o);
                        } else if (ann.annotationType().equals(Configuration.class)) {
                            configs.add(class1.newInstance());
                        } else if (ann.annotationType().equals(ScreenElement.class)) {
                            Object o = class1.newInstance();
                            objs.put(class1.getName(), o);
                            screenElements.add(o);
                        } else if (ann.annotationType().equals(Prototype.class)) {
                            prototypes.add(class1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.e("Error", e);
        }
    }

    protected String getAppPackageName() {
        return context.getPackageName();
    }

    public void registerObject(Object o, boolean withView) {
        LOGGER.d("New obj was registered. Obj: {}, with view: {}", o.getClass().getName(), withView);
        try {
            objs.put(o.getClass().getName(), o);
            resolveDependenciesInSuperClass(o);
            injectObjectWhereDependency(o);
            if (withView) {
                injectViews(o);
            }
            invokePostConstruct(o);
        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
            LOGGER.e(e.getMessage(), e);
        }
    }

    @Override
    public void registerObject(Object obj) {
        registerObject(obj, false);
    }

    @Override
    public boolean resolveDependencies(Object obj) {
        try {
            internalResolveDependencies(obj);
            return true;
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            LOGGER.e(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public <T> void unregisterClass(Class<T> clazz) {
        objs.remove(clazz.getName());
    }

    private void injectObjectWhereDependency(Object o) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        for (Object entry : objs.values()) {
            for (Field field : entry.getClass().getDeclaredFields()) {
                if (field.getAnnotation(Inject.class) != null && field.getType().isInstance(o)) {
                    setFieldValue(entry, field, o);
                }
            }
        }
    }

    public void injectViews(Object o) {
        try {
            activity(o);
            injectViewOnScreenElement(o);
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.e(e.getMessage(), e);
        }
    }

    private void activity(Object o) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchFieldException {
        Class<?> clazz = o.getClass();
        Layout layout = clazz.getAnnotation(Layout.class);
        if (layout != null) {
            setLayout(o, clazz, layout);
            screenElements.add(o);
        } else {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                layout = field.getAnnotation(Layout.class);
                if (layout != null) {
                    setLayout(o, clazz, layout);
                    screenElements.add(o);
                }
            }
        }
    }

    private void setLayout(Object o, Class<?> clazz, Layout layout) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, NoSuchFieldException {
        if (o instanceof Activity) {
            Method method = clazz.getMethod("setContentView", int.class);
            method.invoke(o, layout.value());
        } else if (o instanceof DialogFragment) {
            LayoutInflater inflater = (LayoutInflater) objs.get(LayoutInflater.class.getName());
            View view = inflater.inflate(layout.value(), null);
            setFieldValue(o, VestDialogFragment.class.getDeclaredField("layout"), view);
        } else if (o instanceof Fragment) {
            LayoutInflater inflater = (LayoutInflater) objs.get(LayoutInflater.class.getName());
            View view = inflater.inflate(layout.value(), null);
            setFieldValue(o, VestFragment.class.getDeclaredField("layout"), view);
        }
    }

    public void setFieldValue(Object o, Field field, Object value) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        field.setAccessible(true);
        field.set(o, value);
    }

    @Override
    public <T> T getVest(String name, Class<T> clazz) {
        return clazz.cast(objs.get(name));
    }

    @Override
    public <T> T getVest(Class<T> clazz) {
        return clazz.cast(objs.get(clazz.getName()));
    }

    @Override
    public void destroy() {
        inst = null;
        objs.clear();
        screenElements.clear();
        configs.clear();
        prototypes.clear();

        objs = null;
        screenElements = null;
        configs = null;
    }

}