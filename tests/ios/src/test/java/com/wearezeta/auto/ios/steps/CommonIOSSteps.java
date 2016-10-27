package com.wearezeta.auto.ios.steps;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.ios.reporter.IOSLogListener;
import com.wearezeta.auto.ios.tools.*;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import gherkin.formatter.model.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.LoginPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.openqa.selenium.remote.RemoteWebDriver;

import static com.wearezeta.auto.common.CommonUtils.*;

public class CommonIOSSteps {
    private final CommonSteps commonSteps = CommonSteps.getInstance();
    private static final String DEFAULT_USER_AVATAR = "android_dialog_sendpicture_result.png";
    //    private static final String IOS_WD_APP_BUNDLE = "com.apple.test.WebDriverAgentRunner-Runner";
//    private static final String FACEBOOK_WD_APP_BUNDLE = "com.facebook.IntegrationApp";
    private static final String ADDRESSBOOK_HELPER_APP_NAME = "AddressbookApp.ipa";
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private static Logger log = ZetaLogger.getLog(CommonIOSSteps.class.getSimpleName());

    // We keep this short and compatible with spell checker
    public static final String DEFAULT_AUTOMATION_MESSAGE = "1 message";

    public static final String CAPABILITY_NAME_ADDRESSBOOK = "addressbookStart";
    public static final String TAG_NAME_ADDRESSBOOK = "@" + CAPABILITY_NAME_ADDRESSBOOK;

