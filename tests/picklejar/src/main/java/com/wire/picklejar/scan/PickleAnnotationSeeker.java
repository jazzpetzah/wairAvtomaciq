package com.wire.picklejar.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PickleAnnotationSeeker {
    
    private static final Logger LOG = LogManager.getLogger();

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                // abort if Object class, an interface, a primitive type, or void
                break;
            }
        }
        return methods;
    }

}
