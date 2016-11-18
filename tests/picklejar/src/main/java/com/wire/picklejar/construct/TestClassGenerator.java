package com.wire.picklejar.construct;

import com.wire.picklejar.Config;
import com.wire.picklejar.PickleJar;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClassGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TestClassGenerator.class.getSimpleName());

    private static final String CLASS_OUTPUT_FOLDER = "target/test-classes/";
    private static final String TEST_TEMPLATE_LOCATION = "src/main/resources/";
    private static final String TEST_TEMPLATE_NAME = "testTemplate.txt";

    public class InMemoryJavaFileObject extends SimpleJavaFileObject {

        private final String code;

        InMemoryJavaFileObject(String name, String code) {
            super(URI.create("string:///"
                    + name.replace('.', '/')
                    + Kind.SOURCE.extension),
                    Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public static void main(String[] args) throws IOException {
        TestClassGenerator generator = new TestClassGenerator();
        Collection<Object[]> testcases;
        if (Config.RANDOM != 0) {
            testcases = PickleJar.getRandom(6);
        }else{
            testcases = PickleJar.getTestcases();
        }
        for (TestCase generateTestCase : generator.generateTestCases(testcases)) {
            String testClassName = new TestCaseConverter().toClassName(generateTestCase);
            LOG.info("Generated Testclass: {}", testClassName);
            generator.compile(testClassName, generateTestCase.getCode());
        }
    }

    public List<TestCase> generateTestCases(Collection<Object[]> testcases) throws IOException {
        String template = new String(Files.readAllBytes(Paths.get(TEST_TEMPLATE_LOCATION + TEST_TEMPLATE_NAME)),
                StandardCharsets.UTF_8);

        return testcases.stream()
                .map((testcase) -> new TestCase(testcase, template))
                .collect(Collectors.toList());
    }

    private boolean compile(String testName, String source) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        InMemoryJavaFileObject classSourceObject = new InMemoryJavaFileObject(
                Config.GENERATED_TEST_PACKAGE + "." + testName + "Test", source);
        Iterable<? extends JavaFileObject> files = Arrays.asList(classSourceObject);

        new File(CLASS_OUTPUT_FOLDER).mkdirs();

        List<String> optionList = new ArrayList<>();
        optionList.addAll(Arrays.asList("-d", CLASS_OUTPUT_FOLDER));
        optionList.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));
        
        LOG.trace("Compiling source: \n{}", source);

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                null, optionList, null, files);
        return task.call();
    }

    public class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            LOG.info("Line Number -> {}", diagnostic.getLineNumber());
            LOG.info("code -> {}", diagnostic.getCode());
            LOG.info("Message -> {}", diagnostic.getMessage(Locale.ENGLISH));
            LOG.info("Source -> {}\n", diagnostic.getSource());
        }
    }
}
