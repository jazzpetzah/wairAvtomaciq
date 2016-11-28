package com.wearezeta.auto.ios;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty", "html:target/report",
        "json:target/test_report.json",
        "com.wearezeta.auto.common.ZetaFormatter" }, tags = { "@consecutive_call" })
public class CallCrunchingConsecutiveRun {
}
