package com.wearezeta.auto.web;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report",
		"json:target/smoke_report.json",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "~@mute",
		"~@bug", "@smoke" })
public class SmokeRun {

}
