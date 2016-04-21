package com.wearezeta.auto.web.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledExecutorService;

import com.wearezeta.auto.common.log.ZetaLogger;
import cucumber.api.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Steps that are only used for migration tests (tests that check if a release is not breaking accounts from already
 * registered users)
 */
public class MigrationSteps {

    public static final Logger log = ZetaLogger.getLog(MigrationSteps.class.getSimpleName());

    private static final int IS_RUNNING_CHECK_INTERVAL = 20; // milliseconds
    private Path temp;
    private Process gruntProcess;

    private void createProcessLogger(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (process.isAlive()) {
                String line = reader.readLine();
                while (line != null) {
                    log.info(String.format("%s\n", line));
                    line = reader.readLine();
                }
                Thread.sleep(IS_RUNNING_CHECK_INTERVAL);
            }
        } catch (NullPointerException | IOException | InterruptedException ex) {
            log.error(String.format("Process died"), ex);
        }
        log.info("Process exited with " + process.exitValue());
    }

    private void runCommand(Path temp, String[] command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(temp.toFile());
        builder.redirectErrorStream(true); //merge error and input steam into one stream
        Process process = builder.start();
        createProcessLogger(process);
    }

    private Process runCommandUnattached(Path temp, String[] command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(temp.toFile());
        builder.redirectErrorStream(true); //merge error and input steam into one stream
        return builder.start();
    }

    @When("^I deploy latest production version$")
    public void IDeployProduction() throws Exception {
        temp = Files.createTempDirectory("webapp");
        log.info("Created temp directory: " + temp.toAbsolutePath());
        runCommand(temp, new String[]{"git", "clone", "git@github.com:wearezeta/mars.git", "."});
        runCommand(temp, new String[]{"git", "checkout", "tags/2016-04-21-11-59"});
        runCommand(temp, new String[]{"npm", "install"});
        runCommand(temp, new String[]{"grunt", "init", "prepare_staging"});
        gruntProcess = runCommandUnattached(temp, new String[]{"grunt", "connect", "watch"});
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet("http://localhost:8888"));
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            log.info("yeah!");
        }
    }

    @When("^I deploy latest staging version$")
    public void IDeployStaging() throws Exception {
        gruntProcess.destroy();
        log.info("Process exited with " + gruntProcess.exitValue());
        runCommand(temp, new String[]{"git", "checkout", "staging"});
        runCommand(temp, new String[]{"grunt", "init", "prepare_staging"});
        runCommandUnattached(temp, new String[]{"grunt", "connect", "watch"});
    }

    @When("^I wait until latest staging version is deployed$")
    public void IWaitUntilStagingIsDeployed() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet("http://localhost:8888"));
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200) {
            log.info("yeah!");
        }
    }
}
