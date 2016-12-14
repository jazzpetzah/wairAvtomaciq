package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.Platform;
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

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(TestContext.class.getSimpleName());

    // IDLE_TIMEOUT 90s https://www.browserstack.com/automate/timeouts
    private static final long DRIVER_INIT_TIMEOUT = 360; //seconds

    private final Platform currentPlatform = Platform.Web;
    private final Pinger pinger;

    private TestContext childContext;
    
    private final String testname;
    private final CommonSteps commonSteps;
    private final ClientUsersManager userManager;
    private final SEBridge deviceManager;
    private final CommonCallingSteps2 callingManager;
    private final LogManager logManager;
    private final AbstractPagesCollection<? extends BasePage> pagesCollection;
    private final Future<? extends RemoteWebDriver> driver;
    
    private final ConversationStates conversationStates;

    public TestContext(String uniqueTestname, Future<? extends RemoteWebDriver> driver) throws Exception {
        this.testname = uniqueTestname;
        this.driver = driver;
        this.childContext = null;
        this.userManager = new ClientUsersManager();
        this.deviceManager = SEBridge.getInstance();
        this.commonSteps = new CommonSteps(userManager, deviceManager);
        this.callingManager = new CommonCallingSteps2(userManager);
        this.pagesCollection = new WebappPagesCollection();

        this.logManager = new LogManager(this);
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(this);
    }

    private TestContext(String testname, CommonSteps commonSteps, ClientUsersManager userManager, SEBridge deviceManager,
            CommonCallingSteps2 callingManager, AbstractPagesCollection<? extends BasePage> pagesCollection,
            Future<? extends RemoteWebDriver> driver) {
        this.testname = testname;
        this.commonSteps = commonSteps;
        this.userManager = userManager;
        this.deviceManager = deviceManager;
        this.callingManager = callingManager;
        this.pagesCollection = pagesCollection;
        this.driver = driver;
        this.childContext = null;
        
        this.conversationStates = new ConversationStates();
        this.logManager = new LogManager(this);
        this.pinger = new Pinger(this);
    }

    public TestContext fromPrimaryContext(Future<? extends RemoteWebDriver> driver,
            AbstractPagesCollection<? extends BasePage> pagesCollection) throws Exception {
        setChildContext(new TestContext(this.testname, this.commonSteps, this.userManager, this.deviceManager, this.callingManager,
                pagesCollection, driver));
        return getChildContext();
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
        return (WebappPagesCollection) pagesCollection;
    }

    public <T extends AbstractPagesCollection<?>> T getPagesCollection(Class<T> type) {
        return (T) pagesCollection;
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
    
    public LogManager getLogManager() {
        return logManager;
    }

    public void startPinging() {
        pinger.startPinging();
    }

    public void stopPinging() {
        pinger.stopPinging();
    }

    public TestContext getChildContext() {
        return childContext;
    }

    private void setChildContext(TestContext childContext) {
        this.childContext = childContext;
    }
    
}
