package net.masterthought.jenkins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.Result;
import hudson.slaves.SlaveComputer;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.tools.ant.DirectoryScanner;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

public class CucumberReportPublisher extends Recorder {

    public final String customDescription;
    public final String coverage;
    public final String realBuildNumber;
    public final String jsonReportDirectory;
    public final String pluginUrlPath;
    public final boolean skippedFails;
    public final boolean pendingFails;
    public final boolean undefinedFails;
    public final boolean allSkippedFails;
	public final boolean markNotBuiltIfNoResults;
    public final boolean ignoreFailedTests;
    public final boolean skippedHaveScreenshots;

	@DataBoundConstructor
	public CucumberReportPublisher(String customDescription, String coverage, String realBuildNumber,
                                   String jsonReportDirectory, String pluginUrlPath, boolean skippedFails,
                                   boolean pendingFails, boolean undefinedFails, boolean allSkippedFails,
                                   boolean markNotBuiltIfNoResults, boolean ignoreFailedTests, boolean skippedHaveScreenshots) {
        this.customDescription = customDescription;
        this.coverage = coverage;
        this.realBuildNumber = realBuildNumber;
        this.jsonReportDirectory = jsonReportDirectory;
        this.pluginUrlPath = pluginUrlPath;
        this.skippedFails = skippedFails;
        this.undefinedFails = undefinedFails;
        this.pendingFails = pendingFails;
        this.allSkippedFails = allSkippedFails;
        this.markNotBuiltIfNoResults = markNotBuiltIfNoResults;
        this.ignoreFailedTests = ignoreFailedTests;
        this.skippedHaveScreenshots = skippedHaveScreenshots;
    }

