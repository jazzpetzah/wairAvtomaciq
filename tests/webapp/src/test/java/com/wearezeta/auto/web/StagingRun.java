package com.wearezeta.auto.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report",
		"json:target/staging_report.json",
		"junit:target/surefire-reports/TEST-com.wearezeta.auto.web.StagingRun.xml",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "@staging" })
public class StagingRun {

}
