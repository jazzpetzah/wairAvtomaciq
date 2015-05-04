package com.wearezeta.auto.osx;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(format = {"pretty", "html:target/report", "json:target/test_report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags  = { "@calling_basic" } )
public class CallCrunchingBasicRun {

}
