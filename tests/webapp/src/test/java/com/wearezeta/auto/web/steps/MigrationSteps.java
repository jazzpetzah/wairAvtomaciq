package com.wearezeta.auto.web.steps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

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
    private static final int RETRY_TIMEOUT = 10000; // milliseconds

    private Path temp = null;
    private Process gruntProcess = null;

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
            log.error("Process died", ex);
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
        unattachedLogger.setDaemon(true);
        unattachedLogger.start();
        return process;
    }

    private void waitUntilReachable(String url) throws IOException, InterruptedException {
        int statusCode;
        HttpClient client = HttpClientBuilder.create().build();
        int retry = 0;
        while (retry < MAX_RETRY) {
            try {
                HttpResponse response = client.execute(new HttpGet(url));
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    log.info(String.format("Webserver is reachable via %s!", url));
                    break;
                }
            } catch (HttpHostConnectException e) {
                log.info("Caught HttpHostConnectException, will retry..." + e.getMessage());
            }
            retry++;
            Thread.sleep(RETRY_TIMEOUT);
        }
    }

    @When("^I( initially)? deploy version with tag (.*)$")
    public void IDeployVersionWithTag(String initially, String tag) throws Exception {
        String url = deployWebapp("tags/" + tag);
        if (initially != null) {
            WebPage page = webappPagesCollection.getPage(WebPage.class);
            page.setUrl(url);
            page.navigateTo();
        }
    }

    @When("^I deploy latest staging version$")
    public void IDeployStaging() throws Exception {
        deployWebapp("staging");
    }

    private String deployWebapp(String branch) throws Exception {
        if (temp == null) {
            temp = Files.createTempDirectory("webapp");
            log.info("Created temp directory: " + temp.toAbsolutePath());
            runCommand(temp, new String[]{"git", "clone", "git@github.com:wearezeta/mars.git", "."});
        }
        if (gruntProcess != null) {
            try {
                gruntProcess.destroy();
                log.info("Process exited with " + gruntProcess.exitValue());
            } catch (IllegalThreadStateException e) {
                log.error(e.getMessage());
                gruntProcess.destroyForcibly();
            }
        }
        runCommand(temp, new String[]{"git", "checkout", branch});
        // cherry pick the feature to set port number
        runCommand(temp, new String[]{"git", "cherry-pick", "93cd7fb11e54b1883e3c122bb876db3a57e75eae"});
        runCommand(temp, new String[]{"npm", "install"});
        runCommand(temp, new String[]{"grunt", "prepare_dist", "gitinfo", "set_version:staging"});
        // Get a port number in between 9000 and 10000
        // TODO: String port = String.valueOf(ThreadLocalRandom.current().nextInt(9000, 10000));
        String port = "8888";
        gruntProcess = runCommandUnattached(temp, new String[]{"grunt", "host:" + port + ":false"});
        final String backend = CommonUtils.getBackendType(MigrationSteps.class);
        // We use wire.ms as a alias-domain for localhost
        final String host = "wire.ms";
        final String url = String.format("http://%s:%s/?env=%s", host, port, backend);
        waitUntilReachable(url);
        return url;
    }
}
