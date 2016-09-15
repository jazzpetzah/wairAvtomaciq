package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.rest.RESTError;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.List;

public class FBElement implements WebElement, FindsByFBAccessibilityId, FindsByFBClassName,
        FindsByFBPredicate, FindsByFBXPath {
    private FBDriverAPI fbDriverAPI;

    private String uuid;

    public FBElement(String uuid) {
        this.uuid = uuid;
        this.fbDriverAPI = new FBDriverAPI();
    }

    @Override
    public void click() {
        try {
            fbDriverAPI.click(this.uuid);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public void submit() {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        try {
            fbDriverAPI.setValue(this.uuid, String.join("", charSequences));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public void clear() {
        try {
            fbDriverAPI.clear(this.uuid);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public String getTagName() {
        try {
            return fbDriverAPI.getTagName(this.uuid);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public String getAttribute(String attrName) {
        try {
            return fbDriverAPI.getAttribute(this.uuid, attrName);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public boolean isSelected() {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return by.findElements(this);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public String getCssValue(String s) {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public FBElement findElementByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findChildElementByFBAccessibilityId(this.uuid, value);
        } catch (RESTError e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBAccessibilityId(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBClassName(String value) {
        try {
            return fbDriverAPI.findChildElementByFBClassName(this.uuid, value);
        } catch (RESTError e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBClassName(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBClassName(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBPredicate(String value) {
        try {
            return fbDriverAPI.findChildElementByFBPredicate(this.uuid, value);
        } catch (RESTError e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBPredicate(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBPredicate(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBXPath(String value) {
        try {
            return fbDriverAPI.findChildElementByFBXPath(this.uuid, value);
        } catch (RESTError e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBXPath(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBXPath(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }
}
