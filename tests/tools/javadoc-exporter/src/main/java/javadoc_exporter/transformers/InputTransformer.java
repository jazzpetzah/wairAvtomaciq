package javadoc_exporter.transformers;

import java.util.ArrayList;
import java.util.List;

import javadoc_exporter.model.Step;
import javadoc_exporter.model.StepAttribute;
import javadoc_exporter.model.StepsContainer;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InputTransformer {
	private WebDriver source;
	private static final String STEP_NAME_MARKER = "Step Declaration:";
	private static final String STEP_THROWS_LIST_MARKER = "Throws:";
	private static final String STEP_PARAMS_LIST_MARKER = "Parameters:";
	private static final String NONE_VALUE = "None";


	public InputTransformer(WebDriver source) {
		this.source = source;
	}

	private static final String CONTAINER_NAME_LOCATOR = "//div[@class='header']/h2";

	private String getContainerName() {
		final String className = source.findElement(
				By.xpath(CONTAINER_NAME_LOCATOR)).getText();
		String[] words = className.split("\\s+");
		String result = words[words.length - 1];
		return result.replaceAll("([a-z])([A-Z])", "$1 $2");
	}

	private static final String CONTAINER_DESCRIPTION_LOCATOR = "//div[@class='description']//div[@class='block']";

	private String getContainerDescription() {
		try {
			return source.findElement(By.xpath(CONTAINER_DESCRIPTION_LOCATOR))
					.getText();
		} catch (NoSuchElementException e) {
			return NONE_VALUE;
		}
	}

	private static final String CONTAINER_PKGNAME_LOCATOR = "//li/ul[@class='inheritance']";

	private String getPackageName() {
		return source.findElement(By.xpath(CONTAINER_PKGNAME_LOCATOR))
				.getText();
	}

	private static final String STEPS_LOCATOR = "//dt[contains(.,'"
			+ STEP_NAME_MARKER + "')]/ancestor::li[1]";

	private List<Step> getContainerSteps() {
		List<Step> result = new ArrayList<Step>();
		List<WebElement> stepsElements = source.findElements(By
				.xpath(STEPS_LOCATOR));
		for (WebElement stepElement : stepsElements) {
			result.add(createStep(stepElement));
		}
		return result;
	}

	private static final String ATTR_LIST_ELEMENTS_LOCATOR_TEMPLATE = ".//dt[normalize-space(.)='%s']/following-sibling::*";
	private static final String ATTR_NAME_LOCATOR = ".//code";

	private List<StepAttribute> getAttributesList(WebElement stepElement,
			String listMaker) {
		List<StepAttribute> result = new ArrayList<StepAttribute>();
		final List<WebElement> attrElements = stepElement.findElements(By
				.xpath(String.format(ATTR_LIST_ELEMENTS_LOCATOR_TEMPLATE,
						listMaker)));
		for (WebElement attrElement : attrElements) {
			if (!attrElement.getTagName().toLowerCase().equals("dd")) {
				// This is to skip all the following lists starting with <dt>
				break;
			}
			final String attrName = attrElement.findElement(
					By.xpath(ATTR_NAME_LOCATOR)).getText();
			String attrValue = attrElement.getText();
			final int nameValueSeparatorLength = 3;
			if (attrValue.length() > attrName.length()
					+ nameValueSeparatorLength) {
				attrValue = attrValue.substring(attrName.length()
						+ nameValueSeparatorLength, attrValue.length());
			} else {
				attrValue = NONE_VALUE;
			}
			result.add(new StepAttribute(attrName, attrValue));
		}
		return result;
	}

	private static final String METHOD_NAME_LOCATOR = ".//h4";
	private static final String STEP_NAME_LOCATOR = ".//dt[contains(., '"
			+ STEP_NAME_MARKER + "')]/following-sibling::dd";
	private static final String STEP_DESCRIPTION_LOCATOR = ".//div[@class='block']";

	private Step createStep(final WebElement stepElement) {
		final String methodName = stepElement.findElement(
				By.xpath(METHOD_NAME_LOCATOR)).getText();
		final String name = stepElement
				.findElement(By.xpath(STEP_NAME_LOCATOR)).getText();
		String description = NONE_VALUE;
		try {
			description = stepElement.findElement(
					By.xpath(STEP_DESCRIPTION_LOCATOR)).getText();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		final List<StepAttribute> paramsList = getAttributesList(stepElement,
				STEP_PARAMS_LIST_MARKER);
		final List<StepAttribute> throwsList = getAttributesList(stepElement,
				STEP_THROWS_LIST_MARKER);
		return new Step(name, methodName, description, throwsList, paramsList);
	}

	public StepsContainer transform() {
		StepsContainer result = new StepsContainer(this.getContainerName(),
				this.getContainerDescription(), this.getPackageName(),
				this.getContainerSteps());
		return result;
	}
}
