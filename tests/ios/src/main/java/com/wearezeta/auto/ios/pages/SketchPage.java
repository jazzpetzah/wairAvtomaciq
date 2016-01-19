package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.DriverUtils;

public class SketchPage extends IOSPage{
	private static final By nameSketchSendButton = By.name("SketchConfirmButton");

	public SketchPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);
	}
	
	public void sketchRandomLines() throws Exception {
		for (int i=0; i<5; i++) {
			Random random = new Random();
			int startX = random.nextInt(100);
			int endX = random.nextInt(100);
			int endY = random.nextInt(100);
			DriverUtils.swipeByCoordinates(getDriver(), 500, startX, 50, endX, endY);
		}
	}
	
	public void sendSketch() throws Exception {
		getElement(nameSketchSendButton).click();
	}

}
