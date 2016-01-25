package com.wearezeta.auto.ios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import com.wearezeta.auto.common.rc.RCTestcase;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", format = {
		"html:target/report", "json:target/report.json",
		"ccom.wearezeta.auto.common.ZetaFormatterr", "rerun:target/rerun.txt" }, tags = { RCTestcase.RC_TAG })
public class RCRun {

}
