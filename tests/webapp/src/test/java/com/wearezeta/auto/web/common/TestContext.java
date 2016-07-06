package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.sync_engine_bridge.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestContext {
    
    private static final Logger LOG = ZetaLogger.getLog(TestContext.class.getSimpleName());
    
    // IDLE_TIMEOUT 90s https://www.browserstack.com/automate/timeouts
    private static final long DRIVER_INIT_TIMEOUT = 91; //seconds
    
    static Future<ZetaWebAppDriver> COMPAT_WEB_DRIVER;

    private final Platform currentPlatform = Platform.Web;
    private final Pinger pinger;

    private final String testname;
    private final CommonSteps commonSteps;
    private final ClientUsersManager userManager;
    private final SEBridge deviceManager;
    private final CommonCallingSteps2 callingManager;
    private final WebappPagesCollection pagesCollection;
    private final Future<? extends RemoteWebDriver> driver;
    private final ConversationStates conversationStates;

    public TestContext(String uniqueTestname, Future<? extends RemoteWebDriver> driver) throws Exception {
        this.testname = uniqueTestname;
        this.driver = driver;
        this.userManager = new ClientUsersManager();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = new CommonSteps(userManager, deviceManager);
        this.callingManager = new CommonCallingSteps2(userManager);
        this.pagesCollection = new WebappPagesCollection();
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(driver);
    }

    /**
     * Constructor for downward compatibility with cucumber execution mechanisms
     */
    public TestContext() {
        this.testname = "";
        this.driver = COMPAT_WEB_DRIVER;
        this.userManager = ClientUsersManager.getInstance();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = CommonSteps.getInstance();
        this.callingManager = CommonCallingSteps2.getInstance();
        this.pagesCollection = WebappPagesCollection.getInstance();
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(driver);
    }

    public String getTestname() {
        return testname;
    }

    public CommonSteps getCommonSteps() {
        return commonSteps;
    }

    public ClientUsersManager getUserManager() {
        return userManager;
    }

    public SEBridge getDeviceManager() {
        return deviceManager;
    }

    public CommonCallingSteps2 getCallingManager() {
        return callingManager;
    }

    public WebappPagesCollection getPagesCollection() {
        return pagesCollection;
    }

    public Future<? extends RemoteWebDriver> getFutureDriver() {
        return driver;
    }

    public RemoteWebDriver getDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return driver.get(DRIVER_INIT_TIMEOUT, TimeUnit.SECONDS);
    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

    public ConversationStates getConversationStates() {
        return conversationStates;
    }
    
    public void startPinging() {
        pinger.startPinging();
    }
    public void stopPinging() {
        pinger.stopPinging();
    }
    
}
