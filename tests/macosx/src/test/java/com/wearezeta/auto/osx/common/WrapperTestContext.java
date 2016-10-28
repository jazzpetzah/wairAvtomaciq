package com.wearezeta.auto.osx.common;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.osx.OSXPagesCollection;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
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
    static Future<ZetaOSXDriver> COMPAT_OSX_DRIVER;

    private final OSXPagesCollection osxPagesCollection;
    private final Future<? extends RemoteWebDriver> osxDriver;

    public WrapperTestContext(String uniqueTestname, Future<? extends RemoteWebDriver> webdriver, Future<? extends RemoteWebDriver> osxDriver) throws Exception {
        super(uniqueTestname, webdriver);
        this.osxDriver = osxDriver;
        this.osxPagesCollection = new OSXPagesCollection();
    }

    /**
     * Constructor for downward compatibility with cucumber execution mechanisms
     */
    public WrapperTestContext() {
        super();
        this.osxDriver = COMPAT_OSX_DRIVER;
        this.osxPagesCollection = OSXPagesCollection.getInstance();
    }

    //TODO rename getPagesCollection() to getWebappPagesCollection() in TestContext
    public WebappPagesCollection getWebappPagesCollection() {
        return getPagesCollection();
    }
    public OSXPagesCollection getOSXPagesCollection() {
        return osxPagesCollection;
    }

    //TODO rename getFutureDriver() to getFutureWebDriver() in TestContext
    public Future<? extends RemoteWebDriver> getFutureWebDriver() {
        return getFutureDriver();
    }

    //TODO rename getDriver() to getWebDriver() in TestContext
    public RemoteWebDriver getWebDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return getDriver();
    }
    
    public Future<? extends RemoteWebDriver> getFutureOSXDriver() {
        return osxDriver;
    }

    public RemoteWebDriver getOSXDriver() throws InterruptedException, ExecutionException, TimeoutException {
        return osxDriver.get(DRIVER_INIT_TIMEOUT, TimeUnit.SECONDS);
    }
    
}