	private String[] findJsonFiles(File targetDirectory) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { "**/*.json" });
		scanner.setBasedir(targetDirectory);
		scanner.scan();
		return scanner.getIncludedFiles();
	}

	@Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {

        listener.getLogger().println("[CucumberReportPublisher] Compiling Cucumber Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;

        workspaceJsonReportDirectory = (jsonReportDirectory.isEmpty()) ? build.getWorkspace() :
                new FilePath(build.getWorkspace(), jsonReportDirectory);

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "cucumber-html-reports");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
        }

        String buildNumber = String.valueOf(build.getNumber());
        String buildProject = build.getProject().getName();

        listener.getLogger().println((Computer.currentComputer() instanceof SlaveComputer) ?
                ("[CucumberReportPublisher] " + "copying all json files from slave: "
                        + workspaceJsonReportDirectory.getRemote() + " to master reports directory: " + targetBuildDirectory) :
                ("[CucumberReportPublisher] copying all json files from: "
                        + workspaceJsonReportDirectory.getRemote() + " to reports directory: " + targetBuildDirectory));

        workspaceJsonReportDirectory.copyRecursiveTo("**/*.json", new FilePath(targetBuildDirectory));

        // generate the reports from the targetBuildDirectory
		Result result = Result.NOT_BUILT;
        String[] jsonReportFiles = findJsonFiles(targetBuildDirectory);
        if (jsonReportFiles.length != 0) {

            listener.getLogger().println("[CucumberReportPublisher] Found the following number of json files: "
                    + jsonReportFiles.length);
            int jsonIndex = 0;
            for (String jsonReportFile : jsonReportFiles) {
                listener.getLogger().println("[CucumberReportPublisher] " + jsonIndex + ". Found a json file: "
                        + jsonReportFile);
                jsonIndex++;
            }

            String parsedCoverage = null;
            String parsedCustomDescription = null;
            String parsedRealBuildNumber = "";
            
            // Parse parameters
            listener.getLogger().println("[CucumberReportPublisher] given.coverage=" + coverage);
            listener.getLogger().println("[CucumberReportPublisher] given.customDescription=" + customDescription);
            listener.getLogger().println("[CucumberReportPublisher] given.realBuildNumber=" + realBuildNumber);
            listener.getLogger().println("[CucumberReportPublisher] envVars=" + build.getEnvironment(listener));

            parsedCustomDescription = (build.getEnvironment(listener).get(customDescription) == null) ? customDescription :
                    build.getEnvironment(listener).get(customDescription);
            parsedCoverage = (build.getEnvironment(listener).get(coverage) == null) ? coverage :
                    build.getEnvironment(listener).get(coverage);

            String buildSeparator = null;
            if (realBuildNumber.contains(".")) {
                buildSeparator = ".";
            }
            if (realBuildNumber.contains(" ")) {
                buildSeparator = " ";
            }
            if (buildSeparator != null) {
                listener.getLogger().println("[CucumberReportPublisher] Build separator detected, it is '" + buildSeparator + "'");
                String[] buildArray = null;
                buildArray = realBuildNumber.split("\\" + buildSeparator);
                for (String s : buildArray) {
                    parsedRealBuildNumber = (build.getEnvironment(listener).get(s) == null) ?
                            parsedRealBuildNumber.concat(s).concat(buildSeparator) :
                            parsedRealBuildNumber.concat(build.getEnvironment(listener).get(s)).concat(buildSeparator);
                }
                parsedRealBuildNumber = parsedRealBuildNumber.substring(0, parsedRealBuildNumber.length() - 1);
            } else {
                parsedRealBuildNumber = (build.getEnvironment(listener).get(realBuildNumber) == null) ? realBuildNumber :
                        build.getEnvironment(listener).get(realBuildNumber);
            }
            
            listener.getLogger().println("[CucumberReportPublisher] Parsed coverage:" + parsedCoverage);
            listener.getLogger().println("[CucumberReportPublisher] Parsed customDescription:" + parsedCustomDescription);
            listener.getLogger().println("[CucumberReportPublisher] Parsed realBuildNumber:" + parsedRealBuildNumber);
            listener.getLogger().println("[CucumberReportPublisher] Read json report...");
            try {
                ReportBuilder reportBuilder = new ReportBuilder(
                        fullPathToJsonFiles(jsonReportFiles, targetBuildDirectory),
                        targetBuildDirectory,
                        parsedCustomDescription,
                        parsedCoverage,
                        parsedRealBuildNumber,
                        pluginUrlPath,
                        buildNumber,
                        buildProject,
                        skippedFails,
                        pendingFails,
                        undefinedFails,
                        allSkippedFails,
                        skippedHaveScreenshots,
                        true,
                        false,
                        "");
                listener.getLogger().println("[CucumberReportPublisher] Generating HTML reports");
                reportBuilder.generateReports();

				boolean buildSuccess = reportBuilder.getBuildStatus();


				if (buildSuccess) {
                    result = (markNotBuiltIfNoResults && reportBuilder.getTotalNumberOfSteps() == 0) ? Result.NOT_BUILT :
                            Result.SUCCESS;
                } else {
					result = ignoreFailedTests ? Result.UNSTABLE : Result.FAILURE;
				}
				
            } catch (Exception e) {
                e.printStackTrace();
				result = Result.FAILURE;
                listener.getLogger().println("[CucumberReportPublisher] there was an error generating the reports: " + e);
                for(StackTraceElement error : e.getStackTrace()){
                   listener.getLogger().println(error);
                }
            }
        } else {
			result = Result.SUCCESS;
            listener.getLogger().println("[CucumberReportPublisher] there were no json results found in: " + targetBuildDirectory);
        }

        build.addAction(new CucumberReportBuildAction(build));
		build.setResult(result);
		
        return true;
    }

	private List<String> fullPathToJsonFiles(String[] jsonFiles,
			File targetBuildDirectory) {
		List<String> fullPathList = new ArrayList<String>();
		for (String file : jsonFiles) {
			fullPathList.add(new File(targetBuildDirectory, file)
					.getAbsolutePath());
		}
		return fullPathList;
	}

	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new CucumberReportProjectAction(project);
	}

	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {
		@Override
		public String getDisplayName() {
			return "Publish cucumber results as a report";
		}

		// Performs on-the-fly validation on the file mask wildcard.
		public FormValidation doCheck(@AncestorInPath AbstractProject project,
				@QueryParameter String value) throws IOException,
				ServletException {
			FilePath ws = project.getSomeWorkspace();
			return ws != null ? ws.validateRelativeDirectory(value)
					: FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
}
