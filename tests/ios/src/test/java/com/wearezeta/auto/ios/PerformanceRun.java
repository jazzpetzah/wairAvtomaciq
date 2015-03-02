package com.wearezeta.auto.ios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/smoke_report.json", "com.wearezeta.auto.ios.tools.IOSZetaFormatter"}, tags  = { "@performance" } )
public class PerformanceRun {

}