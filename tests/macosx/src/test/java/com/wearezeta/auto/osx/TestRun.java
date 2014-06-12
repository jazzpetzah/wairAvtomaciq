package com.wearezeta.auto.osx;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(format = {"html:report", "json:target/report.json"} )
public class TestRun {

}
