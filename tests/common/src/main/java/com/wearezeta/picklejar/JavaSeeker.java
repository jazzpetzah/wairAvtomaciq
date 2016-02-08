package com.wearezeta.picklejar;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavaSeeker {

    public static Collection<Class> getClasses(final String pack) throws IOException {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null);
        return StreamSupport.stream(fileManager.list(StandardLocation.CLASS_PATH, pack, Collections.singleton(JavaFileObject.Kind.CLASS), true).spliterator(), false)
                .map(javaFileObject -> {
                    try {
                        final String packagePath = pack.
                                replaceAll("\\.", File.separator);
                        final String strippedFullClassNamePath = javaFileObject.getName().
                                replaceAll("(.*)" + packagePath, packagePath).
                                replace(".class", "").
                                replace(")", "");
                        final String fullClassName = strippedFullClassNamePath.
                                replaceAll(Pattern.quote(File.separator), ".");
                        System.out.println(fullClassName);
                        return Class.forName(fullClassName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Collection<File> getResource(final String pack, String extension) throws IOException {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null);
        return StreamSupport.stream(fileManager.list(StandardLocation.CLASS_PATH, pack, Collections.singleton(JavaFileObject.Kind.OTHER), true).spliterator(), false)
                .map(javaFileObject -> {
                    return new File(javaFileObject.getName());
                })
                .filter((file) -> Files.getFileExtension(file.getName()).equalsIgnoreCase(extension))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> getLoadedClasses()
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        ClassLoader classLoader = getClassLoader();
        Class classLoaderClass = classLoader.getClass();
        while (classLoaderClass != java.lang.ClassLoader.class) {
            classLoaderClass = classLoaderClass.getSuperclass();
        }
        Field classLoaderClassesField = classLoaderClass
                .getDeclaredField(CLASS_LOADER_CLASSES_FIELD_NAME);
        classLoaderClassesField.setAccessible(true);
        ArrayList<Class<?>> classesList = new ArrayList((List) classLoaderClassesField.get(classLoader));
        return classesList;
    }
    private static final String CLASS_LOADER_CLASSES_FIELD_NAME = "classes";

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
