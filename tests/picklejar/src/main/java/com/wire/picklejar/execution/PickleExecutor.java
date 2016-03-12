package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import com.wire.picklejar.scan.PickleAnnotationSeeker;
import com.wire.picklejar.scan.JavaSeeker;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PickleExecutor {
    
    private static final Logger LOG = LogManager.getLogger();

    private static final String CUCUMBER_ANNOTATION_REGEX_METHOD_NAME = "value";
    private static final Class<? extends Annotation>[] CUCUMBER_STEP_ANNOTATIONS = new Class[]{
        When.class, Then.class, And.class, Given.class
    };

    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    private final Map<String, Object> classInstanceCache = new ConcurrentHashMap<>();

    public PickleExecutor() {
        try {
            Collection<Class> loadedClasses = JavaSeeker.getClasses(Config.STEP_PACKAGE);
            for (Class<?> loadedClass : loadedClasses) {
                for (Class<? extends Annotation> annotationClass : CUCUMBER_STEP_ANNOTATIONS) {
                    cacheMethodsByAnnotationValue(loadedClass, annotationClass);
                }
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | InstantiationException ex) {
            LOG.log(Level.ERROR, "Could not load step classes and cache step methods", ex);
        }
    }

    private void cacheMethodsByAnnotationValue(Class<?> loadedClass, Class<? extends Annotation> annotationClass) throws
            NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException {
        for (Method method : PickleAnnotationSeeker.getMethodsAnnotatedWith(loadedClass, annotationClass)) {
            final Method annotationValueMethod = annotationClass.getMethod(CUCUMBER_ANNOTATION_REGEX_METHOD_NAME);
            final Annotation annotationInstance = method.getAnnotation(annotationClass);
            final boolean accessible = annotationValueMethod.isAccessible();
            annotationValueMethod.setAccessible(true);
            final String annotationValue = (String) annotationValueMethod.invoke(annotationInstance);
            methodCache.put(annotationValue, method);
            annotationValueMethod.setAccessible(accessible);
        }
    }

    public void invokeMethodForStep(String rawStep, Map<String, String> exampleParams, Object... constructorParams) throws
            Exception {
        boolean match = false;
        final String step = replaceExampleOccurences(rawStep, exampleParams);

        for (Map.Entry<String, Method> entrySet : methodCache.entrySet()) {
            final String regex = entrySet.getKey();
            final Method method = entrySet.getValue();

            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(step);

            if (matcher.matches()) {
                LOG.log(Level.INFO, "Method " + method.getName() + " matches");

                if (matcher.groupCount() == method.getParameterTypes().length) {
                    LOG.log(Level.DEBUG, "Number of Regex groups and number of method paramaters match");
                } else {
                    LOG.log(Level.DEBUG, "Number of Regex groups and number of method paramaters do not match:\n"
                            + "Regex groups: " + matcher.groupCount() + "\n"
                            + "Method paramaters: " + method.getParameterTypes().length);
                    LOG.log(Level.INFO, "Parameters do not match - Looking for other method");
                    continue;
                }

                final List<Object> params = new ArrayList<>();
                Class<?>[] types = method.getParameterTypes();
                LOG.log(Level.DEBUG, "Expected parameter types: \n" + Arrays.asList(types));
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    params.add(tryCast(matcher.group(i), types[i - 1]));
                }
                LOG.log(Level.DEBUG, "Actual parameters: \n" + params);

                try {
                    if (params.isEmpty()) {
                        method.invoke(getOrAddCachedDeclaringClassForMethod(method, constructorParams));
                    } else {
                        method.invoke(getOrAddCachedDeclaringClassForMethod(method, constructorParams), params.toArray());
                    }
                } catch (InvocationTargetException ite) {
                    throw new Exception(ite.getCause());
                }
                match = true;
                break;
            }
        }
        if (!match) {
            LOG.log(Level.ERROR, String.format("Could not find any match for step '%s'", rawStep));
        }
    }

    private Object getOrAddCachedDeclaringClassForMethod(final Method method, Object... constructorParams) throws
            InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        final Object declaringClassObject;
        final String declaringClassName = method.getDeclaringClass().getName();
        if (classInstanceCache.containsKey(declaringClassName)) {
            LOG.log(Level.INFO, "Using cached decalred class " + declaringClassName);
            declaringClassObject = classInstanceCache.get(declaringClassName);
        } else {
            final List<Object> constructorParamsList = Arrays.asList(constructorParams);
            final List<Class<?>> constructorParamTypesList = constructorParamsList.stream().map((object) -> object.getClass()).
                    collect(Collectors.toList());
            LOG.log(Level.DEBUG, "Step constructor param list size: "+constructorParamsList.size());
            LOG.log(Level.DEBUG, "Step constructor param type list size: "+constructorParamTypesList.size());
            
            final Constructor<?> ctor = method.getDeclaringClass().getConstructor(constructorParamTypesList.toArray(
                    new Class<?>[constructorParamTypesList.size()]));
            declaringClassObject = method.getDeclaringClass().cast(ctor.newInstance(constructorParamsList.toArray()));

            classInstanceCache.put(declaringClassName, declaringClassObject);
        }
        return declaringClassObject;
    }

    private static Object tryCast(String thingToCast, Class<?> clazz) {
        switch (clazz.toString()) {
            case "int":
                try {
                    return Integer.parseInt(thingToCast);
                } catch (NumberFormatException e) {
                    // Fallthrough
                }
            case "class java.lang.String":
            default:
                return thingToCast;
        }
    }

    public static String replaceExampleOccurences(String rawStep, Map<String, String> exampleParams) {
        for (String key : exampleParams.keySet()) {
            rawStep = rawStep.replaceAll("<" + key + ">", exampleParams.get(key));
        }
        return rawStep;
    }

}
