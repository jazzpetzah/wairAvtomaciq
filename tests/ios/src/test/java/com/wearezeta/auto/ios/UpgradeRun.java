package com.wearezeta.auto.ios;

import com.wearezeta.auto.ios.steps.CommonIOSSteps;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/report.json",
        "com.wearezeta.auto.common.ZetaFormatter", "rerun:target/upgrade.txt"},
        tags = {CommonIOSSteps.TAG_NAME_UPGRADE})
public class UpgradeRun {

}
