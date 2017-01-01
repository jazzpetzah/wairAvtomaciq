package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.test_context.TestContext;
import com.wearezeta.auto.common.wire_actors.SEBridge;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebAppTestContext extends TestContext {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(WebAppTestContext.class.getSimpleName());

    // IDLE_TIMEOUT 90s https://www.browserstack.com/automate/timeouts
    private static final long DRIVER_INIT_TIMEOUT = 360; //seconds

    private final Platform currentPlatform = Platform.Web;
    private final Pinger pinger;

    private WebAppTestContext childContext;
    
    private final String testname;
    private final LogManager logManager;
    private final Future<? extends RemoteWebDriver> driver;

    private final ConversationStates conversationStates;

    public WebAppTestContext(String uniqueTestname, Future<? extends RemoteWebDriver> driver) throws Exception {
        super(new WebappPagesCollection());
        this.testname = uniqueTestname;
        this.driver = driver;
        this.childContext = null;

        this.logManager = new LogManager(this);
        this.conversationStates = new ConversationStates();
        this.pinger = new Pinger(this);
    }

    private WebAppTestContext(String testname, CommonSteps commonSteps, ClientUsersManager userManager, SEBridge deviceManager,
                              CommonCallingSteps2 callingManager, AbstractPagesCollection<? extends BasePage> pagesCollection,
                              Future<? extends RemoteWebDriver> driver) throws Exception {
        super(userManager, deviceManager, callingManager, commonSteps, pagesCollection);
        this.testname = testname;
        this.driver = driver;
        this.childContext = null;
        
        this.conversationStates = new ConversationStates();
        this.logManager = new LogManager(this);
        this.pinger = new Pinger(this);
    }

    public WebAppTestContext fromPrimaryContext(Future<? extends RemoteWebDriver> driver,
                                                AbstractPagesCollection<? extends BasePage> pagesCollection)
            throws Exception {
        setChildContext(new WebAppTestContext(this.testname, this.getCommonSteps(), this.getUsersManager(),
                this.getDevicesManager(), this.getCallingManager(), pagesCollection, driver));
        return getChildContext();
    }

    public String getTestname() {
        return testname;
    }

    @Override
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

    public WebAppTestContext getChildContext() {
        return childContext;
    }

    private void setChildContext(WebAppTestContext childContext) {
        this.childContext = childContext;
    }
}
