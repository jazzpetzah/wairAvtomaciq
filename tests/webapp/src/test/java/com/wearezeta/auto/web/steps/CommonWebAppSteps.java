package com.wearezeta.auto.web.steps;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.wearezeta.auto.common.email.AccountDeletionMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.web.pages.external.DeleteAccountPage;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.RegistrationPage;
import com.wearezeta.auto.web.pages.WebPage;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriverException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertTrue;

public class CommonWebAppSteps {

    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final WebappPagesCollection webappPagesCollection = WebappPagesCollection.getInstance();

    public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
            .getSimpleName());

    public static final Map<String, Future<ZetaWebAppDriver>> webdrivers = new HashMap<>();

    public static final Platform CURRENT_PLATFORM = Platform.Web;

    public static final int SAFARI_DRIVER_CREATION_RETRY = 3;
    private static final int DELETION_RECEIVING_TIMEOUT = 120;

    private String rememberedPage = null;

    private static final String DEFAULT_USER_PICTURE = "/images/aqaPictureContact600_800.jpg";

    private static final int DRIVER_COMMAND_TIMEOUT_SECONDS = 30;
    private static final ScheduledExecutorService PING_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> RUNNING_PINGER;
    private static final Runnable PINGER = new Runnable() {
        @Override
        public void run() {
            for (Map.Entry<String, Future<ZetaWebAppDriver>> entry : webdrivers.entrySet()) {
                try {
                    ZetaWebAppDriver driver = entry.getValue().get(1, TimeUnit.SECONDS);
                    log.debug(String.format("Pinging driver for \"%s\"",entry.getKey()));
                    driver.getPageSource();
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    log.warn(String.format("Could not ping driver: %s", ex.getMessage()));
                }

            }
        }
    };

    private static void startPinging() {
        if (RUNNING_PINGER != null) {
            log.warn("Driver pinger is already running - Please stop the driver pinger before starting it again");
            return;
        }
        RUNNING_PINGER = PING_EXECUTOR.scheduleAtFixedRate(PINGER, 30, DRIVER_COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private static void stopPinging() {
        if (RUNNING_PINGER != null) {
            if (!RUNNING_PINGER.cancel(true)) {
                log.warn("Could not stop driver pinger");
            }
            RUNNING_PINGER = null;
        }
    }

    private static void setCustomChromeProfile(DesiredCapabilities capabilities)
            throws Exception {
        ChromeOptions options = new ChromeOptions();
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");

        // allow skipping the security prompt for notifications in chrome 46++
        Map<String, Object> prefs = new HashMap<>();
        Map<String, Object> profile = new HashMap<>();
        Map<String, Object> contentSettings = new HashMap<>();
        contentSettings.put("notifications", 1);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        // prefs.put("profile.managed_default_content_settings.notifications",
        // 1);
        options.setExperimentalOption("prefs", prefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    private static void setCustomOperaProfile(DesiredCapabilities capabilities)
            throws Exception {
        final String userProfileRoot = WebCommonUtils.getOperaProfileRoot();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + userProfileRoot);
        // simulate a fake webcam and mic for testing
        options.addArguments("use-fake-device-for-media-stream");
        // allow skipping the security prompt for sharing the media device
        options.addArguments("use-fake-ui-for-media-stream");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    private static void setCustomFirefoxProfile(DesiredCapabilities capabilities) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("dom.webnotifications.enabled", false);
        // allow skipping the security prompt for sharing the media device
        profile.setPreference("media.navigator.permission.disabled", true);
        capabilities.setCapability("firefox_profile", profile);
    }

    private static void setCustomSafariProfile(DesiredCapabilities capabilities) {
        SafariOptions options = new SafariOptions();
        options.setUseCleanSession(true);
        capabilities.setCapability(SafariOptions.CAPABILITY, options);
    }

    private static void setCustomIEProfile(DesiredCapabilities capabilities) {
        capabilities.setCapability("ie.ensureCleanSession", true);
    }

    private static void setExtendedLoggingLevel(
            DesiredCapabilities capabilities, String loggingLevelName) {
        final LoggingPreferences logs = new LoggingPreferences();
        // set it to SEVERE by default
        Level level = Level.SEVERE;
        try {
            level = Level.parse(loggingLevelName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Just continue with the default logging level
        }
        logs.enable(LogType.BROWSER, level);
        // logs.enable(LogType.CLIENT, Level.ALL);
        // logs.enable(LogType.DRIVER, Level.ALL);
        // logs.enable(LogType.PERFORMANCE, Level.ALL);
        // logs.enable(LogType.PROFILER, Level.ALL);
        // logs.enable(LogType.SERVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
        log.debug("Browser logging level has been set to '" + level.getName()
                + "'");
    }

    private static final long DRIVER_INIT_TIMEOUT = 60 * 1000;

    @Before("~@performance")
    public void setUp(Scenario scenario) throws Exception {

        try {
            SEBridge.getInstance().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (scenario.getSourceTagNames().contains("@useSpecialEmail")) {
            usrMgr.setUseSpecialEmailFlag();
        }

        String platform = WebAppExecutionContext.getPlatform();
        String osName = WebAppExecutionContext.getOsName();
        String osVersion = WebAppExecutionContext.getOsVersion();
        String browserName = WebAppExecutionContext.getBrowserName();
        String browserVersion = WebAppExecutionContext.getBrowserVersion();

        // create unique name for saucelabs and webdriver instances
        String uniqueName = getUniqueTestName(scenario);
        log.debug("Unique name for this test: " + uniqueName);

        // get custom capabilities
        DesiredCapabilities capabilities = getCustomCapabilities(platform,
                osName, osVersion, browserName, browserVersion, uniqueName);

        final String hubHost = System.getProperty("hubHost");
        final String hubPort = System.getProperty("hubPort");
        final String url = CommonUtils
                .getWebAppApplicationPathFromConfig(CommonWebAppSteps.class);
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        Callable<ZetaWebAppDriver> callableWebAppDriver = new Callable<ZetaWebAppDriver>() {

            @Override
            public ZetaWebAppDriver call() throws Exception {
                ZetaWebAppDriver lazyWebDriver = null;
                URL hubUrl = new URL("http://" + hubHost + ":" + hubPort
                        + "/wd/hub");
                if (WebAppExecutionContext.getBrowser().equals(Browser.Safari)) {
                    int retries = SAFARI_DRIVER_CREATION_RETRY;
                    boolean failed = false;
                    do {
                        try {
                            retries--;
                            // wait for safari to close properly before starting
                            // it for
                            // a new test
                            Thread.sleep(5000);
                            lazyWebDriver = new ZetaWebAppDriver(hubUrl,
                                    capabilities);
                            failed = false;
                        } catch (WebDriverException e) {
                            log.warn("Safari driver init failed - retrying", e);
                            failed = true;
                        }
                    } while (failed && retries > 0);

                    if (failed) {
                        throw new Exception("Failed to init Safari driver");
                    }
                } else {
                    lazyWebDriver = new ZetaWebAppDriver(hubUrl, capabilities);
                }
                // setup of the browser
                lazyWebDriver.setFileDetector(new LocalFileDetector());
                if (WebAppExecutionContext.getBrowser().equals(Browser.Safari)) {
                    WebCommonUtils.closeAllAdditionalTabsInSafari(lazyWebDriver
                            .getNodeIp());
                    WebCommonUtils.clearHistoryInSafari(lazyWebDriver
                            .getNodeIp());
                }
                if (WebAppExecutionContext.getBrowser()
                        .isSupportingMaximizingTheWindow()) {
                    lazyWebDriver.manage().window().maximize();
                } else if (WebAppExecutionContext.getBrowser()
                        .isSupportingSettingWindowSize()) {
                    // http://stackoverflow.com/questions/14373371/ie-is-continously-maximizing-and-minimizing-when-test-suite-executes
                    lazyWebDriver
                            .manage()
                            .window()
                            .setSize(
                                    new Dimension(
                                            WebAppConstants.MIN_WEBAPP_WINDOW_WIDTH,
                                            WebAppConstants.MIN_WEBAPP_WINDOW_HEIGHT));
                }
                return lazyWebDriver;
            }
        };

        final Future<ZetaWebAppDriver> lazyWebDriver = pool
                .submit(callableWebAppDriver);
        webdrivers.put(uniqueName, lazyWebDriver);
        lazyWebDriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS).get(url);
        WebappPagesCollection.getInstance().setFirstPage(
                new RegistrationPage(lazyWebDriver, url));
        ZetaFormatter.setLazyDriver(lazyWebDriver);
    }

    private String getUniqueTestName(Scenario scenario) {
        String browserName = WebAppExecutionContext.getBrowserName();
        String browserVersion = WebAppExecutionContext.getBrowserVersion();
        String platform = WebAppExecutionContext.getPlatform();

        String id = scenario.getId().substring(
                scenario.getId().lastIndexOf(";") + 1);

        return scenario.getName() + " (" + id + ") on " + platform + " with "
                + browserName + " " + browserVersion;
    }

    private static DesiredCapabilities getCustomCapabilities(String platform,
            String osName, String osVersion, String browserName,
            String browserVersion, String uniqueTestName) throws Exception {
        final DesiredCapabilities capabilities;
        Browser browser = Browser.fromString(browserName);
        switch (browser) {
            case Chrome:
                capabilities = DesiredCapabilities.chrome();
                setCustomChromeProfile(capabilities);
                break;
            case Opera:
                capabilities = DesiredCapabilities.chrome();
                setCustomOperaProfile(capabilities);
                break;
            case Firefox:
                capabilities = DesiredCapabilities.firefox();
                setCustomFirefoxProfile(capabilities);
                break;
            case Safari:
                capabilities = DesiredCapabilities.safari();
                setCustomSafariProfile(capabilities);
                break;
            case InternetExplorer:
                capabilities = DesiredCapabilities.internetExplorer();
                setCustomIEProfile(capabilities);
                break;
            case MicrosoftEdge:
                capabilities = DesiredCapabilities.edge();
                break;
            default:
                throw new NotImplementedException(
                        "Unsupported/incorrect browser name is set: " + browserName);
        }

        if (browser.isSupportingConsoleLogManagement()) {
            setExtendedLoggingLevel(
                    capabilities,
                    WebCommonUtils
                    .getExtendedLoggingLevelInConfig(CommonCallingSteps2.class));
        }

        capabilities.setCapability("platform", platform);
        capabilities.setCapability("version", browserVersion);
        capabilities.setCapability("os", osName);
        capabilities.setCapability("os_version", osVersion);
        capabilities.setCapability("browser_version", browserVersion);
        capabilities.setCapability("name", uniqueTestName);
        capabilities.setCapability("browserstack.debug", "true");

        return capabilities;
    }

    /**
     * This step will throw special PendingException whether the current browser does support calling or not. This will cause
     * Cucumber interpreter to skip the current test instead of failing it.
     *
     * @param doesNot is set to null if "does not" part does not exist
     * @throws Exception
     * @step. ^My browser( does not)? support[s] calling$
     */
    @Given("^My browser( does not)? support[s]? calling$")
    public void MyBrowserSupportsCalling(String doesNot) throws Exception {
        if (doesNot == null) {
            // should support calling
            if (!WebAppExecutionContext.getBrowser().isSupportingCalls()) {
                throw new PendingException("Browser "
                        + WebAppExecutionContext.getBrowser().toString()
                        + " does not support calling.");
            }
        } else // should not support calling
        if (WebAppExecutionContext.getBrowser().isSupportingCalls()) {
            throw new PendingException(
                    "Browser "
                    + WebAppExecutionContext.getBrowser()
                    .toString()
                    + " does support calling but this test is just for browsers without support.");
        }
    }

    @Given("^I switch language to (.*)$")
    public void ISwitchLanguageTo(String language) throws Exception {
        WebappPagesCollection.getInstance().getPage(WebPage.class).switchLanguage(language);
    }

    @Then("^I see a string (.*) on the page$")
    public void ISeeAStringOnPage(String string) throws Throwable {
        assertThat(WebappPagesCollection.getInstance().getPage(WebPage.class).getText(), containsString(string));
    }

    @Then("^I see a placeholder (.*) on the page$")
    public void ISeeAPlaceholderOnPage(String placeholder) throws Throwable {
        assertThat(WebappPagesCollection.getInstance().getPage(WebPage.class).getPlaceholders(), hasItem(placeholder));
    }

    @Then("^I see a button with (.*) on the page$")
    public void ISeeAButtonOnPage(String value) throws Throwable {
        assertThat(WebappPagesCollection.getInstance().getPage(WebPage.class).getButtonValues(), hasItem(value));
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. Avatar picture for Self user is set
     * automatically
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
            throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
                myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
    }

    /**
     * Changes the accent color settings of the given user
     *
     * @param userNameAlias alias of the user where the accent color will be changed
     * @param newColor one of possible accent colors: StrongBlue|StrongLimeGreen|BrightYellow
     * |VividRed|BrightOrange|SoftPink|Violet
     * @throws Exception
     * @step. ^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow
     * |VividRed|BrightOrange|SoftPink|Violet)$
     */
    @Given("^User (\\w+) change accent color to (StrongBlue|StrongLimeGreen|BrightYellow|VividRed|BrightOrange|SoftPink|Violet)$")
    public void IChangeAccentColor(String userNameAlias, String newColor)
            throws Exception {
        commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. Avatar picture for Self user is NOT set
     * automatically
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me without avatar picture$")
    public void ThereAreNUsersWhereXIsMeWithoutAvatar(int count,
            String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count,
                myNameAlias);
    }

    /**
     * Creates specified number of users and sets user with specified name as main user. The user is registered with a phone
     * number only and has no email address attached
     *
     * @param count number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count,
            String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
    }

    /**
     * Set avatar picture for a particular user
     *
     * @param userNameAlias user name/alias
     * @param path path to a picture on a local file system or 'default' to set the default picture
     * @throws Exception
     * @step. ^User (\\w+) changes? avatar picture to (.*)
     */
    @When("^User (\\w+) changes? avatar picture to (.*)")
    public void IChangeUserAvatarPicture(String userNameAlias, String path)
            throws Exception {
        String avatar = null;
        final String rootPath = "/images/";
        if (path.equals("default")) {
            avatar = DEFAULT_USER_PICTURE;
        } else {
            avatar = rootPath + path;
        }
        URI uri = new URI(CommonWebAppSteps.class.getResource(avatar)
                .toString());
        log.debug("Change avatar of user " + userNameAlias + " to "
                + uri.getPath());
        commonSteps.IChangeUserAvatarPicture(userNameAlias, uri.getPath());
    }

    /**
     * Creates connection between to users
     *
     * @param userFromNameAlias user which sends connection request
     * @param usersToNameAliases user which accepts connection request
     * @throws Exception
     * @step. ^(\\w+) is connected to (.*)$
     */
    @Given("^(\\w+) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias,
            String usersToNameAliases) throws Exception {
        commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    /**
     * Blocks a user
     *
     * @param userAsNameAlias user which wants to block another
     * @param userToBlockNameAlias user to block
     * @throws Exception
     * @step. ^(\\w+) blocked (\\w+)$
     */
    @Given("^(\\w+) blocked (\\w+)$")
    public void UserBlocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        commonSteps.BlockContact(userAsNameAlias, userToBlockNameAlias);
    }

    /**
     * Creates group chat with specified users
     *
     * @param chatOwnerNameAlias user that creates group chat
     * @param chatName group chat name
     * @param otherParticipantsNameAlises list of users which will be added to chat separated by comma
     * @throws Exception
     * @step. ^(.*) (?:has|have) group chat (.*) with (.*)
     */
    @Given("^(.*) (?:has|have) group chat (.*) with (.*)")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
            String chatName, String otherParticipantsNameAlises)
            throws Exception {
        commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
                otherParticipantsNameAlises);
    }

    /**
     * Sets self user to be the current user. Avatar picture for this user is set automatically
     *
     * @param nameAlias user to be set as self user
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e$
     */
    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
        IChangeUserAvatarPicture(nameAlias, "default");
    }

    /**
     * Sets self user to be the current user. Avatar picture for this user is NOT set automatically
     *
     * @param nameAlias user to be set as self user
     * @throws Exception
     * @step. ^User (\\w+) is [Mm]e without avatar$
     */
    @Given("^User (\\w+) is [Mm]e without avatar$")
    public void UserXIsMeWithoutAvatar(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
    }

    /**
     * Sends connection request by one user to another
     *
     * @param userFromNameAlias user that sends connection request
     * @param usersToNameAliases user which receive connection request
     * @throws Exception
     * @step. ^(.*) sent connection request to (.*)
     */
    @Given("^(.*) sent connection request to (.*)")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
            String usersToNameAliases) throws Throwable {
        commonSteps.ConnectionRequestIsSentTo(userFromNameAlias,
                usersToNameAliases);
    }

    /**
     * Pings BackEnd until user is indexed and avialable in search
     *
     * @param searchByNameAlias user name to search string
     * @param query querry string
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) exists in backend search results$
     */
    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Pings BackEnd until user is indexed and available in top people
     *
     * @param searchByNameAlias user name to search string
     * @param size number of top people
     * @throws Exception
     * @step. ^(\\w+) waits? until (.*) exists in backend search results$
     */
    @Given("^(\\w+) waits? until (\\d+) people in backend top people results$")
    public void UserWaitsUntilContactExistsInTopPeopleResults(
            String searchByNameAlias, int size) throws Exception {
        commonSteps.WaitUntilTopPeopleContactsIsFoundInSearch(
                searchByNameAlias, size);
    }

    /**
     * Wait for specified amount of seconds
     *
     * @param seconds
     * @throws NumberFormatException
     * @throws InterruptedException
     * @step. ^I wait for (\\d+) seconds?$
     */
    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        commonSteps.WaitForTime(seconds);
    }

    /**
     * Mute conversation
     *
     * @param userToNameAlias   user who want to mute conversation
     * @param muteUserNameAlias conversation or user to be muted
     * @throws Exception
     * @step. ^(.*) muted conversation with (.*)$
     */
    @When("^(.*) muted conversation with (user|group) (.*) on device (.*)$")
    public void MuteConversationWithUser(String userToNameAlias, String convType,
                                         String muteUserNameAlias, String deviceName) throws Exception {
        commonSteps.UserMutesConversation(userToNameAlias, muteUserNameAlias, deviceName, convType.equals("group"));
    }

    /**
     * Archive conversation on the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param archivedUserNameAlias the name of conversation to archive
     * @throws Exception
     * @step. ^(.*) archived conversation with (.*)$
     */
    @When("^(.*) archived conversation with (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias,
            String archivedUserNameAlias) throws Exception {
        commonSteps.ArchiveConversationWithUser(userToNameAlias,
                archivedUserNameAlias);
    }

    /**
     * Send Ping into a conversation using the backend
     *
     * @param pingFromUserNameAlias conversations list owner name/alias
     * @param dstConversationName the name of conversation to send ping to
     * @throws Exception
     * @step. ^User (.*) pinged in the conversation with (.*)$
     */
    @When("^User (.*) pinged in the conversation with (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        commonSteps.UserPingedConversation(pingFromUserNameAlias,
                dstConversationName);
    }

    /**
     * Send Hotping into a conversation using the backend
     *
     * @param pingFromUserNameAlias conversations list owner name/alias
     * @param dstConversationName the name of conversation to send ping to
     * @throws Exception
     * @step. ^User (.*) pinged twice in the conversation with (.*)$
     */
    @When("^User (.*) pinged twice in the conversation with (.*)$")
    public void UserHotPingedConversation(String pingFromUserNameAlias,
            String dstConversationName) throws Exception {
        commonSteps.UserHotPingedConversation(pingFromUserNameAlias,
                dstConversationName);
    }

    /**
     * User A sends a simple text message to user B
     *
     * @param msgFromUserNameAlias the user who sends the message
     * @param msg a message to send. Random string will be sent if it is empty
     * @param dstConvoName The user to receive the message
     * @param isEncrypted whether the message has to be encrypted
     * @param convoType either 'user' or 'group conversation'
     * @throws Exception
     * @step. ^Contact (.*) sends? (encrypted )?message "?(.*?)"?\s?(?:via device (.*)\s)?to (user|group conversation) (.*)$
     */
    @When("^Contact (.*) sends? (encrypted )?message \"?(.*?)\"?\\s?(?:via device (.*)\\s)?to (user|group conversation) (.*)$")
    public void UserSendMessageToConversation(String msgFromUserNameAlias, String isEncrypted,
            String msg, String deviceName, String convoType, String dstConvoName) throws Exception {
        final String msgToSend = (msg == null || msg.trim().length() == 0)
                ? CommonUtils.generateRandomString(10) : msg.trim();
        if (convoType.equals("user")) {
            if (isEncrypted == null) {
                commonSteps.UserSentMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend);
            } else {
                commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
            }
        } else if (isEncrypted == null) {
            commonSteps.UserSentMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend);
        } else {
            commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, msgToSend, deviceName);
        }
    }

    @When("^Contact (.*) sends? (\\d+) encrypted messages with prefix (.*) via device (.*) to (user|group conversation) (.*)$")
    public void UserSendAmountOfMessages(String msgFromUserNameAlias, int amount, String prefix, String deviceName,
                                         String convoType, String dstConvoName) throws Exception {
        if (convoType.equals("user")) {
            for (int i = 0; i < amount; i++) {
                commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias, dstConvoName, prefix + i, deviceName);
            }
        } else {
            for (int i = 0; i < amount; i++) {
                commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias, dstConvoName, prefix + i, deviceName);
            }
        }
    }

    @When("^I remember current page$")
    public void IRememberCurrentPage() throws Exception {
        WebPage page = WebappPagesCollection.getInstance().getPage(WebPage.class);
        rememberedPage = page.getCurrentUrl();
    }

    @When("^I navigate to previously remembered page$")
    public void INavigateToPage() throws Exception {
        if (rememberedPage == null) {
            throw new RuntimeException(
                    "No page has been remembered before!");
        }

        WebPage page = WebappPagesCollection.getInstance().getPage(WebPage.class);
        page.setUrl(rememberedPage);
        page.navigateTo();
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param isEncrypted whether the image has to be encrypted
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName the file path name of the image to send. The path name is defined relative to the image file defined
     * in Configuration.cnf.
     * @param conversationType "single user" or "group" conversation.
     * @param dstConversationName the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)
     */
    @When("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias, String isEncrypted,
            String imageFileName, String conversationType,
            String dstConversationName) throws Exception {
        final String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
        final boolean isGroup = conversationType.equals("group");
        if (isEncrypted == null) {
            commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            commonSteps.UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    @When("^I break the session with device (.*) of user (.*)$")
    public void IBreakTheSession(String deviceName, String userAlias) throws Exception {
        ClientUser user = usrMgr.findUserByNameOrNameAlias(userAlias);
        String deviceId = SEBridge.getInstance().getDeviceId(user, deviceName);
        webappPagesCollection.getPage(WebPage.class).breakSession(deviceId);
    }

    /**
     * Send message to a conversation
     *
     * @param userFromNameAlias user who want to mute conversation
     * @param message message to send
     * @param conversationName the name of existing conversation to send the message to
     * @throws Exception
     * @step. ^User (.*) sent message (.*) to conversation (.*)
     */
    @When("^User (.*) sends? message (.*) to conversation (.*)")
    public void UserSentMessageToConversation(String userFromNameAlias,
            String message, String conversationName) throws Exception {
        commonSteps.UserSentMessageToConversation(userFromNameAlias,
                conversationName, message);
    }

    /**
     * Send personal invitation over the backend
     *
     * @param userToNameAlias the name/alias of conversations list owner
     * @param toMail the email to send the invitation to
     * @param message the message for the invitee
     * @throws Exception
     * @step. ^(.*) send personal invitation to mail (.*) with name (.*) and message (.*)$
     */
    @When("^(.*) sends personal invitation to mail (.*) with message (.*)$")
    public void UserXSendsPersonalInvitation(String userToNameAlias,
            String toMail, String message) throws Exception {
        commonSteps.UserXSendsPersonalInvitationWithMessageToUserWithMail(
                userToNameAlias, toMail, message);
    }

    /**
     * Verify that invitation email exists in user's mailbox
     *
     * @param alias user name/alias
     * @throws Throwable
     * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
     */
    @Then("^I verify user (.*) has received (?:an |\\s*)email invitation$")
    public void IVerifyUserReceiverInvitation(String alias) throws Throwable {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(alias);
        assertTrue(
                String.format("Invitation email for %s is not valid", user.getEmail()),
                BackendAPIWrappers
                .getInvitationMessage(user)
                .orElseThrow(
                        () -> {
                            throw new IllegalStateException(
                                    "Invitation message has not been received");
                        }).isValid());
    }

    @Then("^I delete account of user (.*) via email$")
    public void IDeleteAccountViaEmail(String alias) throws Throwable {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(alias);
        IMAPSMailbox mbox = IMAPSMailbox.getInstance(user.getEmail(), user.getPassword());
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, user.getEmail());
        expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
        AccountDeletionMessage message = new AccountDeletionMessage(mbox.getMessage(expectedHeaders,
                DELETION_RECEIVING_TIMEOUT, 0).get());
        final String url = message.extractAccountDeletionLink();
        log.info("URL: " + url);
        DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
                .getPage(DeleteAccountPage.class);
        deleteAccountPage.setUrl(url);
        deleteAccountPage.navigateTo();
        deleteAccountPage.clickDeleteAccountButton();
        assertTrue("Delete account page does not show success message", deleteAccountPage.isSuccess());
    }

    /**
     * Navigates to the prefilled personal invitation registration page
     *
     * @param alias user name/alias
     * @throws Throwable
     * @step. ^(.*) navigates to personal invitation registration page$
     */
    @Then("^(.*) navigates to personal invitation registration page$")
    public void INavigateToPersonalInvitationRegistrationPage(String alias) throws Throwable {
        final ClientUser user = usrMgr.findUserByNameOrNameAlias(alias);
        String url = BackendAPIWrappers
                .getInvitationMessage(user)
                .orElseThrow(
                        () -> {
                            throw new IllegalStateException(
                                    "Invitation message has not been received");
                        }).extractInvitationLink();
        RegistrationPage registrationPage = WebappPagesCollection.getInstance()
                .getPage(RegistrationPage.class);
        registrationPage.setUrl(url);
        registrationPage.navigateTo();
    }

    /**
     * Add one or more of your contacts to the existing group conversation on the backend
     *
     * @param asUser user name to add as
     * @param contacts the comma separated list of contacts to add
     * @param conversationName conversation name to add contacts to
     * @throws Exception
     * @step. ^User (.*) added contacts? (.*) to group chat (.*)
     */
    @Given("^User (.*) added contacts? (.*) to group chat (.*)")
    public void UserXAddedContactsToGroupChat(String asUser, String contacts,
            String conversationName) throws Exception {
        commonSteps.UserXAddedContactsToGroupChat(asUser, contacts,
                conversationName);
    }

    /**
     * Wait until suggestions are in the backend for a certain user
     *
     * @param userNameAlias the name of the user
     * @throws Exception
     */
    @Given("^There are suggestions for user (.*) on backend$")
    public void suggestions(String userNameAlias) throws Exception {
        commonSteps.WaitUntilSuggestionFound(userNameAlias);
    }

    /**
     * Add email(s) into address book of a user and upload address book in backend
     *
     * @param asUser name of the user where the address book is uploaded
     * @param emails list of email addresses seperated by comma
     * @throws Exception
     */
    @Given("^User (.*) has contacts? (.*) in address book")
    public void UserXHasContactsInAddressBook(String asUser, String emails)
            throws Exception {
        commonSteps.UserXHasContactsInAddressBook(asUser, emails);
    }

    /**
     * Record SHA256-hash of current user profile picture
     *
     * @param asUser user name/alias
     * @throws Exception
     * @step. (.*) takes? snapshot of current profile picture$
     */
    @Given("(.*) takes? snapshot of current profile picture$")
    public void UserXTakesSnapshotOfProfilePicture(String asUser)
            throws Exception {
        commonSteps.UserXTakesSnapshotOfProfilePicture(asUser);
    }

    /**
     * Verify whether current user picture is changed since the last snapshot was made
     *
     * @param userNameAlias user name/alias
     * @throws Exception
     * @step. ^I verify that current profile picture snapshot of (.*) differs? from the previous one$
     */
    @Then("^I verify that current profile picture snapshot of (.*) differs? from the previous one$")
    public void UserXVerifiesSnapshotOfProfilePictureIsDifferent(
            String userNameAlias) throws Exception {
        commonSteps
                .UserXVerifiesSnapshotOfProfilePictureIsDifferent(userNameAlias);
    }

    /**
     * User adds a remote device to his list of devices
     *
     * @step. user (.*) adds a new device (.*)$
     *
     * @param userNameAlias user name/alias
     * @param deviceName unique name of the device
     * @throws Exception
     */
    @When("user (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias,
            String deviceName, String label) throws Exception {
        startPinging();
        commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
        stopPinging();
    }

    /**
     * Will throw PendingException if the current browser does not support synthetic drag and drop
     *
     * @step. ^My browser supports synthetic drag and drop$
     */
    @Given("^My browser supports synthetic drag and drop$")
    public void MyBrowserSupportsSyntheticDragDrop() {
        if (!WebAppExecutionContext.getBrowser()
                .isSupportingSyntheticDragAndDrop()) {
            throw new PendingException();
        }
    }

    /**
     * Verifies whether current browser log is empty or not
     *
     * @throws Exception
     * @step. ^I verify browser log is empty$
     */
    @Then("^I verify browser log is empty$")
    public void VerifyBrowserLogIsEmpty() throws Exception {
        if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
            try {
                if (WebAppExecutionContext.getBrowser()
                        .isSupportingConsoleLogManagement()) {
                    List<LogEntry> browserLog = getBrowserLog(PlatformDrivers
                            .getInstance().getDriver(CURRENT_PLATFORM)
                            .get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS));

                    StringBuilder bLog = new StringBuilder("\n");
                    browserLog.stream().forEach(
                            (entry) -> {
                                bLog.append(entry.getLevel()).append(":")
                                .append(entry.getMessage())
                                .append("\n");
                            });
                    assertTrue("BrowserLog is not empty: " + bLog.toString(),
                            browserLog.isEmpty());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Refreshes page by getting and setting the current URL. Note: Alternative 'WebDriver.navigate().refresh()' hangs with
     * Firefox.
     *
     * @throws Exception
     * @step. ^I refresh page$
     */
    @Then("^I refresh page$")
    public void IRefreshPage() throws Exception {
        WebappPagesCollection.getInstance().getPage(RegistrationPage.class)
                .refreshPage();
    }

    @SuppressWarnings("unchecked")
    private List<LogEntry> getBrowserLog(RemoteWebDriver driver) {
        return IteratorUtils.toList((Iterator<LogEntry>) driver.manage().logs()
                .get(LogType.BROWSER).iterator());
    }

    private void writeBrowserLogsIntoMainLog(RemoteWebDriver driver) {
        List<LogEntry> logEntries = getBrowserLog(driver);
        if (!logEntries.isEmpty()) {
            log.debug("BROWSER CONSOLE LOGS:");
            for (LogEntry logEntry : logEntries) {
                log.debug(logEntry.getMessage().replaceAll("^.*z\\.", "z\\."));
            }
            log.debug("--- END OF LOG ---");
        }

    }

    @After
    public void tearDown(Scenario scenario) throws Exception {
        stopPinging();
        try {
            // async calls/waiting instances cleanup
            CommonCallingSteps2.getInstance().cleanup();
        } catch (Exception e) {
            log.warn(e);
        }

        WebPage.clearPagesCollection();

        String uniqueName = getUniqueTestName(scenario);

        if (webdrivers.containsKey(uniqueName)) {
            Future<ZetaWebAppDriver> webdriver = webdrivers.get(uniqueName);
            try {
                ZetaWebAppDriver driver = webdriver.get(DRIVER_INIT_TIMEOUT,
                        TimeUnit.MILLISECONDS);

                // save browser console if possible
                if (WebAppExecutionContext.getBrowser()
                        .isSupportingConsoleLogManagement()) {
                    writeBrowserLogsIntoMainLog(driver);
                }

                if (driver instanceof ZetaWebAppDriver) {
                    // logout with JavaScript because otherwise backend will
                    // block
                    // us because of top many login requests
                    String logoutScript = "(typeof wire !== 'undefined') && wire.auth.repository.logout();";
                    driver.executeScript(logoutScript);
                }

                // show link to saucelabs
                String link = "https://saucelabs.com/jobs/"
                        + driver.getSessionId();
                log.debug("See more information on " + link);
                String html = "<html><body><a id='link' href='"
                        + link
                        + "'>See more information on "
                        + link
                        + "</a><script>window.location.href = document.getElementById('link').getAttribute('href');</script></body></html>";
                scenario.embed(html.getBytes(Charset.forName("UTF-8")),
                        "text/html");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                log.debug("Trying to quit webdriver for " + uniqueName);
                webdriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.MILLISECONDS)
                        .quit();
                log.debug("Remove webdriver for " + uniqueName + " from map");
                webdrivers.remove(uniqueName);
            }
        }
        commonSteps.getUserManager().resetUsers();
    }

    /**
     * Sends an image from one user to a conversation
     *
     * @param imageSenderUserNameAlias the user to sending the image
     * @param imageFileName the file path name of the image to send. The path name is defined relative to the image file defined
     * in Configuration.cnf.
     * @param conversationType "single user" or "group" conversation.
     * @param dstConversationName the name of the conversation to send the image to.
     * @throws Exception
     * @step. ^Contact (.*) sends image (.*) to (.*) conversation (.*)$
     */
    @When("^Contact (.*) sends image (.*) to (.*) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
            String imageFileName, String conversationType,
            String dstConversationName) throws Exception {
        String imagePath = WebCommonUtils.getFullPicturePath(imageFileName);
        Boolean isGroup = null;
        if (conversationType.equals("single user")) {
            isGroup = false;
        } else if (conversationType.equals("group")) {
            isGroup = true;
        }
        if (isGroup == null) {
            throw new Exception(
                    "Incorrect type of conversation specified (single user | group) expected.");
        }
        commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                imagePath, dstConversationName, isGroup);
    }

    /**
     * Unblocks user
     *
     * @param userAsNameAlias user which wants to unblock another
     * @param userToBlockNameAlias user to unblock
     * @throws Exception
     * @step. ^(\\w+) unblocks (\\w+)$
     */
    @Given("^(\\w+) unblocks user (\\w+)$")
    public void UserUnblocks(String userAsNameAlias, String userToBlockNameAlias)
            throws Exception {
        commonSteps.UnblockContact(userAsNameAlias, userToBlockNameAlias);
    }

    /**
     * Open the sign in page directly (not through a link). This is useful when testing pages with dead ends (forget password,
     * email verification)
     *
     * @throws Exception
     * @step. ^I open Sign In page$
     */
    @Given("^I open Sign In page$")
    public void IOpenSignInPage() throws Exception {
        WebappPagesCollection.getInstance().getPage(RegistrationPage.class)
                .openSignInPage();
    }
    
    /**
     * Remove all registered OTR clients for the particular user
     *
     * @param userAs user name/alias
     * @throws Exception
     * @step. ^User (.*) removes all his registered OTR clients$
     */
    @Given("^User (.*) removes all his registered OTR clients$")
    public void UserRemovesAllRegisteredOtrClients(String userAs) throws Exception {
        commonSteps.UserRemovesAllRegisteredOtrClients(userAs);
    }
    
    /**
     * Remove all registered OTR clients for the particular user except of the X most recent ones
     *
     * @param userAs       user name/alias
     * @param clientsCount the count of recents OTR clients to keep
     * @throws Exception
     * @step. ^User (.*) only keeps his (\d+) most recent OTR clients$
     */
    @Given("^User (.*) only keeps his (\\d+) most recent OTR clients$")
    public void UserKeepsXOtrClients(String userAs, int clientsCount) throws Exception {
        commonSteps.UserKeepsXOtrClients(userAs, clientsCount);
    }

}
