package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.ios.locators.IOSLocators;


public class SketchPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.SketchPageElements.nameSketchSendButton)
	private WebElement sendSketchButton;

	public SketchPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);
		// TODO Auto-generated constructor stub
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
	
	public void sendSketch() {
		sendSketchButton.click();
	}

}
