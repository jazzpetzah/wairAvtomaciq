package com.wearezeta.auto.android;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty", "html:target/report", "json:target/test_report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags  = { "@calling_advanced" } )
public class CallCrunchingAdvancedRun {

}