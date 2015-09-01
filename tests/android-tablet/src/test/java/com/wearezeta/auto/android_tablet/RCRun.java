package com.wearezeta.auto.android_tablet;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import com.wearezeta.auto.common.rc.RCTestcase;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report", "json:target/report.json",
		"com.wearezeta.auto.common.ZetaFormatter",
		"rerun:target/rerun.txt" }, tags = { RCTestcase.RC_TAG })
public class RCRun {

}
