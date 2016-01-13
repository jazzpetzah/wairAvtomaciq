package com.wearezeta.auto.ios.pages;

import java.util.Random;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.DriverUtils;

public class SketchPage extends IOSPage{

	public static final String nameSketchSendButton = "SketchConfirmButton";
	@FindBy(name = nameSketchSendButton)
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
