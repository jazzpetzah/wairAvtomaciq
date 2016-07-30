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
import com.wearezeta.auto.ios.tools.FastLoginContainer;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import com.wearezeta.auto.ios.tools.RealDeviceHelpers;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import gherkin.formatter.model.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
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
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private static Logger log = ZetaLogger.getLog(CommonIOSSteps.class.getSimpleName());

    // We keep this short and compatible with spell checker
    public static final String DEFAULT_AUTOMATION_MESSAGE = "1 message";

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

    private static boolean isUseNativeInstrumentsEnabled() throws Exception {
        return Boolean.parseBoolean(getIsUseNativeInstrumentsLibFromConfig(
                CommonIOSSteps.class).orElseGet(() -> "false"));
    }

    private static String getAppName() throws Exception {
        return getIOSAppName(CommonIOSSteps.class);
    }

    private static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static Map<String, String> cachedBundleIds = new HashMap<>();

    @SuppressWarnings("unchecked")
    public Future<ZetaIOSDriver> resetIOSDriver(String ipaPath,
                                                Optional<Map<String, Object>> additionalCaps,
                                                int retriesCount) throws Exception {
        Optional<String> udid = Optional.empty();

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("nativeInstrumentsLib", isUseNativeInstrumentsEnabled());
        capabilities.setCapability("newCommandTimeout", AppiumServer.DEFAULT_COMMAND_TIMEOUT);
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        capabilities.setCapability("app", ipaPath);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("appName", getAppName());
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()));
        } else {
            // We don't really care about which particular real device model we have
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()).split("\\s+")[0]);
            udid = RealDeviceHelpers.getUDID();
            capabilities.setCapability("udid", udid.orElseThrow(
                    () -> new IllegalStateException("Cannot detect any connected iDevice")
            ));
        }
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("launchTimeout", ZetaIOSDriver.MAX_COMMAND_DURATION_MILLIS);
        final String backendType = getBackendType(this.getClass());
        final List<String> processArgs = new ArrayList<>(Arrays.asList(
                "-UseHockey", "0",
                "-ZMBackendEnvironmentType", backendType,
                // https://wearezeta.atlassian.net/browse/ZIOS-5769
                "--disable-autocorrection",
                // https://wearezeta.atlassian.net/browse/ZIOS-5259
                "-AnalyticsUserDefaultsDisabledKey", "0"
                // ,"--debug-log-network"
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
                    capabilities.setCapability(entry.getKey(), entry.getValue());
                }
            }
        }
        capabilities.setCapability("processArguments", String.join(" ", processArgs));

        if (!CommonUtils.getIsSimulatorFromConfig(getClass()) &&
                (capabilities.is("noReset") && !((Boolean) capabilities.getCapability("noReset")) ||
                        !capabilities.is("noReset"))) {
            // FIXME: Sometimes Appium fails to reset app prefs properly on real device
            if (!cachedBundleIds.containsKey(ipaPath)) {
                final File appPath = IOSCommonUtils.extractAppFromIpa(new File(ipaPath));
                try {
                    cachedBundleIds.put(ipaPath, IOSCommonUtils.getBundleId(
                            new File(appPath.getCanonicalPath() + File.separator + "Info.plist")));
                } finally {
                    FileUtils.deleteDirectory(appPath);
                }
            }
            RealDeviceHelpers.uninstallApp(udid.orElseThrow(
                    () -> new IllegalStateException("Cannot detect any connected iDevice")
            ), cachedBundleIds.get(ipaPath));
            capabilities.setCapability("fullReset", false);
        }

        return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance()
                .resetDriver(getUrl(), capabilities, retriesCount);
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
        if (!scenario.getSourceTagNames().contains("@noAcceptAlert")) {
            additionalCaps.put("autoAcceptAlerts", true);
        }

        String appPath = getAppPath();
        if (scenario.getSourceTagNames().contains("@upgrade")) {
            appPath = getOldAppPath();
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

        if (scenario.getSourceTagNames().contains("@upgrade")) {
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

    @When("^I press keyboard Delete button$")
    public void IPressKeyboardDeleteBtn() throws Exception {
        pagesCollection.getCommonPage().clickKeyboardDeleteButton();
        pagesCollection.getCommonPage().clickKeyboardDeleteButton();
    }

    @When("^I scroll up page a bit$")
    public void IScrollUpPageABit() throws Exception {
        pagesCollection.getCommonPage().smallScrollUp();
    }

    @When("^I accept alert$")
    public void IAcceptAlert() throws Exception {
        pagesCollection.getCommonPage().acceptAlertIfVisible();
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

    @When("^I dismiss alert$")
    public void IDismissAlert() throws Exception {
        pagesCollection.getCommonPage().dismissAlertIfVisible();
    }

    /**
     * click the corresponding on-screen keyboard button
     *
     * @param btnName button name, either Space or Hide or Done
     * @throws Exception
     * @step. ^I click (\\w+) keyboard button$
     */
    @When("^I click (\\w+) keyboard button$")
    public void IClickHideKeyboardBtn(String btnName) throws Exception {
        switch (btnName.toLowerCase()) {
            case "hide":
                pagesCollection.getCommonPage().clickHideKeyboardButton();
                break;
            case "space":
                pagesCollection.getCommonPage().clickSpaceKeyboardButton();
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
     * @step. ^I close the app for (.*) seconds$
     */
    @When("^I close the app for (\\d+) seconds$")
    public void ICloseApp(int seconds) throws Exception {
        pagesCollection.getCommonPage().minimizeApplication(seconds);
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
     * @step. ^(.*) removed (.*) from group chat (.*)$
     */
    @Given("^(.*) removed (.*) from group chat (.*)$")
    public void UserARemovedUserBFromGroupChat(String chatOwnerNameAlias,
                                               String userToRemove, String chatName) throws Exception {
        commonSteps.UserXRemoveContactFromGroupChat(chatOwnerNameAlias,
                userToRemove, chatName);
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

    @Given("^User (.*) sends (\\d+) encrypted messages? using device (.*) to (user|group conversation) (.*)$")
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

    @Given("^User (.*) sends (encrypted )?message \"(.*)\" to (user|group conversation) (.*)$")
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
        final String rootPath = getImagesPath(getClass());
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

    @Given("^User (.*) sends (encrypted )?image (.*) to (single user|group) conversation (.*)")
    public void ContactSendImageToConversation(String imageSenderUserNameAlias,
                                               String isEncrypted,
                                               String imageFileName, String conversationType,
                                               String dstConversationName) throws Exception {
        final String imagePath = CommonUtils.getImagesPath(this.getClass()) + File.separator + imageFileName;
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
    public void WhenIRotateUILandscape(ScreenOrientation orientation)
            throws Exception {
        pagesCollection.getCommonPage().rotateScreen(orientation);
        Thread.sleep(1000); // fix for animation
    }

    /**
     * Tap in center of the screen
     *
     * @throws Exception
     * @step. ^I tap on center of the screen$
     */
    @When("^I tap on center of the screen$")
    public void ITapOnCenterOfTheScreen() throws Exception {
        pagesCollection.getCommonPage().tapOnCenterOfScreen();
    }

    /**
     * Tap in top left corner of the screen
     *
     * @throws Exception
     * @step. ^I tap on top left corner of the screen$
     */
    @When("^I tap on top left corner of the screen$")
    public void ITapOnTopLeftCornerOfTheScreen() throws Exception {
        pagesCollection.getCommonPage().tapOnTopLeftScreen();
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
    @When("^User (.*) sends? (temporary )?file (.*) having MIME type (.*) to (single user|group) conversation (.*) using device (.*)")
    public void UserSendsFile(String sender, String isTemporary, String fileName, String mimeType, String convoType,
                              String convoName, String deviceName) throws Exception {
        String root;
        if (isTemporary == null) {
            root = CommonUtils.getImagesPath(getClass());
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
        final File srcVideo = new File(getImagesPath(getClass()) + File.separator + name);
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
     * @throws Exception
     * @step. ^I tap (?:Commit|Return|Send|Enter) button on the keyboard$
     */
    @When("^I tap (?:Commit|Return|Send|Enter) button on the keyboard$")
    public void ITapCommitButtonOnKeyboard() throws Exception {
        pagesCollection.getCommonPage().tapKeyboardCommitButton();
    }
}
