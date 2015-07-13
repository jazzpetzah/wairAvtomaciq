package com.wearezeta.auto.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report",
		"json:target/regression_report.json",
		"junit:target/surefire-reports/TEST-com.wearezeta.auto.web.RegressionRun.xml",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "~@mute",
		"~@bug", "@regression" })
public class RegressionRun {

}
