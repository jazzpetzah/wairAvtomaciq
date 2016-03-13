package com.wire.picklejar.construct;

import com.wire.picklejar.Config;
import com.wire.picklejar.PickleJar;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestClassGenerator {

    private static final Logger LOG = LogManager.getLogger();

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

    public class TestCase {

        private final String featureName;
        private final String scenarioName;
        private final int exampleNum;
        private final List<String> steps;
        private final Map<String, String> examples;
        private final String template;

        /**
         * 0 - Feature name<br>
         * 1 - Scenario name<br>
         * 2 - Example row number (starting with 1, 0 means no examples)<br>
         * 3 - List of scenario steps<br>
         * 4 - Map of tuples of examples with header<br>
         *
         */
        public TestCase(Object[] testcase, String template) {
            this.featureName = (String) testcase[0];
            this.scenarioName = (String) testcase[1];
            this.exampleNum = (Integer) testcase[2];
            this.steps = (List<String>) testcase[3];
            this.examples = (Map<String, String>) testcase[4];

            this.template = template
                    .replaceAll("\\$\\(TESTNAME\\)", toClassName())
                    .replaceAll("\\$\\(TESTPACKAGE\\)", Config.GENERATED_TEST_PACKAGE)
                    .replaceAll("\\$\\(DATA\\)", toData());
        }

        public String toClassName() {
            return (featureName + "" + scenarioName).replaceAll("[^a-zA-Z0-9]", "");
        }

        public String toSource() {
            return template;
        }

        public String toData() {
            StringBuilder data = new StringBuilder("List<Object[]> testcases = new ArrayList<>();\n");
            data.append(buildExamplesMap());
            data.append(buildStepList());
            data.append(String.format("testcases.add(new Object[]{\"%s\", \"%s\", %d, steps, examples});\n",
                    featureName, scenarioName, exampleNum));
            data.append("return testcases;");
            return data.toString();
        }

        private String buildExamplesMap() {
            StringBuilder mapString = new StringBuilder("Map<String, String> examples = new HashMap<>();\n");
            for (Map.Entry<String, String> entry : examples.entrySet()) {
                mapString.append(String.format("examples.put(\"%s\", \"%s\");\n", entry.getKey(), entry.getValue()));
            }
            return mapString.toString();
        }

        private String buildStepList() {
            StringBuilder listString = new StringBuilder("List<String> steps = new ArrayList<>();\n");
            for (String step : steps) {
                listString.append(String.format("steps.add(\"%s\");\n", step));
            }
            return listString.toString();
        }

    }

    public static void main(String[] args) throws IOException {

        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            LOG.log(Level.DEBUG, key + "=" + value);

        }

        TestClassGenerator generator = new TestClassGenerator();

        for (TestCase generateTestCase : generator.generateTestCases()) {
            LOG.log(Level.INFO, "Generated Testclass: " + generateTestCase.toClassName());
            generator.compile(generateTestCase.toClassName(), generateTestCase.toSource());
        }
    }

    public List<TestCase> generateTestCases() throws IOException {
        List<TestCase> testObjects = new ArrayList<>();
        Collection<Object[]> testcases = PickleJar.getTestcases();
        String template = new String(Files.readAllBytes(Paths.get(TEST_TEMPLATE_LOCATION + TEST_TEMPLATE_NAME)));

        for (Object[] testcase : testcases) {
            testObjects.add(new TestCase(testcase, template));
        }
        return testObjects;
    }

    private boolean compile(String testName, String source) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager
                = compiler.getStandardFileManager(null, Locale.ENGLISH, null);

        InMemoryJavaFileObject classSourceObject = new InMemoryJavaFileObject(
                Config.GENERATED_TEST_PACKAGE + "." + testName + "Test", source);
        Iterable<? extends JavaFileObject> files = Arrays.asList(classSourceObject);

        new File(CLASS_OUTPUT_FOLDER).mkdirs();

        List<String> optionList = new ArrayList<String>();
        optionList.addAll(Arrays.asList("-d", CLASS_OUTPUT_FOLDER));
        optionList.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,
                null, optionList, null,
                files);
        return task.call();
    }

    public class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {

        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {

            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->"
                    + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }

}
