package com.wearezeta.auto.web.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Steps that are only used for migration tests (tests that check if a release is not breaking accounts from already
 * registered users)
 */
public class MigrationSteps {

    public static final Logger log = ZetaLogger.getLog(MigrationSteps.class.getSimpleName());

    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    private static final int IS_RUNNING_CHECK_INTERVAL = 20; // milliseconds
    private static final int MAX_RETRY = 3;
    private static final int RETRY_TIMEOUT = 20000; // milliseconds

    private Path temp;
    private Process gruntProcess;

    private void createProcessLogger(Process process) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (process.isAlive()) {
                String line = reader.readLine();
                while (line != null) {
                    log.info(String.format("%s", line));
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
        Process process = builder.start();
        Thread unattachedLogger = new Thread(() -> createProcessLogger(process));
        unattachedLogger.start();
        return process;
    }

    private void waitUntilReachable(String url) throws IOException, InterruptedException {
        int statusCode = 0;
        HttpClient client = HttpClientBuilder.create().build();
        int retry = 0;
        while (retry < MAX_RETRY) {
            try {
                HttpResponse response = client.execute(new HttpGet(url));
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    log.info("yeah!");
                    break;
                }
            } catch (HttpHostConnectException e) {
                log.info("Caught HttpHostConnectException, will retry..." + e.getMessage());
            }
            retry++;
            Thread.sleep(RETRY_TIMEOUT);
        }
    }

    @When("^I deploy latest production version$")
    public void IDeployProduction() throws Exception {
        temp = Files.createTempDirectory("webapp");
        log.info("Created temp directory: " + temp.toAbsolutePath());
        runCommand(temp, new String[]{"git", "clone", "git@github.com:wearezeta/mars.git", "."});
        runCommand(temp, new String[]{"git", "checkout", "tags/2016-04-21-11-59"});
        runCommand(temp, new String[]{"npm", "install"});
        runCommand(temp, new String[]{"grunt", "init", "prepare_dist", "gitinfo", "set_version:staging"});
        gruntProcess = runCommandUnattached(temp, new String[]{"grunt", "connect", "watch"});
        final String backend = CommonUtils.getBackendType(MigrationSteps.class);
        final String ip = Inet4Address.getLocalHost().getHostAddress();
        final String url = String.format("http://%s:8888/?env=%s", ip, backend);
        waitUntilReachable(url);
        WebPage page = webappPagesCollection.getPage(WebPage.class);
        page.setUrl(url);
        page.navigateTo();
    }

    @When("^I deploy latest staging version$")
    public void IDeployStaging() throws Exception {
        try {
            gruntProcess.destroy();
            log.info("Process exited with " + gruntProcess.exitValue());
        } catch (IllegalThreadStateException e) {
            log.error(e.getMessage());
            gruntProcess.destroyForcibly();
        }
        runCommand(temp, new String[]{"git", "checkout", "staging"});
        runCommand(temp, new String[]{"grunt", "init", "prepare_dist", "gitinfo", "set_version:staging"});
        runCommandUnattached(temp, new String[]{"grunt", "connect", "watch"});
        final String backend = CommonUtils.getBackendType(MigrationSteps.class);
        final String ip = Inet4Address.getLocalHost().getHostAddress();
        final String url = String.format("http://%s:8888/?env=%s", ip, backend);
        waitUntilReachable(url);
    }
}
