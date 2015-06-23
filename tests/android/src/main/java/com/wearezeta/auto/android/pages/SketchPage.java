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
	
	//dp * screen density. The padding value is taken from the file fragment_drawing.xml
	//I'm not sure how to get it programmatically, but it shouldn't change very often.
	//TODO calculate screen density through selendroid
	private final int colorPickerPadding = 24 * 3;
	
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
		double colorPickerElementWidth = colorPicker.getSize().width;
		//the actual select area is a bit smaller than the width of the element
		double colorPickerSelectorWidth = colorPickerElementWidth - 2 * colorPickerPadding;
		
		double colorWidth = colorPickerSelectorWidth / numColors;
		
		double colorPosition = colorWidth * selectedColorIndex + ((0.5) * colorWidth);
		double percentX = (colorPickerPadding + colorPosition) / colorPickerElementWidth * 100;
		int percentY = 50;
		
		DriverUtils.tapOnPercentOfElement(this.getDriver(), colorPicker, (int) percentX, percentY);
	}

}
