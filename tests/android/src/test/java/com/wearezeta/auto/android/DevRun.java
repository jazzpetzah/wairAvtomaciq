package com.wearezeta.auto.android;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = {/*"com.wearezeta.auto.common.ZetaFormatter"*/"html:target/report", "json:target/report.json", "com.wearezeta.auto.common.ZetaFormatter"}, tags = { "@torun" })
public class DevRun {

}
