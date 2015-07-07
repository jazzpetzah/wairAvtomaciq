package com.wearezeta.auto.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report",
		"json:target/smoke_report.json",
		"junit:target/surefire-reports/TEST-com.wearezeta.auto.web.SmokeRun.xml",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "~@mute",
		"~@bug", "@smoke" })
public class SmokeRun {

}
