package com.wire.picklejar.scan;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleAnnotationSeeker {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(PickleAnnotationSeeker.class.getSimpleName());

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

    public static Collection<Class> getAnnotationClassesFromPackage(final String[] pkgs) throws IOException {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().
                getStandardFileManager(null, null, null);
        Set<Class> classes = new LinkedHashSet<>();
        for (String pkg : pkgs) {
            LOG.debug(String.format("Loading annotation classes from %s...", pkg));
            final Spliterator<JavaFileObject> classesSpliterator = fileManager.list(StandardLocation.CLASS_PATH, pkg,
                    Collections.
                            singleton(JavaFileObject.Kind.CLASS), true).spliterator();
            classes.addAll(StreamSupport.stream(classesSpliterator, false)
                    .map(javaFileObject -> {
                        try {
                            LOG.trace("Processing class: {}", javaFileObject.getName());
                            final String fullClassName = javaFileObject.getName()
                                    .replace(".class", "")
                                    .replace(")", "")
                                    .replaceAll(Pattern.quote(File.separator), ".")
                                    .replaceAll(Pattern.quote("/"), ".")
                                    .replaceAll("(.*)" + pkg, pkg);
                            LOG.trace("Loading class: {}", fullClassName);
                            return Class.forName(fullClassName);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter((c) -> {
                        LOG.debug("Class '{}' is annotation '{}'", c.getName(), c.isAnnotation());
                        return c.isAnnotation();
                    })
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        return classes;
    }

    public static boolean isClassWithAnnotation(Class clazz, String annotationQualifiedName) throws ClassNotFoundException {
        Class<?> annotationClass = Class.forName(annotationQualifiedName);
        if (!annotationClass.isAnnotation()) {
            return false;
        } else {
            return clazz.isAnnotationPresent((Class<Annotation>) annotationClass);
        }
    }
}
