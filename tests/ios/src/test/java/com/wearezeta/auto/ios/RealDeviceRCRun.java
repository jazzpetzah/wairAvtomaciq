package com.wearezeta.auto.ios;

import com.wearezeta.auto.common.rc.RCTestcase;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/report.json",
        "com.wearezeta.auto.common.ZetaFormatter", "rerun:target/rerun.txt"},
        tags = {RCTestcase.REAL_RC_TAG})
public class RealDeviceRCRun {

}
