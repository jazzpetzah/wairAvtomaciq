package com.wearezeta.suite_splitter.reporting;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ReportGenerator {
    private static Logger LOG = Logger.getLogger(ReportGenerator.class.getSimpleName());

    public static void generate(ReportModel model) {
        for (Entry<String, Map<String, List<String>>> entry : model
                .getSplittedFeatures().entrySet()) {
            final String containerPath = entry.getKey();

            int tcCount = 0;
            for (Entry<String, List<String>> partEntry : entry.getValue().entrySet()) {
                tcCount += partEntry.getValue().size();
            }
            System.out.println(String
                    .format("The part %s contains %d feature file(s) (%d test case(s) in all files):",
                            containerPath, entry.getValue().size(), tcCount));

            for (Entry<String, List<String>> partEntry : entry.getValue().entrySet()) {
                final String partName = partEntry.getKey();
                final List<String> testcases = partEntry.getValue();
                LOG.info(String.format("\t%s (%d test case(s))", partName, testcases.size()));
                for (String tc : testcases) {
                    LOG.info(String.format("\t\t%s", tc));
                }
            }
            LOG.info("\n");
        }
    }
}
