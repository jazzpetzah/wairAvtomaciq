package com.wearezeta.picklejar;

import com.wearezeta.picklejar.gherkin.GherkinParser;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PickleJar {

    private static final String FEATURE_PACKAGE = "com.wearezeta.auto.android";
    private static final String FEATURE_EXTENSION = "feature";

    private GherkinParser parser;
    private List<String> featureFiles = new ArrayList<>();

    public PickleJar(String[] tags) throws IOException {
        List<String> featureFileStrings = PickleFeatureReader.readFiles(getFeatureFiles());
        parser = new GherkinParser(featureFileStrings, tags);
    }
    
    public List<ScenarioOutline> getScenarios(){
        return parser.getScenarios();
    }

    private List<File> getFeatureFiles() throws IOException {
        final ArrayList<File> featureFiles = new ArrayList<File>();
        Collection<File> resource = JavaSeeker.getResource(FEATURE_PACKAGE, FEATURE_EXTENSION);
        for (File file : resource) {
            System.out.println(file.getAbsolutePath());
            featureFiles.add(file);
        }
        return featureFiles;
    }

}
