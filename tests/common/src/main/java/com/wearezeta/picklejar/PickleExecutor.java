package com.wearezeta.picklejar;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PickleExecutor {

    private static final String CUCUMBER_ANNOTATION_REGEX_METHOD_NAME = "value";
    private static final Class<? extends Annotation>[] CUCUMBER_STEP_ANNOTATIONS = new Class[]{
        When.class, Then.class, And.class, Given.class
    };

    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    private final Map<String, Object> classInstanceCache = new ConcurrentHashMap<>();

    public PickleExecutor(String pkg) throws Exception {
        Collection<Class> loadedClasses = JavaSeeker.getClasses(pkg);
        for (Class<?> loadedClass : loadedClasses) {
            for (Class<? extends Annotation> annotationClass : CUCUMBER_STEP_ANNOTATIONS) {
                cacheMethodsByAnnotationValue(loadedClass, annotationClass);
            }
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
                System.out.println("Method " + method.getName() + " matches");

                if (matcher.groupCount() == method.getParameterTypes().length) {
                    System.out.println("Number of Regex groups and number of method paramaters match");
                } else {
                    System.out.println("Number of Regex groups and number of method paramaters do not match:\n"
                            + "Regex groups: " + matcher.groupCount() + "\n"
                            + "Method paramaters: " + method.getParameterTypes().length);
                    System.out.println("Looking for other method");
                    continue;
                }

                final List<Object> params = new ArrayList<>();
                Class<?>[] types = method.getParameterTypes();
                System.out.println("Expected parameter types: \n" + Arrays.asList(types));
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    params.add(tryCast(matcher.group(i), types[i - 1]));
                }
                System.out.println("Actual parameters: \n" + params);

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
            System.out.println(String.format("Could not find any match for step '%s'", rawStep));
        }
    }

    private Object getOrAddCachedDeclaringClassForMethod(final Method method, Object... constructorParams) throws
            InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException,
            InvocationTargetException {
        final Object declaringClassObject;
        final String declaringClassName = method.getDeclaringClass().getName();
        if (classInstanceCache.containsKey(declaringClassName)) {
            System.out.println("Using cached decalred class " + declaringClassName);
            declaringClassObject = classInstanceCache.get(declaringClassName);
        } else {
            //###############################
//            System.out.println("Adding new decalred class " + declaringClassName + " to cache");
//            declaringClassObject = method.getDeclaringClass().newInstance();
            //###############################
            
            
            final List<Object> constructorParamsList = Arrays.asList(constructorParams);
            final List<Class<?>> constructorParamTypesList = constructorParamsList.stream().map((object) -> object.getClass()).
                    collect(Collectors.toList());
            System.out.println("param list size: "+constructorParamsList.size());
            System.out.println("param type list size: "+constructorParamTypesList.size());
            
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
