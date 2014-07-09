package com.wearezeta.auto.osx;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:target/report", "json:target/report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags  = { "~@mute", "@smoke" } )
public class SmokeRun {

}
