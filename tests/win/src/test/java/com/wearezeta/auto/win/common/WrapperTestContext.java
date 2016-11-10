package com.wearezeta.auto.win.common;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WrapperTestContext extends TestContext {
    
    private static final Logger LOG = ZetaLogger.getLog(WrapperTestContext.class.getSimpleName());
    
    // IDLE_TIMEOUT 90s https://www.browserstack.com/automate/timeouts
    private static final long DRIVER_INIT_TIMEOUT = 360; //seconds
    
    static Future<ZetaWebAppDriver> COMPAT_WEB_DRIVER;
    static Future<? extends RemoteWebDriver> COMPAT_WIN_DRIVER;

    private final WinPagesCollection winPagesCollection;
    private final Future<? extends RemoteWebDriver> winDriver;

    public WrapperTestContext(String uniqueTestname, Future<? extends RemoteWebDriver> webdriver, Future<? extends RemoteWebDriver> winDriver) throws Exception {
        super(uniqueTestname, webdriver);
        this.winDriver = winDriver;
        this.winPagesCollection = new WinPagesCollection();
    }

    /**
     * Constructor for downward compatibility with cucumber execution mechanisms
     */
    public WrapperTestContext() {
        super();
        this.winDriver = COMPAT_WIN_DRIVER;
        this.winPagesCollection = WinPagesCollection.getInstance();
    }

    //TODO rename getPagesCollection() to getWebappPagesCollection() in TestContext
    public WebappPagesCollection getWebappPagesCollection() {
        return getPagesCollection();
    }
    public WinPagesCollection getWinPagesCollection() {
        return winPagesCollection;
    }

    //TODO rename getFutureDriver() to getFutureWebDriver() in TestContext
    public Future<? extends RemoteWebDriver> getFutureWebDriver() {
        return getFutureDriver();
    }

    //TODO rename getDriver() to getWebDriver() in TestContext
    public RemoteWebDriver getWebDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return getDriver();
    }
    
    public Future<? extends RemoteWebDriver> getFutureWinDriver() {
        return winDriver;
    }

    public RemoteWebDriver getWinDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return winDriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.SECONDS);
    }
    
}
