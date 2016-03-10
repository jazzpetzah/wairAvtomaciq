package com.wearezeta.auto.ios;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "html:target/report", "json:target/report.json",
		"com.wearezeta.auto.common.ZetaFormatter", "rerun:target/upgrade.txt" }, tags = { "@upgrade" })
public class UpgradeRun {

}
