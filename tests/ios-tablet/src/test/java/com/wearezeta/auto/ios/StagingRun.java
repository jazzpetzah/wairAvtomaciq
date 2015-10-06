package com.wearezeta.auto.ios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", format = {
		"html:target/report", "json:target/report.json",
		"com.wearezeta.auto.ios.tools.IOSZetaFormatter" }, tags = { "@staging" })
public class StagingRun {

}
