package com.wire.picklejar.execution;

import com.wire.picklejar.Config;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.execution.exception.StepNotFoundException;
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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickleExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(PickleExecutor.class.getSimpleName());

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
            LOG.error("Could not load step classes and cache step methods", ex);
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

    public long invokeMethodForStep(String rawStep, Map<String, String> exampleParams, Object... constructorParams) throws
            StepNotFoundException, StepNotExecutableException {
        boolean match = false;
        Instant startTime = null;
        Instant endTime = null;
        final String step = replaceExampleOccurences(rawStep, exampleParams);

        for (Map.Entry<String, Method> entrySet : methodCache.entrySet()) {
            final String regex = entrySet.getKey();
            final Method method = entrySet.getValue();

            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(step);

            if (matcher.matches()) {
                LOG.debug("Method {} matches", method.getName());

                if (matcher.groupCount() == method.getParameterTypes().length) {
                    LOG.debug("Number of Regex groups and number of method paramaters match");
                } else {
                    LOG.debug("Number of Regex groups and number of method paramaters do not match:\n"
                            + "Regex groups: {}\n"
                            + "Method paramaters: {}", new Object[]{matcher.groupCount(), method.getParameterTypes().length});
                    LOG.info("Parameters do not match - Looking for other method");
                    continue;
                }

                final List<Object> params = new ArrayList<>();
                Class<?>[] types = method.getParameterTypes();
                LOG.debug("Expected parameter types: \n{}", new Object[]{Arrays.asList(types)});
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    params.add(tryCast(matcher.group(i), types[i - 1]));
                }
                LOG.debug("Actual parameters: \n{}", new Object[]{params});

                startTime = Instant.now();
                try {
                    LOG.info("\n"
                            + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
                            + "::          {}\n"
                            + "''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''", step);
                    if (params.isEmpty()) {
                        method.invoke(getOrAddCachedDeclaringClassForMethod(method, constructorParams));
                    } else {
                        method.invoke(getOrAddCachedDeclaringClassForMethod(method, constructorParams), params.toArray());
                    }
                } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException |
                        NoSuchMethodException ite) {
                    throw new StepNotExecutableException(String.format("\n"
                            + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
                            + "::   Execution of step\n"
                            + "::   '%s'\n"
                            + "::   FAILED\n"
                            + "''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''", step), ite);
                }
                endTime = Instant.now();
                match = true;
                break;
            }
        }
        if (!match) {
            throw new StepNotFoundException(String.format("Could not find any match for step '%s'", rawStep));
        }
        return Duration.between(startTime, endTime).toNanos();
    }

    private Object getOrAddCachedDeclaringClassForMethod(final Method method, Object... constructorParams) throws
            InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        final Object declaringClassObject;
        final String declaringClassName = method.getDeclaringClass().getName();
        if (classInstanceCache.containsKey(declaringClassName)) {
            LOG.debug("Using cached decalred class {}", declaringClassName);
            declaringClassObject = classInstanceCache.get(declaringClassName);
        } else {
            final List<Object> constructorParamsList = Arrays.asList(constructorParams);
            final List<Class<?>> constructorParamTypesList = constructorParamsList.stream().map((object) -> object.getClass()).
                    collect(Collectors.toList());
            LOG.debug("Step constructor param list size: {}", constructorParamsList.size());
            LOG.debug("Step constructor param type list size: {}", constructorParamTypesList.size());

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
