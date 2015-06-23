package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SketchPage extends AndroidPage {
	
	private static final String idCanvas = "dcv__canvas";
	@FindBy(id = idCanvas)
	private WebElement canvas;
	

	private static final String idColorPicker = "ll__color_layout";
	@FindBy(id = idColorPicker)
	private WebElement colorPicker;
	
	public static final String[] colors = {
		"white",
		"black",
		"blue",
		"green",
		"yellow",
		"red",
		"orange",
		"pink",
		"purple"
	};
	
	private int selectedColorIndex = 0; //default to white

	public SketchPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void setColor(int colorIndex) throws Exception {
		this.selectedColorIndex = colorIndex;
		selectColorFromChooser();
	}
	
	private void selectColorFromChooser() throws Exception {
		int numColors = colors.length;
		double colorPickerWidth = colorPicker.getSize().width;
		
		double colorWidth = colorPickerWidth / numColors;
		
		System.out.println("SELECTED COLOR: " + colors[selectedColorIndex]);
		
		double colorPosition = colorWidth * selectedColorIndex + ((0.5) * colorWidth);
		double percentX = colorPosition / colorPickerWidth * 100;
		int percentY = 50;
		
		DriverUtils.tapOnPercentOfElement(this.getDriver(), colorPicker, (int) percentX, percentY);
	}

}
