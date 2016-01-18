package net.masterthought.jenkins;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
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

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CucumberReportPublisher extends Recorder {

	public final String jsonReportDirectory;
	public final String pluginUrlPath;
	public final boolean skippedFails;
	public final boolean undefinedFails;
	public final boolean noFlashCharts;
	public final boolean ignoreFailedTests;
	public String realBuildNumber;
	public String coverage;
	public String customDescription;

	@DataBoundConstructor
	public CucumberReportPublisher(String jsonReportDirectory,
			String pluginUrlPath, boolean skippedFails, boolean undefinedFails,
			boolean noFlashCharts, boolean ignoreFailedTests,
			String realBuildNumber, String coverage, String customDescription) {
		this.jsonReportDirectory = jsonReportDirectory;
		this.pluginUrlPath = pluginUrlPath;
		this.skippedFails = skippedFails;
		this.undefinedFails = undefinedFails;
		this.noFlashCharts = noFlashCharts;
		this.ignoreFailedTests = ignoreFailedTests;
		this.realBuildNumber = realBuildNumber;
		this.coverage = coverage;
		this.customDescription = customDescription;
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

        listener.getLogger().println("[CucumberReportPublisher] Compiling Cucumber Html Reports ...");

        // source directory (possibly on slave)
        FilePath workspaceJsonReportDirectory;
        if (jsonReportDirectory.isEmpty()) {
            workspaceJsonReportDirectory = build.getWorkspace();
        } else {
            workspaceJsonReportDirectory = new FilePath(build.getWorkspace(), jsonReportDirectory);
        }

        // target directory (always on master)
        File targetBuildDirectory = new File(build.getRootDir(), "cucumber-html-reports");
        if (!targetBuildDirectory.exists()) {
            targetBuildDirectory.mkdirs();
        }

        String	buildNumber = Integer.toString(build.getNumber());
        
        String buildProject = build.getProject().getName();

        if (Computer.currentComputer() instanceof SlaveComputer) {
            listener.getLogger().println("[CucumberReportPublisher] copying all json files from slave: " + workspaceJsonReportDirectory.getRemote() + " to master reports directory: " + targetBuildDirectory);
        } else {
            listener.getLogger().println("[CucumberReportPublisher] copying all json files from: " + workspaceJsonReportDirectory.getRemote() + " to reports directory: " + targetBuildDirectory);
        }
        workspaceJsonReportDirectory.copyRecursiveTo("**/*.json", new FilePath(targetBuildDirectory));

        // generate the reports from the targetBuildDirectory
		Result result = Result.NOT_BUILT;
        String[] jsonReportFiles = findJsonFiles(targetBuildDirectory);
        if (jsonReportFiles.length != 0) {

            listener.getLogger().println("[CucumberReportPublisher] Found the following number of json files: " + jsonReportFiles.length);
            int jsonIndex = 0;
            for (String jsonReportFile : jsonReportFiles) {
                listener.getLogger().println("[CucumberReportPublisher] " + jsonIndex + ". Found a json file: " + jsonReportFile);
                jsonIndex++;
            }
            listener.getLogger().println("[CucumberReportPublisher] Generating HTML reports");

            String parsedCoverage = null;
            String parsedCustomDescription = null;
            String parsedRealBuildNumber = "";
            
            // Parse parameters
            listener.getLogger().println("given.coverage=" + coverage);
            listener.getLogger().println("given.customDescription=" + customDescription);
            listener.getLogger().println("given.realBuildNumber=" + realBuildNumber);
            listener.getLogger().println("envVars=" + build.getEnvironment(listener));
            
            if (build.getEnvironment(listener).get(customDescription) != null)
                parsedCustomDescription = build.getEnvironment(listener).get(customDescription);
            else
                parsedCustomDescription = customDescription;
            
            if (build.getEnvironment(listener).get(coverage) != null)
                parsedCoverage = build.getEnvironment(listener).get(coverage);
            else
                parsedCoverage = coverage;
            
            String separator = null;
            String separator2 = null;
            if (realBuildNumber.contains(".")) {
                separator = "\\.";
                separator2 = ".";
            }
            if (realBuildNumber.contains(" ")) {
                separator = " ";
                separator2 = " ";
            }
            
            if (separator != null) {
                listener.getLogger().println("Build separator detected, it is '" + separator2 + "'");
                String[] buildArray = null;
                buildArray = realBuildNumber.split(separator);
                for (int i = 0; i < buildArray.length; i++) {
                    if (build.getEnvironment(listener).get(buildArray[i]) != null)
                        parsedRealBuildNumber = parsedRealBuildNumber
                        .concat(build.getEnvironment(listener).get(buildArray[i])).concat(separator2);
                    else
                        parsedRealBuildNumber = parsedRealBuildNumber.concat(buildArray[i]).concat(separator2);
                }
            parsedRealBuildNumber = parsedRealBuildNumber.substring(0, parsedRealBuildNumber.length() - 1);
            } else if (build.getEnvironment(listener).get(realBuildNumber) != null)
                parsedRealBuildNumber = build.getEnvironment(listener).get(realBuildNumber);
            else
                parsedRealBuildNumber = realBuildNumber;
            
            listener.getLogger().println("parsed.coverage=" + parsedCoverage);
            listener.getLogger().println("parsed.customDescription=" + parsedCustomDescription);
            listener.getLogger().println("parsed.realBuildNumber=" + parsedRealBuildNumber);
            
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
                        undefinedFails,
                        !noFlashCharts,
                        true,
                        false,
                        "",
                        false);
                reportBuilder.generateReports();

				boolean buildSuccess = reportBuilder.getBuildStatus();

				if (buildSuccess)
				{
					result = Result.SUCCESS;
				}
				else
				{
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