    public static final String TAG_NAME_UPGRADE = "@upgrade";

    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
    }

    public static final Platform CURRENT_PLATFORM = Platform.iOS;

    private static String getPlatformVersion() throws Exception {
        return getPlatformVersionFromConfig(CommonIOSSteps.class);
    }

    private static String getUrl() throws Exception {
        return getIosAppiumUrlFromConfig(CommonIOSSteps.class);
    }

    private static String getAppPath() throws Exception {
        return getIosApplicationPathFromConfig(CommonIOSSteps.class);
    }

    private static String getOldAppPath() throws Exception {
        return getOldAppPathFromConfig(CommonIOSSteps.class);
    }

    /**
     * https://github.com/wireapp/wire-automation-addressbook-ios
     */
    public static String getiOSAddressbookAppPath() throws Exception {
        return getIOSToolsRoot(CommonIOSSteps.class) + File.separator + ADDRESSBOOK_HELPER_APP_NAME;
    }

    private static String getAppName() throws Exception {
        return getIOSAppName(CommonIOSSteps.class);
    }

    private static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static Map<String, String> cachedBundleIds = new HashMap<>();

    private static final long INSTALL_DELAY_MS = 5000;

    // These settings are needed to properly sign WDA for real device tests
    // See https://github.com/appium/appium-xcuitest-driver for more details
    private static final String KEYCHAIN_PATH = String.format("%s/%s",
            System.getProperty("user.home"), "/Library/Keychains/MyKeychain.keychain");
    private static final String KEYCHAIN_PASSWORD = "123456";

    private static void prepareRealDevice(String udid, String ipaPath) throws Exception {
        if (!cachedBundleIds.containsKey(ipaPath)) {
            final File appPath = CommonUtils.extractAppFromIpa(new File(ipaPath));
            try {
                cachedBundleIds.put(ipaPath, ZetaIOSDriver.parseBundleId(
                        new File(appPath.getCanonicalPath() + File.separator + "Info.plist")));
            } finally {
                FileUtils.deleteDirectory(appPath);
            }
        }
        RealDeviceHelpers.uninstallApp(udid, cachedBundleIds.get(ipaPath));
    }

    @SuppressWarnings("unchecked")
    public Future<ZetaIOSDriver> resetIOSDriver(String ipaPath,
                                                Optional<Map<String, Object>> additionalCaps,
                                                int retriesCount) throws Exception {
        final boolean isRealDevice = !CommonUtils.getIsSimulatorFromConfig(getClass());

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("newCommandTimeout", AppiumServer.DEFAULT_COMMAND_TIMEOUT);
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        capabilities.setCapability(ZetaIOSDriver.AUTOMATION_NAME_CAPABILITY_NAME,
                ZetaIOSDriver.AUTOMATION_MODE_XCUITEST);
        capabilities.setCapability("app", ipaPath);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("appName", getAppName());
        if (isRealDevice) {
            final String udid = RealDeviceHelpers.getUDID().orElseThrow(
                    () -> new IllegalStateException("Cannot detect any connected iDevice")
            );
            prepareRealDevice(udid, ipaPath);

            // We don't really care about which particular real device model we have
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()).split("\\s+")[0]);
            capabilities.setCapability("udid", udid);
            capabilities.setCapability("realDeviceLogger",
                    "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
            capabilities.setCapability("showXcodeLog", true);
            capabilities.setCapability("keychainPath", KEYCHAIN_PATH);
            capabilities.setCapability("keychainPassword", KEYCHAIN_PASSWORD);
        } else {
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()));
            // https://github.com/appium/appium-xcuitest-driver/pull/184/files
            capabilities.setCapability("iosInstallPause", INSTALL_DELAY_MS);
        }
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("launchTimeout", ZetaIOSDriver.MAX_SESSION_INIT_DURATION_MILLIS);
        final String backendType = getBackendType(this.getClass());
        final List<String> processArgs = new ArrayList<>(Arrays.asList(
                "-UseHockey", "0",
                "-ZMBackendEnvironmentType", backendType,
                // https://wearezeta.atlassian.net/browse/ZIOS-5769
                "--disable-autocorrection",
                // https://wearezeta.atlassian.net/browse/ZIOS-5259
                "-AnalyticsUserDefaultsDisabledKey", "0"
                //,"--debug-log-network"
        ));

        if (additionalCaps.isPresent()) {
            for (Map.Entry<String, Object> entry : additionalCaps.get().entrySet()) {
                if (entry.getKey().equals(FastLoginContainer.CAPABILITY_NAME) &&
                        (entry.getValue() instanceof ClientUser)) {
                    processArgs.addAll(Arrays.asList(
                            // https://github.com/wearezeta/zclient-ios/pull/2152
                            // https://wearezeta.atlassian.net/browse/ZIOS-6747
                            "--loginemail=" + ((ClientUser) entry.getValue()).getEmail(),
                            "--loginpassword=" + ((ClientUser) entry.getValue()).getPassword()
                    ));
                } else {
                    if (entry.getKey().equals(CAPABILITY_NAME_ADDRESSBOOK) &&
                            (entry.getValue() instanceof Boolean) && (Boolean) entry.getValue()) {
                        processArgs.addAll(Arrays.asList(
                                "--addressbook-on-simulator",
                                "--addressbook-search-delay=2"
                        ));
                    }
                    capabilities.setCapability(entry.getKey(), entry.getValue());
                }
            }
        }
        final JSONObject argsValue = new JSONObject();
        argsValue.put("args", processArgs);
        capabilities.setCapability("processArguments", argsValue.toString());

        return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance().resetDriver(
                getUrl(), capabilities, retriesCount
        );
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        try {
            SEBridge.getInstance().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppiumServer.getInstance().resetLog();

        if (scenario.getSourceTagNames().contains("@useSpecialEmail")) {
            usrMgr.useSpecialEmail();
        }

        if (scenario.getSourceTagNames().contains("@performance")) {
            CommonUtils.defineNoHeadlessEnvironment();
            try {
                IOSLogListener.getInstance().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final Map<String, Object> additionalCaps = new HashMap<>();
        String appPath = getAppPath();
        if (scenario.getSourceTagNames().contains(TAG_NAME_UPGRADE) ||
                scenario.getSourceTagNames().contains(TAG_NAME_ADDRESSBOOK)) {
            if (scenario.getSourceTagNames().contains(TAG_NAME_UPGRADE)) {
                appPath = getOldAppPath();
            }

            if (scenario.getSourceTagNames().contains(TAG_NAME_ADDRESSBOOK)) {
                additionalCaps.put(CAPABILITY_NAME_ADDRESSBOOK, true);
            }

            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
            if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
                IOSSimulatorHelper.reset();
            } else {
                // TODO: Make sure the app is uninstalled from the real device
                throw new NotImplementedException("Reset action is only available for Simulator");
            }
            additionalCaps.put("noReset", true);
            additionalCaps.put("fullReset", false);
        }

        if (scenario.getSourceTagNames().contains(FastLoginContainer.TAG_NAME)) {
            FastLoginContainer.getInstance().enable(this::resetIOSDriver, appPath,
                    additionalCaps.isEmpty() ? Optional.empty() : Optional.of(additionalCaps),
                    DRIVER_CREATION_RETRIES_COUNT);
        } else {
            final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(appPath,
                    additionalCaps.isEmpty() ? Optional.empty() : Optional.of(additionalCaps),
                    DRIVER_CREATION_RETRIES_COUNT);
            updateDriver(lazyDriver);
        }
    }

    private void updateDriver(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        ZetaFormatter.setLazyDriver(lazyDriver);
        if (pagesCollection.hasPages()) {
            pagesCollection.clearAllPages();
        }
        pagesCollection.setFirstPage(new LoginPage(lazyDriver));
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            // async calls/waiting instances cleanup
            CommonCallingSteps2.getInstance().cleanup();
        } catch (Exception e) {
            // do not fail if smt fails here
            e.printStackTrace();
        }

        FastLoginContainer.getInstance().reset();

        pagesCollection.clearAllPages();

        try {
            if (!scenario.getStatus().equals(Result.PASSED) && getIsSimulatorFromConfig(getClass())) {
                log.debug(IOSSimulatorHelper.getLogsAndCrashes() + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            } else if (scenario.getSourceTagNames().contains("@performance")) {
                IOSLogListener.forceStopAll();
                IOSLogListener.writeDeviceLogsToConsole(IOSLogListener.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            usrMgr.resetUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (scenario.getSourceTagNames().contains(TAG_NAME_UPGRADE)) {
            try {
                if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
                    IOSSimulatorHelper.kill();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Upgrade Wire to the recent version if the old one was previously installed
     *
     * @throws Exception
     * @step. ^I upgrade Wire to the recent version$
     */
    @Given("^I upgrade Wire to the recent version$")
    public void IUpgradeWire() throws Exception {
        final Map<String, Object> customCaps = new HashMap<>();
        if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
            final RemoteWebDriver currentDriver = PlatformDrivers.getInstance().getDriver(CURRENT_PLATFORM).get();
            final Map<String, ?> currentCapabilities = currentDriver.getCapabilities().asMap();
            for (Map.Entry<String, ?> capabilityItem : currentCapabilities.entrySet()) {
                customCaps.put(capabilityItem.getKey(), capabilityItem.getValue());
            }
            try {
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        customCaps.put("noReset", true);
        customCaps.put("fullReset", false);
        final String appPath = getAppPath();
        if (appPath.endsWith(".ipa")) {
            pagesCollection.getCommonPage().installIpa(new File(appPath));
        } else if (appPath.endsWith(".app")) {
            pagesCollection.getCommonPage().installApp(new File(appPath));
        } else {
            throw new IllegalArgumentException(String.format("Only .app and .ipa package formats are supported. " +
                    "%s is given instead.", appPath));
        }
        final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(appPath, Optional.of(customCaps), 1);
        updateDriver(lazyDriver);
    }

    /**
     * Restarts currently executed Wire instance
     *
     * @throws Exception
     * @step. ^I restart Wire$
     */
    @When("^I restart Wire$")
    public void IRestartWire() throws Exception {
        final RemoteWebDriver currentDriver =
                PlatformDrivers.getInstance().getDriver(CURRENT_PLATFORM)
                        .get(ZetaIOSDriver.MAX_COMMAND_DURATION_MILLIS, TimeUnit.MILLISECONDS);
        final Map<String, ?> currentCapabilities = currentDriver.getCapabilities().asMap();
        try {
            PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Map<String, Object> customCaps = new HashMap<>();
        for (Map.Entry<String, ?> capabilityItem : currentCapabilities.entrySet()) {
            customCaps.put(capabilityItem.getKey(), capabilityItem.getValue());
        }
        customCaps.put("noReset", true);
        customCaps.put("fullReset", false);
        final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(getAppPath(), Optional.of(customCaps), 1);
        updateDriver(lazyDriver);
    }

    private ElementState screenState = new ElementState(
            () -> pagesCollection.getCommonPage().takeScreenshot().orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of the current device state")
            )
    );

    /**
     * Store current screen state into an internal variable
     *
     * @throws Exception
     * @step. ^I remember current screen state$
     */
    @When("^I remember current screen state$")
    public void IRememberCurrentScreenState() throws Exception {
        screenState.remember();
    }

    /**
     * Verify whether screen state has been changed or not
     *
     * @param moreOrLess     either one of two possible values
     * @param score          similarity score value. Can be positive float number less than 1
     * @param timeoutSeconds screen change timeout
     * @throws Exception
     * @step. ^I verify that current screen similarity score is (more|less) than ([\d\.]+) within (\d+) seconds?$
     */
    @Then("^I verify that current screen similarity score is (more|less) than ([\\d\\.]+) within (\\d+) seconds?$")
    public void IVerifyScreenState(String moreOrLess, String score, int timeoutSeconds) throws Exception {
        if (moreOrLess.equals("less")) {
            Assert.assertTrue(
                    String.format("Current screen state looks too similar to the previous one after %s seconds",
                            timeoutSeconds), screenState.isChanged(timeoutSeconds, Double.parseDouble(score)));
        } else {
            Assert.assertTrue(
                    String.format("Current screen state looks different to the previous one after %s seconds",
                            timeoutSeconds), screenState.isNotChanged(timeoutSeconds, Double.parseDouble(score)));
        }
    }

    /**
     * Process on-screen alert
     *
     * @param action    either accept or dismiss
     * @param mayIgnore whether to throw an exception if alert is not present
     * @throws Exception
     * @step. ^I (accept|dismiss) alert( if visible)?$
     */
    @When("^I (accept|dismiss) alert( if visible)?$")
    public void IAcceptAlert(String action, String mayIgnore) throws Exception {
        switch (action.toLowerCase()) {
            case "accept":
                if (mayIgnore == null) {
                    pagesCollection.getCommonPage().acceptAlert();
                } else {
                    pagesCollection.getCommonPage().acceptAlertIfVisible();
                }
                break;
            case "dismiss":
                if (mayIgnore == null) {
                    pagesCollection.getCommonPage().dismissAlert();
                } else {
                    pagesCollection.getCommonPage().dismissAlertIfVisible();
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown alert action: '%s'", action));
        }
    }

    /**
     * Tap the corresponding button on an alert
     *
     * @param caption button namem as it is visible in UI
     * @throws Exception
     * @step. ^I tap (.*) button on the alert$
     */
    @And("^I tap (.*) button on the alert$")
    public void ITapAlertButton(String caption) throws Exception {
        pagesCollection.getCommonPage().tapAlertButton(caption);
    }

    /**
     * Tap the corresponding on-screen keyboard button
     *
     * @param btnName button name
     * @throws Exception
     * @step. ^I tap (Hide|Space|Done) keyboard button$
     */
    @When("^I tap (Hide|Space|Done) keyboard button$")
    public void ITapHideKeyboardBtn(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "hide":
                pagesCollection.getCommonPage().tapHideKeyboardButton();
                break;
            case "space":
                pagesCollection.getCommonPage().tapSpaceKeyboardButton();
                break;
            case "done":
                pagesCollection.getCommonPage().tapKeyboardCommitButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: %s", btnName));
        }
    }

    /**
     * Closes the app for a certain amount of time in seconds
     *
     * @param seconds time in seconds to close the app
     * @throws Exception
     * @step. ^I close the app for (.*) seconds?$
     */
    @When("^I close the app for (\\d+) seconds?$")
    public void ICloseApp(int seconds) throws Exception {
        pagesCollection.getCommonPage().pressHomeButton(seconds);
    }

    /**
     * Locks screen for a certain amount of time in seconds
     *
     * @param seconds time in seconds to lock screen
     * @throws Exception
     * @step. ^I lock screen for (.*) seconds$
     */
    @When("^I lock screen for (\\d+) seconds$")
    public void ILockScreen(int seconds) throws Exception {
        pagesCollection.getCommonPage().lockScreen(seconds);
    }

    @Given("^(.*) sent connection request to (.*)$")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias,
                                               String usersToNameAliases) throws Throwable {
        commonSteps.ConnectionRequestIsSentTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^(.*) has group chat (.*) with (.*)$")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises) throws Exception {
        commonSteps.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName, otherParticipantsNameAlises);
    }

    /**
     * Removes user from group conversation
     *
     * @param chatOwnerNameAlias name of the user who deletes
     * @param userToRemove       name of the user to be removed
     * @param chatName           name of the group conversation
     * @throws Exception
     * @step. ^(.*) removes? (.*) from group chat (.*)$
     */
    @Given("^(.*) removes? (.*) from group chat (.*)$")
    public void UserARemoveUserBFromGroupChat(String chatOwnerNameAlias,
                                              String userToRemove, String chatName) throws Exception {
        commonSteps.UserXRemoveContactFromGroupChat(chatOwnerNameAlias, userToRemove, chatName);
    }

    /**
     * Adding user to group conversation
     *
     * @param chatOwnerNameAlias name of the user who is adding
     * @param userToAdd          name of the user to be added
     * @param chatName           name of the group conversation
     * @throws Exception
     * @step. ^(.*) added (.*) to group chat (.*)
     */
    @When("^(.*) added (.*) to group chat (.*)")
    public void UserXaddUserBToGroupChat(String chatOwnerNameAlias,
                                         String userToAdd, String chatName) throws Exception {
        commonSteps.UserXAddedContactsToGroupChat(chatOwnerNameAlias, userToAdd, chatName);
    }

    /**
     * User leaves group chat
     *
     * @param userName name of the user who leaves
     * @param chatName chat name that user leaves
     * @throws Exception
     * @step. ^(.*) leaves? group chat (.*)$
     */
    @Given("^(.*) leaves? group chat (.*)$")
    public void UserLeavesGroupChat(String userName, String chatName) throws Exception {
        commonSteps.UserXLeavesGroupChat(userName, chatName);
    }

    @Given("^(.*) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        commonSteps.UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^There \\w+ (\\d+) user[s]*$")
    public void ThereAreNUsers(int count) throws Exception {
        commonSteps.ThereAreNUsers(CURRENT_PLATFORM, count);
    }

    /**
     * Use this step if you have @fastLogin option set and you want the application to log in
     * under particular user, skipping the whole login flow in UI, which is supposed to be quite faster
     * in comparison to the "classic" flow
     *
     * @param alias user name/alias to sign in as. This user should have his email address registered on the backedn
     * @throws Exception
     * @step. ^I prepare Wire to perform fast log in by email as (.*)
     */
    @Given("^I prepare Wire to perform fast log in by email as (.*)")
    public void IDoFastLogin(String alias) throws Exception {
        final FastLoginContainer flc = FastLoginContainer.getInstance();
        if (!flc.isEnabled()) {
            throw new IllegalStateException(
                    String.format("Fast login should be enabled first in order to call this step." +
                            "Make sure you have the '%s' tag in your scenario", FastLoginContainer.TAG_NAME));
        }
        updateDriver(flc.executeDriverCreation(usrMgr.findUserByNameOrNameAlias(alias)));
    }

    @Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "default");
        final FastLoginContainer flc = FastLoginContainer.getInstance();
        if (flc.isEnabled()) {
            updateDriver(flc.executeDriverCreation(usrMgr.getSelfUserOrThrowError()));
        }
    }

    /**
     * Creates specified number of users and sets user with specified name as
     * main user. The user is registered with a phone number only and has no
     * email address attached
     *
     * @param count       number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with phone number
     * only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with phone number only$")
    public void ThereAreNUsersWhereXIsMeWithoutEmail(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
        final FastLoginContainer flc = FastLoginContainer.getInstance();
        if (flc.isEnabled()) {
            throw new IllegalStateException("Fast login feature is only supported in log in by email");
        }
    }

    /**
     * Creates specified number of users and sets user with specified name as
     * main user. The user is registered with a email only and has no phone
     * number attached
     *
     * @param count       number of users to create
     * @param myNameAlias user name or name alias to use as main user
     * @throws Exception
     * @step. ^There (?:is|are) (\\d+) users? where (.*) is me with email only$
     */
    @Given("^There (?:is|are) (\\d+) users? where (.*) is me with email only$")
    public void ThereAreNUsersWhereXIsMeWithoutPhone(int count, String myNameAlias) throws Exception {
        commonSteps.ThereAreNUsersWhereXIsMeRegOnlyByMail(count, myNameAlias);
        final FastLoginContainer flc = FastLoginContainer.getInstance();
        if (flc.isEnabled()) {
            updateDriver(flc.executeDriverCreation(usrMgr.getSelfUserOrThrowError()));
        }
    }

    @When("^(.*) ignore all requests$")
    public void IgnoreAllIncomingConnectRequest(String userToNameAlias) throws Exception {
        commonSteps.IgnoreAllIncomingConnectRequest(userToNameAlias);
    }

    /**
     * Cancel all outgoing connection requests in pending status
     *
     * @param userToNameAlias user name who will cancel requests
     * @throws Exception
     * @step. ^(.*) cancel all outgoing connection requests$
     */
    @When("^(.*) cancel all outgoing connection requests$")
    public void CancelAllOutgoingConnectRequest(String userToNameAlias) throws Exception {
        commonSteps.CancelAllOutgoingConnectRequests(userToNameAlias);
    }

    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        commonSteps.WaitForTime(seconds);
    }

    @When("^User (.*) blocks? user (.*)")
    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        commonSteps.BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
    }

    @When("^User (.*) archives? (single user|group) conversation (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias, String isGroup,
                                            String dstConvoName) throws Exception {
        if (isGroup.equals("group")) {
            commonSteps.ArchiveConversationWithGroup(userToNameAlias, dstConvoName);
        } else {
            commonSteps.ArchiveConversationWithUser(userToNameAlias, dstConvoName);
        }
    }

    /**
     * Silences conversation in backend
     *
     * @param userToNameAlias user that mutes the conversation
     * @param isGroup         equals to "group" if a group convo is going to be silenced
     * @param dstConvo        name of single conversation to mute
     * @throws Exception
     * @step. ^User (.*) silences? (single user|group) conversation (.*)
     */
    @When("^User (.*) silences? (single user|group) conversation (.*)")
    public void MuteConversationWithUser(String userToNameAlias, String isGroup,
                                         String dstConvo) throws Exception {
        if (isGroup.equals("group")) {
            commonSteps.MuteConversationWithGroup(userToNameAlias, dstConvo);
        } else {
            commonSteps.MuteConversationWithUser(userToNameAlias, dstConvo);
        }
    }

    /**
     * Rename conversation in backend
     *
     * @param user                username who renames
     * @param oldConversationName old conversation name string
     * @param newConversationName new conversation name string
     * @throws Exception
     * @step. ^User (.*) renames? conversation (.*) to (.*)$
     */
    @When("^User (.*) renames? conversation (.*) to (.*)$")
    public void UserChangeGruopChatName(String user, String oldConversationName, String newConversationName)
            throws Exception {
        commonSteps.ChangeGroupChatName(user, oldConversationName, newConversationName);
    }

    /**
     * Call method in BackEnd that should generate new verification code
     *
     * @param user user name
     * @throws Exception
     */
    @When("New verification code is generated for user (.*)")
    public void newVerificationCodeGenrated(String user) throws Exception {
        commonSteps.GenerateNewLoginCode(user);
    }

    @When("^(.*) accept all requests$")
    public void AcceptAllIncomingConnectionRequests(String userToNameAlias)
            throws Exception {
        commonSteps.AcceptAllIncomingConnectionRequests(userToNameAlias);
    }

    @Given("^User (\\w+) (securely )?pings conversation (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias, String isSecure,
                                       String dstConversationName) throws Exception {
        if (isSecure == null) {
            commonSteps.UserPingedConversation(pingFromUserNameAlias, dstConversationName);
        } else {
            commonSteps.UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
        }
    }

    @Given("^User (.*) sends (\\d+) (encrypted )?messages? to (user|group conversation) (.*)$")
    public void UserSendXMessagesToConversation(String msgFromUserNameAlias,
                                                int msgsCount, String areEncrypted,
                                                String conversationType,
                                                String conversationName) throws Exception {
        for (int i = 0; i < msgsCount; i++) {
            if (conversationType.equals("user")) {
                // 1:1 conversation
                if (areEncrypted == null) {
                    commonSteps.UserSentMessageToUser(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                } else {
                    commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE, null);
                }
            } else {
                // group conversation
                if (areEncrypted == null) {
                    commonSteps.UserSentMessageToConversation(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE);
                } else {
                    commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias,
                            conversationName, DEFAULT_AUTOMATION_MESSAGE, null);
                }
            }
        }
    }

    /**
     * Sends default message using device
     *
     * @param msgFromUserNameAlias username who sends message
     * @param msgsCount            messages count
     * @param deviceName           device name to send message from
     * @param conversationType     user or group conversation
     * @param conversationName     conversation name
     * @throws Exception
     * @step. ^User (.*) sends? (\d+) encrypted messages? using device (.*) to (user|group conversation) (.*)$
     */
    @Given("^User (.*) sends? (\\d+) encrypted messages? using device (.*) to (user|group conversation) (.*)$")
    public void UserSendXMessagesToConversationUsingDevice(String msgFromUserNameAlias,
                                                           int msgsCount, String deviceName,
                                                           String conversationType,
                                                           String conversationName) throws Exception {
        for (int i = 0; i < msgsCount; i++) {
            if (conversationType.equals("user")) {
                // 1:1 conversation
                commonSteps.UserSentOtrMessageToUser(msgFromUserNameAlias,
                        conversationName, DEFAULT_AUTOMATION_MESSAGE, deviceName);
            } else {
                // group conversation
                commonSteps.UserSentOtrMessageToConversation(msgFromUserNameAlias,
                        conversationName, DEFAULT_AUTOMATION_MESSAGE, deviceName);
            }
        }
    }

    /**
     * User A sends a simple text message to user/goup B
     *
     * @param userFromNameAlias the user who sends the message
     * @param areEncrypted      whether the message has to be encrypted
     * @param msg               a message to send. Random string will be sent if it is empty
     * @param conversationType  either 'user' or 'group conversation'
     * @param conversationName  The user/group chat to receive the message
     * @throws Exception
     * @step. ^User (.*) sends? (encrypted )?message "(.*)" to (user|group conversation) (.*)$
     */
    @Given("^User (.*) sends? (encrypted )?message \"(.*)\" to (user|group conversation) (.*)$")
    public void UserSentMessageToConversation(String userFromNameAlias,
                                              String areEncrypted, String msg,
                                              String conversationType, String conversationName) throws Exception {
        if (conversationType.equals("user")) {
            // 1:1 conversation
            if (areEncrypted == null) {
                commonSteps.UserSentMessageToConversation(userFromNameAlias, conversationName, msg);
            } else {
                commonSteps.UserSentOtrMessageToConversation(userFromNameAlias, conversationName, msg, null);
            }
        } else {
            // group conversation
            if (areEncrypted == null) {
                commonSteps.UserSentMessageToConversation(userFromNameAlias, conversationName, msg);
            } else {
                commonSteps.UserSentOtrMessageToConversation(userFromNameAlias, conversationName, msg, null);
            }
        }
    }

    @When("^User (\\w+) changes? avatar picture to (.*)$")
    public void IChangeUserAvatarPicture(String userNameAlias, String name)
            throws Exception {
        final String rootPath = getImagesPathFromConfig(getClass());
        commonSteps.IChangeUserAvatarPicture(userNameAlias, rootPath
                + "/"
                + (name.toLowerCase().equals("default") ? DEFAULT_USER_AVATAR
                : name));
    }

    /**
     * Change user name on the backend
     *
     * @param userNameAlias user's name alias to change
     * @param newName       new given name
     * @throws Exception
     * @step. ^User (\\w+) changes? name to (.*)$
     */
    @When("^User (\\w+) changes? name to (.*)$")
    public void IChangeUserName(String userNameAlias, String newName)
            throws Exception {
        commonSteps.IChangeUserName(userNameAlias, newName);
    }

    @When("^User (\\w+) changes? accent color to (.*)$")
    public void IChangeAccentColor(String userNameAlias, String newColor) throws Exception {
        commonSteps.IChangeUserAccentColor(userNameAlias, newColor);
    }

    @Given("^There \\w+ (\\d+) shared user[s]* with name prefix (\\w+)$")
    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
            throws Exception {
        commonSteps.ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
    }

    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        commonSteps.UserXIsMe(nameAlias);
    }

    @Given("^(\\w+) waits? until (.*) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Waits until user becomes first search result
     *
     * @param searchByNameAlias user to search for
     * @param query             to send to the BE
     * @throws Exception
     * @step. ^(\w+) waits? until (.*) is first search result on backend$
     */
    @Given("^(\\w+) waits? until (.*) is first search result on backend$")
    public void UserWaitsUntilContactIsFirstSearchResult(String searchByNameAlias, String query) throws Exception {
        commonSteps.WaitUntilContactIsSuggestedInSearchResult(searchByNameAlias, query);
    }

    @Given("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
                                               String isEncrypted,
                                               String imageFileName, String conversationType,
                                               String dstConversationName) throws Exception {
        final String imagePath = CommonUtils.getImagesPathFromConfig(this.getClass()) + File.separator + imageFileName;
        final boolean isGroup = conversationType.equals("group");
        if (isEncrypted == null) {
            commonSteps.UserSentImageToConversation(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        } else {
            commonSteps.UserSentImageToConversationOtr(imageSenderUserNameAlias,
                    imagePath, dstConversationName, isGroup);
        }
    }

    /**
     * Rotate device to landscape
     *
     * @param orientation must be landscape or portrait
     * @throws Exception
     * @step. ^I rotate UI to (landscape|portrait)$
     */
    @When("^I rotate UI to (landscape|portrait)$")
    public void WhenIRotateUILandscape(ScreenOrientation orientation) throws Exception {
        pagesCollection.getCommonPage().rotateScreen(orientation);
        Thread.sleep(1000); // fix for animation
    }

    /**
     * A user adds another user to a group chat
     *
     * @param user          that adds someone to a chat
     * @param userToBeAdded user that gets added by someone
     * @param group         group chat you get added to
     * @throws Exception
     * @step. ^User (.*) adds [Uu]ser (.*) to group chat (.*)$
     */
    @When("^User (.*) adds [Uu]ser (.*) to group chat (.*)$")
    public void UserAddsUserToGroupChat(String user, String userToBeAdded, String group) throws Exception {
        commonSteps.UserXAddedContactsToGroupChat(user, userToBeAdded, group);
    }

    /**
     * Click Simulator window at the corresponding position
     *
     * @param strX float number 0 <= x <= 1, relative width position of click point
     * @param strY float number 0 <= y <= 1, relative height position of click point
     * @throws Exception
     * @step. ^I click at ([\d\.]+),([\d\.]+) of Simulator window$
     */
    @When("^I click at ([\\d\\.]+),([\\d\\.]+) of Simulator window$")
    public void ReturnToWireApp(String strX, String strY) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.clickAt(strX, strY, String.format("%.3f", DriverUtils.SINGLE_TAP_DURATION / 1000.0));
        } else {
            throw new PendingException("This step is not available for non-simulator devices");
        }
    }

    /**
     * Delete self profile picture
     *
     * @param nameAlias user name/alias
     * @throws Exception
     * @step. ^User (.*) removes his avatar picture$
     */
    @Given("^User (.*) removes his avatar picture$")
    public void UserRemovesAvatarPicture(String nameAlias) throws Exception {
        commonSteps.UserDeletesAvatarPicture(nameAlias);
    }

    /**
     * Verify whether currently visible alert contains particular text
     *
     * @param expectedText the text (or part of it) to verify
     * @throws Exception
     * @step. ^I verify the alert contains text (.*)
     */
    @Then("^I verify the alert contains text (.*)")
    public void IVerifyAlertContains(String expectedText) throws Exception {
        Assert.assertTrue(String.format("There is no '%s' text on the alert", expectedText),
                pagesCollection.getCommonPage().isAlertContainsText(expectedText));
    }

    /**
     * User adds a remote device to his list of devices
     *
     * @param userNameAlias user name/alias
     * @param deviceName    unique name of the device
     * @throws Exception
     * @step. User (.*) adds a new device (.*)$
     */
    @When("^User (.*) adds a new device (.*) with label (.*)$")
    public void UserAddRemoteDeviceToAccount(String userNameAlias,
                                             String deviceName, String label) throws Exception {
        commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, deviceName, label);
    }

    /**
     * User adds multiple devices to his list of devices
     *
     * @param userNameAlias user name/alias
     * @param deviceNames   unique name of devices, comma-separated list
     * @throws Exception
     * @step. User (.*) adds new devices (.*)
     */
    @When("^User (.*) adds new devices? (.*)")
    public void UserAddRemoteDeviceToAccount(String userNameAlias, String deviceNames) throws Exception {
        final List<String> names = CommonSteps.splitAliases(deviceNames);
        final int poolSize = 2;  // Runtime.getRuntime().availableProcessors()
        final ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        for (String name : names) {
            pool.submit(() -> {
                try {
                    commonSteps.UserAddsRemoteDeviceToAccount(userNameAlias, name, CommonUtils.generateRandomString(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
        final int secondsTimeout = (names.size() / poolSize + 1) * 60;
        if (!pool.awaitTermination(secondsTimeout, TimeUnit.SECONDS)) {
            throw new IllegalStateException(String.format(
                    "Devices '%s' were not created within %s seconds timeout", names, secondsTimeout));
        }
    }

    /**
     * Press Enter button on the keyboard if this is simulator or Commit button on the
     * on-screen keyboard if real device
     *
     * @throws Exception
     * @step. ^I press Enter key in Simulator window$
     */
    @When("^I press Enter key in Simulator window$")
    public void IPressEnterKey() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.pressEnterKey();
        } else {
            pagesCollection.getCommonPage().tapKeyboardCommitButton();
        }
    }

    /**
     * Check whether web browser is visible with particular url
     *
     * @param expectedUrl full web page URL
     * @throws Exception
     * @step. ^I see "(.*)" web page opened$
     */
    @Then("^I see \"(.*)\" web page opened$")
    public void ISeeWebPage(String expectedUrl) throws Exception {
        Assert.assertTrue(String.format("The expected URL '%s' has not been opened in web browser", expectedUrl),
                pagesCollection.getCommonPage().isWebPageVisible(expectedUrl));
    }

    /**
     * Tap the corresponding button to switch back to Wire app from browser view
     *
     * @throws Exception
     * @step. ^I tap Back To Wire button$
     */
    @When("^I tap Back To Wire button$")
    public void ITapBackToWire() throws Exception {
        pagesCollection.getCommonPage().tapBackToWire();
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
     * Pings BackEnd until user is indexed and available in top people
     *
     * @param searchByNameAlias user name to search string
     * @throws Exception
     * @step. ^(\w+) waits? until (?:his|my) Top People list is not empty on the backend$
     */
    @Given("^(\\w+) waits? until (?:his|my) Top People list is not empty on the backend$")
    public void UserWaitsUntilContactExistsInTopPeopleResults(String searchByNameAlias) throws Exception {
        commonSteps.WaitUntilTopPeopleContactsIsFoundInSearch(searchByNameAlias, 1);
    }

    /**
     * Tap OK/Cancel button on the current page
     *
     * @param action either 'confirm' or 'deny'
     * @throws Exception
     * @step. ^I (confirm|discard) my choice$
     */
    @When("^I (confirm|discard) my choice$")
    public void IDoChoice(String action) throws Exception {
        switch (action.toLowerCase()) {
            case "confirm":
                pagesCollection.getCommonPage().tapConfirmButton();
                break;
            case "discard":
                pagesCollection.getCommonPage().tapCancelButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Illegal action name: '%s'", action));
        }
    }

    /**
     * Execute Delete Conversation action on the particular device registered for this user
     *
     * @param userAs     user name/alias
     * @param convoType  either 'group' or 'single user'
     * @param convoName  conversation name
     * @param deviceName device name (this one should already exist)
     * @throws Exception
     * @step. ^User (.*) deletes? (single user|group) conversation (.*) using device (.*)
     */
    @Given("^User (.*) deletes? (single user|group) conversation (.*) using device (.*)")
    public void UserDeletedConversation(String userAs, String convoType, String convoName, String deviceName)
            throws Exception {
        commonSteps.UserClearsConversation(userAs, convoName, deviceName, convoType.equals("group"));
    }

    /**
     * Send an existing file to a conversation
     *
     * @param sender      user name/alias
     * @param isTemporary equals to null if the file is located in images directory. Otherwise it should belocated in
     *                    project.build.directory folder
     * @param fileName    the name of an existing file
     * @param mimeType    MIME type of the file, for example text/plain. Check
     *                    http://www.freeformatter.com/mime-types-list.html to get the full list of available MIME
     *                    types
     * @param convoType   either 'single user' or 'group'
     * @param convoName   conversation name
     * @param deviceName  the name of user device. The device will be created automatically if it does not exist yet
     * @throws Exception
     * @step. ^User (.*) sends? file (.*) having MIME type (.*) to (single user|group) conversation (.*) using device (.*)
     */
    @When("^User (.*) sends? (temporary )?file (.*) having MIME type (.*) to (single user|group) conversation (.*) using " +
            "device (.*)")
    public void UserSendsFile(String sender, String isTemporary, String fileName, String mimeType, String convoType,
                              String convoName, String deviceName) throws Exception {
        String root;
        if (isTemporary == null) {
            if (mimeType.toLowerCase().contains("image")) {
                root = CommonUtils.getImagesPathFromConfig(getClass());
            } else {
                root = CommonUtils.getAudioPathFromConfig(getClass());
            }
        } else {
            root = CommonUtils.getBuildPathFromConfig(getClass());
        }
        commonSteps.UserSentFileToConversation(sender, convoName, root + File.separator + fileName, mimeType,
                deviceName, convoType.equals("group"));
    }

    /**
     * Create random file in project.build.directory folder for further usage
     *
     * @param size file size. Can be float value. Example: 1MB, 2.00KB
     * @param name file name without extension
     * @param ext  file extension
     * @throws Exception
     * @step. ^I create temporary file (.*) in size with name "(.*)" and extension "(.*)"
     */
    @Given("^I create temporary file (.*) in size with name \"(.*)\" and extension \"(.*)\"$")
    public void ICreateTemporaryFile(String size, String name, String ext) throws Exception {
        final String tmpFilesRoot = CommonUtils.getBuildPathFromConfig(getClass());
        CommonUtils.createRandomAccessFile(tmpFilesRoot + File.separator + name + "." + ext, size);
    }

    /**
     * Locks screen without any time limit on real device
     * This is a asynchronous step.
     * TODO: expect to have timeout explicitly defined here
     *
     * @throws Exception
     * @step. ^I lock screen on real device$
     */
    @When("^I lock screen on real device$")
    public void ILockScreenOnRealDevice() throws Exception {
        pagesCollection.getCommonPage().lockScreenOnRealDevice();
    }

    /**
     * Tap Done button
     *
     * @throws Exception
     * @step. ^I tap Done button$
     */
    @When("^I tap Done button$")
    public void ITapDoneButton() throws Exception {
        pagesCollection.getCommonPage().tapDoneButton();
    }

    // Check ZIOS-6570 for more details
    private static final String SIMULATOR_VIDEO_MESSAGE_PATH = "/var/tmp/video.mp4";

    /**
     * Prepares the existing video file to be uploaded by iOS simulator
     *
     * @param name the name of an existing file. The file should be located in tools/img folder
     * @throws Exception
     * @step. ^I prepare (.*) to be uploaded as a video message$
     */
    @Given("^I prepare (.*) to be uploaded as a video message$")
    public void IPrepareVideoMessage(String name) throws Exception {
        final File srcVideo = new File(getAudioPathFromConfig(getClass()) + File.separator + name);
        if (!srcVideo.exists()) {
            throw new IllegalArgumentException(String.format("The file %s does not exist or is not accessible",
                    srcVideo.getCanonicalPath()));
        }
        final Path from = srcVideo.toPath();
        final Path to = Paths.get(SIMULATOR_VIDEO_MESSAGE_PATH);
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Send location sharing message
     *
     * @param userNameAlias sender name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  user name/alias or group conversation name
     * @param deviceName    destination device
     * @throws Exception
     * @step. ^User (.*) shares? the default location to (user|group conversation) (.*) via device (.*)
     */
    @When("^User (.*) shares? the default location to (user|group conversation) (.*) via device (.*)")
    public void UserXSharesLocationTo(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        commonSteps.UserSharesLocationTo(userNameAlias, dstNameAlias, convoType.equals("group conversation"),
                deviceName);
    }

    /**
     * Verify whether on-screen keyboard is visible or not
     *
     * @param shouldNotSee equals to null if the keyboard should be visible
     * @throws Exception
     * @step. ^I (do not )?see the on-screen keyboard$
     */
    @Then("^I (do not )?see the on-screen keyboard$")
    public void ISeeOnScreenKeyboard(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("On-screen keyboard is not visible", pagesCollection.getCommonPage().isKeyboardVisible());
        } else {
            Assert.assertTrue("On-screen keyboard is visible, but should be hidden",
                    pagesCollection.getCommonPage().isKeyboardInvisible());
        }
    }

    /**
     * Clicks the send button on the keyboard
     *
     * @param canSkip equals to null if this step should throw an error if the button is not available for tapping
     * @throws Exception
     * @step. ^I tap (?:Commit|Return|Send|Enter) button on the keyboard( if visible)?$
     */
    @When("^I tap (?:Commit|Return|Send|Enter) button on the keyboard( if visible)?$")
    public void ITapCommitButtonOnKeyboard(String canSkip) throws Exception {
        try {
            pagesCollection.getCommonPage().tapKeyboardCommitButton();
        } catch (IllegalStateException e) {
            if (canSkip != null) {
                return;
            }
            throw e;
        }
    }

    /**
     * User X delete message from User/Group via specified device
     * Note : The recent message means the recent message sent from specified device by SE, the device should online.
     *
     * @param userNameAlias user name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  destination user name/alias or group convo name
     * @throws Exception
     * @step. ^User (.*) deletes? the recent message from (user|group conversation) (.*)$
     */
    @When("^User (.*) deletes? the recent message from (user|group conversation) (.*)$")
    public void UserXDeleteLastMessage(String userNameAlias, String convoType, String dstNameAlias) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserDeleteLatestMessage(userNameAlias, dstNameAlias, null, isGroup);
    }

    /**
     * User X delete message from User/Group via specified device
     * Note : The recent message means the recent message sent from specified device by SE, the device should online.
     *
     * @param userNameAlias    user name/alias
     * @param deleteEverywhere not null means delete everywhere, otherwise delete local
     * @param convoType        either 'user' or 'group conversation'
     * @param dstNameAlias     destination user name/alias or group convo name
     * @param deviceName       source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) deletes? the recent message (everywhere )?from (user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) deletes? the recent message (everywhere )?from (user|group conversation) (.*) via device (.*)$")
    public void UserXDeleteLastMessage(String userNameAlias, String deleteEverywhere, String convoType,
                                       String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        boolean isDeleteEverywhere = deleteEverywhere != null;
        commonSteps.UserDeleteLatestMessage(userNameAlias, dstNameAlias, deviceName, isGroup, isDeleteEverywhere);
    }

    /**
     * Remember the recent message Id
     *
     * @param userNameAlias user name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  destination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) remembers? the recent message from (user|group conversation) (.*) via device (.*)$
     */
    @When("^User (.*) remembers? the recent message from (user|group conversation) (.*) via device (.*)$")
    public void UserXRemembersLastMessage(String userNameAlias, String convoType, String dstNameAlias, String deviceName)
            throws Exception {
        commonSteps.UserXRemembersLastMessage(userNameAlias, convoType.equals("group conversation"),
                dstNameAlias, deviceName);
    }

    /**
     * Check the remembered message is changed
     *
     * @param userNameAlias user name/alias
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  destination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     * @step. ^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is
     * changed( in \\d+ seconds?)?$
     */
    @Then("^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is " +
            "changed( in \\d+ seconds?)?$")
    public void UserXFoundLastMessageChanged(String userNameAlias, String convoType, String dstNameAlias,
                                             String deviceName, String waitDuration) throws Exception {
        final int durationSeconds = (waitDuration == null) ?
                CommonSteps.DEFAULT_WAIT_UNTIL_TIMEOUT_SECONDS
                : Integer.parseInt(waitDuration.replaceAll("[\\D]", ""));
        commonSteps.UserXFoundLastMessageChanged(userNameAlias, convoType.equals("group conversation"), dstNameAlias,
                deviceName, durationSeconds);
    }

    /**
     * Verify visibility of the corresponding badge item
     *
     * @param shouldNotSee equals to null if the corresponding item should be visible
     * @param itemName     the badge item name
     * @throws Exception
     * @step. ^I (do not )?see (Select All|Copy|Delete|Paste|Save|Edit|Like|Unlike) badge item$
     */
    @Then("^I (do not )?see (Select All|Copy|Delete|Paste|Save|Edit|Like|Unlike) badge item$")
    public void ISeeBadge(String shouldNotSee, String itemName) throws Exception {
        boolean result;
        if (shouldNotSee == null) {
            result = pagesCollection.getCommonPage().isBadgeItemVisible(itemName);
        } else {
            result = pagesCollection.getCommonPage().isBadgeItemInvisible(itemName);
        }
        Assert.assertTrue(String.format("The '%s' badge item is %s", itemName,
                (shouldNotSee == null) ? "not visible" : "still visible"), result);
    }

    /**
     * Tap on pointed badge item
     *
     * @param itemName the badge item name
     * @throws Exception
     * @step. ^I tap on (Select All|Copy|Save|Delete|Paste|Edit|Like|Unlike) badge item$
     */
    @When("^I tap on (Select All|Copy|Save|Delete|Paste|Edit|Like|Unlike) badge item$")
    public void ITapBadge(String itemName) throws Exception {
        pagesCollection.getCommonPage().tapBadgeItem(itemName);
    }

    /**
     * User X edit his own messages, be careful this message will not control the type of the message you edit.
     *
     * @param userNameAlias user name/alias
     * @param newMessage    the message you want to update to
     * @param convoType     either 'user' or 'group conversation'
     * @param dstNameAlias  estination user name/alias or group convo name
     * @param deviceName    source device name. Will be created if does not exist yet
     * @throws Exception
     */
    @When("^User (.*) edits? the recent message to \"(.*)\" from (user|group conversation) (.*) via device (.*)$")
    public void UserXEditLastMessage(String userNameAlias, String newMessage, String convoType,
                                     String dstNameAlias, String deviceName) throws Exception {
        boolean isGroup = convoType.equals("group conversation");
        commonSteps.UserUpdateLatestMessage(userNameAlias, dstNameAlias, newMessage, deviceName, isGroup);
    }

    /**
     * Verify the type of the recent message in the conversation
     *
     * @param msgFromUserNameAlias name/alias of message sender
     * @param dstConversationName  destination conversation name
     * @param expectedType         the expected conversation types. See the source of SE actors file, Type enumeration
     *                             for more details
     * @param deviceName           user's device name
     * @throws Exception
     */
    @Then("^User (.*) verifies that the most recent message type from (?:user|group conversation) (.*) is " +
            "(TEXT|TEXT_EMOJI_ONLY||ASSET|ANY_ASSET|VIDEO_ASSET|AUDIO_ASSET|KNOCK|MEMBER_JOIN|MEMBER_LEAVE|CONNECT_REQUEST|CONNECT_ACCEPTED|RENAME|MISSED_CALL|INCOMING_CALL|RICH_MEDIA|OTR_ERROR|OTR_IDENTITY_CHANGED|OTR_VERIFIED|OTR_UNVERIFIED|OTR_DEVICE_ADDED|STARTED_USING_DEVICE|HISTORY_LOST|LOCATION|UNKNOWN|RECALLED) " +
            "via device (.*)")
    public void UserXVerifiesRecentMessageType(String msgFromUserNameAlias, String dstConversationName,
                                               String expectedType, String deviceName) throws Exception {
        commonSteps.UserXVerifiesRecentMessageType(msgFromUserNameAlias, dstConversationName, deviceName, expectedType);
    }

    /**
     * Add email(s) into address book of a user and upload address book in backend
     * This step is used to directly upload contacts data to the backend without touching SE
     *
     * @param asUser name of the user where the address book is uploaded
     * @throws Exception
     * @step. ^User (.*) has (?: emails?|phone numbers?) (.*) in Address Book$
     */
    @Given("^User (.*) has (?:emails?|phone numbers?) (.*) in Address Book$")
    public void UserXHasEmailsInAddressBook(String asUser, String contacts)
            throws Exception {
        commonSteps.UserXHasContactsInAddressBook(asUser, contacts);
    }

    private Long recentMsgId = null;

    private WireDatabase getWireDb() throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(getClass())) {
            throw new IllegalStateException("This step is only supported on Simulator");
        }
        final List<File> files = IOSSimulatorHelper.locateFilesOnInternalFS(WireDatabase.DB_FILE_NAME);
        if (files.isEmpty()) {
            throw new IllegalStateException("The internal Wire database file cannot be located");
        }
        return new WireDatabase(files.get(0));
    }

    /**
     * Store the id of the recent message into the local variable
     *
     * @param fromContact user name/alias
     * @throws Exception
     * @step. ^I remember the state of the recent message from user (.*) in the local database$
     */
    @When("^I remember the state of the recent message from user (.*) in the local database$")
    public void IRememberMessageStateInLocalDatabase(String fromContact) throws Exception {
        final ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(fromContact);
        final WireDatabase db = getWireDb();
        this.recentMsgId = db.getRecentMessageId(dstUser).orElseThrow(
                () -> new IllegalStateException(
                        String.format("No messages from user %s have been found in the local database",
                                dstUser.getName()))
        );
    }

    /**
     * Verify whether the previously saved message has been properly removed from the local DB
     *
     * @throws Exception
     * @step. ^I verify the remembered message has been deleted from the local database$
     */
    @Then("^I verify the remembered message has been deleted from the local database$")
    public void IVerifyMessageStateInLocalDB() throws Exception {
        if (this.recentMsgId == null) {
            throw new IllegalStateException("Please remember the message state first");
        }
        final WireDatabase db = getWireDb();
        Assert.assertTrue("The previously remembered message appears to be improperly deleted " +
                        "from the local database",
                db.isMessageDeleted(this.recentMsgId));
    }

    private String recentMsg = null;

    /**
     * Store the id of the recent message into the local variable
     *
     * @param fromContact user name/alias
     * @throws Exception
     * @step. ^I remember the recent message from user (.*) in the local database$
     */
    @When("^I remember the recent message from user (.*) in the local database$")
    public void IRememberRecentMessageInLocalDatabase(String fromContact) throws Exception {
        final ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(fromContact);
        final WireDatabase db = getWireDb();
        this.recentMsgId = db.getRecentMessageId(dstUser).orElseThrow(
                () -> new IllegalStateException(
                        String.format("No messages from user %s have been found in the local database",
                                dstUser.getName()))
        );
        this.recentMsg = db.getMessageContent(this.recentMsgId);
    }

    /**
     * Verify whether the previously saved message has been properly removed from the local DB
     *
     * @throws Exception
     * @step. ^I verify the remembered message has (not )?been changed in the local database$
     */
    @Then("^I verify the remembered message has (not )?been changed in the local database$")
    public void IVerifyMessageChangedInLocalDB(String shouldNotChange) throws Exception {
        if (this.recentMsgId == null || this.recentMsg == null) {
            throw new IllegalStateException("Please remember the message first");
        }
        final WireDatabase db = getWireDb();
        final String currentMsg = db.getMessageContent(this.recentMsgId);
        if (shouldNotChange == null) {
            Assert.assertNotEquals(String.format(
                    "The previously remembered message should not be equal to the current one: " +
                            "'%s' == '%s'", this.recentMsg, currentMsg), this.recentMsg, currentMsg);
        } else {
            Assert.assertEquals(String.format(
                    "The previously remembered message should be equal to the current one: " +
                            "'%s' != '%s'", this.recentMsg, currentMsg), this.recentMsg, currentMsg);
        }
    }

    /**
     * User X react(like or unlike) the recent message in 1:1 conversation or group conversation
     *
     * @param userNameAlias User X's name or alias
     * @param reactionType  User X's reaction , could be like or unlike, be careful you should use like before unlike
     * @param dstNameAlias  the conversation which message is belong to
     * @throws Exception
     * @step. ^User (.*) (likes|unlikes) the recent message from (?:user|group conversation) (.*))$
     */
    @When("^User (.*) (likes|unlikes|reads) the recent message from (user|group conversation) (.*)$")
    public void UserReactLastMessage(String userNameAlias, String reactionType, String convType, String dstNameAlias)
            throws Exception {
        switch (reactionType.toLowerCase()) {
            case "likes":
                commonSteps.UserLikeLatestMessage(userNameAlias, dstNameAlias, null);
                break;
            case "unlikes":
                commonSteps.UserUnlikeLatestMessage(userNameAlias, dstNameAlias, null);
                break;
            case "reads":
                commonSteps.UserReadLastEphemeralMessage(userNameAlias, dstNameAlias, null,
                        convType.equals("group conversation"));
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the reaction type '%s'",
                        reactionType));
        }
    }

    /**
     * Minimizes/restores the App
     *
     * @param action either restore or minimize
     * @throws Exception
     * @step. ^I (minimize|restore) Wire$
     */
    @Given("^I (minimize|restore) Wire$")
    public void IMinimizeWire(String action) throws Exception {
        switch (action.toLowerCase()) {
            case "minimize":
                pagesCollection.getCommonPage().pressHomeButton();
                break;
            case "restore":
                pagesCollection.getCommonPage().restoreWire();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action keyword: '%s'", action));
        }
    }

    /**
     * Switch the corresponding conversation to ephemeral mode
     *
     * @param userAs      user name/alias
     * @param isGroup     whether is 1:1 or group conversation
     * @param convoName   conversation name
     * @param timeout     ephemeral messages timeout
     * @param timeMetrics either seconds or minutes
     * @throws Exception
     * @step. ^User (.*) switches (user|group conversation) (.*) to ephemeral mode with (\d+) (seconds?|minutes?) timeout$"
     */
    @Given("^User (.*) switches (user|group conversation) (.*) to ephemeral mode with (\\d+) (seconds?|minutes?) timeout$")
    public void UserSwitchesToEphemeralMode(String userAs, String isGroup, String convoName, int timeout,
                                            String timeMetrics) throws Exception {
        final long timeoutMs = timeMetrics.startsWith("minute") ? timeout * 60 * 1000 : timeout * 1000;
        commonSteps.UserSwitchesToEphemeralMode(userAs, convoName, timeoutMs, isGroup.equals("group conversation"),
                null);
    }
}