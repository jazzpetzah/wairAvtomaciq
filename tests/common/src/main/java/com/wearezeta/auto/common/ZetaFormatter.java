package com.wearezeta.auto.common;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

import org.openqa.selenium.remote.RemoteWebDriver;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

public class ZetaFormatter implements Formatter, Reporter {
	
	private static RemoteWebDriver driver = null;
	
	private String feature = "";
	private String scenario = "";
	private Queue<String> step = new LinkedList<String>();

	@Override
	public void background(Background arg0) {
		
	}

	@Override
	public void close() {
	
	}

	@Override
	public void done() {
	
	}

	@Override
	public void eof() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void examples(Examples arg0) {
		
	}

	@Override
	public void feature(Feature arg0) {
		feature = arg0.getName();
	}

	@Override
	public void scenario(Scenario arg0) {
		scenario = arg0.getName();
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {

	}

	@Override
	public void step(Step arg0) {
		step.add(arg0.getName());
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2,
			String arg3, Integer arg4) {
	
	}

	@Override
	public void uri(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void after(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void before(Match arg0, Result arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void match(Match arg0) {
		
	}

	@Override
	public void result(Result arg0) {	
		if (driver != null) {
			try {
				BufferedImage image = DriverUtils.takeScreenshot(driver);
				String picturePath = CommonUtils.getPictureResultsPathFromConfig(this.getClass());
				File outputfile = new File(picturePath + feature + "/" +
						scenario + "/" + step.poll() + ".png");
				
				if (!outputfile.getParentFile().exists()) {
					outputfile.getParentFile().mkdirs();
				}
			    ImageIO.write(image, "png", outputfile);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public static RemoteWebDriver getDriver() {
		return driver;
	}

	public static void setDriver(RemoteWebDriver driver) {
		ZetaFormatter.driver = driver;
	}

}
