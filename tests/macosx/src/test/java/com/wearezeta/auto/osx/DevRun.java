package com.wearezeta.auto.osx;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report", "json:target/report.json",
		"junit:target/surefire-reports/TEST-com.wearezeta.auto.macosx.DevRun.xml",
		"com.wearezeta.auto.common.ZetaFormatter" }, tags = { "@torun" })
public class DevRun {

}
