package com.wearezeta.auto.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report", "json:target/report.json",
		"junit:target/surefire-reports/TEST-com.wearezeta.auto.web.LocalyticsRun.xml",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "@localytics" })
public class LocalyticsRun {

}
