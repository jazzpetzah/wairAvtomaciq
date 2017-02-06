package com.wearezeta.auto.ios.steps;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.common.driver.device_helpers.IOSRealDeviceHelpers;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDriverAPI;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.IOSDistributable;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.wire_actors.models.AssetsVersion;
import com.wearezeta.auto.ios.common.IOSPagesCollection;
import com.wearezeta.auto.ios.common.IOSTestContext;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.tools.*;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.en.Then;
import gherkin.formatter.model.Result;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.Capabilities;
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
    private static final String DEFAULT_USER_AVATAR = "android_dialog_sendpicture_result.png";
    // private static final String IOS_WD_APP_BUNDLE = "com.apple.test.WebDriverAgentRunner-Runner";
    // private static final String FACEBOOK_WD_APP_BUNDLE = "com.facebook.IntegrationApp";
    private static final String ADDRESSBOOK_HELPER_APP_NAME = "AddressbookApp.ipa";
    private static Logger log = ZetaLogger.getLog(CommonIOSSteps.class.getSimpleName());

    // We keep this short and compatible with spell checker
    public static final String DEFAULT_AUTOMATION_MESSAGE = "1 message";

    public static final String CAPABILITY_NAME_ADDRESSBOOK = "addressbookStart";
    public static final String TAG_NAME_ADDRESSBOOK = "@" + CAPABILITY_NAME_ADDRESSBOOK;

    public static final String CAPABILITY_NAME_NO_UNINSTALL = "noUninstall";
    public static final String TAG_NAME_UPGRADE = "@upgrade";

    public static final String CAPABILITY_NAME_FORCE_RESET = "forceReset";
    public static final String TAG_NAME_FORCE_RESET = "@" + CAPABILITY_NAME_FORCE_RESET;

    public static final String CAPABILITY_NAME_FORCE_RESET_AFTER_TEST = "forceResetAfterTest";
    public static final String TAG_NAME_FORCE_RESET_AFTER_TEST = "@" + CAPABILITY_NAME_FORCE_RESET_AFTER_TEST;

    public static final String CAPABILITY_NAME_ENABLE_LOCALYTICS_LOGS = "enableLocalyticsLogs";
    public static final String TAG_NAME_ENABLE_LOCALYTICS_LOGS = "@" + CAPABILITY_NAME_ENABLE_LOCALYTICS_LOGS;

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

    private static String getAppPath() {
        try {
            return getIosApplicationPathFromConfig(CommonIOSSteps.class);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
    }

    private static String getOldAppPath() {
        try {
            return getOldAppPathFromConfig(CommonIOSSteps.class);
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return null;
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

    private static final long INSTALL_DELAY_MS = 5000;

    // These settings are needed to properly sign WDA for real device tests
    // See https://github.com/appium/appium-xcuitest-driver for more details
    private static final File KEYCHAIN = new File(String.format("%s/%s",
            System.getProperty("user.home"), "/Library/Keychains/MyKeychain.keychain"));
    private static final String KEYCHAIN_PASSWORD = "123456";

    // https://github.com/wireapp/wire-ios/pull/339
    private static final String ARGS_FILE_NAME = "wire_arguments.txt";

    @SuppressWarnings("unchecked")
    public Future<ZetaIOSDriver> resetIOSDriver(String appPath,
                                                Optional<Map<String, Object>> additionalCaps,
                                                int retriesCount) throws Exception {
        final boolean isRealDevice = !CommonUtils.getIsSimulatorFromConfig(getClass());

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("newCommandTimeout", AppiumServer.DEFAULT_COMMAND_TIMEOUT.asSeconds());
        capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
        capabilities.setCapability(ZetaIOSDriver.AUTOMATION_NAME_CAPABILITY_NAME,
                ZetaIOSDriver.AUTOMATION_MODE_XCUITEST);
        capabilities.setCapability("app", appPath);
        capabilities.setCapability("appName", getAppName());
        capabilities.setCapability("bundleId", IOSDistributable.getInstance(appPath).getBundleId());
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("autoLaunch", false);
        capabilities.setCapability("clearSystemFiles", true);
        if (isRealDevice) {
            final String udid = IOSRealDeviceHelpers.getUDID();

            // We don't really care about which particular real device model we have
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()).split("\\s+")[0]);
            capabilities.setCapability("udid", udid);
            if (!IOSRealDeviceHelpers.IDEVICE_CONSOLE.exists()) {
                throw new IllegalStateException(
                        "ideviceconsole is expected to be installed: npm install -g deviceconsole"
                );
            }
            capabilities.setCapability("realDeviceLogger", IOSRealDeviceHelpers.IDEVICE_CONSOLE.getCanonicalPath());
            capabilities.setCapability("showXcodeLog", true);
            if (CommonUtils.isRunningInJenkinsNetwork()) {
                if (!KEYCHAIN.exists()) {
                    throw new IllegalStateException(
                            String.format("The default keychain file does not exist at '%s'", KEYCHAIN.getCanonicalPath())
                    );
                }
                capabilities.setCapability("keychainPath", KEYCHAIN.getCanonicalPath());
                capabilities.setCapability("keychainPassword", KEYCHAIN_PASSWORD);
            }
        } else {
            capabilities.setCapability("deviceName", getDeviceName(this.getClass()));
            // https://github.com/appium/appium-xcuitest-driver/pull/184/files
            capabilities.setCapability("iosInstallPause", INSTALL_DELAY_MS);
        }
        capabilities.setCapability("platformVersion", getPlatformVersion());
        capabilities.setCapability("launchTimeout", ZetaIOSDriver.MAX_SESSION_INIT_DURATION);
        final String backendType = getBackendType(this.getClass());
        final List<String> processArgs = new ArrayList<>(Arrays.asList(
                "-UseHockey", "0"
                , "-ZMBackendEnvironmentType", backendType
                // https://wearezeta.atlassian.net/browse/ZIOS-5769
                , "--disable-autocorrection"
                // https://wearezeta.atlassian.net/browse/ZIOS-5259
                , "-AnalyticsUserDefaultsDisabledKey", "0"
                // ,"--debug-log-network"
                , "-com.apple.CoreData.ConcurrencyDebug", "1"
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
                } else if (entry.getKey().equals(CAPABILITY_NAME_ENABLE_LOCALYTICS_LOGS) &&
                        (entry.getValue() instanceof Boolean) && (Boolean) entry.getValue()) {
                    processArgs.add("-ConsoleAnalytics");
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

        if (isRealDevice) {
            prepareRealDevice(capabilities);
        } else {
            prepareSimulator(capabilities, processArgs);
        }

        if (!capabilities.is(CAPABILITY_NAME_FORCE_RESET) && FBDriverAPI.isAlive()) {
            capabilities.setCapability(ZetaIOSDriver.CAPABILITY_NAME_USE_PREBUILT_WDA, true);
        }

        return (Future<ZetaIOSDriver>) PlatformDrivers.getInstance().resetDriver(
                getUrl(), capabilities, retriesCount, CommonIOSSteps::denyXCTestScreenshoting, null
        );
    }

    private static final File DERIVED_DATA_ROOT = new File(
            String.format("%s/Library/Developer/Xcode/DerivedData", System.getProperty("user.home"))
    );

    private static final String WDA_DERIVED_DATA_PREFIX = "WebDriverAgent";
    private static final String WDA_ATTACHMENTS_FOLDER_NAME = "Attachments";

    private static void findFolders(String name, File root, List<File> resultList) {
        final File[] list = root.listFiles();
        if (list == null) {
            return;
        }
        for (File fil : list) {
            if (fil.isDirectory()) {
                if (fil.getName().equals(name)) {
                    resultList.add(fil);
                }
                findFolders(name, fil, resultList);
            }
        }
    }

    private static void setReadOnlyPermissions(File dst) {
        try {
            final Set<PosixFilePermission> perms = Files.getPosixFilePermissions(dst.toPath());
            perms.removeAll(Arrays.asList(
                    PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OTHERS_WRITE)
            );
            log.debug(
                    String.format("Setting read-only permissions to '%s'...", dst.getAbsolutePath())
            );
            Files.setPosixFilePermissions(dst.toPath(), perms);
            log.debug("Permissions have been successfully set");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFolderContents(File folder) {
        final File[] list = folder.listFiles();
        if (list == null) {
            return;
        }
        for (File fil : list) {
            if (fil.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(fil);
                } catch (IOException e) {
                    // ignore silently
                }
            } else {
                fil.delete();
            }
        }
    }

    private static void denyXCTestScreenshoting(RemoteWebDriver drv) {
        if (!DERIVED_DATA_ROOT.exists()) {
            return;
        }
        final File[] list = DERIVED_DATA_ROOT.listFiles();
        if (list == null) {
            return;
        }
        List<File> dstFolders = new ArrayList<>();
        for (File fil : list) {
            if (fil.isDirectory() && fil.getName().startsWith(WDA_DERIVED_DATA_PREFIX)) {
                dstFolders.add(fil);
            }
        }
        for (File dstFolder : dstFolders) {
            List<File> attachmentFolders = new ArrayList<>();
            findFolders(WDA_ATTACHMENTS_FOLDER_NAME, dstFolder, attachmentFolders);
            attachmentFolders.forEach(CommonIOSSteps::deleteFolderContents);
            attachmentFolders.forEach(CommonIOSSteps::setReadOnlyPermissions);
        }
    }

    private static void prepareRealDevice(final Capabilities caps) throws Exception {
        final String udid = (String) caps.getCapability("udid");
        final IOSDistributable currentPackage =
                IOSDistributable.getInstance((String) caps.getCapability("app"));
        IOSRealDeviceHelpers.installApp(udid, !caps.is(CAPABILITY_NAME_NO_UNINSTALL), currentPackage.getAppRoot());
    }

    // We don't know Wire Application ID for sure,
    // so we do create tmp folder for every application under the current simulator
    private static List<File> createInternalWireTempRoots() throws Exception {
        final List<File> result = new ArrayList<>();
        for (File appRoot : IOSSimulatorHelpers.getInternalApplicationsRoots()) {
            final File tmpRoot = new File(String.format("%s/tmp", appRoot.getCanonicalPath()));
            tmpRoot.mkdirs();
            result.add(tmpRoot);
        }
        return result;
    }

    private static void prepareSimulator(final Capabilities caps, final List<String> args) throws Exception {
        if (caps.is(CAPABILITY_NAME_FORCE_RESET)) {
            IOSSimulatorHelpers.reset();
        }

        int ntry = 0;
        Exception storedException = null;
        do {
            try {
                if (ntry > 0) {
                    if (caps.is(CAPABILITY_NAME_NO_UNINSTALL)) {
                        IOSSimulatorHelpers.shutdown();
                    } else {
                        IOSSimulatorHelpers.reset();
                    }
                }
                IOSSimulatorHelpers.start();
                final IOSDistributable currentPackage =
                        IOSDistributable.getInstance((String) caps.getCapability("app"));
                if (!caps.is(CAPABILITY_NAME_NO_UNINSTALL)) {
                    IOSSimulatorHelpers.uninstallApp(currentPackage.getBundleId());
                }
                IOSSimulatorHelpers.installApp(currentPackage.getAppRoot());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                storedException = e;
            }
            ntry++;
        } while (ntry < DRIVER_CREATION_RETRIES_COUNT);
        if (ntry >= DRIVER_CREATION_RETRIES_COUNT) {
            throw new IllegalStateException(
                    String.format("Cannot start %s (%s) Simulator on %s after %s retries",
                            getDeviceName(CommonIOSSteps.class), getPlatformVersion(),
                            InetAddress.getLocalHost().getHostName(), DRIVER_CREATION_RETRIES_COUNT),
                    storedException
            );
        }
        // if (new DefaultArtifactVersion(getPlatformVersion()).compareTo(new DefaultArtifactVersion("10.0")) < 0) {
        // Workaround for https://github.com/appium/appium/issues/7091
        for (File tmpRoot : createInternalWireTempRoots()) {
            final File argsFile = new File(String.format("%s/%s", tmpRoot.getCanonicalPath(), ARGS_FILE_NAME));
            Files.write(argsFile.toPath(), String.join(" ", args).getBytes());
        }
    }

    @Before
    public void setUp(Scenario scenario) throws Exception {
        final IOSTestContext iosTestContext = new IOSTestContext(scenario, new IOSPagesCollection());
        IOSTestContextHolder.getInstance().setTestContext(iosTestContext);

        AppiumServer.getInstance().resetLog();

        if (scenario.getSourceTagNames().contains("@useSpecialEmail")) {
            iosTestContext.getUsersManager().useSpecialEmail();
        }

        if (!getIsSimulatorFromConfig(getClass())) {
            IOSRealDeviceHelpers.startLogListener();
        }

        final Map<String, Object> additionalCaps = new HashMap<>();
        String appPath = IOSDistributable.getInstance(getAppPath()).getAppRoot().getAbsolutePath();
        if (scenario.getSourceTagNames().contains(TAG_NAME_UPGRADE) ||
                scenario.getSourceTagNames().contains(TAG_NAME_ADDRESSBOOK)) {
            if (scenario.getSourceTagNames().contains(TAG_NAME_UPGRADE)) {
                appPath = IOSDistributable.getInstance(getOldAppPath()).getAppRoot().getAbsolutePath();
            }

            if (scenario.getSourceTagNames().contains(TAG_NAME_ADDRESSBOOK)) {
                additionalCaps.put(CAPABILITY_NAME_ADDRESSBOOK, true);
            }

            additionalCaps.put(CAPABILITY_NAME_FORCE_RESET, true);
        }

        if (scenario.getSourceTagNames().contains(TAG_NAME_ENABLE_LOCALYTICS_LOGS)) {
            additionalCaps.put(CAPABILITY_NAME_ENABLE_LOCALYTICS_LOGS, true);
        }

        if (scenario.getSourceTagNames().contains(TAG_NAME_FORCE_RESET)) {
            additionalCaps.put(CAPABILITY_NAME_FORCE_RESET, true);
        }

        if (scenario.getSourceTagNames().contains(FastLoginContainer.TAG_NAME)) {
            iosTestContext.getFastLoginContainer().enable(this::resetIOSDriver,
                    appPath,
                    Optional.of(additionalCaps),
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
        if (IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().hasPages()) {
            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().clearAllPages();
        }
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .setFirstPage(new LoginPage(lazyDriver));
    }

    @After
    public void tearDown(Scenario scenario) {
        final IOSTestContext iosTestContext = IOSTestContextHolder.getInstance().getTestContext();

        try {
            if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
                iosTestContext.getPagesCollection().getCommonPage().forceAcceptAlert();
                if (!scenario.getStatus().equals(Result.PASSED) && iosTestContext.getPagesCollection().hasPages()) {
                    iosTestContext.getPagesCollection().getCommonPage().printPageSource();
                }
                PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!scenario.getStatus().equals(Result.PASSED)) {
                if (getIsSimulatorFromConfig(getClass())) {
                    log.debug(IOSSimulatorHelpers.getLogsAndCrashes());
                } else {
                    log.debug(IOSRealDeviceHelpers.getLogs());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!getIsSimulatorFromConfig(getClass())) {
                IOSRealDeviceHelpers.stopLogListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (getIsSimulatorFromConfig(getClass())
                    && scenario.getSourceTagNames().contains(TAG_NAME_FORCE_RESET_AFTER_TEST)) {
                IOSSimulatorHelpers.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        iosTestContext.reset();

        log.debug("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
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
        customCaps.put(CAPABILITY_NAME_FORCE_RESET, false);
        customCaps.put(CAPABILITY_NAME_NO_UNINSTALL, true);
        final File currentBuildRoot = IOSDistributable.getInstance(getAppPath()).getAppRoot();
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                .installApp(currentBuildRoot);
        final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(
                currentBuildRoot.getAbsolutePath(),
                Optional.of(customCaps), 1);
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
                        .get(ZetaIOSDriver.MAX_COMMAND_DURATION.asMilliSeconds(), TimeUnit.MILLISECONDS);
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
        customCaps.put(CAPABILITY_NAME_FORCE_RESET, false);
        customCaps.put(CAPABILITY_NAME_NO_UNINSTALL, true);
        final Future<ZetaIOSDriver> lazyDriver = resetIOSDriver(
                IOSDistributable.getInstance(getAppPath()).getAppRoot().getAbsolutePath(),
                Optional.of(customCaps),
                1);
        updateDriver(lazyDriver);
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
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                            .acceptAlert();
                } else {
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                            .acceptAlertIfVisible();
                }
                break;
            case "dismiss":
                if (mayIgnore == null) {
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                            .dismissAlert();
                } else {
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                            .dismissAlertIfVisible();
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown alert action: '%s'", action));
        }
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
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                        .tapHideKeyboardButton();
                break;
            case "space":
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                        .tapSpaceKeyboardButton();
                break;
            case "done":
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                        .tapKeyboardCommitButton();
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
     * @step. ^I minimize Wire for (.*) seconds?$
     */
    @When("^I minimize Wire for (\\d+) seconds?$")
    public void IMinimizeWire(int seconds) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                .putWireToBackgroundFor(seconds);
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
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                .lockScreen(Timedelta.fromSeconds(seconds));
    }

    @Given("^(.*) sent connection request to (.*)$")
    public void GivenConnectionRequestIsSentTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ConnectionRequestIsSentTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^(.*) has group chat (.*) with (.*)$")
    public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
                                             String chatName, String otherParticipantsNameAlises) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName, otherParticipantsNameAlises);
    }

    /**
     * User leaves group chat
     *
     * @param userName name of the user who leaves
     * @param chatName chat name that user leaves
     * @throws Exception
     * @step. ^User (.*) leaves group chat (.*)$
     */
    @Given("^User (.*) leaves group chat (.*)$")
    public void UserLeavesGroupChat(String userName, String chatName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UserXLeavesGroupChat(userName, chatName);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserXRemoveContactFromGroupChat(chatOwnerNameAlias, userToRemove, chatName);
    }

    /**
     * Adding user to group conversation
     *
     * @param chatOwnerNameAlias name of the user who is adding
     * @param userToAdd          name of the user to be added
     * @param chatName           name of the group conversation
     * @throws Exception
     * @step. ^User (.*) adds (.*) to group chat (.*)
     */
    @When("^User (.*) adds (.*) to group chat (.*)")
    public void UserXDddsUserYToGroupChat(String chatOwnerNameAlias,
                                          String userToAdd, String chatName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserXAddedContactsToGroupChat(chatOwnerNameAlias, userToAdd, chatName);
    }

    @Given("^(.*) is connected to (.*)$")
    public void UserIsConnectedTo(String userFromNameAlias, String usersToNameAliases) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserIsConnectedTo(userFromNameAlias, usersToNameAliases);
    }

    @Given("^There (?:is|are) (\\d+) users?$")
    public void ThereAreNUsers(int count) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().ThereAreNUsers(CURRENT_PLATFORM, count);
    }

    @Given("^There (?:is|are) (\\d+) users? where (.*) is me$")
    public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ThereAreNUsersWhereXIsMe(CURRENT_PLATFORM, count, myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "", "default");
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(myNameAlias);
        final FastLoginContainer flc = IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer();
        if (flc.isEnabled()) {
            updateDriver(flc.executeDriverCreation(IOSTestContextHolder.getInstance()
                    .getTestContext().getUsersManager().getSelfUserOrThrowError()));
        }
    }

    /**
     * Assign a unique username
     *
     * @param userAs       user name/alias, who changes the username
     * @param action       either 'sets' or 'changes'. The uniqUsername parameter will be ignored if the action
     *                     is set to 'sets' and the autogenerated unique username, which is available
     *                     as 'user%dUniqueUsername' alias
     * @param uniqUsername unique username or an alias
     * @throws Exception
     * @step. User (.*) (sets|changes) the unique username( to ".*")?(?: via device (.*))?$
     */
    @Given("^User (.*) (sets|changes) the unique username( to \".*\")?(?: via device (.*))?$")
    public void UserSetsUniqueUsername(String userAs, String action, String uniqUsername, String deviceName) throws Exception {
        switch (action.toLowerCase()) {
            case "sets":
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(userAs);
                break;
            case "changes":
                if (uniqUsername == null) {
                    throw new IllegalArgumentException("Unique username is mandatory to set");
                }
                // Exclude quotes
                uniqUsername = uniqUsername.substring(5, uniqUsername.length() - 1);
                uniqUsername = IOSTestContextHolder.getInstance().getTestContext()
                        .getUsersManager().replaceAliasesOccurences(uniqUsername,
                                ClientUsersManager.FindBy.UNIQUE_USERNAME_ALIAS);
                if (deviceName == null) {
                    IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                            .IChangeUniqueUsername(userAs, uniqUsername);
                } else {
                    IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                            .UpdateUniqueUsername(userAs, uniqUsername, deviceName);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown action '%s'", action));

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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ThereAreNUsersWhereXIsMeWithPhoneNumberOnly(count, myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "", "default");
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(myNameAlias);
        final FastLoginContainer flc = IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer();
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ThereAreNUsersWhereXIsMeRegOnlyByMail(count, myNameAlias);
        IChangeUserAvatarPicture(myNameAlias, "", "default");
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersSetUniqueUsername(myNameAlias);
        final FastLoginContainer flc = IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer();
        if (flc.isEnabled()) {
            updateDriver(flc.executeDriverCreation(IOSTestContextHolder.getInstance().getTestContext()
                    .getUsersManager().getSelfUserOrThrowError()));
        }
    }

    /**
     * Cancel all outgoing connection requests in pending status
     *
     * @param userToNameAlias user name who will cancel requests
     * @throws Exception
     * @step. ^User (.*) cancels all outgoing connection requests$
     */
    @When("^User (.*) cancels all outgoing connection requests$")
    public void CancelAllOutgoingConnectRequest(String userToNameAlias) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .CancelAllOutgoingConnectRequests(userToNameAlias);
    }

    @When("^I wait for (\\d+) seconds?$")
    public void WaitForTime(int seconds) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().WaitForTime(seconds);
    }

    @When("^User (.*) blocks? user (.*)")
    public void BlockContact(String blockAsUserNameAlias,
                             String userToBlockNameAlias) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .BlockContact(blockAsUserNameAlias, userToBlockNameAlias);
    }

    @When("^User (.*) archives? (single user|group) conversation (.*)$")
    public void ArchiveConversationWithUser(String userToNameAlias, String isGroup,
                                            String dstConvoName) throws Exception {
        if (isGroup.equals("group")) {
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .ArchiveConversationWithGroup(userToNameAlias, dstConvoName);
        } else {
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .ArchiveConversationWithUser(userToNameAlias, dstConvoName);
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
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .MuteConversationWithGroup(userToNameAlias, dstConvo);
        } else {
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .MuteConversationWithUser(userToNameAlias, dstConvo);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ChangeGroupChatName(user, oldConversationName, newConversationName);
    }

    @Given("^User (\\w+) pings conversation (.*)$")
    public void UserPingedConversation(String pingFromUserNameAlias, String dstConversationName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserPingedConversationOtr(pingFromUserNameAlias, dstConversationName);
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
     * @step. ^User (.*) sends? (\d+) messages? using device (.*) to (user|group conversation) (.*)$
     */
    @Given("^User (.*) sends? (\\d+) messages? using device (.*) to (user|group conversation) (.*)$")
    public void UserSendXMessagesToConversationUsingDevice(String msgFromUserNameAlias,
                                                           int msgsCount, String deviceName,
                                                           String conversationType,
                                                           String conversationName) throws Exception {
        for (int i = 0; i < msgsCount; i++) {
            if (conversationType.equals("user")) {
                // 1:1 conversation
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .UserSentOtrMessageToUser(msgFromUserNameAlias,
                                conversationName, DEFAULT_AUTOMATION_MESSAGE, deviceName);
            } else {
                // group conversation
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .UserSentOtrMessageToConversation(msgFromUserNameAlias,
                                conversationName, DEFAULT_AUTOMATION_MESSAGE, deviceName);
            }
        }
    }

    /**
     * Change or set user profile picture
     *
     * @param userNameAlias user name/alias
     * @param protocol      one of possible protocol names. Empty string means that all active protocols should be affected
     * @param name          picture file name. Can be equal to 'default'
     * @throws Exception
     * @step. ^User (\w+) changes? (v2 |v3 |\s*)avatar picture to (.*)
     */
    @When("^User (\\w+) changes? (v2 |v3 |\\s*)avatar picture to (.*)")
    public void IChangeUserAvatarPicture(String userNameAlias, String protocol, String name) throws Exception {
        final String picturePath = String.format("%s/%s", getImagesPathFromConfig(getClass()),
                name.toLowerCase().equals("default") ? DEFAULT_USER_AVATAR : name);
        protocol = protocol.trim();
        switch (protocol) {
            case "":
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .IChangeUserAvatarPicture(userNameAlias, picturePath);
                break;
            case "v2":
            case "v3":
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .IChangeUserAvatarPicture(userNameAlias, picturePath,
                                AssetsVersion.valueOf(protocol.toUpperCase()));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown protocol name '%s'", protocol));
        }
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
    public void IChangeName(String userNameAlias, String newName)
            throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().IChangeName(userNameAlias, newName);
    }

    @When("^User (\\w+) changes? accent color to (.*)$")
    public void IChangeAccentColor(String userNameAlias, String newColor) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .IChangeUserAccentColor(userNameAlias, newColor);
    }

    @Given("^There (are|is) (\\d+) shared user[s]* with name prefix (\\w+)$")
    public void ThereAreNSharedUsersWithNamePrefix(int count, String namePrefix)
            throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .ThereAreNSharedUsersWithNamePrefix(count, namePrefix);
    }

    @Given("^User (\\w+) is [Mm]e$")
    public void UserXIsMe(String nameAlias) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UserXIsMe(nameAlias);
    }

    @Given("^(\\w+) waits? until (\\w+) exists in backend search results$")
    public void UserWaitsUntilContactExistsInHisSearchResults(
            String searchByNameAlias, String query) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .WaitUntilContactIsFoundInSearch(searchByNameAlias, query);
    }

    /**
     * Wait until contact friends are calculated on the backend
     *
     * @param userAsAlias          user name/alias who executes the search
     * @param query                user mame/alias who is supposed to have friends
     * @param expectedFriendsCount the minimum expected friends count
     * @throws Exception
     * @step. ^(\w+) waits? until (\w+) has (\d+) common friends?$
     */
    @Given("^(\\w+) waits? until (\\w+) has (\\d+) common friends? on the backend$")
    public void UserWaitForCommonFriends(String userAsAlias, String query, int expectedFriendsCount) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .WaitUntilCommonContactsIsGenerated(userAsAlias, query, expectedFriendsCount);
    }

    /**
     * Send Giphy image to a conversation
     *
     * @param senderNameAlias     sender name/alias
     * @param giphyTag            Giphy search tag
     * @param conversationType    either 'single user' or 'group'
     * @param dstConversationName conversation name
     * @throws Exception
     * @step. User (.*) sends Giphy animation with tag "(.*)" to (single user|group) conversation (.*)
     */
    @Given("^User (.*) sends Giphy animation with tag \"(.*)\" to (single user|group) conversation (.*)")
    public void UserSendsGiphy(String senderNameAlias, String giphyTag, String conversationType,
                               String dstConversationName) throws Exception {
        final boolean isGroup = conversationType.equals("group");
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSendsGiphy(senderNameAlias, dstConversationName, giphyTag, null, isGroup);
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
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                .rotateScreen(orientation);
        Thread.sleep(1000); // fix for animation
    }

    /**
     * Click Simulator window at the corresponding position
     *
     * @param strX float number 0 <= x <= 1, relative width position of click point
     * @param strY float number 0 <= y <= 1, relative height position of click point
     * @throws Exception
     * @step. ^I click at ([\d.]+),([\d.]+) of Simulator window$
     */
    @When("^I click at ([\\d.]+),([\\d.]+) of Simulator window$")
    public void ReturnToWireApp(String strX, String strY) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelpers.clickAt(strX, strY, String.format("%.3f", DriverUtils.SINGLE_TAP_DURATION / 1000.0));
        } else {
            throw new PendingException("This step is not available for non-simulator §s");
        }
    }

    /**
     * Tap at the corresponding point of the visible viewport
     *
     * @param percentX 0 <= percentX <= 100
     * @param percentY 0 <= percentY <= 100
     * @throws Exception
     * @step. ^I tap at (\d+)%,(\d+)% of the viewport size"
     */
    @When("^I tap at (\\d+)%,(\\d+)% of the viewport size")
    public void ITapAtPoint(int percentX, int percentY) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().tapScreenByPercents(percentX, percentY);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserDeletesAvatarPicture(nameAlias);
    }

    /**
     * Verify whether currently visible alert contains particular text
     *
     * @param shouldNotBeVisible equals to null if the alert text should be visible
     * @param expectedText       the text (or part of it) to verify
     * @throws Exception
     * @step. ^I (do not )?see alert contains text (.*)
     */
    @Then("^I (do not )?see alert contains text (.*)")
    public void ISeeAlertContains(String shouldNotBeVisible, String expectedText) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("There is no '%s' text on the alert", expectedText),
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getCommonPage().isAlertContainsText(expectedText));
        } else {
            Assert.assertTrue(String.format("There is '%s' text on the alert", expectedText),
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getCommonPage().isAlertDoesNotContainsText(expectedText));
        }
    }

    /**
     * Add one or more remote devices to one or more remote users
     *
     * @param mappingAsJson this should be valid JSON string. Keys are mandatory and
     *                      are interpreted as user names/aliases and values are device(s) info
     *                      mapped to these users. Device info objects can include following
     *                      optional fields:
     *                      name - device name (will be set to random unique value if not set)
     *                      label - the device label (will not be set if missing)
     *                      Examples:
     *                      {"user1Name" : [{}]}
     *                      {"user1Name" : [{}], "user2Name" : [{"name": "blabla", "label": "label"},
     *                      {"name": "blabla2", "label": "label2"}]}
     * @param mappingAsJson
     * @throws Exception
     * @step. ^Users? adds? devices? (.*)
     */
    @Given("^Users? adds? the following devices?: (.*)")
    public void UsersAddDevices(String mappingAsJson) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UsersAddDevices(mappingAsJson);
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
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage()
                        .isWebPageVisible(expectedUrl));
    }

    /**
     * Tap the corresponding button to switch back to Wire app from browser view
     *
     * @throws Exception
     * @step. ^I tap Back To Wire button$
     */
    @When("^I tap Back To Wire button$")
    public void ITapBackToWire() throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage().tapBackToWire();
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UserRemovesAllRegisteredOtrClients(userAs);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .WaitUntilTopPeopleContactsIsFoundInSearch(searchByNameAlias, 1);
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
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().tapConfirmButton();
                break;
            case "discard":
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().tapCancelButton();
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserClearsConversation(userAs, convoName, deviceName, convoType.equals("group"));
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSentFileToConversation(sender, convoName, root + File.separator + fileName, mimeType,
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
        CommonUtils.createRandomAccessFile(String.format("%s%s%s.%s", tmpFilesRoot, File.separator, name, ext), size);
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
        final FastLoginContainer flc = IOSTestContextHolder.getInstance().getTestContext().getFastLoginContainer();
        if (!flc.isEnabled()) {
            throw new IllegalStateException(
                    String.format("Fast login should be enabled first in order to call this step." +
                            "Make sure you have the '%s' tag in your scenario", FastLoginContainer.TAG_NAME));
        }
        updateDriver(flc.executeDriverCreation(IOSTestContextHolder.getInstance().getTestContext()
                .getUsersManager().findUserByNameOrNameAlias(alias)));
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSharesLocationTo(userNameAlias, dstNameAlias, convoType.equals("group conversation"),
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
            Assert.assertTrue("On-screen keyboard is not visible",
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getCommonPage().isKeyboardVisible());
        } else {
            Assert.assertTrue("On-screen keyboard is visible, but should be hidden",
                    IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                            .getCommonPage().isKeyboardInvisible());
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
            IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getCommonPage().tapKeyboardCommitButton();
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserDeleteLatestMessage(userNameAlias, dstNameAlias, null, isGroup);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserDeleteLatestMessage(userNameAlias, dstNameAlias, deviceName, isGroup, isDeleteEverywhere);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserXRemembersLastMessage(userNameAlias, convoType.equals("group conversation"),
                        dstNameAlias, deviceName);
    }

    /**
     * Check the remembered message is changed
     *
     * @param userNameAlias      user name/alias
     * @param convoType          either 'user' or 'group conversation'
     * @param dstNameAlias       destination user name/alias or group convo name
     * @param shouldNotBeChanged equals to null if the message is expected to be changed
     * @param deviceName         source device name. Will be created if does not exist yet
     * @param waitDuration       how much seconds to wait until the event happens
     * @throws Exception
     * @step. ^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is
     * (not )?changed( in \\d+ seconds?)?$
     */
    @Then("^User (.*) sees? the recent message from (user|group conversation) (.*) via device (.*) is " +
            "(not )?changed( in \\d+ seconds?)?$")
    public void UserXFoundLastMessageChanged(String userNameAlias, String convoType, String dstNameAlias,
                                             String deviceName, String shouldNotBeChanged, String waitDuration)
            throws Exception {
        final int durationSeconds = (waitDuration == null) ?
                CommonSteps.DEFAULT_WAIT_UNTIL_TIMEOUT.asSeconds()
                : Integer.parseInt(waitDuration.replaceAll("[\\D]", ""));
        final boolean isGroup = convoType.equals("group conversation");
        if (shouldNotBeChanged == null) {
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .UserXFoundLastMessageChanged(userNameAlias, isGroup, dstNameAlias, deviceName,
                            durationSeconds);
        } else {
            IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                    .UserXFoundLastMessageNotChanged(userNameAlias, isGroup, dstNameAlias, deviceName,
                            durationSeconds);
        }
    }

    /**
     * Verify visibility of the corresponding badge item
     *
     * @param shouldNotSee equals to null if the corresponding item should be visible
     * @param itemName     the badge item name
     * @throws Exception
     * @step. ^I (do not )?see (Select All|Copy|Delete|Paste|Save|Edit|Like|Unlike|Forward|Share) badge item$
     */
    @Then("^I (do not )?see (Select All|Copy|Delete|Paste|Save|Edit|Like|Unlike|Forward|Share) badge item$")
    public void ISeeBadge(String shouldNotSee, String itemName) throws Exception {
        boolean result;
        if (shouldNotSee == null) {
            result = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getCommonPage().isBadgeItemVisible(itemName);
        } else {
            result = IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                    .getCommonPage().isBadgeItemInvisible(itemName);
        }
        Assert.assertTrue(String.format("The '%s' badge item is %s", itemName,
                (shouldNotSee == null) ? "not visible" : "still visible"), result);
    }

    /**
     * Tap on pointed badge item
     *
     * @param itemName the badge item name
     * @throws Exception
     * @step. ^I tap on (Select All|Copy|Save|Delete|Paste|Edit|Like|Unlike|Forward|Reveal|Share) badge item$
     */
    @When("^I tap on (Select All|Copy|Save|Delete|Paste|Edit|Like|Unlike|Forward|Reveal|Share) badge item$")
    public void ITapBadge(String itemName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage().tapBadgeItem(itemName);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserUpdateLatestMessage(userNameAlias, dstNameAlias, newMessage, deviceName, isGroup);
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserXVerifiesRecentMessageType(msgFromUserNameAlias, dstConversationName, deviceName, expectedType);
    }

    private Long recentMsgId = null;

    private WireDatabase getWireDb() throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(getClass())) {
            throw new IllegalStateException("This step is only supported on Simulator");
        }
        final List<File> files = IOSSimulatorHelpers.locateFilesOnInternalFS(WireDatabase.DB_FILE_NAME);
        final String currentBundleId = IOSDistributable.getInstance(getAppPath()).getBundleId();
        final Optional<File> matchedFile = files.stream().filter(
                x -> x.getParentFile().getName().equals(currentBundleId)
        ).findFirst();
        return new WireDatabase(matchedFile.orElseThrow(
                () -> new IllegalStateException(
                        String.format("The internal Wire database file cannot be located in\n%s", files)
                ))
        );
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
        final ClientUser dstUser = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(fromContact);
        final WireDatabase db = getWireDb();
        this.recentMsgId = db.getRecentMessageId(dstUser).orElseThrow(
                () -> new IllegalStateException(
                        String.format("No messages from user %s have been found in the local database",
                                dstUser.getName())
                )
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
        final ClientUser dstUser = IOSTestContextHolder.getInstance().getTestContext().getUsersManager()
                .findUserByNameOrNameAlias(fromContact);
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
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .UserLikeLatestMessage(userNameAlias, dstNameAlias, null);
                break;
            case "unlikes":
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .UserUnlikeLatestMessage(userNameAlias, dstNameAlias, null);
                break;
            case "reads":
                IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                        .UserReadLastEphemeralMessage(userNameAlias, dstNameAlias, null,
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
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage().pressHomeButton();
                break;
            case "restore":
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection().getCommonPage().restoreWire();
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
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSwitchesToEphemeralMode(userAs, convoName, timeoutMs, isGroup.equals("group conversation"),
                        null);
    }

    /**
     * Verify visibility of default Map application
     *
     * @throws Exception
     * @step. ^I see map application is opened$
     */
    @Then("^I see map application is opened$")
    public void VerifyMapDefaultApplicationVisibility() throws Exception {
        Assert.assertTrue("The default map application is not visible",
                IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                        .getCommonPage().isDefaultMapApplicationVisible());
    }


    /**
     * Tap the corresponding key on Emoji keyboard
     *
     * @param keyName The name of the corresponding key
     * @throws Exception
     * @step. ^I tap "(.*)" key on Emoji Keyboard$
     */
    @When("^I tap \"(.*)\" key on Emoji Keyboard$")
    public void TapKeyOnEmojiKeyboard(String keyName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().tapEmojiKeyboardKey(keyName);
    }

    /**
     * Puts Wire into the background on real device
     *
     * @throws Exception
     * @step. ^I press Home button$
     */
    @Given("^I press Home button$")
    public void IPressHomeButton() throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getCommonPage().pressHomeButton();
    }

    /**
     * Switch the Actor's AssetProtocol mode
     *
     * @param userAs     user name/alias
     * @param mode       V2 or V3
     * @param deviceName device name
     * @throws Exception
     * @step. User (.*) switches assets to (V2|V3) protocol(?: via device (.*))?$
     */
    @Given("^User (.*) switches assets to (V2|V3) protocol(?: via device (.*))?$")
    public void UserSwitchAssetMode(String userAs, String mode, String deviceName) throws Exception {
        AssetsVersion asset = AssetsVersion.valueOf(mode.toUpperCase());
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().UserSetAssetMode(userAs, asset, deviceName);
    }

    /**
     * Upload self user properties (name and email) to /onboarding endpoint.
     * This is mandatory to be able to get matches
     *
     * @param aliases self users alias(es)
     * @throws Exception
     * @step. ^Users? (.*) uploads? own details$
     */
    @Given("^Users? (.*) uploads? own details$")
    public void uploadSelfUser(String aliases) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().uploadSelfContact(aliases);
    }

    /**
     * Simulates isTyping event from the other user
     *
     * @param userAs       user name/alias
     * @param conversation conversation name
     * @throws Exception
     * @step. ^User (.*) starts typing in conversation (.*)
     */
    @When("^User (.*) starts typing in conversation (.*)")
    public void UserXStartsTyping(String userAs, String conversation) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps().
                UserIsTypingInConversation(userAs, conversation);
    }

    /**
     * Send multiple images or videos to the conversation
     *
     * @param senderUserNameAlias sender name/alias
     * @param count               count of items to send
     * @param fileName            the name of existing media file.
     *                            Only m4a/mp4 files can be set for audio and video types
     * @param fileType            one of possible file types
     * @param dstConversationName destination conversation name
     * @throws Exception
     * @step. ^User (.*) sends (\d+) (image|video|audio|temporary) files? (.*) to conversation (.*)
     */
    @Given("^User (.*) sends (\\d+) (image|video|audio|temporary) files? (.*) to conversation (.*)")
    public void UserSendsMultiplePictures(String senderUserNameAlias, int count,
                                          String fileType, String fileName,
                                          String dstConversationName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSendMultipleMedias(senderUserNameAlias, count, fileType, fileName, dstConversationName);
    }

    /**
     * Send multiple messages to the conversation
     *
     * @param senderUserNameAlias sender name/alias
     * @param count               count of text messages to send
     * @param msg                 either 'default' to send the default message or the actual messages
     *                            enclosed by double quotes
     * @param dstConversationName destination conversation name
     * @throws Exception
     * @step. ^User (.*) sends (\d+) (default|".*") messages? to conversation (.*)
     */
    @Given("^User (.*) sends (\\d+) (default|\".*\") messages? to conversation (.*)")
    public void UserSendsMultipleMessages(String senderUserNameAlias, int count,
                                          String msg, String dstConversationName) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserSendsMultipleMessages(senderUserNameAlias, count, msg, dstConversationName,
                        CommonIOSSteps.DEFAULT_AUTOMATION_MESSAGE);
    }

    /**
     * Verify user details on the backend
     *
     * @step. ^I verify user's (.*) (email|name|unique username) on the backend is equal to (.*)
     * @param user user name/alias
     * @param detail one of possible values
     * @param expectedValue the expected value/value alias. One may set it to CommonSteps.USER_DETAIL_NOT_SET
     *                      constant to verify that the corresponding value is not set on the backend
     * @throws Exception
     */
    @Then("^I verify user's (.*) (email|name|unique username|phone number) on the backend is equal to (.*)")
    public void IVerifyDetailsOnBackend(String user, String detail, String expectedValue) throws Exception {
        IOSTestContextHolder.getInstance().getTestContext().getCommonSteps()
                .UserVerifiesDetails(user, detail, expectedValue);
    }
}